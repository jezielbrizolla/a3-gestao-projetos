package com.gjw;

import com.gjw.dao.ConexaoDB;
import com.gjw.model.*;
import com.gjw.service.*;
import com.gjw.util.DadosIniciais;

import java.util.Scanner;

/**
 * Classe principal do Sistema de Gestão de Projetos e Equipes.
 * Projeto GJW - 2026 | UC Dual - Programação de Soluções Computacionais
 *
 * Integrantes: Glauciele Cristina Caetano Silva, Jeziel Tapia Brizolla
 */
public class Main {

    static Scanner sc = new Scanner(System.in);
    static UsuarioService usuarioService   = new UsuarioService();
    static EquipeService equipeService     = new EquipeService();
    static ProjetoService projetoService   = new ProjetoService();

    public static void main(String[] args) {

        // Inicializa o banco e cria as tabelas
        ConexaoDB.inicializarBanco();

        // Verifica se o banco está vazio e pergunta sobre carga inicial
        if (ConexaoDB.bancoEstaVazio()) {
            System.out.println("\n+--------------------------------------------------+");
            System.out.println("| Banco de dados vazio detectado.                  |");
            System.out.println("| Deseja carregar os dados iniciais de exemplo?    |");
            System.out.println("+--------------------------------------------------+");
            System.out.print("  Digite S para Sim ou N para Não: ");
            String resposta = sc.nextLine().trim().toUpperCase();
            if (resposta.equals("S")) {
                DadosIniciais.carregar();
            } else {
                System.out.println("Sistema iniciado sem dados. Você pode cadastrar tudo pelo menu.");
            }
        }

        System.out.println("\n=========================================");
        System.out.println("  Sistema de Gestão de Projetos e Equipes");
        System.out.println("  Projeto GJW - 2026");
        System.out.println("=========================================");

        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerInt("Escolha: ");
            switch (opcao) {
                case 1: menuUsuarios();   break;
                case 2: menuEquipes();    break;
                case 3: menuProjetos();   break;
                case 4: menuRelatorios();    break;
                case 5: restaurarDados();   break;
                case 0: System.out.println("Encerrando sistema. Até logo!"); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // =====================================================================
    // MENU PRINCIPAL
    // =====================================================================

    static void exibirMenuPrincipal() {
        System.out.println("\n========== MENU PRINCIPAL ==========");
        System.out.println("1. Gerenciar Usuários");
        System.out.println("2. Gerenciar Equipes");
        System.out.println("3. Gerenciar Projetos");
        System.out.println("4. Relatórios");
        System.out.println("5. Restaurar dados de demonstração");
        System.out.println("0. Sair");
        System.out.println("=====================================");
    }

    // =====================================================================
    // MENU USUÁRIOS
    // =====================================================================

    static void menuUsuarios() {
        int opcao;
        do {
            System.out.println("\n--- USUÁRIOS ---");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Listar usuários");
            System.out.println("3. Atualizar usuário");
            System.out.println("4. Remover usuário");
            System.out.println("0. Voltar");
            opcao = lerInt("Escolha: ");
            switch (opcao) {
                case 1: cadastrarUsuario();      break;
                case 2: usuarioService.listar(); break;
                case 3: atualizarUsuario();      break;
                case 4: removerUsuario();        break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    static void cadastrarUsuario() {
        System.out.println("\n-- Cadastrar Usuário --");

        // Validação nome
        String nome;
        do {
            System.out.print("Nome completo: ");
            nome = sc.nextLine().trim();
            if (nome.isEmpty()) System.out.println("Nome não pode ser vazio.");
        } while (nome.isEmpty());

        // Validação CPF — exatamente 11 dígitos numéricos
        String cpf;
        do {
            System.out.print("CPF (11 dígitos, somente números): ");
            cpf = sc.nextLine().trim();
            if (!cpf.matches("\\d{11}")) System.out.println("CPF inválido. Digite exatamente 11 números.");
        } while (!cpf.matches("\\d{11}"));

        // Validação e-mail
        String email;
        do {
            System.out.print("E-mail: ");
            email = sc.nextLine().trim();
            if (!email.contains("@") || !email.contains(".")) System.out.println("E-mail inválido.");
        } while (!email.contains("@") || !email.contains("."));

        Cargo cargo = escolherCargo();

        // Validação login
        String login;
        do {
            System.out.print("Login: ");
            login = sc.nextLine().trim();
            if (login.isEmpty()) System.out.println("Login não pode ser vazio.");
        } while (login.isEmpty());

        // Validação senha
        String senha;
        do {
            System.out.print("Senha (mínimo 6 caracteres): ");
            senha = sc.nextLine().trim();
            if (senha.length() < 6) System.out.println("Senha deve ter pelo menos 6 caracteres.");
        } while (senha.length() < 6);

        String perfil = escolherPerfil();
        usuarioService.cadastrar(nome, cpf, email, cargo, login, senha, perfil);
    }

    static void atualizarUsuario() {
        usuarioService.listar();
        int id = lerInt("ID do usuário a atualizar: ");
        System.out.print("Novo nome completo: ");
        String nome = sc.nextLine().trim();
        System.out.print("Novo e-mail: ");
        String email = sc.nextLine().trim();
        Cargo novoCargo = escolherCargo();
        usuarioService.atualizar(id, nome, email, novoCargo);
    }

    static void removerUsuario() {
        usuarioService.listar();
        int id = lerInt("ID do usuário a remover: ");
        usuarioService.remover(id);
    }

    // =====================================================================
    // MENU EQUIPES
    // =====================================================================

    static void menuEquipes() {
        int opcao;
        do {
            System.out.println("\n--- EQUIPES ---");
            System.out.println("1. Cadastrar equipe");
            System.out.println("2. Listar equipes");
            System.out.println("3. Adicionar membro à equipe");
            System.out.println("4. Remover membro da equipe");
            System.out.println("5. Remover equipe");
            System.out.println("0. Voltar");
            opcao = lerInt("Escolha: ");
            switch (opcao) {
                case 1: cadastrarEquipe();       break;
                case 2: equipeService.listar();  break;
                case 3: adicionarMembro();       break;
                case 4: removerMembro();         break;
                case 5: removerEquipe();         break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    static void cadastrarEquipe() {
        System.out.println("\n-- Cadastrar Equipe --");
        System.out.print("Nome da equipe: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        equipeService.cadastrar(nome, descricao);
    }

    static void adicionarMembro() {
        equipeService.listar();
        int eqId = lerInt("ID da equipe: ");
        usuarioService.listar();
        int usId = lerInt("ID do usuário a adicionar: ");
        equipeService.adicionarMembro(eqId, usId);
    }

    static void removerMembro() {
        equipeService.listar();
        int eqId = lerInt("ID da equipe: ");
        usuarioService.listar();
        int usId = lerInt("ID do usuário a remover: ");
        equipeService.removerMembro(eqId, usId);
    }

    static void removerEquipe() {
        equipeService.listar();
        int id = lerInt("ID da equipe a remover: ");
        equipeService.remover(id);
    }

    // =====================================================================
    // MENU PROJETOS
    // =====================================================================

    static void menuProjetos() {
        int opcao;
        do {
            System.out.println("\n--- PROJETOS ---");
            System.out.println("1. Cadastrar projeto");
            System.out.println("2. Listar projetos");
            System.out.println("3. Atualizar projeto");
            System.out.println("4. Atualizar status do projeto");
            System.out.println("5. Adicionar tarefa ao projeto");
            System.out.println("6. Atualizar status de tarefa");
            System.out.println("7. Alocar equipe ao projeto");
            System.out.println("8. Remover projeto");
            System.out.println("0. Voltar");
            opcao = lerInt("Escolha: ");
            switch (opcao) {
                case 1: cadastrarProjeto();       break;
                case 2: projetoService.listar();  break;
                case 3: atualizarProjeto();       break;
                case 4: atualizarStatusProjeto(); break;
                case 5: adicionarTarefa();        break;
                case 6: atualizarStatusTarefa();  break;
                case 7: alocarEquipe();           break;
                case 8: removerProjeto();         break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    static void cadastrarProjeto() {
        System.out.println("\n-- Cadastrar Projeto --");
        System.out.print("Nome do projeto: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Data de início (dd/mm/aaaa): ");
        String inicio = sc.nextLine();
        System.out.print("Data de término prevista (dd/mm/aaaa): ");
        String termino = sc.nextLine();

        // Mostra apenas gerentes disponíveis
        System.out.println("\nGerentes disponíveis:");
        usuarioService.listarPorPerfil("GERENTE");
        int gId = lerInt("ID do gerente responsável: ");
        projetoService.cadastrar(nome, descricao, inicio, termino, gId);
    }

    static void atualizarProjeto() {
        projetoService.listar();
        int pId = lerInt("ID do projeto a atualizar: ");
        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        System.out.print("Nova descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Nova data de início (dd/mm/aaaa): ");
        String inicio = sc.nextLine();
        System.out.print("Nova data de término prevista (dd/mm/aaaa): ");
        String termino = sc.nextLine();
        System.out.println("\nGerentes disponíveis:");
        usuarioService.listarPorPerfil("GERENTE");
        int gId = lerInt("ID do novo gerente responsável: ");
        projetoService.atualizarProjeto(pId, nome, descricao, inicio, termino, gId);
    }

    static void atualizarStatusProjeto() {
        projetoService.listar();
        int pId = lerInt("ID do projeto: ");
        System.out.println("Status:");
        System.out.println("  1 - PLANEJADO");
        System.out.println("  2 - EM ANDAMENTO");
        System.out.println("  3 - CONCLUIDO");
        System.out.println("  4 - CANCELADO");
        int s = lerInt("Escolha: ");
        StatusProjeto status;
        switch (s) {
            case 2: status = StatusProjeto.EM_ANDAMENTO; break;
            case 3: status = StatusProjeto.CONCLUIDO;    break;
            case 4: status = StatusProjeto.CANCELADO;    break;
            default: status = StatusProjeto.PLANEJADO;
        }
        projetoService.atualizarStatus(pId, status);
    }

    static void adicionarTarefa() {
        projetoService.listar();
        int pId = lerInt("ID do projeto: ");
        System.out.println("\n-- Nova Tarefa --");
        System.out.print("Nome da tarefa: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Prazo (dd/mm/aaaa): ");
        String prazo = sc.nextLine();
        usuarioService.listar();
        int uId = lerInt("ID do responsável (0 = sem responsável): ");
        projetoService.adicionarTarefa(pId, nome, descricao, prazo, uId);
    }

    static void atualizarStatusTarefa() {
        projetoService.listar();
        int pId = lerInt("ID do projeto: ");
        Projeto p = projetoService.buscarPorId(pId);
        if (p == null) return;
        System.out.println("ID da tarefa (consulte o relatório do projeto para ver as tarefas):");
        int tId = lerInt("ID da tarefa: ");
        System.out.println("Status:");
        System.out.println("  1 - PENDENTE");
        System.out.println("  2 - EM ANDAMENTO");
        System.out.println("  3 - CONCLUIDA");
        System.out.println("  4 - CANCELADA");
        int s = lerInt("Escolha: ");
        StatusTarefa status;
        switch (s) {
            case 2: status = StatusTarefa.EM_ANDAMENTO; break;
            case 3: status = StatusTarefa.CONCLUIDA;    break;
            case 4: status = StatusTarefa.CANCELADA;    break;
            default: status = StatusTarefa.PENDENTE;
        }
        projetoService.atualizarStatusTarefa(tId, status);
    }

    static void alocarEquipe() {
        projetoService.listar();
        int pId = lerInt("ID do projeto: ");
        equipeService.listar();
        int eId = lerInt("ID da equipe: ");
        projetoService.alocarEquipe(pId, eId);
    }

    static void removerProjeto() {
        projetoService.listar();
        int id = lerInt("ID do projeto a remover: ");
        projetoService.remover(id);
    }

    // =====================================================================
    // MENU RELATÓRIOS — com do/while e 3 relatórios completos
    // =====================================================================

    static void menuRelatorios() {
        int opcao;
        do {
            System.out.println("\n--- RELATÓRIOS ---");
            System.out.println("1. Relatório detalhado de um projeto");
            System.out.println("2. Relatório consolidado do portfólio");
            System.out.println("3. Relatório por equipe");
            System.out.println("0. Voltar");
            opcao = lerInt("Escolha: ");
            switch (opcao) {
                case 1:
                    projetoService.listar();
                    int id = lerInt("ID do projeto: ");
                    projetoService.exibirRelatorio(id);
                    break;
                case 2:
                    projetoService.exibirRelatorioPortfolio();
                    break;
                case 3:
                    equipeService.listar();
                    int eqId = lerInt("ID da equipe: ");
                    projetoService.exibirRelatorioEquipe(eqId);
                    break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // =====================================================================
    // RESTAURAR DADOS DE DEMONSTRAÇÃO
    // =====================================================================

    static void restaurarDados() {
        System.out.print("\n  Senha de administrador: ");
        String senha = sc.nextLine().trim();
        if (!senha.equals("123456")) {
            System.out.println("Senha incorreta. Operação cancelada.");
            return;
        }
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("| ATENÇÃO: todos os dados atuais serão apagados!   |");
        System.out.println("| Os dados de demonstração serão recarregados.     |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("  Tem certeza? Digite S para confirmar ou N para cancelar: ");
        String resposta = sc.nextLine().trim().toUpperCase();
        if (resposta.equals("S")) {
            ConexaoDB.limparBanco();
            DadosIniciais.carregar();
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    // =====================================================================
    // MÉTODOS AUXILIARES
    // =====================================================================

    static Cargo escolherCargo() {
        System.out.println("Cargo:");
        System.out.println("  1 - DESENVOLVEDOR");
        System.out.println("  2 - ANALISTA DE SISTEMAS");
        System.out.println("  3 - DESIGNER DE INTERFACE");
        System.out.println("  4 - GERENTE DE PROJETOS");
        int c = lerInt("Escolha: ");
        switch (c) {
            case 1: return Cargo.DESENVOLVEDOR;
            case 2: return Cargo.ANALISTA_DE_SISTEMAS;
            case 3: return Cargo.DESIGNER_DE_INTERFACE;
            case 4: return Cargo.GERENTE_DE_PROJETOS;
            default:
                System.out.println("Opção inválida. Definindo como DESENVOLVEDOR.");
                return Cargo.DESENVOLVEDOR;
        }
    }

    static String escolherPerfil() {
        System.out.println("Perfil:");
        System.out.println("  1 - ADMINISTRADOR");
        System.out.println("  2 - GERENTE");
        System.out.println("  3 - COLABORADOR");
        int p = lerInt("Escolha: ");
        switch (p) {
            case 1: return "ADMINISTRADOR";
            case 2: return "GERENTE";
            default: return "COLABORADOR";
        }
    }

    static int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }
}