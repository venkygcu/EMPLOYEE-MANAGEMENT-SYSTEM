package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewEmployee extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JTextField tfSearch;
    JButton btnSearch, btnPrint, btnUpdate, btnBack;

    public ViewEmployee() {
        setTitle("View Employee Details");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Search / View Employee");
        heading.setBounds(420, 20, 400, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(heading);

        JLabel lblSearch = new JLabel("Search by Employee ID / Name:");
        lblSearch.setBounds(50, 80, 220, 30);
        lblSearch.setFont(new Font("serif", Font.PLAIN, 16));
        add(lblSearch);

        tfSearch = new JTextField();
        tfSearch.setBounds(280, 80, 200, 30);
        add(tfSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(500, 80, 100, 30);
        btnSearch.setBackground(Color.BLACK);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(this);
        add(btnSearch);

        btnPrint = new JButton("Print Table");
        btnPrint.setBounds(620, 80, 120, 30);
        btnPrint.setBackground(Color.BLACK);
        btnPrint.setForeground(Color.WHITE);
        btnPrint.addActionListener(this);
        add(btnPrint);

        btnUpdate = new JButton("Update Employee");
        btnUpdate.setBounds(760, 80, 150, 30);
        btnUpdate.setBackground(Color.BLACK);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnBack = new JButton("Back");
        btnBack.setBounds(930, 80, 100, 30);
        btnBack.setBackground(Color.BLACK);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        add(btnBack);

        String[] columns = {
            "Emp ID", "Name", "Father's Name", "DOB", "Salary", 
            "Address", "Phone", "Email", "Education", "Designation", "Aadhaar"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(30, 140, 1040, 480);
        add(jsp);

        loadEmployeeData("");

        setSize(1120, 680);
        setLocation(250, 50);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadEmployeeData(String searchTerm) {
        model.setRowCount(0);
        String query;
        boolean hasFilter = !searchTerm.trim().isEmpty();

        if (hasFilter) {
            query = "SELECT * FROM employee WHERE empId = ? OR name LIKE ?";
        } else {
            query = "SELECT * FROM employee";
        }

        try (Conn c = new Conn()) {
            PreparedStatement ps = c.prepare(query);
            if (hasFilter) {
                ps.setString(1, searchTerm.trim());
                ps.setString(2, "%" + searchTerm.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] rowData = {
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
                };
                model.addRow(rowData);
            }
            
            if (model.getRowCount() == 0 && hasFilter) {
                JOptionPane.showMessageDialog(this, "No matching records found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching records: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSearch) {
            loadEmployeeData(tfSearch.getText());
        } else if (ae.getSource() == btnPrint) {
            try {
                boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, 
                    new java.text.MessageFormat("Employee Directory Summary"), 
                    new java.text.MessageFormat("Page {0}"));
                if (complete) {
                    JOptionPane.showMessageDialog(this, "Printing completed successfully.");
                }
            } catch (java.awt.print.PrinterException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Printing execution failed: " + e.getMessage());
            }
        } else if (ae.getSource() == btnUpdate) {
            int selectedRow = table.getSelectedRow();
            String selectedEmpId = null;
            if (selectedRow >= 0) {
                selectedEmpId = (String) model.getValueAt(selectedRow, 0);
            }
            setVisible(false);
            dispose();
            new UpdateEmployee(selectedEmpId);
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            new Home();
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}