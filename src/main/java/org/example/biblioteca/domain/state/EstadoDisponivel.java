package org.example.biblioteca.domain.state;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.model.Exemplar;

/**
 * Estado {@code DISPONIVEL}: exemplar disponível para empréstimo.
 * <p>
 * Transições válidas:
 * <ul>
 *   <li>empréstimo → {@code EMPRESTADO}</li>
 *   <li>envio para reparo → {@code EM_REPARO}</li>
 * </ul>
 *
 * @see design 3.2
 * @see tasks T2.8
 */
public class EstadoDisponivel implements EstadoExemplar {

    @Override
    public boolean podeEmprestar() {
        return true;
    }

    @Override
    public void executarEmprestimo(Exemplar exemplar) {
        exemplar.setStatus(StatusExemplar.EMPRESTADO);
    }

    @Override
    public void executarDevolucao(Exemplar exemplar, boolean danificado, boolean perdido)
            throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar disponível não pode ser devolvido (não está emprestado). Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarEnviarReparo(Exemplar exemplar) {
        exemplar.setStatus(StatusExemplar.EM_REPARO);
    }

    @Override
    public void executarBaixa(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar disponível não pode ser baixado. Código de tombo: " + exemplar.getCodigoTombo());
    }
}
