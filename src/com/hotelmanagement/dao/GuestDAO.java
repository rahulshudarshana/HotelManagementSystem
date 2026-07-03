package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.enums.Gender;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public Guest getGuestById(int guestID) throws SQLException {
        String sql = "SELECT * FROM Guests WHERE GuestID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGuest(rs);
                }
            }
        }
        return null;
    }

    public Guest getGuestByNIC(String nic) throws SQLException {
        String sql = "SELECT * FROM Guests WHERE NIC = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nic);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGuest(rs);
                }
            }
        }
        return null;
    }

    public Guest getGuestByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM Guests WHERE Phone = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGuest(rs);
                }
            }
        }
        return null;
    }

    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM Guests ORDER BY LastName, FirstName";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                guests.add(mapGuest(rs));
            }
        }
        return guests;
    }

    public List<Guest> searchGuests(String keyword) throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM Guests WHERE FirstName LIKE ? OR LastName LIKE ? OR NIC LIKE ? OR Phone LIKE ? OR Email LIKE ? ORDER BY LastName, FirstName";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);
            stmt.setString(5, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    guests.add(mapGuest(rs));
                }
            }
        }
        return guests;
    }

    public int insertGuest(Guest guest) throws SQLException {
        String sql = "INSERT INTO Guests (FirstName, LastName, NIC, Phone, Email, Address, Gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            stmt.setString(3, guest.getNic());
            stmt.setString(4, guest.getPhone());
            stmt.setString(5, guest.getEmail());
            stmt.setString(6, guest.getAddress());
            stmt.setString(7, guest.getGender().name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateGuest(Guest guest) throws SQLException {
        String sql = "UPDATE Guests SET FirstName = ?, LastName = ?, NIC = ?, Phone = ?, Email = ?, Address = ?, Gender = ? WHERE GuestID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            stmt.setString(3, guest.getNic());
            stmt.setString(4, guest.getPhone());
            stmt.setString(5, guest.getEmail());
            stmt.setString(6, guest.getAddress());
            stmt.setString(7, guest.getGender().name());
            stmt.setInt(8, guest.getGuestID());
            stmt.executeUpdate();
        }
    }

    public void deleteGuest(int guestID) throws SQLException {
        String sql = "DELETE FROM Guests WHERE GuestID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestID);
            stmt.executeUpdate();
        }
    }

    private Guest mapGuest(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setGuestID(rs.getInt("GuestID"));
        guest.setFirstName(rs.getString("FirstName"));
        guest.setLastName(rs.getString("LastName"));
        guest.setNic(rs.getString("NIC"));
        guest.setPhone(rs.getString("Phone"));
        guest.setEmail(rs.getString("Email"));
        guest.setAddress(rs.getString("Address"));
        guest.setGender(Gender.valueOf(rs.getString("Gender")));
        if (rs.getTimestamp("CreatedAt") != null) {
            guest.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return guest;
    }
}
