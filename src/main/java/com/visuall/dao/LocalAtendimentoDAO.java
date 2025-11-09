package com.visuall.dao;

import com.visuall.config.DatabaseConfig;
import com.visuall.model.LocalAtendimento;
import com.visuall.model.dto.LocalAtendimentoDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LocalAtendimentoDAO {

    public List<LocalAtendimentoDTO> findAll() {
        List<LocalAtendimentoDTO> locais = new ArrayList<>();

        locais.add(criarLocal(1, "Hospital Central", "Av. Principal, 1000"));
        locais.add(criarLocal(2, "Clínica Saúde Total", "Rua Saúde, 500"));
        locais.add(criarLocal(3, "Laboratório Análises", "Praça Exames, 200"));
        locais.add(criarLocal(4, "Posto de Saúde", "Rua Municipal, 300"));
        locais.add(criarLocal(5, "Consultório Particular", "Av. Médicos, 150"));

        System.out.println("Retornando " + locais.size() + " locais fixos");
        return locais;
    }

    public LocalAtendimento findById(Integer id) {
        List<LocalAtendimentoDTO> todosLocais = findAll();

        if (id >= 1 && id <= todosLocais.size()) {
            LocalAtendimentoDTO vo = todosLocais.get(id - 1);
            LocalAtendimento local = new LocalAtendimento();
            local.setId(vo.getId());
            local.setNome(vo.getNome());
            local.setEndereco(vo.getEndereco());
            return local;
        }
        return null;
    }

    private LocalAtendimentoDTO criarLocal(Integer id, String nome, String endereco) {
        LocalAtendimentoDTO local = new LocalAtendimentoDTO();
        local.setId(id);
        local.setNome(nome);
        local.setEndereco(endereco);
        return local;
    }
}