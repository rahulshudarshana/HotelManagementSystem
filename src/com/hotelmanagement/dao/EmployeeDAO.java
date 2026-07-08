package com.hotelmanagement.dao;

import com.hotelmanagement.model.Employee;
import com.hotelmanagement.model.enums.Gender;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class EmployeeDAO extends BaseDAO<Employee> {

    private static final Logger LOGGER = Logger.getLogger(EmployeeDAO.class.getName());

    private static final String COLUMNS = "EmployeeID, EmployeeNumber, FirstName, LastName, NIC, Gender, DateOfBirth, Phone, Email, Address, DepartmentID, Position, Salary, HireDate, EmploymentStatusID, CreatedAt";
    private static final String SQL_BY_ID = "SELECT " + COLUMNS + " FROM Employees WHERE EmployeeID = ?";
    private static final String SQL_BY_NUMBER = "SELECT " + COLUMNS + " FROM Employees WHERE EmployeeNumber = ?";
    private static final String SQL_ALL = "SELECT " + COLUMNS + " FROM Employees ORDER BY LastName, FirstName";
    private static final String SQL_BY_DEPT = "SELECT " + COLUMNS + " FROM Employees WHERE DepartmentID = ? ORDER BY LastName, FirstName";
    private static final String SQL_INSERT = "INSERT INTO Employees (EmployeeNumber, FirstName, LastName, NIC, Gender, DateOfBirth, Phone, Email, Address, DepartmentID, Position, Salary, HireDate, EmploymentStatusID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Employees SET EmployeeNumber = ?, FirstName = ?, LastName = ?, NIC = ?, Gender = ?, DateOfBirth = ?, Phone = ?, Email = ?, Address = ?, DepartmentID = ?, Position = ?, Salary = ?, HireDate = ?, EmploymentStatusID = ? WHERE EmployeeID = ?";
    private static final String SQL_DELETE = "DELETE FROM Employees WHERE EmployeeID = ?";
    private static final String SQL_SEARCH = "SELECT " + COLUMNS + " FROM Employees WHERE FirstName LIKE ? OR LastName LIKE ? OR EmployeeNumber LIKE ? ORDER BY LastName, FirstName";

    public Employee getEmployeeById(int employeeID) throws SQLException {
        return findOne(SQL_BY_ID, employeeID);
    }

    public Employee getEmployeeByNumber(String employeeNumber) throws SQLException {
        return findOne(SQL_BY_NUMBER, employeeNumber);
    }

    public List<Employee> getAllEmployees() throws SQLException {
        return findAll(SQL_ALL);
    }

    public List<Employee> getEmployeesByDepartment(int departmentID) throws SQLException {
        return findAll(SQL_BY_DEPT, departmentID);
    }

    public List<Employee> searchEmployees(String keyword) throws SQLException {
        String pattern = "%" + keyword + "%";
        return findAll(SQL_SEARCH, pattern, pattern, pattern);
    }

    public int insertEmployee(Employee employee) throws SQLException {
        return insert(SQL_INSERT,
            employee.getEmployeeNumber(), employee.getFirstName(), employee.getLastName(),
            employee.getNic(), employee.getGender(), employee.getDateOfBirth(),
            employee.getPhone(), employee.getEmail(), employee.getAddress(),
            employee.getDepartmentID(), employee.getPosition(), employee.getSalary(),
            employee.getHireDate(), employee.getEmploymentStatusID());
    }

    public void updateEmployee(Employee employee) throws SQLException {
        update(SQL_UPDATE,
            employee.getEmployeeNumber(), employee.getFirstName(), employee.getLastName(),
            employee.getNic(), employee.getGender(), employee.getDateOfBirth(),
            employee.getPhone(), employee.getEmail(), employee.getAddress(),
            employee.getDepartmentID(), employee.getPosition(), employee.getSalary(),
            employee.getHireDate(), employee.getEmploymentStatusID(),
            employee.getEmployeeID());
    }

    public void deleteEmployee(int employeeID) throws SQLException {
        delete(SQL_DELETE, employeeID);
    }

    @Override
    protected Employee mapRow(ResultSet rs) throws SQLException {
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
