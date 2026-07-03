-- ============================================================
-- Hotel Management System - SQL Server Database Schema
-- Database: HotelManagementDB
-- Engine:  SQL Server 2022+
-- Author:  Senior Database Architect
-- ============================================================

USE HotelManagementDB;
GO

-- ============================================================
-- 1. USER ROLES (Lookup Table)
-- Defines access control roles for the system.
-- ============================================================
CREATE TABLE UserRoles (
    RoleID      INT IDENTITY(1,1) PRIMARY KEY,
    RoleName    VARCHAR(50)   NOT NULL UNIQUE,
    Description NVARCHAR(255) NULL
);
GO

-- ============================================================
-- 2. DEPARTMENTS (Lookup Table)
-- Employee departments for organizational structure.
-- ============================================================
CREATE TABLE Departments (
    DepartmentID   INT IDENTITY(1,1) PRIMARY KEY,
    DepartmentName VARCHAR(100) NOT NULL UNIQUE,
    Description    NVARCHAR(255) NULL
);
GO

-- ============================================================
-- 3. EMPLOYMENT STATUSES (Lookup Table)
-- Tracks employment status for staff records.
-- ============================================================
CREATE TABLE EmploymentStatuses (
    EmploymentStatusID INT IDENTITY(1,1) PRIMARY KEY,
    StatusName         VARCHAR(50) NOT NULL UNIQUE
);
GO

-- ============================================================
-- 4. ROOM TYPES (Lookup Table)
-- Categorizes rooms by type with default pricing.
-- ============================================================
CREATE TABLE RoomTypes (
    RoomTypeID   INT IDENTITY(1,1) PRIMARY KEY,
    RoomTypeName VARCHAR(50)  NOT NULL UNIQUE,
    Description  NVARCHAR(255) NULL,
    DefaultPrice DECIMAL(10,2) NULL,
    MaxCapacity  INT          NULL
);
GO

-- ============================================================
-- 5. PAYMENT METHODS (Lookup Table)
-- Accepted payment methods for billing.
-- ============================================================
CREATE TABLE PaymentMethods (
    PaymentMethodID INT IDENTITY(1,1) PRIMARY KEY,
    MethodName      VARCHAR(50) NOT NULL UNIQUE,
    Description     NVARCHAR(255) NULL
);
GO

-- ============================================================
-- 6. ROOMS
-- Master room catalog with pricing, capacity, and status.
-- Status values: Available, Occupied, Reserved, Maintenance, Cleaning
-- ============================================================
CREATE TABLE Rooms (
    RoomID        INT IDENTITY(1,1) PRIMARY KEY,
    RoomNumber    VARCHAR(10)  NOT NULL UNIQUE,
    RoomTypeID    INT          NOT NULL,
    Floor         INT          NOT NULL,
    PricePerNight DECIMAL(10,2) NOT NULL,
    Capacity      INT          NOT NULL,
    Status        VARCHAR(20)  NOT NULL DEFAULT 'Available',
    Description   NVARCHAR(500) NULL,
    CreatedAt     DATETIME2    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Rooms_RoomType FOREIGN KEY (RoomTypeID) REFERENCES RoomTypes(RoomTypeID),
    CONSTRAINT CK_Rooms_PricePerNight CHECK (PricePerNight > 0),
    CONSTRAINT CK_Rooms_Capacity CHECK (Capacity > 0),
    CONSTRAINT CK_Rooms_Floor CHECK (Floor >= 0),
    CONSTRAINT CK_Rooms_Status CHECK (Status IN ('Available', 'Occupied', 'Reserved', 'Maintenance', 'Cleaning'))
);
GO

CREATE INDEX IX_Rooms_RoomTypeID ON Rooms(RoomTypeID);
GO
CREATE INDEX IX_Rooms_Status ON Rooms(Status);
GO
CREATE INDEX IX_Rooms_RoomNumber ON Rooms(RoomNumber);
GO

