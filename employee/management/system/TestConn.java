package employee.management.system;

public class TestConn {
    public static void main(String[] args) {
        try (Conn c = new Conn()) {
            System.out.println("Connection Successful!");
        } catch (Exception e) {
            System.err.println("Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
