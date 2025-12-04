package com.vocabmaster.gui;

import com.vocabmaster.model.User;
import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    
    public MainDashboard(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("VocabMaster - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createSidebarPanel(), BorderLayout.WEST);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setPreferredSize(new Dimension(getWidth(), 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("VocabMaster");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("User: " + currentUser.getUsername());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(userLabel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(245, 245, 245));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        JButton[] buttons = {
            createMenuButton("Vocabulary Mgmt"),
            createMenuButton("Start Learning"),
            createMenuButton("Review Quiz"),
            createMenuButton("Statistics"),
            createMenuButton("Settings"),
            createMenuButton("Logout")
        };
        
        for (JButton btn : buttons) {
            sidebarPanel.add(btn);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        addMenuListeners(buttons);
        return sidebarPanel;
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 45));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
    
    private void addMenuListeners(JButton[] buttons) {
        buttons[0].addActionListener(e -> switchContent(new VocabularyPanel()));
        buttons[1].addActionListener(e -> switchContent(new StudyPanel()));
        buttons[2].addActionListener(e -> switchContent(new ReviewPanel()));
        buttons[3].addActionListener(e -> switchContent(new StatsPanel()));
        buttons[4].addActionListener(e -> switchContent(new SettingsPanel(currentUser)));
        buttons[5].addActionListener(e -> logout());
    }
    
    private void switchContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel welcome = new JLabel(
            "<html><center><h1>Welcome, " + currentUser.getUsername() + "</h1>" +
            "<p>Please select a function from the left menu.</p></center></html>", 
            JLabel.CENTER);
        contentPanel.add(welcome, BorderLayout.CENTER);
        return contentPanel;
    }
    
    private void logout() {
        int opt = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}