package bank.client;

import java.rmi.RemoteException;

import bank.common.Account;
import bank.common.RemoteBank;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Withdraw {
	// Returns true if the user inputs a valid double number
	public static boolean isValidDoube(String input) {
		try {
			@SuppressWarnings("unused")
			double test = Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) { 
			return false;
		}
	}
	
	// Check if the amount entered is positive
	public static boolean isPositive(double amount) {
		if(amount >= 0)
			return true;
		else
			return false;
	}
	
	public static void display(Stage  window, RemoteBank bank) throws RemoteException {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene displayScene;
		
		// Declaring Labels
		Label menuMessage = new Label("Withdraw Center");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label amount = new Label("Amount($):");
		amount.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label accountNum = new Label("Account #:");
		accountNum.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Declaring Input Fields
		TextField amountInput = new TextField();
		TextField accountInput = new TextField();
		
		// Declaring and Implementing Buttons
		Button homeMenu = new Button("Home Menu");
		homeMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button withdraw = new Button("Withdraw");
		withdraw.setOnAction(e -> {			
			Account tmp = null;
			boolean result = false;
			
			if(isValidDoube(amountInput.getText())) {
				double amount_ = Double.parseDouble(amountInput.getText());
				
				if(isPositive(amount_)) {
					int index = -1;
					
					try {
						index = bank.getAccountIndex(accountInput.getText(), bank.isAdmin());
						
						if(index >= 0) {
							result = bank.withdraw(index, amount_);
							if(result) {
								tmp = bank.searchByAccountNumber(accountInput.getText());
								
								if(tmp != null) {
									DisplayAccount.display("Account Info", "New Account Info:", tmp);
								}
							}
							else {
								AlertBox.display("Withdraw Not Processed!", "Withdraw could not be processed!");
							}
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
				else {
					AlertBox.display("Wrong Amount Input", "The Amount entered cannot be negative!");
				}
			}
			else {
				AlertBox.display("Wrong Amount Input", "The Amount entered is Invalid!");
			}
		});
		withdraw.setDefaultButton(true);
		
		// Creating Initial Layout
		GridPane body = new GridPane();
		body.setAlignment(Pos.CENTER);
		body.setHgap(3);
		body.setVgap(2);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.add(amount, 0, 0);
		GridPane.setHalignment(amount, HPos.RIGHT);
		body.add(amountInput, 1, 0);
		GridPane.setHalignment(amountInput, HPos.LEFT);
		body.add(accountNum, 0, 1);
		GridPane.setHalignment(accountNum, HPos.RIGHT);
		body.add(accountInput, 1, 1);
		GridPane.setHalignment(accountInput, HPos.LEFT);
		body.add(withdraw, 1, 2);
		GridPane.setHalignment(withdraw, HPos.LEFT);
		
		BorderPane.setAlignment(homeMenu, Pos.CENTER_RIGHT);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(homeMenu, new Insets(12,12,12,12));
		BorderPane registerProfileLayout = new BorderPane();
		registerProfileLayout.setTop(menuMessage);
		registerProfileLayout.setCenter(body);
		registerProfileLayout.setBottom(homeMenu);
		registerProfileLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		displayScene = new Scene(registerProfileLayout, 520,386);
		window.setScene(displayScene);
	}
}
