package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RemoveEmployee extends JFrame implements ActionListener {

    JTextField tfEmpId;
    JTextArea taDetails;
    JButton btnSearch, btnDelete, btnBack;

    public RemoveEmployee() {
        setTitle("Remove Employee");
        setLayout(null);

        // Background Image Canvas Integration
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/delete.jpg"));
        Image i2 = i1.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(i2));
        background.setBounds(0, 0, 800, 500);
        background.setLayout(null);
        add(background);

        // UI components rendered directly over the background canvas
        JLabel heading = new JLabel("Remove Employee");
        heading.setBounds(300, 20, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        heading.setForeground(Color.BLACK);
        background.add(heading);

        JLabel lblEmpId = new JLabel("Enter Employee ID:");
        lblEmpId.setBounds(50, 80, 150, 30);
        lblEmpId.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmpId.setForeground(Color.BLACK);
        background.add(lblEmpId);

        tfEmpId = new JTextField();
        tfEmpId.setBounds(200, 80, 200, 30);
        tfEmpId.setFont(new Font("Tahoma", Font.PLAIN, 14));
        background.add(tfEmpId);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(420, 80, 100, 30);
        btnSearch.setBackground(Color.BLACK);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(this);
        background.add(btnSearch);

        taDetails = new JTextArea();
        taDetails.setBounds(50, 130, 700, 200);
        taDetails.setEditable(false);
        taDetails.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // Soft background color for readability on top of the image
        taDetails.setBackground(new Color(255, 255, 255, 220)); 
        background.add(taDetails);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(250, 360, 150, 40);
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnDelete.addActionListener(this);
        background.add(btnDelete);

        btnBack = new JButton("Back");
        btnBack.setBounds(420, 360, 150, 40);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnBack.addActionListener(this);
        background.add(btnBack);

        // Frame Layout Window Specifications
        setSize(800, 500);
        setLocation(300, 150);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String empId = tfEmpId.getText().trim();

        if (ae.getSource() == btnSearch) {
            if (empId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Employee ID.");
                return;
            }

            try (Conn c = new Conn()) {
                String query = "SELECT * FROM employee WHERE empId = ?";
                PreparedStatement ps = c.prepare(query);
                ps.setString(1, empId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" Name: ").append(rs.getString("name")).append("\n");
                    sb.append(" DOB: ").append(rs.getString("dob")).append("\n");
                    sb.append(" Father's Name: ").append(rs.getString("fatherName")).append("\n");
                    sb.append(" Address: ").append(rs.getString("address")).append("\n");
                    sb.append(" Email: ").append(rs.getString("email")).append("\n");
                    sb.append(" Phone: ").append(rs.getString("phone")).append("\n");
                    sb.append(" Designation: ").append(rs.getString("designation")).append("\n");
                    sb.append(" Salary: ").append(rs.getString("salary")).append("\n");
                    sb.append(" Education: ").append(rs.getString("education")).append("\n");
                    sb.append(" Aadhaar: ").append(rs.getString("aadhar")).append("\n");

                    taDetails.setText(sb.toString());
                } else {
                    taDetails.setText("");
                    JOptionPane.showMessageDialog(this, "Employee not found.");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                e.printStackTrace();
            }

        } else if (ae.getSource() == btnDelete) {
            if (empId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Employee ID.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Conn c = new Conn()) {
                    String query = "DELETE FROM employee WHERE empId = ?";
                    PreparedStatement ps = c.prepare(query);
                    ps.setString(1, empId);
                    int result = ps.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                        taDetails.setText("");
                        tfEmpId.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Delete failed. Employee ID not found.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            new Home();
        }
    }

    public static void main(String[] args) {
        new RemoveEmployee();
    }
}