package bank.client;

import java.rmi.RemoteException;

import bank.common.Account;
import bank.common.Chequing;
import bank.common.GIC;
import bank.common.RemoteBank;
import bank.common.Savings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DisplayAccounts {
	
	// Returns true if the full name has a first name and second name comma separated
	public static boolean sinIsGood(String SIN) {
		int sin = 0;
		
		try {
			sin = Integer.parseInt(SIN);
			
			if(sin != 0) {
				if(sin > 000000000 && sin <999999999)
					return true;
				else
					return false;
			}
		} catch(NumberFormatException e) {
			AlertBox.display("Wrong SIN!", "SIN Number is invalid!");
			return false;
		}
		return false;
	}
		
	// Returns true if the account number is only alphaNumerical
	public static boolean isValidAcountNumber(String input) {
		return input.matches("[a-zA-Z0-9]+");
	}
	
	@SuppressWarnings("unchecked")
	public static void display(Stage  window, RemoteBank bank) throws RemoteException {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene openAccountScene;
				
		// Declaring Labels
		Label menuMessage = new Label("Search Accounts");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label searchBy = new Label("Search Account by: ");
		searchBy.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label searchCriteria = new Label("Search: ");
		searchCriteria.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label savingsAccountsLabel = new Label("Savings Accounts");
		savingsAccountsLabel.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label chequingAccountsLabel = new Label("Chequing Accounts");
		chequingAccountsLabel.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label gicAccountsLabel = new Label("GIC Accounts");
		gicAccountsLabel.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label accountsType = new Label("Accounts Type");
		accountsType.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Declaring Radio Fields
		ToggleGroup searchTypes = new ToggleGroup();
		
		RadioButton all = new RadioButton("All");
		all.setUserData("All");
		all.setToggleGroup(searchTypes);
		all.setSelected(true);
		RadioButton type = new RadioButton("Type");
		type.setUserData("Type");
		type.setToggleGroup(searchTypes);
		RadioButton sin = new RadioButton("SIN");
		sin.setUserData("SIN");
		sin.setToggleGroup(searchTypes);
		RadioButton number = new RadioButton("Account Number");
		number.setUserData("Number");
		number.setToggleGroup(searchTypes);
		
		HBox types = new HBox(20);
		
		if(bank.isAdmin())
			types.getChildren().addAll(all, type, number, sin);
		else
			types.getChildren().addAll(all, type, number);
		
		// Create Combo Box
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "Savings",
			        "Chequing",
			        "GIC"
			    );
		
		final ComboBox<String> typesBox = new ComboBox<String>(options);
		typesBox.getSelectionModel().selectFirst();
		
		// Declare Input Field
		TextField input = new TextField();
		
		// Declaring Buttons
		Button mainMenu = new Button("Main Menu");
		mainMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button search = new Button("Search");
		
		// Creating Initial Layout		
		VBox body = new VBox(10);
		body.setAlignment(Pos.TOP_CENTER);
		
		HBox searchByAcc = new HBox(10);
		HBox searchByCrit = new HBox(10);
		HBox accountTypes = new HBox(10);
		searchByAcc.setAlignment(Pos.TOP_CENTER);
		searchByCrit.setAlignment(Pos.TOP_CENTER);
		accountTypes.setAlignment(Pos.TOP_CENTER);
		
		searchByAcc.getChildren().addAll(searchBy, types);
		searchByCrit.getChildren().addAll(searchCriteria, input);
		accountTypes.getChildren().addAll(accountsType, typesBox);
		
		body.getChildren().addAll(searchByAcc, search);
		
		BorderPane.setAlignment(mainMenu, Pos.CENTER_RIGHT);
		BorderPane.setAlignment(menuMessage, Pos.CENTER);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(mainMenu, new Insets(12,12,12,12));
		BorderPane openAccountLayout = new BorderPane();
		openAccountLayout.setTop(menuMessage);
		openAccountLayout.setCenter(body);
		openAccountLayout.setBottom(mainMenu);
		openAccountLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Implement search Button
		search.setOnAction(e -> {
			Account[] tmp = null;
			Account tmp_ = null;
			boolean isType = false;
			boolean isAll = false;
			
			if(searchTypes.getSelectedToggle().getUserData().toString().equals("All")) {
				try {
					tmp = bank.getAll();
					isAll = true;
				} catch (RemoteException e1) {
					AlertBox.display("Error Message: ", e1.toString());
				}
			}
			
			if(searchTypes.getSelectedToggle().getUserData().toString().equals("Type")) {
				try {
					String t = null;
					
					if(typesBox.getValue().compareTo("Savings") == 0)
						t = "SAV";
					else if(typesBox.getValue().compareTo("Chequing") == 0)
						t = "CHQ";
					else
						t = "GIC";
					
					tmp = bank.getAllByType(t);
					isType = true;
				} catch (RemoteException e1) {
					AlertBox.display("Error Message: ", e1.toString());
				}
			}
			
			if(searchTypes.getSelectedToggle().getUserData().toString().equals("Number")) {
				try {
					boolean isValid = isValidAcountNumber(input.getText());
					
					if(isValid)
						tmp_ = bank.searchByAccountNumber(input.getText());
					else
						AlertBox.display("Invalid Input", "Input is Wrong. Check your fields");
					
				} catch (RemoteException e1) {
					AlertBox.display("Error Message: ", e1.toString());
				}
			}
			
			if(searchTypes.getSelectedToggle().getUserData().toString().equals("SIN")) {
				try {
					boolean isValid = sinIsGood(input.getText());
					int sin_ = Integer.parseInt(input.getText());
					
					if(isValid)
						tmp = bank.getAllBySIN(sin_);
					else
						AlertBox.display("Invalid Input", "Input is Wrong. Check your fields");					
					
				} catch (RemoteException | NumberFormatException e1) {
					AlertBox.display("Error Message: ", e1.toString());
				}
			}
			
			// Get All Savings Accounts
			ObservableList<Savings> savingsAccounts = FXCollections.observableArrayList();
			if(tmp_ != null && tmp_ instanceof Savings)
				savingsAccounts.add((Savings) tmp_);
			if(tmp != null) {
				for(int i = 0; i < tmp.length; i++) {
					if(tmp[i].getType().equals("SAV"))
						savingsAccounts.add((Savings) tmp[i]);
				}
			}
			
			
			// Get All Chequing Accounts
			ObservableList<Chequing> chequingAccounts = FXCollections.observableArrayList();
			if(tmp_ != null && tmp_ instanceof Chequing)
				chequingAccounts.add((Chequing) tmp_);
			if(tmp != null) {
				for(int i = 0; i < tmp.length; i++) {
					if(tmp[i].getType().equals("CHQ"))
						chequingAccounts.add((Chequing) tmp[i]);
				}
			}
			
			
			// Get All GIC Accounts
			ObservableList<GIC> gicAccounts = FXCollections.observableArrayList();
			if(tmp_ != null && tmp_ instanceof GIC)
				gicAccounts.add((GIC) tmp_);
			if(tmp != null) {
				for(int i = 0; i < tmp.length; i++) {
					if(tmp[i].getType().equals("GIC"))
						gicAccounts.add((GIC) tmp[i]);
				}
			}
			
			
			// Create Table for Savings Accounts
			TableView<Savings> savingsTable;
			
			// Create Columns for savingsTable
			TableColumn<Savings, String> nameColumn = new TableColumn<>("Holder Name");
			nameColumn.setMinWidth(110);
			nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
			
			TableColumn<Savings, String> accountNumberColumn = new TableColumn<>("Account Number");
			accountNumberColumn.setMinWidth(130);
			accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
			
			TableColumn<Savings, String> dateColumn = new TableColumn<>("Opened Date");
			dateColumn.setMinWidth(130);
			dateColumn.setCellValueFactory(new PropertyValueFactory<>("openedDate"));
			
			TableColumn<Savings, Double> feeColumn = new TableColumn<>("Withdraw Fee");
			feeColumn.setMinWidth(130);
			feeColumn.setCellValueFactory(new PropertyValueFactory<>("withdrawFee"));
			
			TableColumn<Savings, Double> feesColumn = new TableColumn<>("Withdraw Fees");
			feesColumn.setMinWidth(130);
			feesColumn.setCellValueFactory(new PropertyValueFactory<>("withdrawFees"));
			
			TableColumn<Savings, Double> interestColumn = new TableColumn<>("Interest Rate");
			interestColumn.setMinWidth(110);
			interestColumn.setCellValueFactory(new PropertyValueFactory<>("annualInterestRate"));
			
			TableColumn<Savings, Double> interestIncomeColumn = new TableColumn<>("Interest Income");
			interestIncomeColumn.setMinWidth(130);
			interestIncomeColumn.setCellValueFactory(new PropertyValueFactory<>("interestIncome"));
			
			TableColumn<Savings, Double> finalBalanceColumn = new TableColumn<>("Final Balance");
			finalBalanceColumn.setMinWidth(110);
			finalBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
			
			savingsTable = new TableView<>();
			savingsTable.setItems(savingsAccounts);
			savingsTable.getColumns().addAll(nameColumn, accountNumberColumn, dateColumn, feeColumn, feesColumn, interestColumn, interestIncomeColumn, finalBalanceColumn);
			
			// Create Table for Chequing Accounts
			TableView<Chequing> chequingTable;
						
			// Create Columns for chequingTable
			TableColumn<Chequing, String> name2Column = new TableColumn<>("Holder Name");
			name2Column.setMinWidth(110);
			name2Column.setCellValueFactory(new PropertyValueFactory<>("fullName"));
			
			TableColumn<Chequing, String> account2NumberColumn = new TableColumn<>("Account Number");
			account2NumberColumn.setMinWidth(130);
			account2NumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
			
			TableColumn<Chequing, String> date2Column = new TableColumn<>("Opened Date");
			date2Column.setMinWidth(130);
			date2Column.setCellValueFactory(new PropertyValueFactory<>("openedDate"));
			
			TableColumn<Chequing, Double> serviceChargeColumn = new TableColumn<>("Service Fee");
			serviceChargeColumn.setMinWidth(100);
			serviceChargeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionFee"));
			
			TableColumn<Chequing, Double> totalServiceChargeColumn = new TableColumn<>("Total Fees");
			totalServiceChargeColumn.setMinWidth(90);
			totalServiceChargeColumn.setCellValueFactory(new PropertyValueFactory<>("totalTransactionFees"));
			
			TableColumn<Chequing, Integer> maxTransactionsColumn = new TableColumn<>("Transactions Allowed");
			maxTransactionsColumn.setMinWidth(165);
			maxTransactionsColumn.setCellValueFactory(new PropertyValueFactory<>("maxTransactions"));
			
			TableColumn<Chequing, String> listTransactionsColumn = new TableColumn<>("transactions");
			listTransactionsColumn.setMinWidth(110);
			listTransactionsColumn.setCellValueFactory(new PropertyValueFactory<>("transactions"));
			
			TableColumn<Chequing, Double> finalBalance2Column = new TableColumn<>("Final Balance");
			finalBalance2Column.setMinWidth(140);
			finalBalance2Column.setCellValueFactory(new PropertyValueFactory<>("balance"));
			
			chequingTable = new TableView<>();
			chequingTable.setItems(chequingAccounts);
			chequingTable.getColumns().addAll(name2Column, account2NumberColumn, date2Column, 
					serviceChargeColumn, totalServiceChargeColumn, maxTransactionsColumn, listTransactionsColumn, finalBalance2Column);
			
			// Create Table for Savings Accounts
			TableView<GIC> gicTable;
						
			// Create Columns for savingsTable
			TableColumn<GIC, String> name3Column = new TableColumn<>("Holder Name");
			name3Column.setMinWidth(110);
			name3Column.setCellValueFactory(new PropertyValueFactory<>("fullName"));
			
			TableColumn<GIC, String> accountNumber3Column = new TableColumn<>("Account Number");
			accountNumber3Column.setMinWidth(130);
			accountNumber3Column.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
			
			TableColumn<GIC, String> date3Column = new TableColumn<>("Opened Date");
			date3Column.setMinWidth(130);
			date3Column.setCellValueFactory(new PropertyValueFactory<>("openedDate"));
			
			TableColumn<GIC, Double> annualInterestRateColumn = new TableColumn<>("Annual Interest Rate");
			annualInterestRateColumn.setMinWidth(170);
			annualInterestRateColumn.setCellValueFactory(new PropertyValueFactory<>("annualInterestRate"));
			
			TableColumn<GIC, Double> investmentPeriodColumn = new TableColumn<>("Investement" + System.lineSeparator() + "Period");
			investmentPeriodColumn.setMinWidth(115);
			investmentPeriodColumn.setCellValueFactory(new PropertyValueFactory<>("years"));
			
			TableColumn<GIC, Double> intrestMaturity = new TableColumn<>("Interest Income" + System.lineSeparator() + "at Maturity");
			intrestMaturity.setMinWidth(145);
			intrestMaturity.setCellValueFactory(new PropertyValueFactory<>("interestIncome"));
			
			TableColumn<GIC, Double> balanceAtMaturityColumn = new TableColumn<>("Balance" + System.lineSeparator() + "at Maturity");
			balanceAtMaturityColumn.setMinWidth(130);
			balanceAtMaturityColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
			
			gicTable = new TableView<>();
			gicTable.setItems(gicAccounts);
			gicTable.getColumns().addAll(name3Column, accountNumber3Column, date3Column, 
					annualInterestRateColumn, investmentPeriodColumn, intrestMaturity, balanceAtMaturityColumn);
			
			VBox results = new VBox(10, savingsAccountsLabel, savingsTable, chequingAccountsLabel, chequingTable,
					gicAccountsLabel, gicTable);
			results.setAlignment(Pos.TOP_CENTER);
			
			ScrollPane tables = new ScrollPane();
			tables.setHbarPolicy(ScrollBarPolicy.NEVER);
			tables.setContent(results);
			
			body.getChildren().clear();
			if(isType)
				body.getChildren().addAll(searchByAcc, accountTypes, search, tables);
			else if(isAll)
				body.getChildren().addAll(searchByAcc, search, tables);
			else
				body.getChildren().addAll(searchByAcc, searchByCrit, search, tables);
		});
		search.setDefaultButton(true);
		
		// Create New Listener to Change the Scene when needed
		// Create New Listener to Change the Scene when needed
		searchTypes.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
		            if (searchTypes.getSelectedToggle() != null) {
		            	// When "All' is selected
		            	if(searchTypes.getSelectedToggle().getUserData().toString().equals("All")) {
		            		body.getChildren().clear();
		            		body.getChildren().add(searchByAcc);
		            		
		            		search.fire();
		            		
		            		openAccountLayout.setCenter(body);
		            	}
		            	
		            	// When "Type' is selected
		            	if(searchTypes.getSelectedToggle().getUserData().toString().equals("Type")) {
		            		body.getChildren().clear();
		            		body.getChildren().addAll(searchByAcc, accountTypes, search);
		            		
		            		openAccountLayout.setCenter(body);
		            	}
		            	
		            	// When "Type' or 'NUmber' is selected
		            	if(searchTypes.getSelectedToggle().getUserData().toString().equals("Name") || searchTypes.getSelectedToggle().getUserData().toString().equals("Number")) {
		            		body.getChildren().clear();
		            		body.getChildren().addAll(searchByAcc, searchByCrit, search);
		            		
		            		openAccountLayout.setCenter(body);
		            	}
		            }                
		        }
		});
		
		// Create New Scene and Set it to the Window
		openAccountScene = new Scene(openAccountLayout, 1000,686);
		window.setScene(openAccountScene);
	}
}
