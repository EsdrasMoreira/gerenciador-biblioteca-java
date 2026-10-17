package org.example.biblioteca.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.enums.StatusMulta;
import org.example.biblioteca.domain.enums.StatusReserva;
import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.exception.*;
import org.example.biblioteca.domain.infrastructure.persistence.*;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Reserva;
import org.example.biblioteca.domain.model.Titulo;
import org.example.biblioteca.domain.model.Usuario;
import org.example.biblioteca.domain.service.EmprestimoService;
import org.example.biblioteca.domain.strategy.ContextoPoliticaEmprestimo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes de {@link EmprestimoService} e reserva (T5.5).
 * {@see design 4.1}
 * {@see tasks T3.4, T3.7, T3.8, T5.5}
 */
class EmprestimoServiceTest {

    private UsuarioRepository usuarioRepository;
    private TituloRepository tituloRepository;
    private ExemplarRepository exemplarRepository;
    private EmprestimoRepository emprestimoRepository;
    private ReservaRepository reservaRepository;
    private MultaRepository multaRepository;
    private ContextoPoliticaEmprestimo contexto;
    private EmprestimoService service;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryImpl();
        tituloRepository = new TituloRepositoryImpl();
        exemplarRepository = new ExemplarRepositoryImpl();
        emprestimoRepository = new EmprestimoRepositoryImpl();
        reservaRepository = new ReservaRepositoryImpl();
        multaRepository = new MultaRepositoryImpl();
        contexto = new ContextoPoliticaEmprestimo();
        service = new EmprestimoServiceImpl(usuarioRepository, tituloRepository, exemplarRepository,
                emprestimoRepository, reservaRepository, multaRepository, contexto);
    }

    private Usuario salvarUsuario(String nome, String matricula, TipoUsuario tipo, boolean ativo) {
        Usuario u = new Usuario(null, nome, nome.toLowerCase() + "@email.com", matricula, tipo, ativo);
        return usuarioRepository.salvar(u);
    }

    private Titulo salvarTitulo(String nome, String isbn) {
        return tituloRepository.salvar(new Titulo(null, nome, "Autor", isbn, true));
    }

    private Exemplar salvarExemplar(String tombo, Titulo titulo, StatusExemplar status) {
        Exemplar e = new Exemplar(null, tombo, titulo, status, true);
        return exemplarRepository.salvar(e);
    }

    private Exemplar salvarExemplarDisponivel(String tombo, Titulo titulo) {
        return salvarExemplar(tombo, titulo, StatusExemplar.DISPONIVEL);
    }

    @Test
    @DisplayName("empréstimo válido de aluno: exemplar EMPRESTADO e data prevista em 14 dias (RF01, RF02, RN01)")
    void emprestimoValidoAlunoDataPrevista14Dias() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "M001", TipoUsuario.ALUNO, true);
        Titulo titulo = salvarTitulo("Dom Casmurro", "ISBN-001");
        Exemplar exemplar = salvarExemplarDisponivel("T-001", titulo);

        Emprestimo emprestimo = service.emprestar(usuario.getId(), exemplar.getId(), "bibliotecario");

        assertNotNull(emprestimo.getId());
        assertTrue(emprestimo.isAtivo());
        assertEquals(usuario.getId(), emprestimo.getUsuario().getId());
        assertEquals("bibliotecario", emprestimo.getOperador());
        assertEquals(StatusExemplar.EMPRESTADO, exemplar.getStatus());
        LocalDateTime agora = LocalDateTime.now();
        assertTrue(emprestimo.getDataPrevistaDevolucao().isAfter(agora.plusDays(13)));
        assertTrue(emprestimo.getDataPrevistaDevolucao().isBefore(agora.plusDays(15)));
        assertEquals(1, usuario.quantidadeEmprestimosAtivos());
        assertTrue(usuario.getEmprestimosAtivos().contains(emprestimo));
    }

    @Test
    @DisplayName("empréstimo válido de professor: data prevista em 30 dias (RF02, RN02)")
    void emprestimoValidoProfessorDataPrevista30Dias() throws Exception {
        Usuario usuario = salvarUsuario("Prof", "M002", TipoUsuario.PROFESSOR, true);
        Titulo titulo = salvarTitulo("Obra", "ISBN-002");
        Exemplar exemplar = salvarExemplarDisponivel("T-002", titulo);

        Emprestimo emprestimo = service.emprestar(usuario.getId(), exemplar.getId(), "autoatendimento");

        assertTrue(emprestimo.isAtivo());
        LocalDateTime agora = LocalDateTime.now();
        assertTrue(emprestimo.getDataPrevistaDevolucao().isAfter(agora.plusDays(29)));
        assertTrue(emprestimo.getDataPrevistaDevolucao().isBefore(agora.plusDays(31)));
    }

    @Test
    @DisplayName("empréstimo negado para usuário inativo (RF01, RF10)")
    void emprestimoNegadoUsuarioInativo() throws Exception {
        Usuario usuario = salvarUsuario("Inativo", "M003", TipoUsuario.ALUNO, false);
        Titulo titulo = salvarTitulo("Obra", "ISBN-003");
        Exemplar exemplar = salvarExemplarDisponivel("T-003", titulo);

        assertThrows(UsuarioBloqueadoException.class,
                () -> service.emprestar(usuario.getId(), exemplar.getId(), "op"));
    }

    @Test
    @DisplayName("empréstimo negado para usuário com multa pendente (RF06, RN06)")
    void emprestimoNegadoUsuarioComMultaPendente() throws Exception {
        Usuario usuario = salvarUsuario("Devendo", "M004", TipoUsuario.ALUNO, true);
        Multa multa = new Multa();
        multa.setValor(BigDecimal.valueOf(6));
        multa.setStatus(StatusMulta.PENDENTE);
        usuario.adicionarMultaPendente(multa);
        usuarioRepository.salvar(usuario);
        Titulo titulo = salvarTitulo("Obra", "ISBN-004");
        Exemplar exemplar = salvarExemplarDisponivel("T-004", titulo);

        assertThrows(UsuarioBloqueadoException.class,
                () -> service.emprestar(usuario.getId(), exemplar.getId(), "op"));
    }

    @Test
    @DisplayName("empréstimo negado quando limite de simultâneos atingido (aluno = 3) (RN01)")
    void emprestimoNegadoLimiteAtingidoAluno() throws Exception {
        Usuario usuario = salvarUsuario("Leitor", "M005", TipoUsuario.ALUNO, true);
        Titulo titulo = salvarTitulo("Série", "ISBN-005");
        Exemplar e1 = salvarExemplarDisponivel("S-001", titulo);
        Exemplar e2 = salvarExemplarDisponivel("S-002", titulo);
        Exemplar e3 = salvarExemplarDisponivel("S-003", titulo);

        service.emprestar(usuario.getId(), e1.getId(), "op");
        service.emprestar(usuario.getId(), e2.getId(), "op");
        service.emprestar(usuario.getId(), e3.getId(), "op");
        assertEquals(3, usuario.quantidadeEmprestimosAtivos());

        Exemplar e4 = salvarExemplarDisponivel("S-004", titulo);
        assertThrows(LimiteEmprestimoAtingidoException.class,
                () -> service.emprestar(usuario.getId(), e4.getId(), "op"));
    }

    @Test
    @DisplayName("empréstimo negado quando exemplar não disponível, com opção de reserva (RF03, RN05)")
    void emprestimoNegadoSemExemplarDisponivelComReserva() throws Exception {
        Usuario usuario = salvarUsuario("Ana", "M006", TipoUsuario.ALUNO, true);
        Titulo titulo = salvarTitulo("Esgotado", "ISBN-006");
        Exemplar emprestado = salvarExemplar("E-001", titulo, StatusExemplar.EMPRESTADO);

        assertThrows(ExemplarIndisponivelException.class,
                () -> service.emprestar(usuario.getId(), emprestado.getId(), "op"));

        // opção de reserva: fila do título
        Reserva reserva = service.reservar(usuario.getId(), titulo.getId());
        assertNotNull(reserva.getId());
        assertEquals(StatusReserva.AGUARDANDO, reserva.getStatus());
        assertEquals(usuario.getId(), reserva.getUsuario().getId());
        assertEquals(titulo.getId(), reserva.getTitulo().getId());
    }

    @Test
    @DisplayName("reserva insere usuário na fila do título por ordem de criação (RN05)")
    void reservarFilaPorOrdemDeCriacao() throws Exception {
        Usuario u1 = salvarUsuario("U1", "R001", TipoUsuario.ALUNO, true);
        Usuario u2 = salvarUsuario("U2", "R002", TipoUsuario.ALUNO, true);
        Titulo titulo = salvarTitulo("Fila", "ISBN-007");

        Reserva r1 = service.reservar(u1.getId(), titulo.getId());
        Reserva r2 = service.reservar(u2.getId(), titulo.getId());

        List<Reserva> fila = reservaRepository.listarPorTitulo(titulo.getId());
        assertEquals(2, fila.size());
        assertEquals(r1.getId(), fila.get(0).getId());
        assertEquals(r2.getId(), fila.get(1).getId());
    }

    @Test
    @DisplayName("existeExemplarDisponível reflete disponibilidade do título")
    void existeExemplarDisponivel() throws Exception {
        Titulo titulo = salvarTitulo("Disp", "ISBN-008");
        assertFalse(service.existeExemplarDisponivel(titulo.getId()));

        salvarExemplarDisponivel("D-001", titulo);
        assertTrue(service.existeExemplarDisponivel(titulo.getId()));
    }

    @Test
    @DisplayName("listarEmprestimosAtivos lista apenas empréstimos não devolvidos do usuário")
    void listarEmprestimosAtivos() throws Exception {
        Usuario usuario = salvarUsuario("L", "L001", TipoUsuario.ALUNO, true);
        Titulo titulo = salvarTitulo("Livro", "ISBN-009");
        Exemplar exemplar = salvarExemplarDisponivel("L-001", titulo);

        service.emprestar(usuario.getId(), exemplar.getId(), "op");

        List<Emprestimo> ativos = service.listarEmprestimosAtivos(usuario.getId());
        assertEquals(1, ativos.size());
        assertEquals(exemplar.getId(), ativos.get(0).getExemplar().getId());
    }
}
