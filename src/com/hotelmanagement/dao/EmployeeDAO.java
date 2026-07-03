package com.hotelmanagement.dao;

import com.hotelmanagement.config.DatabaseConfig;
import com.hotelmanagement.model.Employee;
import com.hotelmanagement.model.enums.Gender;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public Employee getEmployeeById(int employeeID) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        }
        return null;
    }

    public Employee getEmployeeByNumber(String employeeNumber) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE EmployeeNumber = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employeeNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees ORDER BY LastName, FirstName";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
        }
        return employees;
    }

    public List<Employee> getEmployeesByDepartment(int departmentID) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE DepartmentID = ? ORDER BY LastName, FirstName";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        }
        return employees;
    }

    public int insertEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employees (EmployeeNumber, FirstName, LastName, NIC, Gender, DateOfBirth, Phone, Email, Address, DepartmentID, Position, Salary, HireDate, EmploymentStatusID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employee.getEmployeeNumber());
            stmt.setString(2, employee.getFirstName());
            stmt.setString(3, employee.getLastName());
            stmt.setString(4, employee.getNic());
            stmt.setString(5, employee.getGender().name());
            stmt.setObject(6, employee.getDateOfBirth());
            stmt.setString(7, employee.getPhone());
            stmt.setString(8, employee.getEmail());
            stmt.setString(9, employee.getAddress());
            stmt.setInt(10, employee.getDepartmentID());
            stmt.setString(11, employee.getPosition());
            stmt.setBigDecimal(12, employee.getSalary());
            stmt.setObject(13, employee.getHireDate());
            stmt.setInt(14, employee.getEmploymentStatusID());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employees SET EmployeeNumber = ?, FirstName = ?, LastName = ?, NIC = ?, Gender = ?, DateOfBirth = ?, Phone = ?, Email = ?, Address = ?, DepartmentID = ?, Position = ?, Salary = ?, HireDate = ?, EmploymentStatusID = ? WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getEmployeeNumber());
            stmt.setString(2, employee.getFirstName());
            stmt.setString(3, employee.getLastName());
            stmt.setString(4, employee.getNic());
            stmt.setString(5, employee.getGender().name());
            stmt.setObject(6, employee.getDateOfBirth());
            stmt.setString(7, employee.getPhone());
            stmt.setString(8, employee.getEmail());
            stmt.setString(9, employee.getAddress());
            stmt.setInt(10, employee.getDepartmentID());
            stmt.setString(11, employee.getPosition());
            stmt.setBigDecimal(12, employee.getSalary());
            stmt.setObject(13, employee.getHireDate());
            stmt.setInt(14, employee.getEmploymentStatusID());
            stmt.setInt(15, employee.getEmployeeID());
            stmt.executeUpdate();
        }
    }

    public void deleteEmployee(int employeeID) throws SQLException {
        String sql = "DELETE FROM Employees WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeID);
            stmt.executeUpdate();
        }
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmployeeID(rs.getInt("EmployeeID"));
        emp.setEmployeeNumber(rs.getString("EmployeeNumber"));
        emp.setFirstName(rs.getString("FirstName"));
        emp.setLastName(rs.getString("LastName"));
        emp.setNic(rs.getString("NIC"));
        emp.setGender(Gender.valueOf(rs.getString("Gender")));
        if (rs.getDate("DateOfBirth") != null) {
            emp.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
        }
        emp.setPhone(rs.getString("Phone"));
        emp.setEmail(rs.getString("Email"));
        emp.setAddress(rs.getString("Address"));
        emp.setDepartmentID(rs.getInt("DepartmentID"));
        emp.setPosition(rs.getString("Position"));
        emp.setSalary(rs.getBigDecimal("Salary"));
        if (rs.getDate("HireDate") != null) {
            emp.setHireDate(rs.getDate("HireDate").toLocalDate());
        }
        emp.setEmploymentStatusID(rs.getInt("EmploymentStatusID"));
        if (rs.getTimestamp("CreatedAt") != null) {
            emp.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        }
        return emp;
    }
}
