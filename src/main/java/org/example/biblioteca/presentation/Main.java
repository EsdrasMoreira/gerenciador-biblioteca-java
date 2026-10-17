package org.example.biblioteca.presentation;

import org.example.biblioteca.application.dto.DevolucaoDTO;
import org.example.biblioteca.application.dto.EmprestimoDTO;
import org.example.biblioteca.application.dto.ExemplarDTO;
import org.example.biblioteca.application.dto.MultaDTO;
import org.example.biblioteca.application.dto.ReservaDTO;
import org.example.biblioteca.application.dto.TituloDTO;
import org.example.biblioteca.application.dto.UsuarioDTO;
import org.example.biblioteca.application.mapper.DevolucaoMapper;
import org.example.biblioteca.application.mapper.EmprestimoMapper;
import org.example.biblioteca.application.mapper.ExemplarMapper;
import org.example.biblioteca.application.mapper.MultaMapper;
import org.example.biblioteca.application.mapper.ReservaMapper;
import org.example.biblioteca.application.mapper.TituloMapper;
import org.example.biblioteca.application.mapper.UsuarioMapper;
import org.example.biblioteca.domain.enums.TipoUsuario;
import org.example.biblioteca.domain.exception.RegraNegocioException;
import org.example.biblioteca.domain.factory.DominioFactory;
import org.example.biblioteca.domain.infrastructure.persistence.*;
import org.example.biblioteca.domain.model.Emprestimo;
import org.example.biblioteca.domain.model.Exemplar;
import org.example.biblioteca.domain.model.Multa;
import org.example.biblioteca.domain.model.Titulo;
import org.example.biblioteca.domain.model.Usuario;
import org.example.biblioteca.application.service.DevolucaoServiceImpl;
import org.example.biblioteca.application.service.EmprestimoServiceImpl;
import org.example.biblioteca.domain.service.DevolucaoService;
import org.example.biblioteca.domain.service.EmprestimoService;
import org.example.biblioteca.domain.strategy.ContextoPoliticaEmprestimo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Classe de apresentação (CLI) interativa do Gerenciador de Biblioteca.
 * <p>
 * Exercita os casos de uso CU01 (empréstimo), CU02 (devolução) e CU03 (reserva),
 * cobrendo os critérios de aceitação do spec.md seção 8:
 * <ul>
 *   <li>Cadastro de usuários (aluno/professor/funcionário) e de títulos/exemplares.</li>
 *   <li>Empréstimo dentro do limite; recusa quando indisponível com criação de reserva.</li>
 *   <li>Devolução com cálculo de multa por atraso (R$ 2,00/dia).</li>
 *   <li>Exemplar danificado/perdido: taxa de reposição R$ 50,00 + baixa.</li>
 *   <li>Tratamento de exceções de negócio com mensagens claras no console.</li>
 * </ul>
 *
 * @see tasks T4.4
 */
public class Main {

    private final Scanner scanner = new Scanner(System.in);

    // Camada de persistência em memória (thread-safe, substituível por JPA+H2)
    private final UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();
    private final TituloRepository tituloRepository = new TituloRepositoryImpl();
    private final ExemplarRepository exemplarRepository = new ExemplarRepositoryImpl();
    private final EmprestimoRepository emprestimoRepository = new EmprestimoRepositoryImpl();
    private final ReservaRepository reservaRepository = new ReservaRepositoryImpl();
    private final MultaRepository multaRepository = new MultaRepositoryImpl();

    // Camada de aplicação (serviços de caso de uso)
    private final ContextoPoliticaEmprestimo contextoPolitica = new ContextoPoliticaEmprestimo();
    private final EmprestimoService emprestimoService = new EmprestimoServiceImpl(
            usuarioRepository, tituloRepository, exemplarRepository,
            emprestimoRepository, reservaRepository, multaRepository, contextoPolitica);
    private final DevolucaoService devolucaoService = new DevolucaoServiceImpl(
            usuarioRepository, tituloRepository, exemplarRepository,
            emprestimoRepository, reservaRepository, multaRepository);

