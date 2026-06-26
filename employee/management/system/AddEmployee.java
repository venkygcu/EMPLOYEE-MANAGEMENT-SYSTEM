package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddEmployee extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfaddress, tfphone, tfmail, tfsalary, tfdesig, tfaadhar, tfdob;
    JComboBox<String> cbeducation;
    JLabel lblempId;
    JButton add, back;

    public AddEmployee() {
        setTitle("Add Employee Details - Employee Management System");
        setLayout(null);

        // Background Image Canvas Integration
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/add.employee.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(i2));
        background.setBounds(0, 0, 900, 600);
        background.setLayout(null);
        add(background);

        // Heading rendered over background
        JLabel heading = new JLabel("Add Employee Details");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 25));
        heading.setForeground(Color.BLACK);
        background.add(heading);

        // Interactive Input Form Fields
        JLabel labelname = new JLabel("Name");
        labelname.setBounds(50, 130, 150, 30);
        labelname.setFont(new Font("serif", Font.BOLD, 18));
        labelname.setForeground(Color.BLACK);
        background.add(labelname);

        tfname = new JTextField();
        tfname.setBounds(200, 130, 150, 30);
        background.add(tfname);

        JLabel labelfname = new JLabel("Father's Name");
        labelfname.setBounds(430, 130, 150, 30);
        labelfname.setFont(new Font("serif", Font.BOLD, 18));
        labelfname.setForeground(Color.BLACK);
        background.add(labelfname);

        tffname = new JTextField();
        tffname.setBounds(620, 130, 150, 30);
        background.add(tffname);

        JLabel labeldob = new JLabel("DOB (YYYY-MM-DD)");
        labeldob.setBounds(50, 190, 150, 30);
        labeldob.setFont(new Font("serif", Font.BOLD, 16));
        labeldob.setForeground(Color.BLACK);
        background.add(labeldob);

        tfdob = new JTextField();
        tfdob.setBounds(200, 190, 150, 30);
        background.add(tfdob);

        JLabel labelsalary = new JLabel("Salary");
        labelsalary.setBounds(430, 190, 150, 30);
        labelsalary.setFont(new Font("serif", Font.BOLD, 18));
        labelsalary.setForeground(Color.BLACK);
        background.add(labelsalary);

        tfsalary = new JTextField();
        tfsalary.setBounds(620, 190, 150, 30);
        background.add(tfsalary);

        JLabel labeladdress = new JLabel("Address");
        labeladdress.setBounds(50, 250, 150, 30);
        labeladdress.setFont(new Font("serif", Font.BOLD, 18));
        labeladdress.setForeground(Color.BLACK);
        background.add(labeladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(200, 250, 150, 30);
        background.add(tfaddress);

        JLabel labelphone = new JLabel("Phone");
        labelphone.setBounds(430, 250, 150, 30);
        labelphone.setFont(new Font("serif", Font.BOLD, 18));
        labelphone.setForeground(Color.BLACK);
        background.add(labelphone);

        tfphone = new JTextField();
        tfphone.setBounds(620, 250, 150, 30);
        background.add(tfphone);

        JLabel labelemail = new JLabel("Email");
        labelemail.setBounds(50, 310, 150, 30);
        labelemail.setFont(new Font("serif", Font.BOLD, 18));
        labelemail.setForeground(Color.BLACK);
        background.add(labelemail);

        tfmail = new JTextField();
        tfmail.setBounds(200, 310, 150, 30);
        background.add(tfmail);

        JLabel labeleducation = new JLabel("Highest Education");
        labeleducation.setBounds(430, 310, 150, 30);
        labeleducation.setFont(new Font("serif", Font.BOLD, 18));
        labeleducation.setForeground(Color.BLACK);
        background.add(labeleducation);

        String[] items = {"BTECH", "BBA", "BCA", "BSC", "BA", "MTECH", "MBA", "MCA", "MSC", "PHD"};
        cbeducation = new JComboBox<>(items);
        cbeducation.setBounds(620, 310, 150, 30);
        cbeducation.setBackground(Color.WHITE);
        background.add(cbeducation);

        JLabel labeldesig = new JLabel("Designation");
        labeldesig.setBounds(50, 370, 150, 30);
        labeldesig.setFont(new Font("serif", Font.BOLD, 18));
        labeldesig.setForeground(Color.BLACK);
        background.add(labeldesig);

        tfdesig = new JTextField();
        tfdesig.setBounds(200, 370, 150, 30);
        background.add(tfdesig);

        JLabel labelaadhar = new JLabel("Aadhaar Number");
        labelaadhar.setBounds(430, 370, 150, 30);
        labelaadhar.setFont(new Font("serif", Font.BOLD, 18));
        labelaadhar.setForeground(Color.BLACK);
        background.add(labelaadhar);

        tfaadhar = new JTextField();
        tfaadhar.setBounds(620, 370, 150, 30);
        background.add(tfaadhar);

        JLabel labelempId = new JLabel("Employee ID:");
        labelempId.setBounds(50, 430, 150, 30);
        labelempId.setFont(new Font("serif", Font.BOLD, 18));
        labelempId.setForeground(Color.BLACK);
        background.add(labelempId);

        lblempId = new JLabel();
        lblempId.setBounds(200, 430, 150, 30);
        lblempId.setFont(new Font("serif", Font.BOLD, 20));
        lblempId.setForeground(Color.BLUE);
        background.add(lblempId);

        // Fetch dynamic auto-incremented tracking number sequence
        try (Conn c = new Conn()) {
            lblempId.setText(c.getNextEmployeeId());
        } catch (Exception e) {
            lblempId.setText("EMP0001");
            e.printStackTrace();
        }

        // Action Controlling Operations Layer
        add = new JButton("Add Details");
        add.setBounds(250, 500, 150, 40);
        add.addActionListener(this);
        add.setBackground(Color.BLACK);
        add.setForeground(Color.WHITE);
        add.setFont(new Font("Tahoma", Font.BOLD, 14));
        background.add(add);

        back = new JButton("Back");
        back.setBounds(450, 500, 150, 40);
        back.addActionListener(this);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Tahoma", Font.BOLD, 14));
        background.add(back);

        // Absolute window configuration definitions
        setSize(900, 600);
        setLocation(300, 50);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String name = tfname.getText().trim();
            String fatherName = tffname.getText().trim();
            String dob = tfdob.getText().trim();
            String salary = tfsalary.getText().trim();
            String address = tfaddress.getText().trim();
            String phone = tfphone.getText().trim();
            String email = tfmail.getText().trim();
            String education = (String) cbeducation.getSelectedItem();
            String designation = tfdesig.getText().trim();
            String aadhar = tfaadhar.getText().trim();
            String empId = lblempId.getText();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required.");
                return;
            }
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone number must be a 10-digit number.");
                return;
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format.");
                return;
            }

            try (Conn c = new Conn()) {
                String query = "INSERT INTO employee (empId, name, fatherName, dob, salary, address, phone, email, education, designation, aadhar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                java.sql.PreparedStatement ps = c.prepare(query);
                ps.setString(1, empId);
                ps.setString(2, name);
                ps.setString(3, fatherName);
                ps.setString(4, dob.isEmpty() ? null : dob);
                ps.setString(5, salary);
                ps.setString(6, address);
                ps.setString(7, phone);
                ps.setString(8, email);
                ps.setString(9, education);
                ps.setString(10, designation);
                ps.setString(11, aadhar);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Details added successfully");
                setVisible(false);
                dispose();
                new Home();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting details: " + e.getMessage());
            }
        } else {
            setVisible(false);
            dispose();
            new Home();
        }
    }

    public static void main(String[] args) {
        new AddEmployee();
    }
}