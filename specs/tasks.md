# tasks.md — Implementação: Gerenciador de Biblioteca

**Base:** `specs/spec.md` (regras de negócio, RF/RN, casos de uso, critérios de aceitação) e `specs/design.md` (arquitetura em camadas, padrões de projeto, interfaces de serviço).  
**Pacote base:** `org.example.biblioteca` (conforme `design.md`; ajuste se o projeto mantiver `org.example`).  
**Linguagem:** Java 17+ · Build: Maven (`pom.xml` na raiz).  
**Formato:** cada item é uma tarefa incremental e testável. Marque `[x]` ao concluir.

---

## Convenções

- IDs de tarefa: `T<fase>.<n>` (ex: `T1.1`).
- Referências: `[RF01]`, `[RN02]`, `[spec 3.6]`, `[design 3.1]`.
- Critério de teste: o que deve ser verdadeiro após concluir a tarefa (verificável por teste unitário, de integração ou execução do Main/CLI).
- Exceções de negócio herdam sempre de `RegraNegocioException` ([design 4.3]).
- Valores monetários usam `BigDecimal`.

---

# Fase 1 — Modelo de Domínio e Exceções

**Objetivo:** criar enums, entidades de domínio e exceções de negócio puros (sem dependência de frameworks), refletindo o glossário e as regras do spec.md.

- [x] **T1.1** Criar enum `TipoUsuario` com `ALUNO`, `PROFESSOR`, `FUNCIONARIO`. `[design 2.1]`
  - Critério: compila e contém exatamente esses três valores.
- [x] **T1.2** Criar enum `StatusExemplar` com `DISPONIVEL`, `EMPRESTADO`, `EM_REPARO`, `DANIFICADO`, `PERDIDO`. `[design 2.1]`
- [x] **T1.3** Criar enum `StatusReserva` com `AGUARDANDO`, `NOTIFICADA`, `CONCLUIDA`, `CANCELADA`, `EXPIRADA`. `[design 2.1]`
- [x] **T1.4** Criar enum `StatusMulta` com `PENDENTE`, `PAGA`, `CANCELADA`. `[design 2.1]`
- [x] **T1.5** Criar entidade `Usuario` (id, nome, email, matrícula única, tipo, ativo, empréstimos ativos, multas pendentes, reservas) com métodos `estaBloqueado()` e `quantidadeEmprestimosAtivos()`. `[RF01, RF10, RN06]`
  - Critério de teste: `estaBloqueado()` retorna `true` quando inativo, quando possui multa `PENDENTE` ou quando tem empréstimo atrasado.
- [x] **T1.6** Criar entidade `Titulo` (id, nome, autor, ISBN, ativo, lista de exemplares) com `buscarExemplarDisponivel()` e `possuiExemplarDisponivel()`. `[RF04]`
- [x] **T1.7** Criar entidade `Exemplar` (id, código de tombo único, título, status, ativo, empréstimo ativo) com `estaDisponivel()` e `estaIndisponivelPorEstado()`. `[RF04, RN04]`
  - Critério de teste: só está disponível se status `DISPONIVEL` **e** ativo.
- [x] **T1.8** Criar entidade `Emprestimo` (id, usuário, exemplar, operador, data empréstimo, data prevista, data real, ativo, multa) com `estaAtrasado()` e `diasAtraso()`. `[RF02, RF05, NF02]`
  - Critério de teste: `diasAtraso()` retorna 0 se não atrasado; senão, diferença em dias entre `dataPrevistaDevolucao` e agora.
