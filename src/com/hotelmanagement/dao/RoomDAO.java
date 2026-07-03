package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.enums.RoomStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public Room getRoomById(int roomID) throws SQLException {
        String sql = "SELECT * FROM Rooms WHERE RoomID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }
        }
        return null;
    }

    public Room getRoomByNumber(String roomNumber) throws SQLException {
        String sql = "SELECT * FROM Rooms WHERE RoomNumber = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }
        }
        return null;
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms ORDER BY RoomNumber";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        }
        return rooms;
    }

    public List<Room> getRoomsByStatus(RoomStatus status) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE Status = ? ORDER BY RoomNumber";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRoom(rs));
                }
            }
        }
        return rooms;
    }

    public List<Room> getRoomsByType(int roomTypeID) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE RoomTypeID = ? ORDER BY RoomNumber";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomTypeID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRoom(rs));
                }
            }
        }
        return rooms;
    }

    public List<Room> getAvailableRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE Status = 'Available' ORDER BY RoomNumber";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        }
        return rooms;
    }

    public int insertRoom(Room room) throws SQLException {
        String sql = "INSERT INTO Rooms (RoomNumber, RoomTypeID, Floor, PricePerNight, Capacity, Status, Description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getRoomTypeID());
            stmt.setInt(3, room.getFloor());
            stmt.setBigDecimal(4, room.getPricePerNight());
            stmt.setInt(5, room.getCapacity());
            stmt.setString(6, room.getStatus().name());
            stmt.setString(7, room.getDescription());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE Rooms SET RoomNumber = ?, RoomTypeID = ?, Floor = ?, PricePerNight = ?, Capacity = ?, Status = ?, Description = ? WHERE RoomID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getRoomTypeID());
            stmt.setInt(3, room.getFloor());
            stmt.setBigDecimal(4, room.getPricePerNight());
            stmt.setInt(5, room.getCapacity());
            stmt.setString(6, room.getStatus().name());
            stmt.setString(7, room.getDescription());
            stmt.setInt(8, room.getRoomID());
            stmt.executeUpdate();
        }
    }

    public void updateRoomStatus(int roomID, RoomStatus status) throws SQLException {
        String sql = "UPDATE Rooms SET Status = ? WHERE RoomID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, roomID);
            stmt.executeUpdate();
        }
    }

    public void deleteRoom(int roomID) throws SQLException {
        String sql = "DELETE FROM Rooms WHERE RoomID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomID);
            stmt.executeUpdate();
        }
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
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
