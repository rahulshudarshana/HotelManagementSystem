package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.enums.Gender;
import com.hotelmanagement.service.GuestService;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.guest.GuestManagementPanel;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GuestController {
    private final GuestManagementPanel view;
    private final GuestService service;

    public GuestController(GuestManagementPanel view) {
        this.view = view;
        this.service = new GuestService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnAdd().addActionListener(e -> addGuest());
        view.getBtnUpdate().addActionListener(e -> updateGuest());
        view.getBtnDelete().addActionListener(e -> deleteGuest());
        view.getBtnSearch().addActionListener(e -> searchGuests());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        int id = (int) view.getTable().getValueAt(row, 0);
                        Guest guest = service.getGuestById(id);
                        if (guest != null) {
                            populateForm(guest);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Failed to load guest for selection", ex);
                    }
                }
            }
        });
    }

    private void addGuest() {
        try {
            Guest guest = new Guest();
            guest.setFirstName(view.getTxtFirstName().getText().trim());
            guest.setLastName(view.getTxtLastName().getText().trim());
            guest.setNic(view.getTxtNIC().getText().trim());
            guest.setPhone(view.getTxtPhone().getText().trim());
            guest.setEmail(view.getTxtEmail().getText().trim());
            guest.setAddress(view.getTxtAddress().getText().trim());
            guest.setGender(view.getRadioMale().isSelected() ? Gender.Male : Gender.Female);
            service.createGuest(guest);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Guest added successfully.");
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Database error adding guest", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void updateGuest() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a guest to update.");
                return;
            }
            String firstName = view.getTxtFirstName().getText().trim();
            String lastName = view.getTxtLastName().getText().trim();
            String nic = view.getTxtNIC().getText().trim();
            String phone = view.getTxtPhone().getText().trim();
            if (firstName.isEmpty() || lastName.isEmpty() || nic.isEmpty() || phone.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "First name, Last name, NIC, and Phone are required.");
                return;
            }
            int guestID = (int) view.getTable().getValueAt(selectedRow, 0);
            Guest guest = service.getGuestById(guestID);
            if (guest != null) {
                guest.setFirstName(firstName);
                guest.setLastName(lastName);
                guest.setNic(nic);
                guest.setPhone(phone);
                guest.setEmail(view.getTxtEmail().getText().trim());
                guest.setAddress(view.getTxtAddress().getText().trim());
                guest.setGender(view.getRadioMale().isSelected() ? Gender.Male : Gender.Female);
                service.updateGuest(guest);
                loadTable();
                javax.swing.JOptionPane.showMessageDialog(view, "Guest updated successfully.");
            }
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Database error updating guest", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void deleteGuest() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a guest to delete.");
                return;
            }
            int guestID = (int) view.getTable().getValueAt(selectedRow, 0);
            int confirm = javax.swing.JOptionPane.showConfirmDialog(view, "Delete this guest?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                service.deleteGuest(guestID);
                loadTable();
                clearForm();
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Database error deleting guest", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void searchGuests() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            List<Guest> results = service.searchGuests(keyword);
            populateTable(results);
        } catch (SQLException ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Failed to search guests", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            List<Guest> guests = service.getAllGuests();
            populateTable(guests);
        } catch (SQLException ex) {
            Logger.getLogger(GuestController.class.getName()).log(Level.SEVERE, "Failed to load guests", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<Guest> guests) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "NIC", "Phone", "Email", "Gender"}, 0);
        for (Guest g : guests) {
            model.addRow(new Object[]{g.getGuestID(), g.getFirstName(), g.getLastName(), g.getNic(), g.getPhone(), g.getEmail(), g.getGender().name()});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtFirstName().setText("");
        view.getTxtLastName().setText("");
        view.getTxtNIC().setText("");
        view.getTxtPhone().setText("");
        view.getTxtEmail().setText("");
        view.getTxtAddress().setText("");
        view.getRadioMale().setSelected(true);
        view.getTxtSearch().setText("");
    }

    private void populateForm(Guest guest) {
        view.getTxtFirstName().setText(guest.getFirstName() != null ? guest.getFirstName() : "");
        view.getTxtLastName().setText(guest.getLastName() != null ? guest.getLastName() : "");
        view.getTxtNIC().setText(guest.getNic() != null ? guest.getNic() : "");
        view.getTxtPhone().setText(guest.getPhone() != null ? guest.getPhone() : "");
        view.getTxtEmail().setText(guest.getEmail() != null ? guest.getEmail() : "");
        view.getTxtAddress().setText(guest.getAddress() != null ? guest.getAddress() : "");
        if (guest.getGender() == Gender.Male) {
            view.getRadioMale().setSelected(true);
        } else {
            view.getRadioFemale().setSelected(true);
        }
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
