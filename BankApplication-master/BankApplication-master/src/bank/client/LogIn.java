package bank.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bank.common.RemoteBank;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LogIn {
	
	//Validate Password
	private static boolean validatePass(String password) {
		if(password.length() > 0) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Password");
			alert.setHeaderText(null);
			alert.setContentText("Password field is Empty!");
			alert.showAndWait();
			
			return false;
		}
	}
	
	//Validate Email Address
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
	
	// displays the register window with a form users have to fill out if they wish to register an account
	public static void display(Stage window, RemoteBank bank,Scene oldHome) {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene logInScene;
		
		// Declaring Labels
		Label menuMessage = new Label("User Log In Form");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label email = new Label("Email Address: ");
		email.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label password = new Label("Password: ");
		password.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Creating Input Fields
		TextField emailInput = new TextField();
		PasswordField passInput = new PasswordField();
		
		// Declaring and Implementing Buttons
		Button mainMenu = new Button("Main Menu");
		mainMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button logIn = new Button("Log In");
		logIn.setOnAction(e -> {			
			if(validateEmail(emailInput.getText()) && validatePass(passInput.getText())) {
				try {
					boolean success = bank.login(emailInput.getText(), passInput.getText());
					if(success)
						MainMenu.display(window, bank, oldHome);
				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("User Login");
					alert.setHeaderText(null);
					alert.setContentText("Ops, invalid password and/or email address!");
					alert.showAndWait();
				}
			}
		});
		logIn.setDefaultButton(true);
		
		// Creating Initial Layout
		GridPane body = new GridPane();
		body.setAlignment(Pos.CENTER);
		body.setHgap(3);
		body.setVgap(2);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.add(email, 0, 0);
		GridPane.setHalignment(email, HPos.RIGHT);
		body.add(emailInput, 1, 0);
		GridPane.setHalignment(emailInput, HPos.LEFT);
		body.add(password, 0, 1);
		GridPane.setHalignment(password, HPos.RIGHT);
		body.add(passInput, 1, 1);
		GridPane.setHalignment(passInput, HPos.LEFT);
		body.add(logIn, 1, 2);
		GridPane.setHalignment(logIn, HPos.LEFT);
		
		BorderPane.setAlignment(mainMenu, Pos.CENTER_RIGHT);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(mainMenu, new Insets(12,12,12,12));
		BorderPane logInLayout = new BorderPane();
		logInLayout.setTop(menuMessage);
		logInLayout.setCenter(body);
		logInLayout.setBottom(mainMenu);
		logInLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		logInScene = new Scene(logInLayout, 520,386);
		window.setScene(logInScene);
	}
}
