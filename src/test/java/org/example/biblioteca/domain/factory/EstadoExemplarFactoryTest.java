package org.example.biblioteca.domain.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.state.EstadoDanificado;
import org.example.biblioteca.domain.state.EstadoDisponivel;
import org.example.biblioteca.domain.state.EstadoEmprestado;
import org.example.biblioteca.domain.state.EstadoEmReparo;
import org.example.biblioteca.domain.state.EstadoExemplar;
import org.example.biblioteca.domain.state.EstadoPerdido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes da Factory de estados do exemplar (T5.4).
 * {@see design 3.3}
 * {@see tasks T2.9, T5.4}
 */
class EstadoExemplarFactoryTest {

    @Test
    @DisplayName("mapeia cada StatusExemplar para seu estado concreto (State)")
    void criaEstados() {
        assertInstanceOf(EstadoDisponivel.class, EstadoExemplarFactory.criar(StatusExemplar.DISPONIVEL));
        assertInstanceOf(EstadoEmprestado.class, EstadoExemplarFactory.criar(StatusExemplar.EMPRESTADO));
        assertInstanceOf(EstadoEmReparo.class, EstadoExemplarFactory.criar(StatusExemplar.EM_REPARO));
        assertInstanceOf(EstadoDanificado.class, EstadoExemplarFactory.criar(StatusExemplar.DANIFICADO));
        assertInstanceOf(EstadoPerdido.class, EstadoExemplarFactory.criar(StatusExemplar.PERDIDO));
    }

    @Test
    @DisplayName("estado criado é do tipo correto e pode ser usado por Exemplar")
    void estadoCriadoFuncionaNoExemplar() {
        EstadoExemplar estado = EstadoExemplarFactory.criar(StatusExemplar.DISPONIVEL);
        assertTrue(estado.podeEmprestar());
    }
}
