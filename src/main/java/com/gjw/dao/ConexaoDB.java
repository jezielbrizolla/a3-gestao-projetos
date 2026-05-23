package com.gjw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável pela conexão com o banco de dados H2.
 * Gerencia apenas a conexão e a criação das tabelas.
 */
public class ConexaoDB {

    private static final String URL = "jdbc:h2:./gestao_projetos;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Retorna uma conexão ativa com o banco de dados.
     */
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Cria todas as tabelas do sistema se ainda não existirem.
     */
    public static void inicializarBanco() {
        try (Connection conn = getConexao();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome_completo VARCHAR(100) NOT NULL,
                    cpf VARCHAR(14) NOT NULL UNIQUE,
                    email VARCHAR(100) NOT NULL,
                    cargo VARCHAR(30) NOT NULL,
                    login VARCHAR(50) NOT NULL UNIQUE,
                    senha VARCHAR(50) NOT NULL,
                    perfil VARCHAR(20) NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS equipes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    descricao VARCHAR(255)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS projetos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    descricao VARCHAR(255),
                    data_inicio VARCHAR(10) NOT NULL,
                    data_termino_prevista VARCHAR(10) NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    gerente_id INT,
                    FOREIGN KEY (gerente_id) REFERENCES usuarios(id)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tarefas (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    descricao VARCHAR(255),
                    prazo VARCHAR(10) NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    responsavel_id INT,
                    projeto_id INT NOT NULL,
                    FOREIGN KEY (responsavel_id) REFERENCES usuarios(id),
                    FOREIGN KEY (projeto_id) REFERENCES projetos(id)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS equipe_membros (
                    equipe_id INT NOT NULL,
                    usuario_id INT NOT NULL,
                    PRIMARY KEY (equipe_id, usuario_id),
                    FOREIGN KEY (equipe_id) REFERENCES equipes(id),
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS projeto_equipes (
                    projeto_id INT NOT NULL,
                    equipe_id INT NOT NULL,
                    PRIMARY KEY (projeto_id, equipe_id),
                    FOREIGN KEY (projeto_id) REFERENCES projetos(id),
                    FOREIGN KEY (equipe_id) REFERENCES equipes(id)
                )
            """);

            System.out.println("Banco de dados inicializado!");

        } catch (SQLException e) {
            System.out.println("Erro ao inicializar banco: " + e.getMessage());
        }
    }

    /**
     * Verifica se o banco está vazio — sem usuários cadastrados.
     */
    public static boolean bancoEstaVazio() {
        try (Connection conn = getConexao();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            rs.next();
            return rs.getInt(1) == 0;
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Apaga todos os dados de todas as tabelas respeitando a ordem das foreign keys.
     */
    public static void limparBanco() {
        try (Connection conn = getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM projeto_equipes");
            stmt.execute("DELETE FROM equipe_membros");
            stmt.execute("DELETE FROM tarefas");
            stmt.execute("DELETE FROM projetos");
            stmt.execute("DELETE FROM equipes");
            stmt.execute("DELETE FROM usuarios");
            stmt.execute("ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 1");
            stmt.execute("ALTER TABLE equipes ALTER COLUMN id RESTART WITH 1");
            stmt.execute("ALTER TABLE projetos ALTER COLUMN id RESTART WITH 1");
            stmt.execute("ALTER TABLE tarefas ALTER COLUMN id RESTART WITH 1");
        } catch (SQLException e) {
            System.out.println("Erro ao limpar banco: " + e.getMessage());
        }
    }
}