
import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

	private GamePanel panel;

	// Constructor starts the game and sets properties for the game window
	public GameFrame() {
		panel = new GamePanel();
		this.add(panel);
		this.setTitle("PONG");
		this.setResizable(false);
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

}