package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.enums.StatusMulta;
import org.example.biblioteca.domain.model.Multa;

import java.util.List;

/**
 * Repositório em memória de {@link Multa}.
 * <p>
 * Oferece consulta de multas pendentes de um usuário.
 *
 * @see tasks T3.3
 */
public class MultaRepositoryImpl extends AbstractCrudRepository<Multa> implements MultaRepository {

    /**
     * Retorna as multas com status PENDENTE de um usuário.
     *
     * @param usuarioId id do usuário
     * @return lista de multas pendentes do usuário
     */
    public List<Multa> listarPendentesPorUsuario(Long usuarioId) {
        return listarTodos().stream()
                .filter(m -> usuarioId != null && usuarioId.equals(usuarioId(m)))
                .filter(m -> m.getStatus() == StatusMulta.PENDENTE)
                .toList();
    }

    private Long usuarioId(Multa multa) {
        return multa.getUsuario() != null ? multa.getUsuario().getId() : null;
    }
}
