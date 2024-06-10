
/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 4
 * Description: Paddle class creates and controls movement of paddle on screen
 */
import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {

	public int xVelocity;
	public final int SPEED = 10; // speed of paddle
	public static int width = GamePanel.GAME_WIDTH / 10;
	public static final int HEIGHT = 5; // dimensions of paddle
	
	// constructor creates paddle at given location with given dimensions
	public Paddle(int x, int y) {
		super(x, y, width, HEIGHT);

	}
	
	public void setWidth(int wid) {
		x -= (wid-width)/2;
		super.width=wid;
		width = wid;
	}

	// checks for specific key input for paddle
	public void keyPressed(KeyEvent e) {

		// paddle only moves using left and right arrow keys
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			setXDirection(SPEED * -1);
			move();
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			setXDirection(SPEED);
			move();
		}

	}

	// makes paddle stop moving after arrow keys are released
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			setXDirection(0);
			move();
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			setXDirection(0);
			move();
		}

	}

//called when movement of the paddle changes in the x-direction 
	public void setXDirection(int xDirection) {
		xVelocity = xDirection;
	}

	// position of paddle updated according to velocity
	public void move() {
		x = x + xVelocity;
	}

	// paddle appearance
	// white in color
	public void draw(Graphics g) {

		g.setColor(Color.white);
		g.fillRect(x, y, width, HEIGHT);

	}

}
