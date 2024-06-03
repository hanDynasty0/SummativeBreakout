



import java.awt.*;
import java.awt.event.KeyEvent;

public class Ball extends Rectangle {

	public static final int size = 20;
	public int yVelocity;
	public int xVelocity;


	public Ball(int x, int y) {
		super(x, y, size, size);
	}

	public void keyPressed(KeyEvent e) {

		// if ball is not moving
		if (xVelocity == 0 && yVelocity == 0) {

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				xVelocity = 0;
				yVelocity = 8;

				

			}
		}

	}

	// called when movement of the ball changes in the y-direction
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}

	// called when movement of the ball changes in the x-direction
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
		g.setColor(Color.white);
		g.fillOval(x, y, size, size);
	}

}
