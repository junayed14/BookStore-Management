package bookStore;  // The class is part of the 'bookStore' package

import javax.swing.*;  // Imports Swing library components like JFrame and ImageIcon
import java.awt.*;  // Imports AWT library components for handling graphics and colors

// Abstract class 'BaseScreen' that extends JFrame
public abstract class BaseScreen extends JFrame {

    // Constructor for BaseScreen
    public BaseScreen() {
        setSize(800, 600);  // Sets the size of the window to 800x600 pixels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Closes the application when the window is closed
        setLocationRelativeTo(null);  // Centers the window on the screen
        setTitle(getWindowTitle());  // Sets the window title using the polymorphic method (abstract method)
        setIconImage(new ImageIcon("favicon.jpeg").getImage());
    }

    // Abstract method for subclasses to define their specific window title
    protected abstract String getWindowTitle();

    // Method to draw a background image with an overlay color
    protected void drawBackground(Graphics g, String imagePath) {
        // Creates an ImageIcon from the specified image path
        ImageIcon backgroundImage = new ImageIcon(imagePath);
        // Draws the image scaled to the size of the window
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);

        // Casts Graphics object to Graphics2D for advanced drawing features
        Graphics2D g2d = (Graphics2D) g;
        // Sets the overlay color (semi-transparent black with an alpha value of 100)
        Color overlayColor = new Color(0, 0, 0, 100);
        g2d.setColor(overlayColor);
        // Fills the entire window with the overlay color
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
