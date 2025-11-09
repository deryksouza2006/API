package com.visuall.dao;

import com.visuall.model.LembretePessoal;
import com.visuall.model.dto.LembreteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LembreteDAO {

    @Inject
    EntityManager em;

    public LembreteResponseDTO readByIdDTO(Integer id) {
        LembretePessoal entity = em.find(LembretePessoal.class, id);
        if (entity == null) return null;
        return toDTO(entity);
    }

    public List<LembreteResponseDTO> readByPacienteId(Integer pacienteId) {
        List<LembretePessoal> lembretes = em.createQuery(
                        "SELECT l FROM LembretePessoal l WHERE l.idPaciente = :pacienteId ORDER BY l.dataCriacao DESC",
                        LembretePessoal.class)
                .setParameter("pacienteId", pacienteId)
                .getResultList();
        return lembretes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public LembretePessoal readById(Integer id) {
        return em.find(LembretePessoal.class, id);
    }

    @Transactional
    public Integer create(LembretePessoal lembrete) {
        em.persist(lembrete);
        em.flush();
        return lembrete.getId();
    }

    @Transactional
    public boolean update(LembretePessoal lembrete) {
        LembretePessoal existing = em.find(LembretePessoal.class, lembrete.getId());
        if (existing == null) return false;
        existing.setTitulo(lembrete.getTitulo());
        existing.setEnviado(lembrete.getAtivo() ? "N" : "S");
        em.merge(existing);
        return true;
    }

    @Transactional
    public boolean delete(Integer id, Integer pacienteId) {
        LembretePessoal lembrete = em.find(LembretePessoal.class, id);
        if (lembrete == null || !lembrete.getIdPaciente().equals(pacienteId)) return false;
        em.remove(lembrete);
        return true;
    }

    public List<LembreteResponseDTO> buscarAtivosPorPaciente(Integer pacienteId) {
        List<LembretePessoal> lembretes = em.createQuery(
                        "SELECT l FROM LembretePessoal l WHERE l.idPaciente = :pacienteId AND l.enviado = 'N' ORDER BY l.dataCriacao DESC",
                        LembretePessoal.class)
                .setParameter("pacienteId", pacienteId)
                .getResultList();
        return lembretes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private LembreteResponseDTO toDTO(LembretePessoal entity) {
        LembreteResponseDTO dto = new LembreteResponseDTO();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setNomeMedico("Dr. João Silva");
        dto.setEspecialidade("Clínico Geral");
        dto.setDataConsulta(entity.getDataConsulta() != null ? entity.getDataConsulta() : LocalDate.now());
        dto.setHoraConsulta(LocalTime.of(12, 0));
        dto.setLocalConsulta("Consultório");
        dto.setObservacoes(entity.getObservacoes());
        dto.setConcluido(!"S".equals(entity.getEnviado()));
        dto.setUsuarioId(entity.getIdPaciente());
        dto.setDataCriacao(entity.getDataCriacao() != null ? entity.getDataCriacao().toString() : LocalDate.now().toString());
        return dto;
    }
}