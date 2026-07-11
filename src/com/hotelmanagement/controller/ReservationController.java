package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.ReservationStatus;
import com.hotelmanagement.service.GuestService;
import com.hotelmanagement.service.ReservationService;
import com.hotelmanagement.service.RoomService;
import com.hotelmanagement.util.SessionManager;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.reservation.ReservationManagementPanel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ReservationController {
    private final ReservationManagementPanel view;
    private final ReservationService service;
    private final GuestService guestService;
    private final RoomService roomService;

    public ReservationController(ReservationManagementPanel view) {
        this.view = view;
        this.service = new ReservationService();
        this.guestService = new GuestService();
        this.roomService = new RoomService();
        loadComboBoxes();
        initControllers();
        loadTable();
    }

    private void loadComboBoxes() {
        try {
            view.getCmbGuest().removeAllItems();
            List<Guest> guests = guestService.getAllGuests();
            if (guests.isEmpty()) {
                view.getCmbGuest().addItem("-- No guests available --");
            } else {
                for (Guest g : guests) {
                    view.getCmbGuest().addItem(g.getGuestID() + " - " + g.getFirstName() + " " + g.getLastName());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Failed to load guests", ex);
            view.getCmbGuest().addItem("-- Error loading guests --");
            javax.swing.JOptionPane.showMessageDialog(view, "Could not load guests. Database connection failed.", "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        try {
            view.getCmbRoom().removeAllItems();
            List<Room> rooms = roomService.getAllRooms();
            if (rooms.isEmpty()) {
                view.getCmbRoom().addItem("-- No rooms available --");
            } else {
                for (Room r : rooms) {
                    view.getCmbRoom().addItem(r.getRoomID() + " - " + r.getRoomNumber());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Failed to load rooms", ex);
            view.getCmbRoom().addItem("-- Error loading rooms --");
            javax.swing.JOptionPane.showMessageDialog(view, "Could not load rooms. Database connection failed.", "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initControllers() {
        view.getBtnBook().addActionListener(e -> addReservation());
        view.getBtnUpdate().addActionListener(e -> updateReservation());
        view.getBtnCancel().addActionListener(e -> cancelReservation());
        view.getBtnSearch().addActionListener(e -> searchReservations());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        int id = (int) view.getTable().getValueAt(row, 0);
                        Reservation r = service.getReservationById(id);
                        if (r != null) {
                            populateForm(r);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Failed to load reservation for selection", ex);
                    }
                }
            }
        });
    }

    private void addReservation() {
        try {
            if (view.getCmbGuest().getSelectedItem() == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a guest.");
                return;
            }
            if (view.getCmbRoom().getSelectedItem() == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a room.");
                return;
            }
            String guestsText = view.getTxtNumberOfGuests().getText().trim();
            if (guestsText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please enter the number of guests.");
                return;
            }
            int numberOfGuests = Integer.parseInt(guestsText);
            if (numberOfGuests <= 0) {
                javax.swing.JOptionPane.showMessageDialog(view, "Number of guests must be greater than zero.");
                return;
            }
            Date checkInDate = view.getDpCheckIn().getDate();
            if (checkInDate == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a check-in date.");
                return;
            }
            Date checkOutDate = view.getDpCheckOut().getDate();
            if (checkOutDate == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a check-out date.");
                return;
            }
            LocalDate checkIn = checkInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = checkOutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!checkOut.isAfter(checkIn)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Check-out date must be after check-in date.");
                return;
            }

            Reservation r = new Reservation();
            r.setGuestID(Integer.parseInt(view.getCmbGuest().getSelectedItem().toString().split("-")[0].trim()));
            r.setRoomID(Integer.parseInt(view.getCmbRoom().getSelectedItem().toString().split("-")[0].trim()));
            r.setCheckInDate(checkIn);
            r.setCheckOutDate(checkOut);
            r.setNumberOfGuests(numberOfGuests);
            r.setStatus(mapStatus((String) view.getCmbStatus().getSelectedItem()));
            r.setSpecialRequests(view.getTxtSpecialRequests().getText().trim());
            r.setCreatedBy(SessionManager.getInstance().getCurrentUserId());
            service.createReservation(r);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Reservation created successfully.");
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Database error creating reservation", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void updateReservation() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            String guestsText = view.getTxtNumberOfGuests().getText().trim();
            if (guestsText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Number of guests is required.");
                return;
            }
            int numberOfGuests = Integer.parseInt(guestsText);
            if (numberOfGuests <= 0) {
                javax.swing.JOptionPane.showMessageDialog(view, "Number of guests must be greater than zero.");
                return;
            }
            int id = (int) view.getTable().getValueAt(selectedRow, 0);
            Reservation r = service.getReservationById(id);
            if (r != null) {
                r.setNumberOfGuests(numberOfGuests);
                if (view.getCmbGuest().getSelectedItem() != null) {
                    r.setGuestID(Integer.parseInt(view.getCmbGuest().getSelectedItem().toString().split("-")[0].trim()));
                }
                if (view.getCmbRoom().getSelectedItem() != null) {
                    r.setRoomID(Integer.parseInt(view.getCmbRoom().getSelectedItem().toString().split("-")[0].trim()));
                }
                if (view.getDpCheckIn().getDate() != null) {
                    r.setCheckInDate(view.getDpCheckIn().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                if (view.getDpCheckOut().getDate() != null) {
                    r.setCheckOutDate(view.getDpCheckOut().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                r.setSpecialRequests(view.getTxtSpecialRequests().getText().trim());
                r.setStatus(mapStatus((String) view.getCmbStatus().getSelectedItem()));
                service.updateReservation(r);
                loadTable();
                javax.swing.JOptionPane.showMessageDialog(view, "Reservation updated.");
            }
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Database error updating reservation", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
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
        } catch (DataAccessException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Database error cancelling reservation", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private ReservationStatus mapStatus(String displayText) {
        if (displayText == null) return ReservationStatus.Pending;
        return switch (displayText) {
            case "Checked In" -> ReservationStatus.CheckedIn;
            case "Checked Out" -> ReservationStatus.CheckedOut;
            default -> {
                try {
                    yield ReservationStatus.valueOf(displayText);
                } catch (IllegalArgumentException e) {
                    yield ReservationStatus.Pending;
                }
            }
        };
    }

    private void searchReservations() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            populateTable(service.searchReservations(keyword));
        } catch (SQLException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Failed to search reservations", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            loadComboBoxes();
            populateTable(service.getAllReservations());
        } catch (SQLException ex) {
            Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, "Failed to load reservations", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<Reservation> reservations) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Guest", "Room", "Check-In", "Check-Out", "Guests", "Status"}, 0);
        for (Reservation r : reservations) {
            String guestName = "";
            for (int i = 0; i < view.getCmbGuest().getItemCount(); i++) {
                String item = view.getCmbGuest().getItemAt(i);
                if (item.startsWith(r.getGuestID() + " - ")) {
                    guestName = item.substring(item.indexOf(" - ") + 3);
                    break;
                }
            }
            String roomInfo = "";
            for (int i = 0; i < view.getCmbRoom().getItemCount(); i++) {
                String item = view.getCmbRoom().getItemAt(i);
                if (item.startsWith(r.getRoomID() + " - ")) {
                    roomInfo = item.substring(item.indexOf(" - ") + 3);
                    break;
                }
            }
            model.addRow(new Object[]{r.getReservationID(), guestName, roomInfo,
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
        view.getDpCheckIn().setDate(null);
        view.getDpCheckOut().setDate(null);
    }

    private void populateForm(Reservation r) {
        view.getTxtNumberOfGuests().setText(String.valueOf(r.getNumberOfGuests()));
        view.getTxtSpecialRequests().setText(r.getSpecialRequests() != null ? r.getSpecialRequests() : "");
        if (r.getCheckInDate() != null) {
            view.getDpCheckIn().setDate(Date.from(r.getCheckInDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (r.getCheckOutDate() != null) {
            view.getDpCheckOut().setDate(Date.from(r.getCheckOutDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        for (int i = 0; i < view.getCmbGuest().getItemCount(); i++) {
            String item = view.getCmbGuest().getItemAt(i);
            if (item.startsWith(r.getGuestID() + " - ")) {
                view.getCmbGuest().setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < view.getCmbRoom().getItemCount(); i++) {
            String item = view.getCmbRoom().getItemAt(i);
            if (item.startsWith(r.getRoomID() + " - ")) {
                view.getCmbRoom().setSelectedIndex(i);
                break;
            }
        }
        if (r.getStatus() != null) {
            String displayStatus = switch (r.getStatus()) {
                case CheckedIn -> "Checked In";
                case CheckedOut -> "Checked Out";
                default -> r.getStatus().name();
            };
            view.getCmbStatus().setSelectedItem(displayStatus);
        }
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
