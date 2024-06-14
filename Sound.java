/*
 * Author: Hazel Bains and Han Fang
 * Date of Latest Update: June 17, 2024
 * Sound class defines behaviours for music and sound effects in the game
 * Reference:
 * https://www.youtube.com/watch?v=nUHh_J2Acy8
 */

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	private Clip clip; // stores the current audio clip
	private URL soundURL[] = new URL[7]; // array of all the audio urls to be used in the game

	// constructor adds the audio urls to the relevant array
	public Sound() {
		soundURL[0] = getClass().getResource("/sound/Opening.wav");
		soundURL[1] = getClass().getResource("/sound/Brick_Hit.wav");
		soundURL[2] = getClass().getResource("/sound/Wall_Paddle_Hit.wav");
		soundURL[3] = getClass().getResource("/sound/Power_Up.wav");
		soundURL[4] = getClass().getResource("/sound/Reset.wav");
		soundURL[5] = getClass().getResource("/sound/Win.wav");
		soundURL[6] = getClass().getResource("/sound/Loss.wav");
	}

	// sets which audio file the object is referencing in other methods
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			// Should not happen since all the sound files the urls refer to exist
			// Needed because AudioSystem's possible exception is flagged for an error
		}
	}

	// plays the selected audio file
	public void play() {
		clip.start();
	}

	// loops the selected audio file
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
}
