package com.hotelmanagement.service;

import com.hotelmanagement.dao.CheckOutDAO;
import com.hotelmanagement.dao.ReservationDAO;
import com.hotelmanagement.dao.RoomDAO;
import com.hotelmanagement.model.CheckOut;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.CheckIn;
import com.hotelmanagement.model.enums.ReservationStatus;
import com.hotelmanagement.model.enums.RoomStatus;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CheckOutService {
    private final CheckOutDAO checkOutDAO;
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final CheckInService checkInService;

    public CheckOutService() {
        this.checkOutDAO = new CheckOutDAO();
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
        this.checkInService = new CheckInService();
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

    public int performCheckOut(int reservationID, BigDecimal additionalCharges, String notes, Integer processedBy) throws SQLException {
        Reservation reservation = reservationDAO.getReservationById(reservationID);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }
        if (reservation.getStatus() != ReservationStatus.CheckedIn) {
            throw new IllegalStateException("Reservation must be in CheckedIn status to check out.");
        }

        CheckIn checkIn = checkInService.getCheckInByReservation(reservationID);
        if (checkIn == null) {
            throw new IllegalStateException("No check-in record found for this reservation.");
        }

        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), LocalDate.now());
        if (nights <= 0) nights = 1;

        BigDecimal roomCharges = reservationDAO.getReservationById(reservationID).getRoomID() != 0
            ? roomDAO.getRoomById(reservation.getRoomID()).getPricePerNight().multiply(BigDecimal.valueOf(nights))
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

        int checkOutID = checkOutDAO.insertCheckOut(checkOut);
        reservationDAO.updateReservationStatus(reservationID, ReservationStatus.CheckedOut);
        roomDAO.updateRoomStatus(reservation.getRoomID(), RoomStatus.Cleaning);
        return checkOutID;
    }
}
