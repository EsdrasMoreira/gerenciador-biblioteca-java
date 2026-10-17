package org.example.biblioteca.application.dto;

/**
 * DTO de {@link org.example.biblioteca.domain.model.Titulo}.
 * <p>
 * Agrupa informações da obra bibliográfica e resumo de seus exemplares.
 *
 * @see tasks T4.1
 */
public class TituloDTO {

    private Long id;
    private String nome;
    private String autor;
    private String isbn;
    private boolean ativo;
    private int quantidadeExemplares;
    private int exemplaresDisponiveis;

    public TituloDTO() {
    }

    public TituloDTO(Long id, String nome, String autor, String isbn,
                     boolean ativo, int quantidadeExemplares, int exemplaresDisponiveis) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.isbn = isbn;
        this.ativo = ativo;
        this.quantidadeExemplares = quantidadeExemplares;
        this.exemplaresDisponiveis = exemplaresDisponiveis;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getQuantidadeExemplares() {
        return quantidadeExemplares;
    }

    public void setQuantidadeExemplares(int quantidadeExemplares) {
        this.quantidadeExemplares = quantidadeExemplares;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public void setExemplaresDisponiveis(int exemplaresDisponiveis) {
        this.exemplaresDisponiveis = exemplaresDisponiveis;
    }

    @Override
    public String toString() {
        return "TituloDTO{"
            + "id=" + id
            + ", nome='" + nome + '\''
            + ", autor='" + autor + '\''
            + ", isbn='" + isbn + '\''
            + ", ativo=" + ativo
            + ", exemplares=" + quantidadeExemplares
            + ", disponiveis=" + exemplaresDisponiveis
            + '}';
    }
}
