package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class ExportEmployee extends JFrame implements ActionListener {

    JButton btnExportCSV, btnExportTXT, btnBack;
    JLabel lblStatus;

    public ExportEmployee() {
        setTitle("Export Employee Records");
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Export Employee Records");
        heading.setBounds(300, 20, 400, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(heading);

        JLabel instruction = new JLabel("Select a format to export employee data:");
        instruction.setBounds(150, 80, 400, 30);
        instruction.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(instruction);

        btnExportCSV = new JButton("Export as CSV");
        btnExportCSV.setBounds(200, 150, 150, 40);
        btnExportCSV.addActionListener(this);
        add(btnExportCSV);

        btnExportTXT = new JButton("Export as TXT");
        btnExportTXT.setBounds(400, 150, 150, 40);
        btnExportTXT.addActionListener(this);
        add(btnExportTXT);

        lblStatus = new JLabel("");
        lblStatus.setBounds(50, 220, 600, 30);
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblStatus.setForeground(Color.BLUE);
        add(lblStatus);

        btnBack = new JButton("Back");
        btnBack.setBounds(300, 300, 100, 40);
        btnBack.addActionListener(this);
        add(btnBack);

        setSize(700, 400);
        setLocation(300, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private String safe(String val) {
        return val == null ? "" : val;
    }

    private void exportAsCSV() {
        try {
            String filePath = "employee_records.csv";
            try (FileWriter fw = new FileWriter(filePath)) {
                fw.append("Emp ID,Name,Father's Name,DOB,Salary,Address,Phone,Email,Education,Designation,Aadhaar\n");

                try (Conn c = new Conn()) {
                    String query = "SELECT * FROM employee";
                    ResultSet rs = c.getStatement().executeQuery(query);

                    while (rs.next()) {
                        fw.append(safe(rs.getString("empId"))).append(",");
                        fw.append(safe(rs.getString("name"))).append(",");
                        fw.append(safe(rs.getString("fatherName"))).append(",");
                        fw.append(safe(rs.getString("dob"))).append(",");
                        fw.append(safe(rs.getString("salary"))).append(",");
                        fw.append(safe(rs.getString("address"))).append(",");
                        fw.append(safe(rs.getString("phone"))).append(",");
                        fw.append(safe(rs.getString("email"))).append(",");
                        fw.append(safe(rs.getString("education"))).append(",");
                        fw.append(safe(rs.getString("designation"))).append(",");
                        fw.append(safe(rs.getString("aadhar"))).append("\n");
                    }
                }
            }
            lblStatus.setText("File exported successfully to: " + new File(filePath).getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Employee records exported to " + filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exportAsTXT() {
        try {
            String filePath = "employee_records.txt";
            try (FileWriter fw = new FileWriter(filePath)) {
                try (Conn c = new Conn()) {
                    String query = "SELECT * FROM employee";
                    ResultSet rs = c.getStatement().executeQuery(query);

                    fw.append("================== EMPLOYEE RECORDS ==================\n\n");

                    while (rs.next()) {
                        fw.append("Employee ID: ").append(safe(rs.getString("empId"))).append("\n");
                        fw.append("Name: ").append(safe(rs.getString("name"))).append("\n");
                        fw.append("Father's Name: ").append(safe(rs.getString("fatherName"))).append("\n");
                        fw.append("Date of Birth: ").append(safe(rs.getString("dob"))).append("\n");
                        fw.append("Salary: ").append(safe(rs.getString("salary"))).append("\n");
                        fw.append("Address: ").append(safe(rs.getString("address"))).append("\n");
                        fw.append("Phone: ").append(safe(rs.getString("phone"))).append("\n");
                        fw.append("Email: ").append(safe(rs.getString("email"))).append("\n");
                        fw.append("Education: ").append(safe(rs.getString("education"))).append("\n");
                        fw.append("Designation: ").append(safe(rs.getString("designation"))).append("\n");
                        fw.append("Aadhaar: ").append(safe(rs.getString("aadhar"))).append("\n");
                        fw.append("========================================================\n\n");
                    }
                }
            }
            lblStatus.setText("File exported successfully to: " + new File(filePath).getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Employee records exported to " + filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnExportCSV) {
            exportAsCSV();
        } else if (ae.getSource() == btnExportTXT) {
            exportAsTXT();
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ExportEmployee();
    }
}