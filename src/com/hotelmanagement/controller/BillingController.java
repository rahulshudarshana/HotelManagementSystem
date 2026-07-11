package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.Payment;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.enums.PaymentStatus;
import com.hotelmanagement.service.BillingService;
import com.hotelmanagement.service.PaymentService;
import com.hotelmanagement.service.ReservationService;
import com.hotelmanagement.util.SessionManager;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.billing.billingpanel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class BillingController {
    private final billingpanel view;
    private final BillingService billingService;
    private final PaymentService paymentService;

    public BillingController(billingpanel view) {
        this.view = view;
        this.billingService = new BillingService();
        this.paymentService = new PaymentService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnCalculate().addActionListener(e -> calculateTotal());
        view.getBtnProcessPayment().addActionListener(e -> processPayment());
        view.getBtnSearch().addActionListener(e -> search());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
        view.getBtnPrintInvoice().addActionListener(e ->
            javax.swing.JOptionPane.showMessageDialog(view, "Print functionality is not available in this version."));
    }

    private void calculateTotal() {
        try {
            String reservationIDStr = view.getTxtReservation().getText().trim();
            if (reservationIDStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Please enter a Reservation ID.");
                return;
            }
            int reservationID = Integer.parseInt(reservationIDStr);

            ReservationService reservationService = new ReservationService();
            Reservation reservation = reservationService.getReservationById(reservationID);
            if (reservation == null) {
                javax.swing.JOptionPane.showMessageDialog(view, "Reservation ID not found.");
                return;
            }

            String roomChargesText = view.getTxtRoomCharges().getText().trim();
            if (roomChargesText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Room charges is required.");
                return;
            }
            BigDecimal roomCharges = new BigDecimal(roomChargesText);
            BigDecimal extra = view.getTxtExtraCharges().getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(view.getTxtExtraCharges().getText().trim());
            BigDecimal discountPct = view.getTxtDiscount().getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(view.getTxtDiscount().getText().trim());
            BigDecimal taxPct = view.getTxtTax().getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(view.getTxtTax().getText().trim());

            Bill bill = new Bill();
            bill.setReservationID(reservationID);
            bill.setGuestID(reservation.getGuestID());
            bill.setRoomCharges(roomCharges);
            bill.setAdditionalCharges(extra);
            bill.setDiscount(discountPct);
            bill.setTax(taxPct);
            bill.setCreatedBy(SessionManager.getInstance().getCurrentUserId());

            int invoiceID = billingService.createInvoice(bill);
            Bill saved = billingService.getInvoiceById(invoiceID);

            view.getTxtTotalAmount().setText(saved.getTotalAmount().setScale(2, RoundingMode.HALF_UP).toString());
            view.getTxtAmountPaid().setText(saved.getAmountPaid().setScale(2, RoundingMode.HALF_UP).toString());
            view.getTxtBalance().setText(saved.getBalance().setScale(2, RoundingMode.HALF_UP).toString());
            view.getTxtSearchID().setText(String.valueOf(invoiceID));

            loadTable();
            javax.swing.JOptionPane.showMessageDialog(view, "Invoice #" + saved.getInvoiceNumber() + " created successfully.");
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Invalid input. Please check the values.");
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, "Database error creating invoice", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void processPayment() {
        try {
            String invoiceIDStr = view.getTxtSearchID().getText().trim();
            String amountStr = view.getTxtPayment().getText().trim();
            if (invoiceIDStr.isEmpty() || amountStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "Invoice ID and payment amount are required.");
                return;
            }
            Payment payment = new Payment();
            payment.setInvoiceID(Integer.parseInt(invoiceIDStr));
            payment.setAmount(new BigDecimal(amountStr));
            payment.setPaymentMethodID(view.getCmbPaymentMethod().getSelectedIndex() + 1);
            paymentService.recordPayment(payment);
            loadTable();
            javax.swing.JOptionPane.showMessageDialog(view, "Payment processed successfully.");
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, "Database error processing payment", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void search() {
        try {
            String keyword = view.getTxtSearchID().getText().trim();
            populateTable(billingService.searchInvoices(keyword));
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, "Failed to search invoices", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            populateTable(billingService.getAllInvoices());
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, "Failed to load invoices", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<Bill> invoices) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Invoice No", "Reservation", "Total", "Paid", "Balance", "Status"}, 0);
        for (Bill b : invoices) {
            model.addRow(new Object[]{b.getInvoiceID(), b.getInvoiceNumber(), b.getReservationID(),
                b.getTotalAmount(), b.getAmountPaid(), b.getBalance(), b.getStatus().name()});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtSearchID().setText("");
        view.getTxtRoomNo().setText("");
        view.getTxtGuestName().setText("");
        view.getTxtReservation().setText("");
        view.getTxtRoomCharges().setText("");
        view.getTxtExtraCharges().setText("");
        view.getTxtDiscount().setText("");
        view.getTxtTax().setText("");
        view.getTxtTotalAmount().setText("");
        view.getTxtPayment().setText("");
        view.getTxtAmountPaid().setText("");
        view.getTxtBalance().setText("");
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
