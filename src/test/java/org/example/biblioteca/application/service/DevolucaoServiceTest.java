package org.example.biblioteca.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.enums.StatusMulta;
import org.example.biblioteca.domain.enums.StatusReserva;
import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.exception.EmprestimoNaoEncontradoException;
import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.infrastructure.persistence.*;
import org.example.biblioteca.domain.model.Devolucao;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Reserva;
import org.example.biblioteca.domain.model.Titulo;
import org.example.biblioteca.domain.model.Usuario;
import org.example.biblioteca.domain.service.DevolucaoService;
import org.example.biblioteca.domain.service.EmprestimoService;
import org.example.biblioteca.application.service.EmprestimoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes de {@link DevolucaoService} e multas (T5.6).
 * {@see design 4.2}
 * {@see tasks T3.5, T3.6, T5.6}
 */
class DevolucaoServiceTest {

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
        emprestimoService = new EmprestimoServiceImpl(usuarioRepository, tituloRepository, exemplarRepository,
                emprestimoRepository, reservaRepository, multaRepository, new org.example.biblioteca.domain.strategy.ContextoPoliticaEmprestimo());
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

    private Emprestimo emprestar(Usuario usuario, Exemplar exemplar) throws Exception {
        return emprestimoService.emprestar(usuario.getId(), exemplar.getId(), "operador");
    }

    private void atrasar(Emprestimo emprestimo, long dias) {
        emprestimo.setDataPrevistaDevolucao(LocalDateTime.now().minusDays(dias));
        emprestimoRepository.salvar(emprestimo);
    }