-- ============================================================
-- 7. EMPLOYEES
-- Staff information linked to departments and employment status.
-- ============================================================
CREATE TABLE Employees (
    EmployeeID         INT IDENTITY(1,1) PRIMARY KEY,
    EmployeeNumber     VARCHAR(20)  NOT NULL UNIQUE,
    FirstName          NVARCHAR(100) NOT NULL,
    LastName           NVARCHAR(100) NOT NULL,
    NIC                VARCHAR(20)  NOT NULL UNIQUE,
    Gender             VARCHAR(10)  NOT NULL,
    DateOfBirth        DATE         NULL,
    Phone              VARCHAR(20)  NOT NULL,
    Email              VARCHAR(100) NULL,
    Address            NVARCHAR(255) NULL,
    DepartmentID       INT          NOT NULL,
    Position           NVARCHAR(100) NULL,
    Salary             DECIMAL(12,2) NULL,
    HireDate           DATE         NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    EmploymentStatusID INT          NOT NULL,
    CreatedAt          DATETIME2    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Employees_Department FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID),
    CONSTRAINT FK_Employees_EmploymentStatus FOREIGN KEY (EmploymentStatusID) REFERENCES EmploymentStatuses(EmploymentStatusID),
    CONSTRAINT CK_Employees_Gender CHECK (Gender IN ('Male', 'Female', 'Other')),
    CONSTRAINT CK_Employees_Salary CHECK (Salary IS NULL OR Salary >= 0)
);
GO

CREATE INDEX IX_Employees_DepartmentID ON Employees(DepartmentID);
GO
CREATE INDEX IX_Employees_EmploymentStatusID ON Employees(EmploymentStatusID);
GO

-- ============================================================
-- 8. USERS
-- Login credentials for system access. Each employee has one user account.
-- Status values: Active, Inactive, Locked
-- ============================================================
CREATE TABLE Users (
    UserID       INT IDENTITY(1,1) PRIMARY KEY,
    EmployeeID   INT          NOT NULL UNIQUE,
    Username     VARCHAR(50)  NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    RoleID       INT          NOT NULL,
    Status       VARCHAR(20)  NOT NULL DEFAULT 'Active',
    LastLogin    DATETIME2    NULL,
    CreatedAt    DATETIME2    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Users_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
    CONSTRAINT FK_Users_Role FOREIGN KEY (RoleID) REFERENCES UserRoles(RoleID),
    CONSTRAINT CK_Users_Status CHECK (Status IN ('Active', 'Inactive', 'Locked'))
);
GO

CREATE INDEX IX_Users_Username ON Users(Username);
GO
CREATE INDEX IX_Users_RoleID ON Users(RoleID);
GO

-- ============================================================
-- 9. GUESTS
-- Customer information for hotel stays.
-- ============================================================
CREATE TABLE Guests (
    GuestID    INT IDENTITY(1,1) PRIMARY KEY,
    FirstName  NVARCHAR(100) NOT NULL,
    LastName   NVARCHAR(100) NOT NULL,
    NIC        VARCHAR(20)  NOT NULL UNIQUE,
    Phone      VARCHAR(20)  NOT NULL,
    Email      VARCHAR(100) NULL,
    Address    NVARCHAR(255) NULL,
    Gender     VARCHAR(10)  NOT NULL,
    CreatedAt  DATETIME2    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT CK_Guests_Gender CHECK (Gender IN ('Male', 'Female', 'Other'))
);
GO

CREATE INDEX IX_Guests_NIC ON Guests(NIC);
GO
CREATE INDEX IX_Guests_Email ON Guests(Email);
GO
CREATE INDEX IX_Guests_Phone ON Guests(Phone);
GO

