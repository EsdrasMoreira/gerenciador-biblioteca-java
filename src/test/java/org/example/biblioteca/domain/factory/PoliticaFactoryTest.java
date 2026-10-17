package org.example.biblioteca.domain.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.strategy.PoliticaAluno;
import org.example.biblioteca.domain.strategy.PoliticaEmprestimo;
import org.example.biblioteca.domain.strategy.PoliticaFuncionario;
import org.example.biblioteca.domain.strategy.PoliticaProfessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes da Factory de estratégias (T5.4).
 * {@see design 3.3}
 * {@see tasks T2.6, T5.4}
 */
class PoliticaFactoryTest {

    @Test
    @DisplayName("criar(ALUNO) retorna PoliticaAluno")
    void criaPoliticaAluno() {
        PoliticaEmprestimo p = PoliticaFactory.criar(TipoUsuario.ALUNO);
        assertInstanceOf(PoliticaAluno.class, p);
        assertEquals(3, p.getLimiteLivrosSimultaneos());
        assertEquals(14, p.getPrazoRetencaoDias());
    }

    @Test
    @DisplayName("criar(PROFESSOR) retorna PoliticaProfessor")
    void criaPoliticaProfessor() {
        PoliticaEmprestimo p = PoliticaFactory.criar(TipoUsuario.PROFESSOR);
        assertInstanceOf(PoliticaProfessor.class, p);
        assertEquals(5, p.getLimiteLivrosSimultaneos());
        assertEquals(30, p.getPrazoRetencaoDias());
    }

    @Test
    @DisplayName("criar(FUNCIONARIO) retorna PoliticaFuncionario")
    void criaPoliticaFuncionario() {
        PoliticaEmprestimo p = PoliticaFactory.criar(TipoUsuario.FUNCIONARIO);
        assertInstanceOf(PoliticaFuncionario.class, p);
        assertEquals(3, p.getLimiteLivrosSimultaneos());
        assertEquals(14, p.getPrazoRetencaoDias());
    }
}
