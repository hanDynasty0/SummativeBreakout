
import java.awt.*;
import java.awt.event.KeyEvent;

public class Ball extends Rectangle {

	private int xVelocity, yVelocity;
	public static final int SPEED = 8;
	public static final int BALL_DIAMETER = 20; // size of ball

	// constructor creates a stationary ball at given location with given dimensions
	public Ball(int x, int y) {
		super(x, y, BALL_DIAMETER, BALL_DIAMETER);
		xVelocity = 0;
		yVelocity = 0;
	}

	// accesses the x velocity of the ball
	public int getXVelocity() {
		return xVelocity;
	}

	// accesses the y velocity of the ball
	public int getYVelocity() {
		return yVelocity;
	}

	// If SPACE is pressed, set the ball's x and y velocities to the specified
	// values
	public void keyPressed(KeyEvent e, int xV, int yV) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && xVelocity == 0 && yVelocity == 0) {
			xVelocity = xV;
			yVelocity = yV;
		}
	}

	// Set the ball's x velocity to the specified value, and let the ball move
	public void setXDirection(int xDirection) {
		xVelocity = xDirection;
		move();
	}

	// Set the ball's y velocity to the specified value, and let the ball move
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
		move();
	}

	// called frequently from the GamePanel class
	// updates the current location of the ball
	public void move() {
		x = x + xVelocity;
		y = y + yVelocity;
	}

	// reset the position of the ball to the specified location
	// and make the ball stationary
	public void reset(int x, int y) {
		this.x = x;
		this.y = y;
		xVelocity = 0;
		yVelocity = 0;
	}

	// called frequently from the GamePanel class
	// draws the current location of the ball to the screen
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
	}

	// remove the ball's visibility to the users
	public void clear(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
	}
}