-- ============================================================
-- 10. RESERVATIONS
-- Booking records linking guests to rooms with date range.
-- Status values: Pending, Confirmed, CheckedIn, CheckedOut, Cancelled
-- ============================================================
CREATE TABLE Reservations (
    ReservationID   INT IDENTITY(1,1) PRIMARY KEY,
    GuestID         INT          NOT NULL,
    RoomID          INT          NOT NULL,
    CheckInDate     DATE         NOT NULL,
    CheckOutDate    DATE         NOT NULL,
    NumberOfGuests  INT          NOT NULL,
    Status          VARCHAR(20)  NOT NULL DEFAULT 'Reserved',
    SpecialRequests NVARCHAR(500) NULL,
    CreatedBy       INT          NULL,
    CreatedAt       DATETIME2    NOT NULL DEFAULT GETDATE(),
    UpdatedAt       DATETIME2    NULL,

    CONSTRAINT FK_Reservations_Guest FOREIGN KEY (GuestID) REFERENCES Guests(GuestID),
    CONSTRAINT FK_Reservations_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_Reservations_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_Reservations_NumberOfGuests CHECK (NumberOfGuests > 0),
    CONSTRAINT CK_Reservations_Dates CHECK (CheckOutDate > CheckInDate),
    CONSTRAINT CK_Reservations_Status CHECK (Status IN ('Pending', 'Confirmed', 'CheckedIn', 'CheckedOut', 'Cancelled'))
);
GO

CREATE INDEX IX_Reservations_GuestID ON Reservations(GuestID);
GO
CREATE INDEX IX_Reservations_RoomID ON Reservations(RoomID);
GO
CREATE INDEX IX_Reservations_Status ON Reservations(Status);
GO
CREATE INDEX IX_Reservations_CheckInDate ON Reservations(CheckInDate);
GO
CREATE INDEX IX_Reservations_CheckOutDate ON Reservations(CheckOutDate);
GO

-- ============================================================
-- 11. CHECK-INS
-- Records the actual check-in event for a reservation.
-- One-to-one relationship with Reservations.
-- ============================================================
CREATE TABLE CheckIns (
    CheckInID         INT IDENTITY(1,1) PRIMARY KEY,
    ReservationID     INT          NOT NULL UNIQUE,
    GuestID           INT          NOT NULL,
    RoomID            INT          NOT NULL,
    ActualCheckInDate DATETIME2    NOT NULL DEFAULT GETDATE(),
    NumberOfGuests    INT          NOT NULL,
    ReceptionistID    INT          NULL,
    Notes             NVARCHAR(500) NULL,
    CreatedAt         DATETIME2    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_CheckIns_Reservation FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID),
    CONSTRAINT FK_CheckIns_Guest FOREIGN KEY (GuestID) REFERENCES Guests(GuestID),
    CONSTRAINT FK_CheckIns_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_CheckIns_Receptionist FOREIGN KEY (ReceptionistID) REFERENCES Users(UserID),
    CONSTRAINT CK_CheckIns_NumberOfGuests CHECK (NumberOfGuests > 0)
);
GO

CREATE INDEX IX_CheckIns_ReservationID ON CheckIns(ReservationID);
GO
CREATE INDEX IX_CheckIns_GuestID ON CheckIns(GuestID);
GO
CREATE INDEX IX_CheckIns_RoomID ON CheckIns(RoomID);
GO

