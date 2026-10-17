package org.example.biblioteca.domain.model;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.exception.OperacaoNaoPermitidaException;
import org.example.biblioteca.domain.factory.EstadoExemplarFactory;
import org.example.biblioteca.domain.state.EstadoExemplar;

/**
 * Cópia física de um título, identificada por código de tombo único.
 * Possui máquina de estados (padrão State, integrada na Fase 2 - T2.11).
 * <p>
 * As transições de estado ({@code emprestar}, {@code devolver},
 * {@code enviarParaReparo}, {@code darBaixa}) são delegadas ao objeto de estado
 * atual, evitando blocos {@code if/else} ou {@code switch} de status espalhados.
 *
 * @see design 2.4
 * @see design 3.2
 * @see tasks T1.7
 * @see tasks T2.11
 */
public class Exemplar extends Entidade {

    private String codigoTombo; // único (RF04)
    private Titulo titulo;
    private StatusExemplar status;
    private EstadoExemplar estado; // máquina de estados (State, T2.11)
    private boolean ativo; // cadastro ativo/inativo (RF10)
    private Emprestimo emprestimoAtivo; // empréstimo vigente, se houver

    public Exemplar() {
        this.status = StatusExemplar.DISPONIVEL;
        this.estado = EstadoExemplarFactory.criar(StatusExemplar.DISPONIVEL);
    }

    public Exemplar(Long id, String codigoTombo, Titulo titulo,
                    StatusExemplar status, boolean ativo) {
        this.id = id;
        this.codigoTombo = codigoTombo;
        this.titulo = titulo;
        this.status = status;
        this.estado = EstadoExemplarFactory.criar(status);
        this.ativo = ativo;
    }

    // Getters e setters

    public String getCodigoTombo() {
        return codigoTombo;
    }

    public void setCodigoTombo(String codigoTombo) {
        this.codigoTombo = codigoTombo;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public StatusExemplar getStatus() {
        return status;
    }

    public void setStatus(StatusExemplar status) {
        this.status = status;
        this.estado = EstadoExemplarFactory.criar(status);
    }

    /**
     * Retorna o objeto de estado atual da máquina de estados do exemplar.
     *
     * @return estado atual (State)
     */
    public EstadoExemplar getEstado() {
        return estado;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Emprestimo getEmprestimoAtivo() {
        return emprestimoAtivo;
    }

    public void setEmprestimoAtivo(Emprestimo emprestimoAtivo) {
        this.emprestimoAtivo = emprestimoAtivo;
    }

    /**
     * Somente está disponível se o status for DISPONIVEL e o exemplar estiver ativo.
     *
     * @see design 2.4
     * @see tasks T1.7
     */
    public boolean estaDisponivel() {
        return status == StatusExemplar.DISPONIVEL && ativo;
    }

    /**
     * Indica se o exemplar está indisponível por motivo de estado
     * (EM_REPARO, DANIFICADO ou PERDIDO).
     *
     * @see design 2.4
     * @see tasks T1.7
     */
    public boolean estaIndisponivelPorEstado() {
        return status == StatusExemplar.EM_REPARO
            || status == StatusExemplar.DANIFICADO
            || status == StatusExemplar.PERDIDO;
    }

    /**
     * Solicita o empréstimo do exemplar. Delegado ao estado atual.
     * Só é permitido quando o exemplar está disponível.
     *
     * @see design 2.4
     * @see tasks T2.11
     */
    /**
     * Solicita o empréstimo do exemplar. Delegado ao estado atual.
     * Só é permitido quando o exemplar está disponível.
     *
     * @see design 2.4
     * @see tasks T2.11
     */
    public void emprestar() throws OperacaoNaoPermitidaException {
        estado.executarEmprestimo(this);
    }

    /**
     * Registra a devolução do exemplar. Delegado ao estado atual.
     *
     * @param danificado indica se o exemplar foi devolvido danificado
     * @param perdido    indica se o exemplar foi devolvido como perdido
     * @see design 2.4
     * @see tasks T2.11
     */
    public void devolver(boolean danificado, boolean perdido) throws OperacaoNaoPermitidaException {
        estado.executarDevolucao(this, danificado, perdido);
    }

    /**
     * Envia o exemplar para reparo. Delegado ao estado atual.
     *
     * @see design 2.4
     * @see tasks T2.11
     */
    public void enviarParaReparo() throws OperacaoNaoPermitidaException {
        estado.executarEnviarReparo(this);
    }

    /**
     * Dá baixa no exemplar (remove do acervo disponível). Delegado ao estado atual.
     *
     * @see design 2.4
     * @see tasks T2.11
     */
    public void darBaixa() throws OperacaoNaoPermitidaException {
        estado.executarBaixa(this);
    }
}
