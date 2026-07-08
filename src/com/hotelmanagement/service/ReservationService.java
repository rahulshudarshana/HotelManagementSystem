package com.hotelmanagement.service;

import com.hotelmanagement.dao.ReservationDAO;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.enums.ReservationStatus;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class ReservationService {
    private static final Logger LOGGER = Logger.getLogger(ReservationService.class.getName());
    private final ReservationDAO reservationDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
    }

    public Reservation getReservationById(int id) throws SQLException {
        return reservationDAO.getReservationById(id);
    }

    public List<Reservation> getReservationsByGuest(int guestID) throws SQLException {
        return reservationDAO.getReservationsByGuest(guestID);
    }

    public List<Reservation> getReservationsByRoom(int roomID) throws SQLException {
        return reservationDAO.getReservationsByRoom(roomID);
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) throws SQLException {
        return reservationDAO.getReservationsByStatus(status);
    }

    public List<Reservation> getReservationsByDateRange(LocalDate from, LocalDate to) throws SQLException {
        return reservationDAO.getReservationsByDateRange(from, to);
    }

    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.getAllReservations();
    }

    public List<Reservation> searchReservations(String keyword) throws SQLException {
        return reservationDAO.searchReservations(keyword);
    }

    public boolean isRoomAvailable(int roomID, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        return reservationDAO.isRoomAvailable(roomID, checkIn, checkOut);
    }

    public int createReservation(Reservation reservation) throws SQLException {
        if (reservation.getCheckInDate() == null) {
            throw new IllegalArgumentException("Check-in date is required.");
        }
        if (reservation.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Check-out date is required.");
        }
        if (!reservation.getCheckOutDate().isAfter(reservation.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        if (reservation.getNumberOfGuests() <= 0) {
            throw new IllegalArgumentException("Number of guests must be greater than zero.");
        }
        if (!isRoomAvailable(reservation.getRoomID(), reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            throw new IllegalArgumentException("The selected room is not available for the specified dates.");
        }
        return reservationDAO.insertReservation(reservation);
    }

    public void updateReservation(Reservation reservation) throws SQLException {
        reservationDAO.updateReservation(reservation);
    }

    public void updateReservationStatus(int reservationID, ReservationStatus status) throws SQLException {
        reservationDAO.updateReservationStatus(reservationID, status);
    }

    public void deleteReservation(int id) throws SQLException {
        reservationDAO.deleteReservation(id);
    }
}
