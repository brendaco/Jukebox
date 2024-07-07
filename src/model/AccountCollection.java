/**
 * Package: model
 * File: AccountCollection.java
 * Author: Jake Newton & Brenda Coutino
 * Date: Oct 19, 2023
 * 
 * This class manages a collection of JukeboxAccount objects.
 * Allows for creation of accounts, login, logout, and 
 * looking up accounts.
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountCollection implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<JukeboxAccount> accounts;

	public AccountCollection() {
		accounts = new ArrayList<>();
		this.accounts.add(new JukeboxAccount("Chris", "1"));
		this.accounts.add(new JukeboxAccount("Devon", "22"));
		this.accounts.add(new JukeboxAccount("River", "333"));
		this.accounts.add(new JukeboxAccount("Ryan", "4444"));
	}

	/*
	 * Method to create a new JukeboxAccount
	 */
	public void createAccount(String accountName, String password) {
		// Checks if account name exists already
		if (findAccount(accountName) == null) {
			JukeboxAccount newAccount = new JukeboxAccount(accountName);
			newAccount.setPassword(password);
			accounts.add(newAccount);
		}
	}

	/*
	 * Method to log user in by verifying account name and password
	 */
	public boolean login(String accountName, String password) {
		JukeboxAccount account = findAccount(accountName);
		if (account != null && account.login(accountName, password)) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Method to log user out by finding account name
	 */
	public void logout(String accountName) {
		JukeboxAccount account = findAccount(accountName);
		if (account != null) {
			account.logout();
		}

	}

	/*
	 * Finds and returns an account by its account name
	 */
	public JukeboxAccount findAccount(String accountName) {
		for (JukeboxAccount account : accounts) {
			if (account.getAccountName().equals(accountName)) {
				return account;
			}
		}
		return null;
	}

	/*
	 * Checks if the collection contains a JukeboxAccount matching the input
	 */
	public boolean contains(String accountName) {
		return accounts.contains(findAccount(accountName));
	}

}
