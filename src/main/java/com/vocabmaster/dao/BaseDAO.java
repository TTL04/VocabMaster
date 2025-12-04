/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vocabmaster.dao;

import java.sql.*;
import java.util.logging.Logger;

public class BaseDAO {
    protected static final Logger logger = Logger.getLogger(BaseDAO.class.getName());
    // Using a consistent database name
    private static final String DB_URL = "jdbc:derby:vocab_db_v8;create=true";
    private static boolean databaseInitialized = false;
    
    static {
        initializeDatabase();
    }
    
    private static void initializeDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("[INFO] Derby database connected successfully.");
            
            // Check if users table exists
            if (!tableExists(conn, "USERS")) {
                createTables(conn);
                insertTestData(conn);
                System.out.println("[INFO] Database tables created and test data inserted.");
            } else {
                System.out.println("[INFO] Database tables already exist.");
            }
            
            databaseInitialized = true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static boolean tableExists(Connection conn, String tableName) {
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, tableName.toUpperCase(), null);
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[ERROR] Error checking if table exists: " + e.getMessage());
            return false;
        }
    }
    
    private static void createTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Create Users table
            String createUserTable = """
                CREATE TABLE users (
                    user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(100),
                    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_login TIMESTAMP,
                    is_active BOOLEAN DEFAULT true
                )
                """;
            
            if (!tableExists(conn, "USERS")) {
                stmt.execute(createUserTable);
                System.out.println("[INFO] User table created successfully.");
            }

            // Create Vocabulary table
            String createVocabTable = """
                CREATE TABLE vocabulary (
                    word_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                    word VARCHAR(100) NOT NULL UNIQUE,
                    word_translation VARCHAR(100),
                    word_language VARCHAR(50),
                    category VARCHAR(50),
                    difficulty_level INT,
                    example_sentence VARCHAR(255),
                    pronunciation VARCHAR(100),
                    part_of_speech VARCHAR(50)
                )
                """;
            
            if (!tableExists(conn, "VOCABULARY")) {
                stmt.execute(createVocabTable);
                System.out.println("[INFO] Vocabulary table created successfully.");
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertTestData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Insert test users
            String insertUsers = """
                INSERT INTO users (username, password, email) VALUES 
                ('admin', 'admin123', 'admin@example.com'),
                ('testuser', 'test123', 'test@example.com'),
                ('user1', 'password1', 'user1@example.com')
                """;
            stmt.executeUpdate(insertUsers);
            System.out.println("[INFO] Test user data inserted successfully.");
            
            // Insert test vocabulary
            String insertVocab = """
                INSERT INTO vocabulary (word, word_translation, category, example_sentence, word_language, difficulty_level, part_of_speech, pronunciation) VALUES 
                ('Apple', '苹果', 'Food', 'I eat an apple.', 'English', 1, 'Noun', ''),
                ('Run', '跑', 'Sport', 'I run fast.', 'English', 1, 'Verb', ''),
                ('Java', 'Java语言', 'Tech', 'Java is robust.', 'English', 1, 'Noun', '')
                """;
            stmt.executeUpdate(insertVocab);
            System.out.println("[INFO] Test vocabulary data inserted successfully.");
            
        } catch (SQLException e) {
            // Ignore duplicate errors if data already exists
            if (!"23505".equals(e.getSQLState())) {
                System.err.println("[ERROR] Error inserting test data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    protected Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            return conn;
        } catch (SQLException e) {
            logger.severe("[ERROR] Failed to get database connection: " + e.getMessage());
            throw e;
        }
    }
    
    protected void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.severe("[ERROR] Failed to close database resources: " + e.getMessage());
        }
    }
    
    protected void closeResources(Connection conn, PreparedStatement pstmt) {
        closeResources(conn, pstmt, null);
    }
    
    // Method to shutdown database (called when application exits)
    public static void shutdownDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            // Derby throws SQLException on successful shutdown (XJ015), which is expected behavior
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("[INFO] Derby database shutdown successfully.");
            } else {
                System.err.println("[ERROR] Error shutting down database: " + e.getMessage());
            }
        }
    }
}