package org.example.biblioteca.domain.exception;

/**
 * Lançada quando um exemplar não é encontrado no sistema.
 *
 * @see design 4.3
 * @see tasks T1.14
 */
public class ExemplarNaoEncontradoException extends RegraNegocioException {

    public ExemplarNaoEncontradoException() {
        super();
    }

    public ExemplarNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ExemplarNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public ExemplarNaoEncontradoException(Throwable causa) {
        super(causa);
    }
}
