package com.visuall.model.dto;

public class EspecialistaDTO {
    private Integer id;
    private String nome; 
    private String especialidade;
    private String crm;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; } 

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
}