package com.hotelmanagement.service;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.dao.UserDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User getUserById(int userID) throws SQLException {
        return userDAO.getUserById(userID);
    }

    public User getUserByUsername(String username) throws SQLException {
        return userDAO.getUserByUsername(username);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public List<User> searchUsers(String keyword) throws SQLException {
        return userDAO.searchUsers(keyword);
    }

    public int createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username is required.");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            throw new ValidationException("Password is required.");
        }
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            if (userDAO.getUserByUsername(user.getUsername(), conn) != null) {
                throw new ValidationException("Username already exists.");
            }
            user.setPasswordHash(AuthService.hashPassword(user.getPasswordHash()));
            int id = userDAO.insertUser(user);

            conn.commit();
            LOGGER.log(Level.INFO, "User {0} created", id);
            return id;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "User creation failed", e);
            throw new DataAccessException("User creation failed due to a database error.", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to restore auto-commit", e);
                }
                try { conn.close(); } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to close connection", e);
                }
            }
        }
    }

    public void updateUser(User user) {
        try {
            userDAO.updateUser(user);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "User update failed", e);
            throw new DataAccessException("User update failed due to a database error.", e);
        }
    }

    public void deleteUser(int userID) {
        try {
            userDAO.deleteUser(userID);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "User deletion failed", e);
            throw new DataAccessException("User deletion failed due to a database error.", e);
        }
    }
}
