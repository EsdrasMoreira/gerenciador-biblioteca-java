# Gerenciador de Biblioteca

Sistema de gerenciamento de acervo bibliotecário que controla **empréstimo, devolução e reserva de exemplares**, com cálculo automático de multas por atraso, taxa de reposição por dano/perda e máquina de estados para o ciclo de vida dos exemplares.

## 1. Visão geral

O Gerenciador de Biblioteca permite cadastrar usuários (alunos, professores e funcionários), títulos e exemplares físicos (identificados por código de tombo único), realizar empréstimos respeitando limites por tipo de usuário, devolver exemplares com ou sem atraso, gerenciar fila de reserva quando todos os exemplares de um título estão emprestados e aplicar multas e taxas de reposição conforme as regras de negócio definidas.

O projeto foi desenvolvido seguindo **Spec-Driven Development (SDD)**: todas as regras de negócio, casos de uso, requisitos (RF/RN) e critérios de aceitação foram definidos em especificações antes de qualquer implementação.

### Especificações (SDD)

As especificações vivem na pasta `specs/`:

| Arquivo | Conteúdo |
|---------|----------|
| `specs/spec.md` | Regras de negócio, casos de uso (CU01-CU03), requisitos funcionais (RF), regras de negócio (RN), casos de borda e critérios de aceitação. |
| `specs/design.md` | Arquitetura em camadas (Layered / Ports & Adapters), modelagem de entidades, interfaces de serviço, exceções de negócio e diagrama de classes. |
| `specs/tasks.md` | Plano de implementação dividido em 5 fases, com tarefas incremental e testável (T1.x a T5.x) e mapeamento direto aos critérios de aceitação do `spec.md`. |

### Pacote base e build

- **Pacote base:** `org.example.biblioteca`
- **Build:** Apache Maven (`pom.xml` na raiz)
- **Java:** 17+ (configurado para Java 26 no `pom.xml`)
- **Testes:** JUnit 5 (Jupiter)

---

## 2. Metodologia: Spec-Driven Development (SDD)

O projeto adota **Spec-Driven Development**, ou desenvolvimento guiado por especificações. Em vez de começar a codificar imediatamente, o trabalho segue a ordem:

1. **`specs/spec.md`** — especificação de requisitos e regras de negócio (fonte única da verdade).
2. **`specs/design.md`** — design técnico derivado do spec: arquitetura, classes, interfaces, padrões de projeto e fluxos de casos de uso.
3. **`specs/tasks.md`** — tarefas de implementação incrementais, cada uma vinculada a trechos do spec e do design, com critério de teste próprio.
4. **Implementação** — codificação seguindo as tarefas, com testes unitários e de integração para validar cada critério de aceitação.

Vantagens dessa abordagem neste projeto:

- Regras de negócio (prazos, multas, limites, estados do exemplar) ficam documentadas e versionadas antes do código.
- Cada tarefa de implementação tem um critério de aceitação verificável por teste.
- Facilita a manutenção: ao mudar uma regra de negócio, basta atualizar o `spec.md` e refletir no código e nos testes.

---

## 3. Padrões de Projeto aplicados

Três padrões de projeto (Gang of Four) foram aplicados no domínio da biblioteca:

### 3.1 Strategy — Política de empréstimo por tipo de usuário

**Onde é usado:** definição do limite de livros simultâneos, prazo de retenção e validação de empréstimo conforme o tipo do usuário (aluno, professor ou funcionário).

| Classe | Papel |
|--------|-------|
| `PoliticaEmprestimo` (interface) | Estratégia: expõe `getLimiteLivrosSimultaneos()`, `getPrazoRetencaoDias()` e `podeEmprestar(Usuario)`. |
| `PoliticaAluno` | Concreto: limite 3 livros, prazo 14 dias (RN01). |
| `PoliticaProfessor` | Concreto: limite 5 livros, prazo 30 dias (RN02). |
| `PoliticaFuncionario` | Concreto: limite 3 livros, prazo 14 dias (RN03). |
| `ContextoPoliticaEmprestimo` | Contexto: seleciona a política conforme `TipoUsuario` e delega prazo, limite e validação. |

