-- Create Employee Management System Database

CREATE DATABASE IF NOT EXISTS employeemanagementsystem;

USE employeemanagementsystem;

-- Create login table
CREATE TABLE IF NOT EXISTS login (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

-- Create employee table
CREATE TABLE IF NOT EXISTS employee (
    empId VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    fatherName VARCHAR(100),
    dob DATE,
    salary VARCHAR(50),
    address VARCHAR(200),
    phone VARCHAR(20),
    email VARCHAR(100),
    education VARCHAR(100),
    designation VARCHAR(100),
    aadhar VARCHAR(20),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default login credentials
INSERT INTO login (username, password) VALUES ('Gunji Venkatesh', 'Gunji@143')
ON DUPLICATE KEY UPDATE password = VALUES(password);
INSERT INTO login (username, password) VALUES ('admin', 'admin@123')
ON DUPLICATE KEY UPDATE password = VALUES(password);

-- Insert sample employee data
INSERT INTO employee (empId, name, fatherName, dob, salary, address, phone, email, education, designation, aadhar) VALUES 
('EMP0001', 'Rajesh Kumar', 'Suresh Kumar', '1990-05-15', '50000', '123 Main St', '9876543210', 'rajesh@example.com', 'B.Tech', 'Developer', '1234-5678-9012')
ON DUPLICATE KEY UPDATE
name = VALUES(name),
fatherName = VALUES(fatherName),
dob = VALUES(dob),
salary = VALUES(salary),
address = VALUES(address),
phone = VALUES(phone),
email = VALUES(email),
education = VALUES(education),
designation = VALUES(designation),
aadhar = VALUES(aadhar);
