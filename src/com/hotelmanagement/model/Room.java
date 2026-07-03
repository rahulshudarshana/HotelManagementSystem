package com.hotelmanagement.model;

import com.hotelmanagement.model.enums.RoomStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Room {
    private int roomID;
    private String roomNumber;
    private int roomTypeID;
    private int floor;
    private BigDecimal pricePerNight;
    private int capacity;
    private RoomStatus status;
    private String description;
    private LocalDateTime createdAt;

    public Room() {}

    public Room(int roomID, String roomNumber, int roomTypeID, int floor,
                BigDecimal pricePerNight, int capacity) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.roomTypeID = roomTypeID;
        this.floor = floor;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.status = RoomStatus.Available;
    }

    public int getRoomID() { return roomID; }
    public void setRoomID(int roomID) { this.roomID = roomID; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getRoomTypeID() { return roomTypeID; }
    public void setRoomTypeID(int roomTypeID) { this.roomTypeID = roomTypeID; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
