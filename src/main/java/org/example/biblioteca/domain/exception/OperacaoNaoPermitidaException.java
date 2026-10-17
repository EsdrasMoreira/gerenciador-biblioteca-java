package org.example.biblioteca.domain.exception;

/**
 * Lançada quando uma operação não é permitida no estado atual do domínio
 * (ex: emprestar um exemplar que não está disponível).
 * <p>
 * Usada pela máquina de estados do exemplar (padrão State) e pelas
 * estratégias de empréstimo (padrão Strategy) para sinalizar violações
 * de regra de negócio de forma padronizada.
 *
 * @see design 3.2
 * @see design 4.3
 * @see tasks T2.8
 */
public class OperacaoNaoPermitidaException extends RegraNegocioException {

    public OperacaoNaoPermitidaException() {
        super();
    }

    public OperacaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }

    public OperacaoNaoPermitidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public OperacaoNaoPermitidaException(Throwable causa) {
        super(causa);
    }
}
