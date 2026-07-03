package com.hotelmanagement.controller;

import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.RoomStatus;
import com.hotelmanagement.service.RoomService;
import com.hotelmanagement.view.room.RoomManagementPanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    }

    private void addRoom() {
        try {
            Room room = new Room();
            room.setRoomNumber(view.getTxtRoomNumber().getText().trim());
            room.setRoomTypeID(view.getCmbRoomType().getSelectedIndex() + 1);
            room.setFloor(Integer.parseInt(view.getTxtFloor().getText().trim()));
            room.setPricePerNight(new BigDecimal(view.getTxtPrice().getText().trim()));
            room.setCapacity(Integer.parseInt(view.getTxtCapacity().getText().trim()));
            room.setStatus(RoomStatus.Available);
            service.createRoom(room);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Room added successfully.");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void updateRoom() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a room.");
                return;
            }
            int roomID = (int) view.getTable().getValueAt(selectedRow, 0);
            Room room = service.getRoomById(roomID);
            if (room != null) {
                room.setRoomNumber(view.getTxtRoomNumber().getText().trim());
                room.setRoomTypeID(view.getCmbRoomType().getSelectedIndex() + 1);
                room.setFloor(Integer.parseInt(view.getTxtFloor().getText().trim()));
                room.setPricePerNight(new BigDecimal(view.getTxtPrice().getText().trim()));
                room.setCapacity(Integer.parseInt(view.getTxtCapacity().getText().trim()));
                room.setStatus(RoomStatus.valueOf((String) view.getCmbStatus().getSelectedItem()));
                service.updateRoom(room);
                loadTable();
                clearForm();
                javax.swing.JOptionPane.showMessageDialog(view, "Room updated successfully.");
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
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
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void searchRooms() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            List<Room> all = service.getAllRooms();
            List<Room> filtered = all.stream().filter(r -> r.getRoomNumber().contains(keyword)).toList();
            populateTable(filtered);
        } catch (SQLException ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllRooms());
        } catch (SQLException ex) {
            Logger.getLogger(RoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateTable(List<Room> rooms) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Room No", "Type", "Floor", "Price", "Capacity", "Status"}, 0);
        for (Room r : rooms) {
            model.addRow(new Object[]{r.getRoomID(), r.getRoomNumber(), r.getRoomTypeID(), r.getFloor(), r.getPricePerNight(), r.getCapacity(), r.getStatus().name()});
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

    private void navigateBack() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
