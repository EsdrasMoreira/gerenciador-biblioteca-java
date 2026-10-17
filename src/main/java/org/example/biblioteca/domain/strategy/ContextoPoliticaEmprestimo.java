package org.example.biblioteca.domain.strategy;

import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.factory.PoliticaFactory;
import org.example.biblioteca.domain.model.Usuario;

/**
 * Contexto da Strategy de empréstimo.
 * <p>
 * Seleciona a política concreta pelo {@link TipoUsuario} e expõe prazo, limite
 * e validação de empréstimo, delegando todo o comportamento para a estratégia
 * atualmente configurada.
 *
 * @see design 3.1
 * @see tasks T2.5
 */
public class ContextoPoliticaEmprestimo {

    private PoliticaEmprestimo politica;

    /**
     * Seleciona a política de empréstimo conforme o tipo de usuário informado.
     *
     * @param tipoUsuario tipo do usuário (ALUNO, PROFESSOR ou FUNCIONARIO)
     */
    public void setPolitica(TipoUsuario tipoUsuario) {
        this.politica = PoliticaFactory.criar(tipoUsuario);
    }

    /**
     * Retorna a política de empréstimo atualmente configurada.
     *
     * @return política concreta, ou {@code null} se nenhuma foi definida
     */
    public PoliticaEmprestimo getPolitica() {
        return politica;
    }

    /**
     * Retorna o prazo de retenção (em dias) da política vigente.
     *
     * @return prazo em dias
     * @throws IllegalStateException se nenhuma política estiver definida
     */
    public int prazoDias() {
        garantirPolitica();
        return politica.getPrazoRetencaoDias();
    }

    /**
     * Retorna o limite de livros simultâneos da política vigente.
     *
     * @return limite de livros
     * @throws IllegalStateException se nenhuma política estiver definida
     */
    public int limite() {
        garantirPolitica();
        return politica.getLimiteLivrosSimultaneos();
    }

    /**
     * Valida se o usuário pode emprestar segundo a política vigente.
     *
     * @param usuario usuário a ser validado
     * @return {@code true} se o usuário pode emprestar
     * @throws IllegalStateException se nenhuma política estiver definida
     */
    public boolean valida(Usuario usuario) {
        garantirPolitica();
        return politica.podeEmprestar(usuario);
    }

    private void garantirPolitica() {
        if (politica == null) {
            throw new IllegalStateException("Politica de emprestimo nao definida. Informe um TipoUsuario.");
        }
    }
}
