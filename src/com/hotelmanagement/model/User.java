package com.hotelmanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private int userID;
    private int employeeID;
    private String username;
    private String passwordHash;
    private int roleID;
    private String status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;

    public User() {}

    public User(int userID, int employeeID, String username, String passwordHash, int roleID) {
        this.userID = userID;
        this.employeeID = employeeID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleID = roleID;
    }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getRoleID() { return roleID; }
    public void setRoleID(int roleID) { this.roleID = roleID; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID == user.userID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userID);
    }

    @Override
    public String toString() {
        return "User{userID=" + userID + ", username='" + username + "', status='" + status + "'}";
    }
}
