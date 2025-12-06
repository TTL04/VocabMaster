package com.vocabmaster.dao;

import com.vocabmaster.model.Vocabulary;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VocabularyDAO {

    /**
     * 1. Add Vocabulary (Create)
     */
    public boolean addVocabulary(Vocabulary v) {
        String sql = "INSERT INTO vocabulary (word, word_translation, word_language, category, difficulty_level, example_sentence, pronunciation, part_of_speech) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getWord());
            pstmt.setString(2, v.getWordTranslation());
            pstmt.setString(3, v.getWordLanguage() != null ? v.getWordLanguage() : "English");
            pstmt.setString(4, v.getCategory());
            pstmt.setInt(5, v.getDifficultyLevel());
            pstmt.setString(6, v.getExampleSentence());
            pstmt.setString(7, v.getPronunciation());
            pstmt.setString(8, v.getPartOfSpeech());

            int affected = pstmt.executeUpdate();
            System.out.println("[INFO] Added word: " + v.getWord());
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add vocabulary: " + e.getMessage());
            return false;
        }
    }

    /**
     * 2. Get All Vocabulary (Read)
     */
    public List<Vocabulary> getAllVocabulary() {
        List<Vocabulary> list = new ArrayList<>();
        String sql = "SELECT * FROM vocabulary ORDER BY word_id DESC"; // Show newest first

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vocabulary v = new Vocabulary();
                v.setWordId(rs.getInt("word_id"));
                v.setWord(rs.getString("word"));
                v.setWordTranslation(rs.getString("word_translation"));
                v.setWordLanguage(rs.getString("word_language"));
                v.setCategory(rs.getString("category"));
                v.setDifficultyLevel(rs.getInt("difficulty_level"));
                v.setExampleSentence(rs.getString("example_sentence"));
                v.setPronunciation(rs.getString("pronunciation"));
                v.setPartOfSpeech(rs.getString("part_of_speech"));
                list.add(v);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to load vocabulary: " + e.getMessage());
        }
        return list;
    }

    /**
     * 3. Update Vocabulary (Update)
     */
    public boolean updateVocabulary(Vocabulary v) {
        String sql = "UPDATE vocabulary SET word=?, word_translation=?, category=?, example_sentence=?, difficulty_level=?, word_language=?, part_of_speech=?, pronunciation=? WHERE word_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getWord());
            pstmt.setString(2, v.getWordTranslation());
            pstmt.setString(3, v.getCategory());
            pstmt.setString(4, v.getExampleSentence());
            pstmt.setInt(5, v.getDifficultyLevel());
            pstmt.setString(6, v.getWordLanguage());
            pstmt.setString(7, v.getPartOfSpeech());
            pstmt.setString(8, v.getPronunciation());
            // ID at the end
            pstmt.setInt(9, v.getWordId());

            int affected = pstmt.executeUpdate();
            System.out.println("[INFO] Updated word ID: " + v.getWordId());
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update vocabulary: " + e.getMessage());
            return false;
        }
    }

    /**
     * 4. Delete Vocabulary (Delete)
     */
    public boolean deleteVocabulary(int id) {
        String sql = "DELETE FROM vocabulary WHERE word_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            
            int affected = pstmt.executeUpdate();
            System.out.println("[INFO] Deleted word ID: " + id);
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete vocabulary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 5. Get By ID (Helper)
     */
    public Vocabulary getVocabularyById(int id) {
        String sql = "SELECT * FROM vocabulary WHERE word_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Vocabulary v = new Vocabulary();
                v.setWordId(rs.getInt("word_id"));
                v.setWord(rs.getString("word"));
                v.setWordTranslation(rs.getString("word_translation"));
                v.setCategory(rs.getString("category"));
                v.setExampleSentence(rs.getString("example_sentence"));
                return v;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 6. Get By Category (Helper for Stats)
     */
    public List<Vocabulary> getVocabularyByCategory(String category) {
        List<Vocabulary> list = new ArrayList<>();
        String sql = "SELECT * FROM vocabulary WHERE category=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vocabulary v = new Vocabulary();
                v.setWordId(rs.getInt("word_id"));
                v.setWord(rs.getString("word"));
                v.setCategory(rs.getString("category"));
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}