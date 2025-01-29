package bookStore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// The LoginScreen class represents the login page for the Book Store Management System.
public class LoginScreen extends JFrame {
    // List to store user data (username, password, role) from the file.
    private List<Users> users = new ArrayList<>();

    // Constructor for the LoginScreen class.
    public LoginScreen() {
        // Load user data from the "users.txt" file.
        loadUserData();

        // Set up the frame properties.
        setTitle("Book Store Management System - Login");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold all components.
        JPanel panel = new JPanel();

        // Create and add a label for the username field.
        JLabel userLabel = new JLabel("Username:");
        panel.add(userLabel);

        // Create and add a text field for entering the username.
        JTextField userText = new JTextField(10);
        panel.add(userText);

        // Create and add a label for the password field.
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        // Create and add a password field for entering the password.
        JPasswordField passwordText = new JPasswordField(10);
        panel.add(passwordText);

        // Create and add a button for login.
        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        // Add an ActionListener to handle the login button click.
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the username and password entered by the user.
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                // Check if the username and password match any user in the list.
                boolean found = false;
                for (Users user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        found = true;
                        // Determine the role and open the corresponding screen.
                        if ("admin".equals(user.getRole())) {
                            System.out.println("Opening Admin Screen...");
                            new AdminScreen(); // Open the Admin screen.
                        } else if ("customer".equals(user.getRole())) {
                            System.out.println("Opening Customer Screen...");
                            new CustomerScreen(); // Open the Customer screen.
                        }

                        // Close the login screen after successful login.
                        dispose();
                        break;
                    }
                }

                // If no match is found, show an error message.
                if (!found) {
                    JOptionPane.showMessageDialog(null, "Invalid login credentials");
                }
            }
        });

        // Add the panel to the frame.
        add(panel);

        // Center the frame on the screen and make it visible.
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to load user data from a file.
    private void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;

            // Read each line from the file.
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Ensure the line has the required data (username, password, role).
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    users.add(new Users(username, password, role)); // Add the user to the list.
                }
            }
        } catch (IOException e) {
            // Display an error message if the file cannot be read.
            JOptionPane.showMessageDialog(null, "Error reading user data: " + e.getMessage());
        }
    }

    // Main method to start the application.
    public static void main(String[] args) {
        // Create and show the login screen.
        new LoginScreen();
    }
}
