package org.example.biblioteca.domain.service;

import org.example.biblioteca.domain.exception.RegraNegocioException;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Reserva;

import java.util.List;

/**
 * Interface do caso de uso CU01 (empréstimo) e CU03 (reserva).
 * <p>
 * Define o contrato para realizar empréstimos de exemplares, consultar
 * disponibilidade, listar empréstimos ativos de um usuário e enfileirar reservas.
 *
 * @see design 4.1
 * @see tasks T3.1, T3.7
 */
public interface EmprestimoService {

    /**
     * Realiza o empréstimo de um exemplar para um usuário.
     * <p>
     * Validações executadas:
     * <ul>
     *   <li>usuário ativo e não bloqueado (RF01, RF10, RN06);</li>
     *   <li>limite de empréstimos simultâneos via Strategy (RN01-RN03);</li>
     *   <li>exemplar disponível (RF03, RN05); caso contrário, lança
     *   {@code ExemplarIndisponivelException} e o cliente pode recorrer a {@link #reservar(Long, Long)}.</li>
     * </ul>
     *
     * @param usuarioId id do usuário
     * @param exemplarId id do exemplar a ser emprestado
     * @param operador identificador do operador (bibliotecário ou "autoatendimento") - auditoria (NF02)
     * @return empréstimo criado, com data prevista de devolução preenchida (RF02)
     * @throws RegraNegocioException se alguma regra de negócio for violada
     */
    Emprestimo emprestar(Long usuarioId, Long exemplarId, String operador)
            throws RegraNegocioException;

    /**
     * Indica se existe pelo menos um exemplar disponível e ativo para o título.
     *
     * @param tituloId id do título
     * @return {@code true} se há exemplar disponível, {@code false} caso contrário
     */
    boolean existeExemplarDisponivel(Long tituloId);

    /**
     * Lista os empréstimos ativos (não devolvidos) de um usuário.
     *
     * @param usuarioId id do usuário
     * @return lista de empréstimos ativos
     */
    List<Emprestimo> listarEmprestimosAtivos(Long usuarioId);

    /**
     * Insere o usuário na fila de reserva do título, por ordem de criação (RN05).
     *
     * @param usuarioId id do usuário
     * @param tituloId id do título
     * @return reserva criada, com status AGUARDANDO
     * @throws RegraNegocioException se usuário ou título não forem encontrados
     */
    Reserva reservar(Long usuarioId, Long tituloId) throws RegraNegocioException;
}
