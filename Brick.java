import java.awt.*;

public class Brick extends Rectangle{
	public static final int LENGTH = 100;
	public static final int WIDTH = 50;
	
	boolean hasPowerUp;
	
	Color color;
	
	public Brick(int x, int y, boolean hasPower, Color col) {
		super(x,y);
		hasPowerUp = hasPower;
		color = col;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, LENGTH, WIDTH);
	}
}
