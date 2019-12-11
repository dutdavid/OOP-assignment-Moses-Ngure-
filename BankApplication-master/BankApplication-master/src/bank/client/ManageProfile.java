package bank.client;

import bank.common.RemoteBank;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageProfile {
	
	public static void display(Stage window, RemoteBank bank) {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene manageProfileScene;
		
		// Declaring Labels
		Label menuMessage = new Label("Manage Your Account");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		
		// Declaring and Implementing Buttons
		Button homeMenu = new Button("Home Menu");
		homeMenu.setOnAction(e -> {
			window.setScene(home);
		});
		homeMenu.setDefaultButton(true);
		Button changeName = new Button("Change Full Name");
		changeName.setOnAction(e -> {			
			FillOutWindow.display("Update Full Name", "Enter New Name", "fullName", bank);
		});
		Button changeEmail = new Button("Change Email");
		changeEmail.setOnAction(e -> {			
			FillOutWindow.display("Update Emai", "Enter New Email", "email", bank);
		});
		Button changePass = new Button("Change Password");
		changePass.setOnAction(e -> {			
			FillOutWindow.display("Update Password", "Enter New Password", "password", bank);
		});
		
		// Creating Initial Layout
		VBox body = new VBox(20);
		body.setAlignment(Pos.CENTER);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.getChildren().addAll(changeName, changeEmail, changePass);
		
		BorderPane.setAlignment(homeMenu, Pos.CENTER_RIGHT);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(homeMenu, new Insets(12,75,12,75));
		BorderPane manageProfileLayout = new BorderPane();
		manageProfileLayout.setTop(menuMessage);
		manageProfileLayout.setCenter(body);
		manageProfileLayout.setBottom(homeMenu);
		manageProfileLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		manageProfileScene = new Scene(manageProfileLayout, 520,386);
		window.setScene(manageProfileScene);
	}
}
