package com.hotelmanagement.controller;

import com.hotelmanagement.service.ReportService;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.report.reportpanel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportController {
    private final reportpanel view;
    private final ReportService service;

    public ReportController(reportpanel view) {
        this.view = view;
        this.service = new ReportService();
        initControllers();
    }

    private void initControllers() {
        view.getBtnGenerate().addActionListener(e -> generateReport());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
    }

    private void generateReport() {
        try {
            String reportType = (String) view.getCmbReportType().getSelectedItem();
            LocalDate from = view.getDpFromDate().getDate() != null
                ? view.getDpFromDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : LocalDate.now().minusDays(30);
            LocalDate to = view.getDpToDate().getDate() != null
                ? view.getDpToDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : LocalDate.now();

            var model = new javax.swing.table.DefaultTableModel();

            switch (reportType) {
                case "Revenue" -> {
                    model.addColumn("Metric");
                    model.addColumn("Value");
                    model.addRow(new Object[]{"Total Revenue", service.getRevenue(from, to)});
                    model.addRow(new Object[]{"Total Payments", service.getPayments(from, to)});
                    model.addRow(new Object[]{"Revenue Today", service.getRevenueToday()});
                }
                case "Occupancy" -> {
                    model.addColumn("Metric");
                    model.addColumn("Value");
                    model.addRow(new Object[]{"Total Rooms", service.getTotalRooms()});
                    model.addRow(new Object[]{"Available", service.getAvailableRooms()});
                    model.addRow(new Object[]{"Occupied", service.getOccupiedRooms()});
                    model.addRow(new Object[]{"Occupancy Rate", service.getOccupancyRate() + "%"});
                }
                case "Housekeeping" -> {
                    model.addColumn("Metric");
                    model.addColumn("Value");
                    model.addRow(new Object[]{"Pending Tasks", service.getPendingTasks()});
                    model.addRow(new Object[]{"In Progress", service.getInProgressTasks()});
                }
                default -> {
                    model.addColumn("Metric");
                    model.addColumn("Value");
                    model.addRow(new Object[]{"Total Guests", service.getTotalGuests()});
                    model.addRow(new Object[]{"Total Employees", service.getTotalEmployees()});
                    model.addRow(new Object[]{"Total Reservations", service.getTotalReservations()});
                    model.addRow(new Object[]{"Active Reservations", service.getActiveReservations()});
                }
            }
            view.getTable().setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(view, "Failed to generate report.");
        }
    }

    private void clearForm() {
        view.getTxtKeyword().setText("");
        view.getTable().setModel(new javax.swing.table.DefaultTableModel());
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
