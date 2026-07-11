package com.hotelmanagement.controller;

import com.hotelmanagement.service.ReportService;
import com.hotelmanagement.util.SessionManager;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.dashboard.DashboardPanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController {
    private final DashboardPanel view;
    private final MainFrame mainFrame;
    private final ReportService reportService;

    public DashboardController(DashboardPanel view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
        this.reportService = new ReportService();
        initControllers();
        loadDashboardStats();
    }

    private void initControllers() {
        view.getBtnGuest().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_GUEST));
        view.getBtnRoom().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_ROOM));
        view.getBtnReservation().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_RESERVATION));
        view.getBtnCheckIn().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_CHECKIN));
        view.getBtnCheckOut().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_CHECKOUT));
        view.getBtnBilling().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_BILLING));
        view.getBtnEmployee().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_EMPLOYEE));
        view.getBtnReports().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_REPORT));
        view.getBtnUser().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_USER));
        view.getBtnHousekeeping().addActionListener(e -> mainFrame.navigateTo(MainFrame.PANEL_HOUSEKEEPING));
        view.getBtnLogout().addActionListener(e -> logout());
    }

    private void logout() {
        SessionManager.getInstance().logout();
        mainFrame.dispose();
        new com.hotelmanagement.view.login.LoginView().setVisible(true);
    }

    public void loadDashboardStats() {
        try {
            view.getLabelTotalRooms().setText(String.valueOf(reportService.getTotalRooms()));
            view.getLabelAvailable().setText(String.valueOf(reportService.getAvailableRooms()));
            view.getLabelOccupied().setText(String.valueOf(reportService.getOccupiedRooms()));
            view.getLabelCheckIns().setText(String.valueOf(reportService.getCheckInsToday()));
            view.getLabelCheckOuts().setText(String.valueOf(reportService.getCheckOutsToday()));
            BigDecimal revenue = reportService.getRevenueToday();
            view.getLabelRevenue().setText("$" + (revenue != null ? revenue.setScale(2, java.math.RoundingMode.HALF_UP).toString() : "0.00"));
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Failed to load dashboard stats", ex);
            view.getLabelTotalRooms().setText("Error");
            view.getLabelAvailable().setText("Error");
            view.getLabelOccupied().setText("Error");
            view.getLabelCheckIns().setText("Error");
            view.getLabelCheckOuts().setText("Error");
            view.getLabelRevenue().setText("$Error");
        }
    }
}
