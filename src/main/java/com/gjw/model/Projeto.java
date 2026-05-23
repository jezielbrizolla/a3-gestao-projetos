package com.gjw.model;

public class Projeto {

    private int id;
    private String nome;
    private String descricao;
    private String dataInicio;
    private String dataTerminoPrevista;
    private StatusProjeto status;
    private int gerenteId;

    public Projeto(int id, String nome, String descricao, String dataInicio,
                   String dataTerminoPrevista, int gerenteId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = StatusProjeto.PLANEJADO;
        this.gerenteId = gerenteId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
    public String getDataTerminoPrevista() { return dataTerminoPrevista; }
    public void setDataTerminoPrevista(String data) { this.dataTerminoPrevista = data; }
    public StatusProjeto getStatus() { return status; }
    public void setStatus(StatusProjeto status) { this.status = status; }
    public int getGerenteId() { return gerenteId; }
    public void setGerenteId(int gerenteId) { this.gerenteId = gerenteId; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Status: %s | Início: %s | Término: %s",
                id, nome, status, dataInicio, dataTerminoPrevista);
    }
}