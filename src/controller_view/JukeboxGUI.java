/**
 * Package: controller_view
 * File: JukeboxGUI.java
 * Author: Jake Newton & Brenda Coutino
 * Date: Oct 19, 2023
 * 
 * This class represents the GUI for the jukebox application.
 * It manages user accounts, song playlists, and allows for interaction
 * with song selection.
 */

package controller_view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.AccountCollection;
import model.PlayList;
import model.Song;

public class JukeboxGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	LoginCreateAccountPane loginPane;
	BorderPane everything;
	PlayList playlist;
	private ListView<Song> queueListView;
	private TableView<Song> listTableView;
	private Label songList = new Label("Song List");
	private Label songQueue = new Label("Song Queue");
	private Button playButton = new Button("Play");
	private Label nowPlaying = new Label("Log in to play songs");
	private ObservableList<Song> songsToPlay;

	@Override
	public void start(Stage primaryStage) throws Exception {
		LayoutGUI();
		saveSongs();
		Scene scene = new Scene(everything, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		if (!playlist.songsToPlay.isEmpty()) {
			playlist.start();
		}
		primaryStage.setOnCloseRequest(event -> {
			saveWindow();
		});
	}
	
	/*
	 * Creates alert message to save the current playlist and accounts
	 */
	private void saveWindow() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Click cancel to to not save any changes");
		alert.setContentText("To save the current state, click OK");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			playlist.stop();
			saveCurrentAccounts();
			savePlaylist();
		} else {
			// Do nothing
		}
	}
	
	
	/*
	 * Sets up main Layout GUI
	 */
	private void LayoutGUI() {
		everything = new BorderPane();
		everything.setPadding(new Insets(10, 10, 10, 10));
		loginPane = new LoginCreateAccountPane();
		GridPane bottom = new GridPane();
		HBox headers;
		
        openAlert(); // Open alert to choose between loading old state or starting fresh

		queueListView.setMaxHeight(400);
		queueListView.setMinWidth(300);
		listTableView.setMaxHeight(400);
		listTableView.setMinWidth(300);
		songList.setStyle("-fx-font-size: 20;");
		songQueue.setStyle("-fx-font-size: 20;");

		playButton.setOnAction(event -> {
			// Checks user is logged in 
			if (loginPane.userLoggedIn() != null) { 
				// Checks user can play songs
				if (loginPane.userLoggedIn().canSelect()) {
					// Takes selected song and adds to queue
					if (listTableView.getSelectionModel().getSelectedItem().getTitle() != null) {
						playlist.queueUpNextSong(listTableView.getSelectionModel().getSelectedItem().getTitle());
						loginPane.userLoggedIn.playedSong();
						nowPlaying.setText(
								"Now Playing " + listTableView.getSelectionModel().getSelectedItem().getTitle());
					}
				} else {
					nowPlaying.setText("Please wait until tomorrow to play more songs.");
				}
			} else {
				nowPlaying.setText("Please login to play a song.");
			}
		});

		bottom.add(nowPlaying, 0, 1);
		bottom.add(loginPane, 0, 2);
		headers = new HBox(400, songList, songQueue);

		TableColumn<Song, String> titles = new TableColumn<Song, String>("Title");
		titles.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
		TableColumn<Song, String> artists = new TableColumn<Song, String>("Artist");
		artists.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		TableColumn<Song, String> times = new TableColumn<Song, String>("Time");
		times.setCellValueFactory(new PropertyValueFactory<Song, String>("timeFormat"));
		titles.setMinWidth(150);
		artists.setMinWidth(100);
		times.setMinWidth(50);

		listTableView.getColumns().add(titles);
		listTableView.getColumns().add(artists);
		listTableView.getColumns().add(times);

		everything.setTop(headers);
		everything.setLeft(listTableView);
		everything.setCenter(playButton);
		everything.setRight(queueListView);
		everything.setBottom(bottom);
		bottom.setAlignment(Pos.BOTTOM_CENTER);

	}

	/*
	 * Creates an alert that shows before the main GUI to ask user if they would
	 * want to load previous accounts or start with a fresh collection.
	 */
	private void openAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Click cancel to start fresh.");
		alert.setContentText("Click OK to start from where you left off");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			loadAccounts();
			loadPlaylist();
		} else {
			newAccounts();
			newPlaylist();
		}
	}

	private void newAccounts() {
		loginPane.accounts = new AccountCollection();
	}

	/*
	 * Load .ser file and loads account collection with saved accounts
	 */
	private void loadAccounts() {
		try {
			FileInputStream rawBytes = new FileInputStream("oldLogins.ser");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			AccountCollection oldAccounts = (AccountCollection) inFile.readObject();
			inFile.close();
			loginPane.accounts = oldAccounts;
		} catch (IOException e) {
			System.out.println("Read object failed");
		} catch (ClassNotFoundException e) {
			System.out.println("Read object failed");
		}
	}

	/**
	 * Attempts to save the current list into memory as a persistent serial list
	 * named "oldLogins.ser".
	 */
	private void saveCurrentAccounts() {
		try {
			FileOutputStream bytesToDisk = new FileOutputStream("oldLogins.ser");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			outFile.writeObject(loginPane.accounts);
			outFile.close();
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	private void newPlaylist() {
		playlist = new PlayList();
		listTableView = new TableView<Song>();
		queueListView = new ListView<Song>(playlist.songsPlaying);
	}
	
	/*
	 * Saves the current playlist/queue
	 */
	private void savePlaylist() {
		try {
			FileOutputStream bytesToDisk = new FileOutputStream("oldPlaylistPlaying.ser");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			ArrayList<Song> saveList = new ArrayList<Song>();
			saveList.addAll(playlist.songsPlaying);
			outFile.writeObject(saveList);
			outFile.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	// loads the playlist from the .ser file
	private void loadPlaylist() {
		playlist = new PlayList();
		listTableView = new TableView<Song>();
		queueListView = new ListView<Song>(playlist.songsPlaying);
		try {
			FileInputStream rawBytes = new FileInputStream("oldPlaylistPlaying.ser");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			@SuppressWarnings("unchecked")
			ArrayList<Song> oldPlaylistPlaying = (ArrayList<Song>) inFile.readObject();
			inFile.close();
			playlist.songsPlaying.setAll(oldPlaylistPlaying);
			playlist.songsToPlay.addAll(oldPlaylistPlaying);
			playlist.start();

		} catch (IOException e) {
			System.out.println("Read object failed");
		} catch (ClassNotFoundException e) {
			System.out.println("Read object failed");
		}

	}

	/*
	 * This saves all the songs into an observable list for the table view
	 */
	private void saveSongs() {
		songsToPlay = FXCollections.observableArrayList();

		Song pokemon = new Song("Capture.mp3", "Pokemon Capture", "Pikachu", 5);
		songsToPlay.add(pokemon);
		Song danse = new Song("DanseMacabreViolinHook.mp3", "Danse Macabre", "Kevin MacLeod", 34);
		songsToPlay.add(danse);
		Song determined = new Song("DeterminedTumbao.mp3", "Determined Tumbao", "FreePlay Music", 20);
		songsToPlay.add(determined);
		Song longing = new Song("LongingInTheirHearts.mp3", "Longing In Their Hearts", "Bonnie Raitt", 289);
		songsToPlay.add(longing);
		Song loping = new Song("LopingSting.mp3", "LopingSting", "Kevin MacLeod", 5);
		songsToPlay.add(loping);
		Song swing = new Song("SwingCheese.mp3", "Swing Cheese", "FreePlay Music", 15);
		songsToPlay.add(swing);
		Song curtain = new Song("TheCurtainRises.mp3", "The Curtain Rises", "Kevin MacLeod", 28);
		songsToPlay.add(curtain);
		Song fire = new Song("UntameableFire.mp3", "UntameableFire", "Pierre Langer", 283);
		songsToPlay.add(fire);
		for (Song song : songsToPlay) {
			playlist.availableSongs.add(song);
			listTableView.getItems().add(song);
		}
	}

}