package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.RoomStatus;
import com.hotelmanagement.service.RoomService;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.room.RoomManagementPanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class RoomController {
    private final RoomManagementPanel view;
    private final RoomService service;

    public RoomController(RoomManagementPanel view) {
        this.view = view;
        this.service = new RoomService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnAdd().addActionListener(e -> addRoom());
        view.getBtnUpdate().addActionListener(e -> updateRoom());
        view.getBtnDelete().addActionListener(e -> deleteRoom());
        view.getBtnSearch().addActionListener(e -> searchRooms());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        int id = (int) view.getTable().getValueAt(row, 0);
                        Room room = service.getRoomById(id);
                        if (room != null) {
                            populateForm(room);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Failed to load room for selection", ex);
                    }
                }
            }
        });
    }

    private void addRoom() {
        try {
            String floorText = view.getTxtFloor().getText().trim();
            String priceText = view.getTxtPrice().getText().trim();
            String capacityText = view.getTxtCapacity().getText().trim();
            if (floorText.isEmpty() || priceText.isEmpty() || capacityText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Floor, Price, and Capacity are required.");
                return;
            }
            Room room = new Room();
            room.setRoomNumber(view.getTxtRoomNumber().getText().trim());
            room.setRoomTypeID(view.getCmbRoomType().getSelectedIndex() + 1);
            room.setFloor(Integer.parseInt(floorText));
            room.setPricePerNight(new BigDecimal(priceText));
            room.setCapacity(Integer.parseInt(capacityText));
            room.setStatus(RoomStatus.Available);
            service.createRoom(room);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Room added successfully.");
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Database error adding room", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Unexpected error adding room", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void updateRoom() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a room.");
                return;
            }
            String roomNumber = view.getTxtRoomNumber().getText().trim();
            String floorText = view.getTxtFloor().getText().trim();
            String priceText = view.getTxtPrice().getText().trim();
            String capacityText = view.getTxtCapacity().getText().trim();
            if (roomNumber.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Room number is required.");
                return;
            }
            if (floorText.isEmpty() || priceText.isEmpty() || capacityText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Floor, Price, and Capacity are required.");
                return;
            }
            int roomID = (int) view.getTable().getValueAt(selectedRow, 0);
            Room room = service.getRoomById(roomID);
            if (room != null) {
                room.setRoomNumber(roomNumber);
                room.setRoomTypeID(view.getCmbRoomType().getSelectedIndex() + 1);
                room.setFloor(Integer.parseInt(floorText));
                room.setPricePerNight(new BigDecimal(priceText));
                room.setCapacity(Integer.parseInt(capacityText));
                room.setStatus(RoomStatus.valueOf((String) view.getCmbStatus().getSelectedItem()));
                service.updateRoom(room);
                loadTable();
                javax.swing.JOptionPane.showMessageDialog(view, "Room updated successfully.");
            }
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Database error updating room", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Unexpected error updating room", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void deleteRoom() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int roomID = (int) view.getTable().getValueAt(selectedRow, 0);
            if (javax.swing.JOptionPane.showConfirmDialog(view, "Delete this room?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
                service.deleteRoom(roomID);
                loadTable();
                clearForm();
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Database error deleting room", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Unexpected error deleting room", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void searchRooms() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            populateTable(service.searchRooms(keyword));
        } catch (SQLException ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Failed to search rooms", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllRooms());
        } catch (SQLException ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, "Failed to load rooms", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<Room> rooms) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Room No", "Type", "Floor", "Price", "Capacity", "Status"}, 0);
        for (Room r : rooms) {
            String roomTypeName = r.getRoomTypeID() > 0 && r.getRoomTypeID() <= view.getCmbRoomType().getItemCount()
                ? view.getCmbRoomType().getItemAt(r.getRoomTypeID() - 1) : "Unknown";
            model.addRow(new Object[]{r.getRoomID(), r.getRoomNumber(), roomTypeName, r.getFloor(), r.getPricePerNight(), r.getCapacity(), r.getStatus().name()});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtRoomNumber().setText("");
        view.getTxtFloor().setText("");
        view.getTxtPrice().setText("");
        view.getTxtCapacity().setText("");
        view.getTxtSearch().setText("");
    }

    private void populateForm(Room room) {
        view.getTxtRoomNumber().setText(room.getRoomNumber() != null ? room.getRoomNumber() : "");
        view.getCmbRoomType().setSelectedIndex(Math.max(0, room.getRoomTypeID() - 1));
        view.getTxtFloor().setText(String.valueOf(room.getFloor()));
        view.getTxtPrice().setText(room.getPricePerNight() != null ? room.getPricePerNight().toString() : "");
        view.getTxtCapacity().setText(String.valueOf(room.getCapacity()));
        if (room.getStatus() != null) {
            view.getCmbStatus().setSelectedItem(room.getStatus().name());
        }
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
