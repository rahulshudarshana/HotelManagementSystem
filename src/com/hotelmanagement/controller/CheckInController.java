package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.CheckIn;
import com.hotelmanagement.service.CheckInService;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.checkin.checkinpanel;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class CheckInController {
    private final checkinpanel view;
    private final CheckInService service;

    public CheckInController(checkinpanel view) {
        this.view = view;
        this.service = new CheckInService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnCheckIn().addActionListener(e -> checkIn());
        view.getBtnSearch().addActionListener(e -> search());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
    }

    private void checkIn() {
        try {
            String resText = view.getTxtReservationID().getText().trim();
            String guestText = view.getTxtGuest().getText().trim();
            String roomText = view.getTxtRoomNo().getText().trim();
            String guestsText = view.getTxtNoOfGuests().getText().trim();
            if (resText.isEmpty() || guestText.isEmpty() || roomText.isEmpty() || guestsText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Reservation ID, Guest ID, Room No, and Number of Guests are required.");
                return;
            }
            CheckIn checkIn = new CheckIn();
            checkIn.setReservationID(parseIntOrThrow("Reservation ID", resText));
            checkIn.setGuestID(parseIntOrThrow("Guest ID", guestText));
            checkIn.setRoomID(parseIntOrThrow("Room No", roomText));
            checkIn.setNumberOfGuests(parseIntOrThrow("Number of Guests", guestsText));
            String receptionistText = view.getTxtReceptionist().getText().trim();
            if (!receptionistText.isEmpty()) {
                checkIn.setReceptionistID(parseIntOrThrow("Receptionist ID", receptionistText));
            }
            service.performCheckIn(checkIn);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Check-in successful.");
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, "Database error during check-in", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void search() {
        try {
            String keyword = view.getTxtReservationID().getText().trim();
            populateTable(service.searchCheckIns(keyword));
        } catch (SQLException ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, "Failed to search check-ins", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllCheckIns());
        } catch (SQLException ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, "Failed to load check-ins", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<CheckIn> checkIns) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Reservation", "Guest", "Room", "Date", "Guests"}, 0);
        for (CheckIn c : checkIns) {
            model.addRow(new Object[]{c.getCheckInID(), c.getReservationID(), c.getGuestID(), c.getRoomID(),
                c.getActualCheckInDate() != null ? c.getActualCheckInDate().toString() : "", c.getNumberOfGuests()});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtReservationID().setText("");
        view.getTxtGuest().setText("");
        view.getTxtRoomNo().setText("");
        view.getTxtNoOfGuests().setText("");
        view.getTxtReceptionist().setText("");
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }

    private int parseIntOrThrow(String fieldName, String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " must be a numeric value.");
        }
    }
}
