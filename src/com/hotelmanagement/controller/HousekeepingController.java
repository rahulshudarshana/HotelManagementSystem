package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Housekeeping;
import com.hotelmanagement.model.enums.TaskStatus;
import com.hotelmanagement.service.HousekeepingService;
import com.hotelmanagement.view.housekeeping.housekeepingpanel;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class HousekeepingController {
    private final housekeepingpanel view;
    private final HousekeepingService service;

    public HousekeepingController(housekeepingpanel view) {
        this.view = view;
        this.service = new HousekeepingService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnAssignTask().addActionListener(e -> addTask());
        view.getBtnUpdate().addActionListener(e -> updateTask());
        view.getBtnDelete().addActionListener(e -> deleteTask());
        view.getBtnCompleteTask().addActionListener(e -> completeTask());
        view.getBtnSearch().addActionListener(e -> searchTasks());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
    }

    private void addTask() {
        try {
            Housekeeping task = new Housekeeping();
            task.setRoomID(Integer.parseInt(view.getCmbRoomNo().getSelectedItem().toString().split("-")[0].trim()));
            task.setTaskType((String) view.getCmbTaskType().getSelectedItem());
            task.setPriority((String) view.getCmbPriority().getSelectedItem());
            task.setNotes(view.getNotesArea().getText().trim());
            if (view.getDpScheduledDate().getDate() != null) {
                task.setScheduledDate(view.getDpScheduledDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            if (view.getCmbAssignedEmployee().getSelectedItem() != null) {
                String empInfo = view.getCmbAssignedEmployee().getSelectedItem().toString();
                task.setAssignedEmployeeID(Integer.parseInt(empInfo.split("-")[0].trim()));
            }
            service.createTask(task);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Task assigned successfully.");
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Database error adding task", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void updateTask() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int taskID = (int) view.getTable().getValueAt(selectedRow, 0);
            Housekeeping task = service.getTaskById(taskID);
            if (task != null) {
                task.setTaskType((String) view.getCmbTaskType().getSelectedItem());
                task.setPriority((String) view.getCmbPriority().getSelectedItem());
                task.setStatus(TaskStatus.valueOf((String) view.getCmbStatus().getSelectedItem()));
                task.setNotes(view.getNotesArea().getText().trim());
                service.updateTask(task);
                loadTable();
                clearForm();
                javax.swing.JOptionPane.showMessageDialog(view, "Task updated.");
            }
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Database error updating task", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void deleteTask() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int taskID = (int) view.getTable().getValueAt(selectedRow, 0);
            if (javax.swing.JOptionPane.showConfirmDialog(view, "Delete this task?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
                service.deleteTask(taskID);
                loadTable();
                clearForm();
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Database error deleting task", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void completeTask() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int taskID = (int) view.getTable().getValueAt(selectedRow, 0);
            service.updateTaskStatus(taskID, TaskStatus.Completed);
            loadTable();
            javax.swing.JOptionPane.showMessageDialog(view, "Task marked as completed.");
        } catch (DataAccessException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Database error completing task", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void searchTasks() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            populateTable(service.searchTasks(keyword));
        } catch (SQLException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllTasks());
        } catch (SQLException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateTable(List<Housekeeping> tasks) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Room", "Type", "Priority", "Status", "Employee", "Date"}, 0);
        for (Housekeeping t : tasks) {
            model.addRow(new Object[]{t.getTaskID(), t.getRoomID(), t.getTaskType(), t.getPriority(),
                t.getStatus().name(), t.getAssignedEmployeeID(),
                t.getScheduledDate() != null ? t.getScheduledDate().toString() : ""});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtTaskID().setText("");
        view.getTxtSearch().setText("");
        view.getNotesArea().setText("");
    }

    private void navigateBack() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
