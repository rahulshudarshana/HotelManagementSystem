package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class BaseDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(BaseDAO.class.getName());

    protected abstract T mapRow(ResultSet rs) throws SQLException;

    protected T findOne(String sql, Object... params) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return findOne(sql, conn, params);
        }
    }

    protected T findOne(String sql, Connection conn, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    protected List<T> findAll(String sql, Object... params) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return findAll(sql, conn, params);
        }
    }

    protected List<T> findAll(String sql, Connection conn, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    protected int insert(String sql, Object... params) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return insert(sql, conn, params);
        }
    }

    protected int insert(String sql, Connection conn, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    protected void update(String sql, Object... params) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            update(sql, conn, params);
        }
    }

    protected void update(String sql, Connection conn, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
        }
    }

    protected void delete(String sql, Object... params) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            delete(sql, conn, params);
        }
    }

    protected void delete(String sql, Connection conn, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
        }
    }

    protected int queryInt(String sql, Object... params) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return queryInt(sql, conn, params);
        }
    }

    protected int queryInt(String sql, Connection conn, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof Integer) {
                stmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof String) {
                stmt.setString(i + 1, (String) param);
            } else if (param instanceof BigDecimal) {
                stmt.setBigDecimal(i + 1, (BigDecimal) param);
            } else if (param instanceof LocalDate) {
                stmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) param));
            } else if (param instanceof LocalDateTime) {
                stmt.setTimestamp(i + 1, java.sql.Timestamp.valueOf((LocalDateTime) param));
            } else if (param instanceof Boolean) {
                stmt.setBoolean(i + 1, (Boolean) param);
            } else if (param instanceof Enum) {
                stmt.setString(i + 1, ((Enum<?>) param).name());
            } else if (param == null) {
                stmt.setNull(i + 1, Types.NULL);
            } else {
                stmt.setObject(i + 1, param);
            }
        }
    }
}
