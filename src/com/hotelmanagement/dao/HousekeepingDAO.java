package com.hotelmanagement.dao;

import com.hotelmanagement.model.Housekeeping;
import com.hotelmanagement.model.enums.TaskStatus;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class HousekeepingDAO extends BaseDAO<Housekeeping> {

    private static final Logger LOGGER = Logger.getLogger(HousekeepingDAO.class.getName());

    private static final String COLUMNS = "TaskID, RoomID, AssignedEmployeeID, TaskType, Priority, Status, ScheduledDate, CompletionDate, Notes, CreatedBy, CreatedAt, UpdatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM HousekeepingTasks WHERE TaskID = ?";
    private static final String SQL_BY_ROOM = "SELECT " + COLUMNS + " FROM HousekeepingTasks WHERE RoomID = ? ORDER BY ScheduledDate";
    private static final String SQL_BY_EMPLOYEE = "SELECT " + COLUMNS + " FROM HousekeepingTasks WHERE AssignedEmployeeID = ? ORDER BY ScheduledDate";
    private static final String SQL_BY_STATUS = "SELECT " + COLUMNS + " FROM HousekeepingTasks WHERE Status = ? ORDER BY ScheduledDate";
    private static final String SQL_BY_DATE = "SELECT " + COLUMNS + " FROM HousekeepingTasks WHERE ScheduledDate = ? ORDER BY Priority";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM HousekeepingTasks ORDER BY ScheduledDate DESC";
    private static final String SQL_INSERT = "INSERT INTO HousekeepingTasks (RoomID, AssignedEmployeeID, TaskType, Priority, Status, ScheduledDate, Notes, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE HousekeepingTasks SET RoomID = ?, AssignedEmployeeID = ?, TaskType = ?, Priority = ?, Status = ?, ScheduledDate = ?, CompletionDate = ?, Notes = ?, UpdatedAt = NOW() WHERE TaskID = ?";
    private static final String SQL_UPDATE_STATUS = "UPDATE HousekeepingTasks SET Status = ?, UpdatedAt = NOW() WHERE TaskID = ?";
    private static final String SQL_UPDATE_STATUS_COMPLETED = "UPDATE HousekeepingTasks SET Status = ?, CompletionDate = NOW(), UpdatedAt = NOW() WHERE TaskID = ?";
    private static final String SQL_DELETE = "DELETE FROM HousekeepingTasks WHERE TaskID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM HousekeepingTasks WHERE CAST(TaskID AS CHAR) LIKE ? ORDER BY ScheduledDate DESC";

    public Housekeeping getTaskById(int taskID) throws SQLException {
        return findOne(SQL_BY_ID, taskID);
    }

    public List<Housekeeping> getTasksByRoom(int roomID) throws SQLException {
        return findAll(SQL_BY_ROOM, roomID);
    }

    public List<Housekeeping> getTasksByEmployee(int employeeID) throws SQLException {
        return findAll(SQL_BY_EMPLOYEE, employeeID);
    }

    public List<Housekeeping> getTasksByStatus(TaskStatus status) throws SQLException {
        return findAll(SQL_BY_STATUS, status);
    }

    public List<Housekeeping> getTasksByDate(LocalDate date) throws SQLException {
        return findAll(SQL_BY_DATE, date);
    }

    public List<Housekeeping> getAllTasks() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<Housekeeping> searchTasks(String keyword) throws SQLException {
        return findAll(SQL_SEARCH, "%" + keyword + "%");
    }

    public int insertTask(Housekeeping task) throws SQLException {
        return insert(SQL_INSERT,
            task.getRoomID(), task.getAssignedEmployeeID(), task.getTaskType(),
            task.getPriority(), task.getStatus(), task.getScheduledDate(),
            task.getNotes(), task.getCreatedBy());
    }

    public void updateTask(Housekeeping task) throws SQLException {
        update(SQL_UPDATE,
            task.getRoomID(), task.getAssignedEmployeeID(), task.getTaskType(),
            task.getPriority(), task.getStatus(), task.getScheduledDate(),
            task.getCompletionDate(), task.getNotes(), task.getTaskID());
    }

    public void updateTaskStatus(int taskID, TaskStatus status) throws SQLException {
        String sql = (status == TaskStatus.Completed) ? SQL_UPDATE_STATUS_COMPLETED : SQL_UPDATE_STATUS;
        update(sql, status, taskID);
    }

    public void deleteTask(int taskID) throws SQLException {
        delete(SQL_DELETE, taskID);
    }

    @Override
    protected Housekeeping mapRow(ResultSet rs) throws SQLException {
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
