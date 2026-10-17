package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Emprestimo;

import java.util.List;
import java.util.Optional;

/**
 * Repositório em memória de {@link Emprestimo}.
 * <p>
 * Oferece consultas de empréstimos ativos por usuário e por exemplar.
 *
 * @see tasks T3.3
 */
public class EmprestimoRepositoryImpl extends AbstractCrudRepository<Emprestimo> implements EmprestimoRepository {

    /**
     * Retorna os empréstimos ativos (não devolvidos) de um usuário.
     *
     * @param usuarioId id do usuário
     * @return lista de empréstimos ativos do usuário
     */
    public List<Emprestimo> buscarAtivosPorUsuario(Long usuarioId) {
        return listarTodos().stream()
                .filter(e -> usuarioId != null && usuarioId.equals(usuarioId(e)))
                .filter(Emprestimo::isAtivo)
                .toList();
    }

    /**
     * Busca o empréstimo ativo de um exemplar, se houver.
     *
     * @param exemplarId id do exemplar
     * @return {@link Optional} contendo o empréstimo ativo, ou vazio
     */
    public Optional<Emprestimo> buscarAtivoPorExemplar(Long exemplarId) {
        return listarTodos().stream()
                .filter(e -> exemplarId != null && exemplarId.equals(exemplarId(e)))
                .filter(Emprestimo::isAtivo)
                .findFirst();
    }

    private Long usuarioId(Emprestimo emprestimo) {
        return emprestimo.getUsuario() != null ? emprestimo.getUsuario().getId() : null;
    }

    private Long exemplarId(Emprestimo emprestimo) {
        return emprestimo.getExemplar() != null ? emprestimo.getExemplar().getId() : null;
    }
}
