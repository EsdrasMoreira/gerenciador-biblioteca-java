package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Usuario;

import java.util.Optional;

/**
 * Repositório em memória de {@link Usuario}.
 * <p>
 * Oferece busca por matrícula além das operações CRUD básicas herdadas.
 *
 * @see tasks T3.3
 */
public class UsuarioRepositoryImpl extends AbstractCrudRepository<Usuario> implements UsuarioRepository {

    /**
     * Busca um usuário pela matrícula única.
     *
     * @param matricula matrícula do usuário
     * @return {@link Optional} contendo o usuário, ou vazio se não encontrado
     */
    public Optional<Usuario> buscarPorMatricula(String matricula) {
        return listarTodos().stream()
                .filter(u -> matricula.equals(u.getMatricula()))
                .findFirst();
    }
}
