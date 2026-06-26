package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateEmployee extends JFrame implements ActionListener {
    JTextField tfname, tffname, tfaddress, tfphone, tfmail, tfsalary, tfdesig, tfEmpIdInput;
    JLabel lblempId;
    JButton update, back, load;
    String empId;

    public UpdateEmployee() {
        this(null);
    }

    public UpdateEmployee(String prefilledEmpId) {
        setTitle("Update Employee Details - Employee Management System");
        setLayout(null);
        
        // Background Image Canvas Integration
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/print.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(i2));
        background.setBounds(0, 0, 900, 600);
        background.setLayout(null);
        add(background);
        
        // UI Framework Heading Component
        JLabel heading = new JLabel("Update Employee Details");
        heading.setBounds(320, 20, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 25));
        heading.setForeground(Color.BLACK);
        background.add(heading);
        
        // Data Record Retrieval Inputs Group
        JLabel inputIdLabel = new JLabel("Enter Employee Id:");
        inputIdLabel.setBounds(50, 90, 150, 30);
        inputIdLabel.setFont(new Font("serif", Font.BOLD, 18));
        inputIdLabel.setForeground(Color.BLACK);
        background.add(inputIdLabel);
        
        tfEmpIdInput = new JTextField();
        tfEmpIdInput.setBounds(200, 90, 150, 30);
        tfEmpIdInput.setFont(new Font("Tahoma", Font.PLAIN, 14));
        background.add(tfEmpIdInput);
        
        load = new JButton("Load Record");
        load.setBounds(370, 90, 130, 30);
        load.setBackground(Color.BLACK);
        load.setForeground(Color.WHITE);
        load.setFont(new Font("Tahoma", Font.BOLD, 12));
        load.addActionListener(this);
        background.add(load);
        
        // Form Display & Entry Mapping Parameters
        JLabel labelempId = new JLabel("Employee Id:");
        labelempId.setBounds(50, 150, 150, 30);
        labelempId.setFont(new Font("serif", Font.BOLD, 18));
        labelempId.setForeground(Color.BLACK);
        background.add(labelempId);
        
        lblempId = new JLabel();
        lblempId.setBounds(200, 150, 150, 30);
        lblempId.setFont(new Font("serif", Font.BOLD, 18));
        lblempId.setForeground(Color.BLUE);
        background.add(lblempId);
        
        JLabel labelname = new JLabel("Name");
        labelname.setBounds(50, 200, 150, 30);
        labelname.setFont(new Font("serif", Font.BOLD, 18));
        labelname.setForeground(Color.BLACK);
        background.add(labelname);
        
        tfname = new JTextField();
        tfname.setBounds(200, 200, 150, 30);
        tfname.setEditable(false); // Structural immutable validation key safeguard
        background.add(tfname);
        
        JLabel labelfname = new JLabel("Father's Name");
        labelfname.setBounds(430, 200, 150, 30);
        labelfname.setFont(new Font("serif", Font.BOLD, 18));
        labelfname.setForeground(Color.BLACK);
        background.add(labelfname);
        
        tffname = new JTextField();
        tffname.setBounds(620, 200, 150, 30);
        background.add(tffname);
        
        JLabel labeladdress = new JLabel("Address");
        labeladdress.setBounds(50, 260, 150, 30);
        labeladdress.setFont(new Font("serif", Font.BOLD, 18));
        labeladdress.setForeground(Color.BLACK);
        background.add(labeladdress);
        
        tfaddress = new JTextField();
        tfaddress.setBounds(200, 260, 150, 30);
        background.add(tfaddress);
        
        JLabel labelphone = new JLabel("Phone");
        labelphone.setBounds(430, 260, 150, 30);
        labelphone.setFont(new Font("serif", Font.BOLD, 18));
        labelphone.setForeground(Color.BLACK);
        background.add(labelphone);
        
        tfphone = new JTextField();
        tfphone.setBounds(620, 260, 150, 30);
        background.add(tfphone);
        
        JLabel labelemail = new JLabel("Email");
        labelemail.setBounds(50, 320, 150, 30);
        labelemail.setFont(new Font("serif", Font.BOLD, 18));
        labelemail.setForeground(Color.BLACK);
        background.add(labelemail);
        
        tfmail = new JTextField();
        tfmail.setBounds(200, 320, 150, 30);
        background.add(tfmail);
        
        JLabel labelsalary = new JLabel("Salary");
        labelsalary.setBounds(430, 320, 150, 30);
        labelsalary.setFont(new Font("serif", Font.BOLD, 18));
        labelsalary.setForeground(Color.BLACK);
        background.add(labelsalary);
        
        tfsalary = new JTextField();
        tfsalary.setBounds(620, 320, 150, 30);
        background.add(tfsalary);
        
        JLabel labeldesig = new JLabel("Designation");
        labeldesig.setBounds(50, 380, 150, 30);
        labeldesig.setFont(new Font("serif", Font.BOLD, 18));
        labeldesig.setForeground(Color.BLACK);
        background.add(labeldesig);
        
        tfdesig = new JTextField();
        tfdesig.setBounds(200, 380, 150, 30);
        background.add(tfdesig);
        
        // Transaction Execution Buttons
        update = new JButton("Update Details");
        update.setBounds(250, 480, 150, 40);
        update.setBackground(Color.BLACK);
        update.setForeground(Color.WHITE);
        update.setFont(new Font("Tahoma", Font.BOLD, 14));
        update.addActionListener(this);
        background.add(update);
        
        back = new JButton("Back");
        back.setBounds(450, 480, 150, 40);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Tahoma", Font.BOLD, 14));
        back.addActionListener(this);
        background.add(back);
        
        // Frame Window Spatial Formats
        setSize(900, 600);
        setLocation(300, 50);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pre-load employee if ID was passed (e.g. from ViewEmployee)
        if (prefilledEmpId != null && !prefilledEmpId.isEmpty()) {
            tfEmpIdInput.setText(prefilledEmpId);
            loadEmployee(prefilledEmpId);
        }
    }
    
    private void loadEmployee(String id) {
        try (Conn c = new Conn()) {
            String query = "select * from employee where empId = ?";
            PreparedStatement ps = c.prepare(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                empId = rs.getString("empId");
                lblempId.setText(empId);
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fatherName"));
                tfaddress.setText(rs.getString("address"));
                tfphone.setText(rs.getString("phone"));
                tfmail.setText(rs.getString("email"));
                tfsalary.setText(rs.getString("salary"));
                tfdesig.setText(rs.getString("designation"));
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == load) {
            String id = tfEmpIdInput.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Employee ID to load");
                return;
            }
            loadEmployee(id);
        } else if (ae.getSource() == update) {
            if (empId == null || empId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please load an employee's record before updating.");
                return;
            }

            String fatherName = tffname.getText().trim();
            String salary = tfsalary.getText().trim();
            String address = tfaddress.getText().trim();
            String phone = tfphone.getText().trim();
            String email = tfmail.getText().trim();
            String designation = tfdesig.getText().trim();
            
            if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.");
                return;
            }

            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format.");
                return;
            }
            
            try (Conn c = new Conn()) {
                String query = "update employee set fatherName = ?, salary = ?, address = ?, phone = ?, email = ?, designation = ? where empId = ?";
                PreparedStatement ps = c.prepare(query);
                ps.setString(1, fatherName.isEmpty() ? null : fatherName);
                ps.setString(2, salary.isEmpty() ? null : salary);
                ps.setString(3, address.isEmpty() ? null : address);
                ps.setString(4, phone.isEmpty() ? null : phone);
                ps.setString(5, email.isEmpty() ? null : email);
                ps.setString(6, designation.isEmpty() ? null : designation);
                ps.setString(7, empId);
                
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(null, "Details updated successfully");
                    setVisible(false);
                    dispose();
                    new Home();
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            dispose();
            new Home();
        }
    }

    public static void main(String[] args) {
        new UpdateEmployee();
    }
}