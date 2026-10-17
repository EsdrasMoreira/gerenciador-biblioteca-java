package org.example.biblioteca.domain.exception;

/**
 * Lançada quando uma reserva não é encontrada no sistema.
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class ReservaNaoEncontradaException extends RegraNegocioException {

    public ReservaNaoEncontradaException() {
        super();
    }

    public ReservaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public ReservaNaoEncontradaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public ReservaNaoEncontradaException(Throwable causa) {
        super(causa);
    }
}
