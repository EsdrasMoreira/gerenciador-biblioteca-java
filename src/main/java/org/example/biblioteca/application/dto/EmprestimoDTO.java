package org.example.biblioteca.application.dto;

import java.time.LocalDateTime;

/**
 * DTO de {@link org.example.biblioteca.domain.model.Emprestimo}.
 * <p>
 * Registra a retirada de um exemplar por um usuário, incluindo dados
 * resumidos do usuário, exemplar e multa opcional.
 *
 * @see tasks T4.1
 */
public class EmprestimoDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long exemplarId;
    private String exemplarCodigoTombo;
    private String operador;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataPrevistaDevolucao;
    private LocalDateTime dataRealDevolucao;
    private boolean ativo;
    private MultaDTO multa;

    public EmprestimoDTO() {
    }

    public EmprestimoDTO(Long id, Long usuarioId, String usuarioNome,
                         Long exemplarId, String exemplarCodigoTombo,
                         String operador, LocalDateTime dataEmprestimo,
                         LocalDateTime dataPrevistaDevolucao,
                         LocalDateTime dataRealDevolucao, boolean ativo,
                         MultaDTO multa) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.exemplarId = exemplarId;
        this.exemplarCodigoTombo = exemplarCodigoTombo;
        this.operador = operador;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataRealDevolucao = dataRealDevolucao;
        this.ativo = ativo;
        this.multa = multa;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public Long getExemplarId() {
        return exemplarId;
    }

    public void setExemplarId(Long exemplarId) {
        this.exemplarId = exemplarId;
    }

    public String getExemplarCodigoTombo() {
        return exemplarCodigoTombo;
    }

    public void setExemplarCodigoTombo(String exemplarCodigoTombo) {
        this.exemplarCodigoTombo = exemplarCodigoTombo;
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

    public MultaDTO getMulta() {
        return multa;
    }

    public void setMulta(MultaDTO multa) {
        this.multa = multa;
    }

    @Override
    public String toString() {
        return "EmprestimoDTO{"
            + "id=" + id
            + ", usuarioId=" + usuarioId
            + ", usuarioNome='" + usuarioNome + '\''
            + ", exemplarId=" + exemplarId
            + ", exemplarCodigoTombo='" + exemplarCodigoTombo + '\''
            + ", operador='" + operador + '\''
            + ", dataEmprestimo=" + dataEmprestimo
            + ", dataPrevistaDevolucao=" + dataPrevistaDevolucao
            + ", dataRealDevolucao=" + dataRealDevolucao
            + ", ativo=" + ativo
            + ", multa=" + multa
            + '}';
    }
}
