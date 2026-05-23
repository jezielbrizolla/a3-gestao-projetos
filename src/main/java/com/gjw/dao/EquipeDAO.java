package com.gjw.dao;

import com.gjw.model.Equipe;
import com.gjw.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    // Insere uma nova equipe no banco
    public boolean inserir(Equipe equipe) {
        String sql = "INSERT INTO equipes (nome, descricao) VALUES (?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, equipe.getNome());
            stmt.setString(2, equipe.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) equipe.setId(rs.getInt(1));

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir equipe: " + e.getMessage());
            return false;
        }
    }

    // Retorna todas as equipes cadastradas
    public List<Equipe> listarTodos() {
        List<Equipe> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipes";
        try (Connection conn = ConexaoDB.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Equipe(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar equipes: " + e.getMessage());
        }
        return lista;
    }

    // Busca uma equipe pelo ID
    public Equipe buscarPorId(int id) {
        String sql = "SELECT * FROM equipes WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Equipe(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar equipe: " + e.getMessage());
        }
        return null;
    }

    // Remove uma equipe pelo ID
    public boolean remover(int id) {
        String sql = "DELETE FROM equipes WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao remover equipe: " + e.getMessage());
            return false;
        }
    }

    // Adiciona um usuário como membro de uma equipe
    public boolean adicionarMembro(int equipeId, int usuarioId) {
        String sql = "INSERT INTO equipe_membros (equipe_id, usuario_id) VALUES (?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipeId);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar membro: " + e.getMessage());
            return false;
        }
    }

    // Remove um membro de uma equipe
    public boolean removerMembro(int equipeId, int usuarioId) {
        String sql = "DELETE FROM equipe_membros WHERE equipe_id = ? AND usuario_id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipeId);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao remover membro: " + e.getMessage());
            return false;
        }
    }

    // Lista todos os membros de uma equipe
    public List<Usuario> listarMembros(int equipeId) {
        List<Usuario> membros = new ArrayList<>();
        String sql = """
                SELECT u.* FROM usuarios u
                INNER JOIN equipe_membros em ON u.id = em.usuario_id
                WHERE em.equipe_id = ?
                """;
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipeId);
            ResultSet rs = stmt.executeQuery();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            while (rs.next()) {
                membros.add(usuarioDAO.buscarPorId(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar membros: " + e.getMessage());
        }
        return membros;
    }

    // Verifica se usuário já é membro da equipe
    public boolean membroExiste(int equipeId, int usuarioId) {
        String sql = "SELECT 1 FROM equipe_membros WHERE equipe_id = ? AND usuario_id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipeId);
            stmt.setInt(2, usuarioId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
}