package com.hotelmanagement.model.enums;

public enum TaskStatus {
    // Maps to HousekeepingTasks.Status DB CHECK constraint: 'Pending', 'InProgress', 'Completed', 'Cancelled'
    Pending,
    InProgress,
    Completed,
    Cancelled
}
