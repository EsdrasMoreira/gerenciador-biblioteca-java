package org.example.biblioteca.application.mapper;

import org.example.biblioteca.application.dto.UsuarioDTO;
import org.example.biblioteca.domain.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper bidirecional entre {@link Usuario} (entidade de domínio) e
 * {@link UsuarioDTO} (DTO de apresentação).
 *
 * @see tasks T4.2
 */
public class UsuarioMapper {

    private UsuarioMapper() {
        // classe utilitária com métodos estáticos
    }

    /**
     * Converte entidade de domínio para DTO.
     */
    public static UsuarioDTO toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setMatricula(usuario.getMatricula());
        dto.setTipo(usuario.getTipo());
        dto.setAtivo(usuario.isAtivo());
        dto.setQuantidadeEmprestimosAtivos(usuario.quantidadeEmprestimosAtivos());
        dto.setQuantidadeMultasPendentes(
            usuario.getMultasPendentes() == null ? 0 : usuario.getMultasPendentes().size());
        dto.setQuantidadeReservas(usuario.getReservas() == null ? 0 : usuario.getReservas().size());
        return dto;
    }

    /**
     * Converte DTO para entidade de domínio.
     * <p>
     * Relacionamentos (empréstimos, multas, reservas) não são preenchidos;
     * devem ser associados pelo serviço de domínio após a persistência.
     */
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());
        usuario.setAtivo(dto.isAtivo());
        return usuario;
    }

    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     */
    public static List<UsuarioDTO> toDtoList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return new ArrayList<>();
        }
        List<UsuarioDTO> dtos = new ArrayList<>(usuarios.size());
        for (Usuario u : usuarios) {
            dtos.add(toDto(u));
        }
        return dtos;
    }
}
