package org.example.biblioteca.application.mapper;

import org.example.biblioteca.application.dto.DevolucaoDTO;
import org.example.biblioteca.application.dto.MultaDTO;
import org.example.biblioteca.domain.model.Devolucao;
import org.example.biblioteca.domain.model.Multa;

/**
 * Mapper bidirecional entre {@link Devolucao} (entidade de domínio) e
 * {@link DevolucaoDTO} (DTO de apresentação).
 *
 * @see tasks T4.2
 */
public class DevolucaoMapper {

    private DevolucaoMapper() {
        // classe utilitária com métodos estáticos
    }

    /**
     * Converte entidade de domínio para DTO.
     */
    public static DevolucaoDTO toDto(Devolucao devolucao) {
        if (devolucao == null) {
            return null;
        }
        DevolucaoDTO dto = new DevolucaoDTO();
        dto.setId(devolucao.getId());
        dto.setDataDevolucao(devolucao.getDataDevolucao());
        dto.setExemplarDanificado(devolucao.isExemplarDanificado());
        dto.setExemplarPerdido(devolucao.isExemplarPerdido());

        if (devolucao.getEmprestimo() != null) {
            dto.setEmprestimoId(devolucao.getEmprestimo().getId());
        }
        if (devolucao.getMultaGerada() != null) {
            dto.setMultaGerada(MultaMapper.toDto(devolucao.getMultaGerada()));
        }
        return dto;
    }

    /**
     * Converte DTO para entidade de domínio.
     */
    public static Devolucao toEntity(DevolucaoDTO dto) {
        if (dto == null) {
            return null;
        }
        Devolucao devolucao = new Devolucao();
        devolucao.setId(dto.getId());
        devolucao.setDataDevolucao(dto.getDataDevolucao());
        devolucao.setExemplarDanificado(dto.isExemplarDanificado());
        devolucao.setExemplarPerdido(dto.isExemplarPerdido());
        if (dto.getMultaGerada() != null) {
            devolucao.setMultaGerada(MultaMapper.toEntity(dto.getMultaGerada()));
        }
        return devolucao;
    }
}
