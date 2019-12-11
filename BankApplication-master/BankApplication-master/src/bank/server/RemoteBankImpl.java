/**
 * @title BankApplication
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 25, 2017
 * @param bankName String containing the name of a bank
 * @param accounts An ArrayList containing various Account objects
 * 
 * RemoteBankImpl defines a class of type RemoteBankImpl which
 * extends UnicastRemoteObject and implements the interface RemoteBank.
 * 
 */

package bank.server;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import bank.common.Account;
import bank.common.GIC;
import bank.common.User;
import bank.common.RemoteBank;
import bank.common.Savings;

public class RemoteBankImpl extends UnicastRemoteObject implements RemoteBank {
	// Declaring Variables
	private static final long serialVersionUID = 1L;
	private String bankName;
	private ArrayList<Account> accounts;
	private User usr;
	private LogsIO log;
	private DBConnection connection;

	// Constructor
	public RemoteBankImpl(String bankName) throws RemoteException{
		if (bankName != null && bankName != "")
			this.bankName = bankName;
		else
			this.bankName = "Seneca@York";

		accounts = new ArrayList<Account>();
		try {
			usr = new User();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log = new LogsIO();
		connection = new DBConnection();
		try {
			connection.loadData(accounts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Returns the name of the Bank
	public String getBankName() {
		return bankName;
	}
	
	// Returns isAdmin for user
	public Boolean isAdmin() throws RemoteException {
		return usr.getIsAdmin();
	}
	
	// Returns isAdmin for user
	public int getUsrSIN() throws RemoteException {
		return usr.getSIN();
	}

	// Adds an Account to accounts only if the new object does not exist, yet.
	public boolean addAccount(Account obj) throws RemoteException, FileNotFoundException {
		boolean exists = false;

		if (obj == null) {
			return false;
		} else {
			for (int i = 0; i < accounts.size(); i++) {
				if (accounts.get(i).equals(obj)) {
					exists = true;
				}
			}

			if (exists) {
				return false;
			} else {
				if (accounts.add(obj) && connection.addAccount(obj)) {
					System.out.println("Account Successfully Added!");
					String description = "Account #" + obj.getAccountNumber() + " has been successfully added by "
							+ usr.getFullName() + " with ID " + usr.getSIN() + ".";
					String error = "Error: Cannot write to Excel file. Error occured when Adding Account #"
							+ obj.getAccountNumber();
					if (log.insertRow(description, error, usr)) {
						return true;
					} else {
						System.out.println("Error when writing to file");
						return false;
					}
				} else {
					System.out.println("Error when Adding Account");
					return false;
				}
			}
		}
	}

	// Removes an account from Accounts and returns it.
	public Account removeAccount(String accountNumber) throws RemoteException {
		Account tmp = null;

		if (accountNumber == null) {
			System.out.println("Invalid Account number!");
			return null;
		} else {
			for (int i = 0; i < accounts.size(); i++) {
				if (usr.getIsAdmin()) {
					if (accounts.get(i).getAccountNumber().equals(accountNumber)) {
						tmp = accounts.remove(i);
					}
				} else {
					if (accounts.get(i).getAccountNumber().equals(accountNumber)
							&& accounts.get(i).getFullName().equals(usr.getFullName())) {
						tmp = accounts.remove(i);
					}
				}
			}
		}

		if (tmp != null) {
			if(connection.deleteAccount(tmp)) {
				System.out.println("Account successfully removed!");
				String description = "Account #" + tmp.getAccountNumber() + " has been successfully removed by "
						+ usr.getFullName() + " with ID " + usr.getSIN() + ".";
				String error = "Error: Cannot write to Excel file. Error occured when Deleting Account #"
						+ tmp.getAccountNumber();
				log.insertRow(description, error, usr);
			}
		} else {
			System.out.println("Invalid Account number!");
		}

		return tmp;
	}

	// Searches an account by its account number
	public Account searchByAccountNumber(String number) throws RemoteException {
		Account tmp = null;

		if (usr.getIsAdmin()) {
			for (int i = 0; i < accounts.size(); i++) {
				if (accounts.get(i).getAccountNumber().equals(number)) {
					tmp = accounts.get(i);
				}
			}
		}
		else {
			for (int i = 0; i < accounts.size(); i++) {
				if (accounts.get(i).getAccountNumber().equals(number) && usr.getSIN() == accounts.get(i).getUsrSIN()) {
					tmp = accounts.get(i);
				}
			}
		}

		String description;
		String error = "Error: Cannot write to Excel file. Error occured when searching account #" + number;

		if (tmp != null)
			description = usr.getFullName() + " with ID " + usr.getSIN() + " has searched for account #" + number + ". The Account has been found.";
		else
			description = usr.getFullName() + " with ID " + usr.getSIN() + " has searched for account #" + number
					+ ". The Account has not been found.";

		System.out.println("Search for Accounts by Account Number completed. Returning result to client...");
		log.insertRow(description, error, usr);

		return tmp;
	}

	// Returns all the Accounts held by the same person as an array.
	public Account[] searchByAccountName(String accountName) throws RemoteException {
		int size = 0;
		Account tmp[] = new Account[size];

		if (accountName.isEmpty()) {
			return tmp;
		}

		if (usr.getIsAdmin()) {
			for (int i = 0; i < accounts.size(); i++) {
				if (accounts.get(i).getFullName().compareTo(accountName) == 0) {
					size++;
					tmp = Arrays.copyOf(tmp, tmp.length + 1);
					tmp[size - 1] = accounts.get(i);
				}
			}
		}
		
		System.out.println("Search for Accounts by Account Holder Name completed. Returning results to client...");
		
		String description;
		String error = "Error: Cannot write to Excel file. Error occured when searching accounts with holder's name: "
				+ accountName;

		if (tmp.length > 0)
			description = usr.getFullName() + " with ID " + usr.getSIN() + " has searched for all accounts with holder's name: " + accountName
					+ ". " + tmp.length + " Accounts have been found.";
		else
			description = usr.getFullName() + " with ID " + usr.getSIN() + " has searched for all accounts with holder's name: " + accountName
					+ ". 0 Accounts have been found.";
		log.insertRow(description, error, usr);

		return tmp;

	}
	
	// Searches an account by its account number and returns its index
	public int getAccountIndex(String number, boolean isAdmin) throws RemoteException {
		int index = -1;

		if (isAdmin) {
			for (int i = 0; i < accounts.size(); i++) {
				if (accounts.get(i).getAccountNumber().equals(number)) {
					index = i;
				}
			}
		}
		else {
			for (int i = 0; i < accounts.size(); i++) {
				if (accounts.get(i).getAccountNumber().equals(number) && usr.getSIN() == accounts.get(i).getUsrSIN()) {
					index = i;
				}
			}
		}
		
		return index;
	}

	public boolean withdraw(int index, double amount) throws RemoteException {
		boolean result = accounts.get(index).withdraw(amount);
		try {
			connection.updateObj(accounts.get(index));
		} catch (FileNotFoundException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if(result) {
			System.out.println("Money withdrew with success!");
			String description = usr.getFullName() + " has withdrawn $" + amount + "from account #" + accounts.get(index).getAccountNumber();
			String error = "Error: Cannot write to Excel file. Error occured when withdrawing $" + amount + "for user " + usr.getFullName();
			log.insertRow(description, error, usr);
		}
		else {
			System.out.println("Unuble to withdraw money from account #" + accounts.get(index).getAccountNumber() );
		}
		
		return result;
	}

	public boolean deposit(int index, double amount) throws RemoteException {
		boolean result = accounts.get(index).deposit(amount);
		if(result) {
			try {
				connection.updateObj(accounts.get(index));
			} catch (FileNotFoundException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			String description = usr.getFullName() + " has deposited $" + amount + "to the account #" + accounts.get(index).getAccountNumber() + ".";
			String error = "Error: Cannot write to Excel file. Error occured when depositing $" + amount + "for user " + usr.getFullName();
			log.insertRow(description, error, usr);
		}
		return result;
	}

	public boolean transfer(int index1, int index2, double amount) {
		boolean result = accounts.get(index1).withdraw(amount);
		if(result) {
			result = accounts.get(index2).deposit(amount);
			if(result) {
				String description = "Transfer of $" + amount + "between account #" + accounts.get(index1).getAccountNumber()
				+ " and account #" + accounts.get(index2).getAccountNumber()
				+ " has been successfully completed by " + usr.getFullName();
				String error = "Error: Cannot write to Excel file. Error occured when transferring money";
				log.insertRow(description, error, usr);
			}
		}
		return result;
	}

	public Account[] getAll() throws RemoteException {
		int size = 0;
		Account tmp[] = new Account[size];
		
		if(usr.getIsAdmin()) {
			for (int i = 0; i < accounts.size(); i++) {
				size++;
				tmp = Arrays.copyOf(tmp, tmp.length + 1);
				tmp[size - 1] = accounts.get(i);
			}
		}
		else {
			for (int i = 0; i < accounts.size(); i++) {
				if(accounts.get(i).getUsrSIN() == usr.getSIN()) {
					size++;
					tmp = Arrays.copyOf(tmp, tmp.length + 1);
					tmp[size - 1] = accounts.get(i);
				}
			}
		}

		String description;
		String error = "Error: Cannot write to Excel file. Error occured when searching for all Accounts";

		if (tmp.length > 0)
			description = "Searching for all accounts currently stored. " + tmp.length + " Accounts have been found. Returning result(s) to " + usr.getFullName() + " with ID " + usr.getSIN() + ".";
		else
			description = "Searching for all accounts currently stored. 0 Accounts have been found. Returning result(s) to " + usr.getFullName() + " with ID " + usr.getSIN() + ".";
		log.insertRow(description, error, usr);

		System.out.println("Search for all Accounts completed. Returning results to Admin...");
		
		return tmp;
	}
	
	public Account[] getAllByType(String type) throws RemoteException {
		int size = 0;
		Account tmp[] = new Account[size];
		
		if(usr.getIsAdmin()) {			
			for (int i = 0; i < accounts.size(); i++) {
				if(accounts.get(i).getType().compareToIgnoreCase(type) == 0) {
					size++;
					tmp = Arrays.copyOf(tmp, tmp.length + 1);
					tmp[size - 1] = accounts.get(i);
				}
			}
		}
		else {
			for (int i = 0; i < accounts.size(); i++) {
				if(accounts.get(i).getUsrSIN() == usr.getSIN() && accounts.get(i).getType().compareToIgnoreCase(type) == 0) {
					size++;
					tmp = Arrays.copyOf(tmp, tmp.length + 1);
					tmp[size - 1] = accounts.get(i);
				}
			}
		}

		String description;
		String error = "Error: Cannot write to Excel file. Error occured when searching for all Accounts";

		if (tmp.length > 0)
			description = "Searching for all accounts by type. " + tmp.length + " Accounts have been found. Returning result(s) to " + usr.getFullName() + " with ID " + usr.getSIN() + ".";
		else
			description = "Searching for all accounts by type. 0 Accounts have been found. Returning result(s) to " + usr.getFullName() + " with ID " + usr.getSIN() + ".";
		log.insertRow(description, error, usr);

		System.out.println("Search for all Accounts completed. Returning results to Admin...");
		
		return tmp;
	}
	
	public Account[] getAllBySIN(int sin) throws RemoteException {
		int size = 0;
		Account tmp[] = new Account[size];
		
		for (int i = 0; i < accounts.size(); i++) {
			if(accounts.get(i).getUsrSIN() == sin) {
				size++;
				tmp = Arrays.copyOf(tmp, tmp.length + 1);
				tmp[size - 1] = accounts.get(i);
			}
		}
		
		String description;
		String error = "Error: Cannot write to Excel file. Error occured when searching for all Accounts with the same SIN.";

		if (tmp.length > 0)
			description = "Searching for all accounts by SIN. " + tmp.length + " Accounts have been found. Returning result(s) to " + usr.getFullName() + " with SIN " + usr.getSIN() + ".";
		else
			description = "Searching for all accounts by SIN. 0 Accounts have been found. Returning result(s) to " + usr.getFullName() + " with SIN " + usr.getSIN() + ".";
		log.insertRow(description, error, usr);

		System.out.println("Search for all Accounts completed. Returning results to Admin...");
		
		return tmp;
	}
	
	public String getTaxableAccounts(int SIN) throws RemoteException {
		String output = "";	
		int count = 0;
		
		for (int i = 0; i < accounts.size(); i++) {
			if(accounts.get(i).getUsrSIN() == SIN) {
				if(accounts.get(i).getType().compareTo("GIC") == 0) {
					GIC tmp = (GIC)accounts.get(i);
					output += tmp.createTaxStatement() + System.lineSeparator();
					count++;
				}
				
				if(accounts.get(i).getType().compareTo("SAV") == 0) {
					Savings tmp = (Savings)accounts.get(i);
					output += tmp.createTaxStatement() + System.lineSeparator();
					count++;
				}
			}
		}
		
		String description;
		String error = "Error: Cannot write to Excel file. Error occured when searching for all Taxable Accounts";

		if (count > 0)
			description = "Searching for all Taxable Accounts. " + count + " Accounts have been found. Returning result(s) to " + usr.getFullName() + " with ID " + usr.getSIN() + ".";
		else
			description = "Searching for all Taxable Accounts. 0 Accounts have been found. Returning result(s) to " + usr.getFullName() + " with ID " + usr.getSIN() + ".";
		log.insertRow(description, error, usr);

		System.out.println("Search for all Taxable Accounts completed. Returning results to Admin...");
		
		return output;
	}
	
	public boolean register(int sin, String fullName, String email, String password, LocalDate dateOfBirth, boolean isAdmin) throws RemoteException {
		try {
			return connection.registerUser(sin, fullName, email, password, dateOfBirth, isAdmin);
		} catch (NoSuchAlgorithmException e) {
			
		}
		return false;
	}

	public boolean login(String email, String password) throws RemoteException, NoSuchAlgorithmException, FileNotFoundException {
		if (connection.loginUser(email, password, usr)) {
			if(usr.authenticate())
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
	
	
	public void logOut() throws RemoteException {
		usr.setSIN(0);
		usr.setEmail("");
		usr.setFullName("");
		usr.setPassword("");
		usr.setIsAuthenticatedFalse();
	}
	
	public boolean updateName(String newName) throws RemoteException {
		Boolean result = false;
		
		try {
			result =  connection.updateName(newName, usr);
			if(result)
				log.insertRow("User updated its own Profile Full Name", "Error: Cannot write to Excel file. Error occured when updating profile name.", usr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean updateEmail(String newEmail) throws RemoteException {
		Boolean result = false;
		
		try {
			result = connection.updateEmail(newEmail, usr);
			if(result)
				log.insertRow("User updated its own Profile Email", "Error: Cannot write to Excel file. Error occured when updating profile email.", usr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean updatePassword(String newPassword) throws RemoteException {
		boolean result = false;
		
		try {
			result = connection.updatePassword(newPassword, usr);
			if(result)
				log.insertRow("User updated its own Profile Password", "Error: Cannot write to Excel file. Error occured when updating profile password.", usr);
		} catch (FileNotFoundException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getUserName() throws RemoteException {
		return usr.getFullName();
	}
	
	public String getUserName(int SIN) throws RemoteException{
		try {
			User usr_  = this.connection.getUsrBySIN(SIN);
			
			if(usr_ != null)
				return usr_.getFullName();
			else
				return "";
		} catch (ParseException e) {
			return "";
		}
	}
	
	public String getUserInfo(int SIN) throws RemoteException {
		if(!this.usr.getIsAdmin())
			return this.usr.toString();
		else {
			try {
				User usr_  = this.connection.getUsrBySIN(SIN);
				
				if(usr_ != null)
					return usr_.toString();
				else
					return "";
			} catch (ParseException e) {
				return "";
			}
		}
	}
	
	public User getUserBySIN(int SIN) throws RemoteException {
		try {
			return connection.getUsrBySIN(SIN);
		} catch (ParseException e) {
			return null;
		}
	}
}
