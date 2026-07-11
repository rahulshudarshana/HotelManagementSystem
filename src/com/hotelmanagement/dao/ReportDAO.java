package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Logger;

public class ReportDAO {

    private static final Logger LOGGER = Logger.getLogger(ReportDAO.class.getName());

    private int queryInt(String sql) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private int queryCountByStatus(String table, String column, String status) throws SQLException {
        return queryInt("SELECT COUNT(*) FROM " + table + " WHERE " + column + " = '" + status + "'");
    }

    private BigDecimal queryBigDecimal(String sql) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        }
        return BigDecimal.ZERO;
    }

    public int getTotalRooms() throws SQLException {
        return queryInt("SELECT COUNT(*) FROM Rooms");
    }

    public int getAvailableRoomsCount() throws SQLException {
        return queryCountByStatus("Rooms", "Status", "Available");
    }

    public int getOccupiedRoomsCount() throws SQLException {
        return queryCountByStatus("Rooms", "Status", "Occupied");
    }

    public int getReservedRoomsCount() throws SQLException {
        return queryCountByStatus("Rooms", "Status", "Reserved");
    }

    public int getMaintenanceRoomsCount() throws SQLException {
        return queryCountByStatus("Rooms", "Status", "Maintenance");
    }

    public int getCleaningRoomsCount() throws SQLException {
        return queryCountByStatus("Rooms", "Status", "Cleaning");
    }

    public int getTotalGuests() throws SQLException {
        return queryInt("SELECT COUNT(*) FROM Guests");
    }

    public int getTotalEmployees() throws SQLException {
        return queryInt("SELECT COUNT(*) FROM Employees");
    }

    public int getTotalReservations() throws SQLException {
        return queryInt("SELECT COUNT(*) FROM Reservations");
    }

    public int getActiveReservationsCount() throws SQLException {
        return queryInt("SELECT COUNT(*) FROM Reservations WHERE Status IN ('Pending', 'Confirmed', 'CheckedIn')");
    }

    public int getCheckInsToday() throws SQLException {
        return queryInt("SELECT COUNT(*) FROM CheckIns WHERE DATE(ActualCheckInDate) = CURDATE()");
    }

    public int getCheckOutsToday() throws SQLException {
        return queryInt("SELECT COUNT(*) FROM CheckOuts WHERE DATE(ActualCheckOutDate) = CURDATE()");
    }

    public BigDecimal getTotalRevenueToday() throws SQLException {
        return queryBigDecimal("SELECT COALESCE(SUM(TotalAmount), 0) FROM CheckOuts WHERE DATE(ActualCheckOutDate) = CURDATE()");
    }

    public BigDecimal getTotalRevenue(LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT COALESCE(SUM(TotalAmount), 0) FROM CheckOuts WHERE DATE(ActualCheckOutDate) BETWEEN ? AND ?";
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
        String sql = "SELECT COALESCE(SUM(Amount), 0) FROM Payments WHERE DATE(PaymentDate) BETWEEN ? AND ?";
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
        return queryCountByStatus("HousekeepingTasks", "Status", "Pending");
    }

    public int getInProgressHousekeepingTasks() throws SQLException {
        return queryCountByStatus("HousekeepingTasks", "Status", "InProgress");
    }

    public BigDecimal getOccupancyRate() throws SQLException {
        int total = getTotalRooms();
        if (total == 0) return BigDecimal.ZERO;
        int occupied = getOccupiedRoomsCount() + getReservedRoomsCount();
        return BigDecimal.valueOf(occupied * 100.0 / total);
    }
}
