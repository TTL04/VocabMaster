package com.vocabmaster.gui;

import com.vocabmaster.dao.VocabularyDAO;
import com.vocabmaster.model.Vocabulary;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;

public class StatsPanel extends JPanel {
    public StatsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        List<Vocabulary> allWords = new VocabularyDAO().getAllVocabulary();
        
        JLabel title = new JLabel("Learning Statistics");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);
        
        JPanel content = new JPanel(new GridLayout(2, 1, 20, 20));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(100, 149, 237));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Total Vocabulary");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 18));
        
        JLabel lblValue = new JLabel(String.valueOf(allWords.size()));
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Arial", Font.BOLD, 48));
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        
        totalPanel.add(lblTitle, BorderLayout.NORTH);
        totalPanel.add(lblValue, BorderLayout.CENTER);
        content.add(totalPanel);
        
        Map<String, Long> cats = allWords.stream()
            .collect(Collectors.groupingBy(v -> (v.getCategory()==null || v.getCategory().isEmpty()) ? "Uncategorized" : v.getCategory(), Collectors.counting()));
        
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setText("Category Distribution:\n----------------------\n");
        cats.forEach((k, v) -> area.append(String.format("%-15s : %d\n", k, v)));
        
        content.add(new JScrollPane(area));
        add(content, BorderLayout.CENTER);
    }
}