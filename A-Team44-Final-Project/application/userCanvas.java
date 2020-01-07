/**
 * Project Name: cs400-Final A-Team Project
 * Filename: userCanvas.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: 
 */
package application;

import java.io.IOException;
import java.util.List;
import java.util.*;
import java.math.*;


import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.shape.Circle;

/**
 * Main constructor of the userCanvas
 * 
 * @author Jordan Wilkins & Safwat Rahmen
 *
 */
public class userCanvas {
	private Set<Person> friends;
	private List<Person> friendsList;
	private Canvas networkCanvas;
	private static Main main;
	private int drawAmount = 0;
	private double angle = 0; // default if they have no friends
	private static final int RADIUS = 10;
	private static final double EDGE_LENGTH = 170;

	public userCanvas(Canvas networkCanvas, Main main) {
		this.networkCanvas = networkCanvas;
		this.main = main;
	}

	/**
	 * Call this method to draw the canvas once it has been initialized or update it
	 * if new friends are added
	 * 
	 * @author Safwat Rahmen, Jordan Wilkins, & Eric Sterwald
	 * @param centralUser The current central user's name
	 * @param network     The social network driving the program
	 */
	public void draw(String centralUser, SocialNetwork network, Pane overlay) {
		// initialize GraphicsContext and reset the canvas by drawing over it
		GraphicsContext gc = networkCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, networkCanvas.getWidth(), networkCanvas.getHeight());
		overlay.getChildren().removeAll();

		// Base Case: simple error message in the canvas for non-existant central user
		if (centralUser == null) {
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 200, 0, 200);
			gc.fillText("There is no central user currently set to display.", 100, 50);
			gc.setStroke(Color.WHITE);
			return;
		}
		friends = network.getFriends(centralUser);
		friendsList = new ArrayList<Person>();
		friendsList.addAll(friends);

		final int centralX = 300 - RADIUS;
		final int centralY = 200 - RADIUS;

		// calc the degree between peoples
		// double theta = 0;
		
		if (friends.size() > 50) { // if more than 50 friends of central user
			gc.setFill(Color.BLUE);
			gc.fillOval(centralX - 3 * RADIUS, centralY - 3 * RADIUS, 8 * RADIUS, 8 * RADIUS);
			gc.setFill(Color.WHITE);
			gc.fillText(centralUser, centralX-10, centralY + RADIUS);
			gc.setFill(Color.RED);
			gc.fillText("Network visualization supports a maximum of 40 friends. "
					+ "All friends are listed below.", 5, 395);
			// Warn the user about only 40 friends displaying
			Alert alert = new Alert(AlertType.WARNING);
			Text msg = new Text("Only 40 friends can be displayed. The rest of the friends are listed in the friends list.");
			msg.setWrappingWidth(300);
			alert.getDialogPane().setContent(msg);
			alert.showAndWait().filter(response -> response == ButtonType.CLOSE);
			angle = 9.0f; // 360 / 40 = 9  Set angle for 40 friends if they have more because we do not draw more
			drawAmount = 40; // If they have more than 40 friends only allow 40 to be drawn
		} else if (friends.size() == 0){
			angle = 0;
			drawAmount = 0;
		} else {
			drawAmount = network.getFriends(centralUser).size(); // Less than 40 friends, draw all of them
			angle = 360 / (network.getFriends(centralUser).size()); // Set angle to proper number
		}
		
		// loop through the amount of users it should draw. Value is set above.
		MouseClicker mc = new MouseClicker();
		for (int i = 0; i < drawAmount; ++i) {
			// note we find theta in degress and do pi/180 to convert to radians for
			// Math.cos
			double theta = ((double) i) * angle * (Math.PI / 180);
			// using trig to find two sides of right triangle
			double x = EDGE_LENGTH * (Math.cos(theta));
			double y = EDGE_LENGTH * (Math.sin(theta));
			gc.setFill(Color.BLACK);

			// determining new spot for new friend node
			double newX = centralX + x;
			double newY = centralY - y;

			// drawing nodes and edges and names
			gc.setStroke(Color.BLUE);
			gc.setLineWidth(2);
			gc.strokeLine(centralX + RADIUS, centralY + RADIUS, newX + RADIUS, newY + RADIUS);
			Circle circle = new Circle(RADIUS);
			Text nameOfFriend = new Text(friendsList.get(i).getName()); // Name of friend
			StackPane friendBubble = new StackPane(); // Used to stack name ontop of bubble
			circle.setStroke(Color.LIGHTGRAY);
			circle.setFill(Color.LIGHTGRAY);
			circle.relocate(newX, newY);
			friendBubble.setId(friendsList.get(i).getName());
			nameOfFriend.setBoundsType(TextBoundsType.VISUAL);
			friendBubble.getChildren().addAll(circle, nameOfFriend);
			friendBubble.relocate(newX-4, newY - 4); // Set the location of the bubble to appear
			overlay.getChildren().add(friendBubble); // Add the friend bubble to the screen
			mc.makeClickable(friendBubble); // Make the friend clickable
		}
		gc.setFill(Color.BLUE);
		gc.fillOval(centralX - 3 * RADIUS, centralY - 3 * RADIUS, 8 * RADIUS, 8 * RADIUS);
		gc.setFill(Color.WHITE);
		gc.fillText(centralUser, centralX-10, centralY + RADIUS);
	}

	/**
	 * Private class to handle clicking stuff on the canvas
	 * 
	 * @author Saffy and Jordan
	 *
	 */
	private static class MouseClicker {

		/**
		 * method to call by other classes to make circle clickable
		 * 
		 * @param circle
		 */
		private void makeClickable(StackPane friendBubble) {
			friendBubble.setOnMousePressed(clickEventHandler);
		}
		
		EventHandler<MouseEvent> clickEventHandler = new EventHandler<MouseEvent>() {

		/**
		 * Called when mouse clicked on a circle. new central user set and display refreshed
		 */
		public void handle(MouseEvent mouse) {
			StackPane friendBubble = (StackPane) mouse.getSource();
			String currUser = friendBubble.getId(); // retrieves name attached to circle
			try {
				main.setAndDisplayGlobalUser(currUser);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}}; 
	}
}