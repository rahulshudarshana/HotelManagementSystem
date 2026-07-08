package com.hotelmanagement.service;

import com.hotelmanagement.dao.EmployeeDAO;
import com.hotelmanagement.model.Employee;
import java.sql.SQLException;
import java.util.List;
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

    public int createEmployee(Employee employee) throws SQLException {
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required.");
        }
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required.");
        }
        if (employee.getNic() == null || employee.getNic().trim().isEmpty()) {
            throw new IllegalArgumentException("NIC is required.");
        }
        if (employee.getEmployeeNumber() == null || employee.getEmployeeNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee number is required.");
        }
        return employeeDAO.insertEmployee(employee);
    }

    public void updateEmployee(Employee employee) throws SQLException {
        employeeDAO.updateEmployee(employee);
    }

    public void deleteEmployee(int id) throws SQLException {
        employeeDAO.deleteEmployee(id);
    }
}
