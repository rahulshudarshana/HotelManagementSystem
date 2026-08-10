package com.hotelmanagement.view;

import com.hotelmanagement.controller.BillingController;
import com.hotelmanagement.controller.CheckInController;
import com.hotelmanagement.controller.CheckOutController;
import com.hotelmanagement.controller.EmployeeController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.HousekeepingController;
import com.hotelmanagement.controller.ReportController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.UserController;
import com.hotelmanagement.util.SessionManager;
import com.hotelmanagement.util.UIUtils;
import com.hotelmanagement.view.billing.billingpanel;
import com.hotelmanagement.view.checkin.checkinpanel;
import com.hotelmanagement.view.checkout.checkoutpanel;
import com.hotelmanagement.view.dashboard.DashboardPanel;
import com.hotelmanagement.view.employee.employeemanagementpanel;
import com.hotelmanagement.view.guest.GuestManagementPanel;
import com.hotelmanagement.view.housekeeping.housekeepingpanel;
import com.hotelmanagement.view.login.LoginView;
import com.hotelmanagement.view.report.reportpanel;
import com.hotelmanagement.view.reservation.ReservationManagementPanel;
import com.hotelmanagement.view.room.RoomManagementPanel;
import com.hotelmanagement.view.user.usermanagementpanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends javax.swing.JFrame {

    private static final Color SIDEBAR_BG = new Color(0x1E293B);
    private static final Color SIDEBAR_TEXT = new Color(0xCBD5E1);

    private static MainFrame instance;

    public static MainFrame getInstance() {
        return instance;
    }

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JPanel root;
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
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
        if (instance != null) {
            instance.dispose();
        }
        instance = this;
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
        cardPanel = new JPanel(cardLayout);

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
        root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(cardPanel, BorderLayout.CENTER);
        setContentPane(root);
        com.hotelmanagement.util.UIUtils.applyTheme(cardPanel);
    }

    public void navigateTo(String panelName) {
        cardLayout.show(cardPanel, panelName);
        applySelectedNav(panelName);
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));

        JLabel brand = new JLabel("Hotel Manager");
        brand.setFont(UIUtils.defaultFont().deriveFont(Font.BOLD, 18f));
        brand.setForeground(Color.WHITE);
        brand.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 0));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(14));

        addNavButton(sidebar, "Dashboard", PANEL_DASHBOARD);
        addNavButton(sidebar, "Guests", PANEL_GUEST);
        addNavButton(sidebar, "Rooms", PANEL_ROOM);
        addNavButton(sidebar, "Reservations", PANEL_RESERVATION);
        addNavButton(sidebar, "Check-in", PANEL_CHECKIN);
        addNavButton(sidebar, "Check-out", PANEL_CHECKOUT);
        addNavButton(sidebar, "Billing", PANEL_BILLING);
        addNavButton(sidebar, "Employees", PANEL_EMPLOYEE);
        addNavButton(sidebar, "Reports", PANEL_REPORT);
        addNavButton(sidebar, "Users", PANEL_USER);
        addNavButton(sidebar, "Housekeeping", PANEL_HOUSEKEEPING);

        sidebar.add(Box.createVerticalGlue());

        JButton logout = buildNavButton("Logout");
        logout.setBackground(UIUtils.DANGER);
        logout.setForeground(Color.WHITE);
        logout.addActionListener(e -> logout());
        sidebar.add(logout);

        return sidebar;
    }

    private void addNavButton(JPanel sidebar, String label, String panelName) {
        JButton btn = buildNavButton(label);
        btn.addActionListener(e -> navigateTo(panelName));
        navButtons.put(panelName, btn);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(6));
    }

    private JButton buildNavButton(String label) {
        JButton btn = new JButton(label);
        btn.putClientProperty("JButton.buttonType", "toolBarButton");
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(SIDEBAR_TEXT);
        return btn;
    }

    private void applySelectedNav(String panelName) {
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            JButton btn = entry.getValue();
            if (entry.getKey().equals(panelName)) {
                btn.setBackground(UIUtils.ACCENT);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(SIDEBAR_BG);
                btn.setForeground(SIDEBAR_TEXT);
            }
        }
    }

    private void logout() {
        SessionManager.getInstance().logout();
        dispose();
        new LoginView().setVisible(true);
    }

    private void initComponents() {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 700));
        setLocationRelativeTo(null);
    }
}
