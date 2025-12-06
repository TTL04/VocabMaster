/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vocabmaster.model;

import java.time.LocalDateTime;

public class LearningRecord {
    private int recordId;
    private int userId;
    private int wordId;
    private LocalDateTime studyDate;
    private int masteryLevel; // 0-100
    private int correctCount;
    private int totalAttempts;
    private LocalDateTime lastStudied;

    // Constructors
    public LearningRecord() {}

    public LearningRecord(int userId, int wordId) {
        this.userId = userId;
        this.wordId = wordId;
        this.studyDate = LocalDateTime.now();
        this.masteryLevel = 0;
        this.correctCount = 0;
        this.totalAttempts = 0;
        this.lastStudied = LocalDateTime.now();
    }

    // Getters and Setters
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getWordId() { return wordId; }
    public void setWordId(int wordId) { this.wordId = wordId; }

    public LocalDateTime getStudyDate() { return studyDate; }
    public void setStudyDate(LocalDateTime studyDate) { this.studyDate = studyDate; }

    public int getMasteryLevel() { return masteryLevel; }
    public void setMasteryLevel(int masteryLevel) { this.masteryLevel = masteryLevel; }

    public int getCorrectCount() { return correctCount; }
    public void setCorrectCount(int correctCount) { this.correctCount = correctCount; }

    public int getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }

    public LocalDateTime getLastStudied() { return lastStudied; }
    public void setLastStudied(LocalDateTime lastStudied) { this.lastStudied = lastStudied; }

    // Business Logic Methods
    public void recordAttempt(boolean isCorrect) {
        this.totalAttempts++;
        if (isCorrect) {
            this.correctCount++;
        }
        // Update mastery level
        if (totalAttempts > 0) {
            this.masteryLevel = (correctCount * 100) / totalAttempts;
        }
        this.lastStudied = LocalDateTime.now();
    }
}