**Benefício:** permite trocar o algoritmo de empréstimo em tempo de execução sem alterar o serviço de empréstimo (Open/Closed Principle). Um novo tipo de usuário exige apenas uma nova implementação de `PoliticaEmprestimo`.

### 3.2 State — Máquina de estados do exemplar

**Onde é usado:** gerenciamento das transições de status de cada exemplar físico (`DISPONÍVEL`, `EMPRESTADO`, `EM_REPARO`, `DANIFICADO`, `PERDIDO`).

| Classe | Papel |
|--------|-------|
| `EstadoExemplar` (interface) | Estado: define as operações `executarEmprestimo`, `executarDevolucao`, `executarEnviarReparo`, `executarBaixa` e `podeEmprestar`. |
| `EstadoDisponivel`, `EstadoEmprestado`, `EstadoEmReparo`, `EstadoDanificado`, `EstadoPerdido` | Concretos: encapsulam o comportamento e as transições válidas de cada status. |

**Exemplo de transição:** um exemplar `EMPRESTADO` devolvido em boas condições volta para `DISPONÍVEL`; se for devolvido danificado ou perdido, vai para `DANIFICADO`/`PERDIDO` com taxa de reposição e baixa do acervo (RN07).

**Benefício:** elimina blocos `if/else` ou `switch` de status espalhados pelo código; o exemplar altera seu comportamento conforme seu estado interno.

### 3.3 Factory — Criação centralizada de objetos

**Onde é usado:** criação de estratégias, estados e entidades de domínio, desacoplando o cliente das classes concretas.

| Factory | Cria |
|---------|------|
| `PoliticaFactory.criar(TipoUsuario)` | Política concreta de empréstimo (Strategy). |
| `EstadoExemplarFactory.criar(StatusExemplar)` | Estado concreto do exemplar (State). |
| `DominioFactory` | Entidades: `Usuario`, `Titulo`, `Exemplar`, `Emprestimo`, `Reserva`, `Multa` com valores default consistentes. |

**Benefício:** ponto único de criação, facilita manutenção e permite adicionar novos tipos/status/entidades sem modificar o código cliente.

---

## 4. Regras de Negócio e Limites

Todas as regras abaixo estão detalhadas em `specs/spec.md` (seções 3 e 3.6) e cobertas por testes automatizados.

### 4.1 Limites de empréstimo por perfil de usuário

| Tipo de usuário | Limite de livros simultâneos | Prazo de retenção | Regra |
|-----------------|------------------------------|-------------------|-------|
| Aluno | 3 livros | 14 dias | RN01 |
| Professor | 5 livros | 30 dias | RN02 |
| Funcionário | 3 livros | 14 dias | RN03 |

### 4.2 Disponibilidade de exemplares

- Cada título possui múltiplos exemplares, cada um com **código de tombo único** (RF04).
- Um exemplar só pode ser emprestado se estiver com status **DISPONÍVEL** (RN04).
- Exemplares com status `EM_REPARO`, `DANIFICADO` ou `PERDIDO` são **indisponíveis para empréstimo**.
- Quando todos os exemplares de um título estão emprestados, o empréstimo é negado e o usuário pode entrar na **fila de reserva** daquele título (RN05).

### 4.3 Cálculo de multas

| Parâmetro | Valor | Aplicação |
|-----------|-------|-----------|
| Multa por dia de atraso | **R$ 2,00 por dia** | Cobrada automaticamente na devolução quando a data real é maior que a data prevista (RF05). Exemplo: 3 dias de atraso = R$ 6,00. |
| Taxa de reposição (dano/perda) | **R$ 50,00 por exemplar** | Cobrada quando o exemplar é devolvido danificado ou perdido, seguida de baixa do exemplar (RF08, RN07). |

### 4.4 Bloqueio de usuário

