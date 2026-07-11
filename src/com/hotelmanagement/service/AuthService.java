package com.hotelmanagement.service;

import com.hotelmanagement.dao.UserDAO;
import com.hotelmanagement.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty() || password == null) {
            return null;
        }
        String passwordHash = hashPassword(password);
        User user = userDAO.authenticate(username, passwordHash);
        if (user != null && "Active".equals(user.getStatus())) {
            userDAO.updateLastLogin(user.getUserID());
            LOGGER.log(Level.INFO, "User {0} logged in", username);
            return user;
        }
        LOGGER.log(Level.WARNING, "Login failed for user {0}", username);
        return null;
    }

    public boolean changePassword(int userID, String oldPassword, String newPassword) throws SQLException {
        User user = userDAO.getUserById(userID);
        if (user == null) return false;
        if (!user.getPasswordHash().equals(hashPassword(oldPassword))) return false;
        user.setPasswordHash(hashPassword(newPassword));
        userDAO.updateUser(user);
        LOGGER.log(Level.INFO, "Password changed for user {0}", user.getUsername());
        return true;
    }

    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty.");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
