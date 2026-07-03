package com.hotelmanagement.service;

import com.hotelmanagement.dao.ReportDAO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportService {
    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    public int getTotalRooms() throws SQLException { return reportDAO.getTotalRooms(); }
    public int getAvailableRooms() throws SQLException { return reportDAO.getAvailableRoomsCount(); }
    public int getOccupiedRooms() throws SQLException { return reportDAO.getOccupiedRoomsCount(); }
    public int getReservedRooms() throws SQLException { return reportDAO.getReservedRoomsCount(); }
    public int getMaintenanceRooms() throws SQLException { return reportDAO.getMaintenanceRoomsCount(); }
    public int getCleaningRooms() throws SQLException { return reportDAO.getCleaningRoomsCount(); }
    public int getTotalGuests() throws SQLException { return reportDAO.getTotalGuests(); }
    public int getTotalEmployees() throws SQLException { return reportDAO.getTotalEmployees(); }
    public int getTotalReservations() throws SQLException { return reportDAO.getTotalReservations(); }
    public int getActiveReservations() throws SQLException { return reportDAO.getActiveReservationsCount(); }
    public int getCheckInsToday() throws SQLException { return reportDAO.getCheckInsToday(); }
    public int getCheckOutsToday() throws SQLException { return reportDAO.getCheckOutsToday(); }
    public BigDecimal getRevenueToday() throws SQLException { return reportDAO.getTotalRevenueToday(); }
    public BigDecimal getRevenue(LocalDate from, LocalDate to) throws SQLException { return reportDAO.getTotalRevenue(from, to); }
    public BigDecimal getPayments(LocalDate from, LocalDate to) throws SQLException { return reportDAO.getTotalPayments(from, to); }
    public int getPendingTasks() throws SQLException { return reportDAO.getPendingHousekeepingTasks(); }
    public int getInProgressTasks() throws SQLException { return reportDAO.getInProgressHousekeepingTasks(); }
    public BigDecimal getOccupancyRate() throws SQLException { return reportDAO.getOccupancyRate(); }

    public Map<String, Integer> getRoomStatusSummary() throws SQLException {
        Map<String, Integer> summary = new LinkedHashMap<>();
        summary.put("Available", getAvailableRooms());
        summary.put("Occupied", getOccupiedRooms());
        summary.put("Reserved", getReservedRooms());
        summary.put("Maintenance", getMaintenanceRooms());
        summary.put("Cleaning", getCleaningRooms());
        return summary;
    }

    public Map<String, Integer> getDashboardStats() throws SQLException {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("Total Rooms", getTotalRooms());
        stats.put("Available Rooms", getAvailableRooms());
        stats.put("Occupied Rooms", getOccupiedRooms());
        stats.put("Active Reservations", getActiveReservations());
        stats.put("Total Guests", getTotalGuests());
        stats.put("Check-ins Today", getCheckInsToday());
        stats.put("Check-outs Today", getCheckOutsToday());
        stats.put("Pending Tasks", getPendingTasks());
        return stats;
    }
}