-- ============================================================
-- 12. CHECK-OUTS
-- Records the actual check-out event with billing calculations.
-- One-to-one relationship with Reservations.
-- RoomCharges and AdditionalCharges are calculated at check-out time.
-- ============================================================
CREATE TABLE CheckOuts (
    CheckOutID         INT IDENTITY(1,1) PRIMARY KEY,
    ReservationID      INT          NOT NULL UNIQUE,
    CheckInID          INT          NOT NULL,
    GuestID            INT          NOT NULL,
    RoomID             INT          NOT NULL,
    ActualCheckOutDate DATETIME2    NOT NULL DEFAULT GETDATE(),
    RoomCharges        DECIMAL(12,2) NOT NULL DEFAULT 0,
    AdditionalCharges  DECIMAL(12,2) NOT NULL DEFAULT 0,
    TotalAmount        DECIMAL(12,2) NOT NULL DEFAULT 0,
    Notes              NVARCHAR(500) NULL,
    ProcessedBy        INT          NULL,
    CreatedAt          DATETIME2    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_CheckOuts_Reservation FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID),
    CONSTRAINT FK_CheckOuts_CheckIn FOREIGN KEY (CheckInID) REFERENCES CheckIns(CheckInID),
    CONSTRAINT FK_CheckOuts_Guest FOREIGN KEY (GuestID) REFERENCES Guests(GuestID),
    CONSTRAINT FK_CheckOuts_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_CheckOuts_ProcessedBy FOREIGN KEY (ProcessedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_CheckOuts_RoomCharges CHECK (RoomCharges >= 0),
    CONSTRAINT CK_CheckOuts_AdditionalCharges CHECK (AdditionalCharges >= 0),
    CONSTRAINT CK_CheckOuts_TotalAmount CHECK (TotalAmount >= 0)
);
GO

CREATE INDEX IX_CheckOuts_ReservationID ON CheckOuts(ReservationID);
GO
CREATE INDEX IX_CheckOuts_GuestID ON CheckOuts(GuestID);
GO

-- ============================================================
-- 13. INVOICES
-- Billing records with itemized charges, discount, tax, and balance.
-- InvoiceNumber is a human-readable unique identifier (e.g., INV-20260001).
-- Status values: Pending, Paid, PartiallyPaid, Cancelled, Refunded
-- ============================================================
CREATE TABLE Invoices (
    InvoiceID         INT IDENTITY(1,1) PRIMARY KEY,
    InvoiceNumber     VARCHAR(50)  NOT NULL UNIQUE,
    ReservationID     INT          NOT NULL,
    GuestID           INT          NOT NULL,
    RoomCharges       DECIMAL(12,2) NOT NULL DEFAULT 0,
    AdditionalCharges DECIMAL(12,2) NOT NULL DEFAULT 0,
    Discount          DECIMAL(5,2)  NOT NULL DEFAULT 0,
    Tax               DECIMAL(12,2) NOT NULL DEFAULT 0,
    TotalAmount       DECIMAL(12,2) NOT NULL DEFAULT 0,
    AmountPaid        DECIMAL(12,2) NOT NULL DEFAULT 0,
    Balance           DECIMAL(12,2) NOT NULL DEFAULT 0,
    Status            VARCHAR(20)  NOT NULL DEFAULT 'Pending',
    IssuedDate        DATETIME2    NOT NULL DEFAULT GETDATE(),
    DueDate           DATE         NULL,
    Notes             NVARCHAR(500) NULL,
    CreatedBy         INT          NULL,
    CreatedAt         DATETIME2    NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Invoices_Reservation FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID),
    CONSTRAINT FK_Invoices_Guest FOREIGN KEY (GuestID) REFERENCES Guests(GuestID),
    CONSTRAINT FK_Invoices_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_Invoices_RoomCharges CHECK (RoomCharges >= 0),
    CONSTRAINT CK_Invoices_AdditionalCharges CHECK (AdditionalCharges >= 0),
    CONSTRAINT CK_Invoices_Discount CHECK (Discount >= 0 AND Discount <= 100),
    CONSTRAINT CK_Invoices_Tax CHECK (Tax >= 0),
    CONSTRAINT CK_Invoices_TotalAmount CHECK (TotalAmount >= 0),
    CONSTRAINT CK_Invoices_AmountPaid CHECK (AmountPaid >= 0),
    CONSTRAINT CK_Invoices_Balance CHECK (Balance >= 0),
    CONSTRAINT CK_Invoices_Status CHECK (Status IN ('Pending', 'Paid', 'PartiallyPaid', 'Cancelled', 'Refunded'))
);
GO

