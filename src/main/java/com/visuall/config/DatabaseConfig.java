package com.visuall.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "rm563412";
    private static final String PASSWORD = "091006";

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("✅ Driver Oracle carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ Driver Oracle não encontrado", e);
        }
    }

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexão com Oracle estabelecida!");
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erro na conexão com Oracle: " + e.getMessage(), e);
        }
    }
}