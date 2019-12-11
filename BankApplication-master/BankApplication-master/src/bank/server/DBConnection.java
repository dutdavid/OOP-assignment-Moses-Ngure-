package bank.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import bank.common.Account;
import bank.common.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DBConnection {
	private Connection dbConnection;
	
	public DBConnection() {	}
	
	public boolean connectDB() throws FileNotFoundException {
		File creds = new File("credentials");
		FileReader fileReader = new FileReader(creds);
		BufferedReader bufferReader = new BufferedReader(fileReader);

		String username = null;
		String password = null;
		try {
			username = bufferReader.readLine();
			password = bufferReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			bufferReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			if(username != null && password != null) {
				dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", username, password);
				if(dbConnection != null) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				System.out.println("Invalid username and/or password. Could not connect to the Database");
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return false;
	}
	
	public boolean registerUser(int sin, String fullName, String email, String password, LocalDate dateOfBirth, boolean isAdmin) throws NoSuchAlgorithmException {
		PreparedStatement myStmt = null;
		
		try {
			if(connectDB()) {
				// prepare statement
				myStmt = dbConnection.prepareStatement("insert into users"
						+ " (sin, fullName, email, password, dateOfBirth, isAdmin)"
						+ " values (?, ?, ?, ?, ?, ?)");
				
				// Convert LocalDate to MySql Date
				Calendar cal = new GregorianCalendar(dateOfBirth.getYear(),dateOfBirth.getMonthValue(),dateOfBirth.getDayOfMonth());
				long millis = cal.getTimeInMillis();
				Date newDateOfBirth = new Date(millis);
				
				// set parameters
				myStmt.setInt(1, sin);
				myStmt.setString(2, fullName);
				myStmt.setString(3, email);
				myStmt.setString(4, encryptPass(password));
				myStmt.setDate(5, newDateOfBirth);
				myStmt.setBoolean(6, isAdmin);
					
				// execute SQL statement
				myStmt.executeUpdate();
			}
		} catch(FileNotFoundException | SQLException e) {
			
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Invalid Email Address");
				alert.setHeaderText(null);
				alert.setContentText("Ops, the email address is already used by another User!");
				alert.showAndWait();
		}
		finally {
			try {
				myStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(closeConnection())
			return true;
		else
			return false;
	}
	
	public User getUsrBySIN(int SIN) throws ParseException {
		User user = new User();
		
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("SELECT * FROM users WHERE sin = " + SIN);
					
				// query and fetch results
				ResultSet result = myStmt.executeQuery();
				
				// store results to usr, if any found
				if(!result.wasNull()) {
					result.next();
					user.setSIN(result.getInt("sin"));
					user.setFullName(result.getString("fullName"));
					user.setEmail(result.getString("email"));
					user.setPassword(result.getString("password"));
					user.setDateOfBirth(result.getDate("dateOfBirth"));
					user.setIsAdmin(result.getBoolean("isAdmin"));
				}
				
				// close statement, resultSet and connection
				myStmt.close();
				result.close();
				closeConnection();
				
				return user;
			}
		} catch(SQLException | IOException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid SIN!");
			alert.setHeaderText(null);
			alert.setContentText("Ops, the SIN does not match with any Account!");
			alert.showAndWait();
		}
		
		return null;
	}
	
	public boolean loginUser(String email, String password, User usr) throws NoSuchAlgorithmException, FileNotFoundException {
		String encrypted = encryptPass(password);
		
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("SELECT * FROM users WHERE email='" + email + "' AND password ='" + encrypted + "'");
					
				// query and fetch results
				ResultSet result = myStmt.executeQuery();
				
				// store results to usr, if any found
				if(!result.wasNull()) {
					result.next();
					usr.setSIN(result.getInt("sin"));
					usr.setFullName(result.getString("fullName"));
					usr.setEmail(result.getString("email"));
					usr.setPassword(result.getString("password"));
					usr.setDateOfBirth(result.getDate("dateOfBirth"));
					usr.setIsAdmin(result.getBoolean("isAdmin"));
				}
				
				// close statement and resultSet
				myStmt.close();
				result.close();
			}
		} catch(SQLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Email Address");
			alert.setHeaderText(null);
			alert.setContentText("Ops, the email address is already used by another User!");
			alert.showAndWait();
		}
		
		if(closeConnection())
			return true;
		else
			return false;
	}
	
	public boolean loadData(ArrayList<Account> accounts) throws FileNotFoundException {
		
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("SELECT * FROM accounts");
					
				// query and fetch results
				ResultSet results = myStmt.executeQuery();
				
				// store results to usr, if any found
				if(!results.wasNull()) {
					while(results.next()) {
						ByteArrayInputStream bais;
			            ObjectInputStream ins;
						
			            bais = new ByteArrayInputStream(results.getBytes(2));
			            ins = new ObjectInputStream(bais);
						
						Account mc =(Account)ins.readObject();
			            
			            accounts.add(mc);
			            
			            bais.close();
			            ins.close();
					}
				}
				
				// close statement and resultSet
				myStmt.close();
				results.close();
			}
		} catch(SQLException | IOException | ClassNotFoundException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Email Address");
			alert.setHeaderText(null);
			alert.setContentText("Ops, the email address is already used by another User!");
			alert.showAndWait();
		}
		
		if(closeConnection())
			return true;
		else
			return false;
	}
	
	public boolean updateName(String newName, User usr) throws FileNotFoundException {
		
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("UPDATE users SET fullName = '" + newName + "' WHERE id = " + usr.getSIN());
					
				// execute SQL statement
				myStmt.executeUpdate();
				
				// close statement
				myStmt.close();
				
				// update object
				usr.setFullName(newName);
			}
		} catch(SQLException e) { }
		
		if(closeConnection())
			return true;
		else
			return false;		
	}
	
	public boolean updateEmail(String newEmail, User usr) throws FileNotFoundException {
		
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("UPDATE users SET email = '" + newEmail + "' WHERE id = " + usr.getSIN());
					
				// execute SQL statement
				myStmt.executeUpdate();
				
				// close statement
				myStmt.close();
				
				// update object
				usr.setEmail(newEmail);
			}
		} catch(SQLException e) { }
		
		if(closeConnection())
			return true;
		else
			return false;		
	}
	
	public boolean updatePassword(String newPassword, User usr) throws FileNotFoundException, NoSuchAlgorithmException {
		String encrypted = encryptPass(newPassword);
		
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("UPDATE users SET password = '" + encrypted + "' WHERE id = " + usr.getSIN());
					
				// execute SQL statement
				myStmt.executeUpdate();
				
				// close statement
				myStmt.close();
				
				// update object
				usr.setPassword(encrypted);
			}
		} catch(SQLException e) { }
		
		if(closeConnection())
			return true;
		else
			return false;		
	}
	
	public boolean updateObj(Account obj) throws FileNotFoundException, NoSuchAlgorithmException {
		
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("UPDATE accounts SET account = ? WHERE accountNum = " + obj.getAccountNumber());
				
				// set parameters
				myStmt.setObject(1, obj);
					
				// execute SQL statement
				myStmt.executeUpdate();
				
				// close statement
				myStmt.close();
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
		
		if(closeConnection())
			return true;
		else
			return false;		
	}
	
	public boolean addAccount(Account obj) throws FileNotFoundException {
		PreparedStatement myStmt = null;
		
		try {
			if(connectDB()) {
				// prepare statement
				myStmt = dbConnection.prepareStatement("INSERT INTO accounts (accountNum, account) values (?, ?)");

					
				// set parameters
				myStmt.setString(1, obj.getAccountNumber());
				myStmt.setObject(2, obj);
					
				// execute SQL statement
				myStmt.executeUpdate();
				
				// close statement
				myStmt.close();
			}
		} catch(SQLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Account Number");
			alert.setHeaderText(null);
			alert.setContentText("Ops, the account number is already used by another Customer!");
			alert.showAndWait();
		}
		
		if(closeConnection())
			return true;
		else
			return false;	
	}
	
	public boolean closeConnection() {
		try {
			dbConnection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteAccount(Account obj) {
		try {
			if(connectDB()) {
				// prepare statement
				PreparedStatement myStmt = dbConnection.prepareStatement("DELETE FROM accounts WHERE accountNum = '" + obj.getAccountNumber() + "'");
					
				// execute SQL statement
				myStmt.executeUpdate();
				
				// close statement
				myStmt.close();;
			}
		} catch(SQLException | FileNotFoundException e) { }
		
		if(closeConnection())
			return true;
		else
			return false;	
	}
	
	public String encryptPass(String password) throws NoSuchAlgorithmException {
		// Convert the Password to Bytes
		byte[] pass = password.getBytes();
		// Create a MessageDigest object using the SHA-256 algorithm
		MessageDigest SHA256;
		SHA256 = MessageDigest.getInstance("SHA-256");
		// Compute digest
		SHA256.update(pass);
		// Return digest
		byte[] digest = SHA256.digest();
		// Copy the digest to an Hex format
		StringBuffer hexDigest = new StringBuffer();
		for(int i = 0; i < digest.length; i++) {
			hexDigest.append(Integer.toString((digest[i]&0xff)+0x100,16).substring(1));
		}
		
		return hexDigest.toString();
	}
}
