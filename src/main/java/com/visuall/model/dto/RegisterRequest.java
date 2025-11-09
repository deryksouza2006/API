package com.visuall.model.dto;

public class RegisterRequest {
    public String nome;
    public String email;
    public String senha;

    public RegisterRequest() {}

    public RegisterRequest(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}