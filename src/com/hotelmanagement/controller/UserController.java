package com.hotelmanagement.controller;

import com.hotelmanagement.model.User;
import com.hotelmanagement.service.UserService;
import com.hotelmanagement.view.user.usermanagementpanel;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class UserController {
    private final usermanagementpanel view;
    private final UserService service;

    public UserController(usermanagementpanel view) {
        this.view = view;
        this.service = new UserService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnAddUser().addActionListener(e -> addUser());
        view.getBtnUpdate().addActionListener(e -> updateUser());
        view.getBtnDelete().addActionListener(e -> deleteUser());
        view.getBtnResetPassword().addActionListener(e -> resetPassword());
        view.getBtnSearch().addActionListener(e -> searchUsers());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
    }

    private void addUser() {
        try {
            String password = view.getTxtPassword().getText().trim();
            String confirm = view.getTxtConfirmPassword().getText().trim();
            if (!password.equals(confirm)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Passwords do not match.");
                return;
            }
            User user = new User();
            user.setUsername(view.getTxtUsername().getText().trim());
            user.setPasswordHash(password);
            user.setEmployeeID(Integer.parseInt(view.getCmbEmployee().getSelectedItem().toString().split("-")[0].trim()));
            user.setRoleID(view.getCmbRole().getSelectedIndex() + 1);
            user.setStatus((String) view.getCmbStatus().getSelectedItem());
            service.createUser(user);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "User created successfully.");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void updateUser() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int userID = (int) view.getTable().getValueAt(selectedRow, 0);
            User user = service.getUserById(userID);
            if (user != null) {
                user.setUsername(view.getTxtUsername().getText().trim());
                user.setRoleID(view.getCmbRole().getSelectedIndex() + 1);
                user.setStatus((String) view.getCmbStatus().getSelectedItem());
                service.updateUser(user);
                loadTable();
                clearForm();
                javax.swing.JOptionPane.showMessageDialog(view, "User updated.");
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void deleteUser() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int userID = (int) view.getTable().getValueAt(selectedRow, 0);
            if (javax.swing.JOptionPane.showConfirmDialog(view, "Delete this user?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
                service.deleteUser(userID);
                loadTable();
                clearForm();
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void resetPassword() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int userID = (int) view.getTable().getValueAt(selectedRow, 0);
            String newPassword = view.getTxtPassword().getText().trim();
            if (newPassword.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Enter a new password.");
                return;
            }
            User user = service.getUserById(userID);
            if (user != null) {
                user.setPasswordHash(newPassword);
                service.updateUser(user);
                javax.swing.JOptionPane.showMessageDialog(view, "Password reset successfully.");
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void searchUsers() {
        try {
            String keyword = view.getTxtSearch().getText().trim().toLowerCase();
            List<User> all = service.getAllUsers();
            List<User> filtered = all.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(keyword))
                .toList();
            populateTable(filtered);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllUsers());
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateTable(List<User> users) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Username", "Employee ID", "Role", "Status"}, 0);
        for (User u : users) {
            model.addRow(new Object[]{u.getUserID(), u.getUsername(), u.getEmployeeID(), u.getRoleID(), u.getStatus()});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtUsername().setText("");
        view.getTxtPassword().setText("");
        view.getTxtConfirmPassword().setText("");
        view.getTxtUserID().setText("");
        view.getTxtSearch().setText("");
    }

    private void navigateBack() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
