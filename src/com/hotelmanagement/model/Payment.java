package com.hotelmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private int invoiceID;
    private BigDecimal amount;
    private int paymentMethodID;
    private LocalDateTime paymentDate;
    private String referenceNumber;
    private String notes;
    private Integer processedBy;
    private LocalDateTime createdAt;

    public Payment() {}

    public Payment(int paymentID, int invoiceID, BigDecimal amount, int paymentMethodID) {
        this.paymentID = paymentID;
        this.invoiceID = invoiceID;
        this.amount = amount;
        this.paymentMethodID = paymentMethodID;
    }

    public int getPaymentID() { return paymentID; }
    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }

    public int getInvoiceID() { return invoiceID; }
    public void setInvoiceID(int invoiceID) { this.invoiceID = invoiceID; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public int getPaymentMethodID() { return paymentMethodID; }
    public void setPaymentMethodID(int paymentMethodID) { this.paymentMethodID = paymentMethodID; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getProcessedBy() { return processedBy; }
    public void setProcessedBy(Integer processedBy) { this.processedBy = processedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
