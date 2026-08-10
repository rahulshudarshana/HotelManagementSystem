package com.hotelmanagement.view.checkout;

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

public class checkoutpanel extends JPanel {

    private final JTextField txtGuestName = new JTextField();
    private final JTextField txtRoomNo = new JTextField();
    private final JComboBox<String> cmbRoomType = new JComboBox<>();
    private final JTextField txtRoomCharges = new JTextField();
    private final JTextField txtAdditionalCharges = new JTextField();
    private final JTextField txtTotalAmount = new JTextField();
    private final JTextField txtSearch = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnCalculateBill = new JButton("Calculate Bill");
    private final JButton btnCheckOut = new JButton("Check-out");
    private final JButton btnClear = new JButton("Clear");
    private final JButton btnBack = new JButton("Back");
    private final JButton btnUpdate = new JButton("Update");
    private final JTable table = new JTable();
    private final JScrollPane scroll = new JScrollPane(table);

    private final JPanel header;
    private final JPanel body;
    private final JPanel formCard;

    public checkoutpanel() {
        cmbRoomType.setModel(new DefaultComboBoxModel<>(new String[]{"Single", "Double", "Triple", "Suite", "Deluxe", "Penthouse"}));
        table.setModel(new DefaultTableModel(new String[]{"ID", "Reservation", "Guest", "Room", "Total"}, 0));
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

        JLabel title = new JLabel("Check-out");
        title.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD, 20f));

        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setOpaque(false);
        searchBox.add(new JLabel("Reservation ID"), BorderLayout.WEST);
        txtSearch.setPreferredSize(new Dimension(200, 32));
        searchBox.add(txtSearch, BorderLayout.CENTER);

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
        c.insets = new Insets(6, 0, 6, 0);
        c.anchor = GridBagConstraints.WEST;

        addField(fields, c, "Guest id:", txtGuestName);
        addField(fields, c, "Room number", txtRoomNo);
        addField(fields, c, "Room type:", cmbRoomType);
        addField(fields, c, "Room charges:", txtRoomCharges);
        addField(fields, c, "Additional charges:", txtAdditionalCharges);
        addField(fields, c, "Total Amount:", txtTotalAmount);

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
        p.add(btnCalculateBill);
        p.add(btnCheckOut);
        p.add(btnUpdate);
        p.add(btnClear);
        p.add(btnBack);
        return p;
    }

    private JPanel buildBody() {
        JPanel b = new JPanel(new BorderLayout(16, 0));
        b.setBorder(BorderFactory.createEmptyBorder(16, 22, 22, 22));
        formCard.setPreferredSize(new Dimension(360, 0));
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

    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnCalculateBill() { return btnCalculateBill; }
    public JButton getBtnCheckOut() { return btnCheckOut; }
    public JButton getBtnBack() { return btnBack; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JTable getTable() { return table; }
    public JTextField getTxtAdditionalCharges() { return txtAdditionalCharges; }
    public JTextField getTxtGuestName() { return txtGuestName; }
    public JTextField getTxtRoomCharges() { return txtRoomCharges; }
    public JTextField getTxtRoomNo() { return txtRoomNo; }
    public JComboBox<String> getCmbRoomType() { return cmbRoomType; }
    public JTextField getTxtSearch() { return txtSearch; }
    public JTextField getTxtTotalAmount() { return txtTotalAmount; }
}
