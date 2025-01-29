package bookStore;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TableUtils {
	// Method to populate a DefaultTableModel with book data.
    public static void loadBooksIntoTable(DefaultTableModel tableModel) {
        // Read the books from the file.
        List<Book> books = FileHandler.readBooks();
        tableModel.setRowCount(0); // Clear the existing rows in the table.

        // Add each book as a row in the table.
        for (Book book : books) {
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
}