- [x] **T1.9** Criar entidade `Devolucao` (id, empréstimo, data devolução, danificado, perdido, multa gerada). `[CU02]`
- [x] **T1.10** Criar entidade `Reserva` (id, usuário, título, data reserva, status, data notificação, data expiração = 3 dias corridos após notificação). `[CU03, spec 3.6]`
- [x] **T1.11** Criar entidade `Multa` (id, usuário, empréstimo origem, valor `BigDecimal`, status, data vencimento, descrição). Parâmetros: R$ 2,00/dia de atraso; R$ 50,00 taxa de reposição. `[RF05, RF08, spec 3.6]`
- [x] **T1.12** Implementar `equals`/`hashCode`/`toString` nas entidades baseados no ID.
- [x] **T1.13** Criar exceção base `RegraNegocioException extends Exception`. `[design 4.3]`
- [x] **T1.14** Criar exceções concretas: `UsuarioBloqueadoException`, `ExemplarIndisponivelException`, `LimiteEmprestimoAtingidoException`, `UsuarioNaoEncontradoException`, `TituloNaoEncontradoException`, `ExemplarNaoEncontradoException`, `EmprestimoNaoEncontradoException`, `ReservaNaoEncontradaException` (todas herdam de `RegraNegocioException`). `[design 4.3]`

**Definition of Done Fase 1:** `mvn compile` passa; domínio compila sem frameworks externos; exceções lançam mensagens descritivas.

---

# Fase 2 — Padrões de Projeto (Strategy, State, Factory)

**Objetivo:** encapsular políticas por tipo de usuário, máquina de estados do exemplar e centralizar criação de objetos.

- [x] **T2.1** Criar interface `PoliticaEmprestimo` (`getLimiteLivrosSimultaneos()`, `getPrazoRetencaoDias()`, `podeEmprestar(Usuario)`). `[design 3.1, RN01-RN03]`
- [x] **T2.2** Implementar `PoliticaAluno`: limite 3, prazo 14 dias. `[RN01]`
- [x] **T2.3** Implementar `PoliticaProfessor`: limite 5, prazo 30 dias. `[RN02]`
- [x] **T2.4** Implementar `PoliticaFuncionario`: limite 3, prazo 14 dias. `[RN03]`
  - Critério de teste: cada política retorna limite e prazo da tabela do spec.md seção 3.1.
- [x] **T2.5** Criar `ContextoPoliticaEmprestimo` que seleciona a política pelo `TipoUsuario` e expõe prazo, limite e validação. `[design 3.1]`
- [x] **T2.6** Criar `PoliticaFactory.criar(TipoUsuario)` com `switch` retornando a política concreta. `[design 3.3]`
- [x] **T2.7** Criar interface `EstadoExemplar` (`executarEmprestimo`, `executarDevolucao(exemplar, danificado, perdido)`, `executarEnviarReparo`, `executarBaixa`, `podeEmprestar`). `[design 3.2]`
- [x] **T2.8** Implementar `EstadoDisponivel`, `EstadoEmprestado`, `EstadoEmReparo`, `EstadoDanificado`, `EstadoPerdido` seguindo a matriz de transições do design.md. `[RN04, RN07, spec 3.4]`
  - Critério de teste: devolução OK de emprestado → `DISPONIVEL`; danificado → `DANIFICADO` + baixa; perdido → `PERDIDO` + baixa; emprestar em não-disponível lança exceção de operação não permitida.
- [x] **T2.9** Criar `EstadoExemplarFactory.criar(StatusExemplar)`. `[design 3.3]`
- [x] **T2.10** Criar `DominioFactory` com métodos de criação de `Usuario`, `Titulo`, `Exemplar`, `Emprestimo`, `Reserva`, `Multa`. `[design 3.3]`
- [x] **T2.11** Integrar State em `Exemplar`: `emprestar()`, `devolver(danificado, perdido)`, `enviarParaReparo()`, `darBaixa()` delegam ao estado atual. `[design 2.4]`

**Definition of Done Fase 2:** estratégias trocáveis em tempo de execução; transições de estado seguem a matriz sem blocos `if/else` de status espalhados; factories não expõem classes concretas ao cliente; testes de cada estado passam.

---

# Fase 3 — Camada de Serviços (EmprestimoService e DevolucaoService)

**Objetivo:** interfaces e implementações dos casos de uso CU01, CU02 e CU03 com validações de RN, multas e reservas. Usar repositório em memória (fake) nesta fase; substituído por JPA na Fase 4.

