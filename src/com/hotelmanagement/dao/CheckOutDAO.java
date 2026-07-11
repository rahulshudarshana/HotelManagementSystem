package com.hotelmanagement.dao;

import com.hotelmanagement.model.CheckOut;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CheckOutDAO extends BaseDAO<CheckOut> {

    private static final Logger LOGGER = Logger.getLogger(CheckOutDAO.class.getName());

    private static final String COLUMNS = "CheckOutID, ReservationID, CheckInID, GuestID, RoomID, ActualCheckOutDate, RoomCharges, AdditionalCharges, TotalAmount, Notes, ProcessedBy, CreatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM CheckOuts WHERE CheckOutID = ?";
    private static final String SQL_BY_RESERVATION = "SELECT " + COLUMNS + " FROM CheckOuts WHERE ReservationID = ?";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM CheckOuts ORDER BY ActualCheckOutDate DESC";
    private static final String SQL_INSERT = "INSERT INTO CheckOuts (ReservationID, CheckInID, GuestID, RoomID, RoomCharges, AdditionalCharges, TotalAmount, Notes, ProcessedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM CheckOuts WHERE CheckOutID = ?";
    private static final String SQL_UPDATE = "UPDATE CheckOuts SET RoomCharges = ?, AdditionalCharges = ?, TotalAmount = ?, Notes = ? WHERE CheckOutID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM CheckOuts WHERE CAST(ReservationID AS CHAR) LIKE ? ORDER BY ActualCheckOutDate DESC";

    public CheckOut getCheckOutById(int checkOutID) throws SQLException {
        return findOne(SQL_BY_ID, checkOutID);
    }

    public CheckOut getCheckOutByReservation(int reservationID) throws SQLException {
        return findOne(SQL_BY_RESERVATION, reservationID);
    }

    public List<CheckOut> getAllCheckOuts() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<CheckOut> searchCheckOuts(String keyword) throws SQLException {
        return findAll(SQL_SEARCH, "%" + keyword + "%");
    }

    public int insertCheckOut(CheckOut checkOut) throws SQLException {
        return insert(SQL_INSERT,
            checkOut.getReservationID(), checkOut.getCheckInID(), checkOut.getGuestID(),
            checkOut.getRoomID(), checkOut.getRoomCharges(), checkOut.getAdditionalCharges(),
            checkOut.getTotalAmount(), checkOut.getNotes(), checkOut.getProcessedBy());
    }

    public int insertCheckOut(CheckOut checkOut, Connection conn) throws SQLException {
        return insert(SQL_INSERT, conn,
            checkOut.getReservationID(), checkOut.getCheckInID(), checkOut.getGuestID(),
            checkOut.getRoomID(), checkOut.getRoomCharges(), checkOut.getAdditionalCharges(),
            checkOut.getTotalAmount(), checkOut.getNotes(), checkOut.getProcessedBy());
    }

    public void updateCheckOut(CheckOut checkOut) throws SQLException {
        update(SQL_UPDATE, checkOut.getRoomCharges(), checkOut.getAdditionalCharges(),
            checkOut.getTotalAmount(), checkOut.getNotes(), checkOut.getCheckOutID());
    }

    public void deleteCheckOut(int checkOutID) throws SQLException {
        delete(SQL_DELETE, checkOutID);
    }

    @Override
    protected CheckOut mapRow(ResultSet rs) throws SQLException {
        CheckOut checkOut = new CheckOut();
        checkOut.setCheckOutID(rs.getInt("CheckOutID"));
        checkOut.setReservationID(rs.getInt("ReservationID"));
        checkOut.setCheckInID(rs.getInt("CheckInID"));
        checkOut.setGuestID(rs.getInt("GuestID"));
        checkOut.setRoomID(rs.getInt("RoomID"));
        if (rs.getTimestamp("ActualCheckOutDate") != null) {
            checkOut.setActualCheckOutDate(rs.getTimestamp("ActualCheckOutDate").toLocalDateTime());
        }
        checkOut.setRoomCharges(rs.getBigDecimal("RoomCharges"));
        checkOut.setAdditionalCharges(rs.getBigDecimal("AdditionalCharges"));
        checkOut.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        checkOut.setNotes(rs.getString("Notes"));
        checkOut.setProcessedBy(rs.getObject("ProcessedBy", Integer.class));
        if (rs.getTimestamp("CreatedAt") != null) {
            checkOut.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return checkOut;
    }
}
