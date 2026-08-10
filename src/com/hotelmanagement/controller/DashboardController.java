package com.hotelmanagement.controller;

import com.hotelmanagement.service.ReportService;
import com.hotelmanagement.view.dashboard.DashboardPanel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController {
    private static final int REFRESH_INTERVAL_MS = 60_000;

    private final DashboardPanel view;
    private final ReportService reportService;
    private final javax.swing.Timer refreshTimer;

    public DashboardController(DashboardPanel view) {
        this.view = view;
        this.reportService = new ReportService();
        initControllers();
        loadDashboardStats();
        refreshTimer = new javax.swing.Timer(REFRESH_INTERVAL_MS, e -> loadDashboardStats());
        refreshTimer.start();
    }

    private void initControllers() {
        view.getBtnRefresh().addActionListener(e -> loadDashboardStats());
    }

    public void loadDashboardStats() {
        try {
            view.getLabelTotalRooms().setText(String.valueOf(reportService.getTotalRooms()));
            view.getLabelAvailable().setText(String.valueOf(reportService.getAvailableRooms()));
            view.getLabelOccupied().setText(String.valueOf(reportService.getOccupiedRooms()));
            view.getLabelCheckIns().setText(String.valueOf(reportService.getCheckInsToday()));
            view.getLabelCheckOuts().setText(String.valueOf(reportService.getCheckOutsToday()));
            BigDecimal revenue = reportService.getRevenueToday();
            view.getLabelRevenue().setText("$" + (revenue != null
                    ? revenue.setScale(2, RoundingMode.HALF_UP).toString() : "0.00"));
            view.getLabelLastUpdated().setText("Last updated: "
                    + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Failed to load dashboard stats", ex);
            view.getLabelTotalRooms().setText("Error");
            view.getLabelAvailable().setText("Error");
            view.getLabelOccupied().setText("Error");
            view.getLabelCheckIns().setText("Error");
            view.getLabelCheckOuts().setText("Error");
            view.getLabelRevenue().setText("$Error");
            view.getLabelLastUpdated().setText("Last updated: error");
        }
    }
}
