package com.vocabmaster.gui;

import com.vocabmaster.dao.VocabularyDAO;
import com.vocabmaster.model.Vocabulary;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class VocabularyPanel extends JPanel {
    
    private JTextField txtWord, txtTranslation, txtCategory, txtExample;
    private JTable table;
    private DefaultTableModel tableModel;
    private VocabularyDAO dao;
    private int currentSelectedId = -1;

    public VocabularyPanel() {
        this.dao = new VocabularyDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        refreshTable();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Edit Vocabulary Info"));
        panel.setBackground(Color.WHITE);
        
        txtWord = new JTextField();
        txtTranslation = new JTextField();
        txtCategory = new JTextField();
        txtExample = new JTextField();
        
        panel.add(new JLabel("Word:")); panel.add(txtWord);
        panel.add(new JLabel("Translation:")); panel.add(txtTranslation);
        panel.add(new JLabel("Category:")); panel.add(txtCategory);
        panel.add(new JLabel("Example:")); panel.add(txtExample);
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "Word", "Translation", "Category", "Example"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                try {
                    currentSelectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtWord.setText(getVal(row, 1));
                    txtTranslation.setText(getVal(row, 2));
                    txtCategory.setText(getVal(row, 3));
                    txtExample.setText(getVal(row, 4));
                } catch (Exception ex) { currentSelectedId = -1; }
            }
        });
        return new JScrollPane(table);
    }
    
    private String getVal(int row, int col) {
        Object val = tableModel.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Color.WHITE);
        
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnImport = new JButton("Import File"); 
        JButton btnClear = new JButton("Reset");

        styleButton(btnAdd, new Color(70, 130, 180));
        styleButton(btnUpdate, new Color(70, 130, 180));
        styleButton(btnDelete, new Color(220, 53, 69));
        styleButton(btnImport, new Color(40, 167, 69)); 
        styleButton(btnClear, Color.GRAY);

        btnAdd.addActionListener(e -> {
            String word = txtWord.getText();
            if (word.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Word cannot be empty!");
                return;
            }
            Vocabulary v = new Vocabulary();
            v.setWord(word);
            v.setWordTranslation(txtTranslation.getText());
            v.setCategory(txtCategory.getText());
            v.setExampleSentence(txtExample.getText());
            setDefaults(v);
            
            if (dao.addVocabulary(v)) {
                JOptionPane.showMessageDialog(this, "Added successfully!");
                refreshTable(); clearInputs();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add. (Word might already exist)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // --- Update ---
        btnUpdate.addActionListener(e -> {
            if (currentSelectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first!", "Tip", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Vocabulary v = new Vocabulary();
            v.setWordId(currentSelectedId);
            v.setWord(txtWord.getText());
            v.setWordTranslation(txtTranslation.getText());
            v.setCategory(txtCategory.getText());
            v.setExampleSentence(txtExample.getText());
            setDefaults(v);
            
            if (dao.updateVocabulary(v)) {
                JOptionPane.showMessageDialog(this, "Updated!");
                refreshTable(); clearInputs();
            }
        });
        
        // --- Delete ---
        btnDelete.addActionListener(e -> {
            if (currentSelectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first!", "Tip", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Delete word?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (dao.deleteVocabulary(currentSelectedId)) {
                    JOptionPane.showMessageDialog(this, "Deleted!");
                    refreshTable(); clearInputs();
                }
            }
        });

        // --- 【新增】Import 功能 ---
        btnImport.addActionListener(e -> performImport());
        
        // --- Reset ---
        btnClear.addActionListener(e -> clearInputs());
        
        panel.add(btnAdd); 
        panel.add(btnUpdate); 
        panel.add(btnDelete); 
        panel.add(Box.createHorizontalStrut(20)); // 加点间距
        panel.add(btnImport); 
        panel.add(btnClear);
        return panel;
    }

    private void performImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Vocabulary File (.txt or .csv)");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text/CSV Files", "txt", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            int successCount = 0;
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    

                    String[] parts = line.split(",", -1); // -1 limit keeps empty strings
                    
                    if (parts.length >= 2) {
                        Vocabulary v = new Vocabulary();
                        v.setWord(parts[0].trim());
                        v.setWordTranslation(parts[1].trim());
                        v.setCategory(parts.length > 2 ? parts[2].trim() : "Imported");
                        v.setExampleSentence(parts.length > 3 ? parts[3].trim() : "");
                        setDefaults(v);
                        
                        if (dao.addVocabulary(v)) {
                            successCount++;
                        }
                    }
                }
                JOptionPane.showMessageDialog(this, "Import Complete!\nSuccessfully added: " + successCount + " words.");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Import Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void setDefaults(Vocabulary v) {
        v.setWordLanguage("English");
        v.setDifficultyLevel(1);
        v.setPartOfSpeech("Unknown");
        v.setPronunciation("");
    }
    
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Vocabulary> list = dao.getAllVocabulary();
        for (Vocabulary v : list) {
            tableModel.addRow(new Object[]{v.getWordId(), v.getWord(), v.getWordTranslation(), v.getCategory(), v.getExampleSentence()});
        }
    }
    
    private void clearInputs() {
        txtWord.setText(""); txtTranslation.setText(""); txtCategory.setText(""); txtExample.setText("");
        currentSelectedId = -1; table.clearSelection();
    }
}