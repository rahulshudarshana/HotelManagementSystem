package com.hotelmanagement.controller;

import com.hotelmanagement.exception.DataAccessException;
import com.hotelmanagement.exception.ValidationException;
import com.hotelmanagement.model.Employee;
import com.hotelmanagement.model.enums.Gender;
import com.hotelmanagement.service.EmployeeService;
import com.hotelmanagement.view.MainFrame;
import com.hotelmanagement.view.employee.employeemanagementpanel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        String empNo = (String) view.getTable().getValueAt(row, 1);
                        Employee emp = service.getEmployeeByNumber(empNo);
                        if (emp != null) {
                            populateForm(emp);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Failed to load employee for selection", ex);
                    }
                }
            }
        });
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
            if (view.getDpDateOfBirth().getDate() != null) {
                emp.setDateOfBirth(view.getDpDateOfBirth().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
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
            String detail = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
            javax.swing.JOptionPane.showMessageDialog(view, "Database error: " + detail);
        } catch (Exception ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) return;
            String firstName = view.getTxtFirstName().getText().trim();
            String lastName = view.getTxtLastName().getText().trim();
            String nic = view.getTxtNIC().getText().trim();
            String phone = view.getTxtPhone().getText().trim();
            if (firstName.isEmpty() || lastName.isEmpty() || nic.isEmpty() || phone.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(view, "First name, Last name, NIC, and Phone are required.");
                return;
            }
            String empNo = (String) view.getTable().getValueAt(selectedRow, 1);
            Employee emp = service.getEmployeeByNumber(empNo);
            if (emp != null) {
                emp.setFirstName(firstName);
                emp.setLastName(lastName);
                emp.setNic(nic);
                emp.setGender(Gender.valueOf((String) view.getCmbGender().getSelectedItem()));
                emp.setPhone(phone);
                emp.setEmail(view.getTxtEmail().getText().trim());
                emp.setPosition(view.getTxtPosition().getText().trim());
                emp.setSalary(view.getTxtSalary().getText().isEmpty() ? null : new BigDecimal(view.getTxtSalary().getText().trim()));
                emp.setDepartmentID(view.getCmbDepartment().getSelectedIndex() + 1);
                emp.setEmploymentStatusID(view.getCmbEmploymentStatus().getSelectedIndex() + 1);
                if (view.getDpDateOfBirth().getDate() != null) {
                    emp.setDateOfBirth(view.getDpDateOfBirth().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                if (view.getDpHireDate().getDate() != null) {
                    emp.setHireDate(view.getDpHireDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                service.updateEmployee(emp);
                loadTable();
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
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Failed to search employees", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void loadTable() {
        try {
            populateTable(service.getAllEmployees());
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, "Failed to load employees", ex);
            javax.swing.JOptionPane.showMessageDialog(view, "A database error occurred. Please try again.");
        }
    }

    private void populateTable(List<Employee> employees) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Emp No", "First Name", "Last Name", "NIC", "Phone", "Department"}, 0);
        for (Employee e : employees) {
            String deptName = e.getDepartmentID() > 0 && e.getDepartmentID() <= view.getCmbDepartment().getItemCount()
                ? view.getCmbDepartment().getItemAt(e.getDepartmentID() - 1) : "Unknown";
            model.addRow(new Object[]{e.getEmployeeID(), e.getEmployeeNumber(), e.getFirstName(), e.getLastName(), e.getNic(), e.getPhone(), deptName});
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

    private void populateForm(Employee emp) {
        view.getTxtEmployeeID().setText(emp.getEmployeeNumber() != null ? emp.getEmployeeNumber() : "");
        view.getTxtFirstName().setText(emp.getFirstName() != null ? emp.getFirstName() : "");
        view.getTxtLastName().setText(emp.getLastName() != null ? emp.getLastName() : "");
        view.getTxtNIC().setText(emp.getNic() != null ? emp.getNic() : "");
        view.getTxtPhone().setText(emp.getPhone() != null ? emp.getPhone() : "");
        view.getTxtEmail().setText(emp.getEmail() != null ? emp.getEmail() : "");
        view.getTxtPosition().setText(emp.getPosition() != null ? emp.getPosition() : "");
        view.getTxtSalary().setText(emp.getSalary() != null ? emp.getSalary().toString() : "");
        if (emp.getGender() != null) {
            view.getCmbGender().setSelectedItem(emp.getGender().name());
        }
        view.getCmbDepartment().setSelectedIndex(Math.max(0, emp.getDepartmentID() - 1));
        view.getCmbEmploymentStatus().setSelectedIndex(Math.max(0, emp.getEmploymentStatusID() - 1));
        if (emp.getDateOfBirth() != null) {
            view.getDpDateOfBirth().setDate(Date.from(emp.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (emp.getHireDate() != null) {
            view.getDpHireDate().setDate(Date.from(emp.getHireDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
    }

    private void navigateBack() {
        MainFrame mainFrame = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(view);
        if (mainFrame != null) {
            mainFrame.navigateTo(MainFrame.PANEL_DASHBOARD);
        }
    }
}
