package com.visuall.service;

import com.visuall.dao.LembreteDAO;
import com.visuall.exception.BusinessException;
import com.visuall.model.LembretePessoal;
import com.visuall.model.dto.LembreteRequestDTO;
import com.visuall.model.dto.LembreteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class LembreteService {

    @Inject
    LembreteDAO lembreteDAO;

    // ✅ Criar lembrete
    @Transactional
    public LembreteResponseDTO criarLembrete(LembreteRequestDTO request) {
        if (request.getNomeMedico() == null || request.getNomeMedico().trim().isEmpty()) {
            throw new BusinessException("Nome do médico é obrigatório");
        }
        if (request.getDataConsulta() == null) {
            throw new BusinessException("Data da consulta é obrigatória");
        }

        LembretePessoal lembrete = new LembretePessoal();
        lembrete.setTitulo(request.getTitulo());

        lembrete.setDataConsulta(request.getDataConsulta());
        lembrete.setHoraCompromisso(LocalTime.parse(request.getHoraConsulta() + ":00"));
        lembrete.setObservacoes(request.getObservacoes());
        lembrete.setIdPaciente(request.getUsuarioId());
        lembrete.setAtivo(true);
        lembrete.setEnviado("N");

        Integer id = lembreteDAO.create(lembrete);
        if (id == null || id == -1) {
            throw new BusinessException("Erro ao criar lembrete");
        }

        return lembreteDAO.readByIdDTO(id);
    }

    // 🔧 Editar lembrete existente
    @Transactional
    public LembreteResponseDTO editarLembrete(Integer id, LembreteRequestDTO request) {
        LembretePessoal existente = lembreteDAO.readById(id);
        if (existente == null) {
            throw new BusinessException("Lembrete não encontrado para edição");
        }

        existente.setTitulo(request.getTitulo());
        existente.setDataConsulta(request.getDataConsulta());
        existente.setHoraCompromisso(LocalTime.parse(request.getHoraConsulta() + ":00"));
        existente.setObservacoes(request.getObservacoes());
        existente.setAtivo(true);

        lembreteDAO.update(existente);

        return lembreteDAO.readByIdDTO(id);
    }

    // ☑️ Marcar como concluído
    @Transactional
    public boolean marcarComoConcluido(Integer id) {
        LembretePessoal lembrete = lembreteDAO.readById(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        lembrete.setEnviado("S");
        lembrete.setAtivo(false);
        return lembreteDAO.update(lembrete);
    }

    // ❌ Excluir lembrete
    @Transactional
    public boolean excluirLembrete(Integer id, Integer usuarioId) {
        return lembreteDAO.delete(id, usuarioId);
    }

    // 🔍 Listar lembretes por usuário
    public List<LembreteResponseDTO> listarPorUsuario(Integer usuarioId) {
        return lembreteDAO.readByPacienteId(usuarioId);
    }

    // 🔍 Listar lembretes ativos
    public List<LembreteResponseDTO> listarAtivos(Integer usuarioId) {
        return lembreteDAO.buscarAtivosPorPaciente(usuarioId);
    }
}