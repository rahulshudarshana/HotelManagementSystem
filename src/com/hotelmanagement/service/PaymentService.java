package com.hotelmanagement.service;

import com.hotelmanagement.dao.BillingDAO;
import com.hotelmanagement.dao.PaymentDAO;
import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.Payment;
import com.hotelmanagement.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO;
    private final BillingDAO billingDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.billingDAO = new BillingDAO();
    }

    public Payment getPaymentById(int id) throws SQLException {
        return paymentDAO.getPaymentById(id);
    }

    public List<Payment> getPaymentsByInvoice(int invoiceID) throws SQLException {
        return paymentDAO.getPaymentsByInvoice(invoiceID);
    }

    public List<Payment> getAllPayments() throws SQLException {
        return paymentDAO.getAllPayments();
    }

    public int recordPayment(Payment payment) throws SQLException {
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        int paymentID = paymentDAO.insertPayment(payment);
        Bill invoice = billingDAO.getInvoiceById(payment.getInvoiceID());
        if (invoice != null) {
            BigDecimal newAmountPaid = invoice.getAmountPaid().add(payment.getAmount());
            BigDecimal newBalance = invoice.getTotalAmount().subtract(newAmountPaid);
            PaymentStatus newStatus;
            if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
                newStatus = PaymentStatus.Paid;
                newBalance = BigDecimal.ZERO;
            } else {
                newStatus = PaymentStatus.PartiallyPaid;
            }
            billingDAO.updateInvoicePayment(payment.getInvoiceID(), newAmountPaid, newBalance, newStatus);
        }
        return paymentID;
    }

    public void deletePayment(int id) throws SQLException {
        paymentDAO.deletePayment(id);
    }
}
