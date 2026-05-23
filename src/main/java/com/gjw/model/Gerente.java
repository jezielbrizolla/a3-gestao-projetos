package com.gjw.model;

public class Gerente extends Usuario {

    public Gerente(int id, String nomeCompleto, String cpf, String email,
                   Cargo cargo, String login, String senha) {
        super(id, nomeCompleto, cpf, email, cargo, login, senha);
    }

    @Override
    public String getPerfil() {
        return "GERENTE";
    }
}