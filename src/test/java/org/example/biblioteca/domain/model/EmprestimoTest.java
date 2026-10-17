package org.example.biblioteca.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários de domínio: {@link Emprestimo} (T5.1).
 * {@see design 2.5}
 * {@see tasks T1.8, T5.1}
 */
class EmprestimoTest {

    @Test
    @DisplayName("empréstimo em dia: estaAtrasado falso e diasAtraso zero")
    void naoAtrasado() {
        Emprestimo e = new Emprestimo();
        e.setId(1L);
        e.setAtivo(true);
        e.setDataPrevistaDevolucao(LocalDateTime.now().plusDays(5));
        assertFalse(e.estaAtrasado());
        assertEquals(0, e.diasAtraso());
    }

    @Test
    @DisplayName("empréstimo já devolvido não está atrasado mesmo com data prevista passada")
    void devolvidoNaoAtrasado() {
        Emprestimo e = new Emprestimo();
        e.setId(1L);
        e.setAtivo(false);
        e.setDataPrevistaDevolucao(LocalDateTime.now().minusDays(10));
        assertFalse(e.estaAtrasado());
        assertEquals(0, e.diasAtraso());
    }

    @Test
    @DisplayName("empréstimo atrasado: estaAtrasado verdadeiro e diasAtraso com diferença em dias")
    void atrasado() {
        Emprestimo e = new Emprestimo();
        e.setId(1L);
        e.setAtivo(true);
        e.setDataPrevistaDevolucao(LocalDateTime.now().minusDays(3));
        assertTrue(e.estaAtrasado());
        assertEquals(3, e.diasAtraso());
    }

    @Test
    @DisplayName("empréstimo sem data prevista não está atrasado")
    void semDataPrevistaNaoAtrasado() {
        Emprestimo e = new Emprestimo();
        e.setId(1L);
        e.setAtivo(true);
        e.setDataPrevistaDevolucao(null);
        assertFalse(e.estaAtrasado());
        assertEquals(0, e.diasAtraso());
    }
}
