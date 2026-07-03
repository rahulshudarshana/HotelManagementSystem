package com.hotelmanagement.controller;

import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.enums.ReservationStatus;
import com.hotelmanagement.service.ReservationService;
import com.hotelmanagement.view.reservation.ReservationManagementPanel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class ReservationController {
    private final ReservationManagementPanel view;
    private final ReservationService service;

    public ReservationController(ReservationManagementPanel view) {
        this.view = view;
        this.service = new ReservationService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnBook().addActionListener(e -> addReservation());
        view.getBtnUpdate().addActionListener(e -> updateReservation());
        view.getBtnCancel().addActionListener(e -> cancelReservation());
        view.getBtnSearch().addActionListener(e -> searchReservations());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
    }

    private void addReservation() {
        try {
            Reservation r = new Reservation();
            r.setGuestID(Integer.parseInt(view.getCmbGuest().getSelectedItem().toString().split("-")[0].trim()));
            r.setRoomID(Integer.parseInt(view.getCmbRoom().getSelectedItem().toString().split("-")[0].trim()));
            r.setNumberOfGuests(Integer.parseInt(view.getTxtNumberOfGuests().getText().trim()));
            r.setSpecialRequests(view.getTxtSpecialRequests().getText().trim());
            service.createReservation(r);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Reservation created successfully.");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void updateReservation() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int id = (int) view.getTable().getValueAt(selectedRow, 0);
            Reservation r = service.getReservationById(id);
            if (r != null) {
                r.setNumberOfGuests(Integer.parseInt(view.getTxtNumberOfGuests().getText().trim()));
                r.setSpecialRequests(view.getTxtSpecialRequests().getText().trim());
                r.setStatus(ReservationStatus.valueOf((String) view.getCmbStatus().getSelectedItem()));
                service.updateReservation(r);
                loadTable();
                clearForm();
                javax.swing.JOptionPane.showMessageDialog(view, "Reservation updated.");
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void cancelReservation() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int id = (int) view.getTable().getValueAt(selectedRow, 0);
            if (javax.swing.JOptionPane.showConfirmDialog(view, "Cancel this reservation?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
                service.updateReservationStatus(id, ReservationStatus.Cancelled);
                loadTable();
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void searchReservations() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            List<Reservation> all = service.getAllReservations();
            List<Reservation> filtered = all.stream().filter(r -> String.valueOf(r.getReservationID()).contains(keyword)).toList();
            populateTable(filtered);
        } catch (SQLException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllReservations());
        } catch (SQLException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateTable(List<Reservation> reservations) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Guest", "Room", "Check-In", "Check-Out", "Guests", "Status"}, 0);
        for (Reservation r : reservations) {
            model.addRow(new Object[]{r.getReservationID(), r.getGuestID(), r.getRoomID(),
                r.getCheckInDate() != null ? r.getCheckInDate().format(dtf) : "",
                r.getCheckOutDate() != null ? r.getCheckOutDate().format(dtf) : "",
                r.getNumberOfGuests(), r.getStatus().name()});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtNumberOfGuests().setText("");
        view.getTxtSpecialRequests().setText("");
        view.getTxtSearch().setText("");
    }

    private void navigateBack() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
