package com.gjw.dao;

import com.gjw.model.Tarefa;
import com.gjw.model.StatusTarefa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    // Insere uma nova tarefa no banco
    public boolean inserir(Tarefa tarefa) {
        String sql = """
                INSERT INTO tarefas (nome, descricao, prazo, status, responsavel_id, projeto_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tarefa.getNome());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getPrazo());
            stmt.setString(4, tarefa.getStatus().name());
            stmt.setInt(5, tarefa.getResponsavelId());
            stmt.setInt(6, tarefa.getProjetoId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) tarefa.setId(rs.getInt(1));

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir tarefa: " + e.getMessage());
            return false;
        }
    }

    // Lista todas as tarefas de um projeto
    public List<Tarefa> listarPorProjeto(int projetoId) {
        List<Tarefa> lista = new ArrayList<>();
        String sql = "SELECT * FROM tarefas WHERE projeto_id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projetoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas: " + e.getMessage());
        }
        return lista;
    }

    // Busca uma tarefa pelo ID
    public Tarefa buscarPorId(int id) {
        String sql = "SELECT * FROM tarefas WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar tarefa: " + e.getMessage());
        }
        return null;
    }

    // Atualiza o status de uma tarefa
    public boolean atualizarStatus(int id, StatusTarefa status) {
        String sql = "UPDATE tarefas SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status da tarefa: " + e.getMessage());
            return false;
        }
    }

    // Remove uma tarefa pelo ID
    public boolean remover(int id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao remover tarefa: " + e.getMessage());
            return false;
        }
    }

    // Converte uma linha do banco em objeto Tarefa
    private Tarefa mapear(ResultSet rs) throws SQLException {
        Tarefa t = new Tarefa(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getString("prazo"),
                rs.getInt("responsavel_id"),
                rs.getInt("projeto_id")
        );
        t.setStatus(StatusTarefa.valueOf(rs.getString("status")));
        return t;
    }
}