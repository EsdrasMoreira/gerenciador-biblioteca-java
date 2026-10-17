package org.example.biblioteca.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários de domínio: {@link Titulo} (T5.1).
 * {@see design 2.3}
 * {@see tasks T1.6, T5.1}
 */
class TituloTest {

    private Titulo titulo;

    @BeforeEach
    void setUp() {
        titulo = new Titulo(1L, "Dom Casmurro", "Machado de Assis", "978-85-0000000-0", true);
        titulo.adicionarExemplar(new Exemplar(1L, "T-001", titulo, StatusExemplar.DISPONIVEL, true));
        titulo.adicionarExemplar(new Exemplar(2L, "T-002", titulo, StatusExemplar.EMPRESTADO, true));
        titulo.adicionarExemplar(new Exemplar(3L, "T-003", titulo, StatusExemplar.DISPONIVEL, false));
    }

    @Test
    @DisplayName("busca o primeiro exemplar disponível e ativo do título")
    void buscarExemplarDisponivel() {
        Optional<Exemplar> found = titulo.buscarExemplarDisponivel();
        assertTrue(found.isPresent());
        assertEquals("T-001", found.get().getCodigoTombo());
        assertTrue(titulo.possuiExemplarDisponivel());
    }

    @Test
    @DisplayName("sem exemplares disponíveis retorna vazio")
    void semExemplarDisponivel() {
        Titulo sem = new Titulo(2L, "Esgotado", "Autor", "111", true);
        sem.adicionarExemplar(new Exemplar(4L, "T-004", sem, StatusExemplar.EMPRESTADO, true));
        sem.adicionarExemplar(new Exemplar(5L, "T-005", sem, StatusExemplar.DISPONIVEL, false));
        assertTrue(sem.buscarExemplarDisponivel().isEmpty());
        assertFalse(sem.possuiExemplarDisponivel());
    }

    @Test
    @DisplayName("título sem exemplares")
    void semExemplares() {
        Titulo vazio = new Titulo(3L, "Vazio", "Autor", "222", true);
        assertTrue(vazio.buscarExemplarDisponivel().isEmpty());
        assertFalse(vazio.possuiExemplarDisponivel());
    }
}
