package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReportDAO {

    public int getTotalRooms() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Rooms";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getAvailableRoomsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE Status = 'Available'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getOccupiedRoomsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE Status = 'Occupied'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getReservedRoomsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE Status = 'Reserved'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getMaintenanceRoomsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE Status = 'Maintenance'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getCleaningRoomsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE Status = 'Cleaning'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getTotalGuests() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Guests";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getTotalEmployees() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Employees";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getTotalReservations() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservations";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getActiveReservationsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservations WHERE Status IN ('Pending', 'Confirmed', 'CheckedIn')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getCheckInsToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM CheckIns WHERE CAST(ActualCheckInDate AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getCheckOutsToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM CheckOuts WHERE CAST(ActualCheckOutDate AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public BigDecimal getTotalRevenueToday() throws SQLException {
        String sql = "SELECT COALESCE(SUM(TotalAmount), 0) FROM CheckOuts WHERE CAST(ActualCheckOutDate AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalRevenue(LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT COALESCE(SUM(TotalAmount), 0) FROM CheckOuts WHERE CAST(ActualCheckOutDate AS DATE) BETWEEN ? AND ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, from);
            stmt.setObject(2, to);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalPayments(LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT COALESCE(SUM(Amount), 0) FROM Payments WHERE CAST(PaymentDate AS DATE) BETWEEN ? AND ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, from);
            stmt.setObject(2, to);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    public int getPendingHousekeepingTasks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM HousekeepingTasks WHERE Status = 'Pending'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getInProgressHousekeepingTasks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM HousekeepingTasks WHERE Status = 'InProgress'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public BigDecimal getOccupancyRate() throws SQLException {
        int total = getTotalRooms();
        if (total == 0) return BigDecimal.ZERO;
        int occupied = getOccupiedRoomsCount() + getReservedRoomsCount();
        return BigDecimal.valueOf(occupied * 100.0 / total);
    }
}
