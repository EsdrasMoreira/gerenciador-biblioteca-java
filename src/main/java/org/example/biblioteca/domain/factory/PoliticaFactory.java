package org.example.biblioteca.domain.factory;

import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.strategy.PoliticaAluno;
import org.example.biblioteca.domain.strategy.PoliticaEmprestimo;
import org.example.biblioteca.domain.strategy.PoliticaFuncionario;
import org.example.biblioteca.domain.strategy.PoliticaProfessor;

/**
 * Factory de estratégias de empréstimo (padrão Factory + Strategy).
 * <p>
 * Centraliza a criação de políticas concretas, desacoplando o cliente das
 * classes de implementação.
 *
 * @see design 3.3
 * @see tasks T2.6
 */
public class PoliticaFactory {

    /**
     * Cria a política de empréstimo correspondente ao tipo de usuário informado.
     *
     * @param tipo tipo do usuário (ALUNO, PROFESSOR ou FUNCIONARIO)
     * @return política concreta de empréstimo
     * @throws IllegalArgumentException se o tipo não for reconhecido
     */
    public static PoliticaEmprestimo criar(TipoUsuario tipo) {
        return switch (tipo) {
            case ALUNO -> new PoliticaAluno();
            case PROFESSOR -> new PoliticaProfessor();
            case FUNCIONARIO -> new PoliticaFuncionario();
        };
    }
}
