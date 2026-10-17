package org.example.biblioteca.domain.exception;

/**
 * Lançada quando não há exemplar disponível para empréstimo de um título.
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class ExemplarIndisponivelException extends RegraNegocioException {

    public ExemplarIndisponivelException() {
        super();
    }

    public ExemplarIndisponivelException(String mensagem) {
        super(mensagem);
    }

    public ExemplarIndisponivelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public ExemplarIndisponivelException(Throwable causa) {
        super(causa);
    }
}
