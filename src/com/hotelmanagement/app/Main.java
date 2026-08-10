package com.hotelmanagement.app;

public class Main {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(() ->
            new com.hotelmanagement.view.login.LoginView().setVisible(true));
    }
}
