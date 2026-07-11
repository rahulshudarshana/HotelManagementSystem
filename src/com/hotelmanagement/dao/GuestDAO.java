package com.hotelmanagement.dao;

import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.enums.Gender;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class GuestDAO extends BaseDAO<Guest> {

    private static final Logger LOGGER = Logger.getLogger(GuestDAO.class.getName());

    private static final String COLUMNS = "GuestID, FirstName, LastName, NIC, Phone, Email, Address, Gender, CreatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM Guests WHERE GuestID = ?";
    private static final String SQL_BY_NIC = "SELECT " + COLUMNS + " FROM Guests WHERE NIC = ?";
    private static final String SQL_BY_PHONE = "SELECT " + COLUMNS + " FROM Guests WHERE Phone = ?";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM Guests ORDER BY LastName, FirstName";
    private static final String SQL_INSERT = "INSERT INTO Guests (FirstName, LastName, NIC, Phone, Email, Address, Gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Guests SET FirstName = ?, LastName = ?, NIC = ?, Phone = ?, Email = ?, Address = ?, Gender = ? WHERE GuestID = ?";
    private static final String SQL_DELETE = "DELETE FROM Guests WHERE GuestID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM Guests WHERE FirstName LIKE ? OR LastName LIKE ? OR NIC LIKE ? OR Phone LIKE ? OR Email LIKE ? ORDER BY LastName, FirstName";

    public Guest getGuestById(int guestID) throws SQLException {
        return findOne(SQL_BY_ID, guestID);
    }

    public Guest getGuestByNIC(String nic) throws SQLException {
        return findOne(SQL_BY_NIC, nic);
    }

    public Guest getGuestByNIC(String nic, Connection conn) throws SQLException {
        return findOne(SQL_BY_NIC, conn, nic);
    }

    public Guest getGuestByPhone(String phone) throws SQLException {
        return findOne(SQL_BY_PHONE, phone);
    }

    public List<Guest> getAllGuests() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<Guest> searchGuests(String keyword) throws SQLException {
        String pattern = "%" + keyword + "%";
        return findAll(SQL_SEARCH, pattern, pattern, pattern, pattern, pattern);
    }

    public int insertGuest(Guest guest) throws SQLException {
        return insert(SQL_INSERT,
            guest.getFirstName(), guest.getLastName(), guest.getNic(),
            guest.getPhone(), guest.getEmail(), guest.getAddress(), guest.getGender());
    }

    public void updateGuest(Guest guest) throws SQLException {
        update(SQL_UPDATE,
            guest.getFirstName(), guest.getLastName(), guest.getNic(),
            guest.getPhone(), guest.getEmail(), guest.getAddress(), guest.getGender(),
            guest.getGuestID());
    }

    public void deleteGuest(int guestID) throws SQLException {
        delete(SQL_DELETE, guestID);
    }

    @Override
    protected Guest mapRow(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setGuestID(rs.getInt("GuestID"));
        guest.setFirstName(rs.getString("FirstName"));
        guest.setLastName(rs.getString("LastName"));
        guest.setNic(rs.getString("NIC"));
        guest.setPhone(rs.getString("Phone"));
        guest.setEmail(rs.getString("Email"));
        guest.setAddress(rs.getString("Address"));
        guest.setGender(Gender.valueOf(rs.getString("Gender")));
        if (rs.getTimestamp("CreatedAt") != null) {
            guest.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return guest;
    }
}
