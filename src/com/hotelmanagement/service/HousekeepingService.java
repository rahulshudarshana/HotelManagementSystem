package com.hotelmanagement.service;

import com.hotelmanagement.dao.HousekeepingDAO;
import com.hotelmanagement.model.Housekeeping;
import com.hotelmanagement.model.enums.TaskStatus;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class HousekeepingService {
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

    public int createTask(Housekeeping task) throws SQLException {
        if (task.getRoomID() <= 0) {
            throw new IllegalArgumentException("Room is required.");
        }
        if (task.getTaskType() == null || task.getTaskType().trim().isEmpty()) {
            throw new IllegalArgumentException("Task type is required.");
        }
        return housekeepingDAO.insertTask(task);
    }

    public void updateTask(Housekeeping task) throws SQLException {
        housekeepingDAO.updateTask(task);
    }

    public void updateTaskStatus(int taskID, TaskStatus status) throws SQLException {
        housekeepingDAO.updateTaskStatus(taskID, status);
    }

    public void deleteTask(int id) throws SQLException {
        housekeepingDAO.deleteTask(id);
    }
}
