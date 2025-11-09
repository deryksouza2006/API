package com.visuall.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "DATA_ENVIO")
    private LocalDateTime dataCriacao;

    @Column(name = "ENVIADO")
    private String enviado;

    @Column(name = "ID_PACIENTE")
    private Integer idPaciente;

    @Column(name = "ID_CONSULTA")
    private Integer idConsulta;

    // Campos de aplicação (não persistidos)
    @Transient
    private LocalDate dataConsulta;

    @Transient
    private LocalTime horaConsulta;

    @Transient
    private String observacoes;

    @Transient
    private boolean ativo;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getEnviado() { return enviado; }
    public void setEnviado(String enviado) { this.enviado = enviado; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public Integer getIdConsulta() { return idConsulta; }
    public void setIdConsulta(Integer idConsulta) { this.idConsulta = idConsulta; }

    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public LocalTime getHoraConsulta() { return horaConsulta; }
    public void setHoraCompromisso(LocalTime horaConsulta) { this.horaConsulta = horaConsulta; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public boolean getAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}