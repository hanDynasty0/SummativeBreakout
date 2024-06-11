/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 11
 * Description: Brick class manages the display and position of the bricks
 */

import java.awt.*;

public class Brick extends Rectangle {
	public static final int WIDTH = 80;
	public static final int HEIGHT = 40;

	boolean hasPowerUp;

	Color color;

	// constructor creates a brick at given location with given dimensions
	public Brick(int x, int y, boolean hasPower, Color col) {
		super(x, y, WIDTH, HEIGHT);
		hasPowerUp = hasPower;
		color = col;
	}

	// appearance and position of the brick
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, WIDTH, HEIGHT);
	}
}
