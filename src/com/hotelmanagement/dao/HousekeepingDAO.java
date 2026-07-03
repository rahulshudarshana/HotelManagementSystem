package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.Housekeeping;
import com.hotelmanagement.model.enums.TaskStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingDAO {

    public Housekeeping getTaskById(int taskID) throws SQLException {
        String sql = "SELECT * FROM HousekeepingTasks WHERE TaskID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapTask(rs);
                }
            }
        }
        return null;
    }

    public List<Housekeeping> getTasksByRoom(int roomID) throws SQLException {
        List<Housekeeping> tasks = new ArrayList<>();
        String sql = "SELECT * FROM HousekeepingTasks WHERE RoomID = ? ORDER BY ScheduledDate";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
        }
        return tasks;
    }

    public List<Housekeeping> getTasksByEmployee(int employeeID) throws SQLException {
        List<Housekeeping> tasks = new ArrayList<>();
        String sql = "SELECT * FROM HousekeepingTasks WHERE AssignedEmployeeID = ? ORDER BY ScheduledDate";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
        }
        return tasks;
    }

    public List<Housekeeping> getTasksByStatus(TaskStatus status) throws SQLException {
        List<Housekeeping> tasks = new ArrayList<>();
        String sql = "SELECT * FROM HousekeepingTasks WHERE Status = ? ORDER BY ScheduledDate";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
        }
        return tasks;
    }

    public List<Housekeeping> getTasksByDate(LocalDate date) throws SQLException {
        List<Housekeeping> tasks = new ArrayList<>();
        String sql = "SELECT * FROM HousekeepingTasks WHERE ScheduledDate = ? ORDER BY Priority";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
        }
        return tasks;
    }

    public List<Housekeeping> getAllTasks() throws SQLException {
        List<Housekeeping> tasks = new ArrayList<>();
        String sql = "SELECT * FROM HousekeepingTasks ORDER BY ScheduledDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(mapTask(rs));
            }
        }
        return tasks;
    }

    public int insertTask(Housekeeping task) throws SQLException {
        String sql = "INSERT INTO HousekeepingTasks (RoomID, AssignedEmployeeID, TaskType, Priority, Status, ScheduledDate, Notes, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, task.getRoomID());
            stmt.setObject(2, task.getAssignedEmployeeID());
            stmt.setString(3, task.getTaskType());
            stmt.setString(4, task.getPriority());
            stmt.setString(5, task.getStatus().name());
            stmt.setObject(6, task.getScheduledDate());
            stmt.setString(7, task.getNotes());
            stmt.setObject(8, task.getCreatedBy());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateTask(Housekeeping task) throws SQLException {
        String sql = "UPDATE HousekeepingTasks SET RoomID = ?, AssignedEmployeeID = ?, TaskType = ?, Priority = ?, Status = ?, ScheduledDate = ?, CompletionDate = ?, Notes = ?, UpdatedAt = NOW() WHERE TaskID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, task.getRoomID());
            stmt.setObject(2, task.getAssignedEmployeeID());
            stmt.setString(3, task.getTaskType());
            stmt.setString(4, task.getPriority());
            stmt.setString(5, task.getStatus().name());
            stmt.setObject(6, task.getScheduledDate());
            stmt.setObject(7, task.getCompletionDate());
            stmt.setString(8, task.getNotes());
            stmt.setInt(9, task.getTaskID());
            stmt.executeUpdate();
        }
    }

    public void updateTaskStatus(int taskID, TaskStatus status) throws SQLException {
        String sql = "UPDATE HousekeepingTasks SET Status = ?, UpdatedAt = NOW() WHERE TaskID = ?";
        if (status == TaskStatus.Completed) {
            sql = "UPDATE HousekeepingTasks SET Status = ?, CompletionDate = NOW(), UpdatedAt = NOW() WHERE TaskID = ?";
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, taskID);
            stmt.executeUpdate();
        }
    }

    public void deleteTask(int taskID) throws SQLException {
        String sql = "DELETE FROM HousekeepingTasks WHERE TaskID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskID);
            stmt.executeUpdate();
        }
    }

    private Housekeeping mapTask(ResultSet rs) throws SQLException {
        Housekeeping task = new Housekeeping();
        task.setTaskID(rs.getInt("TaskID"));
        task.setRoomID(rs.getInt("RoomID"));
        task.setAssignedEmployeeID(rs.getObject("AssignedEmployeeID", Integer.class));
        task.setTaskType(rs.getString("TaskType"));
        task.setPriority(rs.getString("Priority"));
        task.setStatus(TaskStatus.valueOf(rs.getString("Status")));
        if (rs.getDate("ScheduledDate") != null) {
            task.setScheduledDate(rs.getDate("ScheduledDate").toLocalDate());
        }
        if (rs.getTimestamp("CompletionDate") != null) {
            task.setCompletionDate(rs.getTimestamp("CompletionDate").toLocalDateTime());
        }
        task.setNotes(rs.getString("Notes"));
        task.setCreatedBy(rs.getObject("CreatedBy", Integer.class));
        if (rs.getTimestamp("CreatedAt") != null) {
            task.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        if (rs.getTimestamp("UpdatedAt") != null) {
            task.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        }
        return task;
    }
}
