package org.example.biblioteca.domain.model;

import org.example.biblioteca.domain.enums.StatusExemplar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Obra bibliográfica. Agrupa múltiplos exemplares.
 *
 * @see design 2.3
 * @see tasks T1.6
 */
public class Titulo extends Entidade {

    private String nome; // ex: "Dom Casmurro"
    private String autor;
    private String isbn;
    private boolean ativo;
    private List<Exemplar> exemplares;

    public Titulo() {
        this.exemplares = new ArrayList<>();
    }

    public Titulo(Long id, String nome, String autor, String isbn, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.isbn = isbn;
        this.ativo = ativo;
        this.exemplares = new ArrayList<>();
    }

    // Getters e setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Exemplar> getExemplares() {
        return exemplares;
    }

    public void setExemplares(List<Exemplar> exemplares) {
        this.exemplares = exemplares;
    }

    public void adicionarExemplar(Exemplar exemplar) {
        if (this.exemplares == null) {
            this.exemplares = new ArrayList<>();
        }
        this.exemplares.add(exemplar);
    }

    /**
     * Busca o primeiro exemplar disponível e ativo do título.
     *
     * @return Optional contendo o exemplar disponível, ou vazio se não houver.
     * @see design 2.3
     * @see tasks T1.6
     */
    public Optional<Exemplar> buscarExemplarDisponivel() {
        if (exemplares == null) {
            return Optional.empty();
        }
        return exemplares.stream()
            .filter(Exemplar::estaDisponivel)
            .findFirst();
    }

    /**
     * Indica se o título possui pelo menos um exemplar disponível e ativo.
     *
     * @see design 2.3
     * @see tasks T1.6
     */
    public boolean possuiExemplarDisponivel() {
        return buscarExemplarDisponivel().isPresent();
    }
}
