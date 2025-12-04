package com.vocabmaster.dao;

import com.vocabmaster.model.Vocabulary;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

/**
 * Unit Test Class: Tests for VocabularyDAO functionality.
 * [cite_start]Requirement: At least 5 test cases [cite: 112]
 */
public class VocabularyDAOTest {
    
    // Ensure database connection is ready before running any tests
    @BeforeAll
    public static void setup() {
        try {
            // Trigger connection to ensure database initialization
            DatabaseConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test Case 1: Test adding a vocabulary word (Create)
     */
    @Test
    public void testAddVocabulary() {
        System.out.println("Running Test 1: Add Vocabulary");
        VocabularyDAO dao = new VocabularyDAO();
        
        // Create a test object
        Vocabulary v = new Vocabulary();
        v.setWord("TestWord_Add");
        v.setWordTranslation("Test Add Translation");
        v.setWordLanguage("English");
        v.setCategory("Test");
        v.setDifficultyLevel(1);
        
        // Execute add operation
        boolean result = dao.addVocabulary(v);
        
        // Assertion: Must return true
        assertTrue(result, "Add vocabulary should return true");
        
        // Verification: Check if it can be retrieved from DB
        List<Vocabulary> list = dao.getAllVocabulary();
        boolean found = list.stream().anyMatch(word -> word.getWord().equals("TestWord_Add"));
        assertTrue(found, "The added word should be found in the database");
    }

    /**
     * Test Case 2: Test retrieving all vocabulary words (Read)
     */
    @Test
    public void testGetAllVocabulary() {
        System.out.println("Running Test 2: Get All Vocabulary");
        VocabularyDAO dao = new VocabularyDAO();
        
        List<Vocabulary> list = dao.getAllVocabulary();
        
        // Assertion: Result should not be null
        assertNotNull(list);
        System.out.println("Current words in DB: " + list.size());
    }

    /**
     * Test Case 3: Test updating a vocabulary word (Update)
     */
    @Test
    public void testUpdateVocabulary() {
        System.out.println("Running Test 3: Update Vocabulary");
        VocabularyDAO dao = new VocabularyDAO();
        
        // 1. Add a word first
        Vocabulary v = new Vocabulary();
        v.setWord("OldName");
        v.setWordTranslation("Old Meaning");
        v.setCategory("Test");
        dao.addVocabulary(v);
        
        // 2. Find its ID
        List<Vocabulary> list = dao.getAllVocabulary();
        int targetId = -1;
        for (Vocabulary word : list) {
            if (word.getWord().equals("OldName")) {
                targetId = word.getWordId();
                break;
            }
        }
        
        // 3. Update it
        if (targetId != -1) {
            Vocabulary newVal = new Vocabulary();
            newVal.setWordId(targetId); // Critical: ID must be correct
            newVal.setWord("NewName");
            newVal.setWordTranslation("New Meaning");
            newVal.setCategory("Test");
            newVal.setDifficultyLevel(2);
            
            boolean updateResult = dao.updateVocabulary(newVal);
            assertTrue(updateResult, "Update should return true");
            
            // Verify if update took effect
            Vocabulary updatedWord = dao.getVocabularyById(targetId);
            assertEquals("NewName", updatedWord.getWord());
        }
    }

    /**
     * Test Case 4: Test deleting a vocabulary word (Delete)
     */
    @Test
    public void testDeleteVocabulary() {
        System.out.println("Running Test 4: Delete Vocabulary");
        VocabularyDAO dao = new VocabularyDAO();
        
        // 1. Add a word to delete
        Vocabulary v = new Vocabulary();
        v.setWord("ToDelete");
        v.setWordTranslation("To Be Deleted");
        v.setCategory("Test");
        dao.addVocabulary(v);
        
        // 2. Find its ID
        List<Vocabulary> list = dao.getAllVocabulary();
        int idToDelete = -1;
        for (Vocabulary word : list) {
            if (word.getWord().equals("ToDelete")) {
                idToDelete = word.getWordId();
                break;
            }
        }
        
        // 3. Delete it
        if (idToDelete != -1) {
            boolean deleteResult = dao.deleteVocabulary(idToDelete);
            assertTrue(deleteResult);
            
            // Verify if it's really gone
            Vocabulary deletedWord = dao.getVocabularyById(idToDelete);
            assertNull(deletedWord, "Deleted word should be null when searched by ID");
        }
    }
    
    /**
     * Test Case 5: Test retrieval by category
     */
    @Test
    public void testGetByCategory() {
        System.out.println("Running Test 5: Get By Category");
        VocabularyDAO dao = new VocabularyDAO();
        
        Vocabulary v = new Vocabulary();
        v.setWord("CatWord");
        v.setWordTranslation("Test Category");
        v.setCategory("UniqueCategory123");
        dao.addVocabulary(v);
        
        List<Vocabulary> list = dao.getVocabularyByCategory("UniqueCategory123");
        assertTrue(list.size() > 0);
        assertEquals("CatWord", list.get(0).getWord());
    }
}