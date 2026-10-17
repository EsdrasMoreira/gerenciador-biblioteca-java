package org.example.biblioteca.domain.service;

import org.example.biblioteca.domain.exception.RegraNegocioException;
import org.example.biblioteca.domain.model.Devolucao;
import org.example.biblioteca.domain.model.Multa;

import java.math.BigDecimal;

/**
 * Interface do caso de uso CU02 (devolução).
 * <p>
 * Define o contrato para devolver um empréstimo, calcular multa por atraso
 * (R$ 2,00/dia) e aplicar taxa de reposição (R$ 50,00) para exemplar
 * danificado ou perdido.
 *
 * @see design 4.2
 * @see tasks T3.5, T3.6
 */
public interface DevolucaoService {

    /**
     * Efetua a devolução de um empréstimo ativo.
     * <p>
     * Se houver atraso, gera multa de R$ 2,00 por dia de atraso e o usuário
     * fica bloqueado (RF05, RF06). A transição de estado do exemplar segue a
     * máquina de estados (RF07, RF08, RN07): OK → DISPONIVEL;
     * danificado/perdido → DANIFICADO/PERDIDO + taxa de reposição R$ 50,00 +
     * baixa. Notifica o primeiro da fila de reserva, se houver (CU03).
     *
     * @param emprestimoId id do empréstimo ativo
     * @param operador identificador do operador (auditoria)
     * @param danificado indica se o exemplar foi devolvido danificado
     * @param perdido indica se o exemplar foi devolvido como perdido
     * @return registro da devolução realizada
     * @throws RegraNegocioException se o empréstimo não for encontrado ou já estiver devolvido
     */
    Devolucao devolver(Long emprestimoId, String operador, boolean danificado, boolean perdido)
            throws RegraNegocioException;

    /**
     * Calcula o valor da multa por atraso de um empréstimo, sem efetivar a
     * devolução (consulta apenas).
     *
     * @param emprestimoId id do empréstimo
     * @return valor da multa (R$ 2,00 por dia de atraso), ou {@code 0} se não houver atraso
     * @throws RegraNegocioException se o empréstimo não for encontrado
     */
    BigDecimal calcularMultaAtraso(Long emprestimoId) throws RegraNegocioException;
}
