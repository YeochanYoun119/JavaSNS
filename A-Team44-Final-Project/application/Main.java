/**
 * Project Name: cs400-Final A-Team Project
 * Filename: Main.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: The main class serves as the GUI that is displayed. It has all the 
 * code for creating the GUI and displaying it except the canvas. The userCanvas is 
 * called for drawing that. This class interacts with the socialNetwork during button
 * presses so that it can build the graph of people.
 */
package application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {
	private Stage globalStage = new Stage();
	private Scene globalScene = null;
	private String globalCentralUser = null;

	private static final int WINDOW_WIDTH = 1200;
	private static final int WINDOW_HEIGHT = 700;
	private static final String APP_TITLE = "Social Network Visualization";
	public SocialNetwork socialNetwork = new SocialNetwork();

	/**
	 * Starts the program
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Call method that displays the stage
		Display(primaryStage, null, null);
	}

	/**
	 * Program main
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Call only by the canvas eventHandler for the mouse
	 * @author Safwat Rahmen, Jordan Wilkins
	 * @param g -- new central user 
	 * @throws IOException 
	 */
	public void setAndDisplayGlobalUser(String g) throws IOException {
		globalCentralUser = g;
		// write to log file 
		socialNetwork.WriteLogFileCUser(g);
		// refresh screen 
		Display(this.globalStage, "Successfully set " + g + " as central user.", "-fx-text-fill: green;");
	}
	
	/**
	 * This method sets up the various GUI elements on the left hand
	 * side of the screen. Each section of the GUI is labeled in the 
	 * method by a comment block and further described with in-line 
	 * comments.
	 * @author Eric Sterwald
	 * @return
	 */
	private VBox setUpLeftBox() {
		VBox vleft = new VBox();
		vleft.setSpacing(15);

		/**
		 * Creating the code for the load file box
		 */
		// Sets up boxes for formating file section of GUI
		VBox fileBox = new VBox();
		HBox fileBoxInput = new HBox();
		fileBox.setSpacing(5);
		fileBoxInput.setSpacing(5);
		
		Button loadFileBtn = new Button("Load File");
		Label success = new Label();
		success.setVisible(false);
		// Launches a file chooser menu. If the file chose is null or there is a
		// problem when loading file, an error message pops up and status message
		// is changed
		loadFileBtn.setOnAction(e -> {
			 FileChooser exportFileChooser = new FileChooser();
			 exportFileChooser.setTitle("Select File to Load into Network");
			 exportFileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
			 try {
				File selectedFile = exportFileChooser.showOpenDialog(globalStage); 
				if (selectedFile != null) {
					globalCentralUser = socialNetwork.loadFromFile(selectedFile);
				}
				Display(this.globalStage, selectedFile.getName() + " loaded successfully!", "-fx-text-fill: green;");
			} catch (FileFormatException ffe) {
				// File is not in correct format
				Display(this.globalStage, "File failed to process.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "File format is incorrect. Please check file");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} catch (IOException io) {
				// Error when reading file
				Display(this.globalStage, "Failed to write log.txt... Contact admin.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Failed to write log file. Please contact administrator");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} catch (Exception x) {
				// File chooser was canceled, no file selected
				Display(this.globalStage, "No file was selected", null);
			}
		});
		fileBoxInput.getChildren().add(loadFileBtn);
		fileBox.getChildren().add(new Label("Select file to load into network.\nNOTE: If addition or removal of user " 
				+ "or friendship does not work, \nfile will continue processing."));
		fileBox.getChildren().add(fileBoxInput);

		/**
		 * Creating the code for the add user section
		 */
		// Sets up boxes for formating user interaction section of GUI
		VBox editUser = new VBox();
		HBox editUserInput = new HBox();
		editUser.setSpacing(5);
		editUserInput.setSpacing(5);
		
		TextField username = new TextField();
		username.setPromptText("Enter Username");
		editUserInput.getChildren().add(username);
		Button addUser = new Button("Add User");
		Button removeUser = new Button("Remove User");
		// Display error when user cannot be added or when usernames are blank
		addUser.setOnAction(e -> {
			if (username.getText().isBlank() || username.getText() == null) {
				// Usernames are blank
				Display(this.globalStage, "Failed to add user to social network.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Username cannot be blank");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else
				try {
					if (!socialNetwork.addUser(username.getText())) {
						// failed to add user
						Display(this.globalStage, "Failed to add user to social network.", "-fx-text-fill: red;");
						Alert alert = new Alert(AlertType.ERROR, "User already exists");
						alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
					} else Display(this.globalStage, username.getText() + 
							" successfully added to network.", "-fx-text-fill: green;");
				} catch (IOException e1) {
					// Failed to write log file
					Display(this.globalStage, "Failed to write log.txt... Contact admin.", "-fx-text-fill: red;");
					Alert alert = new Alert(AlertType.ERROR, "Failed to write log file. Please contact administrator");
					alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
				}
		});
		// Display error when user cannot be removed or when usernames are blank
		removeUser.setOnAction(e -> {
			if (username.getText().isBlank()) {
				// Usernames are blank
				Display(this.globalStage, "Failed to remove user to social network.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Username cannot be blank");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else
				try {
					if (!socialNetwork.removeUser(username.getText())) {
						// failed to add user
						Display(this.globalStage, "Failed to add user to social network.", "-fx-text-fill: red;");
						Alert alert = new Alert(AlertType.ERROR, "User does not exist");
						alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
					} else Display(this.globalStage, username.getText() + 
							" successfully removed from network.", "-fx-text-fill: green;");
				} catch (IOException e1) {
					// Failed to write log file
					Display(this.globalStage, "Failed to write log.txt... Contact admin.", "-fx-text-fill: red;");
					Alert alert = new Alert(AlertType.ERROR, "Failed to write log file. Please contact administrator");
					alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
				}
		}); 
		editUserInput.getChildren().add(addUser);
		editUserInput.getChildren().add(removeUser);
		Label editUserLabel = new Label("Enter username and select 'Add User' to add the"
				+ " social network\nor 'Remove User' to remove the user from the social network.");
		editUserLabel.setWrapText(true);
		editUser.getChildren().add(editUserLabel);
		editUser.getChildren().add(editUserInput);

		/**
		 * Creating the code for the user connection section
		 */
		// Sets up boxes for formating user connection section of GUI
		VBox editConnection = new VBox();
		HBox editConnectionUser = new HBox();
		HBox editConnectionButtons = new HBox();
		editConnection.setSpacing(5);
		editConnectionUser.setSpacing(5);
		editConnectionButtons.setSpacing(5);
		
		TextField firstUser = new TextField();
		TextField secondUser = new TextField();
		firstUser.setPromptText("Enter First Username");
		secondUser.setPromptText("Enter Second Username");
		editConnectionUser.getChildren().add(firstUser);
		editConnectionUser.getChildren().add(secondUser);
		Label editUserConnectionLabel = new Label("Enter username of two users and select 'Add Friendship'"
				+ "to create a friendship\nbetween the two users or 'Remove Friendship' to remove the friendship "
				+ "\nbetween the two users.");
		editUserConnectionLabel.setWrapText(true);
		editConnection.getChildren().add(editUserConnectionLabel);
		editConnection.getChildren().add(editConnectionUser);
		Button addFriendship = new Button("Add Friendship");
		Button removeFriendship = new Button("Remove Friendship");
		// Display error if usernames are blank or if friendship could not be added
		addFriendship.setOnAction(e -> {
			if (firstUser.getText().isBlank() || secondUser.getText().isBlank() || firstUser.getText() == null
					|| secondUser.getText() == null) {
				// Usernames are blank
				Display(this.globalStage, "Failed to add friendship between users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Usernames cannot be blank or null");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (firstUser.getText().equals(secondUser.getText())) {
				// usernames are same, cannot add friendship
				Display(this.globalStage, "Failed to add friendship between users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Usernames cannot be the same");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else
				try {
					if (!socialNetwork.addFriends(firstUser.getText(), secondUser.getText())) {
						// Frienship already exists
						Display(this.globalStage, "Failed to add friendship between users.", "-fx-text-fill: red;");
						Alert alert = new Alert(AlertType.ERROR, "Friendship already exists");
						alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
					} else Display(this.globalStage, "Successfully added friendship between " + firstUser.getText() +
							" and " + secondUser.getText() + ".", "-fx-text-fill: green;");
				} catch (IOException e1) {
					// failed to write to log file
					Display(this.globalStage, "Failed to write log.txt... Contact admin.", "-fx-text-fill: red;");
					Alert alert = new Alert(AlertType.ERROR, "Failed to write log file. Please contact administrator");
					alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
				}
		});
		
		// Display error if usernames are blank or if friendship could not be removed
		removeFriendship.setOnAction(e -> {
			if (firstUser.getText().isBlank() || secondUser.getText().isBlank() || firstUser.getText() == null
					|| secondUser.getText() == null) {
				// Usernames are blank
				Display(this.globalStage, "Failed to remove friendship between users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Usernames cannot be blank");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (firstUser.getText().equals(secondUser.getText())) {
				// Usernames are the same
				Display(this.globalStage, "Failed to remove friendship between users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Usernames cannot be the same");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else
				try {
					if (!socialNetwork.removeFriends(firstUser.getText(), secondUser.getText())) {
						// friendship doesnt exist, cannot remove
						Display(this.globalStage, "Failed to remove friendship between users.", "-fx-text-fill: red;");
						Alert alert = new Alert(AlertType.ERROR, "Friendship does not exist");
						alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
					} else Display(this.globalStage, "Successfully removed friendship between " + firstUser.getText() +
							" and " + secondUser.getText() + ".", "-fx-text-fill: green;");
				} catch (IOException e1) {
					// failed to write log file
					Display(this.globalStage, "Failed to write log.txt... Contact admin.", "-fx-text-fill: red;");
					Alert alert = new Alert(AlertType.ERROR, "Failed to write log file. Please contact administrator");
					alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
				}
		});
		editConnectionButtons.getChildren().add(addFriendship);
		editConnectionButtons.getChildren().add(removeFriendship);
		editConnection.getChildren().add(editConnectionButtons);

		/**
		 * Create the code for selecting central user
		 */
		// Sets up boxes for formating central user selection section of GUI
		VBox cUserSelectionV = new VBox();
		HBox cUserSelectionH = new HBox();
		cUserSelectionV.setSpacing(5);
		cUserSelectionH.setSpacing(5);
		
		TextField newCUser = new TextField();
		newCUser.setPromptText("Enter Username");
		Button updateCUser = new Button("Set Central User");
		updateCUser.setOnAction(e -> {
			if (newCUser.getText() == null || newCUser.getText().isBlank()) {
				// Username is blank
				Display(this.globalStage, "Failed to change central user.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Username cannot be blank or null");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (!socialNetwork.exists(newCUser.getText())) {
				// User does not exist in network
				Display(this.globalStage, "Failed to change central user.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, newCUser.getText() + " does not exist in social network");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else {
				// changes global central user
				globalCentralUser = newCUser.getText();
				try {
					// Trys to write change to log file
					socialNetwork.WriteLogFileCUser(newCUser.getText());
					Display(this.globalStage, "Successfully set " + newCUser.getText() + " as central user", 
							"-fx-text-fill: green;");
				} catch (Exception e1) {
					// failed to write to log file
					Display(this.globalStage, "Failed to write log.txt... Contact admin.", "-fx-text-fill: red;");
					Alert alert = new Alert(AlertType.ERROR, "Failed to write log file. Please contact administrator");
					alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
				}
				
			}
		});
		cUserSelectionH.getChildren().addAll(newCUser, updateCUser);
		Label cUser = new Label("Enter a username to select as current user of social network.");
		cUserSelectionV.getChildren().addAll(cUser, cUserSelectionH);

		/**
		 * Create the code to view mutual friends between two users
		 */
		// Sets up boxes for formating mutual friend section of GUI
		VBox mutualFriendV = new VBox();
		HBox mutualFriendInterface = new HBox();
		mutualFriendV.setSpacing(5);
		mutualFriendInterface.setSpacing(5);
		
		TextField user1 = new TextField();
		TextField user2 = new TextField();
		user1.setPromptText("Enter First Username");
		user2.setPromptText("Enter Second Username");
		Label mutualFriendsLabel = new Label("Enter two users to view the mutural friends between the users.");
		Button viewMutualFriends = new Button("View Mutual Friends");
		// Pops up window with list of mutual friends or prints a fail message
		viewMutualFriends.setOnAction(m -> {
			if (user1.getText().isBlank() || user2.getText().isBlank()) {
				// Usernames are blank
				Display(this.globalStage, "Failed to display mutual friends of users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Name cannot be blank");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (!socialNetwork.exists(user1.getText())) {
				// User1 does not exist in network
				Display(this.globalStage, "Failed to display mutual friends of users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, user1.getText() + " does not exist in network");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (!socialNetwork.exists(user2.getText())) {
				// User2 does not exist in network
				Display(this.globalStage, "Failed to display mutual friends of users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, user2.getText() + " does not exist in network");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (user1.getText().equals(user2.getText())) {
				// Usernames cannot be the same
				Display(this.globalStage, "Failed to display mutual friends of users.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Usernames cannot be the same name");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else {
				// Display the mutual friends and output success status
				Display(this.globalStage, "Successfully displayed mutual friends between " 
						+ user1.getText() + " and " + user2.getText() + ".", "-fx-text-fill: green;");
				DisplayMutualFriends(user1.getText(), user2.getText());
			}
		}); 
		mutualFriendInterface.getChildren().addAll(user1, user2, viewMutualFriends);
		mutualFriendV.getChildren().addAll(mutualFriendsLabel, mutualFriendInterface);

		/**
		 * Create the code to view shortest path between two users
		 */
		// Sets up boxes for formating shortest path section of GUI
		VBox shortestPathV = new VBox();
		HBox shortestPathInterface = new HBox();
		shortestPathV.setSpacing(5);
		shortestPathInterface.setSpacing(5);
		
		TextField u1 = new TextField();
		TextField u2 = new TextField();
		u1.setPromptText("Enter First Username");
		u2.setPromptText("Enter Second Username");
		Label shortestPathsLabel = new Label(
				"Enter two users to view the shortest friendship path." + " between the two users");
		Button viewshortestPaths = new Button("View Path");
		// Display new window with shortest path between users or error explanation
		viewshortestPaths.setOnAction(e -> {
			if (u1.getText().isBlank() || u2.getText().isBlank()) {
				// Usernames cannot be blank
				Display(this.globalStage, "Failed to display shortest path.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Usernames cannot be blank");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (u1.getText().equals(u2.getText())) {
				// Usernames cannot be the same
				Display(this.globalStage, "Failed to display shortest path.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, "Usernames cannot be the same");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (!this.socialNetwork.exists(u1.getText())) {
				Display(this.globalStage, "Failed to display shortest path.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, u1.getText() + " does not exist");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else if (!this.socialNetwork.exists(u2.getText())) {
				Display(this.globalStage, "Failed to display shortest path.", "-fx-text-fill: red;");
				Alert alert = new Alert(AlertType.ERROR, u2.getText() + " does not exist");
				alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			} else {
				List<Person> path = socialNetwork.getShortestPath(u1.getText(), u2.getText());
				if (path == null) { // 
					Display(this.globalStage, "Failed to display shortest path.", "-fx-text-fill: red;");
					Alert alert = new Alert(AlertType.ERROR, "There is no path between the users entered");
					alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
				} else {
					// Generate message to display
					Display(this.globalStage, "Shortest path successfully displayed.", "-fx-text-fill: green;");
					String message = "";
					for (int i = 0; i < path.size(); i++) {
						message += path.get(i).getName();
						if (!(i + 1 == path.size())) {
							message += " -> ";
						}
					}
					// Display the pop up
					DisplayShortestPath(message, u1.getText(), u2.getText());
				}
			}
		});
		shortestPathInterface.getChildren().addAll(u1, u2, viewshortestPaths);
		shortestPathV.getChildren().addAll(shortestPathsLabel, shortestPathInterface);

		/**
		 * Code to display the statistics of the social network
		 */
		// Sets up boxes for formating the network statistics section of GUI
		VBox snStats = new VBox();
		snStats.setSpacing(5);
		
		Label desc = new Label("Social Network Statistics");
		desc.setStyle("-fx-font-weight: bold");
		Label totalUsers = new Label("Number of users in social network: " +  this.socialNetwork.getSize());
		int numGroups = 0;
		if (socialNetwork.getConnectedComponents() != null || !socialNetwork.getConnectedComponents().isEmpty()) {
			numGroups = socialNetwork.getConnectedComponents().size(); // Get number of groups
		}
		Label groups = new Label("Number of groups in the social network: " + numGroups);
		Label totalFriendships = new Label("Number of unique friendships in social network: " 
				+ this.socialNetwork.getFriendshipSize());
		snStats.getChildren().addAll(desc, totalUsers, groups, totalFriendships);
		
		/**
		 * All all parts of left side to vertical box and return VBox
		 */
		vleft.getChildren().addAll(fileBox, editUser, editConnection, 
				cUserSelectionV, mutualFriendV, shortestPathV, snStats);
		return vleft;
	}

	/**
	 * Sets up the right vBox of the main program
	 * 
	 * @param currentCentralUser
	 * @return
	 */
	private VBox setUpRightBox() {
		VBox vright = new VBox();
		VBox network = new VBox(); 
		vright.setSpacing(5);
		network.setSpacing(5);
		
		// setting up canvas
		StackPane holder = new StackPane();
		Canvas networkCanvas = new Canvas(650, 400);
		Pane overlay = new Pane();
		holder.getChildren().addAll(networkCanvas, overlay);
 
		// adding canvas and label to network vbox
		BorderPane labelHolder = new BorderPane();
		VBox canvasDescription = new VBox();
		Label canvasLabel = new Label();
		Label visDesc = new Label("Click on a friend of " + globalCentralUser +
				" to set them as the central user.");
		visDesc.setVisible(false);
		if (globalCentralUser == null || globalCentralUser.isBlank()) {
			canvasLabel.setText("No user selected to display friends of!");
		} else {
			if (socialNetwork.getFriends(globalCentralUser).size() == 0) {
				visDesc.setVisible(false);
			} else visDesc.setVisible(true);
			canvasLabel.setText(globalCentralUser + "'s Friend Network");
		}
		canvasDescription.getChildren().addAll(canvasLabel, visDesc);
		canvasDescription.setAlignment(Pos.CENTER);
		labelHolder.setCenter(canvasDescription);
		network.getChildren().add(labelHolder);
		network.getChildren().add(holder);
		holder.setStyle("-fx-background-color: white");

		// adding network to vright vbox
		vright.getChildren().add(network);
		 
		// draw canvas 
		userCanvas displayCanvas = new userCanvas(networkCanvas,this); 
		displayCanvas.draw(globalCentralUser, socialNetwork, overlay);

		BorderPane container = new BorderPane();
		BorderPane containerRight = new BorderPane();
		BorderPane containerLeft = new BorderPane();
		VBox usersFriendList = new VBox(); // where current user name goes
		VBox allPeople = new VBox(); // will be put in userBox to accommodate current user friends and all users
		usersFriendList.setSpacing(2);
		allPeople.setSpacing(2);

		ListView<String> friendsList = new ListView<String>();
		ListView<String> allUsers = new ListView<String>();
		friendsList.setFocusTraversable(false);
		allUsers.setFocusTraversable(false);

		// Creating two lists to display central user's friends and all users
		Set<Person> friends = socialNetwork.getFriends(globalCentralUser); // central user friends
		Set<Person> all = socialNetwork.getAllUsers(); // all users
		
		// retrieving and adding all users/friends to list
		if (friends == null || friends.isEmpty()) {
			friendsList.getItems().add(globalCentralUser + " has no friends :(");
		} else {
			for (Person p : friends) {
				friendsList.getItems().add(p.getName());
			}
		} 
		if (all == null || all.isEmpty()) {
			allUsers.getItems().add("No users in social network");
		} else {
			for (Person p : all) {
				allUsers.getItems().add(p.getName());
			}
		}
		
		// Display label with describing listbox of friends
		Label cUserLbl = null;
		Label cUserNumFriends = new Label();
		if (globalCentralUser == null || globalCentralUser.isBlank()) {
			cUserLbl = new Label("No Central User Selected");
			cUserNumFriends.setVisible(false);
		} else {
			cUserLbl = new Label("List of friends of: " + globalCentralUser);
			cUserNumFriends.setText(globalCentralUser + " has " 
					+ socialNetwork.getFriends(globalCentralUser).size() + " friends.");
		}
		
		usersFriendList.getChildren().addAll(cUserLbl, cUserNumFriends, friendsList);
		
		// tag to describe what is in the list
		allPeople.getChildren().add(new Label("All users in social network:"));
		allPeople.getChildren().add(new Label()); //Adds a spacer to match list box next to it
		allPeople.getChildren().add(allUsers);
		containerLeft.setCenter(usersFriendList);
		containerRight.setCenter(allPeople);
		container.setCenter(containerLeft);
		container.setRight(containerRight);
		
		vright.getChildren().add(container);
		return vright;
	}

	/**
	 * Sets up the bottom box with buttons for controlling social
	 * network, exporting network and saving/exiting visualizer.
	 * @author Eric Sterwald
	 * @return
	 */
	private BorderPane setUpBottomBox() {
		BorderPane bottom = new BorderPane();

		// Button for clearing network
		Button clearButton = new Button("Clear Network");
		clearButton.setOnAction(e -> {
			try {
				socialNetwork.reset();
				Display(this.globalStage, "Successfully cleared social network.", "-fx-text-fill: green;");
			} catch (Exception e1) {
				Display(this.globalStage, "Failed to cleared social network.", "-fx-text-fill: red;");
			}
		});
		
		// Button for exporting network
		Button exportNetwork = new Button("Export Current Network");
		exportNetwork.setOnAction(e -> {
			if (ExportCurrentNetwork()) {
				DisplayFileMsg(socialNetwork.GetExportFile(), null);
			}
		});
		
		// Button for exiting network, generates pop up menu
		Button exitBtn = new Button("Exit Network Visualizer");
		exitBtn.setOnAction(e -> {
			GetSaveName();
		});

		bottom.setLeft(clearButton);
		bottom.setCenter(exportNetwork);
		bottom.setRight(exitBtn);
		
		return bottom;
	}

	/**
	 * Creates a pop up with the save options for the GUI
	 * @author Eric Sterwald
	 */
	private void GetSaveName() {
		// Setting up Stage, scene and boxes for the scene.
		Stage saveNamePopUp = new Stage();
		BorderPane root = new BorderPane();

		// Creating buttons for save options and adding to HBox
		Button saveCloseBtn = new Button("Save and Exit");
		Button exitBtn = new Button("Exit Without Saving");

		// Save and close button will prompt the user for a file to save the current
		// network to before exiting.
		saveCloseBtn.setOnAction(e -> {
			if (ExportCurrentNetwork()) {
				DisplayFileMsg(socialNetwork.GetExportFile(), socialNetwork.GetLogFile());
				saveNamePopUp.close();
				globalStage.close();
			} else saveNamePopUp.toFront();
		});

		// Exit without saving option. Gives a confirmation message to ensure the user 
		// selected the correction option.
		exitBtn.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit without saving?", 
					ButtonType.YES, ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES) {
				DisplayFileMsg(null, socialNetwork.GetLogFile());
				saveNamePopUp.close();
				globalStage.close();
			} else alert.close();
		});
		
		// Displaying scene
		Scene saveNameMain = new Scene(root, 300, 50);
		root.setLeft(saveCloseBtn);
		root.setRight(exitBtn);
		saveNamePopUp.setTitle("Exit Network Visualizer");
		saveNamePopUp.setScene(saveNameMain);
		saveNamePopUp.show();
	}

	/**
	 * Displays a popup menu with the mutual friends between
	 * two users listed out.
	 * @author Eric Sterwald
	 * @param name1
	 * @param name2
	 */
	private void DisplayMutualFriends(String name1, String name2) {
		// Create stage and boxes for scene
		Stage mutualFriendsPopUp = new Stage();
		BorderPane root = new BorderPane();
		VBox mfPopUp = new VBox();
		
		Label disc = new Label("List of mutual friends between " + name1 + " and " + name2);
		ListView<String> listOfMF = new ListView<String>();
		listOfMF.setFocusTraversable(false);
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> mutualFriendsPopUp.close());
		Set<Person> mutualFriends = socialNetwork.getMutualFriends(name1, name2);
		if (mutualFriends.isEmpty()) {
			// no mutual friends
			listOfMF.getItems().add("There are no mutual friends between these users");
		} else {
			// Adds all mutual friends to list
			for (Person p : mutualFriends) {
				listOfMF.getItems().add(p.getName());
			}
		}
		mfPopUp.getChildren().addAll(disc, listOfMF, closeButton);
		
		// Displaying scene
		Scene mutualFriendsMain = new Scene(root, 600, 400);
		root.setCenter(mfPopUp);
		mutualFriendsPopUp.setTitle("Mutual Friends");
		mutualFriendsPopUp.setScene(mutualFriendsMain);
		mutualFriendsPopUp.show();
	}

	/**
	 * Displays a pop up menu with the shortest path between
	 * 2 users.
	 * @author Eric Sterwald
	 * @param msg
	 * @param name1
	 * @param name2
	 */
	private void DisplayShortestPath(String msg, String name1, String name2) {
		// Sets up stage and boxes for scene
		Stage sPathPopUp = new Stage();
		BorderPane root = new BorderPane();
		VBox shortestPathV = new VBox();
	
		Label disc = new Label("The shortest path between " + name1 + " and " + name2 + " is...");
		TextArea sp = new TextArea(msg);
		sp.setEditable(false);
		sp.setFocusTraversable(false);
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> sPathPopUp.close());
		shortestPathV.getChildren().addAll(disc, sp, closeButton);
		
		// Displaying scene
		Scene shortestPathMain = new Scene(root, 600, 200);
		root.setCenter(shortestPathV);
		sPathPopUp.setTitle("Shortest Path");
		sPathPopUp.setScene(shortestPathMain);
		sPathPopUp.show();
	}
	
	/**
	 * Displays a file chooser to export the network to.
	 * Changes status method on success and fail.
	 * @author Eric Sterwald
	 * @return
	 */
	private boolean ExportCurrentNetwork() {
		 FileChooser exportFileChooser = new FileChooser();
		 exportFileChooser.setTitle("Select File to Export Current Network to");
		 exportFileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		 try {
			File selectedFile = exportFileChooser.showOpenDialog(globalStage); 
			socialNetwork.saveToFile(selectedFile);
			// Success exporting file
			Display(this.globalStage, "Succesfully exported current network to " 
					+ selectedFile.getName() + ".", "-fx-text-fill: green;");
		} catch (IOException e) {
			// Failed to export
			Display(this.globalStage, "Failed to export current network to file selected.", "-fx-text-fill: red;");
			return false;
		} catch (Exception x) {
			// no file selected 
			Display(this.globalStage, "No file was selected", null);
			return false;
		}
		return true;
	}
	
	private void DisplayFileMsg(File export, File log) {
		// Sets up stage and boxes for scene
		Stage fileMessagePopUp = new Stage();
		BorderPane root = new BorderPane();
		VBox fileMsgOutput = new VBox();
		VBox fileInfo = new VBox();
		VBox logInfo = new VBox();
		fileMsgOutput.setSpacing(15);
		fileInfo.setSpacing(2.5);
		logInfo.setSpacing(2.5);
	
		// Labels for the export file
		if (export != null) { // If this is a save operation display log file info
			Label desc = new Label();
			Label fileName = new Label();
			Label filePath = new Label();
			desc.setText("Your file was saved successfully!");
			desc.setStyle("-fx-font-weight: bold");
			fileName.setText("Name of saved file: " + export.getName());
			filePath.setText("Path to saved file: " + export.getAbsolutePath());
			fileInfo.getChildren().addAll(desc, fileName, filePath);
			fileMsgOutput.getChildren().add(fileInfo);
		}

		// Labels for the export file
		if (log != null) { // If this is a close operation display log file info
			Label logDesc = new Label();
			Label logName = new Label();
			Label logPath = new Label();
			logDesc.setText("A log file of interactions was created!");
			logDesc.setStyle("-fx-font-weight: bold");
			logName.setText("Name of log file: " + log.getName());
			logPath.setText("Path to log file: " + log.getAbsolutePath());
			logInfo.getChildren().addAll(logDesc, logName, logPath);
			fileMsgOutput.getChildren().add(logInfo);
		}
		
		// Close button
		Button closeButton = new Button("OK");
		closeButton.setOnAction(e -> fileMessagePopUp.close());
		
		// Displaying scene
		Scene fileMessageMain = new Scene(root, 600, 175);
		root.setCenter(fileMsgOutput);
		root.setBottom(closeButton);
		fileMessagePopUp.setTitle("File Path Information");
		fileMessagePopUp.setScene(fileMessageMain);
		fileMessagePopUp.showAndWait();
	}
	
	/**
	 * Used in user Canvas to get the main scene to allow the cursor to change
	 * @author Eric Sterwald
	 * @return
	 */
	public Scene getScene() {
		return this.globalScene;
	}
	
	/**
	 * Method that displays the main GUI window. It is called every time the
	 * GUI needs to update. This method also has the code to display the
	 * status messages passed into it.
	 * 
	 * @author Eric Sterwald
	 * @param primaryStage
	 * @param statusMsg
	 * @param statusMsgParameters
	 */
	private void Display(Stage primaryStage, String statusMsg, String statusMsgParameters) {
		this.globalStage = primaryStage;
		if (!socialNetwork.exists(globalCentralUser)) {
			globalCentralUser = null;
		}
		
		VBox leftVBox = new VBox();
		HBox statusOutput = new HBox();
		leftVBox.setSpacing(20);
		Label statusWord = new Label("Status: ");
		statusWord.setStyle("-fx-font-weight: bold");
		Label statusLbl = new Label();
		// Determine what the status message should say & color
		statusLbl.setVisible(false);
		if (statusMsg != null) {
			statusLbl.setVisible(true);
			statusLbl.setText(statusMsg);
			if(statusMsgParameters != null) {
				statusLbl.setStyle(statusMsgParameters);
			} else statusLbl.setStyle("-fx-text-fill: black;");
		} 
		statusOutput.getChildren().addAll(statusWord, statusLbl);
		leftVBox.getChildren().addAll(setUpLeftBox(), statusOutput);
		
		// Set up border pane
		BorderPane root = new BorderPane();
		root.setLeft(leftVBox);
		root.setRight(setUpRightBox());
		root.setBottom(setUpBottomBox());

		// Display scene
		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		globalScene = mainScene;
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
}