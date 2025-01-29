package bookStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// The CustomerScreen class represents the customer interface for the Book Store.
public class CustomerScreen extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // Constructor for the CustomerScreen class.
    public CustomerScreen() {
        // Set up the frame properties.
        setTitle("Customer - Book Store");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the buttons.
        JPanel buttonPanel = new JPanel();
        JButton viewBooksButton = new JButton("View Books");
        JButton searchBooksButton = new JButton("Search Books");
        JButton purchaseBookButton = new JButton("Purchase Book");

        // Add the buttons to the button panel.
        buttonPanel.add(viewBooksButton);
        buttonPanel.add(searchBooksButton);
        buttonPanel.add(purchaseBookButton);

        // Create a table to display books and set up its model.
        String[] columnNames = {"Book ID", "Title", "Author", "Price", "Stock"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);

        // Add a scroll pane for the table.
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        // Load the initial list of books into the table.
        TableUtils.loadBooksIntoTable(tableModel);

        // Add action listeners for the buttons.
        viewBooksButton.addActionListener(e -> TableUtils.loadBooksIntoTable(tableModel));
        searchBooksButton.addActionListener(e -> searchBooks());
        purchaseBookButton.addActionListener(e -> purchaseBook());

        // Set up the layout and add components to the frame.
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Center the frame on the screen and make it visible.
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to search for books by title or author.
    private void searchBooks() {
        // Prompt the user to enter a search keyword.
        String keyword = JOptionPane.showInputDialog("Enter title or author to search:");
        List<Book> books = FileHandler.readBooks();
        tableModel.setRowCount(0); // Clear the table for the search results.

        // Add books that match the search keyword to the table.
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                Object[] rowData = {
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPrice(),
                    book.getStock()
                };
                tableModel.addRow(rowData);
            }
        }

        // Show a message if no books match the search.
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No books found matching the search criteria!");
        }
    }

    // Method to purchase a book by its ID and quantity.
    private void purchaseBook() {
        // Prompt the user to enter the book ID and quantity.
        String bookId = JOptionPane.showInputDialog("Enter Book ID to Purchase:");
        String quantityInput = JOptionPane.showInputDialog("Enter Quantity:");

        try {
            int quantity = Integer.parseInt(quantityInput); // Convert quantity to an integer.
            List<Book> books = FileHandler.readBooks();
            boolean found = false;

            // Find the book with the given ID and check if there's enough stock.
            for (Book book : books) {
                if (book.getBookId().equals(bookId) && book.getStock() >= quantity) {
                    book.setStock(book.getStock() - quantity); // Update the stock.
                    found = true;
                    break;
                }
            }

            // If the book is found and stock is sufficient, update the file and show success message.
            if (found) {
                FileHandler.writeBooks(books);
                JOptionPane.showMessageDialog(this, "Purchase successful!");
                TableUtils.loadBooksIntoTable(tableModel); // Reload the updated book list.
            } else {
                JOptionPane.showMessageDialog(this, "Book not found or insufficient stock!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered. Please enter a number.");
        }
    }
}
