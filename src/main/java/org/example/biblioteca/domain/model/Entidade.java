package org.example.biblioteca.domain.model;

import java.util.Objects;

/**
 * Classe base para entidades de domínio.
 * <p>
 * Implementa {@code equals}, {@code hashCode} e {@code toString} baseados no {@code id},
 * conforme exigido pela tarefa T1.12.
 *
 * @see tasks T1.12
 */
public abstract class Entidade {

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entidade)) {
            return false;
        }
        Entidade entidade = (Entidade) o;
        // Entidades com id nulo (transientes) só são iguais se forem a mesma instância.
        return id != null && id.equals(entidade.id);
    }

    @Override
    public int hashCode() {
        // Consistent with equals: id null -> use identity hash so distinct transients don't collide as equal.
        return id == null ? System.identityHashCode(this) : id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Entidades de domínio não devem ser clonadas diretamente.");
    }
}
