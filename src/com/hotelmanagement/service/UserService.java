package com.hotelmanagement.service;

import com.hotelmanagement.dao.UserDAO;
import com.hotelmanagement.model.User;
import java.sql.SQLException;
import java.util.List;

public class UserService {
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

    public int createUser(User user) throws SQLException {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }
        user.setPasswordHash(AuthService.hashPassword(user.getPasswordHash()));
        return userDAO.insertUser(user);
    }

    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    public void deleteUser(int userID) throws SQLException {
        userDAO.deleteUser(userID);
    }
}
