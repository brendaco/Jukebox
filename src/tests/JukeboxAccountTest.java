/**
 * Package: tests
 * File: JukeboxAccountTest.java
 * Author: Jake Newton & Brenda Coutino
 * Date: Oct 19, 2023
 * 
 * A test file for JukeboxAccount.java
 */

package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import model.JukeboxAccount;

class JukeboxAccountTest {

	@Test
	void testGetters() {
		JukeboxAccount aJBA = new JukeboxAccount("Name");
		aJBA.setPassword("hello");
		assertTrue(aJBA.login("Name", "hello"));
		assertEquals(aJBA.getAccountName(), "Name");
		assertEquals(aJBA.getPassword(), "hello");
		assertTrue(aJBA.getLoggedIn());
	}

	@Test
	void testAccount() {
		JukeboxAccount newAcc = new JukeboxAccount("Name");
		// Test login
		newAcc.setPassword("hello");
		assertTrue(newAcc.login("Name", "hello"));
		// Play 3 songs
		assertTrue(newAcc.canPlaySong());
		newAcc.playedSong();
		assertTrue(newAcc.canPlaySong());
		newAcc.playedSong();
		assertTrue(newAcc.canPlaySong());
		newAcc.playedSong();
		// Shouldn't be able to play a 4th
		assertFalse(newAcc.canPlaySong());
		// Pretend it's the following day
		newAcc.pretendItsTomorrow();
		// Songs should be playable again
		assertTrue(newAcc.canSelect());
		assertTrue(newAcc.canPlaySong());
		newAcc.playedSong();
		assertTrue(newAcc.canSelect());
		assertTrue(newAcc.canPlaySong());
		newAcc.pretendItsTomorrow();
		assertTrue(newAcc.canPlaySong());
		assertTrue(newAcc.canSelect());
		// Test logout
		newAcc.logout();
		assertFalse(newAcc.getLoggedIn());

	}

	@Test
	void testChangeOfDateWithAFewTimes() {
		JukeboxAccount user = new JukeboxAccount("Casey", "1111");
		// Test songs played in the day
		assertEquals(0, user.songsSelectedToday());
		user.recordOneSelection();
		assertEquals(1, user.songsSelectedToday());
		user.recordOneSelection();
		assertTrue(user.canSelect());
		user.recordOneSelection();
		assertEquals(3, user.songsSelectedToday());
		assertFalse(user.canSelect());
		// Test next day
		user.pretendItsTomorrow(); // Uses a LocalDate instance variable
		System.out.println(user.toString());
		assertEquals(0, user.songsSelectedToday());
		user.recordOneSelection();
		user.recordOneSelection();
		assertTrue(user.canSelect());
		user.recordOneSelection();
		assertEquals(3, user.songsSelectedToday());
		assertFalse(user.canSelect());
	}
}
