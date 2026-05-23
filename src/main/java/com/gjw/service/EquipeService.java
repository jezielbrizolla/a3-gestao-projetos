package com.gjw.service;

import com.gjw.dao.EquipeDAO;
import com.gjw.dao.UsuarioDAO;
import com.gjw.model.Equipe;
import com.gjw.model.Usuario;

import java.util.List;

public class EquipeService {

    private EquipeDAO equipeDAO = new EquipeDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Cadastra uma nova equipe
    public Equipe cadastrar(String nome, String descricao) {
        Equipe nova = new Equipe(0, nome, descricao);
        if (equipeDAO.inserir(nova)) {
            System.out.println("Equipe cadastrada: " + nova);
            return nova;
        }
        return null;
    }

    // Lista todas as equipes com seus membros
    public void listar() {
        List<Equipe> lista = equipeDAO.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma equipe cadastrada.");
            return;
        }
        System.out.println("\n--- LISTA DE EQUIPES ---");
        for (Equipe e : lista) {
            System.out.println(e);
            List<Usuario> membros = equipeDAO.listarMembros(e.getId());
            if (membros.isEmpty()) {
                System.out.println("  Sem membros cadastrados.");
            } else {
                System.out.println("  Membros:");
                for (Usuario u : membros) {
                    System.out.println("    - " + u.getNomeCompleto()
                            + " | " + u.getCargo()
                            + " | " + u.getPerfil());
                }
            }
        }
    }

    // Busca equipe pelo ID
    public Equipe buscarPorId(int id) {
        Equipe e = equipeDAO.buscarPorId(id);
        if (e == null) System.out.println("Equipe não encontrada.");
        return e;
    }

    // Adiciona um membro à equipe
    public void adicionarMembro(int equipeId, int usuarioId) {
        if (equipeDAO.buscarPorId(equipeId) == null) {
            System.out.println("Equipe não encontrada.");
            return;
        }
        if (usuarioDAO.buscarPorId(usuarioId) == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        if (equipeDAO.membroExiste(equipeId, usuarioId)) {
            System.out.println("Usuário já pertence a esta equipe.");
            return;
        }
        equipeDAO.adicionarMembro(equipeId, usuarioId);
        System.out.println("Membro adicionado com sucesso!");
    }

    // Remove um membro da equipe
    public void removerMembro(int equipeId, int usuarioId) {
        if (!equipeDAO.membroExiste(equipeId, usuarioId)) {
            System.out.println("Usuário não é membro desta equipe.");
            return;
        }
        equipeDAO.removerMembro(equipeId, usuarioId);
        System.out.println("Membro removido com sucesso!");
    }

    // Remove uma equipe
    public boolean remover(int id) {
        if (equipeDAO.buscarPorId(id) == null) {
            System.out.println("Equipe não encontrada.");
            return false;
        }
        equipeDAO.remover(id);
        System.out.println("Equipe removida com sucesso!");
        return true;
    }
}