/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vocabmaster.service;

import com.vocabmaster.dao.VocabularyDAO;
import com.vocabmaster.model.Vocabulary;
import java.util.List;

/**
 * Vocabulary Service Layer: Handles business logic for vocabulary management (validation, default value processing).
 */
public class VocabularyService {
    
    private VocabularyDAO vocabDAO;

    public VocabularyService() {
        this.vocabDAO = new VocabularyDAO();
    }

    /**
     * Add a new vocabulary word.
     * @param v The vocabulary object
     * @return true if successful
     * @throws IllegalArgumentException if required fields are empty
     */
    public boolean addVocabulary(Vocabulary v) {
        // 1. Validate required fields
        if (v == null) {
            throw new IllegalArgumentException("Data object cannot be null.");
        }
        if (v.getWord() == null || v.getWord().trim().isEmpty()) {
            throw new IllegalArgumentException("Word spelling cannot be empty!");
        }
        if (v.getWordTranslation() == null || v.getWordTranslation().trim().isEmpty()) {
            throw new IllegalArgumentException("Translation cannot be empty!");
        }

        // 2. Set default values (if not provided by user)
        if (v.getWordLanguage() == null || v.getWordLanguage().isEmpty()) {
            v.setWordLanguage("English");
        }
        if (v.getDifficultyLevel() <= 0) {
            v.setDifficultyLevel(1); // Default difficulty 1
        }
        if (v.getPartOfSpeech() == null) {
            v.setPartOfSpeech("Unknown");
        }

        // 3. Call DAO to save
        return vocabDAO.addVocabulary(v);
    }

    /**
     * Update an existing vocabulary word.
     */
    public boolean updateVocabulary(Vocabulary v) {
        // Validate if ID is valid
        if (v.getWordId() <= 0) {
            throw new IllegalArgumentException("Invalid Word ID, cannot update.");
        }
        
        // Validate content
        if (v.getWord() == null || v.getWord().trim().isEmpty()) {
            throw new IllegalArgumentException("Word spelling cannot be empty.");
        }
        
        return vocabDAO.updateVocabulary(v);
    }

    /**
     * Delete a vocabulary word.
     */
    public boolean deleteVocabulary(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID.");
        }
        return vocabDAO.deleteVocabulary(id);
    }

    /**
     * Retrieve all vocabulary words.
     */
    public List<Vocabulary> getAllVocabulary() {
        return vocabDAO.getAllVocabulary();
    }

    /**
     * Retrieve vocabulary by category (used for the statistics panel).
     */
    public List<Vocabulary> getVocabularyByCategory(String category) {
        if (category == null) return null;
        return vocabDAO.getVocabularyByCategory(category);
    }
    
    /**
     * Retrieve a single vocabulary word by ID.
     */
    public Vocabulary getVocabularyById(int id) {
        return vocabDAO.getVocabularyById(id);
    }
}