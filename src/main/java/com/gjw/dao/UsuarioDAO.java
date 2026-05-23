package com.gjw.dao;

import com.gjw.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Insere um novo usuário no banco
    public boolean inserir(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getCargo().name());
            stmt.setString(5, usuario.getLogin());
            stmt.setString(6, usuario.getSenha());
            stmt.setString(7, usuario.getPerfil());
            stmt.executeUpdate();

            // Recupera o ID gerado pelo banco e atribui ao objeto
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) usuario.setId(rs.getInt(1));

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }

    // Retorna todos os usuários cadastrados
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = ConexaoDB.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
        return lista;
    }

    // Busca um usuário pelo ID
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    // Verifica se CPF já está cadastrado
    public boolean cpfExiste(String cpf) {
        String sql = "SELECT id FROM usuarios WHERE cpf = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    // Verifica se login já está cadastrado
    public boolean loginExiste(String login) {
        String sql = "SELECT id FROM usuarios WHERE login = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    // Atualiza nome, email e cargo de um usuário
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome_completo = ?, email = ?, cargo = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getCargo().name());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    // Remove um usuário pelo ID
    public boolean remover(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao remover usuário: " + e.getMessage());
            return false;
        }
    }

    // Converte uma linha do banco em objeto Usuario (polimorfismo)
    private Usuario mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome_completo");
        String cpf = rs.getString("cpf");
        String email = rs.getString("email");
        Cargo cargo = Cargo.valueOf(rs.getString("cargo"));
        String login = rs.getString("login");
        String senha = rs.getString("senha");
        String perfil = rs.getString("perfil");

        // Instancia a subclasse correta conforme o perfil salvo no banco
        switch (perfil) {
            case "ADMINISTRADOR": return new Administrador(id, nome, cpf, email, cargo, login, senha);
            case "GERENTE":       return new Gerente(id, nome, cpf, email, cargo, login, senha);
            default:              return new Colaborador(id, nome, cpf, email, cargo, login, senha);
        }
    }
}