    @Test
    @DisplayName("devolução pontual: sem multa, exemplar volta a DISPONIVEL, empréstimo encerrado")
    void devolucaoPontualSemMulta() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D001", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D01");
        Exemplar exemplar = salvarExemplar("D-001", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);

        Devolucao devolucao = devolucaoService.devolver(emprestimo.getId(), "operador", false, false);

        assertNotNull(devolucao.getDataDevolucao());
        assertFalse(devolucao.isExemplarDanificado());
        assertFalse(devolucao.isExemplarPerdido());
        assertNull(devolucao.getMultaGerada());
        assertFalse(emprestimo.isAtivo());
        assertNotNull(emprestimo.getDataRealDevolucao());
        assertEquals(StatusExemplar.DISPONIVEL, exemplar.getStatus());
        assertTrue(exemplar.isAtivo());
        assertTrue(exemplar.estaDisponivel());
        assertEquals(0, usuario.quantidadeEmprestimosAtivos());
    }

    @Test
    @DisplayName("atraso de 3 dias gera multa de R$ 6,00 (R$ 2,00/dia) e bloqueia o usuário (RF05, RF06)")
    void atraso3DiasMulta6Reais() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D002", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D02");
        Exemplar exemplar = salvarExemplar("D-002", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);
        atrasar(emprestimo, 3);

        Devolucao devolucao = devolucaoService.devolver(emprestimo.getId(), "operador", false, false);

        assertNotNull(devolucao.getMultaGerada());
        assertEquals(StatusMulta.PENDENTE, devolucao.getMultaGerada().getStatus());
        assertEquals(0, devolucao.getMultaGerada().getValor().compareTo(BigDecimal.valueOf(6)));
        assertTrue(usuario.estaBloqueado());
        assertEquals(1, multaRepository.listarPendentesPorUsuario(usuario.getId()).size());
        // exemplar volta ao acervo disponível
        assertEquals(StatusExemplar.DISPONIVEL, exemplar.getStatus());
        assertTrue(exemplar.isAtivo());
    }

    @Test
    @DisplayName("calcularMultaAtraso consulta sem efetivar a devolução (T3.6)")
    void calcularMultaAtrasoSemEfetivarDevolucao() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D003", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D03");
        Exemplar exemplar = salvarExemplar("D-003", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);
        atrasar(emprestimo, 3);

        BigDecimal multa = devolucaoService.calcularMultaAtraso(emprestimo.getId());
        assertEquals(0, multa.compareTo(BigDecimal.valueOf(6)));

        // sem efeitos colaterais: empréstimo continua ativo, sem multa gerada
        assertTrue(emprestimoRepository.buscarPorId(emprestimo.getId()).orElseThrow().isAtivo());
        assertNull(emprestimo.getMulta());
    }

    @Test
    @DisplayName("atraso genérico: R$ 2,00 por dia de atraso (ex: 7 dias = R$ 14,00)")
    void atrasoGenericoDoisReaisPorDia() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D004", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D04");
        Exemplar exemplar = salvarExemplar("D-004", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);
        atrasar(emprestimo, 7);

        Devolucao devolucao = devolucaoService.devolver(emprestimo.getId(), "operador", false, false);

        assertNotNull(devolucao.getMultaGerada());
        assertEquals(0, devolucao.getMultaGerada().getValor().compareTo(BigDecimal.valueOf(14)));
    }

    @Test
    @DisplayName("exemplar danificado: taxa de reposição R$ 50,00, baixa e não volta ao acervo (RF07, RF08)")
    void exemplarDanificadoTaxaReposicaoEBaixa() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D005", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D05");
        Exemplar exemplar = salvarExemplar("D-005", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);

        Devolucao devolucao = devolucaoService.devolver(emprestimo.getId(), "operador", true, false);

        assertTrue(devolucao.isExemplarDanificado());
        assertNotNull(devolucao.getMultaGerada());
        assertEquals(StatusMulta.PENDENTE, devolucao.getMultaGerada().getStatus());
        assertEquals(0, devolucao.getMultaGerada().getValor().compareTo(BigDecimal.valueOf(50)));
        assertEquals(StatusExemplar.DANIFICADO, exemplar.getStatus());
        assertFalse(exemplar.isAtivo());
        assertFalse(exemplar.estaDisponivel()); // não volta ao acervo
        assertFalse(emprestimo.isAtivo());
        assertTrue(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("exemplar perdido: taxa de reposição R$ 50,00, baixa e não volta ao acervo (RF07, RF08)")
    void exemplarPerdidoTaxaReposicaoEBaixa() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D006", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D06");
        Exemplar exemplar = salvarExemplar("D-006", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);

        Devolucao devolucao = devolucaoService.devolver(emprestimo.getId(), "operador", false, true);

        assertTrue(devolucao.isExemplarPerdido());
        assertNotNull(devolucao.getMultaGerada());
        assertEquals(0, devolucao.getMultaGerada().getValor().compareTo(BigDecimal.valueOf(50)));
        assertEquals(StatusExemplar.PERDIDO, exemplar.getStatus());
        assertFalse(exemplar.isAtivo());
        assertFalse(exemplar.estaDisponivel());
        assertTrue(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("notifica o primeiro da fila de reserva com expiração em 3 dias corridos (CU03)")
    void notificaPrimeiraReservaAposDevolucao() throws Exception {
        Usuario primeiro = salvarUsuario("Primeiro", "N001", TipoUsuario.ALUNO);
        Usuario segundo = salvarUsuario("Segundo", "N002", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Reservado", "ISBN-N01");
        Exemplar exemplar = salvarExemplar("N-001", titulo);

        // primeiro empresta o único exemplar
        Emprestimo emprestimo = emprestar(primeiro, exemplar);
        // segundo reserva (fila AGUARDANDO)
        Reserva reservaSegundo = emprestimoService.reservar(segundo.getId(), titulo.getId());
        assertEquals(StatusReserva.AGUARDANDO, reservaSegundo.getStatus());

        // devolução disponibiliza o exemplar e notifica o primeiro da fila
        devolucaoService.devolver(emprestimo.getId(), "operador", false, false);

        Reserva notificada = reservaRepository.buscarPorId(reservaSegundo.getId()).orElseThrow();
        assertEquals(StatusReserva.NOTIFICADA, notificada.getStatus());
        assertNotNull(notificada.getDataNotificacao());
        LocalDateTime agora = LocalDateTime.now();
        assertTrue(notificada.getDataExpiracao().isAfter(agora.plusDays(3).minusMinutes(1)));
        assertTrue(notificada.getDataExpiracao().isBefore(agora.plusDays(3).plusMinutes(1)));
    }

    @Test
    @DisplayName("devolução de empréstimo inexistente lança exceção")
    void devolucaoEmprestimoNaoEncontrado() {
        assertThrows(EmprestimoNaoEncontradoException.class,
                () -> devolucaoService.devolver(999L, "operador", false, false));
    }

    @Test
    @DisplayName("devolução de empréstimo já devolvido não é permitida")
    void devolucaoEmprestimoJaDevolvido() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D007", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D07");
        Exemplar exemplar = salvarExemplar("D-007", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);

        devolucaoService.devolver(emprestimo.getId(), "operador", false, false);
        assertThrows(OperacaoNaoPermitidaException.class,
                () -> devolucaoService.devolver(emprestimo.getId(), "operador", false, false));
    }

    @Test
    @DisplayName("multa de atraso deixa usuário bloqueado até quitação (T3.8, RF06)")
    void usuarioBloqueadoAteQuitacaoMulta() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "D008", TipoUsuario.ALUNO);
        Titulo titulo = salvarTitulo("Obra", "ISBN-D08");
        Exemplar exemplar = salvarExemplar("D-008", titulo);
        Emprestimo emprestimo = emprestar(usuario, exemplar);
        atrasar(emprestimo, 3);

        devolucaoService.devolver(emprestimo.getId(), "operador", false, false);
        assertTrue(usuario.estaBloqueado());

        // quitação: multa paga/removida
        List<Multa> pendentes = multaRepository.listarPendentesPorUsuario(usuario.getId());
        assertEquals(1, pendentes.size());
        usuario.removerMultaPendente(pendentes.get(0));
        usuarioRepository.salvar(usuario);
        assertFalse(usuario.estaBloqueado());
    }
}
