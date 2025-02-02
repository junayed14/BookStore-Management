package bookStore;  // The class is part of the 'bookStore' package

import javax.swing.*;  // Imports Swing components like JFrame, JPanel, JButton, JTextField, etc.
import java.awt.*;  // Imports AWT components like GridLayout, GridBagLayout, Insets, etc.
import java.awt.event.ActionEvent;  // For handling action events
import java.io.BufferedWriter;  // For writing data to a file
import java.io.FileWriter;  // For writing data to a file
import java.io.IOException;  // For handling IOExceptions

// RegistrationPage class extends BaseScreen to create the registration page for the application
public class RegistrationPage extends BaseScreen {
    
    // Constructor for RegistrationPage class
    public RegistrationPage() {
        super();  // Calls the parent constructor, which sets the title using getWindowTitle()

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

        // Back Button (top left corner)
        JButton backButton = createBackButton();  // Calls the method to create the back button
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // A panel to hold the back button
        backButtonPanel.setOpaque(false);  // Makes the panel transparent
        backButtonPanel.add(backButton);  // Adds the back button to the panel
        leftPanel.add(backButtonPanel, BorderLayout.NORTH);  // Adds the back button panel to the top of the left panel

        // Right Section - Registration Form
        JPanel rightPanel = new JPanel(new GridBagLayout());  // Creates a GridBagLayout for the registration form
        GridBagConstraints gbc = new GridBagConstraints();  // Creates constraints for positioning components
        gbc.gridx = 0;  // Sets the column to 0 (first column)
        gbc.gridy = 0;  // Sets the row to 0 (first row)
        gbc.insets = new Insets(10, 10, 10, 10);  // Adds padding around the components

        JLabel registerLabel = new JLabel("Register");  // Register label
        registerLabel.setFont(new Font("Serif", Font.BOLD, 24));  // Sets the font of the label
        rightPanel.add(registerLabel, gbc);  // Adds the register label to the right panel

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
        JButton registerButton = new JButton("Register");  // Register button
        registerButton.addActionListener(e -> handleRegistration(usernameField.getText(), new String(passwordField.getPassword())));  // Register button listener
        rightPanel.add(registerButton, gbc);  // Adds the register button

        // Adds both left and right sections to the main content panel
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        // Adds the content panel to the frame and makes it visible
        add(contentPanel);
        setVisible(true);
    }

    // Overrides the abstract method to provide the title for RegistrationPage
    @Override
    protected String getWindowTitle() {
        return "Register - Book Store Management System";  // Title for the RegistrationPage
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
            dispose();  // Closes the current RegistrationPage
        });
        return backButton;  // Returns the created back button
    }

    // Method to handle the registration process
    private void handleRegistration(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {  // Checks if any fields are empty
            JOptionPane.showMessageDialog(null, "Please fill in all fields");  // Shows an error message if fields are empty
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", true))) {  // Opens users.txt for appending new data
            bw.write(username + "," + password + ",customer");  // Writes the username, password, and role (customer) to the file
            bw.newLine();  // Moves to the next line in the file
            JOptionPane.showMessageDialog(null, "Registration successful!");  // Shows a success message
        } catch (IOException ex) {  // Catches IOExceptions if writing to the file fails
            JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage());  // Shows an error message
        }
    }
}
