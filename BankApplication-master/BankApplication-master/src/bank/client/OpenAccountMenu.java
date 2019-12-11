package bank.client;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

import org.joda.time.LocalDate;

import bank.common.Chequing;
import bank.common.GIC;
import bank.common.RemoteBank;
import bank.common.Savings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class OpenAccountMenu {
	
	// Returns true if the full name has a first name and second name comma separated
	public static boolean nameIsGood(String input) {
		return (input.split(",").length == 2) ? true:false;
	}
	
	// Returns true if the account number is only alphaNumerical
	public static boolean isValidAcountNumber(String input) {
		return input.matches("[a-zA-Z0-9]+");
	}
	
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
	
	// Returns true if the user inputs a valid integer number
	public static boolean isValidInt(String input) {
		try {
			@SuppressWarnings("unused")
			int test = Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) { 
			return false;
		}
	}
	
	/*
	 * Displays the main window to add Accounts.
	 * Change the layout based on the radio button which tells us which type of Account we are inserting.
	 * Displays an alert box if the user input was wrong, the account was added or not.
	 */
	public static void display(Stage window, RemoteBank bank) {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene openAccountScene;
		
		// Declaring Labels
		Label menuMessage = new Label("Fill the required fields, click the 'Create' button to save.");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label accountType = new Label("Account Type: ");
		accountType.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label fullName = new Label("Full Name(comma separated): ");
		fullName.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label number = new Label("Account Number: ");
		number.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label currentBalance = new Label("Current Balance: ");
		currentBalance.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label serviceCharge = new Label("Service Charge: ");
		serviceCharge.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label maxTransactions = new Label("Max # transactions allowed: ");
		maxTransactions.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label years = new Label("Years until Maturity: ");
		years.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label interestRate = new Label("Interest Rate: ");
		interestRate.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label fee = new Label("Withdraw Fee: ");
		fee.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Creating Input Fields
		ToggleGroup accountTypes = new ToggleGroup();
		
		RadioButton savings = new RadioButton("Savings");
		savings.setUserData("Savings");
		savings.setToggleGroup(accountTypes);
		savings.setSelected(true);
		RadioButton chequing = new RadioButton("Chequing");
		chequing.setUserData("Chequing");
		chequing.setToggleGroup(accountTypes); 
		RadioButton gic = new RadioButton("GIC");
		gic.setUserData("GIC");
		gic.setToggleGroup(accountTypes);
		
		HBox accounts = new HBox(20, savings, chequing, gic);
				
		TextField nameInput = new TextField();
		try {
			nameInput.setText(bank.getUserName());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		TextField numberInput = new TextField();
		TextField currentBalanceInput = new TextField();
		TextField inputField1 = new TextField(); // Used for Chequing or GIC extra fileds
		TextField inputField2 = new TextField(); // Used for Chequing or GIC extra fileds
		
		// Declaring and Implementing Buttons
		Button mainMenu = new Button("Main Menu");
		mainMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button create = new Button("Create");
		create.setOnAction(e -> {
			if(accountTypes.getSelectedToggle().getUserData().toString().equals("Savings")) {
				boolean nameGood = nameIsGood(nameInput.getText());
				boolean balanceGood = isValidDoube(currentBalanceInput.getText());
				boolean interestGood = isValidDoube(inputField1.getText());
				boolean feeGood = isValidDoube(inputField2.getText());
				boolean numberGood = isValidAcountNumber(numberInput.getText());
				
				if(nameGood && balanceGood && interestGood && feeGood && numberGood) {
					double balance = Double.parseDouble(currentBalanceInput.getText());
					double interest = Double.parseDouble(inputField1.getText());
					double fee2 = Double.parseDouble(inputField2.getText());
					try {
						LocalDate now = new LocalDate();
						boolean success = bank.addAccount(new Savings(nameInput.getText(), numberInput.getText(), balance, bank.getUsrSIN(), interest, fee2, now.getYear(), now.getMonthOfYear(), now.getDayOfMonth()));
						if(success)
							AlertBox.display("Account Added!", "Account Successfully Added!");
						else
							AlertBox.display("Account Not Added!", "Account Already Exists!");
					} catch (RemoteException | FileNotFoundException e1) {
						e1.printStackTrace();
					}
				} else {
					AlertBox.display("Invalid Input", "Input is Wrong. Check your fields");
				}
			}
			
			if(accountTypes.getSelectedToggle().getUserData().toString().equals("Chequing")) {
				boolean nameGood = nameIsGood(nameInput.getText());
				boolean balanceGood = isValidDoube(currentBalanceInput.getText());
				boolean chargeGood = isValidDoube(inputField1.getText());
				boolean maxGood = isValidInt(inputField2.getText());
				boolean numberGood = isValidAcountNumber(numberInput.getText());
				
				if(nameGood && balanceGood && chargeGood && maxGood && numberGood) {
					double balance = Double.parseDouble(currentBalanceInput.getText());
					double charge = Double.parseDouble(inputField1.getText());
					int max = Integer.parseInt(inputField2.getText());
					try {
						LocalDate now = new LocalDate();
						boolean res = bank.addAccount(new Chequing(nameInput.getText(), numberInput.getText(), balance, bank.getUsrSIN(), charge, max, now.getYear(), now.getMonthOfYear(), now.getDayOfMonth()));
						if(res)
							AlertBox.display("Account Added!", "Account Successfully Added!");
						else
							AlertBox.display("Account Not Added!", "Account Already Exists!");
					} catch (RemoteException | FileNotFoundException e1) {
						e1.printStackTrace();
					}
				} else {
					AlertBox.display("Invalid Input", "Input is Wrong. Check your fields");
				}
			} 
			
			if(accountTypes.getSelectedToggle().getUserData().toString().equals("GIC")) {
				boolean nameGood = nameIsGood(nameInput.getText());
				boolean balanceGood = isValidDoube(currentBalanceInput.getText());
				boolean yearGood = isValidInt(inputField1.getText());
				boolean interestGood = isValidDoube(inputField2.getText());
				boolean numberGood = isValidAcountNumber(numberInput.getText());
				
				if(nameGood && balanceGood && yearGood && interestGood && numberGood) {
					double balance = Double.parseDouble(currentBalanceInput.getText());
					int year = Integer.parseInt(inputField1.getText());
					double interest = Double.parseDouble(inputField2.getText());
					try {
						LocalDate now = new LocalDate();
						boolean success = bank.addAccount(new GIC(nameInput.getText(), numberInput.getText(), balance, bank.getUsrSIN(), year, interest, now.getYear(), now.getMonthOfYear(), now.getDayOfMonth()));
						if(success)
							AlertBox.display("Account Added!", "Account Successfully Added!");
						else
							AlertBox.display("Account Not Added!", "Account Already Exists!");
					} catch (RemoteException | FileNotFoundException e1) {
						e1.printStackTrace();
					}
				} else {
					AlertBox.display("Invalid Input", "Input is Wrong. Check your fields");
				}
			}
		});	
		create.setDefaultButton(true);
		
		// Creating Initial Layout
		GridPane body = new GridPane();
		body.setHgap(7);
		body.setVgap(2);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.add(accountType, 0, 0);
		GridPane.setHalignment(accountType, HPos.RIGHT);
		body.add(accounts, 1, 0);
		GridPane.setHalignment(accounts, HPos.LEFT);
		body.add(fullName, 0, 1);
		GridPane.setHalignment(fullName, HPos.RIGHT);
		body.add(nameInput, 1, 1);
		GridPane.setHalignment(nameInput, HPos.LEFT);
		body.add(number, 0, 2);
		GridPane.setHalignment(number, HPos.RIGHT);
		body.add(numberInput, 1, 2);
		GridPane.setHalignment(numberInput, HPos.LEFT);
		body.add(currentBalance, 0, 3);
		GridPane.setHalignment(currentBalance, HPos.RIGHT);
		body.add(currentBalanceInput, 1, 3);
		GridPane.setHalignment(currentBalanceInput, HPos.LEFT);
		body.add(interestRate, 0, 4);
		GridPane.setHalignment(interestRate, HPos.RIGHT);
		body.add(inputField1, 1, 4);
		GridPane.setHalignment(inputField1, HPos.LEFT);
		body.add(fee, 0, 5);
		GridPane.setHalignment(fee, HPos.RIGHT);
		body.add(inputField2, 1, 5);
		GridPane.setHalignment(inputField2, HPos.LEFT);
		body.add(create, 1, 6);
		GridPane.setHalignment(create, HPos.LEFT);
		
		BorderPane.setAlignment(mainMenu, Pos.CENTER_RIGHT);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(mainMenu, new Insets(12,12,12,12));
		BorderPane openAccountLayout = new BorderPane();
		openAccountLayout.setTop(menuMessage);
		openAccountLayout.setCenter(body);
		openAccountLayout.setBottom(mainMenu);
		openAccountLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		openAccountScene = new Scene(openAccountLayout, 620,486);
		window.setScene(openAccountScene);
		
		// Create New Listener to Change the Scene when needed
		accountTypes.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
		            if (accountTypes.getSelectedToggle() != null) {
		            	// When "Savings' is selected
		            	if(accountTypes.getSelectedToggle().getUserData().toString().equals("Savings")) {
		            		body.getChildren().clear();
		            		body.add(accountType, 0, 0);
		            		GridPane.setHalignment(accountType, HPos.RIGHT);
		            		body.add(accounts, 1, 0);
		            		GridPane.setHalignment(accounts, HPos.LEFT);
		            		body.add(fullName, 0, 1);
		            		GridPane.setHalignment(fullName, HPos.RIGHT);
		            		body.add(nameInput, 1, 1);
		            		GridPane.setHalignment(nameInput, HPos.LEFT);
		            		body.add(number, 0, 2);
		            		GridPane.setHalignment(number, HPos.RIGHT);
		            		body.add(numberInput, 1, 2);
		            		GridPane.setHalignment(numberInput, HPos.LEFT);
		            		body.add(currentBalance, 0, 3);
		            		GridPane.setHalignment(currentBalance, HPos.RIGHT);
		            		body.add(currentBalanceInput, 1, 3);
		            		GridPane.setHalignment(currentBalanceInput, HPos.LEFT);
		            		body.add(interestRate, 0, 4);
		            		GridPane.setHalignment(interestRate, HPos.RIGHT);
		            		body.add(inputField1, 1, 4);
		            		GridPane.setHalignment(inputField1, HPos.LEFT);
		            		body.add(fee, 0, 5);
		            		GridPane.setHalignment(fee, HPos.RIGHT);
		            		body.add(inputField2, 1, 5);
		            		GridPane.setHalignment(inputField2, HPos.LEFT);
		            		body.add(create, 1, 6);
		            		GridPane.setHalignment(create, HPos.LEFT);
		            		
		            		openAccountLayout.setCenter(body);
		            	}
		            	
		            	// When "Chequing" is selected
		            	if(accountTypes.getSelectedToggle().getUserData().toString().equals("Chequing")) {
		            		body.getChildren().clear();
		            		body.add(accountType, 0, 0);
		            		GridPane.setHalignment(accountType, HPos.RIGHT);
		            		body.add(accounts, 1, 0);
		            		GridPane.setHalignment(accounts, HPos.LEFT);
		            		body.add(fullName, 0, 1);
		            		GridPane.setHalignment(fullName, HPos.RIGHT);
		            		body.add(nameInput, 1, 1);
		            		GridPane.setHalignment(nameInput, HPos.LEFT);
		            		body.add(number, 0, 2);
		            		GridPane.setHalignment(number, HPos.RIGHT);
		            		body.add(numberInput, 1, 2);
		            		GridPane.setHalignment(numberInput, HPos.LEFT);
		            		body.add(currentBalance, 0, 3);
		            		GridPane.setHalignment(currentBalance, HPos.RIGHT);
		            		body.add(currentBalanceInput, 1, 3);
		            		GridPane.setHalignment(currentBalanceInput, HPos.LEFT);
		            		body.add(serviceCharge, 0, 4);
		            		GridPane.setHalignment(serviceCharge, HPos.RIGHT);
		            		body.add(inputField1, 1, 4);
		            		GridPane.setHalignment(inputField1, HPos.LEFT);
		            		body.add(maxTransactions, 0, 5);
		            		GridPane.setHalignment(maxTransactions, HPos.RIGHT);
		            		body.add(inputField2, 1, 5);
		            		GridPane.setHalignment(inputField2, HPos.LEFT);
		            		body.add(create, 1, 6);
		            		GridPane.setHalignment(create, HPos.LEFT);
		            		
		            		openAccountLayout.setCenter(body);
		            	}
		            	
		            	// When "GIC" is selected
		            	if(accountTypes.getSelectedToggle().getUserData().toString().equals("GIC")) {
		            		body.getChildren().clear();
		            		body.add(accountType, 0, 0);
		            		GridPane.setHalignment(accountType, HPos.RIGHT);
		            		body.add(accounts, 1, 0);
		            		GridPane.setHalignment(accounts, HPos.LEFT);
		            		body.add(fullName, 0, 1);
		            		GridPane.setHalignment(fullName, HPos.RIGHT);
		            		body.add(nameInput, 1, 1);
		            		GridPane.setHalignment(nameInput, HPos.LEFT);
		            		body.add(number, 0, 2);
		            		GridPane.setHalignment(number, HPos.RIGHT);
		            		body.add(numberInput, 1, 2);
		            		GridPane.setHalignment(numberInput, HPos.LEFT);
		            		body.add(currentBalance, 0, 3);
		            		GridPane.setHalignment(currentBalance, HPos.RIGHT);
		            		body.add(currentBalanceInput, 1, 3);
		            		GridPane.setHalignment(currentBalanceInput, HPos.LEFT);
		            		body.add(years, 0, 4);
		            		GridPane.setHalignment(years, HPos.RIGHT);
		            		body.add(inputField1, 1, 4);
		            		GridPane.setHalignment(inputField1, HPos.LEFT);
		            		body.add(interestRate, 0, 5);
		            		GridPane.setHalignment(interestRate, HPos.RIGHT);
		            		body.add(inputField2, 1, 5);
		            		GridPane.setHalignment(inputField2, HPos.LEFT);
		            		body.add(create, 1, 6);
		            		GridPane.setHalignment(create, HPos.LEFT);
		            		
		            		openAccountLayout.setCenter(body);
		            	}
		            }                
		        }
		});
	}
}
