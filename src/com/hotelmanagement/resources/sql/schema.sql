-- ============================================================
-- Hotel Management System - MySQL Database Schema
-- Database: HotelManagementDB
-- Engine:  MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS HotelManagementDB
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE HotelManagementDB;

-- ============================================================
-- 1. USER ROLES (Lookup Table)
-- ============================================================
CREATE TABLE UserRoles (
    RoleID      INT AUTO_INCREMENT PRIMARY KEY,
    RoleName    VARCHAR(50)   NOT NULL UNIQUE,
    Description VARCHAR(255)  NULL
) ENGINE=InnoDB;

-- ============================================================
-- 2. DEPARTMENTS (Lookup Table)
-- ============================================================
CREATE TABLE Departments (
    DepartmentID   INT AUTO_INCREMENT PRIMARY KEY,
    DepartmentName VARCHAR(100) NOT NULL UNIQUE,
    Description    VARCHAR(255) NULL
) ENGINE=InnoDB;

-- ============================================================
-- 3. EMPLOYMENT STATUSES (Lookup Table)
-- ============================================================
CREATE TABLE EmploymentStatuses (
    EmploymentStatusID INT AUTO_INCREMENT PRIMARY KEY,
    StatusName         VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ============================================================
-- 4. ROOM TYPES (Lookup Table)
-- ============================================================
CREATE TABLE RoomTypes (
    RoomTypeID   INT AUTO_INCREMENT PRIMARY KEY,
    RoomTypeName VARCHAR(50)  NOT NULL UNIQUE,
    Description  VARCHAR(255) NULL,
    DefaultPrice DECIMAL(10,2) NULL,
    MaxCapacity  INT          NULL
) ENGINE=InnoDB;

-- ============================================================
-- 5. PAYMENT METHODS (Lookup Table)
-- ============================================================
CREATE TABLE PaymentMethods (
    PaymentMethodID INT AUTO_INCREMENT PRIMARY KEY,
    MethodName      VARCHAR(50) NOT NULL UNIQUE,
    Description     VARCHAR(255) NULL
) ENGINE=InnoDB;

-- ============================================================
-- 6. ROOMS
-- ============================================================
CREATE TABLE Rooms (
    RoomID        INT AUTO_INCREMENT PRIMARY KEY,
    RoomNumber    VARCHAR(10)  NOT NULL UNIQUE,
    RoomTypeID    INT          NOT NULL,
    Floor         INT          NOT NULL,
    PricePerNight DECIMAL(10,2) NOT NULL,
    Capacity      INT          NOT NULL,
    Status        VARCHAR(20)  NOT NULL DEFAULT 'Available',
    Description   TEXT         NULL,
    CreatedAt     DATETIME     NOT NULL DEFAULT NOW(),

    CONSTRAINT FK_Rooms_RoomType FOREIGN KEY (RoomTypeID) REFERENCES RoomTypes(RoomTypeID),
    CONSTRAINT CK_Rooms_PricePerNight CHECK (PricePerNight > 0),
    CONSTRAINT CK_Rooms_Capacity CHECK (Capacity > 0),
    CONSTRAINT CK_Rooms_Floor CHECK (Floor >= 0),
    CONSTRAINT CK_Rooms_Status CHECK (Status IN ('Available', 'Occupied', 'Reserved', 'Maintenance', 'Cleaning'))
) ENGINE=InnoDB;

CREATE INDEX IX_Rooms_RoomTypeID ON Rooms(RoomTypeID);
CREATE INDEX IX_Rooms_Status ON Rooms(Status);
CREATE INDEX IX_Rooms_RoomNumber ON Rooms(RoomNumber);

-- ============================================================
-- 7. EMPLOYEES
-- ============================================================
CREATE TABLE Employees (
    EmployeeID         INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeNumber     VARCHAR(20)  NOT NULL UNIQUE,
    FirstName          VARCHAR(100) NOT NULL,
    LastName           VARCHAR(100) NOT NULL,
    NIC                VARCHAR(20)  NOT NULL UNIQUE,
    Gender             VARCHAR(10)  NOT NULL,
    DateOfBirth        DATE         NULL,
    Phone              VARCHAR(20)  NOT NULL,
    Email              VARCHAR(100) NULL,
    Address            VARCHAR(255) NULL,
    DepartmentID       INT          NOT NULL,
    Position           VARCHAR(100) NULL,
    Salary             DECIMAL(12,2) NULL,
    HireDate           DATE         NOT NULL DEFAULT (CURDATE()),
    EmploymentStatusID INT          NOT NULL,
    CreatedAt          DATETIME     NOT NULL DEFAULT NOW(),

    CONSTRAINT FK_Employees_Department FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID),
    CONSTRAINT FK_Employees_EmploymentStatus FOREIGN KEY (EmploymentStatusID) REFERENCES EmploymentStatuses(EmploymentStatusID),
    CONSTRAINT CK_Employees_Gender CHECK (Gender IN ('Male', 'Female', 'Other')),
    CONSTRAINT CK_Employees_Salary CHECK (Salary IS NULL OR Salary >= 0)
) ENGINE=InnoDB;

