package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

    JTextField tfusername;
    JPasswordField tfpassword;
    JButton login, cancel, signup;
    JLabel lblModeTitle;

    public Login() {
        setTitle("Authentication Panel - Employee Management System");
        setLayout(null);

        // Background Image Canvas Integration
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/remove.jpg"));
        Image i2 = i1.getImage().getScaledInstance(650, 450, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(i2));
        background.setBounds(0, 0, 650, 450);
        background.setLayout(null);
        add(background);

        // Dynamic State / Form Label Indicator
        lblModeTitle = new JLabel("Account Login");
        lblModeTitle.setBounds(60, 30, 300, 35);
        lblModeTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblModeTitle.setForeground(Color.BLACK);
        background.add(lblModeTitle);

        // Input Fields rendered directly over the background canvas
        JLabel lblusername = new JLabel("Username:");
        lblusername.setBounds(60, 100, 120, 30);
        lblusername.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblusername.setForeground(Color.BLACK);
        background.add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(200, 100, 240, 30);
        tfusername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        background.add(tfusername);

        JLabel lblpassword = new JLabel("Password:");
        lblpassword.setBounds(60, 170, 120, 30);
        lblpassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblpassword.setForeground(Color.BLACK);
        background.add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(200, 170, 240, 30);
        tfpassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        background.add(tfpassword);

        // Primary Action Buttons Group
        login = new JButton("Login");
        login.setBounds(70, 260, 130, 40);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Tahoma", Font.BOLD, 14));
        login.addActionListener(this);
        background.add(login);

        signup = new JButton("Sign Up (New User)");
        signup.setBounds(220, 260, 180, 40);
        signup.setBackground(new Color(0, 102, 204));
        signup.setForeground(Color.WHITE);
        signup.setFont(new Font("Tahoma", Font.BOLD, 13));
        signup.addActionListener(this);
        background.add(signup);

        cancel = new JButton("Cancel");
        cancel.setBounds(420, 260, 120, 40);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 14));
        cancel.addActionListener(this);
        background.add(cancel);

        // Frame Layout Window Specifications
        setSize(650, 450);
        setLocation(450, 200);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String username = tfusername.getText().trim();
        String password = new String(tfpassword.getPassword()).trim();

        if (ae.getSource() == login) {
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both Username and Password.");
                return;
            }

            // Master Admin Override Verification Pathway
            if ("Gunji Venkatesh".equals(username) && "Gunji@143".equals(password)) {
                setVisible(false);
                dispose();
                new Home();
                return;
            }

            // Relational Database Authentication Call
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            try (Conn c = new Conn();
                 PreparedStatement ps = c.prepare(query)) {
                
                ps.setString(1, username);
                ps.setString(2, password);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        setVisible(false);
                        dispose();
                        new Home(); // Route safely to the main landing module
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password. If you are new, click 'Sign Up'.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error during authentication: " + e.getMessage());
            }

        } else if (ae.getSource() == signup) {
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "To Sign Up, please enter your desired Username and Password into the fields above.");
                return;
            }

            // Database Duplicate Username Guard Metric
            String checkQuery = "SELECT * FROM login WHERE username = ?";
            String insertQuery = "INSERT INTO login (username, password) VALUES (?, ?)";

            try (Conn c = new Conn()) {
                // Step 1: Ensure username doesn't already exist
                try (PreparedStatement checkPs = c.prepare(checkQuery)) {
                    checkPs.setString(1, username);
                    try (ResultSet rs = checkPs.executeQuery()) {
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(this, "This username is already taken. Please choose another one.");
                            return;
                        }
                    }
                }

                // Step 2: Insert new user data safely using a PreparedStatement
                try (PreparedStatement insertPs = c.prepare(insertQuery)) {
                    insertPs.setString(1, username);
                    insertPs.setString(2, password);
                    int result = insertPs.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Account created successfully!\nYou can now click 'Login' to enter.");
                        lblModeTitle.setText("Account Ready - Please Login");
                    } else {
                        JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database registration error: " + e.getMessage());
            }

        } else if (ae.getSource() == cancel) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}