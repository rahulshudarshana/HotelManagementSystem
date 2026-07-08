package com.hotelmanagement.dao;

import com.hotelmanagement.model.Payment;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class PaymentDAO extends BaseDAO<Payment> {

    private static final Logger LOGGER = Logger.getLogger(PaymentDAO.class.getName());

    private static final String COLUMNS = "PaymentID, InvoiceID, Amount, PaymentMethodID, PaymentDate, ReferenceNumber, Notes, ProcessedBy, CreatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM Payments WHERE PaymentID = ?";
    private static final String SQL_BY_INVOICE = "SELECT " + COLUMNS + " FROM Payments WHERE InvoiceID = ? ORDER BY PaymentDate DESC";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM Payments ORDER BY PaymentDate DESC";
    private static final String SQL_INSERT = "INSERT INTO Payments (InvoiceID, Amount, PaymentMethodID, ReferenceNumber, Notes, ProcessedBy) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM Payments WHERE PaymentID = ?";

    public Payment getPaymentById(int paymentID) throws SQLException {
        return findOne(SQL_BY_ID, paymentID);
    }

    public List<Payment> getPaymentsByInvoice(int invoiceID) throws SQLException {
        return findAll(SQL_BY_INVOICE, invoiceID);
    }

    public List<Payment> getAllPayments() throws SQLException {
        return findAll(SQL_ALL);
    }

    public int insertPayment(Payment payment) throws SQLException {
        return insert(SQL_INSERT,
            payment.getInvoiceID(), payment.getAmount(), payment.getPaymentMethodID(),
            payment.getReferenceNumber(), payment.getNotes(), payment.getProcessedBy());
    }

    public int insertPayment(Payment payment, Connection conn) throws SQLException {
        return insert(SQL_INSERT, conn,
            payment.getInvoiceID(), payment.getAmount(), payment.getPaymentMethodID(),
            payment.getReferenceNumber(), payment.getNotes(), payment.getProcessedBy());
    }

    public void deletePayment(int paymentID) throws SQLException {
        delete(SQL_DELETE, paymentID);
    }

    @Override
    protected Payment mapRow(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentID(rs.getInt("PaymentID"));
        payment.setInvoiceID(rs.getInt("InvoiceID"));
        payment.setAmount(rs.getBigDecimal("Amount"));
        payment.setPaymentMethodID(rs.getInt("PaymentMethodID"));
        if (rs.getTimestamp("PaymentDate") != null) {
            payment.setPaymentDate(rs.getTimestamp("PaymentDate").toLocalDateTime());
        }
        payment.setReferenceNumber(rs.getString("ReferenceNumber"));
        payment.setNotes(rs.getString("Notes"));
        payment.setProcessedBy(rs.getObject("ProcessedBy", Integer.class));
        if (rs.getTimestamp("CreatedAt") != null) {
            payment.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return payment;
    }
}
