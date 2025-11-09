// CONTINUA EXATAMENTE IGUAL - usando dados mock
package com.visuall.dao;

import com.visuall.model.Especialista;
import com.visuall.model.dto.EspecialistaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EspecialistaDAO {

    public List<EspecialistaDTO> findAll() {
        List<EspecialistaDTO> especialistas = new ArrayList<>();

       
        especialistas.add(criarEspecialista(1, "Dr. João Silva", "12345/SP", "Cardiologia"));
        especialistas.add(criarEspecialista(2, "Dra. Maria Santos", "67890/SP", "Dermatologia"));
        especialistas.add(criarEspecialista(3, "Dr. Pedro Costa", "54321/SP", "Ortopedia"));
        especialistas.add(criarEspecialista(4, "Dra. Ana Oliveira", "98765/SP", "Pediatria"));

        System.out.println("✅ Retornando " + especialistas.size() + " especialistas fixos");
        return especialistas;
    }

    public Especialista findById(Integer id) {
       
        if (id == 1) {
            return criarEspecialistaModel(1, "Dr. João Silva", "12345/SP", "Cardiologia");
        } else if (id == 2) {
            return criarEspecialistaModel(2, "Dra. Maria Santos", "67890/SP", "Dermatologia");
        } else if (id == 3) {
            return criarEspecialistaModel(3, "Dr. Pedro Costa", "54321/SP", "Ortopedia");
        } else if (id == 4) {
            return criarEspecialistaModel(4, "Dra. Ana Oliveira", "98765/SP", "Pediatria");
        }
        return null;
    }

    private EspecialistaDTO criarEspecialista(Integer id, String nome, String crm, String especialidade) {
        EspecialistaDTO especialista = new EspecialistaDTO();
        especialista.setId(id);
        especialista.setNome(nome);
        especialista.setCrm(crm);
        especialista.setEspecialidade(especialidade);
        return especialista;
    }

    private Especialista criarEspecialistaModel(Integer id, String nome, String crm, String especialidade) {
        Especialista especialista = new Especialista();
        especialista.setId(id);
        especialista.setNome(nome);
        especialista.setCrm(crm);
        especialista.setEspecialidade(especialidade);
        return especialista;
    }
}