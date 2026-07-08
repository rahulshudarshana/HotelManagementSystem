package com.hotelmanagement.model;

import com.hotelmanagement.model.enums.Gender;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Guest implements Serializable {

    private static final long serialVersionUID = 1L;
    private int guestID;
    private String firstName;
    private String lastName;
    private String nic;
    private String phone;
    private String email;
    private String address;
    private Gender gender;
    private LocalDateTime createdAt;

    public Guest() {}

    public Guest(int guestID, String firstName, String lastName, String nic, String phone, Gender gender) {
        this.guestID = guestID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.phone = phone;
        this.gender = gender;
    }

    public int getGuestID() { return guestID; }
    public void setGuestID(int guestID) { this.guestID = guestID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return guestID == guest.guestID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(guestID);
    }

    @Override
    public String toString() {
        return "Guest{guestID=" + guestID + ", firstName='" + firstName + "', lastName='" + lastName + "'}";
    }
}
