package org.example.biblioteca.domain.strategy;

import org.example.biblioteca.domain.model.Usuario;

/**
 * Política de empréstimo para professores.
 * <p>
 * Limite: 5 livros simultâneos | Prazo: 30 dias (RN02).
 *
 * @see design 3.1
 * @see tasks T2.3
 */
public class PoliticaProfessor implements PoliticaEmprestimo {

    @Override
    public int getLimiteLivrosSimultaneos() {
        return 5;
    }

    @Override
    public int getPrazoRetencaoDias() {
        return 30;
    }

    @Override
    public boolean podeEmprestar(Usuario usuario) {
        return usuario != null && usuario.isAtivo() && !usuario.estaBloqueado();
    }
}
