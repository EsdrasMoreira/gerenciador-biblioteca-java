package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.model.Reserva;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de {@link Reserva}.
 * <p>
 * Mantém a fila de reservas de um título ordenada por data de criação
 * (posição na fila por ordem de criação).
 * Implementação em memória: {@link ReservaRepositoryImpl}.
 *
 * @see tasks T3.3, T3.7, T4.3
 */
public interface ReservaRepository extends CrudRepository<Reserva> {

    /**
     * Retorna as reservas de um título ordenadas por data de reserva (mais antiga primeiro).
     *
     * @param tituloId id do título
     * @return lista de reservas do título ordenada pela fila
     */
    List<Reserva> listarPorTitulo(Long tituloId);

    /**
     * Busca a primeira reserva em status AGUARDANDO de um título (cabeça da fila).
     *
     * @param tituloId id do título
     * @return {@link Optional} contendo a primeira reserva aguardando, ou vazio
     */
    Optional<Reserva> buscarPrimeiraAguardandoPorTitulo(Long tituloId);
}
