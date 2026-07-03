package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.enums.PaymentStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    public Bill getInvoiceById(int invoiceID) throws SQLException {
        String sql = "SELECT * FROM Invoices WHERE InvoiceID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapBill(rs);
                }
            }
        }
        return null;
    }

    public Bill getInvoiceByNumber(String invoiceNumber) throws SQLException {
        String sql = "SELECT * FROM Invoices WHERE InvoiceNumber = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, invoiceNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapBill(rs);
                }
            }
        }
        return null;
    }

    public Bill getInvoiceByReservation(int reservationID) throws SQLException {
        String sql = "SELECT * FROM Invoices WHERE ReservationID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapBill(rs);
                }
            }
        }
        return null;
    }

    public List<Bill> getInvoicesByGuest(int guestID) throws SQLException {
        List<Bill> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoices WHERE GuestID = ? ORDER BY IssuedDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapBill(rs));
                }
            }
        }
        return invoices;
    }

    public List<Bill> getInvoicesByStatus(PaymentStatus status) throws SQLException {
        List<Bill> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoices WHERE Status = ? ORDER BY IssuedDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapBill(rs));
                }
            }
        }
        return invoices;
    }

    public List<Bill> getAllInvoices() throws SQLException {
        List<Bill> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoices ORDER BY IssuedDate DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invoices.add(mapBill(rs));
            }
        }
        return invoices;
    }

    public int insertInvoice(Bill bill) throws SQLException {
        String sql = "INSERT INTO Invoices (InvoiceNumber, ReservationID, GuestID, RoomCharges, AdditionalCharges, Discount, Tax, TotalAmount, AmountPaid, Balance, Status, DueDate, Notes, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bill.getInvoiceNumber());
            stmt.setInt(2, bill.getReservationID());
            stmt.setInt(3, bill.getGuestID());
            stmt.setBigDecimal(4, bill.getRoomCharges());
            stmt.setBigDecimal(5, bill.getAdditionalCharges());
            stmt.setBigDecimal(6, bill.getDiscount());
            stmt.setBigDecimal(7, bill.getTax());
            stmt.setBigDecimal(8, bill.getTotalAmount());
            stmt.setBigDecimal(9, bill.getAmountPaid());
            stmt.setBigDecimal(10, bill.getBalance());
            stmt.setString(11, bill.getStatus().name());
            stmt.setObject(12, bill.getDueDate());
            stmt.setString(13, bill.getNotes());
            stmt.setObject(14, bill.getCreatedBy());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateInvoice(Bill bill) throws SQLException {
        String sql = "UPDATE Invoices SET RoomCharges = ?, AdditionalCharges = ?, Discount = ?, Tax = ?, TotalAmount = ?, AmountPaid = ?, Balance = ?, Status = ?, DueDate = ?, Notes = ? WHERE InvoiceID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, bill.getRoomCharges());
            stmt.setBigDecimal(2, bill.getAdditionalCharges());
            stmt.setBigDecimal(3, bill.getDiscount());
            stmt.setBigDecimal(4, bill.getTax());
            stmt.setBigDecimal(5, bill.getTotalAmount());
            stmt.setBigDecimal(6, bill.getAmountPaid());
            stmt.setBigDecimal(7, bill.getBalance());
            stmt.setString(8, bill.getStatus().name());
            stmt.setObject(9, bill.getDueDate());
            stmt.setString(10, bill.getNotes());
            stmt.setInt(11, bill.getInvoiceID());
            stmt.executeUpdate();
        }
    }

    public void updateInvoicePayment(int invoiceID, BigDecimal amountPaid, BigDecimal balance, PaymentStatus status) throws SQLException {
        String sql = "UPDATE Invoices SET AmountPaid = ?, Balance = ?, Status = ? WHERE InvoiceID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, amountPaid);
            stmt.setBigDecimal(2, balance);
            stmt.setString(3, status.name());
            stmt.setInt(4, invoiceID);
            stmt.executeUpdate();
        }
    }

    public void deleteInvoice(int invoiceID) throws SQLException {
        String sql = "DELETE FROM Invoices WHERE InvoiceID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceID);
            stmt.executeUpdate();
        }
    }

    private Bill mapBill(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setInvoiceID(rs.getInt("InvoiceID"));
        bill.setInvoiceNumber(rs.getString("InvoiceNumber"));
        bill.setReservationID(rs.getInt("ReservationID"));
        bill.setGuestID(rs.getInt("GuestID"));
        bill.setRoomCharges(rs.getBigDecimal("RoomCharges"));
        bill.setAdditionalCharges(rs.getBigDecimal("AdditionalCharges"));
        bill.setDiscount(rs.getBigDecimal("Discount"));
        bill.setTax(rs.getBigDecimal("Tax"));
        bill.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        bill.setAmountPaid(rs.getBigDecimal("AmountPaid"));
        bill.setBalance(rs.getBigDecimal("Balance"));
        bill.setStatus(PaymentStatus.valueOf(rs.getString("Status")));
        if (rs.getTimestamp("IssuedDate") != null) {
            bill.setIssuedDate(rs.getTimestamp("IssuedDate").toLocalDateTime());
        }
        if (rs.getDate("DueDate") != null) {
            bill.setDueDate(rs.getDate("DueDate").toLocalDate());
        }
        bill.setNotes(rs.getString("Notes"));
        bill.setCreatedBy(rs.getObject("CreatedBy", Integer.class));
        if (rs.getTimestamp("CreatedAt") != null) {
            bill.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return bill;
    }
}
