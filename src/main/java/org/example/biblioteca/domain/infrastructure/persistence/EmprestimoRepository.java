package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Emprestimo;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de {@link Emprestimo}.
 * <p>
 * Oferece consultas de empréstimos ativos por usuário e por exemplar.
 * Implementação em memória: {@link EmprestimoRepositoryImpl}.
 *
 * @see tasks T3.3, T4.3
 */
public interface EmprestimoRepository extends CrudRepository<Emprestimo> {

    /**
     * Retorna os empréstimos ativos (não devolvidos) de um usuário.
     *
     * @param usuarioId id do usuário
     * @return lista de empréstimos ativos do usuário
     */
    List<Emprestimo> buscarAtivosPorUsuario(Long usuarioId);

    /**
     * Busca o empréstimo ativo de um exemplar, se houver.
     *
     * @param exemplarId id do exemplar
     * @return {@link Optional} contendo o empréstimo ativo, ou vazio
     */
    Optional<Emprestimo> buscarAtivoPorExemplar(Long exemplarId);
}
