package com.hotelmanagement.dao;

import com.hotelmanagement.model.CheckIn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CheckInDAO extends BaseDAO<CheckIn> {

    private static final Logger LOGGER = Logger.getLogger(CheckInDAO.class.getName());

    private static final String COLUMNS = "CheckInID, ReservationID, GuestID, RoomID, ActualCheckInDate, NumberOfGuests, ReceptionistID, Notes, CreatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM CheckIns WHERE CheckInID = ?";
    private static final String SQL_BY_RESERVATION = "SELECT " + COLUMNS + " FROM CheckIns WHERE ReservationID = ?";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM CheckIns ORDER BY ActualCheckInDate DESC";
    private static final String SQL_INSERT = "INSERT INTO CheckIns (ReservationID, GuestID, RoomID, NumberOfGuests, ReceptionistID, Notes) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE CheckIns SET ReservationID = ?, GuestID = ?, RoomID = ?, NumberOfGuests = ?, Notes = ? WHERE CheckInID = ?";
    private static final String SQL_DELETE = "DELETE FROM CheckIns WHERE CheckInID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM CheckIns WHERE CAST(ReservationID AS CHAR) LIKE ? ORDER BY ActualCheckInDate DESC";

    public CheckIn getCheckInById(int checkInID) throws SQLException {
        return findOne(SQL_BY_ID, checkInID);
    }

    public CheckIn getCheckInByReservation(int reservationID) throws SQLException {
        return findOne(SQL_BY_RESERVATION, reservationID);
    }

    public CheckIn getCheckInByReservation(int reservationID, Connection conn) throws SQLException {
        return findOne(SQL_BY_RESERVATION, conn, reservationID);
    }

    public List<CheckIn> getAllCheckIns() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<CheckIn> searchCheckIns(String keyword) throws SQLException {
        return findAll(SQL_SEARCH, "%" + keyword + "%");
    }

    public int insertCheckIn(CheckIn checkIn) throws SQLException {
        return insert(SQL_INSERT,
            checkIn.getReservationID(), checkIn.getGuestID(), checkIn.getRoomID(),
            checkIn.getNumberOfGuests(), checkIn.getReceptionistID(), checkIn.getNotes());
    }

    public int insertCheckIn(CheckIn checkIn, Connection conn) throws SQLException {
        return insert(SQL_INSERT, conn,
            checkIn.getReservationID(), checkIn.getGuestID(), checkIn.getRoomID(),
            checkIn.getNumberOfGuests(), checkIn.getReceptionistID(), checkIn.getNotes());
    }

    public void updateCheckIn(CheckIn checkIn) throws SQLException {
        update(SQL_UPDATE,
            checkIn.getReservationID(), checkIn.getGuestID(), checkIn.getRoomID(),
            checkIn.getNumberOfGuests(), checkIn.getNotes(), checkIn.getCheckInID());
    }

    public void deleteCheckIn(int checkInID) throws SQLException {
        delete(SQL_DELETE, checkInID);
    }

    @Override
    protected CheckIn mapRow(ResultSet rs) throws SQLException {
        CheckIn checkIn = new CheckIn();
        checkIn.setCheckInID(rs.getInt("CheckInID"));
        checkIn.setReservationID(rs.getInt("ReservationID"));
        checkIn.setGuestID(rs.getInt("GuestID"));
        checkIn.setRoomID(rs.getInt("RoomID"));
        if (rs.getTimestamp("ActualCheckInDate") != null) {
            checkIn.setActualCheckInDate(rs.getTimestamp("ActualCheckInDate").toLocalDateTime());
        }
        checkIn.setNumberOfGuests(rs.getInt("NumberOfGuests"));
        checkIn.setReceptionistID(rs.getObject("ReceptionistID", Integer.class));
        checkIn.setNotes(rs.getString("Notes"));
        if (rs.getTimestamp("CreatedAt") != null) {
            checkIn.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return checkIn;
    }
}
