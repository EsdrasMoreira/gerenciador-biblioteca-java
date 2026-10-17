package org.example.biblioteca.application.dto;

import org.example.biblioteca.domain.enums.StatusReserva;

import java.time.LocalDateTime;

/**
 * DTO de {@link org.example.biblioteca.domain.model.Reserva}.
 * <p>
 * Representa uma posição na fila de espera por um título quando todos
 * os seus exemplares estão emprestados.
 *
 * @see tasks T4.1
 */
public class ReservaDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long tituloId;
    private String tituloNome;
    private LocalDateTime dataReserva;
    private StatusReserva status;
    private LocalDateTime dataNotificacao;
    private LocalDateTime dataExpiracao;

    public ReservaDTO() {
    }

    public ReservaDTO(Long id, Long usuarioId, String usuarioNome,
                      Long tituloId, String tituloNome, LocalDateTime dataReserva,
                      StatusReserva status, LocalDateTime dataNotificacao,
                      LocalDateTime dataExpiracao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.tituloId = tituloId;
        this.tituloNome = tituloNome;
        this.dataReserva = dataReserva;
        this.status = status;
        this.dataNotificacao = dataNotificacao;
        this.dataExpiracao = dataExpiracao;
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

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public LocalDateTime getDataNotificacao() {
        return dataNotificacao;
    }

    public void setDataNotificacao(LocalDateTime dataNotificacao) {
        this.dataNotificacao = dataNotificacao;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    @Override
    public String toString() {
        return "ReservaDTO{"
            + "id=" + id
            + ", usuarioId=" + usuarioId
            + ", usuarioNome='" + usuarioNome + '\''
            + ", tituloId=" + tituloId
            + ", tituloNome='" + tituloNome + '\''
            + ", dataReserva=" + dataReserva
            + ", status=" + status
            + ", dataNotificacao=" + dataNotificacao
            + ", dataExpiracao=" + dataExpiracao
            + '}';
    }
}
