/**
 * Package: model
 * File: PlayList.java
 * Author: Jake Newton & Brenda Coutino
 * Date: Oct 19, 2023
 * 
 * This file implements Playlist and provides functionality 
 * for queuing up and playing songs.
 */

package model;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlayList {
	public LinkedBlockingQueue<Song> songsToPlay; // Queue of songs to play
	public ObservableList<Song> songsPlaying;
	public ArrayList<Song> availableSongs; 
	protected boolean isPlaying;
	Media media;
	MediaPlayer mediaPlayer;

	public PlayList() {
		songsToPlay = new LinkedBlockingQueue<Song>();
		availableSongs = new ArrayList<Song>();
		songsPlaying = FXCollections.observableArrayList();
	}

	/*
	 * Method to queue a song to the playlist.
	 */
	public void queueUpNextSong(String songToAdd) {
		// Looks for song
		for (Song search : availableSongs) {
			if (search.getTitle() != null && search.getTitle().equals(songToAdd)) {
				songsToPlay.add(search);
				songsPlaying.add(search);
				if (!isPlaying) {
					this.start();
				}
			}
		}
	}

	/*
	 * This method starts playing the next song in the queue
	 */
	public void start() {
		Waiter waiter = new Waiter();
		Song nextSong = songsToPlay.poll();
		if (nextSong != null) {
			media = new Media(nextSong.songURI.toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			System.out.println(nextSong.getTitle() + " is playing");
			isPlaying = true;
			// Song is over
			mediaPlayer.setOnEndOfMedia(() -> {
				System.out.println("Song finished playing");
				songsPlaying.remove(0);
				// Calls waiter to start 2 second pause
				waiter.run();
				// Plays next song in queue
				if (songsToPlay != null) {
					this.start();
				} else {
					isPlaying = false;
				}
			});
		}
	}

	public void stop() {
		mediaPlayer.stop();
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public LinkedBlockingQueue<Song> getPlayList() {
		return this.songsToPlay;
	}

	/*
	 * Implements a 2 second pause after a song is played
	 */
	private class Waiter implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("After 2 second pause\n");
			mediaPlayer.stop();
		}
	}

}