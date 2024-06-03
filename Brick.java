import java.awt.*;

public class Brick extends Rectangle{
	public static final int WIDTH = 100;
	public static final int HEIGHT = 50;
	
	boolean hasPowerUp;
	
	Color color;
	
	public Brick(int x, int y, boolean hasPower, Color col) {
		super(x,y, WIDTH, HEIGHT);
		hasPowerUp = hasPower;
		color = col;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, WIDTH, HEIGHT);
	}
}