    public static void main(String[] args) {
        Main app = new Main();
        app.sementeInicial();
        app.executarMenu();
    }

    /**
     * Popula dados iniciais para que o CLI possa ser executado em modo demonstração
     * sem necessidade de cadastro manual de tudo.
     */
    private void sementeInicial() {
        // Usuários
        Usuario aluno = DominioFactory.criarUsuario("Ana Silva", "ana@email.com", "2024001", TipoUsuario.ALUNO);
        Usuario professor = DominioFactory.criarUsuario("Carlos Professor", "carlos@email.com", "2024002", TipoUsuario.PROFESSOR);
        usuarioRepository.salvar(aluno);
        usuarioRepository.salvar(professor);

        // Títulos e exemplares
        Titulo t1 = DominioFactory.criarTitulo("Dom Casmurro", "Machado de Assis", "978-85-1234567-8");
        tituloRepository.salvar(t1);
        exemplarRepository.salvar(DominioFactory.criarExemplar("TOM-001", t1));
        exemplarRepository.salvar(DominioFactory.criarExemplar("TOM-002", t1));

        Titulo t2 = DominioFactory.criarTitulo("Clean Code", "Robert C. Martin", "978-85-7654321-0");
        tituloRepository.salvar(t2);
        exemplarRepository.salvar(DominioFactory.criarExemplar("TOM-003", t2));

        System.out.println("[Semente] Usuários e exemplares iniciais carregados.\n");
    }

    /* ===================== MENU PRINCIPAL ===================== */

