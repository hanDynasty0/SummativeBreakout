/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 11
 * Description: PowerUp class manages the display, position and velocity of the power up
 */

import java.awt.*;

public class PowerUp extends Rectangle{
	public static final Color COLORS[] = {Color.yellow, Color.white, Color.pink};
	
	public static final int WIDTH = 60;
	public static final int HEIGHT = 15;
	public static final int SPEED = 2;
	
	public Color color;
	
	// constructor creates power up at given location with given dimensions
	public PowerUp(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		
		//picks 1 of the random colors in the array each time a power up drops
		color = COLORS[(int)((COLORS.length)*Math.random())];
	}
	
	//power up falls at constant speed downwards
	public void move() {
		y = y + SPEED;
	}
	
	//creates pill shape of power up with appropriate writing on it and color to indicate what power up it is
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x+WIDTH/4, y, WIDTH/2, HEIGHT);
		g.fillOval(x, y, WIDTH/2, HEIGHT);
		g.fillOval(x+WIDTH/2, y, WIDTH/2, HEIGHT);
		
		g.setFont(new Font("Consolas", Font.BOLD, 15));
		g.setColor(Color.black);
		
		//rams through blocks power up
		if(color == Color.yellow) {
			g.drawString("thru", x+WIDTH/4, y+HEIGHT-1);
		}
		
		//paddle length increases power up
		else if(color == Color.white) {
			g.drawString("+L", x+WIDTH/3, y+HEIGHT-1);
		}
		//speed of ball slows power up
		else if(color == Color.pink) {
			g.drawString("-S", x+WIDTH/3, y+HEIGHT-1);
		}
	}
}
