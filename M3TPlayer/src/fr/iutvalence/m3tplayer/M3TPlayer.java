package fr.iutvalence.m3tplayer;

import java.util.Random;

import javax.sound.sampled.FloatControl;
import fr.iutvalence.exceptions.UnknownMediaException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class M3TPlayer{

	/**
	 * The volume of the player.
	 * Value is between 0 and 100.
	 */
	private int volume;
	
	/**
	 * Random playing mod
	 */
	private boolean randomPlaying;
	
	/**
	 * The current media
	 */
	private Media currentMedia;

	/**
	 * The library
	 */
	private Library library;
	
	/**
	 * The audio player
	 */
	private AdvancedPlayer player;
	
	/**
	 * Initializes the player with default values.
	 * Sets the volume to 100 percent, and the random mode to false, and the identifiant to 0.
	 */
	public M3TPlayer() {
		this.library = new Library();
		this.volume = 100;
		this.randomPlaying = false;

		if(this.library.isEmpty())
			this.currentMedia = null;
		else
			this.currentMedia = this.library.getMedia(0);
		
		this.player = null;
	}
	
	/**
	 * Switchs the random playing to on/off
	 */
	public void setRandomPlaying(){
		int size = this.library.listMedias.size();
		if (this.randomPlaying)
			this.randomPlaying=false;
		else {
			this.randomPlaying = true;
			Random rdm = new Random();
			this.currentMedia = this.library.getMedia(rdm.nextInt(size));			
		}
	}
	
	/**
	 * Allow to change the volume of the M3TPlayer
	 */
	public void changeVolume(){
		//TODO method
//		GainControl gain = this.player.getGainControl();
//		gain.setLevel((float)0.5);
	}

	/**
	 * Changes the current media to another one.
	 * @param control <tt>PREVIOUS</tt> to play the previous media,
	 *                <tt>NEXT</tt> to play the next media
	 */
	public void changeMedia(PlayingControl control){
		int size = this.library.listMedias.size();
		if (!randomPlaying){
			int mediaId = 0;
			try {
				mediaId = this.library.getMediaId(this.currentMedia);
			} catch (UnknownMediaException e1) {
				e1.printStackTrace();
			}
			switch (control) {
				case PREVIOUS:
					mediaId -= 1;
					if (mediaId < 0)
						mediaId = size-1;
					break;
				case NEXT:
					mediaId += 1;
					if (mediaId >= size)
						mediaId = 0;
					break;
				default:
					break;
			}
			
			// TODO Check if the prev/next media exists ?
			this.currentMedia = this.library.getMedia(mediaId);
		}
		else{
			Random rdm = new Random();
			this.currentMedia = this.library.getMedia(rdm.nextInt(size));
		}
		this.playMedia();
	}
	
	/**
	 * Plays the current media
	 */
	public void playMedia(){
		try {
			this.player = new AdvancedPlayer(this.currentMedia.getStream());
			this.player.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		} catch(NullPointerException e){
			System.out.println("No media to play ! (the source does not exists)");
		}
	}
	
}