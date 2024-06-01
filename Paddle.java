
import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {

	private int yVelocity;
	private int up, down; // key codes for the paddle going up and down, respectively

	public final int SPEED = 8; // movement speed of paddle
	public static final int LENGTH = 90; // vertical length of paddle
	public static final int WIDTH = 7; // width (or thickness) of paddle

	// constructor creates paddle at given location with given dimensions
	// and specified controls for moving up and down
	public Paddle(int x, int y, int u, int d) {
		super(x, y, WIDTH, LENGTH);
		up = u;
		down = d;
	}

	// called from GamePanel when any keyboard input is detected
	// updates the direction of the ball based on user input
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == up) {
			setYDirection(SPEED * -1);
			move();
		}

		if (e.getKeyCode() == down) {
			setYDirection(SPEED);
			move();
		}
	}

	// called from GamePanel when any key is released (no longer being pressed down)
	// Makes the paddle stop moving in that direction
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == up || e.getKeyCode() == down) {
			setYDirection(0);
			move();
		}
	}

	// called whenever the movement of the ball changes in the y-direction (up/down)
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}

	// called frequently from both PlayerBall class and GamePanel class
	// updates the current location of the ball
	public void move() {
		y = y + yVelocity;
	}

	// resets the position of the paddle to the specified location
	// makes the paddle stationary
	public void reset(int x, int y) {
		this.x = x;
		this.y = y;
		yVelocity = 0;
	}

	// draws the current location of the paddle to the screen
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, WIDTH, LENGTH);
	}

}