CREATE INDEX IX_Employees_DepartmentID ON Employees(DepartmentID);
CREATE INDEX IX_Employees_EmploymentStatusID ON Employees(EmploymentStatusID);

-- ============================================================
-- 8. USERS
-- ============================================================
CREATE TABLE Users (
    UserID       INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeID   INT          NOT NULL UNIQUE,
    Username     VARCHAR(50)  NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    RoleID       INT          NOT NULL,
    Status       VARCHAR(20)  NOT NULL DEFAULT 'Active',
    LastLogin    DATETIME     NULL,
    CreatedAt    DATETIME     NOT NULL DEFAULT NOW(),

    CONSTRAINT FK_Users_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
    CONSTRAINT FK_Users_Role FOREIGN KEY (RoleID) REFERENCES UserRoles(RoleID),
    CONSTRAINT CK_Users_Status CHECK (Status IN ('Active', 'Inactive', 'Locked'))
) ENGINE=InnoDB;

CREATE INDEX IX_Users_Username ON Users(Username);
CREATE INDEX IX_Users_RoleID ON Users(RoleID);

-- ============================================================
-- 9. GUESTS
-- ============================================================
CREATE TABLE Guests (
    GuestID    INT AUTO_INCREMENT PRIMARY KEY,
    FirstName  VARCHAR(100) NOT NULL,
    LastName   VARCHAR(100) NOT NULL,
    NIC        VARCHAR(20)  NOT NULL UNIQUE,
    Phone      VARCHAR(20)  NOT NULL,
    Email      VARCHAR(100) NULL,
    Address    VARCHAR(255) NULL,
    Gender     VARCHAR(10)  NOT NULL,
    CreatedAt  DATETIME     NOT NULL DEFAULT NOW(),

    CONSTRAINT CK_Guests_Gender CHECK (Gender IN ('Male', 'Female', 'Other'))
) ENGINE=InnoDB;

CREATE INDEX IX_Guests_NIC ON Guests(NIC);
CREATE INDEX IX_Guests_Email ON Guests(Email);
CREATE INDEX IX_Guests_Phone ON Guests(Phone);

-- ============================================================
-- 10. RESERVATIONS
-- ============================================================
CREATE TABLE Reservations (
    ReservationID   INT AUTO_INCREMENT PRIMARY KEY,
    GuestID         INT          NOT NULL,
    RoomID          INT          NOT NULL,
    CheckInDate     DATE         NOT NULL,
    CheckOutDate    DATE         NOT NULL,
    NumberOfGuests  INT          NOT NULL,
    Status          VARCHAR(20)  NOT NULL DEFAULT 'Reserved',
    SpecialRequests TEXT         NULL,
    CreatedBy       INT          NULL,
    CreatedAt       DATETIME     NOT NULL DEFAULT NOW(),
    UpdatedAt       DATETIME     NULL,

    CONSTRAINT FK_Reservations_Guest FOREIGN KEY (GuestID) REFERENCES Guests(GuestID),
    CONSTRAINT FK_Reservations_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_Reservations_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_Reservations_NumberOfGuests CHECK (NumberOfGuests > 0),
    CONSTRAINT CK_Reservations_Dates CHECK (CheckOutDate > CheckInDate),
    CONSTRAINT CK_Reservations_Status CHECK (Status IN ('Pending', 'Confirmed', 'CheckedIn', 'CheckedOut', 'Cancelled'))
) ENGINE=InnoDB;

