package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.CheckOut;
import com.hotelmanagement.service.CheckOutService;
import com.hotelmanagement.view.checkout.checkoutpanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
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
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, "Database error during check-out", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
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
            List<CheckOut> results = service.searchCheckOuts(keyword);
            var model = new javax.swing.table.DefaultTableModel(new String[]{"ID", "Reservation", "Guest", "Room", "Total"}, 0);
            for (CheckOut c : results) {
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
