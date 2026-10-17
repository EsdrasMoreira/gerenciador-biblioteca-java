package org.example.biblioteca.application.dto;

import java.time.LocalDateTime;

/**
 * DTO de {@link org.example.biblioteca.domain.model.Devolucao}.
 * <p>
 * Registra a entrega de um exemplar emprestado, incluindo a multa gerada,
 * se houver.
 *
 * @see tasks T4.1
 */
public class DevolucaoDTO {

    private Long id;
    private Long emprestimoId;
    private LocalDateTime dataDevolucao;
    private boolean exemplarDanificado;
    private boolean exemplarPerdido;
    private MultaDTO multaGerada;

    public DevolucaoDTO() {
    }

    public DevolucaoDTO(Long id, Long emprestimoId, LocalDateTime dataDevolucao,
                        boolean exemplarDanificado, boolean exemplarPerdido,
                        MultaDTO multaGerada) {
        this.id = id;
        this.emprestimoId = emprestimoId;
        this.dataDevolucao = dataDevolucao;
        this.exemplarDanificado = exemplarDanificado;
        this.exemplarPerdido = exemplarPerdido;
        this.multaGerada = multaGerada;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmprestimoId() {
        return emprestimoId;
    }

    public void setEmprestimoId(Long emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isExemplarDanificado() {
        return exemplarDanificado;
    }

    public void setExemplarDanificado(boolean exemplarDanificado) {
        this.exemplarDanificado = exemplarDanificado;
    }

    public boolean isExemplarPerdido() {
        return exemplarPerdido;
    }

    public void setExemplarPerdido(boolean exemplarPerdido) {
        this.exemplarPerdido = exemplarPerdido;
    }

    public MultaDTO getMultaGerada() {
        return multaGerada;
    }

    public void setMultaGerada(MultaDTO multaGerada) {
        this.multaGerada = multaGerada;
    }

    @Override
    public String toString() {
        return "DevolucaoDTO{"
            + "id=" + id
            + ", emprestimoId=" + emprestimoId
            + ", dataDevolucao=" + dataDevolucao
            + ", exemplarDanificado=" + exemplarDanificado
            + ", exemplarPerdido=" + exemplarPerdido
            + ", multaGerada=" + multaGerada
            + '}';
    }
}