CREATE INDEX IX_Reservations_GuestID ON Reservations(GuestID);
CREATE INDEX IX_Reservations_RoomID ON Reservations(RoomID);
CREATE INDEX IX_Reservations_Status ON Reservations(Status);
CREATE INDEX IX_Reservations_CheckInDate ON Reservations(CheckInDate);
CREATE INDEX IX_Reservations_CheckOutDate ON Reservations(CheckOutDate);

-- ============================================================
-- 11. CHECK-INS
-- ============================================================
CREATE TABLE CheckIns (
    CheckInID         INT AUTO_INCREMENT PRIMARY KEY,
    ReservationID     INT          NOT NULL UNIQUE,
    GuestID           INT          NOT NULL,
    RoomID            INT          NOT NULL,
    ActualCheckInDate DATETIME     NOT NULL DEFAULT NOW(),
    NumberOfGuests    INT          NOT NULL,
    ReceptionistID    INT          NULL,
    Notes             TEXT         NULL,
    CreatedAt         DATETIME     NOT NULL DEFAULT NOW(),

    CONSTRAINT FK_CheckIns_Reservation FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID),
    CONSTRAINT FK_CheckIns_Guest FOREIGN KEY (GuestID) REFERENCES Guests(GuestID),
    CONSTRAINT FK_CheckIns_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_CheckIns_Receptionist FOREIGN KEY (ReceptionistID) REFERENCES Users(UserID),
    CONSTRAINT CK_CheckIns_NumberOfGuests CHECK (NumberOfGuests > 0)
) ENGINE=InnoDB;

CREATE INDEX IX_CheckIns_ReservationID ON CheckIns(ReservationID);
CREATE INDEX IX_CheckIns_GuestID ON CheckIns(GuestID);
CREATE INDEX IX_CheckIns_RoomID ON CheckIns(RoomID);

-- ============================================================
-- 12. CHECK-OUTS
-- ============================================================
CREATE TABLE CheckOuts (
    CheckOutID         INT AUTO_INCREMENT PRIMARY KEY,
    ReservationID      INT          NOT NULL UNIQUE,
    CheckInID          INT          NOT NULL,
    GuestID            INT          NOT NULL,
    RoomID             INT          NOT NULL,
    ActualCheckOutDate DATETIME     NOT NULL DEFAULT NOW(),
    RoomCharges        DECIMAL(12,2) NOT NULL DEFAULT 0,
    AdditionalCharges  DECIMAL(12,2) NOT NULL DEFAULT 0,
    TotalAmount        DECIMAL(12,2) NOT NULL DEFAULT 0,
    Notes              TEXT         NULL,
    ProcessedBy        INT          NULL,
    CreatedAt          DATETIME     NOT NULL DEFAULT NOW(),

    CONSTRAINT FK_CheckOuts_Reservation FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID),
    CONSTRAINT FK_CheckOuts_CheckIn FOREIGN KEY (CheckInID) REFERENCES CheckIns(CheckInID),
    CONSTRAINT FK_CheckOuts_Guest FOREIGN KEY (GuestID) REFERENCES Guests(GuestID),
    CONSTRAINT FK_CheckOuts_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_CheckOuts_ProcessedBy FOREIGN KEY (ProcessedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_CheckOuts_RoomCharges CHECK (RoomCharges >= 0),
    CONSTRAINT CK_CheckOuts_AdditionalCharges CHECK (AdditionalCharges >= 0),
    CONSTRAINT CK_CheckOuts_TotalAmount CHECK (TotalAmount >= 0)
) ENGINE=InnoDB;

CREATE INDEX IX_CheckOuts_ReservationID ON CheckOuts(ReservationID);
CREATE INDEX IX_CheckOuts_GuestID ON CheckOuts(GuestID);

