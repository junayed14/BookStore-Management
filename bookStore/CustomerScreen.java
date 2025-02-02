package bookStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CustomerScreen extends BaseScreen {

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private Cart cart;

    public CustomerScreen() {
        super();

        // Initialize cart for this customer
        cart = new Cart();

        // Create background panel with custom paint behavior
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g, "background.jpeg");
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Create the button panel for customer actions
        JPanel buttonPanel = ButtonPanel();
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Create the book table
        createBookTable();
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        // Set the table height
        bookTable.setPreferredScrollableViewportSize(new Dimension(bookTable.getPreferredSize().width, 400));

        backgroundPanel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add the background panel to the frame
        add(backgroundPanel);

        // Load books into the table from file
        TableUtils.CloadBooksIntoTable(tableModel);

        setVisible(true);
    }

    @Override
    protected String getWindowTitle() {
        return "Customer - Bookstore";
    }

    private JPanel ButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton viewCartButton = new JButton("View Cart");
        JButton addToCartButton = new JButton("Add to Cart");
        JButton LogOutButton = new JButton("LogOut");

        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(LogOutButton);

        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());
        LogOutButton.addActionListener(e -> {
            new LoginPage(); 
            dispose(); 
        });

        return buttonPanel;
    }

    private void addToCart() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            String bookId = (String) bookTable.getValueAt(selectedRow, 0);
            List<Book> books = FileHandler.readBooks();

            for (Book book : books) {
                if (book.getBookId().equals(bookId)) {
                    // Ask for the quantity of the book to add to the cart
                    String quantityStr = JOptionPane.showInputDialog(this, 
                            "Enter the quantity of '" + book.getTitle() + "' you want to add:", 
                            "Quantity", JOptionPane.QUESTION_MESSAGE);

                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(quantityStr);

                            if (quantity <= 0) {
                                JOptionPane.showMessageDialog(this, "Please enter a positive number.");
                            } else if (quantity <= book.getStock()) {
                                // Add the specified quantity of the book to the cart
                                for (int i = 0; i < quantity; i++) {
                                    cart.addBook(book);
                                }
                                JOptionPane.showMessageDialog(this, "Book(s) added to cart!");
                            } else {
                                JOptionPane.showMessageDialog(this, 
                                        "Not enough stock. Only " + book.getStock() + " available.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.");
                        }
                    }
                    return; // Book found and processed, exit the method
                }
            }
            JOptionPane.showMessageDialog(this, "Book not found!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to add to cart.");
        }
    }


    private void viewCart() {
        StringBuilder cartDetails = new StringBuilder();
        double totalAmount = 0.0;

        // Use a map to track the quantity of each book in the cart
        Map<String, Integer> bookQuantityMap = new HashMap<>();

        // Loop through the books in the cart and track quantities
        for (Book book : cart.getBooks()) {
            String bookId = book.getBookId();
            bookQuantityMap.put(bookId, bookQuantityMap.getOrDefault(bookId, 0) + 1);
        }

        // Loop through the map to display cart details
        for (Map.Entry<String, Integer> entry : bookQuantityMap.entrySet()) {
            String bookId = entry.getKey();
            int quantity = entry.getValue();

            // Find the book details by bookId
            Book book = null;
            List<Book> books = FileHandler.readBooks();
            for (Book b : books) {
                if (b.getBookId().equals(bookId)) {
                    book = b;
                    break;
                }
            }

            // Append the book details and quantity to the cart details
            if (book != null) {
                cartDetails.append(book.getTitle())
                        .append(" - Quantity: ").append(quantity)
                        .append(" - Price per Unit: ").append(book.getPrice())
                        .append("<br>");  // Use <br> for line breaks in HTML
                totalAmount += book.getPrice() * quantity;  // Add the total price for the quantity
            }
        }

        // Create panel for buttons (Clear Cart and Confirm Purchase)
        JPanel buttonPanel = new JPanel();
        JButton clearCartButton = new JButton("Clear Cart");
        JButton confirmPurchaseButton = new JButton("Confirm Purchase");

        buttonPanel.add(clearCartButton);
        buttonPanel.add(confirmPurchaseButton);

        // Add action listener to clear the cart
        clearCartButton.addActionListener(e -> {
            cart.getBooks().clear();  // Clears all books from the cart
            JOptionPane.showMessageDialog(this, "Cart has been cleared.");
            ((JDialog) SwingUtilities.getWindowAncestor(clearCartButton)).dispose();
        });

        // Add action listener to confirm the purchase
        confirmPurchaseButton.addActionListener(e -> {
            List<Book> books = FileHandler.readBooks();

            for (Map.Entry<String, Integer> entry : bookQuantityMap.entrySet()) {
                String bookId = entry.getKey();
                int quantity = entry.getValue();

                // Find the book in the list and update stock
                for (Book book : books) {
                    if (book.getBookId().equals(bookId)) {
                        int newStock = book.getStock() - quantity;
                        if (newStock >= 0) {
                            book.setStock(newStock);  // Update stock
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                    "Not enough stock for " + book.getTitle() + ".");
                            return;  // Stop processing the purchase if stock is insufficient
                        }
                        break;
                    }
                }
            }

            // Update the books data (write back the updated stock)
            FileHandler.writeBooks(books);

            // Refresh the table with updated stock
            TableUtils.CloadBooksIntoTable(tableModel);  // Reload data into the table

            // Clear the cart after purchase
            cart.getBooks().clear();
            JOptionPane.showMessageDialog(this, "Purchase confirmed. Thank you for shopping!");
            ((JDialog) SwingUtilities.getWindowAncestor(confirmPurchaseButton)).dispose(); // Close the cart dialog

        });

        // Show cart details and buttons in a dialog
        String cartMessage = cartDetails.length() > 0 ? cartDetails.toString() : "Your cart is empty.";
        cartMessage += "<br><b>Total Amount: " + totalAmount + "</b>";  // Display total amount at the bottom

        // Create the cart details label with the cart message
        JLabel cartLabel = new JLabel("<html>" + cartMessage + "</html>");
        cartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the cart details label

        // Create a main panel and use a BoxLayout to stack the content vertically
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.add(cartLabel);  // Add the cart details label
        cartPanel.add(Box.createVerticalStrut(10)); // Add space between content and buttons
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the button panel
        cartPanel.add(buttonPanel); // Add the buttons below the content
        
        // Show the cart details in a dialog box
        JOptionPane.showMessageDialog(this, cartPanel, "Cart", JOptionPane.PLAIN_MESSAGE);
    }




    // Create the book table without the "Popular" column
    private void createBookTable() {
        // Define column names excluding "Popular"
        String[] columnNames = {"Book ID", "Title", "Author", "Category", "Price", "Stock"};

        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);

        // Disable editing for the table
        bookTable.setDefaultEditor(Object.class, null);
    }
}
