package com.hotelmanagement.controller;

import com.hotelmanagement.model.CheckIn;
import com.hotelmanagement.service.CheckInService;
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
            CheckIn checkIn = new CheckIn();
            checkIn.setReservationID(Integer.parseInt(view.getTxtReservationID().getText().trim()));
            checkIn.setGuestID(Integer.parseInt(view.getTxtGuest().getText().trim()));
            checkIn.setRoomID(Integer.parseInt(view.getTxtRoomNo().getText().trim()));
            checkIn.setNumberOfGuests(Integer.parseInt(view.getTxtNoOfGuests().getText().trim()));
            String receptionistText = view.getTxtReceptionist().getText().trim();
            if (!receptionistText.isEmpty()) {
                checkIn.setReceptionistID(Integer.parseInt(receptionistText));
            }
            service.performCheckIn(checkIn);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Check-in successful.");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void search() {
        try {
            String keyword = view.getTxtReservationID().getText().trim();
            List<CheckIn> all = service.getAllCheckIns();
            List<CheckIn> filtered = all.stream()
                .filter(c -> String.valueOf(c.getReservationID()).contains(keyword))
                .toList();
            populateTable(filtered);
        } catch (SQLException ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllCheckIns());
        } catch (SQLException ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, null, ex);
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
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
