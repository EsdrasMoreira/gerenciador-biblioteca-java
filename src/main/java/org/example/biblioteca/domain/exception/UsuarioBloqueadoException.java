package org.example.biblioteca.domain.exception;

/**
 * Lançada quando o usuário está bloqueado para realizar empréstimos
 * (cadastro inativo, multa pendente ou empréstimo atrasado).
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class UsuarioBloqueadoException extends RegraNegocioException {

    public UsuarioBloqueadoException() {
        super();
    }

    public UsuarioBloqueadoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioBloqueadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public UsuarioBloqueadoException(Throwable causa) {
        super(causa);
    }
}
