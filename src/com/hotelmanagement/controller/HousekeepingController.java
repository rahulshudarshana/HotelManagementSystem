package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Employee;
import com.hotelmanagement.model.Housekeeping;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.TaskStatus;
import com.hotelmanagement.service.EmployeeService;
import com.hotelmanagement.service.HousekeepingService;
import com.hotelmanagement.service.RoomService;
import com.hotelmanagement.util.SessionManager;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.housekeeping.housekeepingpanel;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class HousekeepingController {
    private final housekeepingpanel view;
    private final HousekeepingService service;
    private final RoomService roomService;
    private final EmployeeService employeeService;

    public HousekeepingController(housekeepingpanel view) {
        this.view = view;
        this.service = new HousekeepingService();
        this.roomService = new RoomService();
        this.employeeService = new EmployeeService();
        loadComboBoxes();
        initControllers();
        loadTable();
    }

    private void loadComboBoxes() {
        try {
            view.getCmbRoomNo().removeAllItems();
            for (Room r : roomService.getAllRooms()) {
                view.getCmbRoomNo().addItem(r.getRoomID() + " - " + r.getRoomNumber());
            }
        } catch (SQLException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Failed to load rooms", ex);
        }
        try {
            view.getCmbAssignedEmployee().removeAllItems();
            for (Employee e : employeeService.getAllEmployees()) {
                view.getCmbAssignedEmployee().addItem(e.getEmployeeID() + " - " + e.getFirstName() + " " + e.getLastName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Failed to load employees", ex);
        }
    }

    private void initControllers() {
        view.getBtnAssignTask().addActionListener(e -> addTask());
        view.getBtnUpdate().addActionListener(e -> updateTask());
        view.getBtnDelete().addActionListener(e -> deleteTask());
        view.getBtnCompleteTask().addActionListener(e -> completeTask());
        view.getBtnSearch().addActionListener(e -> searchTasks());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        int id = (int) view.getTable().getValueAt(row, 0);
                        Housekeeping task = service.getTaskById(id);
                        if (task != null) {
                            populateForm(task);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Failed to load task for selection", ex);
                    }
                }
            }
        });
    }

    private void addTask() {
        try {
            if (view.getCmbRoomNo().getSelectedItem() == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please select a room.");
                return;
            }
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
            task.setCreatedBy(SessionManager.getInstance().getCurrentUserId());
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
                task.setStatus(mapTaskStatus((String) view.getCmbStatus().getSelectedItem()));
                task.setNotes(view.getNotesArea().getText().trim());
                service.updateTask(task);
                loadTable();
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
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Failed to search tasks", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            loadComboBoxes();
            populateTable(service.getAllTasks());
        } catch (SQLException ex) {
            Logger.getLogger(HousekeepingController.class.getName()).log(Level.SEVERE, "Failed to load tasks", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<Housekeeping> tasks) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Room", "Type", "Priority", "Status", "Employee", "Date"}, 0);
        for (Housekeeping t : tasks) {
            String roomInfo = "";
            for (int i = 0; i < view.getCmbRoomNo().getItemCount(); i++) {
                String item = view.getCmbRoomNo().getItemAt(i);
                if (item.startsWith(t.getRoomID() + " - ")) {
                    roomInfo = item.substring(item.indexOf(" - ") + 3);
                    break;
                }
            }
            String empName = "";
            if (t.getAssignedEmployeeID() != null) {
                for (int i = 0; i < view.getCmbAssignedEmployee().getItemCount(); i++) {
                    String item = view.getCmbAssignedEmployee().getItemAt(i);
                    if (item.startsWith(t.getAssignedEmployeeID() + " - ")) {
                        empName = item.substring(item.indexOf(" - ") + 3);
                        break;
                    }
                }
            }
            model.addRow(new Object[]{t.getTaskID(), roomInfo, t.getTaskType(), t.getPriority(),
                t.getStatus().name(), empName,
                t.getScheduledDate() != null ? t.getScheduledDate().toString() : ""});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtTaskID().setText("");
        view.getTxtSearch().setText("");
        view.getNotesArea().setText("");
    }

    private TaskStatus mapTaskStatus(String displayText) {
        if (displayText == null) return TaskStatus.Pending;
        return switch (displayText) {
            case "In Progress" -> TaskStatus.InProgress;
            default -> {
                try {
                    yield TaskStatus.valueOf(displayText);
                } catch (IllegalArgumentException e) {
                    yield TaskStatus.Pending;
                }
            }
        };
    }

    private String formatTaskStatus(TaskStatus status) {
        if (status == null) return "Pending";
        return switch (status) {
            case InProgress -> "In Progress";
            default -> status.name();
        };
    }

    private void populateForm(Housekeeping task) {
        if (task.getStatus() != null) {
            view.getCmbStatus().setSelectedItem(formatTaskStatus(task.getStatus()));
        }
        view.getCmbTaskType().setSelectedItem(task.getTaskType());
        view.getCmbPriority().setSelectedItem(task.getPriority());
        view.getNotesArea().setText(task.getNotes() != null ? task.getNotes() : "");
        if (task.getScheduledDate() != null) {
            view.getDpScheduledDate().setDate(Date.from(task.getScheduledDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        for (int i = 0; i < view.getCmbRoomNo().getItemCount(); i++) {
            String item = view.getCmbRoomNo().getItemAt(i);
            if (item.startsWith(task.getRoomID() + " - ")) {
                view.getCmbRoomNo().setSelectedIndex(i);
                break;
            }
        }
        if (task.getAssignedEmployeeID() != null) {
            for (int i = 0; i < view.getCmbAssignedEmployee().getItemCount(); i++) {
                String item = view.getCmbAssignedEmployee().getItemAt(i);
                if (item.startsWith(task.getAssignedEmployeeID() + " - ")) {
                    view.getCmbAssignedEmployee().setSelectedIndex(i);
                    break;
                }
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
