package com.vocabmaster;

import com.vocabmaster.gui.LoginFrame;
import com.vocabmaster.dao.DatabaseConnection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Enumeration;

public class VocabMaster {
    
    public static void main(String[] args) {
       
        setGlobalFont(new Font("Microsoft YaHei", Font.PLAIN, 14));

        // Start the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                System.out.println("[INFO] GUI Launched Successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Pre-heat database connection in a background thread to improve startup speed
        new Thread(() -> {
            try {
                DatabaseConnection.getConnection();
            } catch (Exception e) {
                System.err.println("[WARN] Database pre-heat failed: " + e.getMessage());
            }
        }).start();
        
        // Register a shutdown hook to close the database connection cleanly
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::closeConnection));
    }

 
    private static void setGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}