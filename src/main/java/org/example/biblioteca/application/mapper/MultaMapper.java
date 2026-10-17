package org.example.biblioteca.application.mapper;

import org.example.biblioteca.application.dto.MultaDTO;
import org.example.biblioteca.domain.model.Multa;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper bidirecional entre {@link Multa} (entidade de domínio) e
 * {@link MultaDTO} (DTO de apresentação).
 *
 * @see tasks T4.2
 */
public class MultaMapper {

    private MultaMapper() {
        // classe utilitária com métodos estáticos
    }

    /**
     * Converte entidade de domínio para DTO.
     */
    public static MultaDTO toDto(Multa multa) {
        if (multa == null) {
            return null;
        }
        MultaDTO dto = new MultaDTO();
        dto.setId(multa.getId());
        dto.setValor(multa.getValor());
        dto.setStatus(multa.getStatus());
        dto.setDataVencimento(multa.getDataVencimento());
        dto.setDescricao(multa.getDescricao());

        if (multa.getUsuario() != null) {
            dto.setUsuarioId(multa.getUsuario().getId());
            dto.setUsuarioNome(multa.getUsuario().getNome());
        }
        if (multa.getEmprestimo() != null) {
            dto.setEmprestimoId(multa.getEmprestimo().getId());
        }
        return dto;
    }

    /**
     * Converte DTO para entidade de domínio.
     */
    public static Multa toEntity(MultaDTO dto) {
        if (dto == null) {
            return null;
        }
        Multa multa = new Multa();
        multa.setId(dto.getId());
        multa.setValor(dto.getValor());
        multa.setStatus(dto.getStatus());
        multa.setDataVencimento(dto.getDataVencimento());
        multa.setDescricao(dto.getDescricao());
        return multa;
    }

    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     */
    public static List<MultaDTO> toDtoList(List<Multa> multas) {
        if (multas == null) {
            return new ArrayList<>();
        }
        List<MultaDTO> dtos = new ArrayList<>(multas.size());
        for (Multa m : multas) {
            dtos.add(toDto(m));
        }
        return dtos;
    }
}
