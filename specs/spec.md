# Spec — Empréstimo e Devolução de Livros

**Projeto:** Gerenciador de Biblioteca  
**Módulo:** Empréstimo, Devolução e Reserva de Exemplares  
**Método:** Spec-Driven Development (SDD)  
**Versão:** 1.0  
**Status:** Em descoberta (pré-código)

---

## 1. Objetivo

Definir, antes de qualquer implementação, as regras de negócio, casos de uso, entidades, validações e critérios de aceitação para a funcionalidade de **empréstimo e devolução de livros**, incluindo controle de múltiplos exemplares por título, fila de reserva, multas por atraso e estados do exemplar.

---

## 2. Glossário / Entidades principais

| Entidade | Descrição |
|----------|-----------|
| **Usuário** | Pessoa cadastrada no sistema (aluno, professor ou funcionário). Possui tipo, cadastro ativo/inativo, livros emprestados, multas pendentes e possíveis reservas. |
| **Título** | Obra bibliográfica (ex: "Dom Casmurro"). Pode ter um ou mais exemplares. |
| **Exemplar** | Cópia física de um título. Identificado por **código de tombo único**. Possui status (DISPONÍVEL, EMPRESTADO, EM_REPARO, DANIFICADO, PERDIDO). |
| **Empréstimo** | Registro da retirada de um exemplar por um usuário, com data de empréstimo, data prevista de devolução e data real de devolução. |
| **Devolução** | Registro da entrega de um exemplar emprestado, podendo gerar multa por atraso. |
| **Reserva** | Fila de espera por um título quando todos os seus exemplares estão emprestados. |
| **Multa** | Valor cobrado por dias de atraso na devolução ou taxa de reposição por exemplar danificado/perdido. |
| **Bibliotecário** | Usuário com permissão para executar operações de empréstimo/devolução em nome de terceiros. |
| **Autoatendimento** | Canal pelo qual o próprio usuário realiza empréstimo/devolução, sujeito às mesmas validações. |

---

## 3. Regras de negócio

### 3.1 Limites de empréstimo por tipo de usuário

| Tipo de usuário | Limite de livros simultâneos | Prazo de retenção |
|-----------------|------------------------------|-------------------|
| Aluno | 3 livros | 14 dias |
| Professor | 5 livros | 30 dias |
| Funcionário | 3 livros | 14 dias |

### 3.2 Disponibilidade de exemplares

- Cada título possui **múltiplos exemplares**, cada um com **código de tombo único**.
- Um exemplar só pode ser emprestado se estiver com status **DISPONÍVEL**.
- Quando **todos os exemplares de um título estão emprestados**, o novo empréstimo é **negado** e o usuário é incluído na **fila de reserva** daquele título.
- Exemplares com status **EM_REPARO, DANIFICADO ou PERDIDO** estão indisponíveis para empréstimo.

### 3.3 Devolução e atraso

- O sistema calcula automaticamente **multa por dia de atraso** na devolução (além da data prevista). Valor: **R$ 2,00 por dia de atraso**.
- Usuário com **livro(s) em atraso** ou **multa(s) pendente(s)** está **bloqueado** para realizar novos empréstimos.
- A devolução de um exemplar altera seu status de volta para **DISPONÍVEL** (ou para o estado adequado, se houver dano/perda detectada).

### 3.4 Estados do exemplar e baixa

Status possíveis: `DISPONÍVEL`, `EMPRESTADO`, `EM_REPARO`, `DANIFICADO`, `PERDIDO`.

- Exemplares `EM_REPARO`, `DANIFICADO` ou `PERDIDO` ficam indisponíveis para empréstimo.
- Para livros **perdidos ou danificados**: é cobrada **taxa de reposição de R$ 50,00 por exemplar** do usuário responsável e o exemplar recebe **baixa** (não volta mais ao acervo disponível).

### 3.5 Permissões e validações obrigatórias

