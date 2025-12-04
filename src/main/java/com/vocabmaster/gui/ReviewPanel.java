package com.vocabmaster.gui;

import com.vocabmaster.dao.VocabularyDAO;
import com.vocabmaster.model.Vocabulary;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewPanel extends JPanel {
    private VocabularyDAO dao;
    private List<Vocabulary> allWords;
    private Vocabulary currentTarget;
    private int score = 0;
    private int totalAnswered = 0;
    
    private JLabel lblWord;
    private JLabel lblScore;
    private JButton[] optionButtons;
    private JLabel lblFeedback;
    private JButton btnNext;

    public ReviewPanel() {
        dao = new VocabularyDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        allWords = dao.getAllVocabulary();
        if (allWords.size() < 4) {
            showError("Not enough words (min 4). Please add more words.");
        } else {
            initUI();
            loadNextQuestion();
        }
    }
    
    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Vocabulary Quiz", JLabel.LEFT);
        // Fix font for title
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
        
        lblScore = new JLabel("Score: 0 / 0", JLabel.RIGHT);
        // Fix font for score
        lblScore.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        lblScore.setForeground(new Color(70, 130, 180));
        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(lblScore, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        lblWord = new JLabel("WORD");
        lblWord.setFont(new Font("Arial", Font.BOLD, 40)); // English word can use Arial
        lblWord.setForeground(new Color(0, 102, 204));
        lblWord.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblFeedback = new JLabel(" ");
        // Fix font for feedback text
        lblFeedback.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        lblFeedback.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setMaximumSize(new Dimension(400, 300));
        
        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton("Option " + (i+1));
            styleOptionButton(optionButtons[i]);
            int index = i;
            optionButtons[i].addActionListener(e -> checkAnswer(index));
            optionsPanel.add(optionButtons[i]);
        }
        
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(lblWord);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(lblFeedback);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(optionsPanel);
        add(centerPanel, BorderLayout.CENTER);
        
        btnNext = new JButton("Next Question");
        // Fix font for button
        btnNext.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        btnNext.setBackground(new Color(240, 240, 240));
        btnNext.setEnabled(false);
        btnNext.addActionListener(e -> loadNextQuestion());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnNext);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadNextQuestion() {
        lblFeedback.setText(" ");
        btnNext.setEnabled(false);
        for (JButton btn : optionButtons) {
            btn.setEnabled(true);
            btn.setBackground(Color.WHITE);
        }
        
        Collections.shuffle(allWords);
        currentTarget = allWords.get(0);
        List<Vocabulary> options = new ArrayList<>();
        options.add(currentTarget);
        for (int i = 1; i < allWords.size(); i++) {
            if (options.size() >= 4) break;
            options.add(allWords.get(i));
        }
        Collections.shuffle(options);
        
        lblWord.setText(currentTarget.getWord());
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options.get(i).getWordTranslation());
            optionButtons[i].putClientProperty("vocab", options.get(i));
        }
    }
    
    private void checkAnswer(int btnIndex) {
        JButton clickedBtn = optionButtons[btnIndex];
        Vocabulary selectedVocab = (Vocabulary) clickedBtn.getClientProperty("vocab");
        totalAnswered++;
        boolean isCorrect = selectedVocab.getWordId() == currentTarget.getWordId();
        
        if (isCorrect) {
            score++;
            clickedBtn.setBackground(new Color(144, 238, 144));
            lblFeedback.setText("Correct!");
            lblFeedback.setForeground(new Color(34, 139, 34));
        } else {
            clickedBtn.setBackground(new Color(255, 182, 193));
            lblFeedback.setText("Wrong! Answer: " + currentTarget.getWordTranslation());
            lblFeedback.setForeground(Color.RED);
            for (JButton btn : optionButtons) {
                Vocabulary v = (Vocabulary) btn.getClientProperty("vocab");
                if (v.getWordId() == currentTarget.getWordId()) {
                    btn.setBackground(new Color(144, 238, 144));
                    break;
                }
            }
        }
        lblScore.setText("Score: " + score + " / " + totalAnswered);
        for (JButton btn : optionButtons) btn.setEnabled(false);
        btnNext.setEnabled(true);
        btnNext.setBackground(new Color(70, 130, 180));
        btnNext.setForeground(Color.WHITE);
    }
    
    private void styleOptionButton(JButton btn) {
        // Fix: Explicitly set font to support Chinese characters in options
        btn.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
    }
    
    private void showError(String msg) {
        removeAll();
        setLayout(new BorderLayout());
        JLabel err = new JLabel("<html><div style='text-align:center'>" + msg + "</div></html>", JLabel.CENTER);
        // Fix font for error message
        err.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        err.setForeground(Color.RED);
        add(err, BorderLayout.CENTER);
    }
}