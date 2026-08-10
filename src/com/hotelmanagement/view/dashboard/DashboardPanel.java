package com.hotelmanagement.view.dashboard;

import com.hotelmanagement.util.SessionManager;
import com.hotelmanagement.util.UIUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardPanel extends JPanel {

    private final JLabel labelTotalRooms = new JLabel("0", SwingConstants.CENTER);
    private final JLabel labelAvailable = new JLabel("0", SwingConstants.CENTER);
    private final JLabel labelOccupied = new JLabel("0", SwingConstants.CENTER);
    private final JLabel labelCheckIns = new JLabel("0", SwingConstants.CENTER);
    private final JLabel labelCheckOuts = new JLabel("0", SwingConstants.CENTER);
    private final JLabel labelRevenue = new JLabel("$0.00", SwingConstants.CENTER);
    private final JLabel labelLastUpdated = new JLabel("--");
    private final JButton btnRefresh = new JButton("Refresh");

    private final JPanel header;
    private final JPanel grid;
    private final JPanel[] cards = new JPanel[6];

    public DashboardPanel() {
        header = buildHeader();
        grid = buildStatsGrid();
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        UIUtils.applyTheme(this);
        styleComponents();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);
        String username = SessionManager.getInstance().isLoggedIn()
                ? SessionManager.getInstance().getCurrentUser().getUsername() : "";
        JLabel welcome = new JLabel("Welcome back, " + (username.isEmpty() ? "Guest" : username));
        welcome.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD, 26f));
        welcome.setForeground(UIUtils.TEXT);
        JLabel subtitle = new JLabel("Today · "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        subtitle.setForeground(UIUtils.TEXT_MUTED);
        titleBox.add(welcome);
        titleBox.add(subtitle);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(Box.createVerticalGlue());
        labelLastUpdated.setForeground(UIUtils.TEXT_MUTED);
        labelLastUpdated.setAlignmentX(Component.RIGHT_ALIGNMENT);
        right.add(labelLastUpdated);
        right.add(Box.createVerticalStrut(10));
        btnRefresh.setAlignmentX(Component.RIGHT_ALIGNMENT);
        right.add(btnRefresh);
        right.add(Box.createVerticalGlue());

        header.add(titleBox, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel buildStatsGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setBorder(BorderFactory.createEmptyBorder(6, 26, 26, 26));
        cards[0] = buildStatCard("Total Rooms", labelTotalRooms, UIUtils.ACCENT);
        cards[1] = buildStatCard("Available", labelAvailable, UIUtils.SUCCESS);
        cards[2] = buildStatCard("Occupied", labelOccupied, UIUtils.WARNING);
        cards[3] = buildStatCard("Check-ins Today", labelCheckIns, UIUtils.ACCENT);
        cards[4] = buildStatCard("Check-outs Today", labelCheckOuts, UIUtils.WARNING);
        cards[5] = buildStatCard("Revenue Today", labelRevenue, UIUtils.SUCCESS);
        for (JPanel card : cards) {
            grid.add(card);
        }
        return grid;
    }

    private JPanel buildStatCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);
        JLabel dot = new JLabel();
        dot.setOpaque(true);
        dot.setBackground(accent);
        dot.setPreferredSize(new Dimension(12, 12));
        dot.putClientProperty("FlatLaf.style", "arc: 6");
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD, 14f));
        titleLabel.setForeground(UIUtils.TEXT_MUTED);
        top.add(dot, BorderLayout.WEST);
        top.add(titleLabel, BorderLayout.CENTER);

        valueLabel.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD, 34f));
        valueLabel.setForeground(accent);

        card.add(top, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void styleComponents() {
        setBackground(UIUtils.PANEL_BG);
        header.setBackground(Color.WHITE);
        header.putClientProperty("FlatLaf.style", "background: #FFFFFF");
        grid.setBackground(UIUtils.PANEL_BG);
        for (JPanel card : cards) {
            card.setBackground(Color.WHITE);
            card.putClientProperty("FlatLaf.style", "background: #FFFFFF");
        }
        btnRefresh.setBackground(UIUtils.GHOST);
        btnRefresh.setForeground(UIUtils.TEXT);
        btnRefresh.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public JLabel getLabelTotalRooms() { return labelTotalRooms; }
    public JLabel getLabelAvailable() { return labelAvailable; }
    public JLabel getLabelOccupied() { return labelOccupied; }
    public JLabel getLabelCheckIns() { return labelCheckIns; }
    public JLabel getLabelCheckOuts() { return labelCheckOuts; }
    public JLabel getLabelRevenue() { return labelRevenue; }
    public JLabel getLabelLastUpdated() { return labelLastUpdated; }
    public JButton getBtnRefresh() { return btnRefresh; }
}
