package com.visuall.dao;

import com.visuall.model.Usuario;
import com.visuall.config.DatabaseConfig;
import java.sql.*;

public class UsuarioDAO {

    public Usuario findByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND ativo = 'S'";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setAtivo(rs.getString("ativo"));

                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                if (dataCriacao != null) {
                    usuario.setDataCriacao(dataCriacao.toLocalDateTime());
                }

                return usuario;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Usuario findById(Integer id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setAtivo(rs.getString("ativo"));

                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                if (dataCriacao != null) {
                    usuario.setDataCriacao(dataCriacao.toLocalDateTime());
                }

                return usuario;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Integer create(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, ativo, data_criacao) VALUES (?, ?, ?, 'S', SYSDATE)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}