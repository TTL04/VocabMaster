/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vocabmaster.service;

import com.vocabmaster.dao.UserDAO;
import com.vocabmaster.model.User;
import java.util.List;

/**
 * User Service Layer: Handles business logic validation for registration and login.
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    

    public boolean registerUser(String username, String password, String email) {
        // 1. Basic input validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.length() < 3) { // Slightly relaxed restriction for testing purposes
            throw new IllegalArgumentException("Password must be at least 3 characters long");
        }
        
        // 2. Check if username already exists (Business Rule)
        if (userDAO.userExists(username.trim())) {
            throw new IllegalArgumentException("Username already taken, please choose another one");
        }
        
        // 3. Create User object and save
        // Note: ID does not need to be set manually, database generates it automatically
        User user = new User(username.trim(), password, email);
        return userDAO.addUser(user);
    }
    
    /**
     * User Login
     * @param username The username
     * @param password The password
     * @return The User object if authenticated, null otherwise
     */
    public User loginUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Call DAO for database query
        return userDAO.authenticateUser(username.trim(), password);
    }
    
    /**
     * Get all users (for debugging or admin functions)
     * @return List of users
     */
    public List<User> getAllUsers() {
        // Placeholder: Implement if admin features are needed later
        return null; 
    }
    
    /**
     * Verify if username is available
     * @param username The username to check
     * @return true if available (not taken), false otherwise
     */
    public boolean isUsernameAvailable(String username) {
        return !userDAO.userExists(username);
    }
}