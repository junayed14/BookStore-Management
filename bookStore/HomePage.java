package bookStore;  // This class is part of the 'bookStore' package

import javax.swing.*;  // Imports Swing components like JFrame, JPanel, JButton, JLabel, etc.
import java.awt.*;  // Imports AWT components like BorderLayout, GridBagLayout, etc.
import java.util.List;  // Imports List to handle collections of books

// HomePage class extends the BaseScreen class, which is an abstract JFrame
public class HomePage extends BaseScreen {

    // Constructor for HomePage class
    public HomePage() {
        super(); // Calls the superclass constructor, which sets the title using getWindowTitle()
        
        // Creates a background panel with custom paint behavior
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  // Calls the parent class's paintComponent method
                drawBackground(g, "background.jpeg");  // Draws the background with a specific image
            }
        };
        backgroundPanel.setLayout(new BorderLayout());  // Sets the layout manager to BorderLayout

        // Title label at the top of the screen
        JLabel titleLabel = new JLabel("Welcome to Book Store", JLabel.CENTER);  // Centered title text
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));  // Sets font for title
        titleLabel.setForeground(Color.WHITE);  // Sets the title text color to white
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);  // Adds the title to the top of the panel

        // Creates the books panel that displays popular books
        JPanel booksPanel = createBooksPanel();
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Wrapper panel to center the books panel
        wrapperPanel.setOpaque(false);  // Makes the wrapper panel transparent
        wrapperPanel.add(booksPanel);  // Adds books panel to the wrapper panel
        backgroundPanel.add(wrapperPanel, BorderLayout.CENTER);  // Adds the books panel to the center

        // Creates the button panel containing Login and Sign Up buttons
        JPanel buttonPanel = createButtonPanel();
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Center panel for the buttons
        centerPanel.setOpaque(false);  // Makes the center panel transparent
        centerPanel.add(buttonPanel);  // Adds the button panel to the center panel
        backgroundPanel.add(centerPanel, BorderLayout.SOUTH);  // Adds button panel to the bottom of the frame

        // Adds the background panel to the main frame
        add(backgroundPanel);
        setVisible(true);  // Makes the window visible
    }

    // Overrides the abstract method to provide a specific title for the HomePage
    @Override
    protected String getWindowTitle() {
        return "Book Store Management System";  // Title specific to HomePage
    }

    // Method to create the books panel which displays popular books
    private JPanel createBooksPanel() {
        JPanel booksPanel = new JPanel(new GridBagLayout());  // Uses GridBagLayout for flexible grid-based placement
        booksPanel.setOpaque(true);  // Makes the panel opaque (not transparent)
        booksPanel.setBackground(new Color(0, 0, 0, 100));  // Sets the background color with semi-transparency
        booksPanel.setPreferredSize(new Dimension(600, 300));  // Sets the preferred size for the panel
        booksPanel.setMinimumSize(new Dimension(600, 300));  // Sets the minimum size for the panel

        GridBagConstraints gbc = new GridBagConstraints();  // GridBagConstraints to manage component positioning
        gbc.gridx = 0;  // First column
        gbc.gridy = 0;  // First row

        JLabel popularBooksLabel = new JLabel("Popular Books:");  // Label for the popular books section
        popularBooksLabel.setFont(new Font("Arial", Font.BOLD, 18));  // Sets the font for the label
        popularBooksLabel.setForeground(Color.WHITE);  // Sets the text color to white
        booksPanel.add(popularBooksLabel, gbc);  // Adds the label to the books panel
        gbc.gridy++;  // Moves to the next row

        // Retrieves the list of books from a file (FileHandler is assumed to read book data)
        List<Book> books = FileHandler.readBooks();
        for (Book book : books) {
            if (book.isPopular()) {  // Only add popular books to the display
                JLabel bookLabel = new JLabel(" - " + book.getTitle() + " by " + book.getAuthor());  // Book label
                bookLabel.setForeground(Color.WHITE);  // Sets the text color to white
                booksPanel.add(bookLabel, gbc);  // Adds the book label to the panel
                gbc.gridy++;  // Moves to the next row for the next book
            }
        }
        return booksPanel;  // Returns the fully created books panel
    }

    // Method to create the button panel containing Login and Sign Up buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();  // Creates a new panel for the buttons
        buttonPanel.setOpaque(false);  // Makes the panel transparent

        // Login button and its action listener
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));  // Sets the font for the button
        loginButton.addActionListener(e -> {  // Action listener for the Login button
            new LoginPage();  // Opens the LoginPage when the button is clicked
            dispose();  // Closes the current HomePage window
        });

        // Sign Up button and its action listener
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));  // Sets the font for the button
        signUpButton.addActionListener(e -> {  // Action listener for the Sign Up button
            new RegistrationPage();  // Opens the RegistrationPage when clicked
            dispose();  // Closes the current HomePage window
        });

        buttonPanel.add(loginButton);  // Adds the login button to the panel
        buttonPanel.add(signUpButton);  // Adds the sign up button to the panel
        return buttonPanel;  // Returns the button panel
    }

    // Main method to run the HomePage
    public static void main(String[] args) {
        new HomePage();  // Creates an instance of HomePage to display the window
    }
}
