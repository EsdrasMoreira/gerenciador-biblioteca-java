package org.example.biblioteca.domain.state;

import static org.junit.jupiter.api.Assertions.*;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Titulo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes da máquina de estados do exemplar (State) — matriz completa de transições (T5.3).
 * {@see design 3.2}
 * {@see tasks T2.7, T2.8, T2.11, T5.3}
 */
class EstadoExemplarTest {

    private Titulo titulo;

    @BeforeEach
    void setUp() throws Exception {
        titulo = new Titulo(1L, "Obra", "Autor", "isbn", true);
    }

    private Exemplar exemplar(StatusExemplar status) {
        return new Exemplar(1L, "T-001", titulo, status, true);
    }

    // --- DISPONIVEL ---

    @Test
    @DisplayName("DISPONIVEL: pode emprestar e transita para EMPRESTADO")
    void disponivelPodeEmprestar() throws Exception {
        Exemplar e = exemplar(StatusExemplar.DISPONIVEL);
        assertTrue(e.getEstado().podeEmprestar());
        e.emprestar();
        assertEquals(StatusExemplar.EMPRESTADO, e.getStatus());
        assertFalse(e.estaDisponivel());
    }

    @Test
    @DisplayName("DISPONIVEL: não pode ser devolvido (não está emprestado)")
    void disponivelNaoPodeDevolver() throws Exception {
        Exemplar e = exemplar(StatusExemplar.DISPONIVEL);
        assertThrows(OperacaoNaoPermitidaException.class, () -> e.devolver(false, false));
    }

    @Test
    @DisplayName("DISPONIVEL: pode ser enviado para reparo")
    void disponivelPodeEnviarParaReparo() throws Exception {
        Exemplar e = exemplar(StatusExemplar.DISPONIVEL);
        e.enviarParaReparo();
        assertEquals(StatusExemplar.EM_REPARO, e.getStatus());
    }

    @Test
    @DisplayName("DISPONIVEL: não pode dar baixa")
    void disponivelNaoPodeDarBaixa() throws Exception {
        Exemplar e = exemplar(StatusExemplar.DISPONIVEL);
        assertThrows(OperacaoNaoPermitidaException.class, e::darBaixa);
    }

    // --- EMPRESTADO ---

    @Test
    @DisplayName("EMPRESTADO: não pode emprestar novamente nem enviar para reparo nem dar baixa")
    void emprestadoOperacoesInvalidas() throws Exception {
        Exemplar e = exemplar(StatusExemplar.EMPRESTADO);
        assertFalse(e.getEstado().podeEmprestar());
        assertThrows(OperacaoNaoPermitidaException.class, e::emprestar);
        assertThrows(OperacaoNaoPermitidaException.class, e::enviarParaReparo);
        assertThrows(OperacaoNaoPermitidaException.class, e::darBaixa);
    }

    @Test
    @DisplayName("EMPRESTADO: devolução em boas condições volta para DISPONIVEL")
    void emprestadoDevolucaoOkViraDisponivel() throws Exception {
        Exemplar e = exemplar(StatusExemplar.EMPRESTADO);
        e.devolver(false, false);
        assertEquals(StatusExemplar.DISPONIVEL, e.getStatus());
        assertTrue(e.isAtivo());
        assertTrue(e.estaDisponivel());
    }

    @Test
    @DisplayName("EMPRESTADO: devolução danificada vira DANIFICADO com baixa (não volta ao acervo)")
    void emprestadoDevolucaoDanificadoViraDanificadoBaixa() throws Exception {
        Exemplar e = exemplar(StatusExemplar.EMPRESTADO);
        e.devolver(true, false);
        assertEquals(StatusExemplar.DANIFICADO, e.getStatus());
        assertFalse(e.isAtivo());
        assertFalse(e.estaDisponivel());
    }

    @Test
    @DisplayName("EMPRESTADO: devolução perdida vira PERDIDO com baixa (não volta ao acervo)")
    void emprestadoDevolucaoPerdidoViraPerdidoBaixa() throws Exception {
        Exemplar e = exemplar(StatusExemplar.EMPRESTADO);
        e.devolver(false, true);
        assertEquals(StatusExemplar.PERDIDO, e.getStatus());
        assertFalse(e.isAtivo());
        assertFalse(e.estaDisponivel());
    }

    // --- EM_REPARO ---

    @Test
    @DisplayName("EM_REPARO: nenhuma operação permitida")
    void emReparoNenhumaOperacaoPermitida() throws Exception {
        Exemplar e = exemplar(StatusExemplar.EM_REPARO);
        assertFalse(e.getEstado().podeEmprestar());
        assertThrows(OperacaoNaoPermitidaException.class, e::emprestar);
        assertThrows(OperacaoNaoPermitidaException.class, () -> e.devolver(false, false));
        assertThrows(OperacaoNaoPermitidaException.class, e::enviarParaReparo);
        assertThrows(OperacaoNaoPermitidaException.class, e::darBaixa);
    }

    // --- DANIFICADO ---

    @Test
    @DisplayName("DANIFICADO: apenas baixa válida; exemplar inativo após a baixa")
    void danificadoApenasBaixa() throws Exception {
        Exemplar e = exemplar(StatusExemplar.DANIFICADO);
        assertFalse(e.getEstado().podeEmprestar());
        assertThrows(OperacaoNaoPermitidaException.class, e::emprestar);
        assertThrows(OperacaoNaoPermitidaException.class, () -> e.devolver(false, false));
        assertThrows(OperacaoNaoPermitidaException.class, e::enviarParaReparo);
        e.darBaixa();
        assertFalse(e.isAtivo());
        assertFalse(e.estaDisponivel());
    }

    // --- PERDIDO ---

    @Test
    @DisplayName("PERDIDO: apenas baixa válida; exemplar inativo após a baixa")
    void perdidoApenasBaixa() throws Exception {
        Exemplar e = exemplar(StatusExemplar.PERDIDO);
        assertFalse(e.getEstado().podeEmprestar());
        assertThrows(OperacaoNaoPermitidaException.class, e::emprestar);
        assertThrows(OperacaoNaoPermitidaException.class, () -> e.devolver(false, false));
        assertThrows(OperacaoNaoPermitidaException.class, e::enviarParaReparo);
        e.darBaixa();
        assertFalse(e.isAtivo());
        assertFalse(e.estaDisponivel());
    }
}

