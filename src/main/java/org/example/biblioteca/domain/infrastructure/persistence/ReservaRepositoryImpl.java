package org.example.biblioteca.domain.infrastructure.persistence;

import org.example.biblioteca.domain.enums.StatusReserva;
import org.example.biblioteca.domain.model.Reserva;

import java.util.List;
import java.util.Optional;

/**
 * Repositório em memória de {@link Reserva}.
 * <p>
 * Mantém a fila de reservas de um título ordenada por data de criação
 * (posição na fila por ordem de criação, RN05).
 *
 * @see tasks T3.3, T3.7
 */
public class ReservaRepositoryImpl extends AbstractCrudRepository<Reserva> implements ReservaRepository {

    /**
     * Retorna as reservas de um título ordenadas por data de reserva (mais antiga primeiro).
     *
     * @param tituloId id do título
     * @return lista de reservas do título ordenada pela fila
     */
    public List<Reserva> listarPorTitulo(Long tituloId) {
        return listarTodos().stream()
                .filter(r -> tituloId != null && tituloId.equals(tituloId(r)))
                .sorted((r1, r2) -> r1.getDataReserva().compareTo(r2.getDataReserva()))
                .toList();
    }

    /**
     * Busca a primeira reserva em status AGUARDANDO de um título (cabeça da fila).
     *
     * @param tituloId id do título
     * @return {@link Optional} contendo a primeira reserva aguardando, ou vazio
     */
    public Optional<Reserva> buscarPrimeiraAguardandoPorTitulo(Long tituloId) {
        return listarPorTitulo(tituloId).stream()
                .filter(r -> r.getStatus() == StatusReserva.AGUARDANDO)
                .findFirst();
    }

    private Long tituloId(Reserva reserva) {
        return reserva.getTitulo() != null ? reserva.getTitulo().getId() : null;
    }
}
