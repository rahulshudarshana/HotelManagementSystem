package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Employee;
import com.hotelmanagement.model.enums.Gender;
import com.hotelmanagement.service.EmployeeService;
import com.hotelmanagement.view.employee.employeemanagementpanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class EmployeeController {
    private final employeemanagementpanel view;
    private final EmployeeService service;

    public EmployeeController(employeemanagementpanel view) {
        this.view = view;
        this.service = new EmployeeService();
        initControllers();
        loadTable();
    }

    private void initControllers() {
        view.getBtnAdd().addActionListener(e -> addEmployee());
        view.getBtnUpdate().addActionListener(e -> updateEmployee());
        view.getBtnDelete().addActionListener(e -> deleteEmployee());
        view.getBtnSearch().addActionListener(e -> searchEmployees());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> navigateBack());
    }

    private void addEmployee() {
        try {
            Employee emp = new Employee();
            emp.setEmployeeNumber(view.getTxtEmployeeID().getText().trim());
            emp.setFirstName(view.getTxtFirstName().getText().trim());
            emp.setLastName(view.getTxtLastName().getText().trim());
            emp.setNic(view.getTxtNIC().getText().trim());
            emp.setGender(Gender.valueOf((String) view.getCmbGender().getSelectedItem()));
            emp.setPhone(view.getTxtPhone().getText().trim());
            emp.setEmail(view.getTxtEmail().getText().trim());
            emp.setPosition(view.getTxtPosition().getText().trim());
            emp.setSalary(view.getTxtSalary().getText().isEmpty() ? null : new BigDecimal(view.getTxtSalary().getText().trim()));
            emp.setDepartmentID(view.getCmbDepartment().getSelectedIndex() + 1);
            emp.setEmploymentStatusID(view.getCmbEmploymentStatus().getSelectedIndex() + 1);
            if (view.getDpHireDate().getDate() != null) {
                emp.setHireDate(view.getDpHireDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            service.createEmployee(emp);
            loadTable();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(view, "Employee added successfully.");
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Database error adding employee", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void updateEmployee() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            String empNo = (String) view.getTable().getValueAt(selectedRow, 1);
            Employee emp = service.getEmployeeByNumber(empNo);
            if (emp != null) {
                emp.setFirstName(view.getTxtFirstName().getText().trim());
                emp.setLastName(view.getTxtLastName().getText().trim());
                emp.setNic(view.getTxtNIC().getText().trim());
                emp.setGender(Gender.valueOf((String) view.getCmbGender().getSelectedItem()));
                emp.setPhone(view.getTxtPhone().getText().trim());
                emp.setEmail(view.getTxtEmail().getText().trim());
                emp.setPosition(view.getTxtPosition().getText().trim());
                emp.setSalary(view.getTxtSalary().getText().isEmpty() ? null : new BigDecimal(view.getTxtSalary().getText().trim()));
                emp.setDepartmentID(view.getCmbDepartment().getSelectedIndex() + 1);
                emp.setEmploymentStatusID(view.getCmbEmploymentStatus().getSelectedIndex() + 1);
                service.updateEmployee(emp);
                loadTable();
                clearForm();
                javax.swing.JOptionPane.showMessageDialog(view, "Employee updated.");
            }
        } catch (ValidationException ex) {
            javax.swing.JOptionPane.showMessageDialog(view, ex.getMessage());
        } catch (DataAccessException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Database error updating employee", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void deleteEmployee() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            int empID = (int) view.getTable().getValueAt(selectedRow, 0);
            if (javax.swing.JOptionPane.showConfirmDialog(view, "Delete this employee?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
                service.deleteEmployee(empID);
                loadTable();
                clearForm();
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Database error deleting employee", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        } catch (Exception ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "An unexpected error occurred.");
        }
    }

    private void searchEmployees() {
        try {
            String keyword = view.getTxtSearch().getText().trim();
            populateTable(service.searchEmployees(keyword));
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllEmployees());
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateTable(List<Employee> employees) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Emp No", "First Name", "Last Name", "NIC", "Phone", "Department"}, 0);
        for (Employee e : employees) {
            model.addRow(new Object[]{e.getEmployeeID(), e.getEmployeeNumber(), e.getFirstName(), e.getLastName(), e.getNic(), e.getPhone(), e.getDepartmentID()});
        }
        view.getTable().setModel(model);
    }

    private void clearForm() {
        view.getTxtEmployeeID().setText("");
        view.getTxtFirstName().setText("");
        view.getTxtLastName().setText("");
        view.getTxtNIC().setText("");
        view.getTxtPhone().setText("");
        view.getTxtEmail().setText("");
        view.getTxtPosition().setText("");
        view.getTxtSalary().setText("");
        view.getTxtSearch().setText("");
    }

    private void navigateBack() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
    }
}
