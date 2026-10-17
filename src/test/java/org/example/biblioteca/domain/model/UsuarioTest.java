package org.example.biblioteca.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.example.biblioteca.domain.enums.StatusMulta;
import org.example.biblioteca.domain.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários de domínio: {@link Usuario} (T5.1).
 * {@see design 2.2}
 * {@see tasks T1.5, T5.1}
 */
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "Ana Silva", "ana@email.com", "20230001", TipoUsuario.ALUNO, true);
    }

    @Test
    @DisplayName("usuário ativo, sem multas e sem empréstimos atrasados não está bloqueado")
    void usuarioAtivoNaoEstaBloqueado() {
        assertFalse(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("usuário inativo está bloqueado (RF10)")
    void usuarioInativoEstaBloqueado() {
        usuario.setAtivo(false);
        assertTrue(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("usuário com multa pendente está bloqueado (RN06)")
    void usuarioComMultaPendenteEstaBloqueado() {
        Multa multa = new Multa();
        multa.setId(1L);
        multa.setValor(BigDecimal.valueOf(6.00));
        multa.setStatus(StatusMulta.PENDENTE);
        usuario.adicionarMultaPendente(multa);
        assertTrue(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("usuário com empréstimo atrasado está bloqueado (RF06, RN06)")
    void usuarioComEmprestimoAtrasadoEstaBloqueado() {
        Emprestimo atrasado = new Emprestimo();
        atrasado.setId(10L);
        atrasado.setAtivo(true);
        atrasado.setDataPrevistaDevolucao(LocalDateTime.now().minusDays(2));
        usuario.adicionarEmprestimoAtivo(atrasado);
        assertTrue(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("empréstimo em dia não bloqueia o usuário")
    void emprestimoEmDiaNaoBloqueia() {
        Emprestimo emDia = new Emprestimo();
        emDia.setId(10L);
        emDia.setAtivo(true);
        emDia.setDataPrevistaDevolucao(LocalDateTime.now().plusDays(5));
        usuario.adicionarEmprestimoAtivo(emDia);
        assertFalse(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("empréstimo já devolvido não bloqueia o usuário mesmo com data prevista passada")
    void emprestimoDevolvidoNaoBloqueia() {
        Emprestimo devolvido = new Emprestimo();
        devolvido.setId(10L);
        devolvido.setAtivo(false);
        devolvido.setDataPrevistaDevolucao(LocalDateTime.now().minusDays(10));
        usuario.adicionarEmprestimoAtivo(devolvido);
        assertFalse(usuario.estaBloqueado());
    }

    @Test
    @DisplayName("quantidade de empréstimos ativos")
    void quantidadeEmprestimosAtivos() {
        assertEquals(0, usuario.quantidadeEmprestimosAtivos());

        Emprestimo e1 = new Emprestimo();
        e1.setId(1L);
        e1.setAtivo(true);
        Emprestimo e2 = new Emprestimo();
        e2.setId(2L);
        e2.setAtivo(true);

        usuario.adicionarEmprestimoAtivo(e1);
        usuario.adicionarEmprestimoAtivo(e2);
        assertEquals(2, usuario.quantidadeEmprestimosAtivos());

        usuario.removerEmprestimoAtivo(e1);
        assertEquals(1, usuario.quantidadeEmprestimosAtivos());

        usuario.removerEmprestimoAtivo(e2);
        assertEquals(0, usuario.quantidadeEmprestimosAtivos());
    }
}
