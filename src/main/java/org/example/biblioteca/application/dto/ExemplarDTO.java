package org.example.biblioteca.application.dto;

import org.example.biblioteca.domain.enums.StatusExemplar;

/**
 * DTO de {@link org.example.biblioteca.domain.model.Exemplar}.
 * <p>
 * Representa uma cópia física de um título, identificada pelo código de tombo.
 *
 * @see tasks T4.1
 */
public class ExemplarDTO {

    private Long id;
    private String codigoTombo;
    private Long tituloId;
    private String tituloNome;
    private StatusExemplar status;
    private boolean ativo;
    private boolean disponivel;

    public ExemplarDTO() {
    }

    public ExemplarDTO(Long id, String codigoTombo, Long tituloId,
                       String tituloNome, StatusExemplar status,
                       boolean ativo, boolean disponivel) {
        this.id = id;
        this.codigoTombo = codigoTombo;
        this.tituloId = tituloId;
        this.tituloNome = tituloNome;
        this.status = status;
        this.ativo = ativo;
        this.disponivel = disponivel;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoTombo() {
        return codigoTombo;
    }

    public void setCodigoTombo(String codigoTombo) {
        this.codigoTombo = codigoTombo;
    }

    public Long getTituloId() {
        return tituloId;
    }

    public void setTituloId(Long tituloId) {
        this.tituloId = tituloId;
    }

    public String getTituloNome() {
        return tituloNome;
    }

    public void setTituloNome(String tituloNome) {
        this.tituloNome = tituloNome;
    }

    public StatusExemplar getStatus() {
        return status;
    }

    public void setStatus(StatusExemplar status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return "ExemplarDTO{"
            + "id=" + id
            + ", codigoTombo='" + codigoTombo + '\''
            + ", tituloId=" + tituloId
            + ", tituloNome='" + tituloNome + '\''
            + ", status=" + status
            + ", ativo=" + ativo
            + ", disponivel=" + disponivel
            + '}';
    }
}
