# 🏨 Hotel Management System

A desktop-based **Hotel Management System** developed using **Java Swing**, **JDBC**, and **MySQL** following the **MVC (Model-View-Controller)** architecture with the **DAO (Data Access Object)** design pattern.

The system is designed for the **Hospitality and Tourism Sector** to streamline hotel operations, including room management, reservations, customer records, billing, staff management, and reporting.

---

# 📖 Overview

The Hotel Management System helps hotels manage their daily operations digitally by replacing manual processes with an efficient, user-friendly application. The system allows hotel staff to manage room bookings, guest check-ins/check-outs, payments, and customer information while maintaining accurate records in a centralized database.

---

# ✨ Features

## 🔐 Authentication
- Secure login system
- Role-based access (Admin & Receptionist)

## 🛏️ Room Management
- Add new rooms
- Update room information
- Delete rooms
- View room availability
- Manage room types and pricing

## 👥 Customer Management
- Register customers
- Update customer details
- Search customers
- View customer history

## 📅 Reservation Management
- Create reservations
- Modify reservations
- Cancel reservations
- Check room availability

## ✅ Check-In & Check-Out
- Guest check-in
- Guest check-out
- Automatic room status updates

## 💳 Billing & Payments
- Generate invoices
- Calculate room charges
- Record payments
- Print receipts

## 👨‍💼 Staff Management
- Add staff
- Update staff details
- Delete staff records
- View employee information

## 📊 Reports
- Reservation reports
- Revenue reports
- Customer reports
- Room occupancy reports

---

# 🛠 Technologies Used

| Technology | Description |
|------------|-------------|
| Java | Programming Language |
| Java Swing | Desktop GUI |
| JDBC | Database Connectivity |
| MySQL | Database Management System |
| NetBeans IDE | Development Environment |

---

# 🏗 System Architecture

The project follows the **MVC (Model-View-Controller)** architecture together with the **DAO (Data Access Object)** design pattern.

### MVC Components

### Model
Represents the application's data and business logic.

Examples:
- Customer
- Room
- Reservation
- Staff
- Payment

### View
Java Swing forms that provide the graphical user interface.

Examples:
- Login Form
- Dashboard
- Room Management
- Reservation Form
- Billing Form

### Controller
Processes user actions, validates input, communicates with the DAO layer, and updates the views.

Examples:
- LoginController
- RoomController
- CustomerController
- ReservationController
- PaymentController

### DAO (Data Access Object)

Responsible for all database operations.

Examples:
- CustomerDAO
- RoomDAO
- ReservationDAO
- PaymentDAO
- DBConnection

---

# 📂 Project Structure

```text
HotelManagementSystem/
│
├── src/
│   ├── model/
│   │     ├── Customer.java
│   │     ├── Room.java
│   │     ├── Reservation.java
│   │     ├── Payment.java
│   │     └── Staff.java
│   │
│   ├── view/
│   │     ├── LoginForm.java
│   │     ├── Dashboard.java
│   │     ├── RoomForm.java
│   │     ├── CustomerForm.java
│   │     ├── ReservationForm.java
│   │     └── BillingForm.java
│   │
│   ├── controller/
│   │     ├── LoginController.java
│   │     ├── RoomController.java
│   │     ├── CustomerController.java
│   │     ├── ReservationController.java
│   │     └── PaymentController.java
│   │
│   ├── dao/
│   │     ├── DBConnection.java
│   │     ├── CustomerDAO.java
│   │     ├── RoomDAO.java
│   │     ├── ReservationDAO.java
│   │     └── PaymentDAO.java
│   │
│   ├── util/
│   │     ├── Validator.java
│   │     ├── Constants.java
│   │     └── Helper.java
│   │
│   └── Main.java
│
├── database/
│     └── hotel_management.sql
│
├── screenshots/
│
├── README.md
│
└── LICENSE
```

---

# 🗄 Database Tables

- Users
- Customers
- Rooms
- Reservations
- Payments
- Staff

---

# 🔄 Application Workflow

```text
User
   │
   ▼
View (Swing GUI)
   │
   ▼
Controller
   │
   ▼
DAO Layer
   │
   ▼
MySQL Database
   ▲
   │
Model
   │
   ▼
Updated View
```

---

# 🚀 Installation

## Prerequisites

- Java JDK 8 or higher
- NetBeans IDE
- MySQL Server
- MySQL Connector/J (JDBC Driver)

## Steps

1. Clone the repository.

```bash
git clone https://github.com/your-username/hotel-management-system.git
```

2. Open the project in NetBeans.

3. Import the SQL file located in the `database` folder into MySQL.

4. Configure your database connection.

```java
String url = "jdbc:mysql://localhost:3306/hotel_management";
String username = "root";
String password = "your_password";
```

5. Run the project.

---

# 🎯 Objectives

- Improve hotel operational efficiency
- Reduce manual record keeping
- Simplify room reservations
- Maintain customer information
- Generate accurate billing
- Produce useful management reports

---

```# 📸 Screenshots```

```Add screenshots of your application here.```

```text
screenshots/
├── login.png
├── dashboard.png
├── rooms.png
├── reservations.png
├── billing.png
├── reports.png
```

---

# 🔮 Future Enhancements

- Online room booking
- Email notifications
- SMS confirmations
- QR Code check-in
- Online payment gateway
- Mobile application
- Multi-hotel support
- Analytics dashboard
- Cloud database integration

---

# 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a new branch
3. Commit your changes
4. Push your branch
5. Create a Pull Request

---

# 📄 License

This project is licensed under the **MIT License**.

---

# 👨‍💻 Author

Developed as an academic software engineering project for the **Hospitality and Tourism Sector** using **Java Swing**, **JDBC**, **MySQL**, and the **MVC + DAO architectural pattern**.