CREATE INDEX IX_Invoices_ReservationID ON Invoices(ReservationID);
GO
CREATE INDEX IX_Invoices_GuestID ON Invoices(GuestID);
GO
CREATE INDEX IX_Invoices_InvoiceNumber ON Invoices(InvoiceNumber);
GO
CREATE INDEX IX_Invoices_Status ON Invoices(Status);
GO

-- ============================================================
-- 14. PAYMENTS
-- Payment transactions applied to invoices.
-- Supports multiple payments per invoice (partial payments).
-- ============================================================
CREATE TABLE Payments (
    PaymentID       INT IDENTITY(1,1) PRIMARY KEY,
    InvoiceID       INT           NOT NULL,
    Amount          DECIMAL(12,2) NOT NULL,
    PaymentMethodID INT           NOT NULL,
    PaymentDate     DATETIME2     NOT NULL DEFAULT GETDATE(),
    ReferenceNumber VARCHAR(100)  NULL,
    Notes           NVARCHAR(500) NULL,
    ProcessedBy     INT           NULL,
    CreatedAt       DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Payments_Invoice FOREIGN KEY (InvoiceID) REFERENCES Invoices(InvoiceID),
    CONSTRAINT FK_Payments_PaymentMethod FOREIGN KEY (PaymentMethodID) REFERENCES PaymentMethods(PaymentMethodID),
    CONSTRAINT FK_Payments_ProcessedBy FOREIGN KEY (ProcessedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_Payments_Amount CHECK (Amount > 0)
);
GO

CREATE INDEX IX_Payments_InvoiceID ON Payments(InvoiceID);
GO
CREATE INDEX IX_Payments_PaymentMethodID ON Payments(PaymentMethodID);
GO
CREATE INDEX IX_Payments_PaymentDate ON Payments(PaymentDate);
GO

-- ============================================================
-- 15. HOUSEKEEPING TASKS
-- Task management for room cleaning and maintenance.
-- Priority: Low, Normal, High, Urgent
-- Status: Pending, InProgress, Completed, Cancelled
-- ============================================================
CREATE TABLE HousekeepingTasks (
    TaskID              INT IDENTITY(1,1) PRIMARY KEY,
    RoomID              INT           NOT NULL,
    AssignedEmployeeID  INT           NULL,
    TaskType            VARCHAR(50)   NOT NULL,
    Priority            VARCHAR(20)   NOT NULL DEFAULT 'Normal',
    Status              VARCHAR(20)   NOT NULL DEFAULT 'Pending',
    ScheduledDate       DATE          NULL,
    CompletionDate      DATETIME2     NULL,
    Notes               NVARCHAR(500) NULL,
    CreatedBy           INT           NULL,
    CreatedAt           DATETIME2     NOT NULL DEFAULT GETDATE(),
    UpdatedAt           DATETIME2     NULL,

    CONSTRAINT FK_HousekeepingTasks_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_HousekeepingTasks_AssignedEmployee FOREIGN KEY (AssignedEmployeeID) REFERENCES Employees(EmployeeID),
    CONSTRAINT FK_HousekeepingTasks_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_HousekeepingTasks_Priority CHECK (Priority IN ('Low', 'Normal', 'High', 'Urgent')),
    CONSTRAINT CK_HousekeepingTasks_Status CHECK (Status IN ('Pending', 'InProgress', 'Completed', 'Cancelled'))
);
GO

CREATE INDEX IX_HousekeepingTasks_RoomID ON HousekeepingTasks(RoomID);
GO
CREATE INDEX IX_HousekeepingTasks_AssignedEmployeeID ON HousekeepingTasks(AssignedEmployeeID);
GO
CREATE INDEX IX_HousekeepingTasks_Status ON HousekeepingTasks(Status);
GO
CREATE INDEX IX_HousekeepingTasks_ScheduledDate ON HousekeepingTasks(ScheduledDate);
GO
