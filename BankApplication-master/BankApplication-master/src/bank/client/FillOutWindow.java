package bank.client;

import java.rmi.RemoteException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bank.common.RemoteBank;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FillOutWindow {
	
	// Validate Full Name
	private static boolean validateFullName(String fullName) {
		Pattern p = Pattern.compile("[A-Z][a-zA-Z -]*[,][ ][A-Z][a-zA-Z -]+");
		Matcher m = p.matcher(fullName);
		
		if(m.find() && m.group().equals(fullName)) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Full Name");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a valid Full Name!");
			alert.showAndWait();
			
			return false;
		}
	}
	
	// Validate Email Address
	private static boolean validateEmail(String email) {
		Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(email);
		
		if(m.find() && m.group().equals(email)) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Email");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a valid Email Address!");
			alert.showAndWait();
			
			return false;
		}
	}
	
	// Validate Password
	private static boolean validatePassword(String pass1) {
		if(pass1.length() > 0) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Password");
			alert.setHeaderText(null);
			alert.setContentText("New Password Can't be empty!");
			alert.showAndWait();
			
			return false;
		}
	}
	
	public static void display(String title, String message, String type, RemoteBank bank) {
		Stage window = new Stage();
		
		// Block events to other windows and create new window
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setWidth(350);
		window.setHeight(150);
		
		// Sets label to value passed into the function
		Label label = new Label(message);
		
		// Declare and Implement 'OK' Button 
		Button update = new Button("Update");
		Button abort = new Button("Abort");
		
		// Declare input field
		// Creating Input Fields
		TextField textInput = new TextField();
		PasswordField passInput = new PasswordField();
		
		update.setOnAction(e -> {
			if(type.compareTo("fullName") == 0) {
				try {
					if(validateFullName(textInput.getText())) {
						if(bank.updateName(textInput.getText())) {
							AlertBox.display("Update Name", "Name Has Been Successfully Updated!");
							window.close();
						}
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			
			if(type.compareTo("email") == 0) {
				try {
					if(validateEmail(textInput.getText())) {
						if(bank.updateEmail(textInput.getText())) {
							AlertBox.display("Update Email", "Email Has Been Successfully Updated!");
							window.close();
						}
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			
			if(type.compareTo("password") == 0) {
				try {
					if(validatePassword(passInput.getText())) {
						if(bank.updatePassword(passInput.getText())) {
							AlertBox.display("Update Password", "Password Has Been Successfully Updated!");
							window.close();
						}
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		update.setDefaultButton(true);
		abort.setOnAction(e -> {
			window.close();
		});
		
		// Create Layout
		VBox layout = new VBox(10);
		HBox buttons = new HBox(20);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(update, abort);
		if(type.compareTo("password") == 0)
			layout.getChildren().addAll(label, passInput, buttons);
		else
			layout.getChildren().addAll(label, textInput, buttons);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: #F2F1F0");
		
		// Add Layout to Scene and display the scene to a new window
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
