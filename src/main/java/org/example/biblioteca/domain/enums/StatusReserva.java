package org.example.biblioteca.domain.enums;

/**
 * Status de uma reserva na fila de espera por um título.
 *
 * @see design 2.1
 * @see tasks T1.3
 */
public enum StatusReserva {
    AGUARDANDO,
    NOTIFICADA,
    CONCLUIDA,
    CANCELADA,
    EXPIRADA
}
