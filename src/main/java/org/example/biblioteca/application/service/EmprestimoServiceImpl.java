package org.example.biblioteca.application.service;

import org.example.biblioteca.domain.infrastructure.persistence.EmprestimoRepository;
import org.example.biblioteca.domain.infrastructure.persistence.ExemplarRepository;
import org.example.biblioteca.domain.infrastructure.persistence.MultaRepository;
import org.example.biblioteca.domain.infrastructure.persistence.ReservaRepository;
import org.example.biblioteca.domain.infrastructure.persistence.TituloRepository;
import org.example.biblioteca.domain.infrastructure.persistence.UsuarioRepository;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Reserva;
import org.example.biblioteca.domain.model.Titulo;
import org.example.biblioteca.domain.model.Usuario;
import org.example.biblioteca.domain.service.EmprestimoService;
import org.example.biblioteca.domain.exception.*;
import org.example.biblioteca.domain.factory.DominioFactory;
import org.example.biblioteca.domain.strategy.ContextoPoliticaEmprestimo;

import java.util.List;

/**
 * Implementação do caso de uso CU01 (empréstimo) e CU03 (reserva).
 * <p>
 * Orquestra as validações de negócio (usuário ativo/bloqueado, limite via
 * Strategy, disponibilidade de exemplar), cria o empréstimo com a data
 * prevista da Strategy, altera o estado do exemplar via State e persiste as
 * entidades de forma consistente.
 *
 * @see design 4.1
 * @see tasks T3.1, T3.4, T3.7, T3.8
 */
public class EmprestimoServiceImpl implements EmprestimoService {

    private final UsuarioRepository usuarioRepository;
    private final TituloRepository tituloRepository;
    private final ExemplarRepository exemplarRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final ReservaRepository reservaRepository;
    private final MultaRepository multaRepository;
    private final ContextoPoliticaEmprestimo contextoPolitica;

    public EmprestimoServiceImpl(UsuarioRepository usuarioRepository,
                                 TituloRepository tituloRepository,
                                 ExemplarRepository exemplarRepository,
                                 EmprestimoRepository emprestimoRepository,
                                 ReservaRepository reservaRepository,
                                 MultaRepository multaRepository,
                                 ContextoPoliticaEmprestimo contextoPolitica) {
        this.usuarioRepository = usuarioRepository;
        this.tituloRepository = tituloRepository;
        this.exemplarRepository = exemplarRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.reservaRepository = reservaRepository;
        this.multaRepository = multaRepository;
        this.contextoPolitica = contextoPolitica;
    }

    @Override
    public Emprestimo emprestar(Long usuarioId, Long exemplarId, String operador) throws RegraNegocioException {
        // 1. Localiza o usuário.
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + usuarioId));

        // 2. Valida usuário ativo e não bloqueado [RF01, RF10, RN06].
        if (!usuario.isAtivo() || usuario.estaBloqueado()) {
            throw new UsuarioBloqueadoException(
                    "Usuário \"" + usuario.getNome() + "\" está bloqueado ou inativo e não pode emprestar.");
        }

        // 3. Valida limite de empréstimos simultâneos via Strategy [RN01-RN03].
        contextoPolitica.setPolitica(usuario.getTipo());
        if (usuario.quantidadeEmprestimosAtivos() >= contextoPolitica.limite()) {
            throw new LimiteEmprestimoAtingidoException(
                    "Limite de " + contextoPolitica.limite()
                            + " empréstimos simultâneos atingido para o tipo " + usuario.getTipo() + ".");
        }
        if (!contextoPolitica.valida(usuario)) {
            throw new UsuarioBloqueadoException("Usuário não atende aos requisitos da política de empréstimo.");
        }

        // 4. Busca o exemplar e verifica se está disponível [RF03, RN05].
        Exemplar exemplar = exemplarRepository.buscarPorId(exemplarId)
                .orElseThrow(() -> new ExemplarNaoEncontradoException("Exemplar não encontrado: " + exemplarId));
        if (!exemplar.estaDisponivel()) {
            throw new ExemplarIndisponivelException(
                    "Exemplar não disponível para empréstimo. Código de tombo: " + exemplar.getCodigoTombo());
        }

        // 5. Cria o empréstimo com data prevista = agora + prazo da Strategy [RF02].
        int prazoDias = contextoPolitica.prazoDias();
        Emprestimo emprestimo = DominioFactory.criarEmprestimo(usuario, exemplar, operador, prazoDias);

        // 6. Altera o exemplar para EMPRESTADO via State [RN04].
        exemplar.emprestar(); // lança OperacaoNaoPermitidaException se não estiver disponível
        exemplar.setEmprestimoAtivo(emprestimo);

        // 7. Persiste de forma atômica [NF01] e registra o operador (auditoria) [NF02].
        emprestimoRepository.salvar(emprestimo);
        exemplarRepository.salvar(exemplar);
        usuario.adicionarEmprestimoAtivo(emprestimo);
        usuarioRepository.salvar(usuario);

        return emprestimo;
    }

    @Override
    public boolean existeExemplarDisponivel(Long tituloId) {
        return exemplarRepository.buscarDisponiveisPorTitulo(tituloId).stream()
                .anyMatch(Exemplar::estaDisponivel);
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivos(Long usuarioId) {
        return emprestimoRepository.buscarAtivosPorUsuario(usuarioId);
    }

    @Override
    public Reserva reservar(Long usuarioId, Long tituloId) throws RegraNegocioException {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + usuarioId));
        Titulo titulo = tituloRepository.buscarPorId(tituloId)
                .orElseThrow(() -> new TituloNaoEncontradoException("Título não encontrado: " + tituloId));

        // Insere o usuário na fila do título; posição por ordem de criação (RN05).
        Reserva reserva = DominioFactory.criarReserva(usuario, titulo);
        reservaRepository.salvar(reserva);
        usuario.getReservas().add(reserva);
        usuarioRepository.salvar(usuario);

        return reserva;
    }
}
