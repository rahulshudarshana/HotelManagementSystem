package com.hotelmanagement.service;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.dao.HousekeepingDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Housekeeping;
import com.hotelmanagement.model.enums.TaskStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HousekeepingService {
    private static final Logger LOGGER = Logger.getLogger(HousekeepingService.class.getName());
    private final HousekeepingDAO housekeepingDAO;

    public HousekeepingService() {
        this.housekeepingDAO = new HousekeepingDAO();
    }

    public Housekeeping getTaskById(int id) throws SQLException {
        return housekeepingDAO.getTaskById(id);
    }

    public List<Housekeeping> getTasksByRoom(int roomID) throws SQLException {
        return housekeepingDAO.getTasksByRoom(roomID);
    }

    public List<Housekeeping> getTasksByEmployee(int employeeID) throws SQLException {
        return housekeepingDAO.getTasksByEmployee(employeeID);
    }

    public List<Housekeeping> getTasksByStatus(TaskStatus status) throws SQLException {
        return housekeepingDAO.getTasksByStatus(status);
    }

    public List<Housekeeping> getTasksByDate(LocalDate date) throws SQLException {
        return housekeepingDAO.getTasksByDate(date);
    }

    public List<Housekeeping> getAllTasks() throws SQLException {
        return housekeepingDAO.getAllTasks();
    }

    public List<Housekeeping> searchTasks(String keyword) throws SQLException {
        return housekeepingDAO.searchTasks(keyword);
    }

    public int createTask(Housekeeping task) {
        if (task.getRoomID() <= 0) {
            throw new ValidationException("Room is required.");
        }
        if (task.getTaskType() == null || task.getTaskType().trim().isEmpty()) {
            throw new ValidationException("Task type is required.");
        }
        try {
            return housekeepingDAO.insertTask(task);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Task creation failed", e);
            throw new DataAccessException("Task creation failed due to a database error.", e);
        }
    }

    public void updateTask(Housekeeping task) {
        try {
            housekeepingDAO.updateTask(task);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Task update failed", e);
            throw new DataAccessException("Task update failed due to a database error.", e);
        }
    }

    public void updateTaskStatus(int taskID, TaskStatus status) {
        try {
            housekeepingDAO.updateTaskStatus(taskID, status);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Task status update failed", e);
            throw new DataAccessException("Task status update failed due to a database error.", e);
        }
    }

    public void deleteTask(int id) {
        try {
            housekeepingDAO.deleteTask(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Task deletion failed", e);
            throw new DataAccessException("Task deletion failed due to a database error.", e);
        }
    }
}
