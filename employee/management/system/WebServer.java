package employee.management.system;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class WebServer {
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("EMS_PORT", "8080"));
    private static final Map<String, String> MEMORY_USERS = new LinkedHashMap<String, String>();
    private static final Map<String, Employee> MEMORY_EMPLOYEES = new LinkedHashMap<String, Employee>();

    static {
        MEMORY_USERS.put("admin", "admin@123");
        MEMORY_USERS.put("Gunji Venkatesh", "Gunji@143");
        MEMORY_EMPLOYEES.put("EMP0001", new Employee("EMP0001", "Rajesh Kumar", "Suresh Kumar", "1990-05-15", "50000", "123 Main St", "9876543210", "rajesh@example.com", "B.Tech", "Developer", "1234-5678-9012"));
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/employees", new EmployeesHandler());
        server.createContext("/api/employees/", new EmployeeDetailHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Employee management web app is running at http://localhost:" + PORT);
    }

    private static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path == null || path.isEmpty() || "/".equals(path)) {
                path = "/index.html";
            }

            if (path.startsWith("/api/")) {
                sendText(exchange, 404, "Not found");
                return;
            }

            Path webRoot = Paths.get("web").toAbsolutePath().normalize();
            Path requested = webRoot.resolve(path.substring(1)).normalize();
            if (!requested.startsWith(webRoot)) {
                sendText(exchange, 403, "Forbidden");
                return;
            }

            if (!Files.exists(requested) || Files.isDirectory(requested)) {
                if (path.endsWith("/")) {
                    requested = webRoot.resolve("index.html").normalize();
                } else {
                    sendText(exchange, 404, "Not found");
                    return;
                }
            }

            String contentType = "text/plain";
            if (path.endsWith(".html")) {
                contentType = "text/html; charset=utf-8";
            } else if (path.endsWith(".css")) {
                contentType = "text/css; charset=utf-8";
            } else if (path.endsWith(".js")) {
                contentType = "application/javascript; charset=utf-8";
            }

            byte[] data = Files.readAllBytes(requested);
            sendBytes(exchange, 200, data, contentType);
        }
    }

    private static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendText(exchange, 405, "Method not allowed");
                return;
            }

            Map<String, String> form = readForm(exchange);
            String username = form.getOrDefault("username", "").trim();
            String password = form.getOrDefault("password", "").trim();

            if (username.isEmpty() || password.isEmpty()) {
                sendJson(exchange, 400, "{\"success\":false,\"message\":\"Username and password are required.\"}");
                return;
            }

            String query = "SELECT 1 FROM login WHERE username = ? AND password = ?";
            try (Conn conn = new Conn(); PreparedStatement ps = conn.prepare(query)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        sendJson(exchange, 200, "{\"success\":true,\"message\":\"Login successful\"}");
                    } else {
                        sendJson(exchange, 401, "{\"success\":false,\"message\":\"Invalid username or password\"}");
                    }
                }
            } catch (Exception e) {
                String storedPassword = MEMORY_USERS.get(username);
                if (storedPassword != null && storedPassword.equals(password)) {
                    sendJson(exchange, 200, "{\"success\":true,\"message\":\"Login successful\"}");
                } else {
                    sendJson(exchange, 401, "{\"success\":false,\"message\":\"Invalid username or password\"}");
                }
            }
        }
    }

    private static class EmployeesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                handleList(exchange);
            } else if ("POST".equals(method)) {
                handleCreate(exchange);
            } else {
                sendText(exchange, 405, "Method not allowed");
            }
        }

        private void handleList(HttpExchange exchange) throws IOException {
            String queryString = exchange.getRequestURI().getQuery();
            String search = "";
            if (queryString != null) {
                for (String segment : queryString.split("&")) {
                    String[] pair = segment.split("=", 2);
                    if (pair.length == 2 && "search".equals(pair[0])) {
                        search = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name());
                    }
                }
            }

            List<Employee> employees = new ArrayList<Employee>();
            String sql = search.isEmpty()
                    ? "SELECT * FROM employee ORDER BY name"
                    : "SELECT * FROM employee WHERE empId = ? OR name LIKE ? ORDER BY name";
            try (Conn conn = new Conn(); PreparedStatement ps = conn.prepare(sql)) {
                if (!search.isEmpty()) {
                    ps.setString(1, search.trim());
                    ps.setString(2, "%" + search.trim() + "%");
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        employees.add(Employee.fromResultSet(rs));
                    }
                }
            } catch (Exception e) {
                employees = listEmployeesFromMemory(search);
            }

            sendJson(exchange, 200, toJson(employees));
        }

        private void handleCreate(HttpExchange exchange) throws IOException {
            Map<String, String> form = readForm(exchange);
            String name = form.getOrDefault("name", "").trim();
            if (name.isEmpty()) {
                sendJson(exchange, 400, "{\"success\":false,\"message\":\"Employee name is required\"}");
                return;
            }

            String phone = form.getOrDefault("phone", "").trim();
            String email = form.getOrDefault("email", "").trim();
            if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
                sendJson(exchange, 400, "{\"success\":false,\"message\":\"Phone number must be 10 digits\"}");
                return;
            }
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                sendJson(exchange, 400, "{\"success\":false,\"message\":\"Invalid email format\"}");
                return;
            }

            try (Conn conn = new Conn()) {
                String empId = form.getOrDefault("empId", "").trim();
                if (empId.isEmpty()) {
                    empId = conn.getNextEmployeeId();
                }
                String sql = "INSERT INTO employee (empId, name, fatherName, dob, salary, address, phone, email, education, designation, aadhar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepare(sql)) {
                    ps.setString(1, empId);
                    ps.setString(2, name);
                    ps.setString(3, emptyToNull(form.get("fatherName")));
                    ps.setString(4, emptyToNull(form.get("dob")));
                    ps.setString(5, emptyToNull(form.get("salary")));
                    ps.setString(6, emptyToNull(form.get("address")));
                    ps.setString(7, emptyToNull(form.get("phone")));
                    ps.setString(8, emptyToNull(form.get("email")));
                    ps.setString(9, emptyToNull(form.get("education")));
                    ps.setString(10, emptyToNull(form.get("designation")));
                    ps.setString(11, emptyToNull(form.get("aadhar")));
                    ps.executeUpdate();
                }
                sendJson(exchange, 201, "{\"success\":true,\"message\":\"Employee created successfully\",\"empId\":\"" + empId + "\"}");
            } catch (Exception e) {
                String empId = form.getOrDefault("empId", "").trim();
                if (empId.isEmpty()) {
                    empId = nextMemoryEmployeeId();
                }
                MEMORY_EMPLOYEES.put(empId, new Employee(empId, name, emptyToNull(form.get("fatherName")), emptyToNull(form.get("dob")), emptyToNull(form.get("salary")), emptyToNull(form.get("address")), emptyToNull(form.get("phone")), emptyToNull(form.get("email")), emptyToNull(form.get("education")), emptyToNull(form.get("designation")), emptyToNull(form.get("aadhar"))));
                sendJson(exchange, 201, "{\"success\":true,\"message\":\"Employee created successfully\",\"empId\":\"" + empId + "\"}");
            }
        }
    }

    private static class EmployeeDetailHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String[] parts = exchange.getRequestURI().getPath().split("/");
            if (parts.length < 3) {
                sendText(exchange, 404, "Not found");
                return;
            }
            String empId = parts[parts.length - 1];

            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                handleGet(exchange, empId);
            } else if ("PUT".equals(method)) {
                handleUpdate(exchange, empId);
            } else if ("DELETE".equals(method)) {
                handleDelete(exchange, empId);
            } else {
                sendText(exchange, 405, "Method not allowed");
            }
        }

        private void handleGet(HttpExchange exchange, String empId) throws IOException {
            String sql = "SELECT * FROM employee WHERE empId = ?";
            try (Conn conn = new Conn(); PreparedStatement ps = conn.prepare(sql)) {
                ps.setString(1, empId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        sendJson(exchange, 200, toJson(Employee.fromResultSet(rs)));
                    } else {
                        sendJson(exchange, 404, "{\"success\":false,\"message\":\"Employee not found\"}");
                    }
                }
            } catch (Exception e) {
                Employee memoryEmployee = MEMORY_EMPLOYEES.get(empId);
                if (memoryEmployee != null) {
                    sendJson(exchange, 200, toJson(memoryEmployee));
                } else {
                    sendJson(exchange, 404, "{\"success\":false,\"message\":\"Employee not found\"}");
                }
            }
        }

        private void handleUpdate(HttpExchange exchange, String empId) throws IOException {
            Map<String, String> form = readForm(exchange);
            String sql = "UPDATE employee SET fatherName = ?, salary = ?, address = ?, phone = ?, email = ?, designation = ? WHERE empId = ?";
            try (Conn conn = new Conn(); PreparedStatement ps = conn.prepare(sql)) {
                ps.setString(1, emptyToNull(form.get("fatherName")));
                ps.setString(2, emptyToNull(form.get("salary")));
                ps.setString(3, emptyToNull(form.get("address")));
                ps.setString(4, emptyToNull(form.get("phone")));
                ps.setString(5, emptyToNull(form.get("email")));
                ps.setString(6, emptyToNull(form.get("designation")));
                ps.setString(7, empId);
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    sendJson(exchange, 200, "{\"success\":true,\"message\":\"Employee updated successfully\"}");
                } else {
                    sendJson(exchange, 404, "{\"success\":false,\"message\":\"Employee not found\"}");
                }
            } catch (Exception e) {
                Employee existing = MEMORY_EMPLOYEES.get(empId);
                if (existing != null) {
                    MEMORY_EMPLOYEES.put(empId, new Employee(empId, existing.name, emptyToNull(form.get("fatherName")), existing.dob, emptyToNull(form.get("salary")), emptyToNull(form.get("address")), emptyToNull(form.get("phone")), emptyToNull(form.get("email")), existing.education, emptyToNull(form.get("designation")), existing.aadhar));
                    sendJson(exchange, 200, "{\"success\":true,\"message\":\"Employee updated successfully\"}");
                } else {
                    sendJson(exchange, 404, "{\"success\":false,\"message\":\"Employee not found\"}");
                }
            }
        }

        private void handleDelete(HttpExchange exchange, String empId) throws IOException {
            String sql = "DELETE FROM employee WHERE empId = ?";
            try (Conn conn = new Conn(); PreparedStatement ps = conn.prepare(sql)) {
                ps.setString(1, empId);
                int deleted = ps.executeUpdate();
                if (deleted > 0) {
                    sendJson(exchange, 200, "{\"success\":true,\"message\":\"Employee deleted successfully\"}");
                } else {
                    sendJson(exchange, 404, "{\"success\":false,\"message\":\"Employee not found\"}");
                }
            } catch (Exception e) {
                if (MEMORY_EMPLOYEES.remove(empId) != null) {
                    sendJson(exchange, 200, "{\"success\":true,\"message\":\"Employee deleted successfully\"}");
                } else {
                    sendJson(exchange, 404, "{\"success\":false,\"message\":\"Employee not found\"}");
                }
            }
        }
    }

    private static Map<String, String> readForm(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int read;
        while ((read = inputStream.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        String body = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        Map<String, String> values = new LinkedHashMap<>();
        if (body.isEmpty()) {
            return values;
        }
        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 0) {
                continue;
            }
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8.name());
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8.name()) : "";
            values.put(key, value);
        }
        return values;
    }

    private static String emptyToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private static List<Employee> listEmployeesFromMemory(String search) {
        List<Employee> employees = new ArrayList<Employee>();
        for (Employee employee : MEMORY_EMPLOYEES.values()) {
            if (search == null || search.trim().isEmpty()) {
                employees.add(employee);
            } else {
                String term = search.trim();
                if (employee.empId.contains(term) || (employee.name != null && employee.name.toLowerCase().contains(term.toLowerCase()))) {
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

    private static String nextMemoryEmployeeId() {
        int maxId = 0;
        for (String empId : MEMORY_EMPLOYEES.keySet()) {
            if (empId != null && empId.startsWith("EMP")) {
                String numericPart = empId.substring(3);
                try {
                    int parsed = Integer.parseInt(numericPart);
                    if (parsed > maxId) {
                        maxId = parsed;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("EMP%04d", maxId + 1);
    }

    private static String toJson(List<Employee> employees) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"success\":true,\"employees\":[");
        for (int i = 0; i < employees.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(employees.get(i).toJson());
        }
        builder.append("]}");
        return builder.toString();
    }

    private static String toJson(Employee employee) {
        return "{\"success\":true,\"employee\":" + employee.toJson() + "}";
    }

    private static void sendJson(HttpExchange exchange, int status, String body) throws IOException {
        sendBytes(exchange, status, body.getBytes(StandardCharsets.UTF_8), "application/json; charset=utf-8");
    }

    private static void sendText(HttpExchange exchange, int status, String body) throws IOException {
        sendBytes(exchange, status, body.getBytes(StandardCharsets.UTF_8), "text/plain; charset=utf-8");
    }

    private static void sendBytes(HttpExchange exchange, int status, byte[] body, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    private static class Employee {
        private final String empId;
        private final String name;
        private final String fatherName;
        private final String dob;
        private final String salary;
        private final String address;
        private final String phone;
        private final String email;
        private final String education;
        private final String designation;
        private final String aadhar;

        private Employee(String empId, String name, String fatherName, String dob, String salary, String address, String phone, String email, String education, String designation, String aadhar) {
            this.empId = empId;
            this.name = name;
            this.fatherName = fatherName;
            this.dob = dob;
            this.salary = salary;
            this.address = address;
            this.phone = phone;
            this.email = email;
            this.education = education;
            this.designation = designation;
            this.aadhar = aadhar;
        }

        static Employee fromResultSet(ResultSet rs) throws SQLException {
            return new Employee(
                    rs.getString("empId"),
                    rs.getString("name"),
                    rs.getString("fatherName"),
                    rs.getString("dob"),
                    rs.getString("salary"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("education"),
                    rs.getString("designation"),
                    rs.getString("aadhar")
            );
        }

        String toJson() {
            return "{\"empId\":\"" + escapeJson(empId) + "\",\"name\":\"" + escapeJson(name) + "\",\"fatherName\":\"" + escapeJson(fatherName) + "\",\"dob\":\"" + escapeJson(dob) + "\",\"salary\":\"" + escapeJson(salary) + "\",\"address\":\"" + escapeJson(address) + "\",\"phone\":\"" + escapeJson(phone) + "\",\"email\":\"" + escapeJson(email) + "\",\"education\":\"" + escapeJson(education) + "\",\"designation\":\"" + escapeJson(designation) + "\",\"aadhar\":\"" + escapeJson(aadhar) + "\"}";
        }
    }

    private static String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
