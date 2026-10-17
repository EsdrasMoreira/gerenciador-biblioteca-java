package org.example.biblioteca.domain.model;

import org.example.biblioteca.domain.enums.StatusMulta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Valor cobrado por atraso ou taxa de reposição por dano/perda.
 * <p>
 * Parâmetros consolidados (spec.md, seção 3.6):
 * <ul>
 *   <li>Multa por atraso: R$ 2,00 por dia (valor = diasAtraso * 2,00).</li>
 *   <li>Taxa de reposição (dano/perda): R$ 50,00 por exemplar.</li>
 * </ul>
 *
 * @see design 2.8
 * @see tasks T1.11
 */
public class Multa extends Entidade {

    private Usuario usuario;
    private Emprestimo emprestimo; // origem (pode ser null para taxa de reposição avulsa)
    private BigDecimal valor; // ex: diasAtraso * 2,00 ou 50,00 (taxa de reposição)
    private StatusMulta status; // PENDENTE, PAGA, CANCELADA
    private LocalDateTime dataVencimento;
    private String descricao; // ex: "Atraso de 3 dias = R$ 6,00" ou "Taxa de reposição - exemplar perdido = R$ 50,00"

    public Multa() {
    }

    public Multa(Long id, Usuario usuario, Emprestimo emprestimo, BigDecimal valor,
                 StatusMulta status, LocalDateTime dataVencimento, String descricao) {
        this.id = id;
        this.usuario = usuario;
        this.emprestimo = emprestimo;
        this.valor = valor;
        this.status = status;
        this.dataVencimento = dataVencimento;
        this.descricao = descricao;
    }

    // Getters e setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusMulta getStatus() {
        return status;
    }

    public void setStatus(StatusMulta status) {
        this.status = status;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
