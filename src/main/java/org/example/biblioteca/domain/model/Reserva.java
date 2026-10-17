package org.example.biblioteca.domain.model;

import org.example.biblioteca.domain.enums.StatusReserva;

import java.time.LocalDateTime;

/**
 * Fila de espera por um título quando todos os exemplares estão emprestados (RN05).
 * A expiração ocorre em 3 dias corridos após a notificação (spec.md, seção 3.6).
 *
 * @see design 2.7
 * @see tasks T1.10
 */
public class Reserva extends Entidade {

    private Usuario usuario;
    private Titulo titulo;
    private LocalDateTime dataReserva;
    private StatusReserva status;
    private LocalDateTime dataNotificacao; // quando notificado da disponibilidade
    private LocalDateTime dataExpiracao; // 3 dias corridos após dataNotificacao

    public Reserva() {
    }

    public Reserva(Long id, Usuario usuario, Titulo titulo, LocalDateTime dataReserva,
                   StatusReserva status, LocalDateTime dataNotificacao, LocalDateTime dataExpiracao) {
        this.id = id;
        this.usuario = usuario;
        this.titulo = titulo;
        this.dataReserva = dataReserva;
        this.status = status;
        this.dataNotificacao = dataNotificacao;
        this.dataExpiracao = dataExpiracao;
    }

    // Getters e setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
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
}
