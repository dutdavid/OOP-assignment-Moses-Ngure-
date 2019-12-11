package bank.client;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import bank.common.RemoteBank;
import bank.common.User;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class TaxStatements {
	
	// Print node
	public static void print(final Node node) {
		Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        node.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }
	}
	
	// Validate SSN
	private static boolean validateSIN(String SIN) {
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
	
	public static int randTaxNumGenerator() {
		Random rand = new Random();
		int tmp = rand.nextInt(100) + (rand.nextInt(100000) + 100) + (rand.nextInt(1000000) + 100000);
		if(tmp < 1000000)
			tmp *= 10;
		return tmp;
	}
	
	public static void display(Stage  window, RemoteBank bank) throws RemoteException {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene displayScene;
		
		// Declaring Labels
		Label menuMessage = new Label("Tax Statement(s)");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label holderSINLabel = new Label("Account Holder SIN:");
		holderSINLabel.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Declaring Input Fields
		TextField holderSINInput = new TextField();
		
		// Generate Random Tax Statement Number
		int randTaxNum = randTaxNumGenerator();
		
		// Get Current Date
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
		Date date = new Date();
				
		// Declaring Text objects
		Text bankNameText = new Text("Cali Imperial Bank");
		bankNameText.setFont(Font.loadFont("file:resources/fonts/myfont.ttf", 24));
		Text taxStatementText = new Text("Tax Statement");
		taxStatementText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		Text statementNumText = new Text("Tax Statement #: " + randTaxNum);
		statementNumText.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 14));
		Text statementDateText = new Text("Date: " + dateFormat.format(date));
		statementDateText.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 14));
		Text holderNameText = new Text();
		holderNameText.setUnderline(true); 
		holderNameText.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 14));
		Text holderInfoText = new Text();
		holderInfoText.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 14));
		Text accountText = new Text("Account Tax Statement(s)");
		accountText.setUnderline(true); 
		accountText.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 14));
		Text accountInfoText = new Text();
		accountInfoText.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 14));
		
		// Declaring and Implementing Buttons
		Button homeMenu = new Button("Home Menu");
		homeMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button search = new Button("Search");
		search.setOnAction(e -> {			
			if(validateSIN(holderSINInput.getText())) {
				int sin = Integer.parseInt(holderSINInput.getText());
				
				try {
					User usr = bank.getUserBySIN(sin);
					if(usr != null) {
						// Initialize Text based on the User logged in who is not an Admin
						holderNameText.setText(usr.getFullName());
						holderInfoText.setText(bank.getUserInfo(sin));
						String accountsInfo = bank.getTaxableAccounts(sin);
						accountInfoText.setText(accountsInfo);
						
						// Create Layout for this Scenario
						GridPane top = new GridPane();
						top.setAlignment(Pos.CENTER);
						top.setHgap(2);
						top.setVgap(2);
						top.setPadding(new Insets(0, 10, 0, 10));
						
						top.add(holderSINLabel, 0, 0);
						GridPane.setHalignment(holderSINLabel, HPos.RIGHT);
						top.add(holderSINInput, 1, 0);
						GridPane.setHalignment(holderSINInput, HPos.LEFT);
						top.add(search, 1, 1);
						GridPane.setHalignment(search, HPos.LEFT);
						
						// Create Layout for this Scenario
						VBox pageContent = new VBox(20);
						pageContent.setAlignment(Pos.CENTER);
						
						HBox numDate = new HBox(20);
						numDate.setAlignment(Pos.CENTER);
						numDate.getChildren().addAll(statementNumText, statementDateText);
						
						pageContent.getChildren().addAll(bankNameText, taxStatementText, numDate, holderNameText, holderInfoText, accountText, accountInfoText);
						
						ScrollPane page = new ScrollPane();
						page.setHbarPolicy(ScrollBarPolicy.NEVER);
						page.setVbarPolicy(ScrollBarPolicy.ALWAYS);
						page.setFitToHeight(true);
						page.setFitToWidth(true);
						page.setContent(pageContent);
						
						VBox body = new VBox(20);
						body.setAlignment(Pos.CENTER);
						body.getChildren().addAll(top, pageContent);
						
						// Declare and implement PRINT button
						Button print = new Button("PRINT");
						print.setOnAction(e1 -> {			
							print(pageContent);
						});
						print.setDefaultButton(true);
						
						HBox bottom = new HBox(20);
						bottom.setAlignment(Pos.CENTER_RIGHT);
						bottom.getChildren().addAll(print, homeMenu);
									
						BorderPane.setAlignment(menuMessage, Pos.CENTER_LEFT);
						BorderPane.setAlignment(body, Pos.CENTER);
						BorderPane.setAlignment(bottom, Pos.CENTER_RIGHT);
						BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
						BorderPane.setMargin(homeMenu, new Insets(12,12,12,12));
						BorderPane.setMargin(bottom, new Insets(12,12,12,12));
						BorderPane taxWindowLayout = new BorderPane();
						taxWindowLayout.setTop(menuMessage);
						taxWindowLayout.setCenter(body);
						taxWindowLayout.setBottom(bottom);
						taxWindowLayout.setStyle("-fx-background-color: #F2F1F0");
						
						// Create New Scene and Set it to the Window
						Scene newScene = new Scene(taxWindowLayout, 856,622);
						window.setScene(newScene);
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		search.setDefaultButton(true);
		
		if(bank.isAdmin()) {
			// Create Layout for this Scenario
			GridPane body = new GridPane();
			body.setAlignment(Pos.CENTER);
			body.setHgap(2);
			body.setVgap(2);
			body.setPadding(new Insets(0, 10, 0, 10));
			
			body.add(holderSINLabel, 0, 0);
			GridPane.setHalignment(holderSINLabel, HPos.RIGHT);
			body.add(holderSINInput, 1, 0);
			GridPane.setHalignment(holderSINInput, HPos.LEFT);
			body.add(search, 1, 1);
			GridPane.setHalignment(search, HPos.LEFT);
			
			BorderPane.setAlignment(homeMenu, Pos.CENTER_RIGHT);
			BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
			BorderPane.setMargin(homeMenu, new Insets(12,12,12,12));
			BorderPane taxWindowLayout = new BorderPane();
			taxWindowLayout.setTop(menuMessage);
			taxWindowLayout.setCenter(body);
			taxWindowLayout.setBottom(homeMenu);
			taxWindowLayout.setStyle("-fx-background-color: #F2F1F0");
			
			// Create New Scene and Set it to the Window
			displayScene = new Scene(taxWindowLayout, 856,622);
			window.setScene(displayScene);
		}
		else {			
			// Initialize Text based on the User logged in who is not an Admin
			holderNameText.setText(bank.getUserName());
			holderInfoText.setText(bank.getUserInfo(bank.getUsrSIN()));
			String accountsInfo = bank.getTaxableAccounts(bank.getUsrSIN());
			accountInfoText.setText(accountsInfo);
			
			// Create Layout for this Scenario
			VBox body = new VBox(20);
			body.setAlignment(Pos.CENTER);
			
			HBox numDate = new HBox(50);
			numDate.getChildren().addAll(statementNumText, statementDateText);
			
			body.getChildren().addAll(bankNameText, taxStatementText, numDate, holderNameText, holderInfoText, accountText, accountInfoText);
			
			ScrollPane page = new ScrollPane();
			page.setContent(body);
			
			// Declare and implement PRINT button
			Button print = new Button("PRINT");
			print.setOnAction(e1 -> {			
				print(body);
			});
			print.setDefaultButton(true);
			
			HBox bottom = new HBox(20);
			bottom.getChildren().addAll(print, homeMenu);
			
			BorderPane.setAlignment(homeMenu, Pos.CENTER_RIGHT);
			BorderPane.setAlignment(page, Pos.CENTER);
			BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
			BorderPane.setMargin(homeMenu, new Insets(12,12,12,12));
			BorderPane taxWindowLayout = new BorderPane();
			taxWindowLayout.setTop(menuMessage);
			taxWindowLayout.setCenter(page);
			taxWindowLayout.setBottom(bottom);
			taxWindowLayout.setStyle("-fx-background-color: #F2F1F0");
			
			// Create New Scene and Set it to the Window
			displayScene = new Scene(taxWindowLayout, 856,622);
			window.setScene(displayScene);
		}
	}
}
