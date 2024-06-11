/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 11
 * Description: Ball class manages the display, position and velocity of the ball
 */

import java.awt.*;
import java.awt.event.KeyEvent;

public class Ball extends Rectangle {

	public static final int SIZE = 20;
	public static final int Y_SPEED = 8;
	public int yVelocity;
	public int xVelocity;
	public static int xVelocityFactor = 6;

	public Color color;

	// constructor creates a stationary ball at given location with given dimensions
	public Ball(int x, int y) {
		super(x, y, SIZE, SIZE);
		xVelocity = 0;
		yVelocity = 0;
		color = Color.white;
	}

	// called from GamePanel when any keyboard input is detected
	// updates the direction of the ball based on user input
	public void keyPressed(KeyEvent e) {

		// if ball is not moving towards or away from the bricks
		// if ball is on paddle
		if (yVelocity == 0) {

			// when space bar is hit, release ball
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				xVelocity = 0;
				yVelocity = -Y_SPEED;
				move();
			}

			// move left with the paddle
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				xVelocity = -10;
				yVelocity = 0;
				move();
			}
			
			// move right with the paddle
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				xVelocity = 10;
				yVelocity = 0;
				move();
			}
		}

	}

	// called from GamePanel when any key is released (no longer being pressed down)
	// Makes the ball stop moving in that direction
	public void keyReleased(KeyEvent e) {
		if (yVelocity == 0) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				xVelocity = 0;
				yVelocity = 0;
				move();
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				xVelocity = 0;
				yVelocity = 0;
				move();
			}
		}
	}

	// called when movement of ball changes in the y-direction
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}

	// called when movement of ball changes in the x-direction
	public void setXDirection(int xDirection) {
		xVelocity = xDirection;
	}

	// position of ball updated according to velocity
	public void move() {
		y = y + yVelocity;
		x = x + xVelocity;
	}

	// appearance and position of ball
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, SIZE, SIZE);
	}

}
