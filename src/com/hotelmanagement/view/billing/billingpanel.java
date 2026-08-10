package com.hotelmanagement.view.billing;

import com.hotelmanagement.util.UIUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class billingpanel extends JPanel {

    private final JTextField txtReservation = new JTextField();
    private final JTextField txtGuestName = new JTextField();
    private final JTextField txtRoomNo = new JTextField();
    private final JTextField txtRoomCharges = new JTextField();
    private final JTextField txtExtraCharges = new JTextField();
    private final JTextField txtDiscount = new JTextField();
    private final JTextField txtTax = new JTextField();
    private final JTextField txtTotalAmount = new JTextField();
    private final JTextField txtAmountPaid = new JTextField();
    private final JTextField txtBalance = new JTextField();
    private final JTextField txtPayment = new JTextField();
    private final JComboBox<String> cmbPaymentMethod = new JComboBox<>();
    private final JComboBox<String> cmbPaymentStatus = new JComboBox<>();
    private final JTextField txtSearchID = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnCalculate = new JButton("Calculation");
    private final JButton btnProcessPayment = new JButton("Process Payment");
    private final JButton btnPrintInvoice = new JButton("Print Invoice");
    private final JButton btnClear = new JButton("Clear");
    private final JButton btnBack = new JButton("Back");
    private final JButton btnUpdate = new JButton("Update");
    private final JTable table = new JTable();
    private final JScrollPane scroll = new JScrollPane(table);

    private final JPanel header;
    private final JPanel body;
    private final JPanel formCard;

    public billingpanel() {
        cmbPaymentMethod.setModel(new DefaultComboBoxModel<>(new String[]{"Cash", "Credit Card", "Debit Card", "Bank Transfer"}));
        cmbPaymentStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Pending", "Paid", "PartiallyPaid", "Cancelled", "Refunded"}));
        table.setModel(new DefaultTableModel(new String[]{"ID", "Invoice No", "Reservation", "Total", "Paid", "Balance", "Status"}, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        header = buildHeader();
        formCard = buildForm();
        body = buildBody();

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        UIUtils.applyTheme(this);
        style();
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout(16, 0));
        h.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel title = new JLabel("Billing & Payment");
        title.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD, 20f));

        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setOpaque(false);
        searchBox.add(new JLabel("Invoice ID"), BorderLayout.WEST);
        txtSearchID.setPreferredSize(new Dimension(200, 32));
        searchBox.add(txtSearchID, BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout(10, 0));
        east.setOpaque(false);
        east.add(searchBox, BorderLayout.CENTER);
        east.add(btnSearch, BorderLayout.EAST);

        h.add(title, BorderLayout.WEST);
        h.add(east, BorderLayout.EAST);
        return h;
    }

    private JPanel buildForm() {
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
        c.anchor = GridBagConstraints.WEST;

        addField(fields, c, "Reservation id:", txtReservation);
        addField(fields, c, "Guest name:", txtGuestName);
        addField(fields, c, "Room number:", txtRoomNo);
        addField(fields, c, "Room charges:", txtRoomCharges);
        addField(fields, c, "Extra charges:", txtExtraCharges);
        addField(fields, c, "Discount:", txtDiscount);
        addField(fields, c, "Tax:", txtTax);
        addField(fields, c, "Total amount:", txtTotalAmount);
        addField(fields, c, "Amount Paid:", txtAmountPaid);
        addField(fields, c, "Balance:", txtBalance);
        addField(fields, c, "Payment:", txtPayment);
        addField(fields, c, "Payment method:", cmbPaymentMethod);
        addField(fields, c, "Payment status:", cmbPaymentStatus);

        card.add(fields, BorderLayout.CENTER);
        card.add(buildButtons(), BorderLayout.SOUTH);
        return card;
    }

    private void addField(JPanel fields, GridBagConstraints c, String label, JComponent field) {
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        fields.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(new Dimension(190, 32));
        fields.add(field, c);
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setOpaque(false);
        p.add(btnCalculate);
        p.add(btnProcessPayment);
        p.add(btnPrintInvoice);
        p.add(btnUpdate);
        p.add(btnClear);
        p.add(btnBack);
        return p;
    }

    private JPanel buildBody() {
        JPanel b = new JPanel(new BorderLayout(16, 0));
        b.setBorder(BorderFactory.createEmptyBorder(16, 22, 22, 22));
        formCard.setPreferredSize(new Dimension(400, 0));
        b.add(formCard, BorderLayout.WEST);
        b.add(scroll, BorderLayout.CENTER);
        return b;
    }

    private void style() {
        setBackground(UIUtils.PANEL_BG);
        header.setBackground(Color.WHITE);
        body.setBackground(UIUtils.PANEL_BG);
        formCard.setBackground(Color.WHITE);
        formCard.putClientProperty("FlatLaf.style", "background: #FFFFFF");
    }

    public JButton getBtnCalculate() { return btnCalculate; }
    public JButton getBtnPrintInvoice() { return btnPrintInvoice; }
    public JButton getBtnProcessPayment() { return btnProcessPayment; }
    public JButton getBtnBack() { return btnBack; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JComboBox<String> getCmbPaymentStatus() { return cmbPaymentStatus; }
    public JComboBox<String> getCmbPaymentMethod() { return cmbPaymentMethod; }
    public JTable getTable() { return table; }
    public JTextField getTxtAmountPaid() { return txtAmountPaid; }
    public JTextField getTxtBalance() { return txtBalance; }
    public JTextField getTxtDiscount() { return txtDiscount; }
    public JTextField getTxtExtraCharges() { return txtExtraCharges; }
    public JTextField getTxtGuestName() { return txtGuestName; }
    public JTextField getTxtPayment() { return txtPayment; }
    public JTextField getTxtReservation() { return txtReservation; }
    public JTextField getTxtRoomCharges() { return txtRoomCharges; }
    public JTextField getTxtRoomNo() { return txtRoomNo; }
    public JTextField getTxtSearchID() { return txtSearchID; }
    public JTextField getTxtTax() { return txtTax; }
    public JTextField getTxtTotalAmount() { return txtTotalAmount; }
}
