package com.hotelmanagement.model.enums;

public enum ReservationStatus {
    // Maps to Reservations.Status DB CHECK constraint: 'Pending', 'Confirmed', 'CheckedIn', 'CheckedOut', 'Cancelled'
    Pending,
    Confirmed,
    CheckedIn,
    CheckedOut,
    Cancelled
}
