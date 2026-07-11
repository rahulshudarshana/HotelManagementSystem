package com.hotelmanagement.dao;

import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.enums.ReservationStatus;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class ReservationDAO extends BaseDAO<Reservation> {

    private static final Logger LOGGER = Logger.getLogger(ReservationDAO.class.getName());

    private static final String COLUMNS = "ReservationID, GuestID, RoomID, CheckInDate, CheckOutDate, NumberOfGuests, Status, SpecialRequests, CreatedBy, CreatedAt, UpdatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM Reservations WHERE ReservationID = ?";
    private static final String SQL_BY_GUEST = "SELECT " + COLUMNS + " FROM Reservations WHERE GuestID = ? ORDER BY CheckInDate DESC";
    private static final String SQL_BY_ROOM = "SELECT " + COLUMNS + " FROM Reservations WHERE RoomID = ? ORDER BY CheckInDate DESC";
    private static final String SQL_BY_STATUS = "SELECT " + COLUMNS + " FROM Reservations WHERE Status = ? ORDER BY CheckInDate";
    private static final String SQL_BY_DATE_RANGE = "SELECT " + COLUMNS + " FROM Reservations WHERE CheckInDate <= ? AND CheckOutDate >= ? ORDER BY CheckInDate";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM Reservations ORDER BY CheckInDate DESC";
    private static final String SQL_AVAILABILITY = "SELECT COUNT(*) FROM Reservations WHERE RoomID = ? AND Status NOT IN ('Cancelled', 'CheckedOut') AND CheckInDate < ? AND CheckOutDate > ?";
    private static final String SQL_INSERT = "INSERT INTO Reservations (GuestID, RoomID, CheckInDate, CheckOutDate, NumberOfGuests, Status, SpecialRequests, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Reservations SET GuestID = ?, RoomID = ?, CheckInDate = ?, CheckOutDate = ?, NumberOfGuests = ?, Status = ?, SpecialRequests = ?, UpdatedAt = NOW() WHERE ReservationID = ?";
    private static final String SQL_UPDATE_STATUS = "UPDATE Reservations SET Status = ?, UpdatedAt = NOW() WHERE ReservationID = ?";
    private static final String SQL_DELETE = "DELETE FROM Reservations WHERE ReservationID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM Reservations WHERE CAST(ReservationID AS CHAR) LIKE ? ORDER BY CheckInDate DESC";

    public Reservation getReservationById(int reservationID) throws SQLException {
        return findOne(SQL_BY_ID, reservationID);
    }

    public Reservation getReservationById(int reservationID, Connection conn) throws SQLException {
        return findOne(SQL_BY_ID, conn, reservationID);
    }

    public List<Reservation> getReservationsByGuest(int guestID) throws SQLException {
        return findAll(SQL_BY_GUEST, guestID);
    }

    public List<Reservation> getReservationsByRoom(int roomID) throws SQLException {
        return findAll(SQL_BY_ROOM, roomID);
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) throws SQLException {
        return findAll(SQL_BY_STATUS, status);
    }

    public List<Reservation> getReservationsByDateRange(LocalDate from, LocalDate to) throws SQLException {
        return findAll(SQL_BY_DATE_RANGE, to, from);
    }

    public List<Reservation> getAllReservations() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<Reservation> searchReservations(String keyword) throws SQLException {
        return findAll(SQL_SEARCH, "%" + keyword + "%");
    }

    public boolean isRoomAvailable(int roomID, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        return queryInt(SQL_AVAILABILITY, roomID, checkOut, checkIn) == 0;
    }

    public boolean isRoomAvailable(int roomID, LocalDate checkIn, LocalDate checkOut, Connection conn) throws SQLException {
        return queryInt(SQL_AVAILABILITY, conn, roomID, checkOut, checkIn) == 0;
    }

    public int insertReservation(Reservation reservation) throws SQLException {
        return insert(SQL_INSERT,
            reservation.getGuestID(), reservation.getRoomID(),
            reservation.getCheckInDate(), reservation.getCheckOutDate(),
            reservation.getNumberOfGuests(), reservation.getStatus(),
            reservation.getSpecialRequests(), reservation.getCreatedBy());
    }

    public int insertReservation(Reservation reservation, Connection conn) throws SQLException {
        return insert(SQL_INSERT, conn,
            reservation.getGuestID(), reservation.getRoomID(),
            reservation.getCheckInDate(), reservation.getCheckOutDate(),
            reservation.getNumberOfGuests(), reservation.getStatus(),
            reservation.getSpecialRequests(), reservation.getCreatedBy());
    }

    public void updateReservation(Reservation reservation) throws SQLException {
        update(SQL_UPDATE,
            reservation.getGuestID(), reservation.getRoomID(),
            reservation.getCheckInDate(), reservation.getCheckOutDate(),
            reservation.getNumberOfGuests(), reservation.getStatus(),
            reservation.getSpecialRequests(), reservation.getReservationID());
    }

    public void updateReservationStatus(int reservationID, ReservationStatus status) throws SQLException {
        update(SQL_UPDATE_STATUS, status, reservationID);
    }

    public void updateReservationStatus(int reservationID, ReservationStatus status, Connection conn) throws SQLException {
        update(SQL_UPDATE_STATUS, conn, status, reservationID);
    }

    public void deleteReservation(int reservationID) throws SQLException {
        delete(SQL_DELETE, reservationID);
    }

    @Override
    protected Reservation mapRow(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setReservationID(rs.getInt("ReservationID"));
        r.setGuestID(rs.getInt("GuestID"));
        r.setRoomID(rs.getInt("RoomID"));
        r.setCheckInDate(rs.getDate("CheckInDate").toLocalDate());
        r.setCheckOutDate(rs.getDate("CheckOutDate").toLocalDate());
        r.setNumberOfGuests(rs.getInt("NumberOfGuests"));
        r.setStatus(ReservationStatus.valueOf(rs.getString("Status")));
        r.setSpecialRequests(rs.getString("SpecialRequests"));
        r.setCreatedBy(rs.getObject("CreatedBy", Integer.class));
        if (rs.getTimestamp("CreatedAt") != null) {
            r.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        if (rs.getTimestamp("UpdatedAt") != null) {
            r.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        }
        return r;
    }
}
