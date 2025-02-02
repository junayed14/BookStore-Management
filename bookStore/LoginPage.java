package bookStore;  // The class is part of the 'bookStore' package

import javax.swing.*;  // Imports Swing components like JFrame, JPanel, JButton, JTextField, etc.
import java.awt.*;  // Imports AWT components like GridLayout, GridBagLayout, Insets, etc.
import java.awt.event.ActionEvent;  // For handling action events
import java.awt.event.ActionListener;  // For handling action events
import java.io.BufferedReader;  // For reading data from a file
import java.io.FileReader;  // For reading a file
import java.io.IOException;  // For handling IOExceptions
import java.util.ArrayList;  // For storing a list of Users
import java.util.List;  // For storing a list of Users

// LoginPage class extends BaseScreen to create the login page for the application
public class LoginPage extends BaseScreen {
    private List<Users> users = new ArrayList<>();  // List to store users loaded from a file

    // Constructor for LoginPage class
    public LoginPage() {
        super();  // Calls the parent constructor, which sets the title using getWindowTitle()

        loadUserData();  // Loads user data from a file

        // Main content panel (divided into two sections: left and right)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));  // A GridLayout with 1 row and 2 columns

        // Left Section - Displays background image
        JPanel leftPanel = new JPanel(new BorderLayout()) {  // Creates a custom panel for background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  // Calls the parent class’s paintComponent method
                ImageIcon welcomeIcon = new ImageIcon("welcome.jpeg");  // Loads the welcome image
                Image welcomeImage = welcomeIcon.getImage();  // Gets the image from the icon
                g.drawImage(welcomeImage, 0, 0, getWidth(), getHeight(), this);  // Draws the image to fill the panel
            }
        };

        // Back Button (in the top left corner)
        JButton backButton = createBackButton();  // Calls the method to create the back button
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // A panel to hold the back button
        backButtonPanel.setOpaque(false);  // Makes the panel transparent
        backButtonPanel.add(backButton);  // Adds the back button to the panel
        leftPanel.add(backButtonPanel, BorderLayout.NORTH);  // Adds the back button panel to the top of the left panel

        // Right Section - Login Form
        JPanel rightPanel = new JPanel(new GridBagLayout());  // Creates a GridBagLayout for the login form
        GridBagConstraints gbc = new GridBagConstraints();  // Creates constraints for positioning components
        gbc.gridx = 0;  // Sets the column to 0 (first column)
        gbc.gridy = 0;  // Sets the row to 0 (first row)
        gbc.insets = new Insets(10, 10, 10, 10);  // Adds padding around the components

        JLabel loginLabel = new JLabel("Login");  // Login label
        loginLabel.setFont(new Font("Serif", Font.BOLD, 24));  // Sets the font of the label
        rightPanel.add(loginLabel, gbc);  // Adds the login label to the right panel

        gbc.gridy++;  // Moves to the next row
        JLabel usernameLabel = new JLabel("Username:");  // Username label
        rightPanel.add(usernameLabel, gbc);  // Adds the username label

        gbc.gridy++;  // Moves to the next row
        JTextField usernameField = new JTextField(15);  // Text field to input the username
        rightPanel.add(usernameField, gbc);  // Adds the username text field

        gbc.gridy++;  // Moves to the next row
        JLabel passwordLabel = new JLabel("Password:");  // Password label
        rightPanel.add(passwordLabel, gbc);  // Adds the password label

        gbc.gridy++;  // Moves to the next row
        JPasswordField passwordField = new JPasswordField(15);  // Password field to input the password
        rightPanel.add(passwordField, gbc);  // Adds the password field

        gbc.gridy++;  // Moves to the next row
        JButton loginButton = new JButton("Login");  // Login button
        loginButton.addActionListener(e -> handleLogin(usernameField.getText(), new String(passwordField.getPassword())));  // Login button listener
        rightPanel.add(loginButton, gbc);  // Adds the login button

        gbc.gridy++;  // Moves to the next row
        JButton signUpButton = new JButton("Sign Up");  // Sign Up button
        signUpButton.addActionListener(e -> {
            dispose();  // Closes the current LoginPage
            new RegistrationPage();  // Opens the RegistrationPage
        });
        rightPanel.add(signUpButton, gbc);  // Adds the sign-up button

        // Adds both left and right sections to the main content panel
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        // Adds the content panel to the frame and makes it visible
        add(contentPanel);
        setVisible(true);
    }

    // Overrides the abstract method to provide the title for LoginPage
    @Override
    protected String getWindowTitle() {
        return "Login - Book Store Management System";  // Title for the LoginPage
    }

    // Method to create the back button
    private JButton createBackButton() {
        ImageIcon backIcon = new ImageIcon("back_icon.png");  // Loads the back button icon
        Image scaledBackIcon = backIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);  // Scales the icon
        JButton backButton = new JButton(new ImageIcon(scaledBackIcon));  // Creates a new button with the scaled icon
        backButton.setBorderPainted(false);  // Removes the button border
        backButton.setContentAreaFilled(false);  // Makes the button background transparent
        backButton.setFocusPainted(false);  // Removes focus painting
        backButton.setPreferredSize(new Dimension(40, 40));  // Sets the preferred size of the button
        backButton.addActionListener(e -> {
            new HomePage();  // Opens the HomePage when the back button is clicked
            dispose();  // Closes the current LoginPage
        });
        return backButton;  // Returns the created back button
    }

    // Method to handle the login process
    private void handleLogin(String username, String password) {
        boolean found = false;  // Flag to check if user credentials match
        for (Users user : users) {  // Loops through the list of users
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {  // If user credentials match
                found = true;  // Sets found flag to true
                dispose();  // Closes the current LoginPage

                // Checks user role and navigates to the corresponding screen
                if ("admin".equals(user.getRole())) {
                    new AdminScreen();  // Opens AdminScreen for admin users
                } else {
                    new CustomerScreen();  // Opens CustomerScreen for regular users
                }
                break;
            }
        }
        if (!found) {  // If no matching user was found
            JOptionPane.showMessageDialog(null, "Invalid login credentials");  // Shows an error message
        }
    }

    // Method to load user data from a file
    private void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {  // Reads the user data file
            String line;
            while ((line = br.readLine()) != null) {  // Loops through each line of the file
                String[] parts = line.split(",");  // Splits the line by commas to extract user info
                if (parts.length == 3) {  // If the line contains the correct number of fields
                    users.add(new Users(parts[0].trim(), parts[1].trim(), parts[2].trim()));  // Adds user to the list
                }
            }
        } catch (IOException e) {  // Catches IO exceptions if reading the file fails
            JOptionPane.showMessageDialog(null, "Error reading user data: " + e.getMessage());  // Shows an error message
        }
    }
}
