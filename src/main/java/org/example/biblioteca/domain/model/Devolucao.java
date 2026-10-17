package org.example.biblioteca.domain.model;

import java.time.LocalDateTime;

/**
 * Registro da entrega de um exemplar emprestado (pode gerar multa).
 *
 * @see design 2.6
 * @see tasks T1.9
 */
public class Devolucao extends Entidade {

    private Emprestimo emprestimo;
    private LocalDateTime dataDevolucao;
    private boolean exemplarDanificado;
    private boolean exemplarPerdido;
    private Multa multaGerada; // null se não houver atraso

    public Devolucao() {
    }

    public Devolucao(Long id, Emprestimo emprestimo, LocalDateTime dataDevolucao,
                     boolean exemplarDanificado, boolean exemplarPerdido, Multa multaGerada) {
        this.id = id;
        this.emprestimo = emprestimo;
        this.dataDevolucao = dataDevolucao;
        this.exemplarDanificado = exemplarDanificado;
        this.exemplarPerdido = exemplarPerdido;
        this.multaGerada = multaGerada;
    }

    // Getters e setters

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isExemplarDanificado() {
        return exemplarDanificado;
    }

    public void setExemplarDanificado(boolean exemplarDanificado) {
        this.exemplarDanificado = exemplarDanificado;
    }

    public boolean isExemplarPerdido() {
        return exemplarPerdido;
    }

    public void setExemplarPerdido(boolean exemplarPerdido) {
        this.exemplarPerdido = exemplarPerdido;
    }

    public Multa getMultaGerada() {
        return multaGerada;
    }

    public void setMultaGerada(Multa multaGerada) {
        this.multaGerada = multaGerada;
    }
}
