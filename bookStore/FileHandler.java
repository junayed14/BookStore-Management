package bookStore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String BOOKS_FILE = "books.txt";
    private static final String USER_FILE = "users.txt";

    // Method to read books from the file
    public static List<Book> readBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) { // Ensure data integrity
                    String bookId = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String category = parts[3];
                    boolean popular = Boolean.parseBoolean(parts[4]);
                    double price = Double.parseDouble(parts[5]);
                    int stock = Integer.parseInt(parts[6]);

                    books.add(new Book(bookId, title, author, category, popular, price, stock));
                } else {
                    // Debugging invalid entries
                    System.out.println("Skipping invalid entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in books file: " + e.getMessage());
        }
        return books;
    }

    // Method to write books to the file
    public static void writeBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing books file: " + e.getMessage());
        }
    }

    // Method to read users from the file
    public static List<Users> readUsers() {
        List<Users> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { // Ensure data integrity
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];

                    users.add(new Users(username, password, role));
                } else {
                    // Debugging invalid entries
                    System.out.println("Skipping invalid entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
        return users;
    }

    // Method to write users to the file
    public static void writeUsers(List<Users> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Users user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing users file: " + e.getMessage());
        }
    }
}
