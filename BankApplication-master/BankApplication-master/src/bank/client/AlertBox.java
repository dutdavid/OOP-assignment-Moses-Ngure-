/**
 * @title Assignment 2
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 9, 2017
 * 
 * AlertBox is a class that implements the GUI
 * for a pop-up window. This window is used to alert
 * the user in different scenarios this is why the function accepts
 * two arguments to set the title of the window and the content of the
 * window accordingly to the scenario needed.
 * 
 */

package bank.client;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {
	public static void display(String title, String message) {
		Stage window = new Stage();
		
		// Block events to other windows and create new window
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setWidth(300);
		window.setHeight(150);
		
		// Sets label to value passed into the function
		Label label = new Label(message);
		
		// Declare and Implement 'OK' Button 
		Button button = new Button("OK");
		
		button.setOnAction(e -> {
			window.close();
		});
		
		// Create Layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, button);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: #F2F1F0");
		
		// Add Layout to Scene and display the scene to a new window
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