Um usuário fica **bloqueado** para novos empréstimos quando:
- possui livro(s) em atraso, ou
- possui multa(s) pendente(s) não quitadas.

O bloqueio vale até a quitação da multa/devolução em dia (RF06, RN06).

### 4.5 Expiração de reserva

- Quando um exemplar do título reservado fica disponível, o primeiro da fila é notificado (`Reserva` → `NOTIFICADA`).
- A reserva **expira após 3 dias corridos** contados a partir da notificação de disponibilidade (spec.md seção 3.6, CU03).
- Após o prazo, a reserva expira e a vaga passa para o próximo da fila.

### 4.6 Validações obrigatórias (RF10)

Antes de qualquer empréstimo ou devolução, o sistema valida que:
1. O **usuário** está com cadastro **ativo**.
2. O **livro/exemplar** está com cadastro **ativo**.

Operações podem ser executadas por **bibliotecários** ou via **autoatendimento** pelo próprio usuário, sujeitas às mesmas validações (RF09).

---

## 5. Como compilar e executar

### Pré-requisitos

- [Apache Maven](https://maven.apache.org/) instalado.
- JDK compatível (Java 17+; o `pom.xml` está configurado para Java 26).

### Compilar o projeto

```bash
mvn compile
```

### Executar a suíte de testes (JUnit 5)

```bash
mvn test
```

Todos os testes de unidade, integração e fluxo ponta a ponta devem passar (suíte verde), cobrindo todos os critérios de aceitação da seção 8 do `specs/spec.md`.

### Executar a aplicação (CLI / Demo)

O ponto de entrada da aplicação é a classe de apresentação interativa:

```bash
mvn compile
java -cp target/classes org.example.biblioteca.presentation.Main
```

O CLI oferece um menu interativo com as opções:

1. Cadastrar usuário
2. Cadastrar título
3. Cadastrar exemplar
4. Listar acervo (títulos, exemplares, usuários)
5. Realizar empréstimo (CU01)
6. Realizar devolução (CU02)
7. Consultar multa por atraso (sem efetivar devolução)
8. Criar reserva (fila de espera) (CU03)
9. **Demo automático** — executa um fluxo completo cobrindo os critérios de aceitação do `spec.md` (empréstimo dentro do limite, prazo conforme tipo, empréstimo negado sem exemplares com reserva, usuário bloqueado, devolução com atraso e multa de R$ 2,00/dia, exemplar perdido/danificado com taxa de R$ 50,00 e baixa)
0. Sair

O `main` carrega uma semente inicial de dados (usuários aluno e professor, títulos "Dom Casmurro" e "Clean Code" com exemplares) para demonstração imediata.

### Estrutura resumida do código-fonte

```
src/main/java/org/example/biblioteca/
├── domain/                 # Domínio puro (sem frameworks)
│   ├── model/              # Entidades: Usuario, Titulo, Exemplar, Emprestimo, Devolucao, Reserva, Multa
│   ├── enums/              # TipoUsuario, StatusExemplar, StatusReserva, StatusMulta
│   ├── exception/          # Exceções de negócio (RegraNegocioException e especializadas)
│   ├── strategy/           # Strategy: PoliticaEmprestimo, PoliticaAluno/Professor/Funcionario, ContextoPoliticaEmprestimo
│   ├── state/              # State: EstadoExemplar e estados concretos do exemplar
│   ├── factory/            # Factory: PoliticaFactory, EstadoExemplarFactory, DominioFactory
│   ├── service/            # Interfaces de serviço (ports): EmprestimoService, DevolucaoService
│   └── infrastructure/     # Repositórios em memória (substituíveis por JPA+H2)
├── application/            # Casos de uso, DTOs e mappers
│   ├── service/            # EmprestimoServiceImpl, DevolucaoServiceImpl
│   ├── dto/                # DTOs de entrada/saída
│   └── mapper/             # Mappers entidade <-> DTO
└── presentation/           # Main (CLI interativo)
```

---

## 6. Licença

Projeto acadêmico de gerenciamento de biblioteca. Sem licença definida.