-- ============================================================
-- 13. INVOICES
-- ============================================================
CREATE TABLE Invoices (
    InvoiceID         INT AUTO_INCREMENT PRIMARY KEY,
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
    IssuedDate        DATETIME     NOT NULL DEFAULT NOW(),
    DueDate           DATE         NULL,
    Notes             TEXT         NULL,
    CreatedBy         INT          NULL,
    CreatedAt         DATETIME     NOT NULL DEFAULT NOW(),

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
) ENGINE=InnoDB;

CREATE INDEX IX_Invoices_ReservationID ON Invoices(ReservationID);
CREATE INDEX IX_Invoices_GuestID ON Invoices(GuestID);
CREATE INDEX IX_Invoices_InvoiceNumber ON Invoices(InvoiceNumber);
CREATE INDEX IX_Invoices_Status ON Invoices(Status);

-- ============================================================
-- 14. PAYMENTS
-- ============================================================
CREATE TABLE Payments (
    PaymentID       INT AUTO_INCREMENT PRIMARY KEY,
    InvoiceID       INT           NOT NULL,
    Amount          DECIMAL(12,2) NOT NULL,
    PaymentMethodID INT           NOT NULL,
    PaymentDate     DATETIME      NOT NULL DEFAULT NOW(),
    ReferenceNumber VARCHAR(100)  NULL,
    Notes           TEXT          NULL,
    ProcessedBy     INT           NULL,
    CreatedAt       DATETIME      NOT NULL DEFAULT NOW(),

    CONSTRAINT FK_Payments_Invoice FOREIGN KEY (InvoiceID) REFERENCES Invoices(InvoiceID),
    CONSTRAINT FK_Payments_PaymentMethod FOREIGN KEY (PaymentMethodID) REFERENCES PaymentMethods(PaymentMethodID),
    CONSTRAINT FK_Payments_ProcessedBy FOREIGN KEY (ProcessedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_Payments_Amount CHECK (Amount > 0)
) ENGINE=InnoDB;

CREATE INDEX IX_Payments_InvoiceID ON Payments(InvoiceID);
CREATE INDEX IX_Payments_PaymentMethodID ON Payments(PaymentMethodID);
CREATE INDEX IX_Payments_PaymentDate ON Payments(PaymentDate);

-- ============================================================
-- 15. HOUSEKEEPING TASKS
-- ============================================================
CREATE TABLE HousekeepingTasks (
    TaskID              INT AUTO_INCREMENT PRIMARY KEY,
    RoomID              INT           NOT NULL,
    AssignedEmployeeID  INT           NULL,
    TaskType            VARCHAR(50)   NOT NULL,
    Priority            VARCHAR(20)   NOT NULL DEFAULT 'Normal',
    Status              VARCHAR(20)   NOT NULL DEFAULT 'Pending',
    ScheduledDate       DATE          NULL,
    CompletionDate      DATETIME      NULL,
    Notes               TEXT          NULL,
    CreatedBy           INT           NULL,
    CreatedAt           DATETIME      NOT NULL DEFAULT NOW(),
    UpdatedAt           DATETIME      NULL,

    CONSTRAINT FK_HousekeepingTasks_Room FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
    CONSTRAINT FK_HousekeepingTasks_AssignedEmployee FOREIGN KEY (AssignedEmployeeID) REFERENCES Employees(EmployeeID),
    CONSTRAINT FK_HousekeepingTasks_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    CONSTRAINT CK_HousekeepingTasks_Priority CHECK (Priority IN ('Low', 'Normal', 'High', 'Urgent')),
    CONSTRAINT CK_HousekeepingTasks_Status CHECK (Status IN ('Pending', 'InProgress', 'Completed', 'Cancelled'))
) ENGINE=InnoDB;

CREATE INDEX IX_HousekeepingTasks_RoomID ON HousekeepingTasks(RoomID);
CREATE INDEX IX_HousekeepingTasks_AssignedEmployeeID ON HousekeepingTasks(AssignedEmployeeID);
CREATE INDEX IX_HousekeepingTasks_Status ON HousekeepingTasks(Status);
CREATE INDEX IX_HousekeepingTasks_ScheduledDate ON HousekeepingTasks(ScheduledDate);
