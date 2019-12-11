package bank.client;

import java.rmi.RemoteException;

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

public class ManageAccounts {
	public static void display(Stage window, RemoteBank bank) {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene manageAccountsScene;
		
		// Declaring Labels
		Label menuMessage = new Label("Manage Accounts");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		
		// Declaring and Implementing Buttons
		Button homeMenu = new Button("Home Menu");
		homeMenu.setOnAction(e -> {
			window.setScene(home);
		});
		homeMenu.setDefaultButton(true);
		Button openAccount = new Button("Open New Account");
		openAccount.setOnAction(e -> {			
			OpenAccountMenu.display(window, bank);
		});
		Button closeAccount = new Button("Close Account");
		closeAccount.setOnAction(e -> {			
			CloseAccountMenu.display(window, bank);
		});
		Button listAccounts = new Button("List Accounts");
		listAccounts.setOnAction(e -> {			
			try {
				DisplayAccounts.display(window, bank);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		});
		
		// Creating Initial Layout
		VBox body = new VBox(20);
		body.setAlignment(Pos.CENTER);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.getChildren().addAll(openAccount, closeAccount, listAccounts);
		
		BorderPane.setAlignment(homeMenu, Pos.CENTER_RIGHT);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(homeMenu, new Insets(12,75,12,75));
		BorderPane manageProfileLayout = new BorderPane();
		manageProfileLayout.setTop(menuMessage);
		manageProfileLayout.setCenter(body);
		manageProfileLayout.setBottom(homeMenu);
		manageProfileLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		manageAccountsScene = new Scene(manageProfileLayout, 520,386);
		window.setScene(manageAccountsScene);
	}
}
