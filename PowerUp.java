/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 11
 * Description: PowerUp class manages the display, position and velocity of the power 
 */

import java.awt.*;

public class PowerUp extends Rectangle{
	public static final Color COLORS[] = {Color.yellow, Color.lightGray, Color.pink, Color.darkGray, Color.white};
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 20;
	public static final int SPEED = 2;
	
	public Color color;
	
	public PowerUp(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		
		color = COLORS[(int)(5*Math.random())];
	}
	
	public void move() {
		y = y + SPEED;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, WIDTH, HEIGHT);
	}
}
