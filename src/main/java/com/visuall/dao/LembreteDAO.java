package com.visuall.dao;

import com.visuall.config.DatabaseConfig;
import com.visuall.model.LembretePessoal;
import com.visuall.model.dto.LembreteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class LembreteDAO {

    @PersistenceContext
    EntityManager em;

    private static final Logger logger = Logger.getLogger(LembreteDAO.class.getName());

    // ✅ MÉTODO CORRIGIDO: Buscar lembrete por ID (para LembretePessoal)
    public LembretePessoal readById(Integer lembreteId) {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES WHERE ID_LEMBRETE = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lembreteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LembretePessoal lembrete = new LembretePessoal();
                lembrete.setId(rs.getInt("ID_LEMBRETE"));
                lembrete.setTitulo(rs.getString("MENSAGEM"));
                lembrete.setNomeMedico(rs.getString("NOME_MEDICO"));
                lembrete.setEspecialidade(rs.getString("ESPECIALIDADE"));
                lembrete.setLocal(rs.getString("LOCAL_CONSULTA"));

                String horaStr = rs.getString("HORA_CONSULTA");
                lembrete.setHoraConsulta(horaStr != null ? LocalTime.parse(horaStr) : null);

                lembrete.setObservacoes(rs.getString("OBSERVACOES"));
                lembrete.setEnviado(rs.getString("ENVIADO"));
                lembrete.setIdPaciente(rs.getInt("ID_USUARIO"));

                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                lembrete.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                return lembrete;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembrete por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembrete: " + e.getMessage(), e);
        }
    }

    // ✅ MÉTODO: Buscar lembrete por ID e retornar DTO
    public LembreteResponseDTO readByIdDTO(Integer lembreteId) {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM as titulo, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES WHERE ID_LEMBRETE = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lembreteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("ID_LEMBRETE"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico(rs.getString("NOME_MEDICO"));
                dto.setEspecialidade(rs.getString("ESPECIALIDADE"));
                dto.setLocalConsulta(rs.getString("LOCAL_CONSULTA"));

                String horaStr = rs.getString("HORA_CONSULTA");
                if (horaStr != null) {
                    dto.setHoraConsulta(LocalTime.parse(horaStr));
                }

                dto.setObservacoes(rs.getString("OBSERVACOES"));
                dto.setConcluido(!"S".equals(rs.getString("ENVIADO")));
                dto.setUsuarioId(rs.getInt("ID_USUARIO"));

                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                dto.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                return dto;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembrete DTO por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembrete: " + e.getMessage(), e);
        }
    }

    // ✅ MÉTODO: Atualizar lembrete
    public Boolean update(LembretePessoal lembrete) {
        String sql = "UPDATE LEMBRETES SET MENSAGEM = ?, ESPECIALIDADE = ?, LOCAL_CONSULTA = ?, OBSERVACOES = ?, " +
                "NOME_MEDICO = ?, HORA_CONSULTA = ?, DATA_CONSULTA = ?, ENVIADO = ? WHERE ID_LEMBRETE = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lembrete.getTitulo());
            stmt.setString(2, lembrete.getEspecialidade());
            stmt.setString(3, lembrete.getLocal());
            stmt.setString(4, lembrete.getObservacoes());
            stmt.setString(5, lembrete.getNomeMedico());
            stmt.setString(6, lembrete.getHoraConsulta() != null ? lembrete.getHoraConsulta().toString() : null);
            stmt.setDate(7, java.sql.Date.valueOf(lembrete.getDataConsulta()));
            stmt.setString(8, lembrete.getEnviado());
            stmt.setInt(9, lembrete.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("Erro ao atualizar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar lembrete: " + e.getMessage(), e);
        }
    }

    // ✅ MÉTODO: Deletar lembrete
    public Boolean delete(Integer lembreteId) {
        String sql = "DELETE FROM LEMBRETES WHERE ID_LEMBRETE = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lembreteId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("Erro ao deletar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar lembrete: " + e.getMessage(), e);
        }
    }

    // ✅ MÉTODO: Buscar lembretes por ID do paciente (LembretePessoal)
    public List<LembretePessoal> readByPacienteId(Integer pacienteId) {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES WHERE ID_USUARIO = ? ORDER BY DATA_CONSULTA DESC";

        List<LembretePessoal> lembretes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembretePessoal lembrete = new LembretePessoal();
                lembrete.setId(rs.getInt("ID_LEMBRETE"));
                lembrete.setTitulo(rs.getString("MENSAGEM"));
                lembrete.setNomeMedico(rs.getString("NOME_MEDICO"));
                lembrete.setEspecialidade(rs.getString("ESPECIALIDADE"));
                lembrete.setLocal(rs.getString("LOCAL_CONSULTA"));

                String horaStr = rs.getString("HORA_CONSULTA");
                lembrete.setHoraConsulta(horaStr != null ? LocalTime.parse(horaStr) : null);

                lembrete.setObservacoes(rs.getString("OBSERVACOES"));
                lembrete.setEnviado(rs.getString("ENVIADO"));
                lembrete.setIdPaciente(rs.getInt("ID_USUARIO"));

                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                lembrete.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                lembretes.add(lembrete);
            }

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembretes por paciente: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lembretes: " + e.getMessage(), e);
        }
        return lembretes;
    }

    // ✅ MÉTODO: Buscar lembretes ativos por paciente (LembretePessoal)
    public List<LembretePessoal> buscarAtivosPorPaciente(Integer pacienteId) {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES WHERE ID_USUARIO = ? AND ENVIADO = 'N' ORDER BY DATA_CONSULTA DESC";

        List<LembretePessoal> lembretes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembretePessoal lembrete = new LembretePessoal();
                lembrete.setId(rs.getInt("ID_LEMBRETE"));
                lembrete.setTitulo(rs.getString("MENSAGEM"));
                lembrete.setNomeMedico(rs.getString("NOME_MEDICO"));
                lembrete.setEspecialidade(rs.getString("ESPECIALIDADE"));
                lembrete.setLocal(rs.getString("LOCAL_CONSULTA"));

                String horaStr = rs.getString("HORA_CONSULTA");
                lembrete.setHoraConsulta(horaStr != null ? LocalTime.parse(horaStr) : null);

                lembrete.setObservacoes(rs.getString("OBSERVACOES"));
                lembrete.setEnviado("N");
                lembrete.setIdPaciente(rs.getInt("ID_USUARIO"));

                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                lembrete.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                lembretes.add(lembrete);
            }

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembretes ativos por paciente: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lembretes ativos: " + e.getMessage(), e);
        }
        return lembretes;
    }

    // ✅ MÉTODO: Criar lembrete
    public Integer create(LembretePessoal lembrete) {
        String sql = "INSERT INTO LEMBRETES (MENSAGEM, ID_USUARIO, ESPECIALIDADE, LOCAL_CONSULTA, OBSERVACOES, NOME_MEDICO, " +
                "HORA_CONSULTA, DATA_CONSULTA, ENVIADO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'N')";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, lembrete.getTitulo());
            stmt.setInt(2, lembrete.getIdPaciente());
            stmt.setString(3, lembrete.getEspecialidade());
            stmt.setString(4, lembrete.getLocal());
            stmt.setString(5, lembrete.getObservacoes());
            stmt.setString(6, lembrete.getNomeMedico());
            stmt.setString(7, lembrete.getHoraConsulta().toString());
            stmt.setDate(8, java.sql.Date.valueOf(lembrete.getDataConsulta()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys != null && generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    logger.info("Lembrete criado com ID: " + idGerado);
                    return idGerado;
                }
            }
            logger.warning("Nenhuma linha afetada ao criar lembrete");
            return -1;

        } catch (SQLException e) {
            logger.severe("Erro ao criar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao criar lembrete: " + e.getMessage(), e);
        }
    }

    // ✅ MÉTODO: Listar lembretes por usuário (LembretePessoal)
    public List<LembretePessoal> listByUsuario(Integer usuarioId) {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES WHERE ID_USUARIO = ? ORDER BY DATA_CONSULTA DESC";

        List<LembretePessoal> lembretes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembretePessoal lembrete = new LembretePessoal();
                lembrete.setId(rs.getInt("ID_LEMBRETE"));
                lembrete.setTitulo(rs.getString("MENSAGEM"));
                lembrete.setNomeMedico(rs.getString("NOME_MEDICO"));
                lembrete.setEspecialidade(rs.getString("ESPECIALIDADE"));
                lembrete.setLocal(rs.getString("LOCAL_CONSULTA"));
                lembrete.setHoraConsulta(LocalTime.parse(rs.getString("HORA_CONSULTA")));
                lembrete.setObservacoes(rs.getString("OBSERVACOES"));
                lembrete.setEnviado(rs.getString("ENVIADO"));
                lembrete.setIdPaciente(rs.getInt("ID_USUARIO"));
                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                lembrete.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());
                lembretes.add(lembrete);
            }

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembretes por ID de usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lembretes: " + e.getMessage(), e);
        }
        return lembretes;
    }

    // ✅ MÉTODOS DTO ADICIONAIS:

    // Listar todos os lembretes como DTO
    public List<LembreteResponseDTO> findAllDTO() {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM as titulo, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES ORDER BY DATA_CONSULTA DESC";

        List<LembreteResponseDTO> lembretes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("ID_LEMBRETE"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico(rs.getString("NOME_MEDICO"));
                dto.setEspecialidade(rs.getString("ESPECIALIDADE"));
                dto.setLocalConsulta(rs.getString("LOCAL_CONSULTA"));

                String horaStr = rs.getString("HORA_CONSULTA");
                if (horaStr != null) {
                    dto.setHoraConsulta(LocalTime.parse(horaStr));
                }

                dto.setObservacoes(rs.getString("OBSERVACOES"));
                dto.setConcluido(!"S".equals(rs.getString("ENVIADO")));
                dto.setUsuarioId(rs.getInt("ID_USUARIO"));

                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                dto.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                lembretes.add(dto);
            }

        } catch (SQLException e) {
            logger.severe("Erro ao buscar todos os lembretes DTO: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lembretes: " + e.getMessage(), e);
        }
        return lembretes;
    }

    // Listar lembretes por usuário como DTO
    public List<LembreteResponseDTO> listByUsuarioDTO(Integer usuarioId) {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM as titulo, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES WHERE ID_USUARIO = ? ORDER BY DATA_CONSULTA DESC";

        List<LembreteResponseDTO> lembretes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("ID_LEMBRETE"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico(rs.getString("NOME_MEDICO"));
                dto.setEspecialidade(rs.getString("ESPECIALIDADE"));
                dto.setLocalConsulta(rs.getString("LOCAL_CONSULTA"));

                String horaStr = rs.getString("HORA_CONSULTA");
                if (horaStr != null) {
                    dto.setHoraConsulta(LocalTime.parse(horaStr));
                }

                dto.setObservacoes(rs.getString("OBSERVACOES"));
                dto.setConcluido(!"S".equals(rs.getString("ENVIADO")));
                dto.setUsuarioId(rs.getInt("ID_USUARIO"));

                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                dto.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                lembretes.add(dto);
            }

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembretes DTO por usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lembretes: " + e.getMessage(), e);
        }
        return lembretes;
    }

    // Buscar ativos por paciente como DTO
    public List<LembreteResponseDTO> buscarAtivosPorPacienteDTO(Integer pacienteId) {
        String sql = "SELECT ID_LEMBRETE, MENSAGEM as titulo, DATA_CONSULTA, HORA_CONSULTA, LOCAL_CONSULTA, ESPECIALIDADE, " +
                "OBSERVACOES, NOME_MEDICO, ENVIADO, ID_USUARIO FROM LEMBRETES WHERE ID_USUARIO = ? AND ENVIADO = 'N' ORDER BY DATA_CONSULTA DESC";

        List<LembreteResponseDTO> lembretes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("ID_LEMBRETE"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico(rs.getString("NOME_MEDICO"));
                dto.setEspecialidade(rs.getString("ESPECIALIDADE"));
                dto.setLocalConsulta(rs.getString("LOCAL_CONSULTA"));

                String horaStr = rs.getString("HORA_CONSULTA");
                if (horaStr != null) {
                    dto.setHoraConsulta(LocalTime.parse(horaStr));
                }

                dto.setObservacoes(rs.getString("OBSERVACOES"));
                dto.setConcluido(false);
                dto.setUsuarioId(rs.getInt("ID_USUARIO"));

                java.sql.Date dataSql = rs.getDate("DATA_CONSULTA");
                dto.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                lembretes.add(dto);
            }

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembretes ativos DTO por paciente: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lembretes ativos: " + e.getMessage(), e);
        }
        return lembretes;
    }
}