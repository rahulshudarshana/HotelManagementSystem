package com.hotelmanagement.dao;

import com.hotelmanagement.model.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO extends BaseDAO<User> {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    private static final String COLUMNS = "UserID, EmployeeID, Username, PasswordHash, RoleID, Status, LastLogin, CreatedAt";
    private static final String SQL_AUTH = "SELECT " + COLUMNS + " FROM Users WHERE Username = ? AND PasswordHash = ?";
    private static final String SQL_BY_USERNAME = "SELECT " + COLUMNS + " FROM Users WHERE Username = ?";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM Users WHERE UserID = ?";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM Users ORDER BY Username";
    private static final String SQL_INSERT = "INSERT INTO Users (EmployeeID, Username, PasswordHash, RoleID, Status) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Users SET EmployeeID = ?, Username = ?, PasswordHash = ?, RoleID = ?, Status = ?, LastLogin = ? WHERE UserID = ?";
    private static final String SQL_UPDATE_LAST_LOGIN = "UPDATE Users SET LastLogin = NOW() WHERE UserID = ?";
    private static final String SQL_DELETE = "DELETE FROM Users WHERE UserID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM Users WHERE LOWER(Username) LIKE ? ORDER BY Username";

    public User authenticate(String username, String passwordHash) throws SQLException {
        return findOne(SQL_AUTH, username, passwordHash);
    }

    public User getUserByUsername(String username) throws SQLException {
        return findOne(SQL_BY_USERNAME, username);
    }

    public User getUserByUsername(String username, Connection conn) throws SQLException {
        return findOne(SQL_BY_USERNAME, conn, username);
    }

    public User getUserById(int userID) throws SQLException {
        return findOne(SQL_BY_ID, userID);
    }

    public List<User> getAllUsers() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<User> searchUsers(String keyword) throws SQLException {
        return findAll(SQL_SEARCH, "%" + keyword.toLowerCase() + "%");
    }

    public int insertUser(User user) throws SQLException {
        return insert(SQL_INSERT,
            user.getEmployeeID(), user.getUsername(), user.getPasswordHash(),
            user.getRoleID(), user.getStatus() != null ? user.getStatus() : "Active");
    }

    public int insertUser(User user, Connection conn) throws SQLException {
        return insert(SQL_INSERT, conn,
            user.getEmployeeID(), user.getUsername(), user.getPasswordHash(),
            user.getRoleID(), user.getStatus() != null ? user.getStatus() : "Active");
    }

    public void updateUser(User user) throws SQLException {
        update(SQL_UPDATE,
            user.getEmployeeID(), user.getUsername(), user.getPasswordHash(),
            user.getRoleID(), user.getStatus(), user.getLastLogin(), user.getUserID());
    }

    public void updateLastLogin(int userID) throws SQLException {
        update(SQL_UPDATE_LAST_LOGIN, userID);
    }

    public void deleteUser(int userID) throws SQLException {
        delete(SQL_DELETE, userID);
    }

    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("UserID"));
        user.setEmployeeID(rs.getInt("EmployeeID"));
        user.setUsername(rs.getString("Username"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setRoleID(rs.getInt("RoleID"));
        user.setStatus(rs.getString("Status"));
        if (rs.getTimestamp("LastLogin") != null) {
            user.setLastLogin(rs.getTimestamp("LastLogin").toLocalDateTime());
        }
        if (rs.getTimestamp("CreatedAt") != null) {
            user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return user;
    }
}
