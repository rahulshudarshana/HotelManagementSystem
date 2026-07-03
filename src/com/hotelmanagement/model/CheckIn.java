package com.hotelmanagement.model;

import java.time.LocalDateTime;

public class CheckIn {
    private int checkInID;
    private int reservationID;
    private int guestID;
    private int roomID;
    private LocalDateTime actualCheckInDate;
    private int numberOfGuests;
    private Integer receptionistID;
    private String notes;
    private LocalDateTime createdAt;

    public CheckIn() {}

    public CheckIn(int checkInID, int reservationID, int guestID, int roomID, int numberOfGuests) {
        this.checkInID = checkInID;
        this.reservationID = reservationID;
        this.guestID = guestID;
        this.roomID = roomID;
        this.numberOfGuests = numberOfGuests;
    }

    public int getCheckInID() { return checkInID; }
    public void setCheckInID(int checkInID) { this.checkInID = checkInID; }

    public int getReservationID() { return reservationID; }
    public void setReservationID(int reservationID) { this.reservationID = reservationID; }

    public int getGuestID() { return guestID; }
    public void setGuestID(int guestID) { this.guestID = guestID; }

    public int getRoomID() { return roomID; }
    public void setRoomID(int roomID) { this.roomID = roomID; }

    public LocalDateTime getActualCheckInDate() { return actualCheckInDate; }
    public void setActualCheckInDate(LocalDateTime actualCheckInDate) { this.actualCheckInDate = actualCheckInDate; }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    public Integer getReceptionistID() { return receptionistID; }
    public void setReceptionistID(Integer receptionistID) { this.receptionistID = receptionistID; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
