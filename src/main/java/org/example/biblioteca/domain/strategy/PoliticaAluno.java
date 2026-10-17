package org.example.biblioteca.domain.strategy;

import org.example.biblioteca.domain.model.Usuario;

/**
 * Política de empréstimo para alunos.
 * <p>
 * Limite: 3 livros simultâneos | Prazo: 14 dias (RN01).
 *
 * @see design 3.1
 * @see tasks T2.2
 */
public class PoliticaAluno implements PoliticaEmprestimo {

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
