
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions of window
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 500;

	// Objects used in the game
	private Thread gameThread;
	private Image image;
	private Graphics graphics;
	private Paddle paddleOne, paddleTwo;
	private Ball ball;
	private Sound sound;

	// Stores if the game has started and ended, respectively
	private boolean started = false, ended = false;

	// Stores the time when the game starts and ends, respectively
	private long gameStartTime = Long.MAX_VALUE, gameEndTime;

	// Constructor creates objects and sets properties used in the game
	public GamePanel() {
		

		// Additional properties for the game panel
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		// Make this class run at the same time as other classes
		gameThread = new Thread(this);
		gameThread.start();
	}

	// Update what appears in the game window using double buffering
	public void paint(Graphics g) {
		image = createImage(GAME_WIDTH, GAME_HEIGHT);
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}

	// call the needed draw and display methods to update positions and text as
	// things
	// move
	private void draw(Graphics g) {
		
	}


	// call the move methods in other classes to update positions
	private void move() {
		paddleOne.move();
		paddleTwo.move();
		ball.move();
	}

	// handles all collision detection and responds accordingly
	private void checkCollision() {

		// force paddles to remain on screen
		if (paddleOne.y <= 0) {
			paddleOne.y = 0;
		}
		if (paddleOne.y >= GAME_HEIGHT - Paddle.LENGTH) {
			paddleOne.y = GAME_HEIGHT - Paddle.LENGTH;
		}
		if (paddleTwo.y <= 0) {
			paddleTwo.y = 0;
		}
		if (paddleTwo.y >= GAME_HEIGHT - Paddle.LENGTH) {
			paddleTwo.y = GAME_HEIGHT - Paddle.LENGTH;
		}

		// forces the game ball to remain on screen
		// makes the ball bounce off the top and bottom edges of the panel
		// and plays the corresponding bouncing sound effect
		if (ball.y <= 0) {
			ball.y = 0;
			ball.setYDirection(-ball.getYVelocity());
		}
		if (ball.y >= GAME_HEIGHT - Ball.BALL_DIAMETER) {
			ball.y = GAME_HEIGHT - Ball.BALL_DIAMETER;
			ball.setYDirection(-ball.getYVelocity());
		}

		// The right player scores a point if the ball goes off the left edge of the
		// panel
		// Reset the positions of the ball and the paddles to their starting positions
		// Play the corresponding sound effect for winning a point
		if (ball.x <= 0) {
			ball.reset((GAME_WIDTH - Ball.BALL_DIAMETER) / 2, (GAME_HEIGHT - Ball.BALL_DIAMETER) / 2);
			paddleOne.reset(GAME_WIDTH / 8, (GAME_HEIGHT - Paddle.LENGTH + Ball.BALL_DIAMETER) / 2);
			paddleTwo.reset(7 * GAME_WIDTH / 8, (GAME_HEIGHT - Paddle.LENGTH + Ball.BALL_DIAMETER) / 2);

		}

		// The left player scores a point if the ball goes off the right edge of the
		// panel
		// Reset the positions of the ball and the paddles to their starting positions
		// Play the corresponding sound effect for winning a point
		if (ball.x >= GAME_WIDTH - Ball.BALL_DIAMETER) {
			ball.reset((GAME_WIDTH - Ball.BALL_DIAMETER) / 2, (GAME_HEIGHT - Ball.BALL_DIAMETER) / 2);
			paddleOne.reset(GAME_WIDTH / 8, (GAME_HEIGHT - Paddle.LENGTH + Ball.BALL_DIAMETER) / 2);
			paddleTwo.reset(7 * GAME_WIDTH / 8, (GAME_HEIGHT - Paddle.LENGTH + Ball.BALL_DIAMETER) / 2);

		}

		// makes the ball bounce off the paddles
		// and play the bouncing sound effect
		if (paddleOne.intersects(ball)) {

			ball.setXDirection(Ball.SPEED);

			// Steadily increases the magnitude of the ball's y velocity
			// Randomly stays the same or increases by 1
			// So the game becomes increasingly harder over time
			if (ball.getYVelocity() >= 1) {
				ball.setYDirection((int) (2 * Math.random()) + ball.getYVelocity());
			} else if (ball.getYVelocity() <= -1) {
				ball.setYDirection((int) (-2 * Math.random()) + ball.getYVelocity());
			}

			// If the ball's y velocity is 0
			// It will randomly become -1, 0 or 1
			else {
				ball.setYDirection((int) (3 * Math.random()) - 1);
			}

		}
		if (paddleTwo.intersects(ball)) {

			ball.setXDirection(-Ball.SPEED);

			// Steadily increases the magnitude of the ball's y velocity
			// Randomly stays the same or increases by 1
			// So the game becomes increasingly harder over time
			if (ball.getYVelocity() >= 1) {
				ball.setYDirection((int) (2 * Math.random()) + ball.getYVelocity());
			} else if (ball.getYVelocity() <= -1) {
				ball.setYDirection((int) (-2 * Math.random()) + ball.getYVelocity());
			}

			// If the ball's y velocity is 0
			// It will randomly become -1, 0 or 1
			else {
				ball.setYDirection((int) (3 * Math.random()) - 1);
			}

		}
	}

	// check if the game has ended
	private void checkGameEnd() {
		
	}

	// makes the game continue running without end, at 60 frames per second
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			// only move objects around and update screen if enough time has passed
			if (delta >= 1) {
				move();
				checkCollision();
				checkGameEnd();
				repaint();
				delta--;
			}
		}
	}

	// if a key is pressed, send it over to the corresponding classes for processing
	public void keyPressed(KeyEvent e) {


	}

	// if a key is released, we'll send it over to the paddles for processing
	public void keyReleased(KeyEvent e) {
		paddleOne.keyReleased(e);
		paddleTwo.keyReleased(e);
	}

	// must be here because it is required to
	// be overridden by the KeyListener interface
	public void keyTyped(KeyEvent e) {

	}
}
