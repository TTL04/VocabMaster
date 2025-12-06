package com.vocabmaster.gui;

import com.vocabmaster.dao.VocabularyDAO;
import com.vocabmaster.model.Vocabulary;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class StudyPanel extends JPanel {
    private VocabularyDAO dao;
    private List<Vocabulary> studyList;
    private int currentIndex = 0;
    
    private JLabel lblWord;
    private JLabel lblPronunciation;
    private JTextArea txtExample;
    private JLabel lblTranslation;
    private JPanel answerPanel;
    private JButton btnShowAnswer;
    private JButton btnNext;
    private JProgressBar progressBar;

    public StudyPanel() {
        this.dao = new VocabularyDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initData();
        if (studyList.isEmpty()) {
            showEmptyState();
        } else {
            initUI();
            showCurrentWord();
        }
    }
    
    private void initData() {
        studyList = dao.getAllVocabulary();
        Collections.shuffle(studyList);
        currentIndex = 0;
    }
    
    private void initUI() {
        progressBar = new JProgressBar(0, studyList.size());
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        // Fix font for progress bar
        progressBar.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        add(progressBar, BorderLayout.NORTH);
        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(245, 245, 250));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        lblWord = new JLabel("Word");
        // Fix font for word display
        lblWord.setFont(new Font("Microsoft YaHei", Font.BOLD, 36));
        lblWord.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblWord.setForeground(new Color(70, 130, 180));
        
        lblPronunciation = new JLabel("/pronunciation/");
        lblPronunciation.setFont(new Font("Microsoft YaHei", Font.ITALIC, 16));
        lblPronunciation.setForeground(Color.GRAY);
        lblPronunciation.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtExample = new JTextArea("Example...");
        txtExample.setWrapStyleWord(true);
        txtExample.setLineWrap(true);
        txtExample.setEditable(false);
        txtExample.setOpaque(false);
        // Fix font for example sentence (crucial for Chinese characters)
        txtExample.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));
        
        answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setOpaque(false);
        answerPanel.setVisible(false);
        
        lblTranslation = new JLabel("Meaning");
        // Fix font for translation
        lblTranslation.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        lblTranslation.setForeground(new Color(34, 139, 34));
        lblTranslation.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerPanel.add(lblTranslation);
        
        cardPanel.add(lblWord);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(lblPronunciation);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(txtExample);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(answerPanel);
        add(cardPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);
        
        btnShowAnswer = new JButton("Show Answer");
        btnNext = new JButton("Next");
        btnNext.setEnabled(false);
        
        styleButton(btnShowAnswer, new Color(100, 149, 237));
        styleButton(btnNext, new Color(60, 179, 113));
        
        btnShowAnswer.addActionListener(e -> {
            answerPanel.setVisible(true);
            btnShowAnswer.setEnabled(false);
            btnNext.setEnabled(true);
        });
        
        btnNext.addActionListener(e -> {
            currentIndex++;
            if (currentIndex < studyList.size()) {
                showCurrentWord();
            } else {
                JOptionPane.showMessageDialog(this, "Congratulations! Session complete.");
                initData();
                showCurrentWord();
            }
        });
        
        btnPanel.add(btnShowAnswer);
        btnPanel.add(btnNext);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    private void showCurrentWord() {
        Vocabulary v = studyList.get(currentIndex);
        lblWord.setText(v.getWord());
        String pron = v.getPronunciation();
        lblPronunciation.setText(pron != null && !pron.isEmpty() ? "/" + pron + "/" : "");
        txtExample.setText(v.getExampleSentence());
        lblTranslation.setText(v.getWordTranslation());
        answerPanel.setVisible(false);
        btnShowAnswer.setEnabled(true);
        btnNext.setEnabled(false);
        progressBar.setValue(currentIndex + 1);
        progressBar.setString("Progress: " + (currentIndex + 1) + " / " + studyList.size());
    }
    
    private void showEmptyState() {
        removeAll();
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Library empty. Please add words in Vocabulary Mgmt first!", JLabel.CENTER);
        // Fix font for empty state message
        label.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        add(label, BorderLayout.CENTER);
    }
    
    private void styleButton(JButton btn, Color color) {
        // Fix font for buttons
        btn.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
    }
}