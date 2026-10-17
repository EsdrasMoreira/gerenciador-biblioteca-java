package org.example.biblioteca.domain.exception;

/**
 * Exceção base para violações de regras de negócio do domínio.
 * <p>
 * Todas as exceções de negócio concretas herdam desta classe,
 * permitindo tratamento padronizado nas camadas de aplicação e apresentação.
 *
 * @see design 4.3
 * @see tasks T1.13
 */
public class RegraNegocioException extends Exception {

    public RegraNegocioException() {
        super();
    }

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }

    public RegraNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public RegraNegocioException(Throwable causa) {
        super(causa);
    }
}
