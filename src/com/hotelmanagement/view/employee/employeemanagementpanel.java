package com.hotelmanagement.view.employee;

import com.hotelmanagement.util.UIUtils;
import com.toedter.calendar.JDateChooser;
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

public class employeemanagementpanel extends JPanel {

    private final JTextField txtEmployeeID = new JTextField();
    private final JTextField txtFirstName = new JTextField();
    private final JTextField txtLastName = new JTextField();
    private final JComboBox<String> cmbGender = new JComboBox<>();
    private final JDateChooser dpDateOfBirth = new JDateChooser();
    private final JTextField txtNIC = new JTextField();
    private final JTextField txtPhone = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JComboBox<String> cmbDepartment = new JComboBox<>();
    private final JTextField txtPosition = new JTextField();
    private final JTextField txtSalary = new JTextField();
    private final JDateChooser dpHireDate = new JDateChooser();
    private final JComboBox<String> cmbEmploymentStatus = new JComboBox<>();
    private final JTextField txtSearch = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnAdd = new JButton("Add");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnClear = new JButton("Clear");
    private final JButton btnBack = new JButton("Back");
    private final JTable table = new JTable();
    private final JScrollPane scroll = new JScrollPane(table);

    private final JPanel header;
    private final JPanel body;
    private final JPanel formCard;

    public employeemanagementpanel() {
        cmbGender.setModel(new DefaultComboBoxModel<>(new String[]{"Male", "Female", "Other"}));
        cmbDepartment.setModel(new DefaultComboBoxModel<>(new String[]{"Management", "Front Office", "Housekeeping", "Finance", "Food & Beverage", "Security"}));
        cmbEmploymentStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Active", "OnLeave", "Terminated", "Suspended"}));
        table.setModel(new DefaultTableModel(new String[]{"ID", "Emp No", "First Name", "Last Name", "NIC", "Phone", "Department"}, 0));
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

        JLabel title = new JLabel("Employee Management");
        title.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD, 20f));

        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setOpaque(false);
        searchBox.add(new JLabel("Search"), BorderLayout.WEST);
        txtSearch.setPreferredSize(new Dimension(220, 32));
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
        c.insets = new Insets(5, 0, 5, 0);
        c.anchor = GridBagConstraints.WEST;

        addField(fields, c, "Employee id:", txtEmployeeID);
        addField(fields, c, "First name:", txtFirstName);
        addField(fields, c, "Last name:", txtLastName);
        addField(fields, c, "Gender:", cmbGender);
        addField(fields, c, "Date of birth:", dpDateOfBirth);
        addField(fields, c, "NIC:", txtNIC);
        addField(fields, c, "Phone:", txtPhone);
        addField(fields, c, "Email:", txtEmail);
        addField(fields, c, "Department:", cmbDepartment);
        addField(fields, c, "Position:", txtPosition);
        addField(fields, c, "Salary:", txtSalary);
        addField(fields, c, "Hire date:", dpHireDate);
        addField(fields, c, "Employee status:", cmbEmploymentStatus);

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
        p.add(btnAdd);
        p.add(btnUpdate);
        p.add(btnDelete);
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

    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnBack() { return btnBack; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JComboBox<String> getCmbDepartment() { return cmbDepartment; }
    public JComboBox<String> getCmbEmploymentStatus() { return cmbEmploymentStatus; }
    public JComboBox<String> getCmbGender() { return cmbGender; }
    public JDateChooser getDpHireDate() { return dpHireDate; }
    public JDateChooser getDpDateOfBirth() { return dpDateOfBirth; }
    public JTable getTable() { return table; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtEmployeeID() { return txtEmployeeID; }
    public JTextField getTxtFirstName() { return txtFirstName; }
    public JTextField getTxtLastName() { return txtLastName; }
    public JTextField getTxtNIC() { return txtNIC; }
    public JTextField getTxtPhone() { return txtPhone; }
    public JTextField getTxtPosition() { return txtPosition; }
    public JTextField getTxtSalary() { return txtSalary; }
    public JTextField getTxtSearch() { return txtSearch; }
}
