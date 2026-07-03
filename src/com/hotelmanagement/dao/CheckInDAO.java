package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.CheckIn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CheckInDAO {

    public CheckIn getCheckInById(int checkInID) throws SQLException {
        String sql = "SELECT * FROM CheckIns WHERE CheckInID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, checkInID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCheckIn(rs);
                }
            }
        }
        return null;
    }

    public CheckIn getCheckInByReservation(int reservationID) throws SQLException {
        String sql = "SELECT * FROM CheckIns WHERE ReservationID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCheckIn(rs);
                }
            }
        }
        return null;
    }

    public List<CheckIn> getAllCheckIns() throws SQLException {
        List<CheckIn> checkIns = new ArrayList<>();
        String sql = "SELECT * FROM CheckIns ORDER BY ActualCheckInDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                checkIns.add(mapCheckIn(rs));
            }
        }
        return checkIns;
    }

    public int insertCheckIn(CheckIn checkIn) throws SQLException {
        String sql = "INSERT INTO CheckIns (ReservationID, GuestID, RoomID, NumberOfGuests, ReceptionistID, Notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, checkIn.getReservationID());
            stmt.setInt(2, checkIn.getGuestID());
            stmt.setInt(3, checkIn.getRoomID());
            stmt.setInt(4, checkIn.getNumberOfGuests());
            stmt.setObject(5, checkIn.getReceptionistID());
            stmt.setString(6, checkIn.getNotes());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateCheckIn(CheckIn checkIn) throws SQLException {
        String sql = "UPDATE CheckIns SET ReservationID = ?, GuestID = ?, RoomID = ?, NumberOfGuests = ?, Notes = ? WHERE CheckInID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, checkIn.getReservationID());
            stmt.setInt(2, checkIn.getGuestID());
            stmt.setInt(3, checkIn.getRoomID());
            stmt.setInt(4, checkIn.getNumberOfGuests());
            stmt.setString(5, checkIn.getNotes());
            stmt.setInt(6, checkIn.getCheckInID());
            stmt.executeUpdate();
        }
    }

    public void deleteCheckIn(int checkInID) throws SQLException {
        String sql = "DELETE FROM CheckIns WHERE CheckInID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, checkInID);
            stmt.executeUpdate();
        }
    }

    private CheckIn mapCheckIn(ResultSet rs) throws SQLException {
        CheckIn checkIn = new CheckIn();
        checkIn.setCheckInID(rs.getInt("CheckInID"));
        checkIn.setReservationID(rs.getInt("ReservationID"));
        checkIn.setGuestID(rs.getInt("GuestID"));
        checkIn.setRoomID(rs.getInt("RoomID"));
        if (rs.getTimestamp("ActualCheckInDate") != null) {
            checkIn.setActualCheckInDate(rs.getTimestamp("ActualCheckInDate").toLocalDateTime());
        }
        checkIn.setNumberOfGuests(rs.getInt("NumberOfGuests"));
        checkIn.setReceptionistID(rs.getObject("ReceptionistID", Integer.class));
        checkIn.setNotes(rs.getString("Notes"));
        if (rs.getTimestamp("CreatedAt") != null) {
            checkIn.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return checkIn;
    }
}
