package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Multa;

import java.util.List;

/**
 * Repositório de {@link Multa}.
 * <p>
 * Oferece consulta de multas pendentes de um usuário.
 * Implementação em memória: {@link MultaRepositoryImpl}.
 *
 * @see tasks T3.3, T4.3
 */
public interface MultaRepository extends CrudRepository<Multa> {

    /**
     * Retorna as multas com status PENDENTE de um usuário.
     *
     * @param usuarioId id do usuário
     * @return lista de multas pendentes do usuário
     */
    List<Multa> listarPendentesPorUsuario(Long usuarioId);
}
