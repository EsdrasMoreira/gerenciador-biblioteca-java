package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Exemplar;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de {@link Exemplar}.
 * <p>
 * Mantém índice lógico por {@code (titulo_id, status, ativo)}, permitindo
 * buscar exemplares disponíveis de um título.
 * Implementação em memória: {@link ExemplarRepositoryImpl}.
 *
 * @see tasks T3.3, T4.3
 */
public interface ExemplarRepository extends CrudRepository<Exemplar> {

    /**
     * Busca um exemplar pelo código de tombo único.
     *
     * @param codigoTombo código de tombo do exemplar
     * @return {@link Optional} contendo o exemplar, ou vazio se não encontrado
     */
    Optional<Exemplar> buscarPorCodigoTombo(String codigoTombo);

    /**
     * Retorna os exemplares disponíveis e ativos de um título.
     *
     * @param tituloId id do título
     * @return lista de exemplares disponíveis do título
     */
    List<Exemplar> buscarDisponiveisPorTitulo(Long tituloId);
}
