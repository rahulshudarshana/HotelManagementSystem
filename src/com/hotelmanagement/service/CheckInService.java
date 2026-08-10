package com.hotelmanagement.service;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.dao.CheckInDAO;
import com.hotelmanagement.dao.GuestDAO;
import com.hotelmanagement.dao.ReservationDAO;
import com.hotelmanagement.dao.RoomDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.CheckIn;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.ReservationStatus;
import com.hotelmanagement.model.enums.RoomStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckInService {
    private static final Logger LOGGER = Logger.getLogger(CheckInService.class.getName());
    private final CheckInDAO checkInDAO;
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final GuestDAO guestDAO;

    public CheckInService() {
        this.checkInDAO = new CheckInDAO();
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
        this.guestDAO = new GuestDAO();
    }

    public CheckIn getCheckInById(int id) throws SQLException {
        return checkInDAO.getCheckInById(id);
    }

    public CheckIn getCheckInByReservation(int reservationID) throws SQLException {
        return checkInDAO.getCheckInByReservation(reservationID);
    }

    public List<CheckIn> getAllCheckIns() throws SQLException {
        return checkInDAO.getAllCheckIns();
    }

    public List<CheckIn> searchCheckIns(String keyword) throws SQLException {
        return checkInDAO.searchCheckIns(keyword);
    }

    public int performCheckIn(CheckIn checkIn) {
        if (checkIn.getNumberOfGuests() <= 0) {
            throw new ValidationException("Number of guests must be greater than zero.");
        }
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            // Verify referenced entities exist (prevent FK violations)
            Reservation reservation = reservationDAO.getReservationById(checkIn.getReservationID(), conn);
            if (reservation == null) {
                throw new ValidationException("Reservation ID " + checkIn.getReservationID() + " not found.");
            }
            Guest guest = guestDAO.getGuestById(checkIn.getGuestID(), conn);
            if (guest == null) {
                throw new ValidationException("Guest ID " + checkIn.getGuestID() + " not found.");
            }
            Room room = roomDAO.getRoomById(checkIn.getRoomID(), conn);
            if (room == null) {
                throw new ValidationException("Room ID " + checkIn.getRoomID() + " not found.");
            }

            // Check for duplicate inside the transaction to prevent TOCTOU
            CheckIn existing = checkInDAO.getCheckInByReservation(checkIn.getReservationID(), conn);
            if (existing != null) {
                throw new ValidationException("This reservation has already been checked in.");
            }

            int checkInID = checkInDAO.insertCheckIn(checkIn, conn);
            reservationDAO.updateReservationStatus(checkIn.getReservationID(), ReservationStatus.CheckedIn, conn);
            roomDAO.updateRoomStatus(checkIn.getRoomID(), RoomStatus.Occupied, conn);

            conn.commit();
            return checkInID;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Check-in failed", e);
            throw new DataAccessException("Check-in failed due to a database error.", e);
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

    public void deleteCheckIn(int id) throws SQLException {
        checkInDAO.deleteCheckIn(id);
    }
}
