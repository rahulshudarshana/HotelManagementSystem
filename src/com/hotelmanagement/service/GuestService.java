package com.hotelmanagement.service;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.dao.GuestDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Guest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestService {
    private static final Logger LOGGER = Logger.getLogger(GuestService.class.getName());
    private final GuestDAO guestDAO;

    public GuestService() {
        this.guestDAO = new GuestDAO();
    }

    public Guest getGuestById(int id) throws SQLException {
        return guestDAO.getGuestById(id);
    }

    public Guest getGuestByNIC(String nic) throws SQLException {
        return guestDAO.getGuestByNIC(nic);
    }

    public List<Guest> getAllGuests() throws SQLException {
        return guestDAO.getAllGuests();
    }

    public List<Guest> searchGuests(String keyword) throws SQLException {
        return guestDAO.searchGuests(keyword);
    }

    public int createGuest(Guest guest) {
        if (guest.getFirstName() == null || guest.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required.");
        }
        if (guest.getLastName() == null || guest.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required.");
        }
        if (guest.getNic() == null || guest.getNic().trim().isEmpty()) {
            throw new ValidationException("NIC is required.");
        }
        if (guest.getPhone() == null || guest.getPhone().trim().isEmpty()) {
            throw new ValidationException("Phone is required.");
        }
        if (guest.getGender() == null) {
            throw new ValidationException("Gender is required.");
        }
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            if (guestDAO.getGuestByNIC(guest.getNic(), conn) != null) {
                throw new ValidationException("A guest with this NIC already exists.");
            }
            int id = guestDAO.insertGuest(guest);

            conn.commit();
            LOGGER.log(Level.INFO, "Guest {0} created", id);
            return id;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Guest creation failed", e);
            throw new DataAccessException("Guest creation failed due to a database error.", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to restore auto-commit", e);
                }
                try { conn.close(); } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to close connection", e);
                }
            }
        }
    }

    public void updateGuest(Guest guest) {
        try {
            guestDAO.updateGuest(guest);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Guest update failed", e);
            throw new DataAccessException("Guest update failed due to a database error.", e);
        }
    }

    public void deleteGuest(int id) {
        try {
            guestDAO.deleteGuest(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Guest deletion failed", e);
            throw new DataAccessException("Guest deletion failed due to a database error.", e);
        }
    }
}
