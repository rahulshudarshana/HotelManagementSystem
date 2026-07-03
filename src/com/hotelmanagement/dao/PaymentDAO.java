package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.Payment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public Payment getPaymentById(int paymentID) throws SQLException {
        String sql = "SELECT * FROM Payments WHERE PaymentID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPayment(rs);
                }
            }
        }
        return null;
    }

    public List<Payment> getPaymentsByInvoice(int invoiceID) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE InvoiceID = ? ORDER BY PaymentDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapPayment(rs));
                }
            }
        }
        return payments;
    }

    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments ORDER BY PaymentDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                payments.add(mapPayment(rs));
            }
        }
        return payments;
    }

    public int insertPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payments (InvoiceID, Amount, PaymentMethodID, ReferenceNumber, Notes, ProcessedBy) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getInvoiceID());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setInt(3, payment.getPaymentMethodID());
            stmt.setString(4, payment.getReferenceNumber());
            stmt.setString(5, payment.getNotes());
            stmt.setObject(6, payment.getProcessedBy());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void deletePayment(int paymentID) throws SQLException {
        String sql = "DELETE FROM Payments WHERE PaymentID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentID);
            stmt.executeUpdate();
        }
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
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
