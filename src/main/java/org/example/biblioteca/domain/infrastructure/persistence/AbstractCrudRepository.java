package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Entidade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório abstrato em memória (fake) com operações CRUD básicas.
 * <p>
 * Utiliza {@link ConcurrentHashMap} para armazenamento e {@link AtomicLong}
 * para geração de identificadores. Destinado à Fase 3; substituído por JPA
 * na Fase 4 (NF03).
 *
 * @param <E> tipo de entidade gerenciada
 */
public abstract class AbstractCrudRepository<E extends Entidade> {

    private final ConcurrentHashMap<Long, E> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    /**
     * Salva (ou atualiza) uma entidade, atribuindo um id caso ainda não possua.
     *
     * @param entidade entidade a ser persistida
     * @return a entidade persistida (com id atribuído)
     */
    public E salvar(E entidade) {
        if (entidade.getId() == null) {
            entidade.setId(idSequence.incrementAndGet());
        }
        storage.put(entidade.getId(), entidade);
        return entidade;
    }

    /**
     * Busca uma entidade pelo seu id.
     *
     * @param id identificador da entidade
     * @return {@link Optional} contendo a entidade, ou vazio se não encontrada
     */
    public Optional<E> buscarPorId(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    /**
     * Retorna todas as entidades armazenadas.
     *
     * @return lista de todas as entidades
     */
    public List<E> listarTodos() {
        return new ArrayList<>(storage.values());
    }

    /**
     * Remove a entidade com o id informado.
     *
     * @param id identificador da entidade
     * @return {@code true} se removeu alguma entidade, {@code false} caso contrário
     */
    public boolean remover(Long id) {
        return storage.remove(id) != null;
    }
}
