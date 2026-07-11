package com.hotelmanagement.service;

import com.hotelmanagement.dao.BillingDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillingService {
    private static final Logger LOGGER = Logger.getLogger(BillingService.class.getName());
    private final BillingDAO billingDAO;

    public BillingService() {
        this.billingDAO = new BillingDAO();
    }

    public Bill getInvoiceById(int id) throws SQLException {
        return billingDAO.getInvoiceById(id);
    }

    public Bill getInvoiceByNumber(String number) throws SQLException {
        return billingDAO.getInvoiceByNumber(number);
    }

    public Bill getInvoiceByReservation(int reservationID) throws SQLException {
        return billingDAO.getInvoiceByReservation(reservationID);
    }

    public List<Bill> getInvoicesByGuest(int guestID) throws SQLException {
        return billingDAO.getInvoicesByGuest(guestID);
    }

    public List<Bill> getInvoicesByStatus(PaymentStatus status) throws SQLException {
        return billingDAO.getInvoicesByStatus(status);
    }

    public List<Bill> getAllInvoices() throws SQLException {
        return billingDAO.getAllInvoices();
    }

    public List<Bill> searchInvoices(String keyword) throws SQLException {
        return billingDAO.searchInvoices(keyword);
    }

    public int createInvoice(Bill bill) {
        if (bill.getInvoiceNumber() == null || bill.getInvoiceNumber().trim().isEmpty()) {
            bill.setInvoiceNumber(generateInvoiceNumber());
        }
        try {
            BigDecimal total = bill.getRoomCharges().add(bill.getAdditionalCharges());
            BigDecimal discountAmount = total.multiply(bill.getDiscount().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
            BigDecimal afterDiscount = total.subtract(discountAmount);
            BigDecimal taxAmount = afterDiscount.multiply(bill.getTax().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
            BigDecimal finalTotal = afterDiscount.add(taxAmount);

            bill.setTotalAmount(finalTotal);
            bill.setAmountPaid(BigDecimal.ZERO);
            bill.setBalance(finalTotal);
            bill.setStatus(PaymentStatus.Pending);
            bill.setIssuedDate(LocalDateTime.now());

            return billingDAO.insertInvoice(bill);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Invoice creation failed", e);
            throw new DataAccessException("Invoice creation failed due to a database error.", e);
        }
    }

    public void updateInvoice(Bill bill) {
        try {
            billingDAO.updateInvoice(bill);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Invoice update failed", e);
            throw new DataAccessException("Invoice update failed due to a database error.", e);
        }
    }

    public void deleteInvoice(int id) {
        try {
            billingDAO.deleteInvoice(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Invoice deletion failed", e);
            throw new DataAccessException("Invoice deletion failed due to a database error.", e);
        }
    }

    private String generateInvoiceNumber() {
        return "INV-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + System.currentTimeMillis() % 10000;
    }
}