- [x] **T3.1** Criar interface `EmprestimoService` com `emprestar(...)`, `existeExemplarDisponivel(Long)`, `listarEmprestimosAtivos(Long)`, `reservar(Long usuarioId, Long tituloId)`. `[design 4.1, CU01, CU03]`
- [x] **T3.2** Criar interface `DevolucaoService` com `devolver(Long emprestimoId, String operador, boolean danificado, boolean perdido)` e `calcularMultaAtraso(Long emprestimoId)`. `[design 4.2, CU02]`
- [x] **T3.3** Criar repositórios em memória (`UsuarioRepository`, `TituloRepository`, `ExemplarRepository`, `EmprestimoRepository`, `ReservaRepository`, `MultaRepository`) com operações CRUD básicas e busca por título/código de tombo. Índice lógico por `(titulo_id, status, ativo)`. `[NF03]`
- [x] **T3.4** Implementar `EmprestimoServiceImpl.emprestar`:
  1. valida usuário ativo e não bloqueado → senão `UsuarioBloqueadoException` `[RF01, RF10, RN06]`;
  2. valida limite via Strategy → senão `LimiteEmprestimoAtingidoException` `[RN01-RN03]`;
  3. busca exemplar `DISPONIVEL` → senão `ExemplarIndisponivelException` e oferece `reservar` `[RF03, RN05]`;
  4. cria empréstimo com `dataPrevista = agora + prazo da Strategy` `[RF02]`;
  5. altera exemplar para `EMPRESTADO` via State `[RN04]`;
  6. persiste de forma atômica `[NF01]` e registra operador (auditoria) `[NF02]`.
- [x] **T3.5** Implementar `DevolucaoServiceImpl.devolver`:
  1. localiza empréstimo ativo do exemplar/usuário;
  2. se `dataReal > dataPrevista`, gera multa `diasAtraso * 2,00` e usuário fica bloqueado `[RF05, RF06]`;
  3. atualiza status do exemplar via State (OK → `DISPONIVEL`; danificado/perdido → `DANIFICADO`/`PERDIDO` + taxa de reposição R$ 50,00 + baixa) `[RF07, RF08, RN07]`;
  4. se houver fila de reserva, notifica o primeiro (`NOTIFICADA`, expiração 3 dias corridos) `[CU03, spec 3.6]`.
- [x] **T3.6** Implementar `calcularMultaAtraso` (consulta sem efetivar a devolução).
- [x] **T3.7** Implementar `reservar`: insere usuário na fila do título; posição na fila por ordem de criação. `[RN05]`
- [x] **T3.8** Regra de bloqueio consolidada: usuário com empréstimo atrasado ou multa `PENDENTE` não pode emprestar. `[RF06, RN06]`

**Definition of Done Fase 3:** cada caso de uso tem cenários de sucesso e falha passando (empréstimo OK; negado por inativo; negado por limite; negado sem exemplar com opção de reserva; devolução pontual sem multa; atraso de 3 dias = R$ 6,00; danificado/perdido = R$ 50,00 + baixa; notificação de reserva).

---

# Fase 4 — Persistência, DTOs, Mappers e Apresentação (Main/CLI)

**Objetivo:** DTOs, mappers, camada de persistência e classe Main/CLI para exercitar o fluxo completo.

- [x] **T4.1** Criar DTOs: `EmprestimoDTO`, `DevolucaoDTO`, `MultaDTO`, `ReservaDTO`, `UsuarioDTO`, `TituloDTO`, `ExemplarDTO`.
- [x] **T4.2** Criar Mappers (`EmprestimoMapper`, etc.) convertendo entidade ↔ DTO.
- [x] **T4.3** Implementar persistência: interfaces de repositório e implementação em memória thread-safe (substituível por JPA+H2 posteriormente). `[NF03]`
- [x] **T4.4** Criar classe `Main` (ou CLI) com fluxo de teste interativo ou fixo que executa CU01/CU02/CU03: cadastra usuário/título/exemplares, empresta, devolve, consulta multa e reserva.
  - Critério: a CLI roda sem erros e cobre os critérios de aceitação do spec.md seção 8.

**Definition of Done Fase 4:** `Main` executa fluxo ponta a ponta; DTOs/mappers convertem corretamente; repositório persiste entre operações.

