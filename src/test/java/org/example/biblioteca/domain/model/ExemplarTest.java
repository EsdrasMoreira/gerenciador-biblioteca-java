package org.example.biblioteca.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários de domínio: {@link Exemplar} (T5.1).
 * {@see design 2.4}
 * {@see tasks T1.7, T5.1}
 */
class ExemplarTest {

    private Titulo titulo;
    private Exemplar exemplar;

    @BeforeEach
    void setUp() {
        titulo = new Titulo(1L, "Dom Casmurro", "Machado de Assis", "978-85-0000000-0", true);
        exemplar = new Exemplar(1L, "TOMBO-001", titulo, StatusExemplar.DISPONIVEL, true);
    }

    @Test
    @DisplayName("exemplar disponível apenas quando status DISPONIVEL e ativo (RN04)")
    void estaDisponivelApenasDisponivelEAtivo() {
        assertTrue(exemplar.estaDisponivel());

        exemplar.setAtivo(false);
        assertFalse(exemplar.estaDisponivel());

        exemplar.setAtivo(true);
        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        assertFalse(exemplar.estaDisponivel());
    }

    @Test
    @DisplayName("indisponível por estado: EM_REPARO, DANIFICADO, PERDIDO")
    void estaIndisponivelPorEstado() {
        assertFalse(exemplar.estaIndisponivelPorEstado());

        exemplar.setStatus(StatusExemplar.EM_REPARO);
        assertTrue(exemplar.estaIndisponivelPorEstado());

        exemplar.setStatus(StatusExemplar.DANIFICADO);
        assertTrue(exemplar.estaIndisponivelPorEstado());

        exemplar.setStatus(StatusExemplar.PERDIDO);
        assertTrue(exemplar.estaIndisponivelPorEstado());

        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        assertFalse(exemplar.estaIndisponivelPorEstado());

        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        assertFalse(exemplar.estaIndisponivelPorEstado());
    }
}
