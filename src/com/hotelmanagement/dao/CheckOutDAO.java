package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.CheckOut;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CheckOutDAO {

    public CheckOut getCheckOutById(int checkOutID) throws SQLException {
        String sql = "SELECT * FROM CheckOuts WHERE CheckOutID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, checkOutID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCheckOut(rs);
                }
            }
        }
        return null;
    }

    public CheckOut getCheckOutByReservation(int reservationID) throws SQLException {
        String sql = "SELECT * FROM CheckOuts WHERE ReservationID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCheckOut(rs);
                }
            }
        }
        return null;
    }

    public List<CheckOut> getAllCheckOuts() throws SQLException {
        List<CheckOut> checkOuts = new ArrayList<>();
        String sql = "SELECT * FROM CheckOuts ORDER BY ActualCheckOutDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                checkOuts.add(mapCheckOut(rs));
            }
        }
        return checkOuts;
    }

    public int insertCheckOut(CheckOut checkOut) throws SQLException {
        String sql = "INSERT INTO CheckOuts (ReservationID, CheckInID, GuestID, RoomID, RoomCharges, AdditionalCharges, TotalAmount, Notes, ProcessedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, checkOut.getReservationID());
            stmt.setInt(2, checkOut.getCheckInID());
            stmt.setInt(3, checkOut.getGuestID());
            stmt.setInt(4, checkOut.getRoomID());
            stmt.setBigDecimal(5, checkOut.getRoomCharges());
            stmt.setBigDecimal(6, checkOut.getAdditionalCharges());
            stmt.setBigDecimal(7, checkOut.getTotalAmount());
            stmt.setString(8, checkOut.getNotes());
            stmt.setObject(9, checkOut.getProcessedBy());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void deleteCheckOut(int checkOutID) throws SQLException {
        String sql = "DELETE FROM CheckOuts WHERE CheckOutID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, checkOutID);
            stmt.executeUpdate();
        }
    }

    private CheckOut mapCheckOut(ResultSet rs) throws SQLException {
        CheckOut checkOut = new CheckOut();
        checkOut.setCheckOutID(rs.getInt("CheckOutID"));
        checkOut.setReservationID(rs.getInt("ReservationID"));
        checkOut.setCheckInID(rs.getInt("CheckInID"));
        checkOut.setGuestID(rs.getInt("GuestID"));
        checkOut.setRoomID(rs.getInt("RoomID"));
        if (rs.getTimestamp("ActualCheckOutDate") != null) {
            checkOut.setActualCheckOutDate(rs.getTimestamp("ActualCheckOutDate").toLocalDateTime());
        }
        checkOut.setRoomCharges(rs.getBigDecimal("RoomCharges"));
        checkOut.setAdditionalCharges(rs.getBigDecimal("AdditionalCharges"));
        checkOut.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        checkOut.setNotes(rs.getString("Notes"));
        checkOut.setProcessedBy(rs.getObject("ProcessedBy", Integer.class));
        if (rs.getTimestamp("CreatedAt") != null) {
            checkOut.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return checkOut;
    }
}
