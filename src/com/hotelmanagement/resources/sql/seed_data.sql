-- ============================================================
-- Hotel Management System - Seed Data
-- Database: HotelManagementDB
-- ============================================================
-- IMPORTANT: The password hash below is SHA-256("admin123")
-- ============================================================

USE HotelManagementDB;

-- ============================================================
-- 1. USER ROLES
-- ============================================================
INSERT INTO UserRoles (RoleName, Description) VALUES
('Admin', 'System administrator with full access'),
('Manager', 'Hotel manager with management access'),
('Receptionist', 'Front desk receptionist'),
('Housekeeping', 'Housekeeping staff'),
('Accountant', 'Finance and billing staff');

-- ============================================================
-- 2. DEPARTMENTS
-- ============================================================
INSERT INTO Departments (DepartmentName, Description) VALUES
('Management', 'Hotel management and administration'),
('Front Office', 'Reception and guest services'),
('Housekeeping', 'Cleaning and maintenance'),
('Finance', 'Accounting and billing'),
('Food & Beverage', 'Restaurant and room service'),
('Security', 'Hotel security');

-- ============================================================
-- 3. EMPLOYMENT STATUSES
-- ============================================================
INSERT INTO EmploymentStatuses (StatusName) VALUES
('Active'),
('OnLeave'),
('Terminated'),
('Suspended');

-- ============================================================
-- 4. ROOM TYPES
-- ============================================================
INSERT INTO RoomTypes (RoomTypeName, Description, DefaultPrice, MaxCapacity) VALUES
('Single', 'Single room with one single bed', 5000.00, 1),
('Double', 'Room with one double bed', 8000.00, 2),
('Triple', 'Room with three single beds', 10000.00, 3),
('Suite', 'Suite with separate living area', 15000.00, 4),
('Deluxe', 'Deluxe room with premium amenities', 20000.00, 2),
('Penthouse', 'Top-floor penthouse suite', 35000.00, 6);

-- ============================================================
-- 5. PAYMENT METHODS
-- ============================================================
INSERT INTO PaymentMethods (MethodName, Description) VALUES
('Cash', 'Cash payment'),
('CreditCard', 'Credit card payment'),
('DebitCard', 'Debit card payment'),
('BankTransfer', 'Direct bank transfer'),
('OnlinePayment', 'Online payment gateway'),
('MobileWallet', 'Mobile wallet payment');

-- ============================================================
-- 6. SAMPLE EMPLOYEE (Admin user's employee record)
-- ============================================================
INSERT INTO Employees (EmployeeNumber, FirstName, LastName, NIC, Gender, Phone, Email, DepartmentID, Position, HireDate, EmploymentStatusID)
VALUES ('EMP001', 'System', 'Administrator', '000000000V', 'Male', '0112000000', 'admin@hotel.com', 1, 'System Administrator', CURDATE(), 1);

-- ============================================================
-- 7. ADMIN USER
-- Username: admin
-- Password: admin123
-- Password hash: SHA-256 of "admin123" = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- ============================================================
INSERT INTO Users (EmployeeID, Username, PasswordHash, RoleID, Status)
VALUES (1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 1, 'Active');

-- ============================================================
-- 8. SAMPLE ROOMS (6 rooms, one per type)
-- ============================================================
INSERT INTO Rooms (RoomNumber, RoomTypeID, Floor, PricePerNight, Capacity, Status, Description) VALUES
('101', 1, 1, 5000.00, 1, 'Available', 'Single room - Ground floor'),
('201', 2, 2, 8000.00, 2, 'Available', 'Double room - First floor'),
('301', 3, 3, 10000.00, 3, 'Available', 'Triple room - Second floor'),
('401', 4, 4, 15000.00, 4, 'Available', 'Suite - Third floor'),
('501', 5, 5, 20000.00, 2, 'Available', 'Deluxe room - Fourth floor'),
('601', 6, 6, 35000.00, 6, 'Available', 'Penthouse - Top floor');