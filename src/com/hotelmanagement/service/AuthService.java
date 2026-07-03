package com.hotelmanagement.service;

import com.hotelmanagement.dao.UserDAO;
import com.hotelmanagement.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) throws SQLException {
        String passwordHash = hashPassword(password);
        User user = userDAO.authenticate(username, passwordHash);
        if (user != null && "Active".equals(user.getStatus())) {
            userDAO.updateLastLogin(user.getUserID());
            return user;
        }
        return null;
    }

    public boolean changePassword(int userID, String oldPassword, String newPassword) throws SQLException {
        User user = userDAO.getUserById(userID);
        if (user == null) return false;
        if (!user.getPasswordHash().equals(hashPassword(oldPassword))) return false;
        user.setPasswordHash(hashPassword(newPassword));
        userDAO.updateUser(user);
        return true;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
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
