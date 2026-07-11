package com.hotelmanagement.service;

import com.hotelmanagement.dao.EmployeeDAO;
import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Employee;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeService {
    private static final Logger LOGGER = Logger.getLogger(EmployeeService.class.getName());
    private final EmployeeDAO employeeDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }

    public Employee getEmployeeById(int id) throws SQLException {
        return employeeDAO.getEmployeeById(id);
    }

    public Employee getEmployeeByNumber(String number) throws SQLException {
        return employeeDAO.getEmployeeByNumber(number);
    }

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();
    }

    public List<Employee> getEmployeesByDepartment(int departmentID) throws SQLException {
        return employeeDAO.getEmployeesByDepartment(departmentID);
    }

    public List<Employee> searchEmployees(String keyword) throws SQLException {
        return employeeDAO.searchEmployees(keyword);
    }

    public int createEmployee(Employee employee) {
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required.");
        }
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required.");
        }
        if (employee.getNic() == null || employee.getNic().trim().isEmpty()) {
            throw new ValidationException("NIC is required.");
        }
        if (employee.getEmployeeNumber() == null || employee.getEmployeeNumber().trim().isEmpty()) {
            throw new ValidationException("Employee number is required.");
        }
        try {
            return employeeDAO.insertEmployee(employee);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Employee creation failed", e);
            throw new DataAccessException("Employee creation failed due to a database error.", e);
        }
    }

    public void updateEmployee(Employee employee) {
        try {
            employeeDAO.updateEmployee(employee);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Employee update failed", e);
            throw new DataAccessException("Employee update failed due to a database error.", e);
        }
    }

    public void deleteEmployee(int id) {
        try {
            employeeDAO.deleteEmployee(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Employee deletion failed", e);
            throw new DataAccessException("Employee deletion failed due to a database error.", e);
        }
    }
}
