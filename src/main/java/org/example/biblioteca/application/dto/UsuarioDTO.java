package org.example.biblioteca.application.dto;

import org.example.biblioteca.domain.enums.TipoUsuario;

/**
 * DTO de {@link org.example.biblioteca.domain.model.Usuario}.
 * <p>
 * Usado para transferência de dados entre camadas de aplicação e apresentação,
 * acoplando apenas informações relevantes para exibição e cadastro.
 *
 * @see tasks T4.1
 */
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private TipoUsuario tipo;
    private boolean ativo;
    private int quantidadeEmprestimosAtivos;
    private int quantidadeMultasPendentes;
    private int quantidadeReservas;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nome, String email, String matricula,
                      TipoUsuario tipo, boolean ativo,
                      int quantidadeEmprestimosAtivos, int quantidadeMultasPendentes,
                      int quantidadeReservas) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.tipo = tipo;
        this.ativo = ativo;
        this.quantidadeEmprestimosAtivos = quantidadeEmprestimosAtivos;
        this.quantidadeMultasPendentes = quantidadeMultasPendentes;
        this.quantidadeReservas = quantidadeReservas;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getQuantidadeEmprestimosAtivos() {
        return quantidadeEmprestimosAtivos;
    }

    public void setQuantidadeEmprestimosAtivos(int quantidadeEmprestimosAtivos) {
        this.quantidadeEmprestimosAtivos = quantidadeEmprestimosAtivos;
    }

    public int getQuantidadeMultasPendentes() {
        return quantidadeMultasPendentes;
    }

    public void setQuantidadeMultasPendentes(int quantidadeMultasPendentes) {
        this.quantidadeMultasPendentes = quantidadeMultasPendentes;
    }

    public int getQuantidadeReservas() {
        return quantidadeReservas;
    }

    public void setQuantidadeReservas(int quantidadeReservas) {
        this.quantidadeReservas = quantidadeReservas;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{"
            + "id=" + id
            + ", nome='" + nome + '\''
            + ", matricula='" + matricula + '\''
            + ", tipo=" + tipo
            + ", ativo=" + ativo
            + ", emprestimosAtivos=" + quantidadeEmprestimosAtivos
            + ", multasPendentes=" + quantidadeMultasPendentes
            + ", reservas=" + quantidadeReservas
            + '}';
    }
}
