package com.hotelmanagement.service;

import com.hotelmanagement.dao.CheckInDAO;
import com.hotelmanagement.dao.ReservationDAO;
import com.hotelmanagement.dao.RoomDAO;
import com.hotelmanagement.model.CheckIn;
import com.hotelmanagement.model.enums.ReservationStatus;
import com.hotelmanagement.model.enums.RoomStatus;
import java.sql.SQLException;
import java.util.List;

public class CheckInService {
    private final CheckInDAO checkInDAO;
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;

    public CheckInService() {
        this.checkInDAO = new CheckInDAO();
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
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

    public int performCheckIn(CheckIn checkIn) throws SQLException {
        if (checkIn.getNumberOfGuests() <= 0) {
            throw new IllegalArgumentException("Number of guests must be greater than zero.");
        }
        if (getCheckInByReservation(checkIn.getReservationID()) != null) {
            throw new IllegalStateException("This reservation has already been checked in.");
        }
        int checkInID = checkInDAO.insertCheckIn(checkIn);
        reservationDAO.updateReservationStatus(checkIn.getReservationID(), ReservationStatus.CheckedIn);
        roomDAO.updateRoomStatus(checkIn.getRoomID(), RoomStatus.Occupied);
        return checkInID;
    }

    public void deleteCheckIn(int id) throws SQLException {
        checkInDAO.deleteCheckIn(id);
    }
}
