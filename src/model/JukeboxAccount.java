/**
 * Package: model
 * File: JukeboxAccount.java
 * Author: Jake Newton & Brenda Coutino
 * Date: Oct 19, 2023
 * 
 * This class represents a user account and allows functionality to log a user
 * in or out and keeping track of song selections. It also checks if user can 
 * play songs.
 */

package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

/*
 * User must log out and back in to refresh the date due to pretendItsTomorrow() functionality.
 */
public class JukeboxAccount implements Serializable {

	private static final long serialVersionUID = 7434661599275847621L;
	private LocalDate today;
	private String accountName;
	private String password;
	private boolean loggedIn;
	private HashMap<LocalDate, Integer> songsPlayed = new HashMap<LocalDate, Integer>();

	public JukeboxAccount(String name) {
		accountName = name; 
		password = "";
		loggedIn = false;
		today = LocalDate.now();
		songsPlayed.put(today, 0);
	}

	public JukeboxAccount(String name, String pass) {
		accountName = name;
		password = pass;
		loggedIn = false;
		today = LocalDate.now();
		songsPlayed.put(today, 0);
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public boolean getLoggedIn() {
		return this.loggedIn;
	}

	/*
	 * This method logs user in
	 */
	public boolean login(String accName, String pass) {
		today = LocalDate.now();
		if (accName.equals(this.accountName) && pass.equals(this.password)) {
			loggedIn = true;
		}
		return loggedIn;
	}

	public void logout() {
		loggedIn = false;
	}

	/*
	 * This method checks if the user can play a song by verifying an account us
	 * logged in and no more than three songs have been played in the current day.
	 */
	public boolean canPlaySong() {
		if (!songsPlayed.containsKey(today)) {
			songsPlayed.put(today, 0);
		}
		// Checks user is logged in and < 3 songs played
		if (songsPlayed.get(today) < 3 && loggedIn == true) {
			return true;
		} else {
			return false;
		}
	}

	// Records that the user has played a song
	public void playedSong() {
		int temp = songsPlayed.get(today);
		if (temp < 3) {
			songsPlayed.replace(today, temp + 1);
		}
	}

	public String toString() {
		String retVal = "";
		retVal += "Account Name: " + accountName + "\n";
		retVal += "Password: " + password + "\n";
		retVal += "Logged In: " + loggedIn + "\n";
		retVal += "Songs Played: " + songsPlayed.toString();
		return retVal;
	}

	/*
	 * Returns the number of songs played today
	 */
	public int songsSelectedToday() {
		if (!songsPlayed.containsKey(today)) {
			songsPlayed.put(today, 0);
		}
		return songsPlayed.get(today);
	}

	public void recordOneSelection() {
		this.playedSong();

	}

	/*
	 * Similar method to canPlaySong but user doesn't need to be logged in to
	 * select.
	 */
	public boolean canSelect() {
		if (!songsPlayed.containsKey(today)) {
			songsPlayed.put(today, 0);
		}
		if (songsPlayed.get(today) < 3) {
			return true;
		} else {
			return false;
		}
	}

	// Advances date to simulate the next day
	public void pretendItsTomorrow() {
		today = today.plusDays(1);
	}

}
