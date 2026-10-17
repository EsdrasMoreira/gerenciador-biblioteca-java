package org.example.biblioteca.domain.exception;

/**
 * Lançada quando um título não é encontrado no sistema.
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class TituloNaoEncontradoException extends RegraNegocioException {

    public TituloNaoEncontradoException() {
        super();
    }

    public TituloNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public TituloNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public TituloNaoEncontradoException(Throwable causa) {
        super(causa);
    }
}
