package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener {

    JButton view, add, update, remove, export;

    public Home() {
        setTitle("Employee Management System - Home");
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/home.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1120, 630, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i2));
        image.setBounds(0, 0, 1120, 630);
        add(image);

        JLabel heading = new JLabel("Employee Management System");
        heading.setBounds(620, 20, 400, 40);
        heading.setFont(new Font("RALEWAY", Font.BOLD, 25));
        image.add(heading);

        add = new JButton("Add Employee");
        add.setBounds(650, 80, 150, 40);
        add.addActionListener(this);
        image.add(add);

        view = new JButton("View Employee");
        view.setBounds(820, 80, 150, 40);
        view.addActionListener(this);
        image.add(view);

        update = new JButton("Update Employee");
        update.setBounds(650, 140, 150, 40);
        update.addActionListener(this);
        image.add(update);

        remove = new JButton("Remove Employee");
        remove.setBounds(820, 140, 150, 40);
        remove.addActionListener(this);
        image.add(remove);

        export = new JButton("Export Records");
        export.setBounds(650, 200, 150, 40);
        export.addActionListener(this);
        image.add(export);

        setSize(1120, 630);
        setLocation(250, 100);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        dispose();

        if (ae.getSource() == add) new AddEmployee();
        else if (ae.getSource() == view) new ViewEmployee();
        else if (ae.getSource() == update) new UpdateEmployee();
        else if (ae.getSource() == remove) new RemoveEmployee();
        else if (ae.getSource() == export) new ExportEmployee();
    }

    public static void main(String[] args) {
        new Home();
    }
}