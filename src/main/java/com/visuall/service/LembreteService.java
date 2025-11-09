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
        lembrete.setNomeMedico(request.getNomeMedico());
        lembrete.setEspecialidade(request.getEspecialidade());
        lembrete.setLocal(request.getLocalConsulta());
        lembrete.setDataConsulta(request.getDataConsulta());

        // ✅ CORREÇÃO: Converter horaConsulta corretamente
        if (request.getHoraConsulta() != null) {
            String horaStr = request.getHoraConsulta().toString();
            if (!horaStr.contains(":")) {
                horaStr += ":00"; // Adiciona segundos se não tiver
            }
            lembrete.setHoraConsulta(LocalTime.parse(horaStr));
        }

        lembrete.setObservacoes(request.getObservacoes());
        lembrete.setAtivo(true);
        lembrete.setEnviado("N");
        lembrete.setIdPaciente(request.getUsuarioId()); // ✅ CORREÇÃO: Definir ID do usuário

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
        existente.setNomeMedico(request.getNomeMedico());
        existente.setEspecialidade(request.getEspecialidade());
        existente.setLocal(request.getLocalConsulta());
        existente.setDataConsulta(request.getDataConsulta());

        // ✅ CORREÇÃO: Converter horaConsulta corretamente
        if (request.getHoraConsulta() != null) {
            String horaStr = request.getHoraConsulta().toString();
            if (!horaStr.contains(":")) {
                horaStr += ":00";
            }
            existente.setHoraConsulta(LocalTime.parse(horaStr));
        }

        existente.setObservacoes(request.getObservacoes());
        existente.setAtivo(true);

        boolean atualizado = lembreteDAO.update(existente);
        if (!atualizado) {
            throw new BusinessException("Erro ao atualizar lembrete");
        }

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

    // ❌ Excluir lembrete - ✅ CORREÇÃO: Remover usuarioId do parâmetro
    @Transactional
    public boolean excluirLembrete(Integer id) {
        return lembreteDAO.delete(id);
    }

    // 🔍 Listar lembretes por usuário - ✅ CORREÇÃO: Usar método DTO
    public List<LembreteResponseDTO> listarPorUsuario(Integer usuarioId) {
        return lembreteDAO.listByUsuarioDTO(usuarioId);
    }

    // 🔍 Listar lembretes ativos - ✅ CORREÇÃO: Usar método DTO
    public List<LembreteResponseDTO> listarAtivos(Integer usuarioId) {
        return lembreteDAO.buscarAtivosPorPacienteDTO(usuarioId);
    }

    // ✅ MÉTODO ADICIONAL: Buscar lembrete por ID
    public LembreteResponseDTO buscarPorId(Integer id) {
        LembreteResponseDTO lembrete = lembreteDAO.readByIdDTO(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }
        return lembrete;
    }

    // ✅ MÉTODO ADICIONAL: Listar todos os lembretes
    public List<LembreteResponseDTO> listarTodos() {
        return lembreteDAO.findAllDTO();
    }
}