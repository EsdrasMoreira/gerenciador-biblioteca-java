package org.example.biblioteca.domain.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes do Contexto da Strategy (T5.2).
 * {@see design 3.1}
 * {@see tasks T2.5, T5.2}
 */
class ContextoPoliticaEmprestimoTest {

    private final ContextoPoliticaEmprestimo contexto = new ContextoPoliticaEmprestimo();

    @Test
    @DisplayName("sem política definida lança IllegalStateException")
    void semPoliticaLancaExcecao() {
        assertThrows(IllegalStateException.class, contexto::prazoDias);
        assertThrows(IllegalStateException.class, contexto::limite);
        assertThrows(IllegalStateException.class, () -> contexto.valida(new Usuario()));
    }

    @Test
    @DisplayName("seleciona política do aluno: prazo 14 dias, limite 3")
    void selecionaAluno() {
        contexto.setPolitica(TipoUsuario.ALUNO);
        assertEquals(14, contexto.prazoDias());
        assertEquals(3, contexto.limite());

        Usuario valido = new Usuario(1L, "A", "a@a.com", "1", TipoUsuario.ALUNO, true);
        assertTrue(contexto.valida(valido));
    }

    @Test
    @DisplayName("seleciona política do professor: prazo 30 dias, limite 5")
    void selecionaProfessor() {
        contexto.setPolitica(TipoUsuario.PROFESSOR);
        assertEquals(30, contexto.prazoDias());
        assertEquals(5, contexto.limite());
    }

    @Test
    @DisplayName("seleciona política do funcionário: prazo 14 dias, limite 3")
    void selecionaFuncionario() {
        contexto.setPolitica(TipoUsuario.FUNCIONARIO);
        assertEquals(14, contexto.prazoDias());
        assertEquals(3, contexto.limite());
    }

    @Test
    @DisplayName("valida false para usuário inativo ou bloqueado")
    void validaBloqueado() {
        contexto.setPolitica(TipoUsuario.ALUNO);

        Usuario inativo = new Usuario(2L, "I", "i@i.com", "2", TipoUsuario.ALUNO, false);
        assertFalse(contexto.valida(inativo));

        Usuario bloqueado = new Usuario(3L, "B", "b@b.com", "3", TipoUsuario.ALUNO, true);
        bloqueado.adicionarMultaPendente(new Multa());
        assertFalse(contexto.valida(bloqueado));
    }
}
