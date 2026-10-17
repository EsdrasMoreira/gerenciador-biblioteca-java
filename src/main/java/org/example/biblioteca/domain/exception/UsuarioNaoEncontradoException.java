package org.example.biblioteca.domain.exception;

/**
 * Lançada quando um usuário não é encontrado no sistema.
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class UsuarioNaoEncontradoException extends RegraNegocioException {

    public UsuarioNaoEncontradoException() {
        super();
    }

    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public UsuarioNaoEncontradoException(Throwable causa) {
        super(causa);
    }
}
