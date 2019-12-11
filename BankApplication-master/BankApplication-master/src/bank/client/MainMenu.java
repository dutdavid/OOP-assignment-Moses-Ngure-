package bank.client;

import java.rmi.RemoteException;

import bank.common.RemoteBank;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenu {
	
	public static void display(Stage window, RemoteBank bank, Scene oldHome) throws RemoteException {
		// Declaring Scenes
		Scene mainMenuScene;
		
		// Declaring Labels
		Label menuMessage = new Label("Welcome " + bank.getUserName() + "!");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		
		// Declaring and Implementing Buttons
		Button managaAccounts = new Button("Manage Accounts");
		managaAccounts.setOnAction(e -> {
			ManageAccounts.display(window, bank);
		});
		Button manageProfile = new Button("Manage Profile");
		manageProfile.setOnAction(e -> {
			ManageProfile.display(window, bank);
		});
		Button logOut = new Button("Log Out");
		logOut.setOnAction(e -> {
			try {
				bank.logOut();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			window.setScene(oldHome);
		});
		Button deposit = new Button("Deposit");
		deposit.setOnAction(e -> {			
			try {
				Deposit.display(window, bank);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		});
		Button withdraw = new Button("Withdraw");
		withdraw.setOnAction(e -> {			
			try {
				Withdraw.display(window, bank);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		});
		Button transfer = new Button("Transfer");
		transfer.setOnAction(e -> {			
			try {
				Transfer.display(window, bank);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		});
		Button taxStatement = new Button("Tax Statement");
		taxStatement.setOnAction(e -> {			
			try {
				TaxStatements.display(window, bank);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		});
		
		// Creating Initial Layout
		VBox body = new VBox(20);
		body.setAlignment(Pos.CENTER);
		body.setPadding(new Insets(0, 10, 0, 10));
		body.getChildren().addAll(deposit, withdraw, transfer, taxStatement);
		
		// Create bottom HBox
		HBox bottom = new HBox(20);
		bottom.getChildren().addAll(managaAccounts, manageProfile, logOut);
		
		BorderPane.setAlignment(bottom, Pos.CENTER);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(bottom, new Insets(12,75,12,75));
		BorderPane mainMenuLayout = new BorderPane();
		mainMenuLayout.setTop(menuMessage);
		mainMenuLayout.setCenter(body);
		mainMenuLayout.setBottom(bottom);
		mainMenuLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		mainMenuScene = new Scene(mainMenuLayout, 520,386);
		window.setScene(mainMenuScene);
	}
}
