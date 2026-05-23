package com.gjw.model;

public class Tarefa {

    private int id;
    private String nome;
    private String descricao;
    private String prazo;
    private StatusTarefa status;
    private int responsavelId;
    private int projetoId;

    public Tarefa(int id, String nome, String descricao, String prazo,
                  int responsavelId, int projetoId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.prazo = prazo;
        this.status = StatusTarefa.PENDENTE;
        this.responsavelId = responsavelId;
        this.projetoId = projetoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getPrazo() { return prazo; }
    public void setPrazo(String prazo) { this.prazo = prazo; }
    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }
    public int getResponsavelId() { return responsavelId; }
    public void setResponsavelId(int responsavelId) { this.responsavelId = responsavelId; }
    public int getProjetoId() { return projetoId; }
    public void setProjetoId(int projetoId) { this.projetoId = projetoId; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Prazo: %s | Status: %s",
                id, nome, prazo, status);
    }
}