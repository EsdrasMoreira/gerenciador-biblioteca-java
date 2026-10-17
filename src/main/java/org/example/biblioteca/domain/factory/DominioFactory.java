package org.example.biblioteca.domain.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.example.biblioteca.domain.enums.StatusExemplar;
import org.example.biblioteca.domain.enums.StatusMulta;
import org.example.biblioteca.domain.enums.StatusReserva;
import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Reserva;
import org.example.biblioteca.domain.model.Titulo;
import org.example.biblioteca.domain.model.Usuario;

/**
 * Factory de entidades de domínio (padrão Factory).
 * <p>
 * Centraliza a criação de objetos de domínio com valores default consistentes,
 * desacoplando o cliente das classes concretas e facilitando a manutenção.
 *
 * @see design 3.3
 * @see tasks T2.10
 */
public class DominioFactory {

    /**
     * Cria um usuário ativo com o tipo informado.
     *
     * @param nome     nome do usuário
     * @param email    e-mail do usuário
     * @param matricula matrícula única do usuário
     * @param tipo     tipo do usuário (ALUNO, PROFESSOR ou FUNCIONARIO)
     * @return usuário criado, ativo
     */
    public static Usuario criarUsuario(String nome, String email, String matricula, TipoUsuario tipo) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setMatricula(matricula);
        usuario.setTipo(tipo);
        usuario.setAtivo(true);
        return usuario;
    }

    /**
     * Cria um título ativo.
     *
     * @param nome  nome da obra
     * @param autor autor da obra
     * @param isbn  ISBN da obra
     * @return título criado, ativo
     */
    public static Titulo criarTitulo(String nome, String autor, String isbn) {
        Titulo titulo = new Titulo();
        titulo.setNome(nome);
        titulo.setAutor(autor);
        titulo.setIsbn(isbn);
        titulo.setAtivo(true);
        return titulo;
    }

    /**
     * Cria um exemplar disponível e ativo vinculado a um título.
     *
     * @param codigoTombo código de tombo único do exemplar
     * @param titulo      título ao qual o exemplar pertence
     * @return exemplar criado, com status DISPONIVEL e ativo
     */
    public static Exemplar criarExemplar(String codigoTombo, Titulo titulo) {
        Exemplar exemplar = new Exemplar();
        exemplar.setCodigoTombo(codigoTombo);
        exemplar.setTitulo(titulo);
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        exemplar.setAtivo(true);
        return exemplar;
    }

    /**
     * Cria um empréstimo ativo com data prevista de devolução baseada no prazo informado.
     *
     * @param usuario   usuário que retira o exemplar
     * @param exemplar  exemplar emprestado
     * @param operador  identificador do operador (bibliotecário ou "autoatendimento")
     * @param prazoDias prazo de retenção em dias (vindo da Strategy)
     * @return empréstimo criado, ativo, com data prevista = agora + prazoDias
     */
    public static Emprestimo criarEmprestimo(Usuario usuario, Exemplar exemplar,
                                             String operador, int prazoDias) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setExemplar(exemplar);
        emprestimo.setOperador(operador);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataPrevistaDevolucao(LocalDateTime.now().plusDays(prazoDias));
        emprestimo.setAtivo(true);
        return emprestimo;
    }

    /**
     * Cria uma reserva em estado AGUARDANDO para um título.
     *
     * @param usuario usuário que reserva
     * @param titulo  título reservado
     * @return reserva criada, com status AGUARDANDO e data de reserva = agora
     */
    public static Reserva criarReserva(Usuario usuario, Titulo titulo) {
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setTitulo(titulo);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setStatus(StatusReserva.AGUARDANDO);
        return reserva;
    }

    /**
     * Cria uma multa pendente.
     *
     * @param usuario  usuário devedor
     * @param valor    valor da multa (ex: diasAtraso * 2,00 ou 50,00 de taxa de reposição)
     * @param descricao descrição da multa
     * @return multa criada, com status PENDENTE
     */
    public static Multa criarMulta(Usuario usuario, BigDecimal valor, String descricao) {
        Multa multa = new Multa();
        multa.setUsuario(usuario);
        multa.setValor(valor);
        multa.setStatus(StatusMulta.PENDENTE);
        multa.setDescricao(descricao);
        multa.setDataVencimento(LocalDateTime.now().plusDays(7));
        return multa;
    }
}
