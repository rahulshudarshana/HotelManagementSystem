package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.enums.ReservationStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public Reservation getReservationById(int reservationID) throws SQLException {
        String sql = "SELECT * FROM Reservations WHERE ReservationID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapReservation(rs);
                }
            }
        }
        return null;
    }

    public List<Reservation> getReservationsByGuest(int guestID) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE GuestID = ? ORDER BY CheckInDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapReservation(rs));
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByRoom(int roomID) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE RoomID = ? ORDER BY CheckInDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapReservation(rs));
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE Status = ? ORDER BY CheckInDate";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapReservation(rs));
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByDateRange(LocalDate from, LocalDate to) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE CheckInDate <= ? AND CheckOutDate >= ? ORDER BY CheckInDate";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, to);
            stmt.setObject(2, from);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapReservation(rs));
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations ORDER BY CheckInDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
        }
        return reservations;
    }

    public boolean isRoomAvailable(int roomID, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservations WHERE RoomID = ? AND Status NOT IN ('Cancelled', 'CheckedOut') AND CheckInDate < ? AND CheckOutDate > ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomID);
            stmt.setObject(2, checkOut);
            stmt.setObject(3, checkIn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }

    public int insertReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservations (GuestID, RoomID, CheckInDate, CheckOutDate, NumberOfGuests, Status, SpecialRequests, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getGuestID());
            stmt.setInt(2, reservation.getRoomID());
            stmt.setObject(3, reservation.getCheckInDate());
            stmt.setObject(4, reservation.getCheckOutDate());
            stmt.setInt(5, reservation.getNumberOfGuests());
            stmt.setString(6, reservation.getStatus().name());
            stmt.setString(7, reservation.getSpecialRequests());
            stmt.setObject(8, reservation.getCreatedBy());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservations SET GuestID = ?, RoomID = ?, CheckInDate = ?, CheckOutDate = ?, NumberOfGuests = ?, Status = ?, SpecialRequests = ?, UpdatedAt = GETDATE() WHERE ReservationID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getGuestID());
            stmt.setInt(2, reservation.getRoomID());
            stmt.setObject(3, reservation.getCheckInDate());
            stmt.setObject(4, reservation.getCheckOutDate());
            stmt.setInt(5, reservation.getNumberOfGuests());
            stmt.setString(6, reservation.getStatus().name());
            stmt.setString(7, reservation.getSpecialRequests());
            stmt.setInt(8, reservation.getReservationID());
            stmt.executeUpdate();
        }
    }

    public void updateReservationStatus(int reservationID, ReservationStatus status) throws SQLException {
        String sql = "UPDATE Reservations SET Status = ?, UpdatedAt = GETDATE() WHERE ReservationID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, reservationID);
            stmt.executeUpdate();
        }
    }

    public void deleteReservation(int reservationID) throws SQLException {
        String sql = "DELETE FROM Reservations WHERE ReservationID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationID);
            stmt.executeUpdate();
        }
    }

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setReservationID(rs.getInt("ReservationID"));
        r.setGuestID(rs.getInt("GuestID"));
        r.setRoomID(rs.getInt("RoomID"));
        r.setCheckInDate(rs.getDate("CheckInDate").toLocalDate());
        r.setCheckOutDate(rs.getDate("CheckOutDate").toLocalDate());
        r.setNumberOfGuests(rs.getInt("NumberOfGuests"));
        r.setStatus(ReservationStatus.valueOf(rs.getString("Status")));
        r.setSpecialRequests(rs.getString("SpecialRequests"));
        r.setCreatedBy(rs.getObject("CreatedBy", Integer.class));
        if (rs.getTimestamp("CreatedAt") != null) {
            r.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        if (rs.getTimestamp("UpdatedAt") != null) {
            r.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        }
        return r;
    }
}
