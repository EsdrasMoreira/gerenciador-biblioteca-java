package org.example.biblioteca.application.mapper;

import org.example.biblioteca.application.dto.TituloDTO;
import org.example.biblioteca.domain.model.Titulo;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper bidirecional entre {@link Titulo} (entidade de domínio) e
 * {@link TituloDTO} (DTO de apresentação).
 *
 * @see tasks T4.2
 */
public class TituloMapper {

    private TituloMapper() {
        // classe utilitária com métodos estáticos
    }

    /**
     * Converte entidade de domínio para DTO.
     */
    public static TituloDTO toDto(Titulo titulo) {
        if (titulo == null) {
            return null;
        }
        TituloDTO dto = new TituloDTO();
        dto.setId(titulo.getId());
        dto.setNome(titulo.getNome());
        dto.setAutor(titulo.getAutor());
        dto.setIsbn(titulo.getIsbn());
        dto.setAtivo(titulo.isAtivo());
        int total = titulo.getExemplares() == null ? 0 : titulo.getExemplares().size();
        dto.setQuantidadeExemplares(total);
        long disponiveis = titulo.getExemplares() == null
            ? 0
            : titulo.getExemplares().stream().filter(org.example.biblioteca.domain.model.Exemplar::estaDisponivel).count();
        dto.setExemplaresDisponiveis((int) disponiveis);
        return dto;
    }

    /**
     * Converte DTO para entidade de domínio.
     */
    public static Titulo toEntity(TituloDTO dto) {
        if (dto == null) {
            return null;
        }
        Titulo titulo = new Titulo();
        titulo.setId(dto.getId());
        titulo.setNome(dto.getNome());
        titulo.setAutor(dto.getAutor());
        titulo.setIsbn(dto.getIsbn());
        titulo.setAtivo(dto.isAtivo());
        return titulo;
    }

    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     */
    public static List<TituloDTO> toDtoList(List<Titulo> titulos) {
        if (titulos == null) {
            return new ArrayList<>();
        }
        List<TituloDTO> dtos = new ArrayList<>(titulos.size());
        for (Titulo t : titulos) {
            dtos.add(toDto(t));
        }
        return dtos;
    }
}
