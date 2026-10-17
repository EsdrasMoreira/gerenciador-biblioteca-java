package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Usuario;

import java.util.Optional;

/**
 * Repositório de {@link Usuario}.
 * <p>
 * Oferece busca por matrícula única além das operações CRUD básicas.
 * Implementação em memória: {@link UsuarioRepositoryImpl}.
 *
 * @see tasks T3.3, T4.3
 */
public interface UsuarioRepository extends CrudRepository<Usuario> {

    /**
     * Busca um usuário pela matrícula única.
     *
     * @param matricula matrícula do usuário
     * @return {@link Optional} contendo o usuário, ou vazio se não encontrado
     */
    Optional<Usuario> buscarPorMatricula(String matricula);
}
