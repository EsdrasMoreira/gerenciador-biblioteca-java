package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Entidade;

import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico de operações CRUD para repositórios de entidades.
 * <p>
 * A implementação em memória ({@link AbstractCrudRepository}) é thread-safe
 * ({@code ConcurrentHashMap}) e pode ser substituída futuramente por JPA+H2
 * sem impactar as camadas de aplicação e apresentação.
 *
 * @param <E> tipo de entidade gerenciada
 * @see tasks T4.3
 */
public interface CrudRepository<E extends Entidade> {

    /**
     * Salva (ou atualiza) uma entidade, atribuindo um id caso ainda não possua.
     *
     * @param entidade entidade a ser persistida
     * @return a entidade persistida (com id atribuído)
     */
    E salvar(E entidade);

    /**
     * Busca uma entidade pelo seu id.
     *
     * @param id identificador da entidade
     * @return {@link Optional} contendo a entidade, ou vazio se não encontrada
     */
    Optional<E> buscarPorId(Long id);

    /**
     * Retorna todas as entidades armazenadas.
     *
     * @return lista de todas as entidades
     */
    List<E> listarTodos();

    /**
     * Remove a entidade com o id informado.
     *
     * @param id identificador da entidade
     * @return {@code true} se removeu alguma entidade, {@code false} caso contrário
     */
    boolean remover(Long id);
}
