package org.example.biblioteca.domain.strategy;

import org.example.biblioteca.domain.model.Usuario;

/**
 * Política de empréstimo para funcionários.
 * <p>
 * Limite: 3 livros simultâneos | Prazo: 14 dias (RN03).
 *
 * @see design 3.1
 * @see tasks T2.4
 */
public class PoliticaFuncionario implements PoliticaEmprestimo {

    @Override
    public int getLimiteLivrosSimultaneos() {
        return 3;
    }

    @Override
    public int getPrazoRetencaoDias() {
        return 14;
    }

    @Override
    public boolean podeEmprestar(Usuario usuario) {
        return usuario != null && usuario.isAtivo() && !usuario.estaBloqueado();
    }
}
