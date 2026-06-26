package employee.management.system;

import javax.swing.*;
import java.awt.*;

public class Splash extends JFrame {

    public Splash() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        boolean dbReady = DatabaseInitializer.initialize();
        if (!dbReady) {
            JOptionPane.showMessageDialog(this,
                    "Database connection could not be established. The application will continue, but database features may be unavailable until MySQL is reachable.",
                    "Database Warning",
                    JOptionPane.WARNING_MESSAGE);
        }

        JLabel heading = new JLabel("EMPLOYEE MANAGEMENT SYSTEM");
        heading.setBounds(80, 30, 1200, 60);
        heading.setFont(new Font("serif", Font.PLAIN, 60));
        heading.setForeground(Color.RED);
        add(heading);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/front.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1100, 700, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i2));
        image.setBounds(50, 100, 1050, 500);
        image.setLayout(null);

        JButton clickhere = new JButton("CLICK HERE TO CONTINUE");
        clickhere.setBounds(400, 350, 300, 70);
        clickhere.setBackground(Color.BLACK);
        clickhere.setForeground(Color.WHITE);
        image.add(clickhere);

        Timer timer = new Timer(500, e -> heading.setVisible(!heading.isVisible()));
        timer.start();

        clickhere.addActionListener(e -> {
            timer.stop();
            setVisible(false);
            dispose();
            new Login();
        });

        add(image);

        setSize(1170, 650);
        setLocation(200, 150);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Splash();
    }
}