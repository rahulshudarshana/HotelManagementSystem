package com.hotelmanagement.app;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() ->
            new com.hotelmanagement.view.login.LoginView().setVisible(true));
    }
}
