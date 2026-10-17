package org.example.biblioteca.domain.model;

import org.example.biblioteca.domain.enums.TipoUsuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma pessoa cadastrada no sistema
 * (aluno, professor ou funcionário).
 * <p>
 * Regras de negócio refletidas:
 * <ul>
 *   <li>{@code ativo} deve ser {@code true} para qualquer operação (RF10).</li>
 *   <li>Usuário bloqueado (livro atrasado ou multa pendente) não pode emprestar (RN06).</li>
 * </ul>
 *
 * @see design 2.2
 * @see tasks T1.5
 */
public class Usuario extends Entidade {

    private String nome;
    private String email;
    private String matricula; // único
    private TipoUsuario tipo;
    private boolean ativo;
    private List<Emprestimo> emprestimosAtivos;
    private List<Multa> multasPendentes;
    private List<Reserva> reservas;

    public Usuario() {
        this.emprestimosAtivos = new ArrayList<>();
        this.multasPendentes = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public Usuario(Long id, String nome, String email, String matricula,
                   TipoUsuario tipo, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.tipo = tipo;
        this.ativo = ativo;
        this.emprestimosAtivos = new ArrayList<>();
        this.multasPendentes = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    // Getters e setters

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

    public List<Emprestimo> getEmprestimosAtivos() {
        return emprestimosAtivos;
    }

    public void setEmprestimosAtivos(List<Emprestimo> emprestimosAtivos) {
        this.emprestimosAtivos = emprestimosAtivos;
    }

    public List<Multa> getMultasPendentes() {
        return multasPendentes;
    }

    public void setMultasPendentes(List<Multa> multasPendentes) {
        this.multasPendentes = multasPendentes;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    /**
     * Adiciona um empréstimo ativo ao usuário.
     */
    public void adicionarEmprestimoAtivo(Emprestimo emprestimo) {
        if (this.emprestimosAtivos == null) {
            this.emprestimosAtivos = new ArrayList<>();
        }
        this.emprestimosAtivos.add(emprestimo);
    }

    /**
     * Remove um empréstimo ativo do usuário (ex: ao devolvê-lo).
     */
    public void removerEmprestimoAtivo(Emprestimo emprestimo) {
        if (this.emprestimosAtivos != null) {
            this.emprestimosAtivos.remove(emprestimo);
        }
    }

    /**
     * Adiciona uma multa pendente ao usuário.
     */
    public void adicionarMultaPendente(Multa multa) {
        if (this.multasPendentes == null) {
            this.multasPendentes = new ArrayList<>();
        }
        this.multasPendentes.add(multa);
    }

    /**
     * Remove uma multa pendente do usuário (ex: ao quitá-la).
     */
    public void removerMultaPendente(Multa multa) {
        if (this.multasPendentes != null) {
            this.multasPendentes.remove(multa);
        }
    }

    /**
     * Retorna {@code true} quando o usuário está inativo,
     * possui multa pendente ou possui empréstimo atrasado.
     *
     * @see design 2.2
     * @see tasks T1.5
     */
    public boolean estaBloqueado() {
        return !ativo
            || !multasPendentes.isEmpty()
            || emprestimosAtivos.stream().anyMatch(Emprestimo::estaAtrasado);
    }

    /**
     * Retorna a quantidade de empréstimos ativos do usuário.
     *
     * @see design 2.2
     * @see tasks T1.5
     */
    public int quantidadeEmprestimosAtivos() {
        return emprestimosAtivos == null ? 0 : emprestimosAtivos.size();
    }
}
