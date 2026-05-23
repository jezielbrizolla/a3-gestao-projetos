package com.gjw.model;

public abstract class Usuario {

    private int id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private Cargo cargo;
    private String login;
    private String senha;

    public Usuario(int id, String nomeCompleto, String cpf, String email,
                   Cargo cargo, String login, String senha) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
    }

    public abstract String getPerfil();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String n) { this.nomeCompleto = n; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Login: %s | Cargo: %s | Perfil: %s",
                id, nomeCompleto, login, cargo, getPerfil());
    }
}