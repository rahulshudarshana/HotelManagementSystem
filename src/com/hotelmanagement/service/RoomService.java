package com.hotelmanagement.service;

import com.hotelmanagement.dao.RoomDAO;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.RoomStatus;
import java.sql.SQLException;
import java.util.List;
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

    public int createRoom(Room room) throws SQLException {
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Room number is required.");
        }
        if (room.getPricePerNight() == null || room.getPricePerNight().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than zero.");
        }
        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (roomDAO.getRoomByNumber(room.getRoomNumber()) != null) {
            throw new IllegalArgumentException("A room with this number already exists.");
        }
        return roomDAO.insertRoom(room);
    }

    public void updateRoom(Room room) throws SQLException {
        roomDAO.updateRoom(room);
    }

    public void updateRoomStatus(int roomID, RoomStatus status) throws SQLException {
        roomDAO.updateRoomStatus(roomID, status);
    }

    public void deleteRoom(int id) throws SQLException {
        roomDAO.deleteRoom(id);
    }
}
