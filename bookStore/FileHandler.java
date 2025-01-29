package bookStore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
	private static final String BOOKS_FILE = "books.txt";

    public static List<Book> readBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                books.add(new Book(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static void writeBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
