package org.example.biblioteca.domain.factory;

import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes da Factory de entidades de domínio (T5.4).
 * {@see design 3.3}
 * {@see tasks T2.10, T5.4}
 */
class DominioFactoryTest {

    @Test
    @DisplayName("criarUsuario cria usuário ativo com listas vazias")
    void criarUsuario() {
        Usuario u = DominioFactory.criarUsuario("Ana", "ana@email.com", "M001", TipoUsuario.ALUNO);
        assertEquals("Ana", u.getNome());
        assertEquals("ana@email.com", u.getEmail());
        assertEquals("M001", u.getMatricula());
        assertEquals(TipoUsuario.ALUNO, u.getTipo());
        assertTrue(u.isAtivo());
        assertTrue(u.getEmprestimosAtivos().isEmpty());
        assertTrue(u.getMultasPendentes().isEmpty());
        assertTrue(u.getReservas().isEmpty());
    }

    @Test
    @DisplayName("criarTitulo cria título ativo")
    void criarTitulo() {
        Titulo t = DominioFactory.criarTitulo("Dom Casmurro", "Machado de Assis", "978-85-0000000-0");
        assertEquals("Dom Casmurro", t.getNome());
        assertEquals("Machado de Assis", t.getAutor());
        assertEquals("978-85-0000000-0", t.getIsbn());
        assertTrue(t.isAtivo());
        assertTrue(t.getExemplares().isEmpty());
    }

    @Test
    @DisplayName("criarExemplar cria exemplar disponível e ativo")
    void criarExemplar() {
        Titulo t = DominioFactory.criarTitulo("T", "A", "isbn");
        Exemplar e = DominioFactory.criarExemplar("TOMBO-001", t);
        assertEquals("TOMBO-001", e.getCodigoTombo());
        assertEquals(t, e.getTitulo());
        assertEquals(StatusExemplar.DISPONIVEL, e.getStatus());
        assertTrue(e.isAtivo());
        assertTrue(e.estaDisponivel());
    }

    @Test
    @DisplayName("criarEmprestimo cria empréstimo ativo com data prevista = agora + prazo")
    void criarEmprestimo() {
        Usuario u = DominioFactory.criarUsuario("Ana", "a@e.com", "M001", TipoUsuario.ALUNO);
        Exemplar e = DominioFactory.criarExemplar("T-001", DominioFactory.criarTitulo("T", "A", "isbn"));

        LocalDateTime agora = LocalDateTime.now();
        Emprestimo emp = DominioFactory.criarEmprestimo(u, e, "bibliotecario", 14);

        assertEquals(u, emp.getUsuario());
        assertEquals(e, emp.getExemplar());
        assertEquals("bibliotecario", emp.getOperador());
        assertTrue(emp.isAtivo());
        assertNotNull(emp.getDataEmprestimo());
        assertNotNull(emp.getDataPrevistaDevolucao());
        assertTrue(emp.getDataPrevistaDevolucao().isAfter(agora.plusDays(13)));
        assertTrue(emp.getDataPrevistaDevolucao().isBefore(agora.plusDays(15)));
        assertNull(emp.getMulta());
    }

    @Test
    @DisplayName("criarReserva cria reserva AGUARDANDO com data de reserva")
    void criarReserva() {
        Usuario u = DominioFactory.criarUsuario("Ana", "a@e.com", "M001", TipoUsuario.ALUNO);
        Titulo t = DominioFactory.criarTitulo("T", "A", "isbn");

        LocalDateTime agora = LocalDateTime.now();
        Reserva r = DominioFactory.criarReserva(u, t);

        assertEquals(u, r.getUsuario());
        assertEquals(t, r.getTitulo());
        assertEquals(StatusReserva.AGUARDANDO, r.getStatus());
        assertNotNull(r.getDataReserva());
        assertTrue(r.getDataReserva().isAfter(agora.minusSeconds(5)));
        assertTrue(r.getDataReserva().isBefore(agora.plusSeconds(5)));
    }

    @Test
    @DisplayName("criarMulta cria multa pendente com vencimento")
    void criarMulta() {
        Usuario u = DominioFactory.criarUsuario("Ana", "a@e.com", "M001", TipoUsuario.ALUNO);

        Multa m = DominioFactory.criarMulta(u, BigDecimal.valueOf(6.00), "Atraso de 3 dias = R$ 6,00");

        assertEquals(u, m.getUsuario());
        assertEquals(0, m.getValor().compareTo(BigDecimal.valueOf(6.00)));
        assertEquals(StatusMulta.PENDENTE, m.getStatus());
        assertEquals("Atraso de 3 dias = R$ 6,00", m.getDescricao());
        assertNotNull(m.getDataVencimento());
    }
}