    private void executarMenu() {
        boolean sair = false;
        while (!sair) {
            imprimirLinha();
            System.out.println("=== GERENCIADOR DE BIBLIOTECA1 ===");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Cadastrar título");
            System.out.println("3. Cadastrar exemplar");
            System.out.println("4. Listar acervo (títulos, exemplares, usuários)");
            System.out.println("5. Realizar empréstimo");
            System.out.println("6. Realizar devolução");
            System.out.println("7. Consultar multa por atraso (sem efetivar devolução)");
            System.out.println("8. Criar reserva (fila de espera)");
            System.out.println("9. Demo automático (fluxo completo dos critérios de aceitação)");
            System.out.println("0. Sair");
            imprimirLinha();
            System.out.print("Escolha uma opção: ");
            String opcao = lerLinha();
            switch (opcao) {
                case "1" -> cadastrarUsuario();
                case "2" -> cadastrarTitulo();
                case "3" -> cadastrarExemplar();
                case "4" -> listarAcervo();
                case "5" -> realizarEmprestimo();
                case "6" -> realizarDevolucao();
                case "7" -> consultarMultaAtraso();
                case "8" -> criarReserva();
                case "9" -> rodarDemo();
                case "0" -> {
                    sair = true;
                    System.out.println("Encerrando o sistema. Até logo!");
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    /* ===================== CADASTROS ===================== */

    private void cadastrarUsuario() {
        try {
            System.out.println("\n--- Cadastro de Usuário ---");
            System.out.print("Nome: ");
            String nome = lerLinha();
            System.out.print("E-mail: ");
            String email = lerLinha();
            System.out.print("Matrícula: ");
            String matricula = lerLinha();
            System.out.print("Tipo (ALUNO / PROFESSOR / FUNCIONARIO): ");
            TipoUsuario tipo = TipoUsuario.valueOf(lerLinha().trim().toUpperCase());

            Usuario usuario = DominioFactory.criarUsuario(nome, email, matricula, tipo);
            usuarioRepository.salvar(usuario);

            UsuarioDTO dto = UsuarioMapper.toDto(usuario);
            System.out.println("\n[SUCESSO] Usuário cadastrado:");
            System.out.println(dto);
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERRO] Tipo de usuário inválido. Use ALUNO, PROFESSOR ou FUNCIONARIO.");
        } catch (Exception e) {
            System.out.println("\n[ERRO] Falha ao cadastrar usuário: " + e.getMessage());
        }
    }

    private void cadastrarTitulo() {
        try {
            System.out.println("\n--- Cadastro de Título ---");
            System.out.print("Nome da obra: ");
            String nome = lerLinha();
            System.out.print("Autor: ");
            String autor = lerLinha();
            System.out.print("ISBN: ");
            String isbn = lerLinha();

            Titulo titulo = DominioFactory.criarTitulo(nome, autor, isbn);
            tituloRepository.salvar(titulo);

            System.out.println("\n[SUCESSO] Título cadastrado: " + TituloMapper.toDto(titulo));
        } catch (Exception e) {
            System.out.println("\n[ERRO] Falha ao cadastrar título: " + e.getMessage());
        }
    }

    private void cadastrarExemplar() {
        try {
            System.out.println("\n--- Cadastro de Exemplar ---");
            System.out.print("Código de tombo: ");
            String codigoTombo = lerLinha();
            System.out.print("ID do título: ");
            Long tituloId = Long.parseLong(lerLinha().trim());

            Titulo titulo = tituloRepository.buscarPorId(tituloId)
                    .orElseThrow(() -> new RegraNegocioException("Título não encontrado: " + tituloId));

            Exemplar exemplar = DominioFactory.criarExemplar(codigoTombo, titulo);
            exemplarRepository.salvar(exemplar);

            System.out.println("\n[SUCESSO] Exemplar cadastrado: " + ExemplarMapper.toDto(exemplar));
        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] ID do título inválido.");
        } catch (RegraNegocioException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERRO] Falha ao cadastrar exemplar: " + e.getMessage());
        }
    }

    /* ===================== LISTAGEM ===================== */

    private void listarAcervo() {
        System.out.println("\n=== ACERVO ===");

        System.out.println("\n-- Usuários --");
        List<Usuario> usuarios = usuarioRepository.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("(nenhum)");
        }
        for (Usuario u : usuarios) {
            UsuarioDTO dto = UsuarioMapper.toDto(u);
            System.out.println("  [" + dto.getId() + "] " + dto.getNome()
                + " | " + dto.getTipo()
                + " | ativo=" + dto.isAtivo()
                + " | empréstimos ativos=" + dto.getQuantidadeEmprestimosAtivos()
                + " | multas pendentes=" + dto.getQuantidadeMultasPendentes()
                + " | reservas=" + dto.getQuantidadeReservas());
        }

        System.out.println("\n-- Títulos e Exemplares --");
        List<Titulo> titulos = tituloRepository.listarTodos();
        if (titulos.isEmpty()) {
            System.out.println("(nenhum)");
        }
        for (Titulo t : titulos) {
            TituloDTO dto = TituloMapper.toDto(t);
            System.out.println("  [" + dto.getId() + "] " + dto.getNome()
                + " | " + dto.getAutor()
                + " | ISBN=" + dto.getIsbn()
                + " | ativo=" + dto.isAtivo()
                + " | exemplares=" + dto.getQuantidadeExemplares()
                + " | disponíveis=" + dto.getExemplaresDisponiveis());
            List<Exemplar> exemplares = exemplarRepository.listarTodos().stream()
                    .filter(e -> t.getId().equals(e.getTitulo().getId())).toList();
            for (Exemplar e : exemplares) {
                ExemplarDTO edto = ExemplarMapper.toDto(e);
                System.out.println("      └─ [" + edto.getId() + "] " + edto.getCodigoTombo()
                    + " | status=" + edto.getStatus()
                    + " | ativo=" + edto.isAtivo()
                    + " | disponível=" + edto.isDisponivel());
            }
        }
    }

    /* ===================== EMPRÉSTIMO (CU01) ===================== */

