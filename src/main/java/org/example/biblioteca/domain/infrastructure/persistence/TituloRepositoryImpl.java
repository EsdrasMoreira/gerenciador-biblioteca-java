package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Titulo;

import java.util.Optional;

/**
 * Repositório em memória de {@link Titulo}.
 * <p>
 * Oferece busca por ISBN além das operações CRUD básicas herdadas.
 *
 * @see tasks T3.3
 */
public class TituloRepositoryImpl extends AbstractCrudRepository<Titulo> implements TituloRepository {

    /**
     * Busca um título pelo ISBN.
     *
     * @param isbn ISBN do título
     * @return {@link Optional} contendo o título, ou vazio se não encontrado
     */
    public Optional<Titulo> buscarPorIsbn(String isbn) {
        return listarTodos().stream()
                .filter(t -> isbn.equals(t.getIsbn()))
                .findFirst();
    }
}
