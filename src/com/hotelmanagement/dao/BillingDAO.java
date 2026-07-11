package com.hotelmanagement.dao;

import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.enums.PaymentStatus;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class BillingDAO extends BaseDAO<Bill> {

    private static final Logger LOGGER = Logger.getLogger(BillingDAO.class.getName());

    private static final String COLUMNS = "InvoiceID, InvoiceNumber, ReservationID, GuestID, RoomCharges, AdditionalCharges, Discount, Tax, TotalAmount, AmountPaid, Balance, Status, IssuedDate, DueDate, Notes, CreatedBy, CreatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM Invoices WHERE InvoiceID = ?";
    private static final String SQL_BY_ID_FOR_UPDATE = "SELECT " + COLUMNS + " FROM Invoices WHERE InvoiceID = ? FOR UPDATE";
    private static final String SQL_BY_NUMBER = "SELECT " + COLUMNS + " FROM Invoices WHERE InvoiceNumber = ?";
    private static final String SQL_BY_RESERVATION = "SELECT " + COLUMNS + " FROM Invoices WHERE ReservationID = ?";
    private static final String SQL_BY_GUEST = "SELECT " + COLUMNS + " FROM Invoices WHERE GuestID = ? ORDER BY IssuedDate DESC";
    private static final String SQL_BY_STATUS = "SELECT " + COLUMNS + " FROM Invoices WHERE Status = ? ORDER BY IssuedDate DESC";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM Invoices ORDER BY IssuedDate DESC";
    private static final String SQL_INSERT = "INSERT INTO Invoices (InvoiceNumber, ReservationID, GuestID, RoomCharges, AdditionalCharges, Discount, Tax, TotalAmount, AmountPaid, Balance, Status, DueDate, Notes, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Invoices SET RoomCharges = ?, AdditionalCharges = ?, Discount = ?, Tax = ?, TotalAmount = ?, AmountPaid = ?, Balance = ?, Status = ?, DueDate = ?, Notes = ? WHERE InvoiceID = ?";
    private static final String SQL_UPDATE_PAYMENT = "UPDATE Invoices SET AmountPaid = ?, Balance = ?, Status = ? WHERE InvoiceID = ?";
    private static final String SQL_DELETE = "DELETE FROM Invoices WHERE InvoiceID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM Invoices WHERE CAST(InvoiceID AS CHAR) LIKE ? OR InvoiceNumber LIKE ? ORDER BY IssuedDate DESC";

    public Bill getInvoiceById(int invoiceID) throws SQLException {
        return findOne(SQL_BY_ID, invoiceID);
    }

    public Bill getInvoiceById(int invoiceID, Connection conn) throws SQLException {
        return findOne(SQL_BY_ID, conn, invoiceID);
    }

    public Bill getInvoiceByIdForUpdate(int invoiceID, Connection conn) throws SQLException {
        return findOne(SQL_BY_ID_FOR_UPDATE, conn, invoiceID);
    }

    public Bill getInvoiceByNumber(String invoiceNumber) throws SQLException {
        return findOne(SQL_BY_NUMBER, invoiceNumber);
    }

    public Bill getInvoiceByReservation(int reservationID) throws SQLException {
        return findOne(SQL_BY_RESERVATION, reservationID);
    }

    public List<Bill> getInvoicesByGuest(int guestID) throws SQLException {
        return findAll(SQL_BY_GUEST, guestID);
    }

    public List<Bill> getInvoicesByStatus(PaymentStatus status) throws SQLException {
        return findAll(SQL_BY_STATUS, status);
    }

    public List<Bill> getAllInvoices() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<Bill> searchInvoices(String keyword) throws SQLException {
        String pattern = "%" + keyword + "%";
        return findAll(SQL_SEARCH, pattern, pattern);
    }

    public int insertInvoice(Bill bill) throws SQLException {
        return insert(SQL_INSERT,
            bill.getInvoiceNumber(), bill.getReservationID(), bill.getGuestID(),
            bill.getRoomCharges(), bill.getAdditionalCharges(), bill.getDiscount(),
            bill.getTax(), bill.getTotalAmount(), bill.getAmountPaid(), bill.getBalance(),
            bill.getStatus(), bill.getDueDate(), bill.getNotes(), bill.getCreatedBy());
    }

    public void updateInvoice(Bill bill) throws SQLException {
        update(SQL_UPDATE,
            bill.getRoomCharges(), bill.getAdditionalCharges(), bill.getDiscount(),
            bill.getTax(), bill.getTotalAmount(), bill.getAmountPaid(), bill.getBalance(),
            bill.getStatus(), bill.getDueDate(), bill.getNotes(), bill.getInvoiceID());
    }

    public void updateInvoicePayment(int invoiceID, BigDecimal amountPaid, BigDecimal balance, PaymentStatus status) throws SQLException {
        update(SQL_UPDATE_PAYMENT, amountPaid, balance, status, invoiceID);
    }

    public void updateInvoicePayment(int invoiceID, BigDecimal amountPaid, BigDecimal balance, PaymentStatus status, Connection conn) throws SQLException {
        update(SQL_UPDATE_PAYMENT, conn, amountPaid, balance, status, invoiceID);
    }

    public void deleteInvoice(int invoiceID) throws SQLException {
        delete(SQL_DELETE, invoiceID);
    }

    @Override
    protected Bill mapRow(ResultSet rs) throws SQLException {
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
