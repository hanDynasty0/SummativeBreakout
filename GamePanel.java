/* 
 * Author: Han Fang and Hazel Bains
 * Date: June 11
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

	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	public Paddle paddle;
	public Ball ball;
	public Sound sound;

	public ArrayList<Brick> curBricks;
	public ArrayList<PowerUp> powerUps;

	public boolean instructions = true;
	public boolean started;
	public boolean runThru, isStick;
	public boolean powerUpActing;
	public int powerBounces, lives = 9;
	public long startMusicTime, endingMusicTime;

	public int level;

	public GamePanel() {

		// creating paddle near the bottom of screen
		paddle = new Paddle((GAME_WIDTH - Paddle.width) / 2, 15 * (GAME_HEIGHT - Paddle.HEIGHT) / 16);

		// creating ball on the paddle
		ball = new Ball(GAME_WIDTH / 2 - Ball.SIZE / 2, paddle.y - Ball.SIZE);

		// enabling sound in the game
		sound = new Sound();

		// creating a list with all the power ups
		powerUps = new ArrayList<>();

		// creating a list with all the bricks
		curBricks = new ArrayList<>();

		powerUpActing = false;
		powerBounces = 0;
		runThru = false;
		isStick = false;

		level = 0;
		startMusicTime = Long.MAX_VALUE;
		endingMusicTime = Long.MAX_VALUE;

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

		if (!started) {
			powerUps.clear();

			g.setColor(Color.white);
			g.setFont(new Font("Calibri", Font.PLAIN, 30));
			g.drawString("WELCOME TO:", GAME_WIDTH * 1 / 4, GAME_HEIGHT * 1 / 7);
			g.setFont(new Font("Calibri", Font.PLAIN, 100));
			g.drawString("BREAKOUT", GAME_WIDTH * 1 / 4, GAME_HEIGHT * 1 / 4);

			g.setFont(new Font("Calibri", Font.PLAIN, 25));
			g.drawString("--> Knock out all of the blocks!", GAME_WIDTH * 1 / 5, GAME_HEIGHT * 10 / 14);
			g.drawString("--> You have 9 lives to complete 3 levels!", GAME_WIDTH * 1 / 5,GAME_HEIGHT * 11 / 14);
			g.drawString("--> Collect power ups to help you! They last for 5 bounces.", GAME_WIDTH * 1 / 5,GAME_HEIGHT * 12 / 14);
			g.setFont(new Font("Calibri", Font.PLAIN, 30));
			g.drawString("Hit SPACE to continue!", GAME_WIDTH * 7/ 20,GAME_HEIGHT * 13/ 14);

			curBricks.add(new Brick(375, 350, true, Color.red));
			curBricks.add(new Brick(460, 350, true, Color.cyan));
			curBricks.add(new Brick(545, 350, true, Color.green));
			curBricks.add(new Brick(415, 305, true, Color.yellow));
			curBricks.add(new Brick(500, 305, true, Color.magenta));
			curBricks.add(new Brick(455, 260, true, Color.blue));

		}
		if (started) {
			paddle.draw(g);
			ball.draw(g);
		}
		// instructions are displayed until space bar is hit at the start
		if (instructions && started) {

			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.PLAIN, 20));
			g.drawString("Press spacebar to start!", GAME_WIDTH * 1 / 3,
					GAME_HEIGHT * 6 / 7);
			g.drawString("Use the left and right arrow keys to move the paddle...", GAME_WIDTH * 1 / 5,
					GAME_HEIGHT * 25 / 28);

		}

		// lives and level displayed at top left corner
		// if the game is playing
		if (lives > 0 && level <= 3 && started) {
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.PLAIN, 15));
			g.drawString("Lives left: " + lives, GAME_WIDTH * 1 / 21, GAME_HEIGHT * 1 / 20);
			g.drawString("Level: " + level, GAME_WIDTH * 1 / 6, GAME_HEIGHT * 1 / 20);
		}

		// draw all bricks in the list
		for (Brick b : curBricks) {
			b.draw(g);
		}

		// draw all power ups in the list
		for (PowerUp p : powerUps) {
			p.draw(g);
		}

		// draw the lose screen if the player has no lives
		if (lives == 0) {
			lose(g);
		}

		// draw the win screen if the player won all the levels
		if (level == 4) {
			win(g);
		}

	}

	// method displays winning message
	public void win(Graphics g) {
		curBricks.clear();
		g.setColor(Color.green);
		g.setFont(new Font("Consolas", Font.PLAIN, 30));
		g.drawString("You Won !!! Hit SPACE to return to home!", GAME_WIDTH / 6, GAME_HEIGHT / 2);
	}

	// method displays losing message
	public void lose(Graphics g) {
		curBricks.clear();
		g.setColor(Color.red);
		g.setFont(new Font("Consolas", Font.PLAIN, 30));
		g.drawString("You Lost !!! Hit SPACE to return to home!", GAME_WIDTH / 6, GAME_HEIGHT / 2);

	}

	// positions of all moving objects constantly updated
	public void move() {
		paddle.move();
		ball.move();

		// move all power ups
		for (PowerUp p : powerUps) {
			p.move();
		}
	}

	// handles all collision detection and responds accordingly
	public void checkCollision() {
		
		if(started) {
			// keep paddle on screen
			if (paddle.x <= 0) {
				paddle.x = 0;
			}
			if (paddle.x >= GAME_WIDTH - Paddle.width) {
				paddle.x = GAME_WIDTH - Paddle.width;
			}

			// keep the ball on the paddle when the player has not launched the ball
			if (ball.yVelocity == 0 && ball.x <= (Paddle.width - Ball.SIZE) / 2) {
				ball.x = (Paddle.width - Ball.SIZE) / 2;
			}
			if (ball.yVelocity == 0 && ball.x >= GAME_WIDTH - (Paddle.width + Ball.SIZE) / 2) {
				ball.x = GAME_WIDTH - (Paddle.width + Ball.SIZE) / 2;
			}

			// ball bounce off top edge
			if (ball.y <= 0) {
				playSoundEffect(2);
				ball.y = 0;
				ball.setYDirection(-ball.yVelocity);
			}

			// ball bounce off left edge
			if (ball.x <= 0) {
				playSoundEffect(2);
				ball.x = 0;
				ball.setXDirection(-ball.xVelocity);
			}

			// ball bounce off right edge
			if (ball.x >= GAME_WIDTH - Ball.SIZE) {
				playSoundEffect(2);
				ball.x = GAME_WIDTH - Ball.SIZE;
				ball.setXDirection(-ball.xVelocity);
			}

			// ball bounces off paddle
			if (ball.intersects(paddle)) {
				playSoundEffect(2);

				int ballX = ball.x + Ball.SIZE / 2;
				int paddleX = paddle.x + Paddle.width / 2;

				ball.y = paddle.y - Ball.SIZE;

				// if the sticky power up is activated
				// the ball stops on the paddle
				if (isStick) {
					ball.setXDirection(0);
					ball.setYDirection(0);
					ball.color = Color.gray;
				}

				// if the ball doesn't stick to the paddle
				else {
					ball.setYDirection(-ball.yVelocity); // to bounce back

					// let the ball bounce in a certain direction depending on where it hits the
					// paddle
					// with a random variance of +-1
					ball.setXDirection((ballX - paddleX) / 7 + (int) (3 * Math.random()) - 1);

					// makes ball bounces controllable by the player
				}

				if (powerUpActing) {
					powerBounces++;
				}
				// power up works for 5 bounces of the ball before it's gone
				if (powerBounces > 5) {
					resetPowerUps();
				}
			}

			// if the sticky power up is activated
			// and the ball is at rest on the paddle
			// set the ball to be in the middle of the paddle
			if (isStick && ball.yVelocity == 0) {
				if (ball.x != paddle.x + (Paddle.width - Ball.SIZE) / 2) {
					ball.x = paddle.x + (Paddle.width - Ball.SIZE) / 2;
				}
			}

			// if ball passes bottom edge
			if (ball.y >= GAME_HEIGHT) {
				powerUps.clear();
				resetPowerUps();

				playSoundEffect(4);
				// player loses a life if ball hits bottom edge of screen
				lives--;
				paddle = new Paddle((GAME_WIDTH - Paddle.width) / 2, 15 * (GAME_HEIGHT - Paddle.HEIGHT) / 16);
				ball = new Ball(GAME_WIDTH / 2 - Ball.SIZE / 2, paddle.y - Ball.SIZE);

			}

			// ball bounces off of bricks
			// loop through all the bricks to detect collisions
			for (int i = 0; i < curBricks.size(); i++) {

				if (ball.intersects(curBricks.get(i))) {

					playSoundEffect(1);

					Brick b = curBricks.remove(i); // removes the brick so it is no longer drawn

					// spawn a power up if the broken brick contains one
					if (b.hasPowerUp) {
						powerUps.add(new PowerUp(b.x, b.y));
					}

					// bounce off the left or right side of the brick
					if (!runThru && ball.y + Ball.SIZE / 2 - ball.yVelocity >= b.y
							&& ball.y + Ball.SIZE / 2 - ball.yVelocity <= b.y + Brick.HEIGHT) {

						ball.setXDirection(-ball.xVelocity);
					}

					// bounce off the top or bottom side of the brick
					else if (!runThru) {

						ball.setYDirection(-ball.yVelocity);
					}

					break;
				}
			}

			// paddle interaction with power ups
			for (int i = 0; i < powerUps.size(); i++) {

				if (powerUps.get(i).intersects(paddle)) {

					playSoundEffect(3);

					PowerUp p = powerUps.remove(i);

					// yellow power up allows ball to run through many bricks
					if (p.color == Color.yellow) {
						resetPowerUps();
						runThru = true;
						ball.color = Color.yellow;

					}
					// white power up increases length of paddle
					else if (p.color == Color.white) {
						resetPowerUps();
						paddle.setWidth(GAME_WIDTH / 7);
					}

					// pink power up makes ball slower
					else if (p.color == Color.pink) {
						resetPowerUps();
						ball.color = Color.pink;
						if (Math.abs(ball.yVelocity) == Ball.Y_SPEED) {
							ball.setXDirection(3 * ball.xVelocity / 4);
						}
						if (ball.yVelocity > 0) {
							ball.setYDirection(3 * Ball.Y_SPEED / 4);
						} else {
							ball.setYDirection(-3 * Ball.Y_SPEED / 4);
						}
						Ball.xVelocityFactor = 8;
					}

					// gray power up makes the ball stick to the paddle
					else if (p.color == Color.gray) {
						resetPowerUps();
						ball.color = Color.gray;
						isStick = true;

						// if the ball is already on the paddle, let the ball remain on the paddle
						if (ball.y == paddle.y - Ball.SIZE) {
							ball.yVelocity = 0;
						}
					}
					powerUpActing = true;

					break;
				}
			}

			// remove the power up from the list if it falls off the screen
			for (int i = 0; i < powerUps.size(); i++) {
				if (powerUps.get(i).y >= GAME_HEIGHT) {
					powerUps.remove(i);
					break;
				}
			}
			
			// draw the lose screen if the player has no lives
			if (lives == 0) {
				startMusicTime = Long.MAX_VALUE;
				stopMusic();
				if (System.currentTimeMillis() < endingMusicTime) {
					endingMusicTime = System.currentTimeMillis();
				}
				while (System.currentTimeMillis() <= endingMusicTime + 10) {
					playSoundEffect(6);
				}
			}

			// draw the win screen if the player won all the levels
			if (level == 4) {
				if (System.currentTimeMillis() < endingMusicTime) {
					endingMusicTime = System.currentTimeMillis();
				}
				while (System.currentTimeMillis() <= endingMusicTime + 10) {
					playSoundEffect(5);
				}
			}
		}
		
		else {
			if (System.currentTimeMillis() < startMusicTime) {
				startMusicTime = System.currentTimeMillis();
			}
			while (System.currentTimeMillis() <= startMusicTime + 10) {
				playMusicLoop(0);
			}
		}
	}

	// changes the level by resetting power ups and adding bricks
	public void changeLevel() {
		if (curBricks.isEmpty() && lives > 0 && level <= 3 && started) {

			resetPowerUps();

			// creating paddle near the bottom of screen
			paddle = new Paddle((GAME_WIDTH - Paddle.width) / 2, 15 * (GAME_HEIGHT - Paddle.HEIGHT) / 16);

			// creating ball on the paddle
			ball = new Ball(GAME_WIDTH / 2 - Ball.SIZE / 2, paddle.y - Ball.SIZE);

			powerUps.clear();

			level++;

			if (level == 1) {

				// level one brick design
				curBricks.add(new Brick(5, 100, true, Color.red));
				curBricks.add(new Brick(90, 100, false, Color.yellow));
				curBricks.add(new Brick(175, 100, false, Color.blue));
				curBricks.add(new Brick(260, 100, false, Color.green));

				curBricks.add(new Brick(70, 145, true, Color.red));
				curBricks.add(new Brick(155, 145, false, Color.yellow));
				curBricks.add(new Brick(240, 145, false, Color.blue));
				curBricks.add(new Brick(325, 145, false, Color.green));

				curBricks.add(new Brick(135, 190, false, Color.red));
				curBricks.add(new Brick(220, 190, false, Color.yellow));
				curBricks.add(new Brick(305, 190, false, Color.blue));
				curBricks.add(new Brick(390, 190, false, Color.green));

				curBricks.add(new Brick(200, 235, false, Color.red));
				curBricks.add(new Brick(285, 235, false, Color.yellow));
				curBricks.add(new Brick(370, 235, false, Color.blue));
				curBricks.add(new Brick(455, 235, false, Color.green));

				curBricks.add(new Brick(265, 280, false, Color.red));
				curBricks.add(new Brick(350, 280, false, Color.yellow));
				curBricks.add(new Brick(435, 280, true, Color.blue));
				curBricks.add(new Brick(520, 280, false, Color.green));

				curBricks.add(new Brick(330, 325, false, Color.red));
				curBricks.add(new Brick(415, 325, false, Color.yellow));
				curBricks.add(new Brick(500, 325, false, Color.blue));
				curBricks.add(new Brick(585, 325, false, Color.green));

				curBricks.add(new Brick(395, 370, false, Color.red));
				curBricks.add(new Brick(480, 370, true, Color.yellow));
				curBricks.add(new Brick(565, 370, false, Color.blue));
				curBricks.add(new Brick(650, 370, false, Color.green));

				curBricks.add(new Brick(460, 415, false, Color.red));
				curBricks.add(new Brick(545, 415, false, Color.yellow));
				curBricks.add(new Brick(630, 415, false, Color.blue));
				curBricks.add(new Brick(715, 415, false, Color.green));

			}

			else if (level == 2) {
				// level 2 brick design
				curBricks.add(new Brick(500, 100, false, Color.cyan));
				curBricks.add(new Brick(415, 100, false, Color.cyan));
				curBricks.add(new Brick(545, 145, false, Color.cyan));
				curBricks.add(new Brick(460, 145, true, Color.magenta));
				curBricks.add(new Brick(375, 145, false, Color.cyan));
				curBricks.add(new Brick(500, 190, false, Color.magenta));
				curBricks.add(new Brick(415, 190, false, Color.magenta));
				curBricks.add(new Brick(330, 190, false, Color.cyan));
				curBricks.add(new Brick(585, 190, false, Color.cyan));
				curBricks.add(new Brick(545, 235, false, Color.cyan));
				curBricks.add(new Brick(460, 235, true, Color.magenta));
				curBricks.add(new Brick(375, 235, false, Color.cyan));
				curBricks.add(new Brick(500, 280, false, Color.cyan));
				curBricks.add(new Brick(415, 280, false, Color.cyan));

				curBricks.add(new Brick(700, 100, true, Color.magenta));
				curBricks.add(new Brick(785, 100, false, Color.magenta));
				curBricks.add(new Brick(870, 100, false, Color.magenta));
				curBricks.add(new Brick(915, 145, false, Color.cyan));
				curBricks.add(new Brick(830, 145, false, Color.cyan));
				curBricks.add(new Brick(745, 145, false, Color.cyan));
				curBricks.add(new Brick(785, 190, false, Color.magenta));
				curBricks.add(new Brick(870, 190, false, Color.magenta));
				curBricks.add(new Brick(830, 235, false, Color.cyan));
				curBricks.add(new Brick(915, 235, false, Color.cyan));
				curBricks.add(new Brick(745, 235, false, Color.cyan));
				curBricks.add(new Brick(700, 280, false, Color.magenta));
				curBricks.add(new Brick(785, 280, false, Color.magenta));
				curBricks.add(new Brick(870, 280, false, Color.magenta));

				curBricks.add(new Brick(215, 100, false, Color.cyan));
				curBricks.add(new Brick(130, 100, false, Color.cyan));
				curBricks.add(new Brick(45, 100, true, Color.cyan));
				curBricks.add(new Brick(175, 145, false, Color.magenta));
				curBricks.add(new Brick(90, 145, false, Color.magenta));
				curBricks.add(new Brick(5, 145, false, Color.magenta));
				curBricks.add(new Brick(130, 190, true, Color.cyan));
				curBricks.add(new Brick(45, 190, false, Color.cyan));
				curBricks.add(new Brick(175, 235, false, Color.magenta));
				curBricks.add(new Brick(90, 235, false, Color.magenta));
				curBricks.add(new Brick(5, 235, false, Color.magenta));
				curBricks.add(new Brick(215, 280, true, Color.cyan));
				curBricks.add(new Brick(130, 280, false, Color.cyan));
				curBricks.add(new Brick(45, 280, false, Color.cyan));

			}

			else if (level == 3) {
				instructions = true;
				// level 3 brick design
				curBricks.add(new Brick(115, 55, false, Color.blue));
				curBricks.add(new Brick(200, 100, false, Color.cyan));
				curBricks.add(new Brick(285, 145, true, Color.cyan));
				curBricks.add(new Brick(370, 190, false, Color.magenta));
				curBricks.add(new Brick(415, 235, false, Color.magenta));
				curBricks.add(new Brick(500, 235, false, Color.magenta));
				curBricks.add(new Brick(545, 190, false, Color.magenta));
				curBricks.add(new Brick(630, 145, false, Color.cyan));
				curBricks.add(new Brick(715, 100, false, Color.cyan));
				curBricks.add(new Brick(800, 55, false, Color.blue));

				curBricks.add(new Brick(500, 280, true, Color.magenta));
				curBricks.add(new Brick(415, 280, false, Color.magenta));
				curBricks.add(new Brick(370, 325, false, Color.magenta));
				curBricks.add(new Brick(285, 370, false, Color.cyan));
				curBricks.add(new Brick(200, 415, false, Color.cyan));
				curBricks.add(new Brick(115, 460, true, Color.blue));

				curBricks.add(new Brick(545, 325, false, Color.magenta));
				curBricks.add(new Brick(630, 370, false, Color.cyan));
				curBricks.add(new Brick(715, 415, false, Color.cyan));
				curBricks.add(new Brick(800, 460, true, Color.blue));
			}

		}
	}

	// method resets power ups so they are no longer in effect
	public void resetPowerUps() {
		runThru = false;
		isStick = false;
		ball.color = Color.white;
		paddle.setWidth(GAME_WIDTH / 10);

		if (Math.abs(ball.yVelocity) < Ball.Y_SPEED) {
			ball.setXDirection(4 * ball.xVelocity / 3);
		}
		if (ball.yVelocity > 0) {
			ball.setYDirection(Ball.Y_SPEED);
		} else {
			ball.setYDirection(-Ball.Y_SPEED);
		}
		Ball.xVelocityFactor = 6;

		powerUpActing = false;
		powerBounces = 0;
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
				changeLevel();
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

		if (!started && e.getKeyCode() == KeyEvent.VK_SPACE) {
			started = true;
			curBricks.clear();
			stopMusic();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE && started) {
			instructions = false; // stop showing instructions after hitting space bar
		}

		// if the player loses all lives or completes level 3
		if (lives == 0 || level == 4) {
			// if they hit space, the game is reset
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				level = 0;
				lives = 9;
				started = false;
				startMusicTime = Long.MAX_VALUE;
				endingMusicTime = Long.MAX_VALUE;
			}
		}

		paddle.keyPressed(e);
		ball.keyPressed(e);
	}

	// calls all keyReleased methods for objects if release of key is detected for
	// further action
	public void keyReleased(KeyEvent e) {
		paddle.keyReleased(e);
		ball.keyReleased(e);
	}

	// left empty as not needed
	// here as required to be overrode by KeyListener interface
	public void keyTyped(KeyEvent e) {

	}

	// play the selected music continuously in a loop
	public void playMusicLoop(int i) {
		sound.setFile(i);
		sound.play();
		sound.loop();
	}

	// play the selected sound effect
	public void playSoundEffect(int i) {
		sound.setFile(i);
		sound.play();
	}
	
	public void stopMusic() {
		sound.stop();
	}
}
