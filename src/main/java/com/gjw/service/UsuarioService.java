package com.gjw.service;

import com.gjw.dao.UsuarioDAO;
import com.gjw.model.*;

import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Cadastra um novo usuário validando CPF e login duplicados
    public Usuario cadastrar(String nomeCompleto, String cpf, String email,
                             Cargo cargo, String login, String senha, String perfil) {

        if (usuarioDAO.cpfExiste(cpf)) {
            System.out.println("Erro: CPF já cadastrado.");
            return null;
        }
        if (usuarioDAO.loginExiste(login)) {
            System.out.println("Erro: Login já em uso.");
            return null;
        }

        // Instancia a subclasse correta conforme o perfil (polimorfismo)
        Usuario novo;
        switch (perfil) {
            case "ADMINISTRADOR":
                novo = new Administrador(0, nomeCompleto, cpf, email, cargo, login, senha);
                break;
            case "GERENTE":
                novo = new Gerente(0, nomeCompleto, cpf, email, cargo, login, senha);
                break;
            default:
                novo = new Colaborador(0, nomeCompleto, cpf, email, cargo, login, senha);
        }

        if (usuarioDAO.inserir(novo)) {
            System.out.println("Usuário cadastrado: " + novo);
            return novo;
        }
        return null;
    }

    // Lista todos os usuários
    public void listar() {
        List<Usuario> lista = usuarioDAO.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE USUÁRIOS ---");
        for (Usuario u : lista) {
            System.out.println(u);
        }
    }

    // Lista usuários filtrados por perfil
    public void listarPorPerfil(String perfil) {
        List<Usuario> lista = usuarioDAO.listarTodos();
        boolean encontrou = false;
        for (Usuario u : lista) {
            if (u.getPerfil().equals(perfil)) {
                System.out.println(u);
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println("Nenhum usuário com perfil " + perfil + " encontrado.");
    }

    // Busca usuário pelo ID
    public Usuario buscarPorId(int id) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u == null) System.out.println("Usuário não encontrado.");
        return u;
    }

    // Atualiza nome, email e cargo
    public boolean atualizar(int id, String novoNome, String novoEmail, Cargo novoCargo) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u == null) {
            System.out.println("Usuário não encontrado.");
            return false;
        }
        u.setNomeCompleto(novoNome);
        u.setEmail(novoEmail);
        u.setCargo(novoCargo);
        if (usuarioDAO.atualizar(u)) {
            System.out.println("Usuário atualizado: " + u);
            return true;
        }
        return false;
    }

    // Remove um usuário pelo ID
    public boolean remover(int id) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u == null) {
            System.out.println("Usuário não encontrado.");
            return false;
        }
        usuarioDAO.remover(id);
        System.out.println("Usuário '" + u.getNomeCompleto() + "' removido.");
        return true;
    }
}