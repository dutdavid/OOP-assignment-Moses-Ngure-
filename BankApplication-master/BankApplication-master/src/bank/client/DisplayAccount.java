/**
 * @title Assignment 2
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 9, 2017
 * 
 * DisplayAccount is used to display a single account as a pop-up window.
 * This is only used when deleting an Account
 * 
 */

package bank.client;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import bank.common.Account;
import javafx.geometry.*;

public class DisplayAccount {
	public static void display(String title, String message, Account tmp) {
		Stage window = new Stage();
		
		// Block events to other windows and create new window
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setWidth(300);
		window.setHeight(220);
		
		// Sets labels to value passed into the function
		Label label = new Label(message);
		Label output = new Label(tmp.toString());
		
		// Declare and Implement 'OK' Button 
		Button button = new Button("OK");
		
		button.setOnAction(e -> {
			window.close();
		});
		
		// Create Layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, output, button);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: #F2F1F0");
		
		// Add Layout to Scene and display the scene to a new window
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}