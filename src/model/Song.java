/**
 * Package: model
 * File: Song.java
 * Author: Jake Newton & Brenda Coutino
 * Date: Oct 9, 2023
 * 
 * Defines Song with the attributes title, artist
 * playtime, fileName, songURI, and timeFormat
 */
package model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

public class Song implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String artist;
	private int playtime;
	protected String fileName;
	protected URI songURI;
	private String timeFormat;

	public Song(String newSong, String title, String artist, int playTime) {
		// Reads the song files folder
		File songDir = new File("songfiles");
		File[] files = songDir.listFiles((dir1, name) -> name.endsWith(".mp3"));
		for (File file : files) {
			if (file.getName().equals(newSong)) {
				this.fileName = file.getName();
				this.title = title;
				this.artist = artist;
				this.playtime = playTime;
				this.songURI = file.toURI();
				;

				// Formats the play time to 0:00
				int min = playtime / 60;
				int sec = playtime % 60;
				this.timeFormat = String.format("%d:%02d", min, sec);
			}
		}
	}

	public String getTitle() {
		return this.title;
	}

	public String getArtist() {
		return this.artist;
	}

	public int getPlaytime() {
		return this.playtime;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getTimeFormat() {
		return this.timeFormat;
	}

	public String toString() {
		String retVal = "";
		retVal += "Title: " + title + "\n";
		retVal += "Artist: " + artist + "\n";
		retVal += "PlayTime: " + timeFormat + "\n";
		retVal += "File: " + fileName;
		return retVal;
	}

}