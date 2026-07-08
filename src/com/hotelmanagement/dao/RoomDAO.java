package com.hotelmanagement.dao;

import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.RoomStatus;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class RoomDAO extends BaseDAO<Room> {

    private static final Logger LOGGER = Logger.getLogger(RoomDAO.class.getName());

    private static final String COLUMNS = "RoomID, RoomNumber, RoomTypeID, Floor, PricePerNight, Capacity, Status, Description, CreatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM Rooms WHERE RoomID = ?";
    private static final String SQL_BY_NUMBER = "SELECT " + COLUMNS + " FROM Rooms WHERE RoomNumber = ?";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM Rooms ORDER BY RoomNumber";
    private static final String SQL_BY_STATUS = "SELECT " + COLUMNS + " FROM Rooms WHERE Status = ? ORDER BY RoomNumber";
    private static final String SQL_BY_TYPE = "SELECT " + COLUMNS + " FROM Rooms WHERE RoomTypeID = ? ORDER BY RoomNumber";
    private static final String SQL_AVAILABLE = "SELECT " + COLUMNS + " FROM Rooms WHERE Status = 'Available' ORDER BY RoomNumber";
    private static final String SQL_INSERT = "INSERT INTO Rooms (RoomNumber, RoomTypeID, Floor, PricePerNight, Capacity, Status, Description) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Rooms SET RoomNumber = ?, RoomTypeID = ?, Floor = ?, PricePerNight = ?, Capacity = ?, Status = ?, Description = ? WHERE RoomID = ?";
    private static final String SQL_UPDATE_STATUS = "UPDATE Rooms SET Status = ? WHERE RoomID = ?";
    private static final String SQL_DELETE = "DELETE FROM Rooms WHERE RoomID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM Rooms WHERE RoomNumber LIKE ? ORDER BY RoomNumber";

    public Room getRoomById(int roomID) throws SQLException {
        return findOne(SQL_BY_ID, roomID);
    }

    public Room getRoomById(int roomID, Connection conn) throws SQLException {
        return findOne(SQL_BY_ID, conn, roomID);
    }

    public Room getRoomByNumber(String roomNumber) throws SQLException {
        return findOne(SQL_BY_NUMBER, roomNumber);
    }

    public List<Room> getAllRooms() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<Room> getRoomsByStatus(RoomStatus status) throws SQLException {
        return findAll(SQL_BY_STATUS, status);
    }

    public List<Room> getRoomsByType(int roomTypeID) throws SQLException {
        return findAll(SQL_BY_TYPE, roomTypeID);
    }

    public List<Room> getAvailableRooms() throws SQLException {
        return findAll(SQL_AVAILABLE);
    }

    public List<Room> searchRooms(String keyword) throws SQLException {
        return findAll(SQL_SEARCH, "%" + keyword + "%");
    }

    public int insertRoom(Room room) throws SQLException {
        return insert(SQL_INSERT,
            room.getRoomNumber(), room.getRoomTypeID(), room.getFloor(),
            room.getPricePerNight(), room.getCapacity(), room.getStatus(), room.getDescription());
    }

    public void updateRoom(Room room) throws SQLException {
        update(SQL_UPDATE,
            room.getRoomNumber(), room.getRoomTypeID(), room.getFloor(),
            room.getPricePerNight(), room.getCapacity(), room.getStatus(),
            room.getDescription(), room.getRoomID());
    }

    public void updateRoomStatus(int roomID, RoomStatus status) throws SQLException {
        update(SQL_UPDATE_STATUS, status, roomID);
    }

    public void updateRoomStatus(int roomID, RoomStatus status, Connection conn) throws SQLException {
        update(SQL_UPDATE_STATUS, conn, status, roomID);
    }

    public void deleteRoom(int roomID) throws SQLException {
        delete(SQL_DELETE, roomID);
    }

    @Override
    protected Room mapRow(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomID(rs.getInt("RoomID"));
        room.setRoomNumber(rs.getString("RoomNumber"));
        room.setRoomTypeID(rs.getInt("RoomTypeID"));
        room.setFloor(rs.getInt("Floor"));
        room.setPricePerNight(rs.getBigDecimal("PricePerNight"));
        room.setCapacity(rs.getInt("Capacity"));
        room.setStatus(RoomStatus.valueOf(rs.getString("Status")));
        room.setDescription(rs.getString("Description"));
        if (rs.getTimestamp("CreatedAt") != null) {
            room.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return room;
    }
}
