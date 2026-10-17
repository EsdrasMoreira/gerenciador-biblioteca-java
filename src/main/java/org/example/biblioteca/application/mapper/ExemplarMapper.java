package org.example.biblioteca.application.mapper;

import org.example.biblioteca.application.dto.ExemplarDTO;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Titulo;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper bidirecional entre {@link Exemplar} (entidade de domínio) e
 * {@link ExemplarDTO} (DTO de apresentação).
 *
 * @see tasks T4.2
 */
public class ExemplarMapper {

    private ExemplarMapper() {
        // classe utilitária com métodos estáticos
    }

    /**
     * Converte entidade de domínio para DTO.
     */
    public static ExemplarDTO toDto(Exemplar exemplar) {
        if (exemplar == null) {
            return null;
        }
        ExemplarDTO dto = new ExemplarDTO();
        dto.setId(exemplar.getId());
        dto.setCodigoTombo(exemplar.getCodigoTombo());
        dto.setStatus(exemplar.getStatus());
        dto.setAtivo(exemplar.isAtivo());
        dto.setDisponivel(exemplar.estaDisponivel());
        Titulo titulo = exemplar.getTitulo();
        if (titulo != null) {
            dto.setTituloId(titulo.getId());
            dto.setTituloNome(titulo.getNome());
        }
        return dto;
    }

    /**
     * Converte DTO para entidade de domínio.
     * <p>
     * O título não é associado aqui; deve ser resolvido pelo serviço de domínio.
     */
    public static Exemplar toEntity(ExemplarDTO dto) {
        if (dto == null) {
            return null;
        }
        Exemplar exemplar = new Exemplar();
        exemplar.setId(dto.getId());
        exemplar.setCodigoTombo(dto.getCodigoTombo());
        exemplar.setStatus(dto.getStatus());
        exemplar.setAtivo(dto.isAtivo());
        return exemplar;
    }

    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     */
    public static List<ExemplarDTO> toDtoList(List<Exemplar> exemplares) {
        if (exemplares == null) {
            return new ArrayList<>();
        }
        List<ExemplarDTO> dtos = new ArrayList<>(exemplares.size());
        for (Exemplar e : exemplares) {
            dtos.add(toDto(e));
        }
        return dtos;
    }
}
