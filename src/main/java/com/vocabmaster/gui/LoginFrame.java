/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vocabmaster.gui;

import com.vocabmaster.model.User;
import com.vocabmaster.service.UserService;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private UserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    // Color Palette Configuration
    private final Color PRIMARY_COLOR = new Color(51, 153, 255); // Bright Blue
    private final Color HOVER_COLOR = new Color(30, 130, 230);   // Darker Blue for Hover
    private final Color BG_COLOR = new Color(240, 242, 245);     // Light Grey Background
    private final Color TEXT_COLOR = new Color(50, 50, 50);      // Dark Grey Text
    
    public LoginFrame() {
        userService = new UserService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("VocabMaster - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600); // Increased height for better spacing
        setLocationRelativeTo(null); // Center on screen
        
        // 1. Global Background Panel (Centers the card)
        JPanel backgroundPanel = new JPanel(new GridBagLayout()); 
        backgroundPanel.setBackground(BG_COLOR);
        
        // 2. White Login Card
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        cardPanel.setPreferredSize(new Dimension(380, 500));
        
        // --- Card Content ---
        
        // A. Header
        JLabel logoLabel = new JLabel("📚", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("VocabMaster");
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Welcome Back!");
        subtitleLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // B. Inputs (CENTERED)
        JLabel userLbl = createLabel("Username");
        usernameField = createStyledTextField();
        
        JLabel passLbl = createLabel("Password");
        passwordField = createStyledPasswordField();
        
        // C. Buttons (Side-by-Side & Centered)
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Important: Center the panel itself
        
        loginButton = createStyledButton("Login", PRIMARY_COLOR, Color.WHITE);
        registerButton = createStyledButton("Register", Color.WHITE, PRIMARY_COLOR);
        registerButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        
        btnPanel.add(loginButton);
        btnPanel.add(registerButton);
        
        // D. Default Account Tip (Centered)
        JLabel tipLabel = new JLabel("Default Admin: admin / admin123");
        tipLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        tipLabel.setForeground(new Color(150, 150, 150));
        tipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tipLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tipLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                usernameField.setText("admin");
                passwordField.setText("admin123");
            }
        });
        
        // --- Assemble Card ---
        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(titleLabel);
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        
        // Add components (Everything is centered now)
        cardPanel.add(userLbl);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createVerticalStrut(15));
        
        cardPanel.add(passLbl);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(30));
        
        cardPanel.add(btnPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(tipLabel);
        
        backgroundPanel.add(cardPanel);
        setContentPane(backgroundPanel);
        
        addEventListeners();
    }
    
    // --- UI Styles (Fixed Center Alignment) ---
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Microsoft YaHei", Font.BOLD, 12));
        lbl.setForeground(TEXT_COLOR);
        // [FIX] Force Center Alignment
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT); 
        return lbl;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        // [FIX] Center text inside the box
        field.setHorizontalAlignment(JTextField.CENTER); 
        
        Border padding = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        Border line = BorderFactory.createLineBorder(new Color(200, 200, 200));
        field.setBorder(BorderFactory.createCompoundBorder(line, padding));
        // [FIX] Center the component itself
        field.setAlignmentX(Component.CENTER_ALIGNMENT); 
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        // [FIX] Center password dots
        field.setHorizontalAlignment(JTextField.CENTER); 
        
        Border padding = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        Border line = BorderFactory.createLineBorder(new Color(200, 200, 200));
        field.setBorder(BorderFactory.createCompoundBorder(line, padding));
        // [FIX] Center the component itself
        field.setAlignmentX(Component.CENTER_ALIGNMENT); 
        return field;
    }
    
    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (bg.equals(PRIMARY_COLOR)) {
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(HOVER_COLOR); }
                public void mouseExited(MouseEvent e) { btn.setBackground(PRIMARY_COLOR); }
            });
        }
        return btn;
    }
    
    // --- Logic ---
    
    private void addEventListeners() {
        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> showRegistrationDialog());
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performLogin();
            }
        });
    }
    
    private void performLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());
        
        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password required.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        loginButton.setEnabled(false);
        loginButton.setText("Wait...");
        
        new Thread(() -> {
            try {
                User user = userService.loginUser(u, p);
                SwingUtilities.invokeLater(() -> {
                    if (user != null) {
                        openMainDashboard(user);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        resetButtons();
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    resetButtons();
                });
            }
        }).start();
    }
    
    private void showRegistrationDialog() {
        JTextField uField = new JTextField();
        JPasswordField pField = new JPasswordField();
        JTextField eField = new JTextField();
        
        // Custom layout for registration dialog
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Username:")); panel.add(uField);
        panel.add(new JLabel("Password:")); panel.add(pField);
        panel.add(new JLabel("Email:")); panel.add(eField);
        
        int option = JOptionPane.showConfirmDialog(this, panel, "Register Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            String u = uField.getText().trim();
            String p = new String(pField.getPassword());
            String email = eField.getText().trim();
            
            if(u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Missing info!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                if(userService.registerUser(u, p, email)) {
                    JOptionPane.showMessageDialog(this, "Account created! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    usernameField.setText(u);
                    passwordField.setText("");
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openMainDashboard(User user) {
        SwingUtilities.invokeLater(() -> {
            MainDashboard md = new MainDashboard(user);
            md.setVisible(true);
        });
    }
    
    private void resetButtons() {
        loginButton.setEnabled(true);
        loginButton.setText("Login");
    }
}