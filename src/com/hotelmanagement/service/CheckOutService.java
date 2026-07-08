package com.hotelmanagement.service;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.dao.CheckInDAO;
import com.hotelmanagement.dao.CheckOutDAO;
import com.hotelmanagement.dao.ReservationDAO;
import com.hotelmanagement.dao.RoomDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.EntityNotFoundException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.CheckOut;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.CheckIn;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.ReservationStatus;
import com.hotelmanagement.model.enums.RoomStatus;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckOutService {
    private static final Logger LOGGER = Logger.getLogger(CheckOutService.class.getName());
    private final CheckOutDAO checkOutDAO;
    private final CheckInDAO checkInDAO;
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;

    public CheckOutService() {
        this.checkOutDAO = new CheckOutDAO();
        this.checkInDAO = new CheckInDAO();
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
    }

    public CheckOut getCheckOutById(int id) throws SQLException {
        return checkOutDAO.getCheckOutById(id);
    }

    public CheckOut getCheckOutByReservation(int reservationID) throws SQLException {
        return checkOutDAO.getCheckOutByReservation(reservationID);
    }

    public List<CheckOut> getAllCheckOuts() throws SQLException {
        return checkOutDAO.getAllCheckOuts();
    }

    public List<CheckOut> searchCheckOuts(String keyword) throws SQLException {
        return checkOutDAO.searchCheckOuts(keyword);
    }

    public int performCheckOut(int reservationID, BigDecimal additionalCharges, String notes, Integer processedBy) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            Reservation reservation = reservationDAO.getReservationById(reservationID, conn);
            if (reservation == null) {
                throw new EntityNotFoundException("Reservation not found.");
            }
            if (reservation.getStatus() != ReservationStatus.CheckedIn) {
                throw new ValidationException("Reservation must be in CheckedIn status to check out.");
            }

            CheckIn checkIn = checkInDAO.getCheckInByReservation(reservationID, conn);
            if (checkIn == null) {
                throw new EntityNotFoundException("No check-in record found for this reservation.");
            }

            long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), LocalDate.now());
            if (nights <= 0) nights = 1;

            Room room = roomDAO.getRoomById(reservation.getRoomID(), conn);
            BigDecimal roomCharges = room != null
                ? room.getPricePerNight().multiply(BigDecimal.valueOf(nights))
                : BigDecimal.ZERO;

            BigDecimal total = roomCharges.add(additionalCharges != null ? additionalCharges : BigDecimal.ZERO);

            CheckOut checkOut = new CheckOut();
            checkOut.setReservationID(reservationID);
            checkOut.setCheckInID(checkIn.getCheckInID());
            checkOut.setGuestID(reservation.getGuestID());
            checkOut.setRoomID(reservation.getRoomID());
            checkOut.setRoomCharges(roomCharges);
            checkOut.setAdditionalCharges(additionalCharges != null ? additionalCharges : BigDecimal.ZERO);
            checkOut.setTotalAmount(total);
            checkOut.setNotes(notes);
            checkOut.setProcessedBy(processedBy);

            int checkOutID = checkOutDAO.insertCheckOut(checkOut, conn);
            reservationDAO.updateReservationStatus(reservationID, ReservationStatus.CheckedOut, conn);
            roomDAO.updateRoomStatus(reservation.getRoomID(), RoomStatus.Cleaning, conn);

            conn.commit();
            return checkOutID;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Check-out failed", e);
            throw new DataAccessException("Check-out failed due to a database error.", e);
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
}
