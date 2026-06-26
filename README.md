# EMPLOYEE-MANAGEMENT-SYSTEM

A comprehensive desktop and web-based application designed to streamline employee management operations, including employee records, recruitment, performance tracking, and administrative functions using Java and MySQL.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Development Workflow](#development-workflow)
- [Tools Used](#tools-used)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

The Employee Management System is a complete Java-based solution for managing employee data, workflows, and organizational processes. It provides HR departments and management teams with tools to handle employee records, recruitment, performance management, and payroll administration from a centralized web-based platform.

## ✨ Features

### Core Functionality
- **Employee Management**
  - Create, update, and maintain employee records
  - Store personal and professional information
  - Track employment history and status
  - Search and filter employee data

- **Employee Information Tracking**
  - Employee ID management
  - Personal details (Name, DOB, Father's Name)
  - Contact information (Phone, Email, Address)
  - Professional details (Designation, Education, Salary)
  - Document management (Aadhar, certifications)

- **Administrative Functions**
  - User authentication and login
  - Role-based access control (RBAC)
  - Database management
  - System configuration

- **Data Management**
  - Real-time data updates
  - Timestamps for record tracking (CreatedAt, UpdatedAt)
  - Secure data storage

## 🛠️ Tech Stack

### Backend
- **Language**: Java
- **Platform**: Java Runtime Environment (JRE)
- **Web Server**: Custom Java WebServer (`employee.management.system.WebServer`)
- **Package Manager**: JDK (Java Development Kit)

### Database
- **Database System**: MySQL
- **Database Name**: `employeemanagementsystem`
- **Version**: MySQL 5.7+

### Frontend
- **Web Interface**: HTML5, CSS3, JavaScript
- **Server-Side**: Java Servlets

### Build & Deployment
- **Compilation**: javac (Java Compiler)
- **Build Scripts**: Batch files (.bat)
- **Output Directory**: `out/` (compiled classes)

### Libraries & Dependencies
- **Location**: `lib/` directory
- **MySQL JDBC Driver**: For database connectivity
- **Other Java Libraries**: As needed for web functionality

### Development Tools
- **Version Control**: Git & GitHub
- **IDE Support**: Visual Studio Code (.vscode config included)
- **Automation**: Batch scripts for compilation and execution

## 🔄 Development Workflow

### Step 1: Setup Environment
```bash
# Navigate to project directory
cd C:\EMPLOYEE MANAGEMENT SYSTEM
```

### Step 2: Database Setup
```bash
# Import the database schema
mysql -u root -p < database_schema.sql

# The script will:
# - Create database: employeemanagementsystem
# - Create login table with credentials
# - Create employee table with all required fields
# - Insert sample data and default admin credentials
```

### Step 3: Compilation
```bash
# Run the compilation script
compile.bat

# This will:
# - Create 'out' directory if not exists
# - Compile all Java files from employee\management\system\
# - Place compiled .class files in out/ directory
# - Use libraries from lib/ directory
```

**Compile Command Breakdown:**
```batch
javac -d out -cp "lib\*;." employee\management\system\*.java
```
- `-d out` → Output directory for compiled classes
- `-cp "lib\*;."` → Classpath including all JAR files and current directory
- Compiles all Java files in `employee\management\system\` directory

### Step 4: Run Application
```bash
# Execute the run script
run.bat

# This will:
# - Start the Java WebServer
# - Make application accessible via web browser
# - Application runs from compiled classes in 'out' directory
```

**Run Command Breakdown:**
```batch
java -cp "out;lib\*;." employee.management.system.WebServer
```
- Main class: `employee.management.system.WebServer`
- Classpath includes compiled classes and all libraries

### Step 5: Access Application
- Open browser and navigate to: `http://localhost:[PORT]`
- Default credentials:
  - Username: `admin` / Password: `admin@123`
  - Username: `Gunji Venkatesh` / Password: `Gunji@143`

## 🛠️ Tools Used

| Tool | Purpose | Usage |
|------|---------|-------|
| **Java (JDK)** | Backend development & compilation | Writing and compiling Java code |
| **MySQL** | Database management | Storing employee and login data |
| **javac** | Java compiler | `compile.bat` - Compiles source to bytecode |
| **java** | Java runtime | `run.bat` - Executes application |
| **Git** | Version control | Code management and collaboration |
| **GitHub** | Repository hosting | Project storage and collaboration |
| **VS Code** | Code editor | Development environment |
| **Batch Scripts** | Automation | `compile.bat` and `run.bat` for easy execution |
| **MySQL JDBC** | Database driver | Java to MySQL database connectivity |

## 📦 Installation

### Prerequisites
- **Java Development Kit (JDK)** - Version 8 or higher
- **MySQL Server** - Version 5.7 or higher
- **Git** - For cloning the repository
- **Windows OS** - For batch script execution (or use equivalent for other OS)

### Setup Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/venkygcu/EMPLOYEE-MANAGEMENT-SYSTEM.git
   cd EMPLOYEE-MANAGEMENT-SYSTEM
   ```

2. **Setup Database**
   ```bash
   # Start MySQL service
   mysql -u root -p
   
   # Import database schema
   source database_schema.sql;
   ```

3. **Verify Java Installation**
   ```bash
   java -version
   javac -version
   ```

4. **Update Path Configuration**
   - Open `compile.bat` and `run.bat`
   - Update the path if your project is not at `C:\EMPLOYEE MANAGEMENT SYSTEM`
   - Save the changes

5. **Compile the Project**
   ```bash
   compile.bat
   # Wait for "Compilation successful!" message
   ```

6. **Run the Application**
   ```bash
   run.bat
   # Application will start and show server status
   ```

## 🚀 Usage

### Login
1. Access the application via web browser
2. Use default credentials:
   - Admin: `admin` / `admin@123`
   - User: `Gunji Venkatesh` / `Gunji@143`

### Managing Employees
1. Navigate to Employee Management section
2. View all employees or search by criteria
3. Add new employee with required details
4. Update or delete employee information
5. View employee details and history

### Employee Information Fields
- Employee ID (Auto-generated)
- Name
- Father's Name
- Date of Birth
- Salary
- Address
- Phone Number
- Email
- Education Qualification
- Designation
- Aadhar Number

## 📁 Project Structure

```
EMPLOYEE-MANAGEMENT-SYSTEM/
├── employee/                       # Java source files
│   └── management/
│       └── system/
│           ├── *.java             # All Java classes
│           └── WebServer.java     # Main web server entry point
├── web/                           # Web resources
│   ├── index.html
│   ├── css/
│   └── js/
├── lib/                           # Library dependencies
│   ├── mysql-connector-java-*.jar # MySQL JDBC driver
│   └── other-libraries.jar
├── icons/                         # Application icons
├── out/                           # Compiled output (.class files)
├── .vscode/                       # VS Code configuration
├── .kombai/                       # Build automation config
├── compile.bat                    # Compilation script
├── run.bat                        # Execution script
├── database_schema.sql            # Database setup script
└── README.md                      # This file
```

## 🗄️ Database Schema

### Database: `employeemanagementsystem`

#### Login Table
```sql
CREATE TABLE login (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);
```

#### Employee Table
```sql
CREATE TABLE employee (
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
```

### Default Login Credentials
- **Username**: admin | **Password**: admin@123
- **Username**: Gunji Venkatesh | **Password**: Gunji@143

### Sample Employee Data
- Employee ID: EMP0001
- Name: Rajesh Kumar
- Father's Name: Suresh Kumar
- DOB: 1990-05-15
- Designation: Developer
- Education: B.Tech

## 🔧 Build & Compilation Details

### Compile Script (compile.bat)
```batch
@echo off
setlocal enabledelayedexpansion

cd /d "C:\EMPLOYEE MANAGEMENT SYSTEM"

if not exist out mkdir out

echo Compiling Java files...
javac -d out -cp "lib\*;." employee\management\system\*.java

if %ERRORLEVEL% equ 0 (
    echo Compilation successful!
) else (
    echo Compilation failed with errors.
)
pause
```

### Run Script (run.bat)
```batch
@echo off
setlocal enabledelayedexpansion

cd /d "C:\EMPLOYEE MANAGEMENT SYSTEM"

echo Starting Employee Management System web app...
java -cp "out;lib\*;." employee.management.system.WebServer

pause
```

## 🤝 Contributing

We welcome contributions! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Make your changes
4. Test compilation (`compile.bat`)
5. Test execution (`run.bat`)
6. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
7. Push to the branch (`git push origin feature/AmazingFeature`)
8. Open a Pull Request

### Coding Standards
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Test your changes before submitting PR
- Ensure compilation succeeds without warnings

## 📞 Support

For support, issues, or questions:
- Create an issue on GitHub
- Check the database_schema.sql for database-related issues
- Verify Java and MySQL installations
- Check compilation logs in console output

## 🔐 Security Notes

⚠️ **Important**: Change default credentials before production deployment
- Update login table with secure passwords
- Hash passwords appropriately
- Never commit sensitive credentials
- Use environment variables for database connections

---

**Project Status**: Active Development  
**Last Updated**: June 2026  
**Version**: 1.0.0  
**Author**: Gunji Venkatesh (@venkygcu)  
**License**: [Specify License]
