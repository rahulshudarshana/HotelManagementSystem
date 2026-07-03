package com.hotelmanagement.view;

import com.hotelmanagement.view.billing.billingpanel;
import com.hotelmanagement.view.checkin.checkinpanel;
import com.hotelmanagement.view.checkout.checkoutpanel;
import com.hotelmanagement.view.dashboard.DashboardPanel;
import com.hotelmanagement.view.employee.employeemanagementpanel;
import com.hotelmanagement.view.guest.GuestManagementPanel;
import com.hotelmanagement.view.housekeeping.housekeepingpanel;
import com.hotelmanagement.view.report.reportpanel;
import com.hotelmanagement.view.reservation.ReservationManagementPanel;
import com.hotelmanagement.view.room.RoomManagementPanel;
import com.hotelmanagement.view.user.usermanagementpanel;
import java.awt.CardLayout;

public class MainFrame extends javax.swing.JFrame {

    private final CardLayout cardLayout;
    private final javax.swing.JPanel cardPanel;
    public static final String PANEL_DASHBOARD = "dashboard";
    public static final String PANEL_GUEST = "guest";
    public static final String PANEL_ROOM = "room";
    public static final String PANEL_RESERVATION = "reservation";
    public static final String PANEL_CHECKIN = "checkin";
    public static final String PANEL_CHECKOUT = "checkout";
    public static final String PANEL_BILLING = "billing";
    public static final String PANEL_EMPLOYEE = "employee";
    public static final String PANEL_REPORT = "report";
    public static final String PANEL_USER = "user";
    public static final String PANEL_HOUSEKEEPING = "housekeeping";

    public MainFrame(DashboardPanel dashboardPanel) {
        cardLayout = new CardLayout();
        cardPanel = new javax.swing.JPanel(cardLayout);

        cardPanel.add(dashboardPanel, PANEL_DASHBOARD);
        cardPanel.add(new GuestManagementPanel(), PANEL_GUEST);
        cardPanel.add(new RoomManagementPanel(), PANEL_ROOM);
        cardPanel.add(new ReservationManagementPanel(), PANEL_RESERVATION);
        cardPanel.add(new checkinpanel(), PANEL_CHECKIN);
        cardPanel.add(new checkoutpanel(), PANEL_CHECKOUT);
        cardPanel.add(new billingpanel(), PANEL_BILLING);
        cardPanel.add(new employeemanagementpanel(), PANEL_EMPLOYEE);
        cardPanel.add(new reportpanel(), PANEL_REPORT);
        cardPanel.add(new usermanagementpanel(), PANEL_USER);
        cardPanel.add(new housekeepingpanel(), PANEL_HOUSEKEEPING);

        initComponents();
    }

    public void navigateTo(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public javax.swing.JPanel getCardPanel() {
        return cardPanel;
    }

    private void initComponents() {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setContentPane(cardPanel);
    }
}
