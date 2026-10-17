package org.example.biblioteca.domain.exception;

/**
 * Lançada quando um empréstimo não é encontrado no sistema.
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class EmprestimoNaoEncontradoException extends RegraNegocioException {

    public EmprestimoNaoEncontradoException() {
        super();
    }

    public EmprestimoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public EmprestimoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public EmprestimoNaoEncontradoException(Throwable causa) {
        super(causa);
    }
}
