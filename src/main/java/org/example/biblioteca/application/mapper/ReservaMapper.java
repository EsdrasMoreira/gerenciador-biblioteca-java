package org.example.biblioteca.application.mapper;

import org.example.biblioteca.application.dto.ReservaDTO;
import org.example.biblioteca.domain.model.Reserva;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper bidirecional entre {@link Reserva} (entidade de domínio) e
 * {@link ReservaDTO} (DTO de apresentação).
 *
 * @see tasks T4.2
 */
public class ReservaMapper {

    private ReservaMapper() {
        // classe utilitária com métodos estáticos
    }

    /**
     * Converte entidade de domínio para DTO.
     */
    public static ReservaDTO toDto(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setDataReserva(reserva.getDataReserva());
        dto.setStatus(reserva.getStatus());
        dto.setDataNotificacao(reserva.getDataNotificacao());
        dto.setDataExpiracao(reserva.getDataExpiracao());

        if (reserva.getUsuario() != null) {
            dto.setUsuarioId(reserva.getUsuario().getId());
            dto.setUsuarioNome(reserva.getUsuario().getNome());
        }
        if (reserva.getTitulo() != null) {
            dto.setTituloId(reserva.getTitulo().getId());
            dto.setTituloNome(reserva.getTitulo().getNome());
        }
        return dto;
    }

    /**
     * Converte DTO para entidade de domínio.
     */
    public static Reserva toEntity(ReservaDTO dto) {
        if (dto == null) {
            return null;
        }
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setDataReserva(dto.getDataReserva());
        reserva.setStatus(dto.getStatus());
        reserva.setDataNotificacao(dto.getDataNotificacao());
        reserva.setDataExpiracao(dto.getDataExpiracao());
        return reserva;
    }

    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     */
    public static List<ReservaDTO> toDtoList(List<Reserva> reservas) {
        if (reservas == null) {
            return new ArrayList<>();
        }
        List<ReservaDTO> dtos = new ArrayList<>(reservas.size());
        for (Reserva r : reservas) {
            dtos.add(toDto(r));
        }
        return dtos;
    }
}
