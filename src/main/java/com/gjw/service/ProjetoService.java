package com.gjw.service;

import com.gjw.dao.EquipeDAO;
import com.gjw.dao.ProjetoDAO;
import com.gjw.dao.TarefaDAO;
import com.gjw.dao.UsuarioDAO;
import com.gjw.model.*;

import java.util.List;

public class ProjetoService {

    private ProjetoDAO projetoDAO = new ProjetoDAO();
    private TarefaDAO tarefaDAO = new TarefaDAO();
    private EquipeDAO equipeDAO = new EquipeDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Cadastra um novo projeto — aceita apenas usuários com perfil GERENTE
    public Projeto cadastrar(String nome, String descricao, String dataInicio,
                             String dataTermino, int gerenteId) {
        Usuario gerente = usuarioDAO.buscarPorId(gerenteId);
        if (gerente == null) {
            System.out.println("Usuário não encontrado.");
            return null;
        }
        if (!gerente.getPerfil().equals("GERENTE")) {
            System.out.println("Erro: apenas usuários com perfil GERENTE podem ser responsáveis por projetos.");
            System.out.println("Usuário selecionado tem perfil: " + gerente.getPerfil());
            return null;
        }
        Projeto novo = new Projeto(0, nome, descricao, dataInicio, dataTermino, gerenteId);
        if (projetoDAO.inserir(novo)) {
            System.out.println("Projeto cadastrado: " + novo);
            return novo;
        }
        return null;
    }

    // Lista todos os projetos
    public void listar() {
        List<Projeto> lista = projetoDAO.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE PROJETOS ---");
        for (Projeto p : lista) {
            Usuario gerente = usuarioDAO.buscarPorId(p.getGerenteId());
            String nomeGerente = gerente != null ? gerente.getNomeCompleto() : "N/A";
            System.out.println(p + " | Gerente: " + nomeGerente);
        }
    }

    // Busca projeto pelo ID
    public Projeto buscarPorId(int id) {
        Projeto p = projetoDAO.buscarPorId(id);
        if (p == null) System.out.println("Projeto não encontrado.");
        return p;
    }

