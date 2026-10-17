package org.example.biblioteca.domain.strategy;

import org.example.biblioteca.domain.model.Usuario;

/**
 * Estratégia de empréstimo por tipo de usuário.
 * <p>
 * Encapsula o limite de livros simultâneos, o prazo de retenção e a validação
 * específica de cada tipo de usuário (aluno, professor ou funcionário), permitindo
 * trocar o algoritmo em tempo de execução sem alterar o serviço de empréstimo
 * (Open/Closed Principle).
 *
 * @see design 3.1
 * @see tasks T2.1
 */
public interface PoliticaEmprestimo {

    /**
     * Retorna o limite máximo de livros emprestados simultaneamente por este tipo de usuário.
     */
    int getLimiteLivrosSimultaneos();

    /**
     * Retorna o prazo de retenção (em dias) para empréstimo deste tipo de usuário.
     */
    int getPrazoRetencaoDias();

    /**
     * Indica se o usuário pode realizar um novo empréstimo segundo as regras desta política.
     *
     * @param usuario usuário a ser validado
     * @return {@code true} se o usuário pode emprestar, {@code false} caso contrário
     */
    boolean podeEmprestar(Usuario usuario);
}
