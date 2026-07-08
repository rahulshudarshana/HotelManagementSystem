package com.hotelmanagement.model;

import com.hotelmanagement.model.enums.Gender;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    private int employeeID;
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String nic;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private int departmentID;
    private String position;
    private BigDecimal salary;
    private LocalDate hireDate;
    private int employmentStatusID;
    private LocalDateTime createdAt;

    public Employee() {}

    public Employee(int employeeID, String employeeNumber, String firstName, String lastName,
                    String nic, Gender gender, String phone, int departmentID, int employmentStatusID) {
        this.employeeID = employeeID;
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.gender = gender;
        this.phone = phone;
        this.departmentID = departmentID;
        this.employmentStatusID = employmentStatusID;
    }

    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getDepartmentID() { return departmentID; }
    public void setDepartmentID(int departmentID) { this.departmentID = departmentID; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public int getEmploymentStatusID() { return employmentStatusID; }
    public void setEmploymentStatusID(int employmentStatusID) { this.employmentStatusID = employmentStatusID; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeID == employee.employeeID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(employeeID);
    }

    @Override
    public String toString() {
        return "Employee{employeeID=" + employeeID + ", employeeNumber='" + employeeNumber + "', name='" + firstName + " " + lastName + "'}";
    }
}