    // Atualiza dados completos de um projeto
    public void atualizarProjeto(int projetoId, String novoNome, String novaDescricao,
                                  String novaDataInicio, String novaDataTermino, int novoGerenteId) {
        Projeto p = projetoDAO.buscarPorId(projetoId);
        if (p == null) {
            System.out.println("Projeto não encontrado.");
            return;
        }
        Usuario gerente = usuarioDAO.buscarPorId(novoGerenteId);
        if (gerente == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        if (!gerente.getPerfil().equals("GERENTE")) {
            System.out.println("Erro: apenas usuários com perfil GERENTE podem ser responsáveis.");
            return;
        }
        projetoDAO.atualizar(projetoId, novoNome, novaDescricao, novaDataInicio, novaDataTermino, novoGerenteId);
        System.out.println("Projeto atualizado com sucesso!");
    }

    // Atualiza o status de um projeto
    public void atualizarStatus(int projetoId, StatusProjeto status) {
        if (projetoDAO.buscarPorId(projetoId) == null) {
            System.out.println("Projeto não encontrado.");
            return;
        }
        projetoDAO.atualizarStatus(projetoId, status);
        System.out.println("Status atualizado para: " + status);
    }

    // Adiciona uma tarefa ao projeto
    public Tarefa adicionarTarefa(int projetoId, String nome, String descricao,
                                  String prazo, int responsavelId) {
        if (projetoDAO.buscarPorId(projetoId) == null) {
            System.out.println("Projeto não encontrado.");
            return null;
        }
        Tarefa tarefa = new Tarefa(0, nome, descricao, prazo, responsavelId, projetoId);
        if (tarefaDAO.inserir(tarefa)) {
            System.out.println("Tarefa adicionada: " + tarefa);
            return tarefa;
        }
        return null;
    }

    // Atualiza o status de uma tarefa
    public void atualizarStatusTarefa(int tarefaId, StatusTarefa status) {
        if (tarefaDAO.buscarPorId(tarefaId) == null) {
            System.out.println("Tarefa não encontrada.");
            return;
        }
        tarefaDAO.atualizarStatus(tarefaId, status);
        System.out.println("Status da tarefa atualizado para: " + status);
    }

    // Aloca uma equipe ao projeto
    public void alocarEquipe(int projetoId, int equipeId) {
        if (projetoDAO.buscarPorId(projetoId) == null) {
            System.out.println("Projeto não encontrado.");
            return;
        }
        if (equipeDAO.buscarPorId(equipeId) == null) {
            System.out.println("Equipe não encontrada.");
            return;
        }
        if (projetoDAO.equipeAlocada(projetoId, equipeId)) {
            System.out.println("Equipe já alocada neste projeto.");
            return;
        }
        projetoDAO.alocarEquipe(projetoId, equipeId);
        System.out.println("Equipe alocada ao projeto com sucesso!");
    }

    // Relatório detalhado de um projeto
    public void exibirRelatorio(int projetoId) {
        Projeto p = projetoDAO.buscarPorId(projetoId);
        if (p == null) {
            System.out.println("Projeto não encontrado.");
            return;
        }

        List<Tarefa> tarefas = tarefaDAO.listarPorProjeto(projetoId);
        int total = tarefas.size();
        int concluidas = 0, emAndamento = 0, pendentes = 0, canceladas = 0;

        for (Tarefa t : tarefas) {
            switch (t.getStatus()) {
                case CONCLUIDA:    concluidas++;   break;
                case EM_ANDAMENTO: emAndamento++;  break;
                case PENDENTE:     pendentes++;    break;
                case CANCELADA:    canceladas++;   break;
            }
        }

        int progresso = total > 0 ? (concluidas * 100) / total : 0;
        Usuario gerente = usuarioDAO.buscarPorId(p.getGerenteId());
        String nomeGerente = gerente != null ? gerente.getNomeCompleto() : "N/A";

        System.out.println("\n========================================");
        System.out.println("  RELATÓRIO: " + p.getNome());
        System.out.println("========================================");
        System.out.println("  Status:           " + p.getStatus());
        System.out.println("  Gerente:          " + nomeGerente);
        System.out.println("  Início:           " + p.getDataInicio());
        System.out.println("  Término previsto: " + p.getDataTerminoPrevista());
        System.out.println("----------------------------------------");
        System.out.println("  Total de tarefas: " + total);
        System.out.println("  Concluídas:       " + concluidas);
        System.out.println("  Em andamento:     " + emAndamento);
        System.out.println("  Pendentes:        " + pendentes);
        System.out.println("  Canceladas:       " + canceladas);
        System.out.println("  Progresso:        " + progresso + "%");
        System.out.println("----------------------------------------");
        System.out.println("  Equipes alocadas:");
        List<Integer> equipeIds = projetoDAO.listarEquipesDoprojeto(projetoId);
        if (equipeIds.isEmpty()) {
            System.out.println("    Nenhuma equipe alocada.");
        } else {
            for (int eqId : equipeIds) {
                Equipe eq = equipeDAO.buscarPorId(eqId);
                if (eq != null) {
                    List<Usuario> membros = equipeDAO.listarMembros(eqId);
                    System.out.println("    - " + eq.getNome() + " (" + membros.size() + " membros)");
                }
            }
        }
        System.out.println("----------------------------------------");
        System.out.println("  Tarefas:");
        if (tarefas.isEmpty()) {
            System.out.println("    Nenhuma tarefa cadastrada.");
        } else {
            for (Tarefa t : tarefas) {
                Usuario resp = usuarioDAO.buscarPorId(t.getResponsavelId());
                String nomeResp = resp != null ? resp.getNomeCompleto() : "Sem responsável";
                System.out.println("    " + t + " | Responsável: " + nomeResp);
            }
        }
        System.out.println("========================================\n");
    }

    // Relatório consolidado de todo o portfólio de projetos
    public void exibirRelatorioPortfolio() {
        List<Projeto> projetos = projetoDAO.listarTodos();
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
            return;
        }

        int totalProjetos = projetos.size();
        int planejados = 0, emAndamento = 0, concluidos = 0, cancelados = 0;
        int totalTarefas = 0, totalConcluidas = 0;

        System.out.println("\n========================================");
        System.out.println("  RELATÓRIO CONSOLIDADO DO PORTFÓLIO");
        System.out.println("========================================");

        for (Projeto p : projetos) {
            List<Tarefa> tarefas = tarefaDAO.listarPorProjeto(p.getId());
            int tConcluidas = 0;
            for (Tarefa t : tarefas) {
                if (t.getStatus() == StatusTarefa.CONCLUIDA) tConcluidas++;
            }
            int progresso = tarefas.size() > 0 ? (tConcluidas * 100) / tarefas.size() : 0;

            switch (p.getStatus()) {
                case PLANEJADO:    planejados++;   break;
                case EM_ANDAMENTO: emAndamento++;  break;
                case CONCLUIDO:    concluidos++;   break;
                case CANCELADO:    cancelados++;   break;
            }

            totalTarefas += tarefas.size();
            totalConcluidas += tConcluidas;

            Usuario gerente = usuarioDAO.buscarPorId(p.getGerenteId());
            String nomeGerente = gerente != null ? gerente.getNomeCompleto() : "N/A";

            System.out.printf("  [%d] %-35s | %-12s | Progresso: %3d%% | Gerente: %s%n",
                    p.getId(), p.getNome(), p.getStatus(), progresso, nomeGerente);
        }

        System.out.println("----------------------------------------");
        System.out.println("  TOTAIS:");
        System.out.println("  Projetos:      " + totalProjetos);
        System.out.println("  Planejados:    " + planejados);
        System.out.println("  Em andamento:  " + emAndamento);
        System.out.println("  Concluídos:    " + concluidos);
        System.out.println("  Cancelados:    " + cancelados);
        System.out.println("  Total tarefas: " + totalTarefas);
        System.out.println("  Concluídas:    " + totalConcluidas);
        int progressoGeral = totalTarefas > 0 ? (totalConcluidas * 100) / totalTarefas : 0;
        System.out.println("  Progresso geral do portfólio: " + progressoGeral + "%");
        System.out.println("========================================\n");
    }

