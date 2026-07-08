package com.hotelmanagement.service;

import com.hotelmanagement.dao.GuestDAO;
import com.hotelmanagement.model.Guest;
import java.sql.SQLException;
import java.util.List;
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

    public int createGuest(Guest guest) throws SQLException {
        if (guest.getFirstName() == null || guest.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required.");
        }
        if (guest.getLastName() == null || guest.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required.");
        }
        if (guest.getNic() == null || guest.getNic().trim().isEmpty()) {
            throw new IllegalArgumentException("NIC is required.");
        }
        if (guest.getPhone() == null || guest.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required.");
        }
        if (guest.getGender() == null) {
            throw new IllegalArgumentException("Gender is required.");
        }
        if (guestDAO.getGuestByNIC(guest.getNic()) != null) {
            throw new IllegalArgumentException("A guest with this NIC already exists.");
        }
        return guestDAO.insertGuest(guest);
    }

    public void updateGuest(Guest guest) throws SQLException {
        guestDAO.updateGuest(guest);
    }

    public void deleteGuest(int id) throws SQLException {
        guestDAO.deleteGuest(id);
    }
}
