/**
 * Package: controller_view
 * File: LoginCreateAccountPane.java
 * Author: Jake Newton & Brenda Coutino
 * Date: Oct 19, 2023
 * 
 * This class manages user accounts, allowing account creation, login, and logout.
 */

package controller_view;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import model.AccountCollection;
import model.JukeboxAccount;

public class LoginCreateAccountPane extends GridPane {

	protected AccountCollection accounts = new AccountCollection();

	private Label accountLabel = new Label("Account Name");
	private TextArea accountBox = new TextArea("");
	private Label passwordLabel = new Label("Password");
	private PasswordField passwordBox = new PasswordField();
	private Label signupAccountLabel = new Label("New Account Name");
	private TextArea signupAccountBox = new TextArea("");
	private Label signupPassLabel = new Label("New Password");
	private PasswordField signupPassBox = new PasswordField();
	private Button loginButton = new Button("Login");
	private Button logoutButton = new Button("Logout");
	private Button createButton = new Button("Create Account");
	private Label loggedInAs = new Label("");
	private Label createResponse = new Label("");
	public JukeboxAccount userLoggedIn;

	/*
	 * Constructor which initializes user interface components and sets up event
	 * handling
	 */
	public LoginCreateAccountPane() {
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setHgap(10);
		this.setVgap(10);

		accountBox.setMaxSize(100, 10);
		passwordBox.setMaxSize(100, 10);
		signupAccountBox.setMaxSize(100, 10);
		signupPassBox.setMaxSize(100, 10);

		logoutButton.setDisable(true);

		loginButton.setOnAction(event -> {
			String userName = accountBox.getText();
			String password = passwordBox.getText();
			// Checks account exists
			if (accounts.contains(userName)) {
				// Checks password is correct
				if (accounts.login(userName, password)) {
					loggedInAs.setText("Logged in as " + userName);
					userLoggedIn = accounts.findAccount(userName);
					accountBox.setDisable(true);
					passwordBox.setDisable(true);
					loginButton.setDisable(true);
					logoutButton.setDisable(false);
				} else {
					accountAlert("Wrong Password");
				}
			} else {
				accountAlert("Account Does Not Exist");
			}
		});

		logoutButton.setOnAction(event -> {
			accounts.logout(accountBox.getText());
			passwordBox.setDisable(false);
			passwordBox.setText("");
			accountBox.setDisable(false);
			accountBox.setText("");
			loggedInAs.setText("");
			userLoggedIn = null;
			loginButton.setDisable(false);
			logoutButton.setDisable(true);
		});

		createButton.setOnAction(event -> {
			String signUpUser = signupAccountBox.getText();
			String signUpPass = signupPassBox.getText();
			// Checks if account name is taken
			if (accounts.contains(signUpUser)) {
				accountAlert("Username Taken");
			} else {
				accounts.createAccount(signUpUser, signUpPass);
				if (accounts.contains(signupAccountBox.getText())) {
					createResponse.setText("Account Created");
					signupAccountBox.setText("");
					signupPassBox.setText("");
				}
			}
		});

		this.add(accountLabel, 0, 0);
		this.add(accountBox, 1, 0);
		this.add(passwordLabel, 0, 1);
		this.add(passwordBox, 1, 1);
		this.add(signupAccountLabel, 2, 0);
		this.add(signupAccountBox, 3, 0);
		this.add(signupPassLabel, 2, 1);
		this.add(signupPassBox, 3, 1);
		this.add(loginButton, 1, 2);
		this.add(logoutButton, 2, 2);
		this.add(createButton, 3, 2);
		this.add(loggedInAs, 1, 4);
		this.add(createResponse, 3, 4);

	}

	// Creates an alert message with given message for invalid account action.
	private void accountAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Invalid Credentials");
		alert.setContentText(message);
		alert.showAndWait();

	}

	public JukeboxAccount userLoggedIn() {
		return userLoggedIn;
	}

}