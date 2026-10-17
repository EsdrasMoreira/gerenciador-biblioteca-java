package org.example.biblioteca.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.enums.StatusMulta;
import org.example.biblioteca.domain.enums.StatusReserva;
import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.exception.LimiteEmprestimoAtingidoException;
import org.example.biblioteca.domain.exception.UsuarioBloqueadoException;
import org.example.biblioteca.domain.infrastructure.persistence.*;
import org.example.biblioteca.domain.model.Devolucao;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Reserva;
import org.example.biblioteca.domain.model.Titulo;
import org.example.biblioteca.domain.model.Usuario;
import org.example.biblioteca.domain.service.DevolucaoService;
import org.example.biblioteca.domain.service.EmprestimoService;
import org.example.biblioteca.application.service.EmprestimoServiceImpl;
import org.example.biblioteca.application.service.DevolucaoServiceImpl;
import org.example.biblioteca.domain.strategy.ContextoPoliticaEmprestimo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Teste de integração / fluxo ponta a ponta (T5.7).
 * {@see design 4.1, design 4.2}
 * {@see tasks T3.4, T3.5, T3.7, T3.8, T5.7}
 */
class FluxoPontaAPontaTest {

    private UsuarioRepository usuarioRepository;
    private TituloRepository tituloRepository;
    private ExemplarRepository exemplarRepository;
    private EmprestimoRepository emprestimoRepository;
    private ReservaRepository reservaRepository;
    private MultaRepository multaRepository;
    private EmprestimoService emprestimoService;
    private DevolucaoService devolucaoService;

    @BeforeEach
    void setUp() throws Exception {
        usuarioRepository = new UsuarioRepositoryImpl();
        tituloRepository = new TituloRepositoryImpl();
        exemplarRepository = new ExemplarRepositoryImpl();
        emprestimoRepository = new EmprestimoRepositoryImpl();
        reservaRepository = new ReservaRepositoryImpl();
        multaRepository = new MultaRepositoryImpl();
        ContextoPoliticaEmprestimo contexto = new ContextoPoliticaEmprestimo();
        emprestimoService = new EmprestimoServiceImpl(usuarioRepository, tituloRepository, exemplarRepository,
                emprestimoRepository, reservaRepository, multaRepository, contexto);
        devolucaoService = new DevolucaoServiceImpl(usuarioRepository, tituloRepository, exemplarRepository,
                emprestimoRepository, reservaRepository, multaRepository);
    }

    private Usuario salvarUsuario(String nome, String matricula, TipoUsuario tipo) {
        Usuario u = new Usuario(null, nome, nome.toLowerCase() + "@email.com", matricula, tipo, true);
        return usuarioRepository.salvar(u);
    }

    private Titulo salvarTitulo(String nome, String isbn) {
        return tituloRepository.salvar(new Titulo(null, nome, "Autor", isbn, true));
    }

    private Exemplar salvarExemplar(String tombo, Titulo titulo) {
        Exemplar e = new Exemplar(null, tombo, titulo, StatusExemplar.DISPONIVEL, true);
        return exemplarRepository.salvar(e);
    }

    @Test
    @DisplayName("empréstimo → devolução com atraso → usuário bloqueado até quitação; reserva notificada e emprestada pelo próximo da fila")
    void fluxoCompletoComAtrasoEReserva() throws Exception {
        Usuario aluno = salvarUsuario("Ana", "F001", TipoUsuario.ALUNO);
        Usuario prox = salvarUsuario("Bruno", "F002", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra Única", "ISBN-FLUXO");
        Exemplar exemplar = salvarExemplar("F-001", titulo);

        // 1. aluno empresta o único exemplar disponível
        Emprestimo emprestimo = emprestimoService.emprestar(aluno.getId(), exemplar.getId(), "bibliotecario");
        assertEquals(StatusExemplar.EMPRESTADO, exemplar.getStatus());

        // 2. próximo da fila reserva o título (sem exemplares disponíveis)
        Reserva reserva = emprestimoService.reservar(prox.getId(), titulo.getId());
        assertEquals(StatusReserva.AGUARDANDO, reserva.getStatus());

        // 3. aluno devolve com 3 dias de atraso → multa R$ 6,00 e fica bloqueado
        emprestimo.setDataPrevistaDevolucao(LocalDateTime.now().minusDays(3));
        emprestimoRepository.salvar(emprestimo);
        Devolucao devolucao = devolucaoService.devolver(emprestimo.getId(), "bibliotecario", false, false);

        assertNotNull(devolucao.getMultaGerada());
        assertEquals(StatusMulta.PENDENTE, devolucao.getMultaGerada().getStatus());
        assertEquals(0, devolucao.getMultaGerada().getValor().compareTo(BigDecimal.valueOf(6)));
        assertTrue(aluno.estaBloqueado());
        assertEquals(StatusExemplar.DISPONIVEL, exemplar.getStatus());
        assertTrue(exemplar.isAtivo());

        // reserva do primeiro da fila é notificada com expiração de 3 dias
        Reserva notificada = reservaRepository.buscarPorId(reserva.getId()).orElseThrow();
        assertEquals(StatusReserva.NOTIFICADA, notificada.getStatus());
        assertNotNull(notificada.getDataExpiracao());

        // 4. aluno bloqueado não consegue novo empréstimo até quitação da multa
        Exemplar outro = salvarExemplar("F-002", titulo);
        assertThrows(UsuarioBloqueadoException.class,
                () -> emprestimoService.emprestar(aluno.getId(), outro.getId(), "bibliotecario"));

        // 5. quitação da multa desbloqueia o usuário
        List<Multa> pendentes = multaRepository.listarPendentesPorUsuario(aluno.getId());
        assertEquals(1, pendentes.size());
        aluno.removerMultaPendente(pendentes.get(0));
        usuarioRepository.salvar(aluno);
        assertFalse(aluno.estaBloqueado());

        // 6. próximo da reserva consegue emprestar o exemplar que voltou ao acervo
        Emprestimo emprestimoProx = emprestimoService.emprestar(prox.getId(), exemplar.getId(), "bibliotecario");
        assertNotNull(emprestimoProx.getId());
        assertEquals(StatusExemplar.EMPRESTADO, exemplar.getStatus());

        // 7. novo empréstimo do aluno (desbloqueado) respeita limite do tipo (aluno = 3)
        for (int i = 0; i < 3; i++) {
            emprestimoService.emprestar(aluno.getId(), salvarExemplar("F-00" + (3 + i), titulo).getId(), "bibliotecario");
        }
        assertEquals(3, aluno.quantidadeEmprestimosAtivos());
        assertThrows(LimiteEmprestimoAtingidoException.class,
                () -> emprestimoService.emprestar(aluno.getId(), salvarExemplar("F-006", titulo).getId(), "bibliotecario"));
    }
}
