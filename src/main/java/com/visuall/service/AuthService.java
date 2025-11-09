package com.visuall.service;

import com.visuall.model.dto.AuthResponse;
import com.visuall.model.dto.UserDTO;
import com.visuall.model.Usuario;
import com.visuall.dao.UsuarioDAO;
import java.util.UUID;

public class AuthService {

    UsuarioDAO usuarioDAO = new UsuarioDAO();

    public AuthResponse login(String email, String senha) {
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Credenciais inválidas");
        }

        AuthResponse response = new AuthResponse();
        response.setToken(UUID.randomUUID().toString());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(usuario.getId());
        userDTO.setNome(usuario.getNome());
        userDTO.setEmail(usuario.getEmail());

        response.setUser(userDTO);
        return response;
    }

    public AuthResponse register(String nome, String email, String senha) {
        if (usuarioDAO.findByEmail(email) != null) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);
        novoUsuario.setAtivo("S");

        Integer id = usuarioDAO.create(novoUsuario);
        if (id == -1) {
            throw new RuntimeException("Erro ao criar usuário");
        }

        Usuario usuarioSalvo = usuarioDAO.findById(id);

        AuthResponse response = new AuthResponse();
        response.setToken(UUID.randomUUID().toString());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(usuarioSalvo.getId());
        userDTO.setNome(usuarioSalvo.getNome());
        userDTO.setEmail(usuarioSalvo.getEmail());

        response.setUser(userDTO);
        return response;
    }
}