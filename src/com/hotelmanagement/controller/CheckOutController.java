package com.hotelmanagement.controller;

import com.hotelmanagement.service.CheckOutService;
import com.hotelmanagement.view.checkout.checkoutpanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckOutController {
    private final checkoutpanel view;
    private final CheckOutService service;

    public CheckOutController(checkoutpanel view) {
        this.view = view;
        this.service = new CheckOutService();
        initControllers();
    }

    private void initControllers() {
        view.getBtnCheckOut().addActionListener(e -> checkOut());
        view.getBtnCalculateBill().addActionListener(e -> calculateBill());
        view.getBtnSearch().addActionListener(e -> search());
        view.getBtnClear().addActionListener(e -> clearForm());
    }

    private void checkOut() {
        try {
            int reservationID = Integer.parseInt(view.getTxtSearch().getText().trim());
            BigDecimal additionalCharges = view.getTxtAdditionalCharges().getText().isEmpty()
                ? BigDecimal.ZERO : new BigDecimal(view.getTxtAdditionalCharges().getText().trim());
            String notes = null;
            service.performCheckOut(reservationID, additionalCharges, notes, null);
            javax.swing.JOptionPane.showMessageDialog(view, "Check-out successful.");
            clearForm();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void calculateBill() {
        try {
            String roomChargesText = view.getTxtRoomCharges().getText().trim();
            String additionalText = view.getTxtAdditionalCharges().getText().trim();
            BigDecimal roomCharges = roomChargesText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(roomChargesText);
            BigDecimal additional = additionalText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(additionalText);
            BigDecimal total = roomCharges.add(additional);
            view.getTxtTotalAmount().setText(total.toString());
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Invalid amount entered.");
        }
    }

    private void search() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            var all = service.getAllCheckOuts();
            var filtered = all.stream()
                .filter(c -> String.valueOf(c.getReservationID()).contains(keyword))
                .toList();
            var model = new javax.swing.table.DefaultTableModel(new String[]{"ID", "Reservation", "Guest", "Room", "Total"}, 0);
            for (var c : filtered) {
                model.addRow(new Object[]{c.getCheckOutID(), c.getReservationID(), c.getGuestID(), c.getRoomID(), c.getTotalAmount()});
            }
            view.getTable().setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearForm() {
        view.getTxtSearch().setText("");
        view.getTxtGuestName().setText("");
        view.getTxtRoomCharges().setText("");
        view.getTxtAdditionalCharges().setText("");
        view.getTxtTotalAmount().setText("");
    }
}
