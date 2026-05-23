package com.gjw.util;

import com.gjw.dao.ConexaoDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe utilitária responsável pela carga de dados iniciais do sistema.
 * Contém dados de demonstração para facilitar a avaliação e testes.
 *
 * Projeto GJW - 2026
 * UC Dual - Programação de Soluções Computacionais
 */
public class DadosIniciais {

    /**
     * Popula o banco com dados de demonstração.
     * Inclui usuários de todos os perfis e cargos,
     * equipes, projetos, tarefas e alocações.
     */
    public static void carregar() {
        try (Connection conn = ConexaoDB.getConexao();
             Statement stmt = conn.createStatement()) {

            carregarUsuarios(stmt);
            carregarEquipes(stmt);
            carregarMembros(stmt);
            carregarProjetos(stmt);
            carregarAlocacoes(stmt);
            carregarTarefas(stmt);

            System.out.println("Dados iniciais carregados com sucesso!");
            System.out.println("  7 usuarios | 2 equipes | 2 projetos | 6 tarefas");

        } catch (SQLException e) {
            System.out.println("Erro ao carregar dados iniciais: " + e.getMessage());
        }
    }

    /**
     * Usuários — cobre todos os perfis (Admin, Gerente, Colaborador)
     * e todos os cargos disponíveis no sistema.
     */
    private static void carregarUsuarios(Statement stmt) throws SQLException {
        stmt.execute("""
            INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil) VALUES
            ('Adilson Menezes',            '11100011100', 'adilson@gjw.com',   'ANALISTA_DE_SISTEMAS',  'amenezes',   'senha123', 'ADMINISTRADOR'),
            ('Jeziel Tapia Brizolla',      '00365753050', 'jeziel@gjw.com',    'DESENVOLVEDOR',         'jtbrizolla', 'senha123', 'GERENTE'),
            ('Gertrudes Azevedo',          '77788899900', 'gertrudes@gjw.com', 'GERENTE_DE_PROJETOS',   'gazevedo',   'senha123', 'GERENTE'),
            ('Glauciele Cristina Caetano', '98765432100', 'glauciele@gjw.com', 'ANALISTA_DE_SISTEMAS',  'gcsilva',    'senha123', 'COLABORADOR'),
            ('Ana da Silva',               '11122233344', 'ana@gjw.com',       'ANALISTA_DE_SISTEMAS',  'asilva',     'senha123', 'COLABORADOR'),
            ('Douglas Martins',            '55566677788', 'douglas@gjw.com',   'DESENVOLVEDOR',         'dmartins',   'senha123', 'COLABORADOR'),
            ('Denise Santos',              '33344455566', 'denise@gjw.com',    'DESIGNER_DE_INTERFACE', 'dsantos',    'senha123', 'COLABORADOR')
        """);
    }

    /**
     * Equipes — duas equipes com nomes e descrições distintos.
     */
    private static void carregarEquipes(Statement stmt) throws SQLException {
        stmt.execute("""
            INSERT INTO equipes (nome, descricao) VALUES
            ('Equipe GJW BETA',  'Equipe Beta de desenvolvimento do projeto GJW 2026'),
            ('Equipe GJW ALPHA', 'Equipe Alpha de suporte ao projeto GJW 2026')
        """);
    }

    /**
     * Membros — vincula usuários às equipes.
     * Equipe BETA: Jeziel e Glauciele
     * Equipe ALPHA: Ana, Douglas e Denise
     */
    private static void carregarMembros(Statement stmt) throws SQLException {
        stmt.execute("""
            INSERT INTO equipe_membros (equipe_id, usuario_id) VALUES
            (1, 2),
            (1, 4),
            (2, 5),
            (2, 6),
            (2, 7)
        """);
    }

    /**
     * Projetos — dois projetos com gerentes distintos.
     */
    private static void carregarProjetos(Statement stmt) throws SQLException {
        stmt.execute("""
            INSERT INTO projetos (nome, descricao, data_inicio, data_termino_prevista, status, gerente_id) VALUES
            ('Sistema de Gestao GJW',         'Sistema para gestao de projetos e equipes Oracle', '11/04/2026', '15/05/2026', 'PLANEJADO',    2),
            ('Refatoracao da API REST Oracle', 'Refatoracao e melhoria da API REST da Oracle',     '16/04/2026', '15/05/2026', 'EM_ANDAMENTO', 3)
        """);
    }

    /**
     * Alocações — vincula equipes aos projetos.
     */
    private static void carregarAlocacoes(Statement stmt) throws SQLException {
        stmt.execute("""
            INSERT INTO projeto_equipes (projeto_id, equipe_id) VALUES
            (1, 1),
            (2, 2)
        """);
    }

    /**
     * Tarefas — distribuídas entre os dois projetos
     * com status variados para demonstrar o relatório.
     */
    private static void carregarTarefas(Statement stmt) throws SQLException {
        stmt.execute("""
            INSERT INTO tarefas (nome, descricao, prazo, status, responsavel_id, projeto_id) VALUES
            ('Implementar cadastro de usuarios', 'Desenvolver CRUD completo de usuarios',     '20/04/2026', 'CONCLUIDA',    2, 1),
            ('Implementar cadastro de equipes',  'Desenvolver CRUD completo de equipes',      '22/04/2026', 'CONCLUIDA',    2, 1),
            ('Implementar relatorios',           'Desenvolver relatorios de desempenho',       '30/04/2026', 'EM_ANDAMENTO', 4, 1),
            ('Mapear endpoints existentes',      'Documentar todos os endpoints da API atual', '25/04/2026', 'CONCLUIDA',    6, 2),
            ('Refatorar autenticacao',           'Melhorar o sistema de autenticacao da API',  '05/05/2026', 'EM_ANDAMENTO', 6, 2),
            ('Criar testes de integracao',       'Desenvolver testes para os novos endpoints', '10/05/2026', 'PENDENTE',     5, 2)
        """);
    }
}