package org.example.biblioteca.domain.state;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.model.Exemplar;

/**
 * Estado {@code EMPRESTADO}: exemplar emprestado a um usuário.
 * <p>
 * Transições válidas:
 * <ul>
 *   <li>devolução em boas condições → {@code DISPONIVEL}</li>
 *   <li>devolução danificada → {@code DANIFICADO} + baixa</li>
 *   <li>devolução perdida → {@code PERDIDO} + baixa</li>
 * </ul>
 *
 * @see design 3.2
 * @see tasks T2.8
 */
public class EstadoEmprestado implements EstadoExemplar {

    @Override
    public boolean podeEmprestar() {
        return false;
    }

    @Override
    public void executarEmprestimo(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar já está emprestado. Não é possível emprestar novamente. Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarDevolucao(Exemplar exemplar, boolean danificado, boolean perdido)
            throws OperacaoNaoPermitidaException {
        if (perdido) {
            exemplar.setStatus(StatusExemplar.PERDIDO);
            exemplar.darBaixa(); // taxa de reposição + baixa (RN07)
        } else if (danificado) {
            exemplar.setStatus(StatusExemplar.DANIFICADO);
            exemplar.darBaixa(); // taxa de reposição + baixa (RN07)
        } else {
            exemplar.setStatus(StatusExemplar.DISPONIVEL);
        }
    }

    @Override
    public void executarEnviarReparo(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar emprestado não pode ser enviado para reparo diretamente. Devolva-o primeiro. Código de tombo: "
            + exemplar.getCodigoTombo());
    }

    @Override
    public void executarBaixa(Exemplar exemplar) throws OperacaoNaoPermitidaException {
        throw new OperacaoNaoPermitidaException(
            "Exemplar emprestado não pode ser baixado diretamente. Devolva-o primeiro. Código de tombo: "
            + exemplar.getCodigoTombo());
    }
}
