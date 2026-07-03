package com.hotelmanagement.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());
    private static final String CONFIG_FILE = "/com/hotelmanagement/resources/config/database.properties";
    private static String url;
    private static String username;
    private static String password;
    private static String driverClass;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                LOGGER.warning("Database config file not found: " + CONFIG_FILE + ". Using default settings.");
                setDefaults();
                return;
            }
            props.load(input);
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driverClass = props.getProperty("db.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load database configuration.", e);
            setDefaults();
        }
    }

    private static void setDefaults() {
        url = "jdbc:sqlserver://localhost:1433;databaseName=HotelManagementDB;encrypt=true;trustServerCertificate=true";
        username = "sa";
        password = "sa123";
        driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC driver not found. Add mssql-jdbc.jar to the classpath.", e);
        }
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(true);
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close database connection.", e);
            }
        }
    }

    public static String getUrl() { return url; }
    public static String getUsername() { return username; }
    public static String getPassword() { return password; }
}