---

# Fase 5 — Testes Unitários e de Integração

**Objetivo:** cobrir domínio, padrões, serviços e fluxos com testes automatizados e validar todos os critérios de aceitação.

- [x] **T5.1** Testes unitários de domínio: `Usuario.estaBloqueado()`, `quantidadeEmprestimosAtivos()`, `Exemplar.estaDisponivel()`, `Emprestimo.estaAtrasado()`/`diasAtraso()`, `Titulo.buscarExemplarDisponivel()`.
- [x] **T5.2** Testes da Strategy: cada `PoliticaEmprestimo` retorna limite/prazo corretos; `podeEmprestar` respeita ativo/bloqueado.
- [x] **T5.3** Testes do State: matriz completa de transições (válidas mudam o estado; inválidas lançam exceção de operação não permitida).
- [x] **T5.4** Testes das Factories: `PoliticaFactory`, `EstadoExemplarFactory`, `DominioFactory` criam objetos com atributos esperados.
- [x] **T5.5** Testes de `EmprestimoService`: empréstimo válido com data prevista correta (14/30 dias); negado para usuário inativo; negado por limite atingido; negado sem exemplar disponível com reserva; reservar e consultar fila.
- [x] **T5.6** Testes de `DevolucaoService`: devolução pontual sem multa; atraso de 3 dias → multa R$ 6,00; atraso genérico → R$ 2,00/dia; exemplar danificado/perdido → taxa R$ 50,00 + baixa + não volta ao acervo; notificação do primeiro da reserva e expiração em 3 dias.
- [x] **T5.7** Teste de integração/fluxo ponta a ponta: empréstimo → devolução → exemplar fica disponível → próximo da reserva consegue emprestar; usuário bloqueado até quitação da multa.
- [x] **T5.8** Executar todos os testes (`mvn test`) e garantir cobertura dos critérios de aceitação do spec.md seção 8 (todos os itens mapeados para pelo menos um teste).

**Definition of Done Fase 5:** suíte verde; todos os critérios de aceitação cobertos por testes; regressão passa após cada mudança.

---

## Critérios de aceitação finais (spec.md, seção 8)

| # | Critério de aceitação | Tarefas que o cobrem |
|---|-----------------------|----------------------|
| 1 | Empréstimo de exemplar `DISPONÍVEL` a usuário ativo dentro do limite | T3.4, T5.5 |
| 2 | Data prevista 14 dias (aluno/funcionário) ou 30 dias (professor) | T2.2–T2.4, T5.5 |
| 3 | Empréstimo negado sem exemplares disponíveis, com opção de reserva | T3.4, T3.7, T5.5 |
| 4 | Usuário com atraso ou multa pendente não consegue novo empréstimo | T3.8, T5.6, T5.7 |
| 5 | Devolução com atraso gera multa automática por dia | T3.5, T5.6 |
| 6 | Exemplar danificado/perdido gera taxa de reposição e recebe baixa | T3.5, T5.6 |
| 7 | `EM_REPARO`, `DANIFICADO`, `PERDIDO` não disponíveis para empréstimo | T2.8, T3.4 |
| 8 | Bibliotecário e autoatendimento executam operações com validação de cadastro ativo | T3.4, T3.5 |
| 9 | Multa R$ 2,00/dia (ex: 3 dias = R$ 6,00) | T3.5, T5.6 |
| 10 | Taxa de reposição R$ 50,00 por exemplar danificado/perdido | T3.5, T5.6 |
| 11 | Reserva expira após 3 dias corridos da notificação, passando a vaga ao próximo | T3.5, T5.6 |

---

## Checklist de conclusão do projeto

- [x] Fase 1 concluída (Domínio e Exceções)
- [x] Fase 2 concluída (Strategy, State, Factories)
- [x] Fase 3 concluída (Camada de Serviços)
- [x] Fase 4 concluída (DTOs, Mappers, Persistência, Main/CLI)
- [x] Fase 5 concluída (Testes Unitários e de Integração)
- [x] Projeto completo: `mvn test` verde e todos os critérios de aceitação atendidos
