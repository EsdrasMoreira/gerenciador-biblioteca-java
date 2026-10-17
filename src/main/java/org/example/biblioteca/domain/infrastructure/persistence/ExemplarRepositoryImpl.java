package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Exemplar;

import java.util.List;
import java.util.Optional;

/**
 * Repositório em memória de {@link Exemplar}.
 * <p>
 * Mantém índice lógico por {@code (titulo_id, status, ativo)}, permitindo
 * buscar exemplares disponíveis de um título (NF03).
 *
 * @see tasks T3.3
 */
public class ExemplarRepositoryImpl extends AbstractCrudRepository<Exemplar> implements ExemplarRepository {

    /**
     * Busca um exemplar pelo código de tombo único.
     *
     * @param codigoTombo código de tombo do exemplar
     * @return {@link Optional} contendo o exemplar, ou vazio se não encontrado
     */
    public Optional<Exemplar> buscarPorCodigoTombo(String codigoTombo) {
        return listarTodos().stream()
                .filter(e -> codigoTombo.equals(e.getCodigoTombo()))
                .findFirst();
    }

    /**
     * Retorna os exemplares disponíveis e ativos de um título.
     *
     * @param tituloId id do título
     * @return lista de exemplares disponíveis do título
     */
    public List<Exemplar> buscarDisponiveisPorTitulo(Long tituloId) {
        return listarTodos().stream()
                .filter(e -> tituloId != null && tituloId.equals(tituloId(e)))
                .filter(Exemplar::estaDisponivel)
                .toList();
    }

    private Long tituloId(Exemplar exemplar) {
        return exemplar.getTitulo() != null ? exemplar.getTitulo().getId() : null;
    }
}
