import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame extends JPanel implements KeyListener {
    // Constants for game window dimensions and unit size
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;

    // Variables for snake and apple positions, snake length, direction, score, and game state
    private final int[] x = new int[(WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE)];
    private final int[] y = new int[(WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE)];
    private int snakeLength = 6;
    private int appleX;
    private int appleY;
    // R - Right, L - Left, U - Up, D - Down
    private char direction = 'R'; 
    private boolean isRunning = false;
    private Timer timer;
    private final int DELAY = 75;
    private int score;

    // Constructor
    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    // Method to start the game
    public void startGame() {
        placeApple(); // Place the apple on the game board
        isRunning = true; // Set game state to running
        timer = new Timer(DELAY, e -> {
            if (isRunning) {
                move(); // Move the snake
                checkApple(); // Check if the snake ate the apple
                checkCollision(); // Check if the snake collided with anything
                repaint(); // Redraw the game board
            }
        });
        timer.start(); // Start the game timer
    }

    // Method to draw the game board
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Method to draw game elements
    public void draw(Graphics g) {
        // Draw the snake
        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // Draw snake head
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // Draw snake body
            }
        }
        // Draw the apple
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        // If game over, display game over message and score
        if (!isRunning) {
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
            g.drawString("Score: " + score, (WIDTH - metrics.stringWidth("Score: " + score)) / 2, (HEIGHT / 2) + 50);
            g.drawString("Press Space to Restart", (WIDTH - metrics.stringWidth("Press Space to Restart")) / 2, (HEIGHT / 2) + 100);
        }
    }

    // Method to move the snake
    public void move() {
        // Move the body parts of the snake
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // Move the head of the snake based on current direction
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    // Method to check if the snake ate the apple
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            snakeLength++; // Increase snake length
            score=snakeLength-6; // Increase score
            placeApple(); // Place a new apple on the game board
        }
    }

    // Method to check if the snake collided with anything
    public void checkCollision() {
        // Check if the snake collided with its own body
        for (int i = snakeLength; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                isRunning = false; // Set game state to over
            }
        }
        // Check if the snake collided with the walls
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            isRunning = false; // Set game state to over
        }
        // Stop the game timer if the game is over
        if (!isRunning) {
            timer.stop();
        }
    }

    // Method to place the apple on the game board
    public void placeApple() {
        appleX = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    // Method to handle key presses
    @Override
    public void keyPressed(KeyEvent e) {
        // If game over and space is pressed, restart the game
        if (!isRunning && e.getKeyCode() == KeyEvent.VK_SPACE) {
            restartGame();
        }
        // Change direction of the snake based on arrow key pressed
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
        }
    }

    // Unused key event methods
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // Method to restart the game
    public void restartGame() {
        isRunning = true; // Set game state to running
        snakeLength = 6; // Reset snake length
        score = 0; // Reset score
        direction = 'R'; // Reset direction
        // Reset snake position
        for (int i = 0; i < snakeLength; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        startGame(); // Start the game
    }

    // Main method to run the game
    public static void main(String[] args) {
        // Create a JFrame to hold the game panel
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
