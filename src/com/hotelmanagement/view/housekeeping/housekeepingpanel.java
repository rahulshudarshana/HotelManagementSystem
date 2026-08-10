package com.hotelmanagement.view.housekeeping;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class housekeepingpanel extends JPanel {

    private final JTextField txtTaskID = new JTextField();
    private final JComboBox<String> cmbRoomNo = new JComboBox<>();
    private final JComboBox<String> cmbAssignedEmployee = new JComboBox<>();
    private final JComboBox<String> cmbTaskType = new JComboBox<>();
    private final JComboBox<String> cmbPriority = new JComboBox<>();
    private final JComboBox<String> cmbStatus = new JComboBox<>();
    private final JDateChooser dpScheduledDate = new JDateChooser();
    private final JDateChooser dpCompletionDate = new JDateChooser();
    private final JTextArea txtNotes = new JTextArea(4, 20);
    private final JScrollPane notesScroll = new JScrollPane(txtNotes);
    private final JTextField txtSearch = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnAssignTask = new JButton("Assign Task");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnCompleteTask = new JButton("Complete Task");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnClear = new JButton("Clear");
    private final JButton btnBack = new JButton("Back");
    private final JTable table = new JTable();
    private final JScrollPane scroll = new JScrollPane(table);

    private final JPanel header;
    private final JPanel body;
    private final JPanel formCard;

    public housekeepingpanel() {
        cmbRoomNo.setEditable(true);
        cmbAssignedEmployee.setEditable(true);
        cmbTaskType.setModel(new DefaultComboBoxModel<>(new String[]{"Cleaning", "Deep Cleaning", "Maintenance Check", "Room Inspection", "Laundry"}));
        cmbPriority.setModel(new DefaultComboBoxModel<>(new String[]{"Low", "Normal", "High", "Urgent"}));
        cmbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Pending", "In Progress", "Completed", "Cancelled"}));
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        table.setModel(new DefaultTableModel(new String[]{"ID", "Room", "Type", "Priority", "Status", "Employee", "Date"}, 0));
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

        JLabel title = new JLabel("Housekeeping Management");
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

        addField(fields, c, "Task id:", txtTaskID);
        addField(fields, c, "Room number:", cmbRoomNo);
        addField(fields, c, "Assigned Employee:", cmbAssignedEmployee);
        addField(fields, c, "Task type:", cmbTaskType);
        addField(fields, c, "Priority:", cmbPriority);
        addField(fields, c, "Task status:", cmbStatus);
        addField(fields, c, "Scheduled date:", dpScheduledDate);
        addField(fields, c, "Completion date:", dpCompletionDate);
        addNotesField(fields, c, "Notes:", notesScroll);

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

    private void addNotesField(JPanel fields, GridBagConstraints c, String label, JComponent field) {
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;
        fields.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(new Dimension(190, 84));
        fields.add(field, c);
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setOpaque(false);
        p.add(btnAssignTask);
        p.add(btnUpdate);
        p.add(btnCompleteTask);
        p.add(btnDelete);
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

    public JButton getBtnAssignTask() { return btnAssignTask; }
    public JButton getBtnBack() { return btnBack; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnCompleteTask() { return btnCompleteTask; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JComboBox<String> getCmbPriority() { return cmbPriority; }
    public JComboBox<String> getCmbStatus() { return cmbStatus; }
    public JComboBox<String> getCmbAssignedEmployee() { return cmbAssignedEmployee; }
    public JComboBox<String> getCmbRoomNo() { return cmbRoomNo; }
    public JComboBox<String> getCmbTaskType() { return cmbTaskType; }
    public JDateChooser getDpScheduledDate() { return dpScheduledDate; }
    public JDateChooser getDpCompletionDate() { return dpCompletionDate; }
    public JTable getTable() { return table; }
    public JTextArea getNotesArea() { return txtNotes; }
    public JTextField getTxtSearch() { return txtSearch; }
    public JTextField getTxtTaskID() { return txtTaskID; }
}
