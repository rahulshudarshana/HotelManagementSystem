package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Employee;
import com.hotelmanagement.model.User;
import com.hotelmanagement.service.AuthService;
import com.hotelmanagement.service.EmployeeService;
import com.hotelmanagement.service.UserService;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.user.usermanagementpanel;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class UserController {
    private final usermanagementpanel view;
    private final UserService service;
    private final EmployeeService employeeService;

    public UserController(usermanagementpanel view) {
        this.view = view;
        this.service = new UserService();
        this.employeeService = new EmployeeService();
        loadComboBoxes();
        initControllers();
        loadTable();
    }

    private void loadComboBoxes() {
        try {
            view.getCmbEmployee().removeAllItems();
            for (Employee e : employeeService.getAllEmployees()) {
                view.getCmbEmployee().addItem(e.getEmployeeID() + " - " + e.getFirstName() + " " + e.getLastName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Failed to load employees", ex);
        }
    }

    private void initControllers() {
        view.getBtnAddUser().addActionListener(e -> addUser());
        view.getBtnUpdate().addActionListener(e -> updateUser());
        view.getBtnDelete().addActionListener(e -> deleteUser());
        view.getBtnResetPassword().addActionListener(e -> resetPassword());
        view.getBtnSearch().addActionListener(e -> searchUsers());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        int id = (int) view.getTable().getValueAt(row, 0);
                        User user = service.getUserById(id);
                        if (user != null) {
                            populateForm(user);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Failed to load user for selection", ex);
                    }
                }
            }
        });
    }

    private void addUser() {
        try {
            String password = view.getTxtPassword().getText().trim();
            String confirm = view.getTxtConfirmPassword().getText().trim();
            if (!password.equals(confirm)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Passwords do not match.");
                return;
            }
            if (view.getCmbEmployee().getSelectedItem() == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select an employee.");
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
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Database error creating user", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void updateUser() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            String username = view.getTxtUsername().getText().trim();
            if (username.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Username is required.");
                return;
            }
            int userID = (int) view.getTable().getValueAt(selectedRow, 0);
            User user = service.getUserById(userID);
            if (user != null) {
                user.setUsername(username);
                if (view.getCmbEmployee().getSelectedItem() != null) {
                    String empId = view.getCmbEmployee().getSelectedItem().toString().split("-")[0].trim();
                    user.setEmployeeID(Integer.parseInt(empId));
                }
                user.setRoleID(view.getCmbRole().getSelectedIndex() + 1);
                user.setStatus((String) view.getCmbStatus().getSelectedItem());
                service.updateUser(user);
                loadTable();
                javax.swing.JOptionPane.showMessageDialog(view, "User updated.");
            }
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Database error updating user", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
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
        } catch (DataAccessException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Database error deleting user", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
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
                user.setPasswordHash(AuthService.hashPassword(newPassword));
                service.updateUser(user);
                loadTable();
                clearForm();
                javax.swing.JOptionPane.showMessageDialog(view, "Password reset successfully.");
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Database error resetting password", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void searchUsers() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            populateTable(service.searchUsers(keyword));
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Failed to search users", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            loadComboBoxes();
            populateTable(service.getAllUsers());
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Failed to load users", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<User> users) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Username", "Employee", "Role", "Status"}, 0);
        for (User u : users) {
            String empName = "";
            for (int i = 0; i < view.getCmbEmployee().getItemCount(); i++) {
                String item = view.getCmbEmployee().getItemAt(i);
                if (item.startsWith(u.getEmployeeID() + " - ")) {
                    empName = item.substring(item.indexOf(" - ") + 3);
                    break;
                }
            }
            String roleName = u.getRoleID() > 0 && u.getRoleID() <= view.getCmbRole().getItemCount()
                ? view.getCmbRole().getItemAt(u.getRoleID() - 1) : "Unknown";
            model.addRow(new Object[]{u.getUserID(), u.getUsername(), empName, roleName, u.getStatus()});
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

    private void populateForm(User user) {
        view.getTxtUsername().setText(user.getUsername() != null ? user.getUsername() : "");
        view.getTxtPassword().setText("");
        view.getTxtConfirmPassword().setText("");
        view.getCmbRole().setSelectedIndex(Math.max(0, user.getRoleID() - 1));
        if (user.getStatus() != null) {
            view.getCmbStatus().setSelectedItem(user.getStatus());
        }
        for (int i = 0; i < view.getCmbEmployee().getItemCount(); i++) {
            String item = view.getCmbEmployee().getItemAt(i);
            if (item.startsWith(user.getEmployeeID() + " - ")) {
                view.getCmbEmployee().setSelectedIndex(i);
                break;
            }
        }
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
