package com.hotelmanagement.service;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.dao.RoomDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.RoomStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomService {
    private static final Logger LOGGER = Logger.getLogger(RoomService.class.getName());
    private final RoomDAO roomDAO;

    public RoomService() {
        this.roomDAO = new RoomDAO();
    }

    public Room getRoomById(int id) throws SQLException {
        return roomDAO.getRoomById(id);
    }

    public Room getRoomByNumber(String number) throws SQLException {
        return roomDAO.getRoomByNumber(number);
    }

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public List<Room> getRoomsByStatus(RoomStatus status) throws SQLException {
        return roomDAO.getRoomsByStatus(status);
    }

    public List<Room> getRoomsByType(int roomTypeID) throws SQLException {
        return roomDAO.getRoomsByType(roomTypeID);
    }

    public List<Room> getAvailableRooms() throws SQLException {
        return roomDAO.getAvailableRooms();
    }

    public List<Room> searchRooms(String keyword) throws SQLException {
        return roomDAO.searchRooms(keyword);
    }

    public int createRoom(Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new ValidationException("Room number is required.");
        }
        if (room.getPricePerNight() == null || room.getPricePerNight().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price per night must be greater than zero.");
        }
        if (room.getCapacity() <= 0) {
            throw new ValidationException("Capacity must be greater than zero.");
        }
        try {
            if (roomDAO.getRoomByNumber(room.getRoomNumber()) != null) {
                throw new ValidationException("A room with this number already exists.");
            }
            return roomDAO.insertRoom(room);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Room creation failed", e);
            throw new DataAccessException("Room creation failed due to a database error.", e);
        }
    }

    public void updateRoom(Room room) {
        try {
            roomDAO.updateRoom(room);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Room update failed", e);
            throw new DataAccessException("Room update failed due to a database error.", e);
        }
    }

    public void updateRoomStatus(int roomID, RoomStatus status) {
        try {
            roomDAO.updateRoomStatus(roomID, status);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Room status update failed", e);
            throw new DataAccessException("Room status update failed due to a database error.", e);
        }
    }

    public void deleteRoom(int id) {
        try {
            roomDAO.deleteRoom(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Room deletion failed", e);
            throw new DataAccessException("Room deletion failed due to a database error.", e);
        }
    }
}
