package org.example.biblioteca.domain.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes da Strategy de empréstimo (T5.2).
 * {@see design 3.1}
 * {@see tasks T2.1-T2.5, T5.2}
 */
class PoliticaEmprestimoTest {

    @Test
    @DisplayName("aluno: limite 3 e prazo 14 dias (RN01)")
    void politicaAluno() {
        PoliticaEmprestimo p = new PoliticaAluno();
        assertEquals(3, p.getLimiteLivrosSimultaneos());
        assertEquals(14, p.getPrazoRetencaoDias());
    }

    @Test
    @DisplayName("professor: limite 5 e prazo 30 dias (RN02)")
    void politicaProfessor() {
        PoliticaEmprestimo p = new PoliticaProfessor();
        assertEquals(5, p.getLimiteLivrosSimultaneos());
        assertEquals(30, p.getPrazoRetencaoDias());
    }

    @Test
    @DisplayName("funcionário: limite 3 e prazo 14 dias (RN03)")
    void politicaFuncionario() {
        PoliticaEmprestimo p = new PoliticaFuncionario();
        assertEquals(3, p.getLimiteLivrosSimultaneos());
        assertEquals(14, p.getPrazoRetencaoDias());
    }

    @Test
    @DisplayName("podeEmprestar true para usuário ativo e não bloqueado")
    void podeEmprestarUsuarioValido() {
        PoliticaEmprestimo p = new PoliticaAluno();
        Usuario u = new Usuario(1L, "Ana", "a@a.com", "1", TipoUsuario.ALUNO, true);
        assertTrue(p.podeEmprestar(u));
    }

    @Test
    @DisplayName("podeEmprestar false para nulo, inativo ou bloqueado")
    void podeEmprestarInvalido() {
        PoliticaEmprestimo p = new PoliticaAluno();
        assertFalse(p.podeEmprestar(null));

        Usuario inativo = new Usuario(2L, "Inativo", "i@i.com", "2", TipoUsuario.ALUNO, false);
        assertFalse(p.podeEmprestar(inativo));

        Usuario bloqueado = new Usuario(3L, "Bloq", "b@b.com", "3", TipoUsuario.ALUNO, true);
        bloqueado.adicionarMultaPendente(new Multa());
        assertFalse(p.podeEmprestar(bloqueado));
    }
}
