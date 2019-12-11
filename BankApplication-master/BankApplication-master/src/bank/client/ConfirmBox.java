/**
 * @title Assignment 2
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 9, 2017
 * 
 * Similarly to AlertBox this pop-up window can be used in
 * different occasions. The difference is that the user has the ability to choose
 * 'yes' or 'no' to make a decision. Based on that decision the function display()
 * returns true or false. The returned value is used to make decisions in the main program.
 * 
 */

package bank.client;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ConfirmBox {
	
	// Return value
	static boolean answer;
	
	public static boolean display(String title, String message) {
		Stage window = new Stage();
		
		// Block events to other windows and create new window
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setWidth(300);
		window.setHeight(150);
		
		// Sets label to value passed into the function
		Label label = new Label(message);
		
		// Declare and Implement 'OK' Button 
		Button yesButton = new Button("Yes");
		Button noButton = new Button("No");
		
		yesButton.setOnAction(e -> {
			answer = true;
			window.close();
		});
		
		noButton.setOnAction(e -> {
			answer = false;
			window.close();
		});
		
		// Create Layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, yesButton, noButton);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: #F2F1F0");
		
		// Add Layout to Scene and display the scene to a new window
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}
}
