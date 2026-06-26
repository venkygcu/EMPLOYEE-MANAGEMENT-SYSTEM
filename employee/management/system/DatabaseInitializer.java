package employee.management.system;

import javax.swing.*;
import java.sql.*;

public class DatabaseInitializer {
    private static final String DB_URL = System.getenv("EMS_DB_ADMIN_URL") != null
            ? System.getenv("EMS_DB_ADMIN_URL")
            : "jdbc:mysql://127.0.0.1:3306?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=5000&socketTimeout=5000";
    private static final String DB_USER = System.getenv("EMS_DB_ADMIN_USER") != null
            ? System.getenv("EMS_DB_ADMIN_USER")
            : (System.getenv("EMS_DB_USER") != null ? System.getenv("EMS_DB_USER") : "root");
    private static final String DB_PASS = System.getenv("EMS_DB_ADMIN_PASS") != null
            ? System.getenv("EMS_DB_ADMIN_PASS")
            : (System.getenv("EMS_DB_PASS") != null ? System.getenv("EMS_DB_PASS") : "gunjivenky@763");

    public static boolean initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS employeemanagementsystem");
            }

            String appDbUrl = "jdbc:mysql://127.0.0.1:3306/employeemanagementsystem?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=5000&socketTimeout=5000";
            try (Connection dbConn = DriverManager.getConnection(appDbUrl, DB_USER, DB_PASS);
                 Statement dbStmt = dbConn.createStatement()) {

                dbStmt.executeUpdate("CREATE TABLE IF NOT EXISTS login (" +
                        "username VARCHAR(50) PRIMARY KEY," +
                        "password VARCHAR(100) NOT NULL)");

                dbStmt.executeUpdate("CREATE TABLE IF NOT EXISTS employee (" +
                        "empId VARCHAR(10) PRIMARY KEY," +
                        "name VARCHAR(100) NOT NULL," +
                        "fatherName VARCHAR(100)," +
                        "dob DATE," +
                        "salary VARCHAR(50)," +
                        "address VARCHAR(200)," +
                        "phone VARCHAR(20)," +
                        "email VARCHAR(100)," +
                        "education VARCHAR(100)," +
                        "designation VARCHAR(100)," +
                        "aadhar VARCHAR(20)," +
                        "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

                dbStmt.executeUpdate("INSERT INTO login (username, password) VALUES ('Gunji Venkatesh', 'Gunji@143') ON DUPLICATE KEY UPDATE password = VALUES(password)");
                dbStmt.executeUpdate("INSERT INTO login (username, password) VALUES ('admin', 'admin@123') ON DUPLICATE KEY UPDATE password = VALUES(password)");
            }
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("Database initialization warning: " + e.getMessage());
            return false;
        }
    }
}