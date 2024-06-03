


import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {

	public int xVelocity;
	public final int SPEED = 8; // speed of paddle
	public static final int HEIGHT = 5, WIDTH = GamePanel.GAME_WIDTH/ 10; // dimensions of paddle

	public Paddle(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
	
	}

	// checks for specific key input for each paddle
	public void keyPressed(KeyEvent e) {


		// second paddle only moves via up and down arrow keys
		
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				setXDirection(SPEED * -1);
				move();
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				setXDirection(SPEED);
				move();
			}
		

	}

	// makes paddles stop moving after specific keys are released
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

	// left paddle appears blue and right paddle appears red
	public void draw(Graphics g) {
		
		g.setColor(Color.white);
		g.fillRect(x, y, WIDTH, HEIGHT);

	}

}

