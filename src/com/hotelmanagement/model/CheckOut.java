package com.hotelmanagement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CheckOut implements Serializable {

    private static final long serialVersionUID = 1L;
    private int checkOutID;
    private int reservationID;
    private int checkInID;
    private int guestID;
    private int roomID;
    private LocalDateTime actualCheckOutDate;
    private BigDecimal roomCharges;
    private BigDecimal additionalCharges;
    private BigDecimal totalAmount;
    private String notes;
    private Integer processedBy;
    private LocalDateTime createdAt;

    public CheckOut() {}

    public CheckOut(int checkOutID, int reservationID, int checkInID, int guestID, int roomID) {
        this.checkOutID = checkOutID;
        this.reservationID = reservationID;
        this.checkInID = checkInID;
        this.guestID = guestID;
        this.roomID = roomID;
        this.roomCharges = BigDecimal.ZERO;
        this.additionalCharges = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
    }

    public int getCheckOutID() { return checkOutID; }
    public void setCheckOutID(int checkOutID) { this.checkOutID = checkOutID; }

    public int getReservationID() { return reservationID; }
    public void setReservationID(int reservationID) { this.reservationID = reservationID; }

    public int getCheckInID() { return checkInID; }
    public void setCheckInID(int checkInID) { this.checkInID = checkInID; }

    public int getGuestID() { return guestID; }
    public void setGuestID(int guestID) { this.guestID = guestID; }

    public int getRoomID() { return roomID; }
    public void setRoomID(int roomID) { this.roomID = roomID; }

    public LocalDateTime getActualCheckOutDate() { return actualCheckOutDate; }
    public void setActualCheckOutDate(LocalDateTime actualCheckOutDate) { this.actualCheckOutDate = actualCheckOutDate; }

    public BigDecimal getRoomCharges() { return roomCharges; }
    public void setRoomCharges(BigDecimal roomCharges) { this.roomCharges = roomCharges; }

    public BigDecimal getAdditionalCharges() { return additionalCharges; }
    public void setAdditionalCharges(BigDecimal additionalCharges) { this.additionalCharges = additionalCharges; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getProcessedBy() { return processedBy; }
    public void setProcessedBy(Integer processedBy) { this.processedBy = processedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckOut checkOut = (CheckOut) o;
        return checkOutID == checkOut.checkOutID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(checkOutID);
    }

    @Override
    public String toString() {
        return "CheckOut{checkOutID=" + checkOutID + ", reservationID=" + reservationID + ", totalAmount=" + totalAmount + "}";
    }
}
