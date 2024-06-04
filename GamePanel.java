/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 4
 * Description: GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called
 */

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions of window
	public static final int GAME_WIDTH = 1000;
	public static final int GAME_HEIGHT = GAME_WIDTH * 2 / 3;
	public boolean instructions = true;

	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	public Paddle paddle;
	public Ball ball;
	
	public ArrayList<Brick> curBricks;

	public GamePanel() {

		// creating paddle near the bottom of screen
		paddle = new Paddle((GAME_WIDTH - Paddle.WIDTH)/2, 15*(GAME_HEIGHT - Paddle.HEIGHT)/16);

		// creating ball near the bottom of screen
		ball = new Ball(GAME_WIDTH / 2 - Ball.size / 2, 3*GAME_HEIGHT/4 - Ball.size/2);
	
		// creating a list with all the bricks
		curBricks = new ArrayList<>();
		
		// adding a sample of bricks to the list
		curBricks.add(new Brick(100,100,false,Color.red));
		curBricks.add(new Brick(300,100,false, Color.red));
		curBricks.add(new Brick(500,100,false, Color.red));
		curBricks.add(new Brick(700,100,false, Color.red));
		curBricks.add(new Brick(900,100,false, Color.red));
		curBricks.add(new Brick(0,150,false, Color.yellow));
		curBricks.add(new Brick(200,150,false, Color.yellow));
		curBricks.add(new Brick(400,150,false, Color.yellow));
		curBricks.add(new Brick(600,150,false, Color.yellow));
		curBricks.add(new Brick(800,150,false, Color.yellow));
		curBricks.add(new Brick(100,200,false,Color.red));
		curBricks.add(new Brick(300,200,false, Color.red));
		curBricks.add(new Brick(500,200,false, Color.red));
		curBricks.add(new Brick(700,200,false, Color.red));
		curBricks.add(new Brick(900,200,false, Color.red));

		this.setFocusable(true);
		this.addKeyListener(this); // start listening for keyboard input

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		// creating threads to run things simultaneously
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void paint(Graphics g) {
		// images drawn off screen and moved on screen to prevent lag visible to humans
		image = createImage(GAME_WIDTH, GAME_HEIGHT);
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);

	}

	// calling all draw methods for all objects to be displayed
	public void draw(Graphics g) {
		paddle.draw(g);
		ball.draw(g);
	
		// instructions are displayed until space bar is hit at the start
		if (instructions) {

			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.PLAIN, 20));
			g.drawString("Press spacebar to start! Knock out all of the blocks!", GAME_WIDTH * 1 / 4, GAME_HEIGHT * 6 / 7);
			g.drawString("Use the left and right arrow keys to move the paddle...", GAME_WIDTH * 1 / 4, GAME_HEIGHT * 25 / 28);
			
		}

		// draw all bricks in the list
		for(Brick b: curBricks) {
			b.draw(g);
		}

	}

	// positions of all moving objects constantly updated
	public void move() {
		paddle.move();
		ball.move();

	}

	// handles all collision detection and responds accordingly
	public void checkCollision() {

		// keep paddle on screen
		if (paddle.x <= 0) {
			paddle.x = 0;
		}
		if (paddle.x >= GAME_WIDTH - Paddle.WIDTH) {
			paddle.x = GAME_WIDTH - Paddle.WIDTH;
		}


		
		// ball bounce off top edge
		if (ball.y <= 0) {
			ball.y = 0;
			ball.setYDirection(-ball.yVelocity);
		}
		
		// ball bounce off left edge
		if (ball.x <= 0) {
			ball.x = 0;
			ball.setXDirection(-ball.xVelocity);
		}
		
		// ball bounce off right edge
		if (ball.x >= GAME_WIDTH - Ball.size) {
			ball.x = GAME_WIDTH - Ball.size;
			ball.setXDirection(-ball.xVelocity);
		}



		
		// ball bounces off paddle
		if (ball.intersects(paddle)) {
			int ballX = ball.x + Ball.size/2;
			int paddleX = paddle.x + Paddle.WIDTH/2;
			
			ball.y = paddle.y - Ball.size;
			ball.setYDirection(-ball.yVelocity); // to bounce back
			
			// let the ball bounce in a certain direction depending on where it hits the paddle
			// with a random variance of +-1
			ball.setXDirection((ballX - paddleX)/10 + (int)(3*Math.random()) - 1);

			// makes ball bounces controllable by the player

		}



		// if ball hits bottom edge
		if (ball.y >= GAME_HEIGHT) {
	
			paddle = new Paddle((GAME_WIDTH - Paddle.WIDTH)/2, 15*(GAME_HEIGHT - Paddle.HEIGHT)/16);
			ball = new Ball(GAME_WIDTH / 2 - Ball.size / 2, 3*GAME_HEIGHT/4 - Ball.size/2);
			

		}	
		
		// ball bounces off of bricks
		// loop through all the bricks to detect collisions
		for(int i = 0; i < curBricks.size(); i++) {
			
			if(ball.intersects(curBricks.get(i))) {
				
				Brick b = curBricks.remove(i); // removes the brick so it is no longer drawn
				
				// bounce off the left or right side of the brick
				if(ball.y - ball.yVelocity >= b.y && ball.y - ball.yVelocity <= b.y + Brick.HEIGHT) {
					
					// if the ball approached the brick from the right, set the ball's position to the right of the brick
					if(ball.xVelocity < 0) {
						ball.x = b.x + Brick.WIDTH;
					}
					
					// if the ball approached the brick from the left, set the ball's position to the left of the brick
					else {
						ball.x = b.x - Ball.size;
					}
					
					ball.setXDirection(-ball.xVelocity);
				}
				
				// bounce off the top or bottom side of the brick
				else {
					
					// if the ball approached the brick from the bottom, set the ball's position to the bottom of the brick
					if(ball.yVelocity < 0) {
						ball.y = b.y + Brick.HEIGHT;
					}
					
					// if the ball approaches the brick from the top, set the ball's position to the top of the brick
					else {
						ball.y = b.y - Ball.size;
					}
					
					ball.setYDirection(-ball.yVelocity);
				}
				
				break;
			}
		}

	}

	// runs and calls other methods continually
	public void run() {

		// following lines of code "force" computer to get stuck in a loop for short
		// intervals between calling other methods to update screen.
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop

			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			// only move objects around and update screen if enough time has passed
			if (delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}

	// calls all keyPressed methods for objects if pressing of key is detected for
	// further action
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			instructions = false; // stop showing instructions after hitting space bar
		}

		paddle.keyPressed(e);
		ball.keyPressed(e);
	}

	// calls all keyReleased methods for objects if release of key is detected for
	// further action
	public void keyReleased(KeyEvent e) {
		paddle.keyReleased(e);
	}

	// left empty as not needed
	// here as required to be overrode by KeyListener interface
	public void keyTyped(KeyEvent e) {

	}
}
