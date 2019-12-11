package bank.client;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.*;

import java.rmi.RemoteException;

import bank.common.Account;
import bank.common.RemoteBank;
import javafx.geometry.*;;

public class CloseAccountMenu {
	public static void display(Stage window, RemoteBank bank){
		// Declaring Scenes
		Scene home = window.getScene();
		Scene deleteAccountScene;
		
		// Declaring Labels
		Label menuMessage = new Label("Insert an Account Number and press 'Delete'");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label accountNumber = new Label("Account Number: ");
		accountNumber.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Declare Input Fields
		TextField numberInput = new TextField();
		
		// Declare Buttons
		Button mainMenu = new Button("Main Menu");
		mainMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button delete = new Button("Delete");
		delete.setOnAction(e -> {
			if(!(numberInput.getText() == null || numberInput.getText().equals("") || numberInput.getText().trim().isEmpty())) {
				Account tmp = null;
				try {
					tmp = bank.removeAccount(numberInput.getText());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(tmp == null)
					AlertBox.display("Account Not Found", "Account Not Found or Permission Denied");
				else
					DisplayAccount.display("Account Found", "Account Successfully Deleted", tmp);
			} else {
				AlertBox.display("Invalid Input", "Input is Empty. Fill the field");
			}
		});
		delete.setDefaultButton(true);
		
		// Creating Initial Layout
		GridPane body = new GridPane();
		body.setHgap(2);
		body.setVgap(2);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.add(accountNumber, 0, 0);
		GridPane.setHalignment(accountNumber, HPos.RIGHT);
		body.add(numberInput, 1, 0);
		GridPane.setHalignment(numberInput, HPos.LEFT);
		body.add(delete, 1, 1);
		GridPane.setHalignment(delete, HPos.LEFT);
		
		BorderPane.setAlignment(mainMenu, Pos.CENTER_RIGHT);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(mainMenu, new Insets(12,12,12,12));
		BorderPane openAccountLayout = new BorderPane();
		openAccountLayout.setTop(menuMessage);
		openAccountLayout.setCenter(body);
		openAccountLayout.setBottom(mainMenu);
		openAccountLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		deleteAccountScene = new Scene(openAccountLayout, 520,286);
		window.setScene(deleteAccountScene);
	}
}