- Operações de empréstimo e devolução podem ser feitas por **bibliotecários** ou via **autoatendimento** pelo próprio usuário.
- Antes de confirmar qualquer operação, o sistema deve validar obrigatoriamente que:
  1. O **usuário** está com cadastro **ativo**.
  2. O **livro/exemplar** está com cadastro **ativo**.

### 3.6 Parâmetros do Sistema

| Parâmetro | Valor | Aplicação |
|-----------|-------|-----------|
| Multa por dia de atraso | **R$ 2,00** | Cobrada automaticamente na devolução quando `dataReal > dataPrevista` (RF05, RN06). |
| Taxa de reposição (dano/perda) | **R$ 50,00** | Cobrada quando exemplar é devolvido danificado ou perdido, seguido de baixa do acervo (RN07, RF08). |
| Expiração da reserva | **3 dias corridos** | Contados a partir da notificação de disponibilidade ao primeiro da fila. Após o prazo, a reserva expira e a vaga passa para o próximo (CU03). |

---

## 4. Casos de uso / Fluxos

### CU01 — Realizar empréstimo de exemplar

**Atores:** Bibliotecário, Usuário (autoatendimento)

**Fluxo principal:**
1. Usuário solicita empréstimo de um título/exemplar.
2. Sistema valida se o usuário está ativo e não está bloqueado (sem atrasos e sem multas pendentes).
3. Sistema verifica se o usuário atingiu o limite de empréstimos simultâneos para seu tipo.
4. Sistema verifica se existe exemplar com status `DISPONÍVEL` para o título.
5. Se houver exemplar disponível: cria o empréstimo, define data prevista de devolução (baseada no tipo do usuário) e altera o status do exemplar para `EMPRESTADO`.
6. Se não houver exemplar disponível: nega o empréstimo e oferece inclusão na fila de reserva.

**Pós-condição:** Exemplar emprestado ao usuário com data de vencimento definida.

### CU02 — Realizar devolução de exemplar

**Atores:** Bibliotecário, Usuário (autoatendimento)

**Fluxo principal:**
1. Usuário devolve um exemplar emprestado.
2. Sistema localiza o empréstimo ativo vinculado ao exemplar e ao usuário.
3. Sistema calcula atraso (se data real > data prevista) e gera multa, se aplicável.
4. Sistema verifica estado do exemplar devolvido:
   - Se em boas condições: status volta para `DISPONÍVEL`.
   - Se danificado/perdido: status muda para `DANIFICADO`/`PERDIDO`, gera taxa de reposição e baixa do exemplar.
5. Se houver usuários na fila de reserva desse título, notifica o primeiro da fila sobre a disponibilidade.

**Pós-condição:** Empréstimo encerrado, exemplar disponível (ou baixado), multa aplicada se houver atraso.

### CU03 — Consultar fila de reserva

**Ator:** Usuário / Bibliotecário

- Consulta posição na fila de reserva por título.
- Recebe notificação quando um exemplar do título reservado fica disponível.

---

## 5. Requisitos funcionais (RF)

| ID | Requisito |
|----|-----------|
| RF01 | O sistema deve permitir empréstimo de exemplar apenas se o usuário estiver ativo, não bloqueado e dentro do limite de seu tipo. |
| RF02 | O sistema deve definir a data prevista de devolução conforme o tipo do usuário (14, 14 ou 30 dias). |
| RF03 | O sistema deve negar empréstimo quando não houver exemplar `DISPONÍVEL` e oferecer reserva na fila. |
| RF04 | O sistema deve controlar múltiplos exemplares por título, cada um com código de tombo único. |
| RF05 | O sistema deve calcular multa automática por dia de atraso na devolução. |
| RF06 | O sistema deve bloquear novos empréstimos para usuários com livros em atraso ou multas pendentes. |
| RF07 | O sistema deve permitir devolução e atualizar o status do exemplar (DISPONÍVEL, DANIFICADO, PERDIDO, EM_REPARO). |
| RF08 | Para exemplar danificado/perdido, o sistema deve cobrar taxa de reposição e dar baixa no exemplar. |
| RF09 | O sistema deve permitir operações por bibliotecários e por autoatendimento do usuário. |
| RF10 | O sistema deve validar que usuário e exemplar estão ativos antes de qualquer empréstimo ou devolução. |

