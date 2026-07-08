package com.hotelmanagement.service;

import com.hotelmanagement.dao.BillingDAO;
import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    public int createInvoice(Bill bill) throws SQLException {
        if (bill.getInvoiceNumber() == null || bill.getInvoiceNumber().trim().isEmpty()) {
            bill.setInvoiceNumber(generateInvoiceNumber());
        }
        BigDecimal total = bill.getRoomCharges().add(bill.getAdditionalCharges());
        BigDecimal discountAmount = total.multiply(bill.getDiscount().divide(BigDecimal.valueOf(100)));
        BigDecimal afterDiscount = total.subtract(discountAmount);
        BigDecimal taxAmount = afterDiscount.multiply(bill.getTax().divide(BigDecimal.valueOf(100)));
        BigDecimal finalTotal = afterDiscount.add(taxAmount);

        bill.setTotalAmount(finalTotal);
        bill.setAmountPaid(BigDecimal.ZERO);
        bill.setBalance(finalTotal);
        bill.setStatus(PaymentStatus.Pending);
        bill.setIssuedDate(LocalDateTime.now());

        return billingDAO.insertInvoice(bill);
    }

    public void updateInvoice(Bill bill) throws SQLException {
        billingDAO.updateInvoice(bill);
    }

    public void deleteInvoice(int id) throws SQLException {
        billingDAO.deleteInvoice(id);
    }

    private String generateInvoiceNumber() {
        return "INV-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + System.currentTimeMillis() % 10000;
    }
}
