package com.gjw.model;

public class Colaborador extends Usuario {

    public Colaborador(int id, String nomeCompleto, String cpf, String email,
                       Cargo cargo, String login, String senha) {
        super(id, nomeCompleto, cpf, email, cargo, login, senha);
    }

    @Override
    public String getPerfil() {
        return "COLABORADOR";
    }
}