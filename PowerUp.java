/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 11
 * Description: PowerUp class manages the display, position and velocity of the power up
 */

import java.awt.*;

public class PowerUp extends Rectangle{
	public static final Color COLORS[] = {Color.yellow, Color.white};
	
	public static final int WIDTH = 60;
	public static final int HEIGHT = 15;
	public static final int SPEED = 2;
	
	public Color color;
	
	public PowerUp(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		
		color = COLORS[(int)((COLORS.length)*Math.random())];
	}
	
	public void move() {
		y = y + SPEED;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x+WIDTH/4, y, WIDTH/2, HEIGHT);
		g.fillOval(x, y, WIDTH/2, HEIGHT);
		g.fillOval(x+WIDTH/2, y, WIDTH/2, HEIGHT);
		
		g.setFont(new Font("Consolas", Font.BOLD, 15));
		g.setColor(Color.black);
		if(color == Color.yellow) {
			g.drawString("thru", x+WIDTH/4, y+HEIGHT-1);
		}
		else if(color == Color.white) {
			g.drawString("+L", x+WIDTH/3, y+HEIGHT-1);
		}
		else if(color == Color.pink) {
			g.drawString("-S", x+WIDTH/3, y+HEIGHT-1);
		}
	}
}