---

## 6. Regras de negócio (RN)

| ID | Regra |
|----|-------|
| RN01 | Aluno: máximo 3 livros, prazo 14 dias. |
| RN02 | Professor: máximo 5 livros, prazo 30 dias. |
| RN03 | Funcionário: máximo 3 livros, prazo 14 dias. |
| RN04 | Exemplar com status EM_REPARO, DANIFICADO ou PERDIDO não pode ser emprestado. |
| RN05 | Todos os exemplares emprestados → empréstimo negado + entrada na fila de reserva. |
| RN06 | Usuário com atraso ou multa pendente = bloqueado para novo empréstimo. |
| RN07 | Perda/dano do exemplar → taxa de reposição ao usuário + baixa do exemplar. |

---

## 7. Casos de borda e exceções

| Caso | Comportamento esperado |
|------|------------------------|
| Usuário inativo tenta empréstimo | Operação negada com mensagem de cadastro inativo. |
| Exemplar inativo/inexistente | Operação negada; exemplar não encontrado ou inativo. |
| Usuário atinge limite de livros do seu tipo | Empréstimo negado; informar limite atingido. |
| Todos os exemplares de um título emprestados | Empréstimo negado; opção de entrar na fila de reserva. |
| Devolução fora do prazo | Calcular multa por dia de atraso e bloquear usuário até quitação. |
| Devolução de exemplar danificado | Cobrar taxa de reposição, dar baixa no exemplar, não voltar ao acervo. |
| Usuário com reserva e exemplar fica disponível | Notificar o primeiro da fila; reserva expira após **3 dias corridos** da notificação, passando a vaga para o próximo da fila. |
| Empréstimo duplicado do mesmo exemplar ao mesmo tempo | Impedido (exemplar já está EMPRESTADO). |
| Autoatendimento por usuário bloqueado | Negado; mesmo fluxo de validação do bibliotecário. |

---

## 8. Critérios de aceitação

- [ ] É possível emprestar um exemplar `DISPONÍVEL` a um usuário ativo dentro do limite de seu tipo.
- [ ] A data prevista de devolução é 14 dias (aluno/funcionário) ou 30 dias (professor).
- [ ] O empréstimo é negado se não houver exemplares disponíveis, com opção de reserva.
- [ ] Usuário com livro atrasado ou multa pendente não consegue novo empréstimo.
- [ ] A devolução com atraso gera multa automática por dia.
- [ ] Um exemplar devolvido danificado/perdido gera taxa de reposição e recebe baixa.
- [ ] Exemplares `EM_REPARO`, `DANIFICADO` ou `PERDIDO` não aparecem como disponíveis para empréstimo.
- [ ] Bibliotecário e usuário (autoatendimento) conseguem executar empréstimo/devolução, desde que as validações de cadastro ativo passem.
- [ ] A multa por atraso é calculada em **R$ 2,00 por dia** (ex: 3 dias de atraso = R$ 6,00).
- [ ] A taxa de reposição para exemplar danificado/perdido é de **R$ 50,00 por exemplar**.
- [ ] A reserva expira após **3 dias corridos** da notificação de disponibilidade, passando a vaga para o próximo da fila.

---

## 9. Requisitos não funcionais (sugestão)

- Consistência de dados: empréstimo e atualização de status do exemplar devem ser atômicos (transação).
- Auditoria: todo empréstimo/devolução deve registrar usuário operador, data/hora e exemplar envolvido.
- Performance: consulta de disponibilidade de exemplar deve ser rápida (índice por título + status).

---

## Próximos passos (após validação deste spec)

1. Revisar e aprovar este `spec.md` com o stakeholder.
2. Gerar classes de domínio (Entidades) a partir das regras definidas.
3. Implementar camada de serviço e validações com base nos critérios de aceitação.
