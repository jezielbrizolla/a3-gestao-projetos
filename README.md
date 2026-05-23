# Sistema de Gestão de Projetos e Equipes
**Projeto GJW - 2026**  
UC Dual - Programação de Soluções Computacionais - Digital 2026-1

**Integrantes:**
- Glauciele Cristina Caetano Silva
- Jeziel Tapia Brizolla

---

## Objetivo

Sistema desenvolvido em Java para gerenciar projetos e equipes em um contexto corporativo, simulando uma demanda real de clientes Oracle. O sistema permite o cadastro e controle de usuários (com diferentes perfis de acesso), projetos, equipes, tarefas e emite relatórios de desempenho — tudo via interface de menu no terminal.

---

## Tecnologias utilizadas

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 25 (LTS) | Linguagem principal |
| Maven | 3.x | Gerenciador de dependências e build |
| H2 Database | 2.2.224 | Banco de dados embutido (sem instalação externa) |
| JDBC | — | API de acesso ao banco de dados |
| IntelliJ IDEA | 2026.x | IDE recomendada |

---

## Pré-requisitos

Antes de rodar o projeto, verifique se você tem instalado:

- **Java JDK 25** — obrigatório (o projeto não compila com versões anteriores)  
  Download: https://adoptium.net
- **Maven 3.x** — necessário para build via terminal  
  Download: https://maven.apache.org/download.cgi  
  *(Usuários IntelliJ: o Maven já vem embutido na IDE, não é necessário instalar separadamente)*

Para verificar as versões instaladas:

```bash
java -version
mvn -version
```

---

## Como instalar e rodar

### Via terminal (Maven)

**1. Clone o repositório:**
```bash
git clone https://github.com/jezielbrizolla/gestao-projetos.git
cd gestao-projetos
```

**2. Instale as dependências:**
```bash
mvn install -DskipTests
```

**3. Execute o sistema:**
```bash
mvn exec:java -Dexec.mainClass="com.gjw.Main"
```

### Via IntelliJ IDEA

1. Abra o IntelliJ IDEA
2. Clique em **File → Open** e selecione a pasta do projeto
3. Aguarde o Maven baixar as dependências automaticamente
4. Abra o arquivo `src/main/java/com/gjw/Main.java`
5. Clique no botão **Run** (▶) ou pressione `Shift + F10`

> O banco de dados H2 é criado automaticamente no diretório raiz do projeto como `gestao_projetos.mv.db`. Não é necessário instalar nenhum banco de dados externo.

---

## Dados iniciais

### Primeira execução (banco vazio)

Na primeira execução, o sistema detecta que o banco está vazio e pergunta:

```
+--------------------------------------------------+
| Banco de dados vazio detectado.                  |
| Deseja carregar os dados iniciais de exemplo?    |
+--------------------------------------------------+
  Digite S para Sim ou N para Não:
```

- **S** — carrega dados de demonstração com 7 usuários, 2 equipes, 2 projetos e 6 tarefas
- **N** — inicia o sistema limpo para cadastrar seus próprios dados

### Restaurar dados a qualquer momento

O menu principal oferece a opção **5. Restaurar dados de demonstração**, que apaga todos os dados atuais e recarrega os dados de exemplo. Uma senha de administrador e uma confirmação são exigidas para evitar restaurações acidentais.

---

## Funcionalidades

### Gerenciamento de Usuários
- Cadastro com nome, CPF (11 dígitos), e-mail, cargo, login e senha (mínimo 6 caracteres)
- Validação de CPF e login únicos no sistema
- Perfis de acesso: **Administrador**, **Gerente** e **Colaborador**
- Cargos disponíveis: Desenvolvedor, Analista de Sistemas, Designer de Interface, Gerente de Projetos
- Atualização de nome, e-mail e cargo
- Remoção de usuários

### Gerenciamento de Equipes
- Cadastro de equipes com nome e descrição
- Adição e remoção de membros
- Regra: um usuário não pode ser adicionado duas vezes na mesma equipe
- Uma equipe pode participar de múltiplos projetos simultaneamente

### Gerenciamento de Projetos
- Cadastro com nome, descrição, datas de início e término, e gerente responsável
- Regra: somente usuários com perfil **Gerente** podem ser responsáveis por projetos
- Atualização completa dos dados do projeto
- Controle de status: **Planejado**, **Em Andamento**, **Concluído**, **Cancelado**
- Alocação de equipes ao projeto (regra: mesma equipe não pode ser alocada duas vezes)

### Gerenciamento de Tarefas
- Cadastro de tarefas vinculadas a projetos com nome, descrição, prazo e responsável
- Controle de status: **Pendente**, **Em Andamento**, **Concluída**, **Cancelada**

### Relatórios
- **Relatório detalhado por projeto** — exibe status, gerente, datas, progresso geral, equipes alocadas e lista de tarefas com responsáveis
- **Relatório consolidado do portfólio** — visão gerencial de todos os projetos com progresso individual e totalizadores
- **Relatório por equipe** — exibe membros com cargos e perfis, e os projetos em que a equipe atua com progresso

---

## Arquitetura

O projeto segue uma arquitetura em camadas inspirada no padrão MVC, adaptada para aplicação Java via terminal:

```
com.gjw
├── model/       — Entidades do sistema (Usuario abstrata, Administrador, Gerente, Colaborador, Projeto, Equipe, Tarefa)
├── dao/         — Acesso ao banco de dados via JDBC (ConexaoDB, UsuarioDAO, EquipeDAO, ProjetoDAO, TarefaDAO)
├── service/     — Regras de negócio (UsuarioService, EquipeService, ProjetoService)
├── util/        — Utilitários (DadosIniciais)
└── Main.java    — Interface de menu via terminal
```

### Pilares de POO aplicados

| Princípio | Como foi aplicado |
|---|---|
| **Abstração** | `Usuario` é abstrata — define o contrato sem poder ser instanciada diretamente |
| **Encapsulamento** | Todos os atributos são privados, acessados via getters e setters públicos |
| **Herança** | `Administrador`, `Gerente` e `Colaborador` herdam de `Usuario` usando `extends` e `super()` |
| **Polimorfismo** | `getPerfil()` é abstrato em `Usuario` e implementado diferente em cada subclasse; `List<Usuario>` armazena qualquer subtipo |

---

## Estrutura do banco de dados

| Tabela | Descrição |
|---|---|
| `usuarios` | Cadastro de usuários do sistema |
| `equipes` | Cadastro de equipes |
| `projetos` | Cadastro de projetos |
| `tarefas` | Tarefas vinculadas aos projetos |
| `equipe_membros` | Relacionamento N:N usuário ↔ equipe |
| `projeto_equipes` | Relacionamento N:N projeto ↔ equipe |

Obrigada!
