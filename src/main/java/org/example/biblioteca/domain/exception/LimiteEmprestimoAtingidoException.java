package org.example.biblioteca.domain.exception;

/**
 * Lançada quando o usuário atingiu o limite de livros simultâneos
 * permitido para seu tipo de usuário.
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class LimiteEmprestimoAtingidoException extends RegraNegocioException {

    public LimiteEmprestimoAtingidoException() {
        super();
    }

    public LimiteEmprestimoAtingidoException(String mensagem) {
        super(mensagem);
    }

    public LimiteEmprestimoAtingidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public LimiteEmprestimoAtingidoException(Throwable causa) {
        super(causa);
    }
}
