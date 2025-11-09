package com.visuall.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "LEMBRETES")
public class LembretePessoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se o Oracle usa sequence, me avise e ajusto
    @Column(name = "ID_LEMBRETE")
    private Integer id;

    @Column(name = "MENSAGEM")
    private String titulo;

    @Column(name = "ENVIADO")
    private String enviado;

    @Column(name = "ID_USUARIO")
    private Integer idPaciente;

    @Transient
    private LocalDate dataConsulta;

    @Transient
    private LocalTime horaConsulta;

    @Transient
    private String observacoes;

    @Transient
    private boolean ativo;

    @Transient
    private String nomeMedico;

    @Transient
    private String especialidade;

    @Transient
    private String local;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEnviado() { return enviado; }
    public void setEnviado(String enviado) { this.enviado = enviado; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }


    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public LocalTime getHoraConsulta() { return horaConsulta; }
    public void setHoraConsulta(LocalTime horaConsulta) { this.horaConsulta = horaConsulta; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public boolean getAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String Especialidade) { this.especialidade = Especialidade; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }


    // Método para formatar a hora da consulta, para seguir o padrão de 'HH:mm'
    public String getHoraConsultaFormatada() {
        return horaConsulta != null ? horaConsulta.toString() : "Hora não definida";
    }
}
