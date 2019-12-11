/**
 * @title BankApplication
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 27, 2017
 * @param bank RemoteBank object used for RMI application
 * 
 * RemoveBankClient is the main menu of the application.
 * Users will be able to select options from a GUI menu.
 * RemoteBankClient is responsible for connecting to RMI registry.
 * 
 */

package bank.client;

import java.rmi.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import bank.common.RemoteBank;

public class RemoteBankClient extends Application  implements EventHandler<ActionEvent>{
	// Declare Buttons, UI Elements and a RemoteBank object
	private Button register = new Button("Register");
	private Button logIn = new Button("Log In");
	private Button exit = new Button("Exit");
	
	private Stage window;
	private Scene home;
	
	RemoteBank bank;

	// launches the program
	public static void main(String[] args) {
		launch(args);
	}
	
	/*
	 * Connects to the RMI registry.
	 * Display the Main Window.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		Label helloMessage = new Label("Welcome to Cali Imperial Bank!");
		helloMessage.setFont(Font.font("Amble CN", FontWeight.BOLD, 26));
		register.setOnAction(this);
		logIn.setOnAction(this);
		exit.setOnAction(this);
		
		try{
			String url = "rmi://localhost:5678/";
			bank = (RemoteBank) Naming.lookup( url + "bank" );
			VBox homeLayout = new VBox(20);
			homeLayout.getChildren().addAll(helloMessage, register, logIn, exit);
			homeLayout.setAlignment(Pos.CENTER);
			homeLayout.setStyle("-fx-background-color: #F2F1F0");
			home = new Scene(homeLayout, 520, 286);
			
			window.setScene(home);
			window.setTitle("Cali Imperial Bank Client");
			window.setOnCloseRequest(e -> {
				e.consume(); // Will consume the request to close the program, so the last decision is ultimately made by the user answer
				boolean result = ConfirmBox.display("Close Program", "Are you sure you want to quit ?");
				if(result) {
					window.close();
				}
			});
			window.show();
		} catch (Exception e) {
			AlertBox.display("Error Message", e.toString());
		}
	}

	// Handles the menu options by redirecting to the correct method
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == register) {
			Register.display(window, bank);
		}
		else if (event.getSource() == logIn) {
			LogIn.display(window, bank, home);
		}
		else if (event.getSource() == exit) {
			boolean result = ConfirmBox.display("Close Program", "Are you sure you want to quit ?");
			if(result) {
				window.close();
			}
		}
	}
}
