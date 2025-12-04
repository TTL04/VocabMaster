/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vocabmaster.model;

public class Vocabulary {
    private int wordId;
    private String word;
    private String wordTranslation;
    private String wordLanguage;
    private String category;
    private int difficultyLevel;
    private String exampleSentence;
    private String pronunciation;
    private String partOfSpeech;

    // Constructors
    public Vocabulary() {}

    public Vocabulary(String word, String wordTranslation, String wordLanguage, String category) {
        this.word = word;
        this.wordTranslation = wordTranslation;
        this.wordLanguage = wordLanguage;
        this.category = category;
        this.difficultyLevel = 1;
    }

    // Getters and Setters
    public int getWordId() { return wordId; }
    public void setWordId(int wordId) { this.wordId = wordId; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getWordTranslation() { return wordTranslation; }
    public void setWordTranslation(String wordTranslation) { this.wordTranslation = wordTranslation; }

    public String getWordLanguage() { return wordLanguage; }
    public void setWordLanguage(String wordLanguage) { this.wordLanguage = wordLanguage; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getExampleSentence() { return exampleSentence; }
    public void setExampleSentence(String exampleSentence) { this.exampleSentence = exampleSentence; }

    public String getPronunciation() { return pronunciation; }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }

    public String getPartOfSpeech() { return partOfSpeech; }
    public void setPartOfSpeech(String partOfSpeech) { this.partOfSpeech = partOfSpeech; }

    @Override
    public String toString() {
        return word + " - " + wordTranslation + " (" + wordLanguage + ")";
    }
}