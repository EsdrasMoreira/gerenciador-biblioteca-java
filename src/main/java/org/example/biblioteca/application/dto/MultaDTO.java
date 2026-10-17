package org.example.biblioteca.application.dto;

import org.example.biblioteca.domain.enums.StatusMulta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de {@link org.example.biblioteca.domain.model.Multa}.
 * <p>
 * Valor cobrado por atraso na devolução ou taxa de reposição por
 * dano/perda de exemplar.
 *
 * @see tasks T4.1
 */
public class MultaDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long emprestimoId;
    private BigDecimal valor;
    private StatusMulta status;
    private LocalDateTime dataVencimento;
    private String descricao;

    public MultaDTO() {
    }

    public MultaDTO(Long id, Long usuarioId, String usuarioNome,
                    Long emprestimoId, BigDecimal valor, StatusMulta status,
                    LocalDateTime dataVencimento, String descricao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.emprestimoId = emprestimoId;
        this.valor = valor;
        this.status = status;
        this.dataVencimento = dataVencimento;
        this.descricao = descricao;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public Long getEmprestimoId() {
        return emprestimoId;
    }

    public void setEmprestimoId(Long emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusMulta getStatus() {
        return status;
    }

    public void setStatus(StatusMulta status) {
        this.status = status;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "MultaDTO{"
            + "id=" + id
            + ", usuarioId=" + usuarioId
            + ", usuarioNome='" + usuarioNome + '\''
            + ", emprestimoId=" + emprestimoId
            + ", valor=" + valor
            + ", status=" + status
            + ", dataVencimento=" + dataVencimento
            + ", descricao='" + descricao + '\''
            + '}';
    }
}
