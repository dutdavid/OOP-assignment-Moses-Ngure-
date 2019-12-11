/**
 * @title BankApplication
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 25, 2017
 * 
 * The Java class Account is a simple super class that
 * provides common functionalities to all its derived classes.
 * Note that the Account class and all of its derived classes
 * use BigDeciaml data types to accomplish calculations where
 * decimal points are important.
 */

package bank.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

public abstract class Account implements Serializable {
	// Variables
	private static final long serialVersionUID = 1L;
	private String fullName,firstName, lastName, accountNumber;
	private int usrSIN;
	protected BigDecimal balance;
	protected LocalDate openedDate;	
	
	// Default Constructor
	public Account() {
		this("","",0,0,0,0,0);
	}
	
	// 3 argument constructor, accountNUmber can only be alphanumeric and balance must be 0 or positive
	public Account(String fullName, String number, double balance, int usrSIN, int year, int month, int day) {
		String[] parts;
		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		boolean hasSpecialChar = p.matcher(number).find();
		
		parts = fullName.split(", ");
		
		if(fullName != null && parts.length == 2) {
			
			this.fullName = fullName;
			this.firstName = parts[1];
			this.lastName = parts[0];
			
		} else {
			this.fullName = "";
			this.firstName = "";
			this.lastName = "";
		}
		
		if(!hasSpecialChar)
			this.accountNumber = number;
		else
			this.accountNumber = "";
		
		if (balance > 0)
			this.balance = new BigDecimal(balance);
		else
			this.balance = new BigDecimal(0);
		
		this.usrSIN = usrSIN;
		
		if(year == 0 && month == 0 && day == 0)
			openedDate = LocalDate.of(year, month, day);
		else
			openedDate = LocalDate.now();
	}
	
	// Calculates hashCode for Account objects following Java conventions
	@Override
    public int hashCode() {
		double bal = this.balance.doubleValue();
        int hash = 18;
        hash = 31 * hash + this.fullName.hashCode();
        hash = 31 * hash + this.firstName.hashCode();
        hash = 31 * hash + this.lastName.hashCode();
        hash = 31 * hash + this.accountNumber.hashCode();
        hash = 31 * hash + (int)(Double.doubleToLongBits(bal) ^ (Double.doubleToLongBits(bal) >>> 32));
        return hash;
    }
	
	// Overriding the equal method to provide an appropriate comparison by value
	@Override
	public boolean equals(Object a)
	{
		if (a == null)
			return false;
		
		if (this == a)
			return true;
				
		if(a instanceof Account)
		{
			Account a2 = (Account) a; 
			
			if((this.fullName.equals(a2.fullName)) && (this.accountNumber.equals(a2.accountNumber)) && this.usrSIN == a2.usrSIN)
				return true;
		}
		
		return false;
	}
	
	// Withdraw money from an account as long as the amount entered is positive and the current balance will be positive
	public boolean withdraw(double amount) {
		if (amount < 0)
			return false;
		else {
			BigDecimal Amount = new BigDecimal(amount);
			if (Amount.compareTo(this.balance) == -1) { // Amount is less than the current balance
				this.balance = this.balance.subtract(Amount);
				return true;
			}
			else
				return false;
		}
	}
	
	// Deposit money as long as the amount entered is positive
	public boolean deposit (double amount) {
		if (amount > 0) {
			this.balance = this.balance.add(new BigDecimal(amount));
			return true;
		}
		return false;
	}
	
	// Returns null because it is a general class
	public String getType() {
		return null;
	}
	
	// Returns fullName
	public String getFullName() {
		return this.fullName;
	}
	
	// Returns firstName
	public String getFirstName() {
		return this.firstName;		
	}
	
	// Returns lastName
	public String getLastName() {
		return this.lastName;	
	}
	
	// Returns accountNumber
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	// Returns accountNumber
	public int getUsrSIN() {
		return this.usrSIN;
	}
	
	// Returns the currentBalance
	public double getBalance() {
		return this.balance.doubleValue();
	}
	
	// Returns the openedDate
	public String getOpenedDate() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy MMM d");
		return this.openedDate.format(format);
	}
	
	/*
	 * Returns a formatted String containing useful information for the user.
	 * StringBuffer is used to allow faster concatenation.
	 */
	@Override
	public String toString() {
		NumberFormat c = NumberFormat.getCurrencyInstance(Locale.CANADA);
		String balance = c.format(this.balance.doubleValue());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		String formattedString = this.openedDate.format(formatter);
		
		StringBuffer output = new StringBuffer ("Name: ").append(this.fullName).append(System.lineSeparator());
		output.append("Number: ").append(this.accountNumber).append(System.lineSeparator());
		output.append("Current Balance: ").append(balance).append(System.lineSeparator());
		output.append("Open Date: ").append(formattedString).append(System.lineSeparator());
		return output.toString();
	}
}