    private void realizarEmprestimo() {
        try {
            System.out.println("\n--- Realizar Empréstimo ---");
            System.out.print("ID do usuário: ");
            Long usuarioId = Long.parseLong(lerLinha().trim());
            System.out.print("ID do exemplar: ");
            Long exemplarId = Long.parseLong(lerLinha().trim());
            System.out.print("Operador (ex: bibliotecario / autoatendimento): ");
            String operador = lerLinha();

            Emprestimo emprestimo = emprestimoService.emprestar(usuarioId, exemplarId, operador);
            EmprestimoDTO dto = EmprestimoMapper.toDto(emprestimo);

            System.out.println("\n[SUCESSO] Empréstimo realizado:");
            System.out.println("  " + dto);
            System.out.println("  Data prevista de devolução: " + dto.getDataPrevistaDevolucao());
        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] IDs inválidos.");
        } catch (RegraNegocioException e) {
            System.out.println("\n[ERRO DE NEGÓCIO] " + e.getMessage());
            System.out.println("  Dica: verifique se o usuário está ativo, não bloqueado, dentro do limite");
            System.out.println("        e se o exemplar está disponível.");
        } catch (Exception e) {
            System.out.println("\n[ERRO] Falha ao realizar empréstimo: " + e.getMessage());
        }
    }

    /* ===================== DEVOLUÇÃO (CU02) ===================== */

    private void realizarDevolucao() {
        try {
            System.out.println("\n--- Realizar Devolução ---");
            System.out.print("ID do empréstimo: ");
            Long emprestimoId = Long.parseLong(lerLinha().trim());
            System.out.print("Operador: ");
            String operador = lerLinha();
            System.out.print("Exemplar devolvido danificado? (s/n): ");
            boolean danificado = lerLinha().trim().equalsIgnoreCase("s");
            System.out.print("Exemplar devolvido perdido? (s/n): ");
            boolean perdido = lerLinha().trim().equalsIgnoreCase("s");

            if (danificado && perdido) {
                System.out.println("\n[AVISO] Um exemplar não pode ser ao mesmo tempo danificado e perdido. Tratando como perdido.");
                danificado = false;
            }

            var devolucao = devolucaoService.devolver(emprestimoId, operador, danificado, perdido);
            var dto = DevolucaoMapper.toDto(devolucao);

            System.out.println("\n[SUCESSO] Devolução realizada:");
            System.out.println("  " + dto);
            if (dto.getMultaGerada() != null) {
                System.out.println("  Multa gerada: R$ " + dto.getMultaGerada().getValor()
                    + " (" + dto.getMultaGerada().getDescricao() + ")");
            } else {
                System.out.println("  Sem multa (devolução dentro do prazo ou sem atraso).");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] ID inválido.");
        } catch (RegraNegocioException e) {
            System.out.println("\n[ERRO DE NEGÓCIO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERRO] Falha ao realizar devolução: " + e.getMessage());
        }
    }

    /* ===================== CONSULTAR MULTA (consulta apenas) ===================== */

    private void consultarMultaAtraso() {
        try {
            System.out.println("\n--- Consultar Multa por Atraso ---");
            System.out.print("ID do empréstimo: ");
            Long emprestimoId = Long.parseLong(lerLinha().trim());

            BigDecimal multa = devolucaoService.calcularMultaAtraso(emprestimoId);
            if (multa.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("\n[RESULTADO] Atraso detectado. Multa calculada: R$ " + multa);
            } else {
                System.out.println("\n[RESULTADO] Sem atraso. Multa: R$ 0,00.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] ID inválido.");
        } catch (RegraNegocioException e) {
            System.out.println("\n[ERRO DE NEGÓCIO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERRO] Falha ao consultar multa: " + e.getMessage());
        }
    }

    /* ===================== RESERVA (CU03) ===================== */

    private void criarReserva() {
        try {
            System.out.println("\n--- Criar Reserva ---");
            System.out.print("ID do usuário: ");
            Long usuarioId = Long.parseLong(lerLinha().trim());
            System.out.print("ID do título: ");
            Long tituloId = Long.parseLong(lerLinha().trim());

            var reserva = emprestimoService.reservar(usuarioId, tituloId);
            var dto = ReservaMapper.toDto(reserva);

            System.out.println("\n[SUCESSO] Reserva criada:");
            System.out.println("  " + dto);
        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] IDs inválidos.");
        } catch (RegraNegocioException e) {
            System.out.println("\n[ERRO DE NEGÓCIO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERRO] Falha ao criar reserva: " + e.getMessage());
        }
    }

    /* ===================== DEMO AUTOMÁTICO ===================== */

    /**
     * Executa um fluxo demonstrativo que cobre os critérios de aceitação do spec.md (seção 8):
     * 1. Empréstimo de exemplar disponível a usuário ativo dentro do limite.
     * 2. Data prevista conforme tipo (14 dias aluno / 30 dias professor).
     * 3. Empréstimo negado sem exemplares, com opção de reserva.
     * 4. Usuário com atraso/multa pendente não consegue novo empréstimo.
     * 5. Devolução com atraso gera multa automática por dia (3 dias = R$ 6,00).
     * 6. Exemplar perdido/danificado gera taxa R$ 50,00 + baixa.
     * 7. Usuário bloqueado até quitação da multa.
     */
    private void rodarDemo() {
        imprimirLinha();
        System.out.println(">>> DEMO AUTOMÁTICO — Fluxo completo dos critérios de aceitação <<<");
        imprimirLinha();

        try {
            // Recupera entidades da semente
            Usuario aluno = usuarioRepository.buscarPorMatricula("2024001")
                    .orElseThrow(() -> new RegraNegocioException("Usuário aluno não encontrado na semente."));
            Usuario prof = usuarioRepository.buscarPorMatricula("2024002")
                    .orElseThrow(() -> new RegraNegocioException("Usuário professor não encontrado na semente."));
            Titulo tituloDomCasmurro = tituloRepository.listarTodos().stream()
                    .filter(t -> "Dom Casmurro".equals(t.getNome())).findFirst()
                    .orElseThrow();
            Titulo tituloCleanCode = tituloRepository.listarTodos().stream()
                    .filter(t -> "Clean Code".equals(t.getNome())).findFirst()
                    .orElseThrow();

            // --- Cenário 1: Empréstimo dentro do limite (aluno, limite 3, prazo 14 dias) ---
            System.out.println("\n[CENÁRIO 1] Aluno 'Ana Silva' pega emprestado 'Dom Casmurro' (exemplar TOM-001).");
            Exemplar ex1 = exemplarRepository.buscarPorCodigoTombo("TOM-001").orElseThrow();
            Emprestimo e1 = emprestimoService.emprestar(aluno.getId(), ex1.getId(), "autoatendimento");
            System.out.println("    OK — Empréstimo [" + e1.getId() + "] criado. Data prevista: " + e1.getDataPrevistaDevolucao()
                + " (prazo 14 dias para aluno).");

            // --- Cenário 2: Professor com prazo 30 dias ---
            System.out.println("\n[CENÁRIO 2] Professor 'Carlos Professor' pega emprestado 'Clean Code' (exemplar TOM-003).");
            Exemplar ex3 = exemplarRepository.buscarPorCodigoTombo("TOM-003").orElseThrow();
            Emprestimo eProf = emprestimoService.emprestar(prof.getId(), ex3.getId(), "bibliotecario");
            System.out.println("    OK — Empréstimo [" + eProf.getId() + "] criado. Data prevista: " + eProf.getDataPrevistaDevolucao()
                + " (prazo 30 dias para professor).");

            // --- Cenário 3: Não há exemplares disponíveis -> reserva ---
            System.out.println("\n[CENÁRIO 3] Aluno tenta pegar o último exemplar de 'Dom Casmurro' (TOM-002) — ainda disponível.");
            Exemplar ex2 = exemplarRepository.buscarPorCodigoTombo("TOM-002").orElseThrow();
            Emprestimo e2 = emprestimoService.emprestar(aluno.getId(), ex2.getId(), "autoatendimento");
            System.out.println("    OK — Empréstimo [" + e2.getId() + "] criado.");

            System.out.println("\n[CENÁRIO 4] Aluno tenta empréstimo de 'Dom Casmurro' novamente — não há mais exemplares disponíveis.");
            try {
                emprestimoService.emprestar(aluno.getId(), ex1.getId(), "autoatendimento");
            } catch (RegraNegocioException ex) {
                System.out.println("    Empréstimo negado: " + ex.getMessage());
                System.out.println("    → Oferecendo reserva na fila...");
                var reserva = emprestimoService.reservar(aluno.getId(), tituloDomCasmurro.getId());
                System.out.println("    Reserva criada: [" + reserva.getId() + "] status=" + reserva.getStatus());
            }

            // --- Cenário 5: Devolução com atraso (simula 3 dias de atraso) -> multa R$ 6,00 ---
            System.out.println("\n[CENÁRIO 5] Devolução de empréstimo [" + e1.getId() + "] com atraso de 3 dias.");
            e1.setDataPrevistaDevolucao(LocalDateTime.now().minusDays(3));
            emprestimoRepository.salvar(e1);
            var devolucao = devolucaoService.devolver(e1.getId(), "bibliotecario", false, false);
            MultaDTO multaDto = MultaMapper.toDto(devolucao.getMultaGerada());
            System.out.println("    Devolução efetuada. Multa gerada: R$ " + multaDto.getValor()
                + " — " + multaDto.getDescricao());
            System.out.println("    Usuário 'Ana Silva' bloqueado? " + aluno.estaBloqueado());

            // --- Cenário 6: Usuário bloqueado não consegue novo empréstimo ---
            System.out.println("\n[CENÁRIO 6] Aluno bloqueado tenta novo empréstimo — deve ser recusado.");
            Exemplar ex4 = exemplarRepository.buscarPorCodigoTombo("TOM-003").orElseThrow();
            // TOM-003 está emprestado ao professor, então não está disponível.
            // Vamos verificar bloqueio antes mesmo da disponibilidade:
            try {
                emprestimoService.emprestar(aluno.getId(), ex4.getId(), "autoatendimento");
            } catch (RegraNegocioException ex) {
                System.out.println("    Empréstimo negado: " + ex.getMessage());
            }

            // --- Cenário 7: Devolução de exemplar perdido -> taxa R$ 50,00 + baixa ---
            System.out.println("\n[CENÁRIO 7] Devolução do empréstimo [" + eProf.getId() + "] como PERDIDO.");
            var devolucaoPerdido = devolucaoService.devolver(eProf.getId(), "bibliotecario", false, true);
            MultaDTO multaPerdido = MultaMapper.toDto(devolucaoPerdido.getMultaGerada());
            System.out.println("    Multa (taxa de reposição): R$ " + multaPerdido.getValor()
                + " — " + multaPerdido.getDescricao());
            Exemplar exPerdido = exemplarRepository.buscarPorCodigoTombo("TOM-003").orElseThrow();
            System.out.println("    Status do exemplar TOM-003 após baixa: " + exPerdido.getStatus()
                + " | ativo=" + exPerdido.isAtivo());

            // --- Cenário 8: Reservas e fila ---
            System.out.println("\n[CENÁRIO 8] Consultando reservas do título 'Dom Casmurro':");
            List<org.example.biblioteca.domain.model.Reserva> reservas =
                    reservaRepository.listarPorTitulo(tituloDomCasmurro.getId());
            for (org.example.biblioteca.domain.model.Reserva r : reservas) {
                System.out.println("    " + ReservaMapper.toDto(r));
            }

            imprimirLinha();
            System.out.println(">>> DEMO CONCLUÍDO COM SUCESSO <<<");
            imprimirLinha();

        } catch (RegraNegocioException e) {
            System.out.println("\n[ERRO DE NEGÓCIO durante o demo] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERRO inesperado durante o demo] " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ===================== UTILITÁRIOS ===================== */

    private String lerLinha() {
        return scanner.nextLine();
    }

    private void imprimirLinha() {
        System.out.println("------------------------------------------------------------");
    }

}
