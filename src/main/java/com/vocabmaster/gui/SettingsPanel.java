package com.vocabmaster.gui;

import com.vocabmaster.dao.UserDAO;
import com.vocabmaster.model.User;
import java.awt.*;
import javax.swing.*;

public class SettingsPanel extends JPanel {
    private User currentUser;
    private JPasswordField txtNewPass, txtConfirm;

    public SettingsPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        add(new JLabel("System Settings", SwingConstants.LEFT), BorderLayout.NORTH);
        
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JPanel passPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        passPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        passPanel.setBackground(Color.WHITE);
        passPanel.setMaximumSize(new Dimension(400, 200));
        passPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtNewPass = new JPasswordField();
        txtConfirm = new JPasswordField();
        JButton btnSave = new JButton("Save Password");
        btnSave.setBackground(new Color(70, 130, 180));
        btnSave.setForeground(Color.WHITE);
        
        passPanel.add(new JLabel("New Password:")); passPanel.add(txtNewPass);
        passPanel.add(new JLabel("Confirm:")); passPanel.add(txtConfirm);
        passPanel.add(new JLabel("")); passPanel.add(btnSave);
        
        btnSave.addActionListener(e -> {
            String p1 = new String(txtNewPass.getPassword());
            String p2 = new String(txtConfirm.getPassword());
            if (p1.length() < 3) {
                JOptionPane.showMessageDialog(this, "Password too short!"); return;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!"); return;
            }
            if (new UserDAO().updatePassword(currentUser.getUserId(), p1)) {
                JOptionPane.showMessageDialog(this, "Password Changed Successfully!");
                txtNewPass.setText(""); txtConfirm.setText("");
            }
        });
        
        center.add(passPanel);
        center.add(Box.createVerticalStrut(20));
        center.add(new JLabel("VocabMaster v1.0 - English Version"));
        
        add(center, BorderLayout.CENTER);
    }
}