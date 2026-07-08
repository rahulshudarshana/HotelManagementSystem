package com.hotelmanagement.model;

import com.hotelmanagement.model.enums.TaskStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Housekeeping implements Serializable {

    private static final long serialVersionUID = 1L;
    private int taskID;
    private int roomID;
    private Integer assignedEmployeeID;
    private String taskType;
    private String priority;
    private TaskStatus status;
    private LocalDate scheduledDate;
    private LocalDateTime completionDate;
    private String notes;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Housekeeping() {}

    public Housekeeping(int taskID, int roomID, String taskType) {
        this.taskID = taskID;
        this.roomID = roomID;
        this.taskType = taskType;
        this.priority = "Normal";
        this.status = TaskStatus.Pending;
    }

    public int getTaskID() { return taskID; }
    public void setTaskID(int taskID) { this.taskID = taskID; }

    public int getRoomID() { return roomID; }
    public void setRoomID(int roomID) { this.roomID = roomID; }

    public Integer getAssignedEmployeeID() { return assignedEmployeeID; }
    public void setAssignedEmployeeID(Integer assignedEmployeeID) { this.assignedEmployeeID = assignedEmployeeID; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    public LocalDateTime getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDateTime completionDate) { this.completionDate = completionDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Housekeeping that = (Housekeeping) o;
        return taskID == that.taskID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(taskID);
    }

    @Override
    public String toString() {
        return "Housekeeping{taskID=" + taskID + ", roomID=" + roomID + ", taskType='" + taskType + "', status=" + status + "}";
    }
}
