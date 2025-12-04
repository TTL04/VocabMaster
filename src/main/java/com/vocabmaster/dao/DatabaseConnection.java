package com.vocabmaster.dao;

import java.sql.*;

public class DatabaseConnection {
    // New DB name to avoid locks
    private static final String DB_URL = "jdbc:derby:vocab_db_v8;create=true";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        createTablesIfNotExist(conn);
        return conn;
    }

    private static void createTablesIfNotExist(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            
            // 1. Users Table
            if (!tableExists(conn, "USERS")) {
                stmt.execute("CREATE TABLE users (" +
                        "user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        "username VARCHAR(50) NOT NULL UNIQUE, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(100), " +
                        "is_active BOOLEAN DEFAULT true)");
                
                // Insert 3 Default Users
                insertUserSafe(stmt, "admin", "admin123", "admin@sys.com");
                insertUserSafe(stmt, "testuser", "test123", "test@example.com");
                insertUserSafe(stmt, "user1", "password1", "user1@example.com");
                
                System.out.println("[INFO] Default users created (admin, testuser, user1).");
            }

            // 2. Vocabulary Table
            if (!tableExists(conn, "VOCABULARY")) {
                stmt.execute("CREATE TABLE vocabulary (" +
                        "word_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        "word VARCHAR(100) NOT NULL UNIQUE, " +
                        "word_translation VARCHAR(100), " +
                        "word_language VARCHAR(50), " +
                        "category VARCHAR(50), " +
                        "difficulty_level INT, " +
                        "example_sentence VARCHAR(255), " +
                        "pronunciation VARCHAR(100), " +
                        "part_of_speech VARCHAR(50))");
                
                // Sample Data
                insertVocabSafe(stmt, "Apple", "苹果", "Food", "I eat an apple.");
                insertVocabSafe(stmt, "Run", "跑", "Sport", "I run fast.");
                insertVocabSafe(stmt, "Java", "Java语言", "Tech", "Java is robust.");
                
                System.out.println("[INFO] Vocabulary table created.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertUserSafe(Statement stmt, String u, String p, String e) {
        try {
            stmt.executeUpdate("INSERT INTO users (username, password, email) VALUES ('" + u + "', '" + p + "', '" + e + "')");
        } catch (SQLException ex) {}
    }

    private static void insertVocabSafe(Statement stmt, String w, String t, String c, String e) {
        try {
            String sql = String.format("INSERT INTO vocabulary (word, word_translation, category, example_sentence, word_language, difficulty_level, part_of_speech, pronunciation) " +
                    "VALUES ('%s', '%s', '%s', '%s', 'English', 1, 'Noun', '')", w, t, c, e);
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {}
    }

    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        return conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null).next();
    }

    public static void closeConnection() {
        try { DriverManager.getConnection("jdbc:derby:;shutdown=true"); } catch (Exception e) {}
    }
}