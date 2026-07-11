package com.hotelmanagement.controller;

import com.hotelmanagement.dao.CheckInDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.CheckIn;
import com.hotelmanagement.model.CheckOut;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.service.CheckOutService;
import com.hotelmanagement.service.GuestService;
import com.hotelmanagement.service.ReservationService;
import com.hotelmanagement.service.RoomService;
import com.hotelmanagement.util.SessionManager;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.checkout.checkoutpanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckOutController {
    private final checkoutpanel view;
    private final CheckOutService service;
    private final ReservationService reservationService;
    private final GuestService guestService;
    private final RoomService roomService;
    private final CheckInDAO checkInDAO;

    public CheckOutController(checkoutpanel view) {
        this.view = view;
        this.service = new CheckOutService();
        this.reservationService = new ReservationService();
        this.guestService = new GuestService();
        this.roomService = new RoomService();
        this.checkInDAO = new CheckInDAO();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnCheckOut().addActionListener(e -> checkOut());
        view.getBtnCalculateBill().addActionListener(e -> calculateBill());
        view.getBtnUpdate().addActionListener(e -> updateBill());
        view.getBtnSearch().addActionListener(e -> search());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
    }

    private void checkOut() {
        try {
            String searchText = view.getTxtSearch().getText().trim();
            if (searchText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please enter a Reservation ID.");
                return;
            }
            int reservationID = Integer.parseInt(searchText);
            BigDecimal additionalCharges = view.getTxtAdditionalCharges().getText().isEmpty()
                ? BigDecimal.ZERO : new BigDecimal(view.getTxtAdditionalCharges().getText().trim());
            String notes = null;
            Integer processedBy = SessionManager.getInstance().getCurrentUserId();
            service.performCheckOut(reservationID, additionalCharges, notes, processedBy);
            search();
            javax.swing.JOptionPane.showMessageDialog(view, "Check-out successful.");
            clearForm();
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Reservation ID must be a numeric value.");
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, "Database error during check-out", ex);
            String detail = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
            javax.swing.JOptionPane.showMessageDialog(view, "Database error: " + detail);
        } catch (Exception ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void calculateBill() {
        try {
            String searchText = view.getTxtSearch().getText().trim();
            if (searchText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please enter a Reservation ID first.");
                return;
            }
            int reservationID = Integer.parseInt(searchText);

            Reservation reservation = reservationService.getReservationById(reservationID);
            if (reservation == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Reservation ID not found.");
                return;
            }

            Room room = roomService.getRoomById(reservation.getRoomID());
            CheckIn checkIn = checkInDAO.getCheckInByReservation(reservationID);
            long nights = 1;
            if (checkIn != null && checkIn.getActualCheckInDate() != null) {
                nights = ChronoUnit.DAYS.between(
                    checkIn.getActualCheckInDate().toLocalDate(), LocalDate.now());
                if (nights <= 0) nights = 1;
            }

            BigDecimal roomCharges = room != null
                ? room.getPricePerNight().multiply(BigDecimal.valueOf(nights))
                : BigDecimal.ZERO;
            String additionalText = view.getTxtAdditionalCharges().getText().trim();
            BigDecimal additional = additionalText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(additionalText);
            BigDecimal total = roomCharges.add(additional);

            view.getTxtRoomCharges().setText(roomCharges.setScale(2, java.math.RoundingMode.HALF_UP).toString());
            view.getTxtTotalAmount().setText(total.setScale(2, java.math.RoundingMode.HALF_UP).toString());
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Invalid amount entered.");
        } catch (SQLException ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, "Failed to calculate bill", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void updateBill() {
        try {
            String roomChargesText = view.getTxtRoomCharges().getText().trim();
            String additionalText = view.getTxtAdditionalCharges().getText().trim();
            if (roomChargesText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please calculate the bill first.");
                return;
            }
            BigDecimal roomCharges = new BigDecimal(roomChargesText);
            BigDecimal additional = additionalText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(additionalText);
            BigDecimal total = roomCharges.add(additional);
            view.getTxtTotalAmount().setText(total.setScale(2, java.math.RoundingMode.HALF_UP).toString());
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Invalid amount entered.");
        }
    }

    private void search() {
        try {
            String searchText = view.getTxtSearch().getText().trim();
            if (searchText.isEmpty()) return;

            int reservationID = Integer.parseInt(searchText);

            Reservation reservation = reservationService.getReservationById(reservationID);
            if (reservation == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Reservation ID not found.");
                return;
            }

            Guest guest = guestService.getGuestById(reservation.getGuestID());
            if (guest != null) {
                view.getTxtGuestName().setText(guest.getFirstName() + " " + guest.getLastName());
            }

            Room room = roomService.getRoomById(reservation.getRoomID());
            if (room != null) {
                view.getTxtRoomNo().setText(room.getRoomNumber());
            }

            CheckIn checkIn = checkInDAO.getCheckInByReservation(reservationID);
            long nights = 1;
            if (checkIn != null && checkIn.getActualCheckInDate() != null) {
                nights = ChronoUnit.DAYS.between(
                    checkIn.getActualCheckInDate().toLocalDate(), LocalDate.now());
                if (nights <= 0) nights = 1;
            }

            BigDecimal roomCharges = room != null
                ? room.getPricePerNight().multiply(BigDecimal.valueOf(nights))
                : BigDecimal.ZERO;
            view.getTxtRoomCharges().setText(roomCharges.setScale(2, java.math.RoundingMode.HALF_UP).toString());
            view.getTxtTotalAmount().setText(roomCharges.setScale(2, java.math.RoundingMode.HALF_UP).toString());

            List<CheckOut> results = service.searchCheckOuts(searchText);
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(new String[]{"ID", "Reservation", "Guest", "Room", "Total"}, 0);
            for (CheckOut c : results) {
                model.addRow(new Object[]{c.getCheckOutID(), c.getReservationID(), c.getGuestID(), c.getRoomID(), c.getTotalAmount()});
            }
            view.getTable().setModel(model);
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Reservation ID must be a numeric value.");
        } catch (SQLException ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, "Failed to search", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            List<CheckOut> results = service.getAllCheckOuts();
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(new String[]{"ID", "Reservation", "Guest", "Room", "Total"}, 0);
            for (CheckOut c : results) {
                model.addRow(new Object[]{c.getCheckOutID(), c.getReservationID(), c.getGuestID(), c.getRoomID(), c.getTotalAmount()});
            }
            view.getTable().setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, "Failed to load check-outs", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void clearForm() {
        view.getTxtSearch().setText("");
        view.getTxtGuestName().setText("");
        view.getTxtRoomNo().setText("");
        view.getTxtRoomCharges().setText("");
        view.getTxtAdditionalCharges().setText("");
        view.getTxtTotalAmount().setText("");
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
