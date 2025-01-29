package bookStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminScreen extends JFrame {

    private JTable bookTable; // Table to display book data
    private DefaultTableModel tableModel; // Model for managing table data

    public AdminScreen() {
        // Set up the main frame properties
        setTitle("Admin - Book Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold buttons
        JPanel buttonPanel = new JPanel();

        // Create buttons for actions
        JButton addBookButton = new JButton("Add Book");
        JButton updateStockButton = new JButton("Update Stock");
        JButton removeBookButton = new JButton("Remove Book");

        // Add buttons to the panel
        buttonPanel.add(addBookButton);
        buttonPanel.add(updateStockButton);
        buttonPanel.add(removeBookButton);

        // Define column names for the table
        String[] columnNames = {"Book ID", "Title", "Author", "Price", "Stock"};
        
        // Initialize the table model with the column names
        tableModel = new DefaultTableModel(columnNames, 0);

        // Create the table and wrap it in a scroll pane
        bookTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        // Load initial book data into the table
        TableUtils.loadBooksIntoTable(tableModel);

        // Add action listeners to buttons
        addBookButton.addActionListener(e -> addBook()); // Add new book
        updateStockButton.addActionListener(e -> updateStock()); // Update stock quantity
        removeBookButton.addActionListener(e -> removeBook()); // Remove a book

        // Use BorderLayout to arrange components
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH); // Add buttons at the top
        add(tableScrollPane, BorderLayout.CENTER); // Add table in the center

        // Center the window on the screen and make it visible
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to handle adding a new book
    private void addBook() {
        // Collect book details from the user
        String bookId = JOptionPane.showInputDialog("Enter Book ID:");
        String title = JOptionPane.showInputDialog("Enter Book Title:");
        String author = JOptionPane.showInputDialog("Enter Book Author:");
        String price = JOptionPane.showInputDialog("Enter Book Price:");
        String stock = JOptionPane.showInputDialog("Enter Stock Quantity:");

        // Read the current book list
        List<Book> books = FileHandler.readBooks();

        // Add the new book to the list
        books.add(new Book(bookId, title, author, Double.parseDouble(price), Integer.parseInt(stock)));

        // Save the updated list to the file
        FileHandler.writeBooks(books);

        // Notify the user
        JOptionPane.showMessageDialog(this, "Book added successfully!");

        // Refresh the table to reflect changes
        TableUtils.loadBooksIntoTable(tableModel);

    }

    // Method to handle updating stock
    private void updateStock() {
        // Prompt the user for the book ID to update
        String bookId = JOptionPane.showInputDialog("Enter Book ID to Update Stock:");

        // Read the current book list
        List<Book> books = FileHandler.readBooks();

        boolean found = false; // Flag to check if the book is found

        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                // If the book is found, ask for the new stock quantity
                String newStock = JOptionPane.showInputDialog("Enter New Stock Quantity:");
                book.setStock(Integer.parseInt(newStock)); // Update the stock
                found = true;
                break;
            }
        }

        if (found) {
            // If the book was found and updated, save changes
            FileHandler.writeBooks(books);
            JOptionPane.showMessageDialog(this, "Stock updated successfully!");
        } else {
            // Notify if the book was not found
            JOptionPane.showMessageDialog(this, "Book not found!");
        }

        // Refresh the table to reflect changes
        TableUtils.loadBooksIntoTable(tableModel);

    }

    // Method to handle removing a book
    private void removeBook() {
        // Prompt the user for the book ID to remove
        String bookId = JOptionPane.showInputDialog("Enter Book ID to Remove:");

        // Read the current book list
        List<Book> books = FileHandler.readBooks();

        // Remove the book from the list if the ID matches
        books.removeIf(book -> book.getBookId().equals(bookId));

        // Save the updated list to the file
        FileHandler.writeBooks(books);

        // Notify the user
        JOptionPane.showMessageDialog(this, "Book removed successfully!");

        // Refresh the table to reflect changes
        TableUtils.loadBooksIntoTable(tableModel);
    }
}
