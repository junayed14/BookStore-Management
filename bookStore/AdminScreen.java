package bookStore;  // Specifies the package name for the class

import javax.swing.*;  // Imports Swing components for building graphical user interfaces
import javax.swing.table.DefaultTableModel;  // Imports the DefaultTableModel class to manage the data for the JTable
import java.awt.*;  // Imports classes for graphical user interface layout and events
import java.util.List;  // Imports the List interface to handle a collection of books

public class AdminScreen extends BaseScreen {  // Defines the AdminScreen class that extends BaseScreen class

    private JTable bookTable; // Declares a JTable to display book data
    private DefaultTableModel tableModel; // Declares a DefaultTableModel to manage the table data

    public AdminScreen() {  // Constructor for AdminScreen class
        super();  // Calls the constructor of the superclass, BaseScreen, to initialize inherited properties
        
        // Creates a background panel with custom paint behavior
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {  // Overrides paintComponent to customize how the background is drawn
                super.paintComponent(g);  // Calls the parent class's paintComponent method
                drawBackground(g, "background.jpeg");  // Draws the background with a specified image file
            }
        };
        backgroundPanel.setLayout(new BorderLayout());  // Sets the layout of the background panel to BorderLayout

        // Create the button panel
        JPanel buttonPanel = ButtonPanel();  // Calls the ButtonPanel method to create a panel with buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Panel with centered buttons
        centerPanel.setOpaque(false);  // Makes the center panel transparent
        centerPanel.add(buttonPanel);  // Adds the button panel to the center panel
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);  // Adds the center panel to the background panel at the center

        // Create the table
        createBookTable();  // Calls the createBookTable method to create the book table with headers
        JScrollPane tableScrollPane = new JScrollPane(bookTable);  // Wraps the book table in a JScrollPane for scroll functionality
        
        // Set the maximum height of the table
        bookTable.setPreferredScrollableViewportSize(new Dimension(bookTable.getPreferredSize().width, 400));  // Sets a fixed height for the table

        backgroundPanel.add(tableScrollPane, BorderLayout.SOUTH);  // Adds the table scroll pane to the bottom of the background panel

        // Adds the background panel to the main frame
        add(backgroundPanel);  // Adds the background panel to the frame of the Admin screen

        // Load books into the table from a file
        TableUtils.loadBooksIntoTable(tableModel);  // Uses a utility function to load data into the table

        setVisible(true);  // Makes the window visible after setting up the UI
    }

    // Overrides the abstract method to provide a specific title for the Admin screen
    @Override
    protected String getWindowTitle() {
        return "Admin - Book Management";  // Returns a string representing the window title for the Admin screen
    }

    // Method to create the button panel with three buttons: Add, Update, and Remove
    private JPanel ButtonPanel() {
        JPanel buttonPanel = new JPanel();  // Creates a new panel to hold buttons
        buttonPanel.setOpaque(false);  // Makes the panel transparent

        // Create buttons for Add, Update, and Remove actions

        JButton addBookButton = new JButton("Add Book");  // Create an Add Book button
        JButton updateStockButton = new JButton("Update Stock");
        JButton updateStatusButton = new JButton("Update Status");  // Create an Update Stock button
        JButton removeBookButton = new JButton("Remove Book");  // Create a Remove Book button
        JButton allUserButton = new JButton("All User");
        JButton LogOutButton = new JButton("LogOut");
        

        // Add buttons to the panel
        buttonPanel.add(addBookButton);  // Adds the Add Book button to the panel
        buttonPanel.add(updateStockButton);  // Adds the Update Stock button to the panel
        buttonPanel.add(updateStatusButton);  // Adds the Update Status button to the panel
        buttonPanel.add(removeBookButton);  // Adds the Remove Book button to the panel
        buttonPanel.add(allUserButton);  // Show all User list
        buttonPanel.add(LogOutButton);  // Show all User list

        // Adds action listeners to buttons
        addBookButton.addActionListener(e -> addBook());  // Calls addBook() method when the Add Book button is clicked
        updateStockButton.addActionListener(e -> updateStock());  // Calls updateStock() method when the Update Stock button is clicked
        updateStatusButton.addActionListener(e -> updateStatus());  // Calls updateStock() method when the Update Stock button is clicked
        removeBookButton.addActionListener(e -> removeBook());  // Calls removeBook() method when the Remove Book button is clicked
        allUserButton.addActionListener(e -> new AllUserScreen());  // Calls AllUserScreen()
        LogOutButton.addActionListener(e -> {
            new LoginPage();  // Opens the LoginPage when the back button is clicked
            dispose();  // Closes the current AdminScreen
        });

        return buttonPanel;  // Returns the button panel to the calling method
    }

    private void updateStatus() {
        // Ask the user for the book ID whose stock needs to be updated
        String bookId = JOptionPane.showInputDialog("Enter Book ID to Update Status:");

        // Read the current list of books
        List<Book> books = FileHandler.readBooks();

        boolean found = false;  // Flag to check if the book is found

        // Loop through the list of books to find the one with the matching ID
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {  // If book ID matches
                String newStatus = JOptionPane.showInputDialog("Enter New Status (true/false):");  // Ask for the new stock quantity
                book.setPopular(Boolean.parseBoolean(newStatus));  // Update the stock of the book
                found = true;  // Set found flag to true
                break;  // Exit the loop since the book is found
            }
        }

        // If the book is found and updated, save changes to the file
        if (found) {
            FileHandler.writeBooks(books);
            JOptionPane.showMessageDialog(this, "Status updated successfully!");  // Notify the user
        } else {
            JOptionPane.showMessageDialog(this, "Book not found!");  // Notify the user if the book is not found
        }

        // Refresh the table to reflect the updated stock
        TableUtils.loadBooksIntoTable(tableModel);
    }

    // Method to handle adding a book
    private void addBook() {
        // Collect book details from the user through input dialogs
        String bookId = JOptionPane.showInputDialog("Enter Book ID:");  // Ask for the book ID
        String title = JOptionPane.showInputDialog("Enter Book Title:");  // Ask for the book title
        String author = JOptionPane.showInputDialog("Enter Book Author:");  // Ask for the author of the book
        String category = JOptionPane.showInputDialog("Enter Book Category:");  // Ask for the book category
        String popularInput = JOptionPane.showInputDialog("Is this book popular? (true/false):");  // Ask if the book is popular
        boolean popular = Boolean.parseBoolean(popularInput);  // Parse the string input to boolean
        String price = JOptionPane.showInputDialog("Enter Book Price:");  // Ask for the book price
        String stock = JOptionPane.showInputDialog("Enter Stock Quantity:");  // Ask for the stock quantity

        // Create a new Book object with the user input data
        Book newBook = new Book(bookId, title, author, category, popular, Double.parseDouble(price), Integer.parseInt(stock));

        // Read the current list of books from the file
        List<Book> books = FileHandler.readBooks();

        // Add the new book to the list of books
        books.add(newBook);

        // Save the updated list of books to the file
        FileHandler.writeBooks(books);

        // Notify the user that the book was added successfully
        JOptionPane.showMessageDialog(this, "Book added successfully!");

        // Refresh the table to reflect the new book addition
        TableUtils.loadBooksIntoTable(tableModel);
    }

    // Method to handle updating the stock of a book
    private void updateStock() {
        // Ask the user for the book ID whose stock needs to be updated
        String bookId = JOptionPane.showInputDialog("Enter Book ID to Update Stock:");

        // Read the current list of books
        List<Book> books = FileHandler.readBooks();

        boolean found = false;  // Flag to check if the book is found

        // Loop through the list of books to find the one with the matching ID
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {  // If book ID matches
                String newStock = JOptionPane.showInputDialog("Enter New Stock Quantity:");  // Ask for the new stock quantity
                book.setStock(Integer.parseInt(newStock));  // Update the stock of the book
                found = true;  // Set found flag to true
                break;  // Exit the loop since the book is found
            }
        }

        // If the book is found and updated, save changes to the file
        if (found) {
            FileHandler.writeBooks(books);
            JOptionPane.showMessageDialog(this, "Stock updated successfully!");  // Notify the user
        } else {
            JOptionPane.showMessageDialog(this, "Book not found!");  // Notify the user if the book is not found
        }

        // Refresh the table to reflect the updated stock
        TableUtils.loadBooksIntoTable(tableModel);
    }

    // Method to handle removing a book
    private void removeBook() {
        // Ask the user for the book ID to remove
        String bookId = JOptionPane.showInputDialog("Enter Book ID to Remove:");

        // Read the current list of books
        List<Book> books = FileHandler.readBooks();

        // Remove the book from the list if its ID matches
        books.removeIf(book -> book.getBookId().equals(bookId));

        // Save the updated list of books to the file
        FileHandler.writeBooks(books);

        // Notify the user that the book was removed successfully
        JOptionPane.showMessageDialog(this, "Book removed successfully!");

        // Refresh the table to reflect the changes
        TableUtils.loadBooksIntoTable(tableModel);
    }

    // Method to create the table to display books
    private void createBookTable() {
        // Define the column names for the table
        String[] columnNames = {"Book ID", "Title", "Author", "Category", "Popular", "Price", "Stock"};

        // Create the table model with no data initially
        tableModel = new DefaultTableModel(columnNames, 0);

        // Create a JTable with the model to display book data
        bookTable = new JTable(tableModel);
    }
}
