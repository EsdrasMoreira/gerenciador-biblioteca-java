package org.example.biblioteca.application.service;

import org.example.biblioteca.domain.infrastructure.persistence.EmprestimoRepository;
import org.example.biblioteca.domain.infrastructure.persistence.ExemplarRepository;
import org.example.biblioteca.domain.infrastructure.persistence.MultaRepository;
import org.example.biblioteca.domain.infrastructure.persistence.ReservaRepository;
import org.example.biblioteca.domain.infrastructure.persistence.TituloRepository;
import org.example.biblioteca.domain.infrastructure.persistence.UsuarioRepository;
import org.example.biblioteca.domain.model.Devolucao;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Reserva;
import org.example.biblioteca.domain.model.Titulo;
import org.example.biblioteca.domain.model.Usuario;
import org.example.biblioteca.domain.enums.StatusReserva;
import org.example.biblioteca.domain.service.DevolucaoService;
import org.example.biblioteca.domain.exception.*;
import org.example.biblioteca.domain.factory.DominioFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação do caso de uso CU02 (devolução).
 * <p>
 * Localiza o empréstimo ativo, calcula e gera multa por atraso
 * (R$ 2,00/dia), realiza a transição de estado do exemplar via State
 * (OK → DISPONIVEL; danificado/perdido → DANIFICADO/PERDIDO + taxa de
 * reposição R$ 50,00 + baixa) e notifica o primeiro da fila de reserva,
 * quando houver (expiração de 3 dias corridos).
 *
 * @see design 4.2
 * @see tasks T3.5, T3.6
 */
public class DevolucaoServiceImpl implements DevolucaoService {

    private static final BigDecimal VALOR_POR_DIA_ATRASO = BigDecimal.valueOf(2.00);
    private static final BigDecimal TAXA_REPOSICAO = BigDecimal.valueOf(50.00);

    private final UsuarioRepository usuarioRepository;
    private final TituloRepository tituloRepository;
    private final ExemplarRepository exemplarRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final ReservaRepository reservaRepository;
    private final MultaRepository multaRepository;

    public DevolucaoServiceImpl(UsuarioRepository usuarioRepository,
                                TituloRepository tituloRepository,
                                ExemplarRepository exemplarRepository,
                                EmprestimoRepository emprestimoRepository,
                                ReservaRepository reservaRepository,
                                MultaRepository multaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tituloRepository = tituloRepository;
        this.exemplarRepository = exemplarRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.reservaRepository = reservaRepository;
        this.multaRepository = multaRepository;
    }

    @Override
    public Devolucao devolver(Long emprestimoId, String operador, boolean danificado, boolean perdido)
            throws RegraNegocioException {
        // 1. Localiza o empréstimo ativo.
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(emprestimoId)
                .orElseThrow(() -> new EmprestimoNaoEncontradoException("Empréstimo não encontrado: " + emprestimoId));
        if (!emprestimo.isAtivo()) {
            throw new OperacaoNaoPermitidaException("Empréstimo já foi devolvido: " + emprestimoId);
        }

        Usuario usuario = emprestimo.getUsuario();
        Exemplar exemplar = emprestimo.getExemplar();
        Titulo titulo = exemplar.getTitulo();
        LocalDateTime agora = LocalDateTime.now();

        // 2. Se atrasado, gera multa de R$ 2,00 por dia e o usuário fica bloqueado [RF05, RF06].
        Multa multaAtraso = null;
        if (emprestimo.estaAtrasado()) {
            long dias = emprestimo.diasAtraso();
            BigDecimal valor = VALOR_POR_DIA_ATRASO.multiply(BigDecimal.valueOf(dias));
            multaAtraso = DominioFactory.criarMulta(usuario, valor,
                    "Atraso de " + dias + " dia(s) = R$ " + valor);
            multaRepository.salvar(multaAtraso);
            usuario.adicionarMultaPendente(multaAtraso);
        }

        // 3. Transição de estado do exemplar via State [RF07, RF08, RN07].
        //    OK      -> DISPONIVEL
        //    danificado -> DANIFICADO + taxa R$ 50,00 + baixa
        //    perdido   -> PERDIDO + taxa R$ 50,00 + baixa
        exemplar.devolver(danificado, perdido);

        Multa multaReposicao = null;
        if (danificado || perdido) {
            multaReposicao = DominioFactory.criarMulta(usuario, TAXA_REPOSICAO,
                    "Taxa de reposição - exemplar " + (perdido ? "perdido" : "danificado") + " = R$ 50,00");
            multaRepository.salvar(multaReposicao);
            usuario.adicionarMultaPendente(multaReposicao);
        }

        // 4. Fecha o empréstimo.
        emprestimo.setAtivo(false);
        emprestimo.setDataRealDevolucao(agora);
        emprestimo.setMulta(multaAtraso != null ? multaAtraso : multaReposicao);
        exemplar.setEmprestimoAtivo(null);

        // 5. Persiste as alterações.
        emprestimoRepository.salvar(emprestimo);
        exemplarRepository.salvar(exemplar);
        usuario.removerEmprestimoAtivo(emprestimo);
        usuarioRepository.salvar(usuario);

        // 6. Registra a devolução.
        Devolucao devolucao = new Devolucao();
        devolucao.setEmprestimo(emprestimo);
        devolucao.setDataDevolucao(agora);
        devolucao.setExemplarDanificado(danificado);
        devolucao.setExemplarPerdido(perdido);
        devolucao.setMultaGerada(multaAtraso != null ? multaAtraso : multaReposicao);

        // 7. Notifica o primeiro da fila de reserva do título (expiração em 3 dias) [CU03, spec 3.6].
        notificarPrimeiraReserva(titulo);

        return devolucao;
    }

    @Override
    public BigDecimal calcularMultaAtraso(Long emprestimoId) throws RegraNegocioException {
        // Consulta apenas, não efetiva a devolução.
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(emprestimoId)
                .orElseThrow(() -> new EmprestimoNaoEncontradoException("Empréstimo não encontrado: " + emprestimoId));
        if (!emprestimo.estaAtrasado()) {
            return BigDecimal.ZERO;
        }
        return VALOR_POR_DIA_ATRASO.multiply(BigDecimal.valueOf(emprestimo.diasAtraso()));
    }

    /**
     * Notifica a primeira reserva AGUARDANDO do título, definindo status
     * NOTIFICADA, data de notificação e expiração em 3 dias corridos.
     *
     * @param titulo título cujo exemplar acabou de ficar disponível
     * @throws RegraNegocioException se o título não for encontrado
     */
    private void notificarPrimeiraReserva(Titulo titulo) throws RegraNegocioException {
        List<Reserva> fila = reservaRepository.listarPorTitulo(titulo.getId());
        Reserva primeira = fila.stream()
                .filter(r -> r.getStatus() == StatusReserva.AGUARDANDO)
                .findFirst()
                .orElse(null);
        if (primeira != null) {
            primeira.setStatus(StatusReserva.NOTIFICADA);
            primeira.setDataNotificacao(LocalDateTime.now());
            primeira.setDataExpiracao(LocalDateTime.now().plusDays(3));
            reservaRepository.salvar(primeira);
        }
    }
}
