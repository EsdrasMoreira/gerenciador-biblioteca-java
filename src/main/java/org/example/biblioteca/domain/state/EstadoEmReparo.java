package org.example.biblioteca.domain.state;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.model.Exemplar;

/**
 * Estado {@code EM_REPARO}: exemplar indisponível, em reparo.
 * <p>
 * Nenhuma transição de empréstimo, devolução ou baixa é permitida neste estado.
 *
 * @see design 3.2
 * @see tasks T2.8
 */
public class EstadoEmReparo implements EstadoExemplar {

    @Override
    public boolean podeEmprestar() {
        return false;
    }

    @Override
    public void executarEmprestimo(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar está em reparo e não pode ser emprestado. Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarDevolucao(Exemplar exemplar, boolean danificado, boolean perdido)
            throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar em reparo não pode ser devolvido (não está emprestado). Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarEnviarReparo(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar já está em reparo. Código de tombo: " + exemplar.getCodigoTombo());
    }

    @Override
    public void executarBaixa(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar em reparo não pode ser baixado. Código de tombo: " + exemplar.getCodigoTombo());
    }
}
