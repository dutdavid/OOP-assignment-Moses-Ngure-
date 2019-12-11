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

public class Transfer {
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
		Label menuMessage = new Label("Transfer Center");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label amount = new Label("Amount($):");
		amount.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label accountNum1 = new Label("From:");
		accountNum1.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label accountNum2 = new Label("To:");
		accountNum2.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Declaring Input Fields
		TextField amountInput = new TextField();
		TextField accountInput1 = new TextField();
		accountInput1.setPromptText("Account #");
		TextField accountInput2 = new TextField();
		accountInput2.setPromptText("Account #");
		
		// Declaring and Implementing Buttons
		Button homeMenu = new Button("Home Menu");
		homeMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button transfer = new Button("Transfer");
		transfer.setOnAction(e -> {			
			boolean result = false;
			
			if(isValidDoube(amountInput.getText())) {
				double amount_ = Double.parseDouble(amountInput.getText());
				
				if(isPositive(amount_)) {
					int index1 = -1;
					int index2 = -1;
					
					try {
						index1 = bank.getAccountIndex(accountInput1.getText(), bank.isAdmin());
						index2 = bank.getAccountIndex(accountInput2.getText(), true);
						
						if(index1 >= 0 && index2 >=0) {
							result = bank.transfer(index1, index2, amount_);
							if(result) {
								Account tmp1 = bank.searchByAccountNumber(accountInput1.getText());
								Account tmp2 = bank.searchByAccountNumber(accountInput2.getText());
								
								if(tmp1 != null && tmp2 != null) {
									String output = "New Balance" + System.lineSeparator() + "Account 1: $" + tmp1.getBalance() +
											System.lineSeparator() + "Account 2: $" + tmp2.getBalance();
									AlertBox.display("Transfer Processed!", output);
								}
							}
							else {
								AlertBox.display("Transfer Not Processed!", "Transfer could not be processed!");
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
		transfer.setDefaultButton(true);
		
		// Creating Initial Layout
		GridPane body = new GridPane();
		body.setAlignment(Pos.CENTER);
		body.setHgap(4);
		body.setVgap(2);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.add(amount, 0, 0);
		GridPane.setHalignment(amount, HPos.RIGHT);
		body.add(amountInput, 1, 0);
		GridPane.setHalignment(amountInput, HPos.LEFT);
		body.add(accountNum1, 0, 1);
		GridPane.setHalignment(accountNum1, HPos.RIGHT);
		body.add(accountInput1, 1, 1);
		GridPane.setHalignment(accountInput1, HPos.LEFT);
		body.add(accountNum2, 0, 2);
		GridPane.setHalignment(accountNum2, HPos.RIGHT);
		body.add(accountInput2, 1, 2);
		GridPane.setHalignment(accountInput2, HPos.LEFT);
		body.add(transfer, 1, 3);
		GridPane.setHalignment(transfer, HPos.LEFT);
		
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
