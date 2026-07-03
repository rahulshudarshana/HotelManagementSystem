package com.hotelmanagement.controller;

import com.hotelmanagement.service.AuthService;
import com.hotelmanagement.model.User;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.dashboard.DashboardPanel;
import com.hotelmanagement.view.login.LoginView;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private final LoginView view;
    private final AuthService authService;

    public LoginController(LoginView view) {
        this.view = view;
        this.authService = new AuthService();
    }

    public void login() {
        if (!view.validateInput()) return;

        String username = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword()).trim();

        try {
            User user = authService.authenticate(username, password);
            if (user != null) {
                view.showSuccess("Login Successful!");
                DashboardPanel dashboard = new DashboardPanel();
                MainFrame mainFrame = new MainFrame(dashboard);
                new DashboardController(dashboard, mainFrame);
                mainFrame.setVisible(true);
                view.dispose();
            } else {
                view.showError("Invalid username or password.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Login failed", ex);
            view.showError("Login failed. Please check database connection.");
        }
    }
}
