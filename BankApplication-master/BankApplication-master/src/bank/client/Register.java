package bank.client;

import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Years;

import bank.common.RemoteBank;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Register {
	
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
	
	// Validate Full Name
	private static boolean validateFullName(String fullName) {
		Pattern p = Pattern.compile("[A-Z][a-zA-Z -]*[,][ ][A-Z][a-zA-Z -]+");
		Matcher m = p.matcher(fullName);
		
		if(m.find() && m.group().equals(fullName)) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Full Name");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a valid Full Name!");
			alert.showAndWait();
			
			return false;
		}
	}
	
	// Validate Email Address
	private static boolean validateEmail(String email) {
		Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(email);
		
		if(m.find() && m.group().equals(email)) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Email");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a valid Email Address!");
			alert.showAndWait();
			
			return false;
		}
	}
	
	// Validate Password
	private static boolean validatePassword(String pass1, String pass2) {
		if(pass1.compareTo(pass2) == 0) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Password");
			alert.setHeaderText(null);
			alert.setContentText("The Passwords Do Not Match");
			alert.showAndWait();
			
			return false;
		}
	}
	
	// Validate Age
	private static boolean validateAge(LocalDate date) {
		DateTime birthDate = new DateTime(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0);
		DateTime now = new DateTime();
		
		Years diff = Years.yearsBetween(birthDate, now);
		
		if(diff.getYears() >= 18) {
			return true;
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Date");
			alert.setHeaderText(null);
			alert.setContentText("You must be at least 18 years old to register an Account!");
			alert.showAndWait();
			
			return false;
		}
	}
	
	// displays the register window with a form users have to fill out if they wish to register an account
	public static void display(Stage window, RemoteBank bank) {
		// Declaring Scenes
		Scene home = window.getScene();
		Scene registerScene;
		
		// Declaring Labels
		Label menuMessage = new Label("New Profile Account Registation Form");
		menuMessage.setFont(Font.font("Amble CN", FontWeight.LIGHT, 20));
		Label fullName = new Label("Full Name: ");
		fullName.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label note = new Label("comma separated  ");
		note.setFont(Font.font("Amble CN", FontWeight.LIGHT, 12));
		Label sin = new Label("SIN: ");
		sin.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label note2 = new Label("only digits, no (-)  ");
		note2.setFont(Font.font("Amble CN", FontWeight.LIGHT, 12));
		Label email = new Label("Email Address: ");
		email.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label password = new Label("Password: ");
		password.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label confirmPassword = new Label("Confirm Password: ");
		confirmPassword.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label birthdate = new Label("Birthdate: ");
		birthdate.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		Label note3 = new Label("must be at least 18 years old of age  ");
		note3.setFont(Font.font("Amble CN", FontWeight.LIGHT, 12));
		Label isAdmin = new Label("is Admin ?: ");
		isAdmin.setFont(Font.font("Amble CN", FontWeight.LIGHT, 18));
		
		// Creating Input Fields
		ToggleGroup isAdminToggle = new ToggleGroup();
		
		RadioButton yes = new RadioButton("Yes");
		yes.setUserData("Yes");
		yes.setToggleGroup(isAdminToggle);
		RadioButton no = new RadioButton("No");
		no.setUserData("No");
		no.setToggleGroup(isAdminToggle); 
		no.setSelected(true);
		
		HBox admin = new HBox(20, yes, no);
				
		TextField nameInput = new TextField();
		TextField emailInput = new TextField();
		TextField sinInput = new TextField();
		PasswordField passInput = new PasswordField();
		PasswordField passConfirmInput = new PasswordField();
		
		// Create Date Picker
	    DatePicker birthdatePicker = new DatePicker(LocalDate.now());
	    Locale.setDefault(Locale.CANADA);
	    
	    // Declaring and Implementing Buttons
		Button mainMenu = new Button("Main Menu");
		mainMenu.setOnAction(e -> {
			window.setScene(home);
		});
		Button register = new Button("Register");
		register.setOnAction(e -> {			
			if(validateFullName(nameInput.getText()) && validateEmail(emailInput.getText()) && validateSIN(sinInput.getText()) && validatePassword(passInput.getText(), passConfirmInput.getText()) && validateAge(birthdatePicker.getValue())) {
				try {
					int sin_ = Integer.parseInt(sinInput.getText());
					boolean success = bank.register(sin_, nameInput.getText(), emailInput.getText(), passInput.getText(), birthdatePicker.getValue(), isAdminToggle.getSelectedToggle().getUserData().toString().equals("Yes"));
					if(success)
						AlertBox.display("User Registration", "User Account Successfully Registered!");
				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Invalid Email Address");
					alert.setHeaderText(null);
					alert.setContentText("Ops, the email address is already used by another User!");
					alert.showAndWait();
				}
			}
		});
		register.setDefaultButton(true);
		
		// Creating Initial Layout
		GridPane body = new GridPane();
		body.setAlignment(Pos.CENTER);
		body.setHgap(10);
		body.setVgap(2);
		body.setPadding(new Insets(0, 10, 0, 10));
		
		body.add(fullName, 0, 0);
		GridPane.setHalignment(fullName, HPos.RIGHT);
		body.add(nameInput, 1, 0);
		GridPane.setHalignment(nameInput, HPos.LEFT);
		body.add(note, 0, 1);
		GridPane.setHalignment(note, HPos.RIGHT);
		body.add(sin, 0, 2);
		GridPane.setHalignment(sin, HPos.RIGHT);
		body.add(sinInput, 1, 2);
		GridPane.setHalignment(sinInput, HPos.LEFT);
		body.add(note2, 0, 3);
		GridPane.setHalignment(note2, HPos.RIGHT);
		body.add(email, 0, 4);
		GridPane.setHalignment(email, HPos.RIGHT);
		body.add(emailInput, 1, 4);
		GridPane.setHalignment(emailInput, HPos.LEFT);
		body.add(password, 0, 5);
		GridPane.setHalignment(password, HPos.RIGHT);
		body.add(passInput, 1, 5);
		GridPane.setHalignment(passInput, HPos.LEFT);
		body.add(confirmPassword, 0, 6);
		GridPane.setHalignment(confirmPassword, HPos.RIGHT);
		body.add(passConfirmInput, 1, 6);
		GridPane.setHalignment(passConfirmInput, HPos.LEFT);
		body.add(birthdate, 0, 7);
		GridPane.setHalignment(birthdate, HPos.RIGHT);
		body.add(birthdatePicker, 1, 7);
		GridPane.setHalignment(birthdatePicker, HPos.LEFT);
		body.add(note3, 0, 8);
		GridPane.setHalignment(note3, HPos.RIGHT);
		body.add(isAdmin, 0, 9);
		GridPane.setHalignment(isAdmin, HPos.RIGHT);
		body.add(admin, 1, 9);
		GridPane.setHalignment(admin, HPos.LEFT);
		body.add(register, 1, 10);
		GridPane.setHalignment(register, HPos.LEFT);
		
		BorderPane.setAlignment(mainMenu, Pos.CENTER_RIGHT);
		BorderPane.setMargin(menuMessage, new Insets(12,12,12,12));
		BorderPane.setMargin(mainMenu, new Insets(12,12,12,12));
		BorderPane registerProfileLayout = new BorderPane();
		registerProfileLayout.setTop(menuMessage);
		registerProfileLayout.setCenter(body);
		registerProfileLayout.setBottom(mainMenu);
		registerProfileLayout.setStyle("-fx-background-color: #F2F1F0");
		
		// Create New Scene and Set it to the Window
		registerScene = new Scene(registerProfileLayout, 520,386);
		window.setScene(registerScene);
	}
}