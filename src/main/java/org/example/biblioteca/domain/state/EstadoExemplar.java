package org.example.biblioteca.domain.state;

import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.model.Exemplar;

/**
 * Estado do ciclo de vida de um {@link Exemplar}.
 * <p>
 * Encapsula o comportamento associado a cada status do exemplar
 * ({@code DISPONIVEL}, {@code EMPRESTADO}, {@code EM_REPARO}, {@code DANIFICADO},
 * {@code PERDIDO}), permitindo que o exemplar altere seu comportamento conforme seu
 * estado interno, sem blocos {@code if/else} ou {@code switch} espalhados pelo código.
 *
 * @see design 3.2
 * @see tasks T2.7
 */
public interface EstadoExemplar {

    /**
     * Executa a transição de empréstimo do exemplar.
     *
     * @param exemplar exemplar alvo da operação
     * @throws OperacaoNaoPermitidaException se a operação não for permitida neste estado
     */
    void executarEmprestimo(Exemplar exemplar) throws OperacaoNaoPermitidaException;

    /**
     * Executa a transição de devolução do exemplar.
     *
     * @param exemplar   exemplar alvo da operação
     * @param danificado indica se o exemplar foi devolvido danificado
     * @param perdido    indica se o exemplar foi devolvido como perdido
     * @throws OperacaoNaoPermitidaException se a operação não for permitida neste estado
     */
    void executarDevolucao(Exemplar exemplar, boolean danificado, boolean perdido)
            throws OperacaoNaoPermitidaException;

    /**
     * Executa a transição de envio para reparo do exemplar.
     *
     * @param exemplar exemplar alvo da operação
     * @throws OperacaoNaoPermitidaException se a operação não for permitida neste estado
     */
    void executarEnviarReparo(Exemplar exemplar) throws OperacaoNaoPermitidaException;

    /**
     * Executa a baixa do exemplar (remove do acervo disponível).
     *
     * @param exemplar exemplar alvo da operação
     * @throws OperacaoNaoPermitidaException se a operação não for permitida neste estado
     */
    void executarBaixa(Exemplar exemplar) throws OperacaoNaoPermitidaException;

    /**
     * Indica se o exemplar neste estado pode ser emprestado.
     *
     * @return {@code true} se empréstimo é permitido neste estado
     */
    boolean podeEmprestar();
}
