# 🏨 Hotel Management System

A Hotel Management System developed to simplify hotel operations by managing room reservations, customer information, check-in/check-out processes, billing, and staff management. This project is designed for the **Hospitality and Tourism Sector** and aims to improve efficiency while providing a better experience for both hotel staff and guests.

---

## 📌 Features

### 👤 User Authentication
- Secure login system
- Role-based access (Admin & Receptionist)

### 🛏️ Room Management
- Add new rooms
- Update room details
- Delete rooms
- View room availability
- Manage room types and pricing

### 👥 Customer Management
- Register new customers
- Update customer details
- Search customer records
- View customer booking history

### 📅 Reservation Management
- Book rooms
- Modify reservations
- Cancel reservations
- Check room availability
- View reservation details

### ✅ Check-In / Check-Out
- Guest check-in
- Guest check-out
- Update room status automatically

### 💳 Billing & Payment
- Generate invoices
- Calculate total charges
- Record payment details
- Print receipts

### 👨‍💼 Staff Management
- Add staff members
- Update staff information
- Delete staff records
- View staff details

### 📊 Reports
- Booking reports
- Customer reports
- Revenue reports
- Room occupancy reports

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Java | Application Development |
| Java Swing | Desktop User Interface |
| JDBC | Database Connectivity |
| MySQL | Database |
| NetBeans IDE | Development Environment |

---

## 📂 Project Structure

```
HotelManagementSystem/
│
├── src/
│   ├── models/
│   ├── views/
│   ├── controllers/
│   ├── database/
│   └── utilities/
│
├── resources/
│
├── database/
│   └── hotel_management.sql
│
├── screenshots/
│
├── README.md
│
└── LICENSE
```

---

## 🗄️ Database Tables

- Users
- Customers
- Rooms
- Reservations
- Payments
- Staff

---

## 🚀 Installation

### Prerequisites

- Java JDK 8 or later
- NetBeans IDE
- MySQL Server
- MySQL Connector/J (JDBC Driver)

### Steps

1. Clone the repository.

```bash
git clone https://github.com/your-username/hotel-management-system.git
```

2. Open the project in NetBeans.

3. Import the database.

```sql
hotel_management.sql
```

4. Update the database connection details.

```java
String url = "jdbc:mysql://localhost:3306/hotel_management";
String username = "root";
String password = "your_password";
```

5. Run the project.



## 💻 Main Modules

- Authentication
- Dashboard
- Room Management
- Customer Management
- Reservation Management
- Check-In / Check-Out
- Billing & Payments
- Staff Management
- Reports



## 🎯 Objectives

- Simplify hotel operations
- Improve booking management
- Reduce manual paperwork
- Track room availability
- Generate accurate billing
- Maintain customer records securely



## 📷 Screenshots

Add screenshots of the application here.

```
screenshots/
├── login.png
├── dashboard.png
├── room-management.png
├── reservation.png
├── billing.png
```



## 🔮 Future Improvements

- Online room booking
- Email confirmation
- SMS notifications
- QR Code check-in
- Credit/Debit card payment integration
- Mobile application
- Multi-branch hotel support
- Analytics dashboard



## 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to your branch
5. Open a Pull Request



## 📄 License

This project is licensed under the MIT License.



## 👨‍💻 Author

Developed as an academic project for the **Hospitality and Tourism Sector**.
