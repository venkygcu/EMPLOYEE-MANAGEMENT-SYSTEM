package employee.management.system;

import java.sql.*;

public class Conn implements AutoCloseable {
    private static final String URL = System.getenv("EMS_DB_URL") != null ? System.getenv("EMS_DB_URL") : "jdbc:mysql://localhost:3306/employeemanagementsystem?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = System.getenv("EMS_DB_USER") != null ? System.getenv("EMS_DB_USER") : "root";
    private static final String PASS = System.getenv("EMS_DB_PASS") != null ? System.getenv("EMS_DB_PASS") : "gunjivenky@763";

    private final Connection connection;
    public Statement s;
    private PreparedStatement ps;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public Conn() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASS);
        s = connection.createStatement();
    }
    
    public PreparedStatement prepare(String sql) throws SQLException {
        if (ps != null) {
            ps.close();
        }
        ps = connection.prepareStatement(sql);
        return ps;
    }

    public Statement getStatement() throws SQLException {
        if (s == null || s.isClosed()) {
            s = connection.createStatement();
        }
        return s;
    }

    public String getNextEmployeeId() throws SQLException {
        String query = "SELECT MAX(id) AS maxId FROM (" +
            "SELECT CAST(SUBSTRING(empId, 4) AS UNSIGNED) AS id FROM employee WHERE empId LIKE 'EMP%'" +
            " UNION " +
            "SELECT CAST(empId AS UNSIGNED) AS id FROM employee WHERE empId REGEXP '^[0-9]+$'" +
            ") AS ids";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (rs.wasNull() || maxId == 0) {
                    return "EMP0001";
                }
                return String.format("EMP%04d", maxId + 1);
            }
        }
        return "EMP0001";
    }

    @Override
    public void close() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (s != null) {
                s.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {
        }
    }
}