package com.hotelmanagement.view;

// TODO: This frame will later use CardLayout for navigating between panels
// (Dashboard, Rooms, Guests, Reservations, etc.) controlled by MainViewController.
// For now it simply hosts the DashboardPanel as a static content pane.

public class MainFrame extends javax.swing.JFrame {

    public MainFrame(javax.swing.JPanel defaultPanel) {
        initComponents(defaultPanel);
    }

    private void initComponents(javax.swing.JPanel defaultPanel) {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setContentPane(defaultPanel);
    }

}
