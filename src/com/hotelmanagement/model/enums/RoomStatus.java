package com.hotelmanagement.model.enums;

public enum RoomStatus {
    // Maps to Rooms.Status DB CHECK constraint: 'Available', 'Occupied', 'Reserved', 'Maintenance', 'Cleaning'
    Available,
    Occupied,
    Reserved,
    Maintenance,
    Cleaning
}
