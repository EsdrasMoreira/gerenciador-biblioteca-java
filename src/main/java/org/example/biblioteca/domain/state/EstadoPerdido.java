package org.example.biblioteca.domain.state;

import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.model.Exemplar;

/**
 * Estado {@code PERDIDO}: exemplar perdido, recebendo baixa do acervo.
 * <p>
 * O exemplar perdido não volta ao acervo disponível; a única transição válida
 * é a baixa (RN07).
 *
 * @see design 3.2
 * @see tasks T2.8
 */
public class EstadoPerdido implements EstadoExemplar {

    @Override
    public boolean podeEmprestar() {
        return false;
    }

    @Override
    public void executarEmprestimo(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar perdido não pode ser emprestado. Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarDevolucao(Exemplar exemplar, boolean danificado, boolean perdido)
            throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar perdido não pode ser devolvido (não está emprestado). Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarEnviarReparo(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar perdido não pode ser enviado para reparo. Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarBaixa(Exemplar exemplar) {
        exemplar.setAtivo(false);
    }
}
