package bookStore;

public class Book {
	private String bookId;
	private String title;
    private String author;
    private double price;
    private int stock;
    
    public Book(String bookId, String title, String author, double price, int stock) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }
    
    // Getters and Setters
    public String getBookId() { 
    	return bookId; 
    }
    
    public void setBookId(String bookId) { 
    	this.bookId = bookId; 
    }
    
    public String getTitle() { 
    	return title; 
   	}
    
    public void setTitle(String title) { 
    	this.title = title; 
   	}
    
    public String getAuthor() { 
    	return author; 
   	}
    
    public void setAuthor(String author) { 
    	this.author = author; 
   	}
    
    public double getPrice() { 
    	return price; 
   	}
    
    public void setPrice(double price) { 
    	this.price = price; 
   	}
    
    public int getStock() { 
    	return stock; 
   	}
    
    public void setStock(int stock) { 
    	this.stock = stock; 
    }
    
    public String toString() {
        return bookId + "," + title + "," + author + "," + price + "," + stock;
    }
}
