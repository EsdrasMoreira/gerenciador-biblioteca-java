package org.example.biblioteca.application.mapper;

import org.example.biblioteca.application.dto.EmprestimoDTO;
import org.example.biblioteca.application.dto.MultaDTO;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Multa;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper bidirecional entre {@link Emprestimo} (entidade de domínio) e
 * {@link EmprestimoDTO} (DTO de apresentação).
 *
 * @see tasks T4.2
 */
public class EmprestimoMapper {

    private EmprestimoMapper() {
        // classe utilitária com métodos estáticos
    }

    /**
     * Converte entidade de domínio para DTO.
     */
    public static EmprestimoDTO toDto(Emprestimo emprestimo) {
        if (emprestimo == null) {
            return null;
        }
        EmprestimoDTO dto = new EmprestimoDTO();
        dto.setId(emprestimo.getId());
        dto.setOperador(emprestimo.getOperador());
        dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
        dto.setDataPrevistaDevolucao(emprestimo.getDataPrevistaDevolucao());
        dto.setDataRealDevolucao(emprestimo.getDataRealDevolucao());
        dto.setAtivo(emprestimo.isAtivo());

        if (emprestimo.getUsuario() != null) {
            dto.setUsuarioId(emprestimo.getUsuario().getId());
            dto.setUsuarioNome(emprestimo.getUsuario().getNome());
        }
        if (emprestimo.getExemplar() != null) {
            dto.setExemplarId(emprestimo.getExemplar().getId());
            dto.setExemplarCodigoTombo(emprestimo.getExemplar().getCodigoTombo());
        }
        if (emprestimo.getMulta() != null) {
            dto.setMulta(MultaMapper.toDto(emprestimo.getMulta()));
        }
        return dto;
    }

    /**
     * Converte DTO para entidade de domínio.
     * <p>
     * Usuário, exemplar e multa não são associados aqui; devem ser
     * resolvidos pelo serviço de domínio.
     */
    public static Emprestimo toEntity(EmprestimoDTO dto) {
        if (dto == null) {
            return null;
        }
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(dto.getId());
        emprestimo.setOperador(dto.getOperador());
        emprestimo.setDataEmprestimo(dto.getDataEmprestimo());
        emprestimo.setDataPrevistaDevolucao(dto.getDataPrevistaDevolucao());
        emprestimo.setDataRealDevolucao(dto.getDataRealDevolucao());
        emprestimo.setAtivo(dto.isAtivo());
        if (dto.getMulta() != null) {
            emprestimo.setMulta(MultaMapper.toEntity(dto.getMulta()));
        }
        return emprestimo;
    }

    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     */
    public static List<EmprestimoDTO> toDtoList(List<Emprestimo> emprestimos) {
        if (emprestimos == null) {
            return new ArrayList<>();
        }
        List<EmprestimoDTO> dtos = new ArrayList<>(emprestimos.size());
        for (Emprestimo e : emprestimos) {
            dtos.add(toDto(e));
        }
        return dtos;
    }
}
