package org.example.biblioteca.domain.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Registro da retirada de um exemplar por um usuário.
 *
 * @see design 2.5
 * @see tasks T1.8
 */
public class Emprestimo extends Entidade {

    private Usuario usuario;
    private Exemplar exemplar;
    private String operador; // bibliotecário ou "autoatendimento" (auditoria, NF02)
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataPrevistaDevolucao; // conforme tipo do usuário (RF02)
    private LocalDateTime dataRealDevolucao;
    private boolean ativo; // empréstimo não encerrado
    private Multa multa; // opcional, gerada na devolução com atraso

    public Emprestimo() {
    }

    public Emprestimo(Long id, Usuario usuario, Exemplar exemplar, String operador,
                      LocalDateTime dataEmprestimo, LocalDateTime dataPrevistaDevolucao,
                      LocalDateTime dataRealDevolucao, boolean ativo, Multa multa) {
        this.id = id;
        this.usuario = usuario;
        this.exemplar = exemplar;
        this.operador = operador;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataRealDevolucao = dataRealDevolucao;
        this.ativo = ativo;
        this.multa = multa;
    }

    // Getters e setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public LocalDateTime getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDateTime getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDateTime dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDateTime getDataRealDevolucao() {
        return dataRealDevolucao;
    }

    public void setDataRealDevolucao(LocalDateTime dataRealDevolucao) {
        this.dataRealDevolucao = dataRealDevolucao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        this.multa = multa;
    }

    /**
     * Retorna {@code true} se o empréstimo está ativo e a data prevista de devolução já passou.
     *
     * @see design 2.5
     * @see tasks T1.8
     */
    public boolean estaAtrasado() {
        return ativo && dataPrevistaDevolucao != null
            && LocalDateTime.now().isAfter(dataPrevistaDevolucao);
    }

    /**
     * Retorna a quantidade de dias de atraso.
     * Retorna 0 se não estiver atrasado; senão, a diferença em dias entre
     * a data prevista de devolução e agora.
     *
     * @see design 2.5
     * @see tasks T1.8
     */
    public long diasAtraso() {
        if (!estaAtrasado()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataPrevistaDevolucao, LocalDateTime.now());
    }
}
