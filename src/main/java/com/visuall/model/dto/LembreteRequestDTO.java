package com.visuall.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class LembreteRequestDTO {
    private Integer id;
    private Integer usuarioId;
    private String titulo;
    private String nomeMedico;
    private String especialidade;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private String localConsulta;
    private String observacoes;
    private Boolean concluido;
    private String dataCriacao;

    // Getters e Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; } // Adicionado
    public void setTitulo(String titulo) { this.titulo = titulo; } // Adicionado

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public LocalTime getHoraConsulta() { return horaConsulta; }
    public void setHoraConsulta(LocalTime horaConsulta) { this.horaConsulta = horaConsulta; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getLocalConsulta() { return localConsulta; }
    public void setLocalConsulta(String localConsulta) { this.localConsulta = localConsulta; }

    public Boolean getConcluido() { return concluido; }
    public void setConcluido(Boolean concluido) { this.concluido = concluido; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

}