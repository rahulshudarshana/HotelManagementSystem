package com.hotelmanagement.controller;

import com.hotelmanagement.model.Bill;
import com.hotelmanagement.model.Payment;
import com.hotelmanagement.model.enums.PaymentStatus;
import com.hotelmanagement.service.BillingService;
import com.hotelmanagement.service.PaymentService;
import com.hotelmanagement.view.billing.billingpanel;
import java.math.BigDecimal;
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
    }

    private void calculateTotal() {
        try {
            BigDecimal roomCharges = new BigDecimal(view.getTxtRoomCharges().getText().trim());
            BigDecimal extra = view.getTxtExtraCharges().getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(view.getTxtExtraCharges().getText().trim());
            BigDecimal discount = view.getTxtDiscount().getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(view.getTxtDiscount().getText().trim());
            BigDecimal tax = view.getTxtTax().getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(view.getTxtTax().getText().trim());

            BigDecimal subtotal = roomCharges.add(extra);
            BigDecimal discountAmount = subtotal.multiply(discount).divide(BigDecimal.valueOf(100));
            BigDecimal afterDiscount = subtotal.subtract(discountAmount);
            BigDecimal taxAmount = afterDiscount.multiply(tax).divide(BigDecimal.valueOf(100));
            BigDecimal total = afterDiscount.add(taxAmount);

            view.getTxtTotalAmount().setText(total.toString());
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Invalid input. Please check the values.");
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
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void search() {
        try {
            String keyword = view.getTxtSearchID().getText().trim();
            List<Bill> all = billingService.getAllInvoices();
            List<Bill> filtered = all.stream()
                .filter(b -> String.valueOf(b.getInvoiceID()).contains(keyword) || (b.getInvoiceNumber() != null && b.getInvoiceNumber().contains(keyword)))
                .toList();
            populateTable(filtered);
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        try {
            populateTable(billingService.getAllInvoices());
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
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
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
