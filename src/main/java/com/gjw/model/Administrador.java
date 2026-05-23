package com.gjw.model;

public class Administrador extends Usuario {

    public Administrador(int id, String nomeCompleto, String cpf, String email,
                         Cargo cargo, String login, String senha) {
        super(id, nomeCompleto, cpf, email, cargo, login, senha);
    }

    @Override
    public String getPerfil() {
        return "ADMINISTRADOR";
    }
}