    // Relatório por equipe — mostra projetos e membros de cada equipe
    public void exibirRelatorioEquipe(int equipeId) {
        Equipe equipe = equipeDAO.buscarPorId(equipeId);
        if (equipe == null) {
            System.out.println("Equipe não encontrada.");
            return;
        }

        List<Usuario> membros = equipeDAO.listarMembros(equipeId);
        List<Projeto> todosProjetos = projetoDAO.listarTodos();

        System.out.println("\n========================================");
        System.out.println("  RELATÓRIO DA EQUIPE: " + equipe.getNome());
        System.out.println("========================================");
        System.out.println("  Descrição: " + equipe.getDescricao());
        System.out.println("  Membros (" + membros.size() + "):");
        for (Usuario m : membros) {
            System.out.println("    - " + m.getNomeCompleto() + " | " + m.getCargo() + " | " + m.getPerfil());
        }

        System.out.println("----------------------------------------");
        System.out.println("  Projetos em que a equipe atua:");
        boolean temProjeto = false;
        for (Projeto p : todosProjetos) {
            if (projetoDAO.equipeAlocada(p.getId(), equipeId)) {
                temProjeto = true;
                List<Tarefa> tarefas = tarefaDAO.listarPorProjeto(p.getId());
                int tConcluidas = 0;
                for (Tarefa t : tarefas) {
                    if (t.getStatus() == StatusTarefa.CONCLUIDA) tConcluidas++;
                }
                int progresso = tarefas.size() > 0 ? (tConcluidas * 100) / tarefas.size() : 0;
                System.out.println("    - " + p.getNome() + " | " + p.getStatus() + " | Progresso: " + progresso + "%");
            }
        }
        if (!temProjeto) System.out.println("    Nenhum projeto alocado.");
        System.out.println("========================================\n");
    }

    // Remove um projeto
    public boolean remover(int id) {
        if (projetoDAO.buscarPorId(id) == null) {
            System.out.println("Projeto não encontrado.");
            return false;
        }
        projetoDAO.remover(id);
        System.out.println("Projeto removido com sucesso!");
        return true;
    }
}