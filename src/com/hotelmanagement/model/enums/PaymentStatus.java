package com.hotelmanagement.model.enums;

public enum PaymentStatus {
    // Maps to Invoices.Status DB CHECK constraint: 'Pending', 'Paid', 'PartiallyPaid', 'Cancelled', 'Refunded'
    Pending,
    Paid,
    PartiallyPaid,
    Cancelled,
    Refunded
}
