package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Titulo;

import java.util.Optional;

/**
 * Repositório de {@link Titulo}.
 * <p>
 * Oferece busca por ISBN além das operações CRUD básicas.
 * Implementação em memória: {@link TituloRepositoryImpl}.
 *
 * @see tasks T3.3, T4.3
 */
public interface TituloRepository extends CrudRepository<Titulo> {

    /**
     * Busca um título pelo ISBN.
     *
     * @param isbn ISBN do título
     * @return {@link Optional} contendo o título, ou vazio se não encontrado
     */
    Optional<Titulo> buscarPorIsbn(String isbn);
}
