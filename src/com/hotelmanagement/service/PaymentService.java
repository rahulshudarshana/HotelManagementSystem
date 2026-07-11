package com.hotelmanagement.service;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.dao.BillingDAO;
import com.hotelmanagement.dao.PaymentDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.Payment;
import com.hotelmanagement.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentService {
    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());
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

    public int recordPayment(Payment payment) {
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Payment amount must be greater than zero.");
        }
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            int paymentID = paymentDAO.insertPayment(payment, conn);

            // Lock the invoice row to prevent lost updates
            Bill invoice = billingDAO.getInvoiceByIdForUpdate(payment.getInvoiceID(), conn);
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
                billingDAO.updateInvoicePayment(payment.getInvoiceID(), newAmountPaid, newBalance, newStatus, conn);
            }

            conn.commit();
            return paymentID;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Payment recording failed", e);
            throw new DataAccessException("Payment recording failed due to a database error.", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to restore auto-commit", e);
                }
                try { conn.close(); } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to close connection", e);
                }
            }
        }
    }

    public void deletePayment(int id) throws SQLException {
        paymentDAO.deletePayment(id);
    }
}
