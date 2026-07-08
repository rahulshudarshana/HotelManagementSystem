package com.hotelmanagement.view;

import com.hotelmanagement.controller.BillingController;
import com.hotelmanagement.controller.CheckInController;
import com.hotelmanagement.controller.CheckOutController;
import com.hotelmanagement.controller.DashboardController;
import com.hotelmanagement.controller.EmployeeController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.HousekeepingController;
import com.hotelmanagement.controller.ReportController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.UserController;
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
    private final DashboardPanel dashboardPanel;
    private final GuestManagementPanel guestPanel;
    private final RoomManagementPanel roomPanel;
    private final ReservationManagementPanel reservationPanel;
    private final checkinpanel checkinPanel;
    private final checkoutpanel checkoutPanel;
    private final billingpanel billingPanel;
    private final employeemanagementpanel employeePanel;
    private final reportpanel reportPanel;
    private final usermanagementpanel userPanel;
    private final housekeepingpanel housekeepingPanel;
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
        this.dashboardPanel = dashboardPanel;
        guestPanel = new GuestManagementPanel();
        roomPanel = new RoomManagementPanel();
        reservationPanel = new ReservationManagementPanel();
        checkinPanel = new checkinpanel();
        checkoutPanel = new checkoutpanel();
        billingPanel = new billingpanel();
        employeePanel = new employeemanagementpanel();
        reportPanel = new reportpanel();
        userPanel = new usermanagementpanel();
        housekeepingPanel = new housekeepingpanel();

        cardLayout = new CardLayout();
        cardPanel = new javax.swing.JPanel(cardLayout);

        cardPanel.add(this.dashboardPanel, PANEL_DASHBOARD);
        cardPanel.add(guestPanel, PANEL_GUEST);
        cardPanel.add(roomPanel, PANEL_ROOM);
        cardPanel.add(reservationPanel, PANEL_RESERVATION);
        cardPanel.add(checkinPanel, PANEL_CHECKIN);
        cardPanel.add(checkoutPanel, PANEL_CHECKOUT);
        cardPanel.add(billingPanel, PANEL_BILLING);
        cardPanel.add(employeePanel, PANEL_EMPLOYEE);
        cardPanel.add(reportPanel, PANEL_REPORT);
        cardPanel.add(userPanel, PANEL_USER);
        cardPanel.add(housekeepingPanel, PANEL_HOUSEKEEPING);

        new GuestController(guestPanel);
        new RoomController(roomPanel);
        new ReservationController(reservationPanel);
        new CheckInController(checkinPanel);
        new CheckOutController(checkoutPanel);
        new BillingController(billingPanel);
        new EmployeeController(employeePanel);
        new UserController(userPanel);
        new HousekeepingController(housekeepingPanel);
        new ReportController(reportPanel);

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
