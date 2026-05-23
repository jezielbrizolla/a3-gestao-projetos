package com.gjw.dao;

import com.gjw.model.Projeto;
import com.gjw.model.StatusProjeto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    // Insere um novo projeto no banco
    public boolean inserir(Projeto projeto) {
        String sql = """
                INSERT INTO projetos (nome, descricao, data_inicio, data_termino_prevista, status, gerente_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setString(3, projeto.getDataInicio());
            stmt.setString(4, projeto.getDataTerminoPrevista());
            stmt.setString(5, projeto.getStatus().name());
            stmt.setInt(6, projeto.getGerenteId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) projeto.setId(rs.getInt(1));

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir projeto: " + e.getMessage());
            return false;
        }
    }

    // Retorna todos os projetos cadastrados
    public List<Projeto> listarTodos() {
        List<Projeto> lista = new ArrayList<>();
        String sql = "SELECT * FROM projetos";
        try (Connection conn = ConexaoDB.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar projetos: " + e.getMessage());
        }
        return lista;
    }

    // Busca um projeto pelo ID
    public Projeto buscarPorId(int id) {
        String sql = "SELECT * FROM projetos WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar projeto: " + e.getMessage());
        }
        return null;
    }

    // Atualiza dados completos de um projeto
    public boolean atualizar(int id, String nome, String descricao,
                             String dataInicio, String dataTermino, int gerenteId) {
        String sql = """
                UPDATE projetos SET nome = ?, descricao = ?, data_inicio = ?,
                data_termino_prevista = ?, gerente_id = ? WHERE id = ?
                """;
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setString(3, dataInicio);
            stmt.setString(4, dataTermino);
            stmt.setInt(5, gerenteId);
            stmt.setInt(6, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar projeto: " + e.getMessage());
            return false;
        }
    }

    // Atualiza o status de um projeto
    public boolean atualizarStatus(int id, StatusProjeto status) {
        String sql = "UPDATE projetos SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status: " + e.getMessage());
            return false;
        }
    }

    // Aloca uma equipe em um projeto
    public boolean alocarEquipe(int projetoId, int equipeId) {
        String sql = "INSERT INTO projeto_equipes (projeto_id, equipe_id) VALUES (?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            stmt.setInt(2, equipeId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao alocar equipe: " + e.getMessage());
            return false;
        }
    }

    // Verifica se equipe já está alocada no projeto
    public boolean equipeAlocada(int projetoId, int equipeId) {
        String sql = "SELECT 1 FROM projeto_equipes WHERE projeto_id = ? AND equipe_id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            stmt.setInt(2, equipeId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    // Lista IDs das equipes alocadas em um projeto
    public List<Integer> listarEquipesDoprojeto(int projetoId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT equipe_id FROM projeto_equipes WHERE projeto_id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) ids.add(rs.getInt("equipe_id"));
        } catch (SQLException e) {
            System.out.println("Erro ao listar equipes do projeto: " + e.getMessage());
        }
        return ids;
    }

    // Remove um projeto pelo ID
    public boolean remover(int id) {
        String sql = "DELETE FROM projetos WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao remover projeto: " + e.getMessage());
            return false;
        }
    }

    // Converte uma linha do banco em objeto Projeto
    private Projeto mapear(ResultSet rs) throws SQLException {
        Projeto p = new Projeto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getString("data_inicio"),
                rs.getString("data_termino_prevista"),
                rs.getInt("gerente_id")
        );
        p.setStatus(StatusProjeto.valueOf(rs.getString("status")));
        return p;
    }
}