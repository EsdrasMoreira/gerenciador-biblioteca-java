package org.example.biblioteca.domain.factory;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.state.EstadoDanificado;
import org.example.biblioteca.domain.state.EstadoDisponivel;
import org.example.biblioteca.domain.state.EstadoEmprestado;
import org.example.biblioteca.domain.state.EstadoEmReparo;
import org.example.biblioteca.domain.state.EstadoExemplar;
import org.example.biblioteca.domain.state.EstadoPerdido;

/**
 * Factory de estados do exemplar (padrão Factory + State).
 * <p>
 * Centraliza a criação de objetos de estado conforme o {@link StatusExemplar},
 * desacoplando o cliente das classes concretas de estado.
 *
 * @see design 3.3
 * @see tasks T2.9
 */
public class EstadoExemplarFactory {

    /**
     * Cria o objeto de estado correspondente ao status do exemplar informado.
     *
     * @param status status do exemplar (DISPONIVEL, EMPRESTADO, EM_REPARO, DANIFICADO, PERDIDO)
     * @return estado concreto do exemplar
     * @throws IllegalArgumentException se o status não for reconhecido
     */
    public static EstadoExemplar criar(StatusExemplar status) {
        return switch (status) {
            case DISPONIVEL -> new EstadoDisponivel();
            case EMPRESTADO -> new EstadoEmprestado();
            case EM_REPARO -> new EstadoEmReparo();
            case DANIFICADO -> new EstadoDanificado();
            case PERDIDO -> new EstadoPerdido();
        };
    }
}
