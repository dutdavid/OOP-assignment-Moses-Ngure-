/**
 * @author Rosario Alessandro Cali
 * @title BankApplication
 * @date April 25, 2017
 * 
 * The Java class Chequing is a subclass of the Account class.
 * The class is very suitable for Chequing account because it provides
 * additional characteristics such as max number of transactions,
 * transactions fees and a basic record of all the transactions made.
 * suitable for dealing with Accounts which have an interest rate. 
 */

package bank.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public class Chequing extends Account {
	// Declaring Variables
	private static final long serialVersionUID = 1L;
	private BigDecimal transactionFee, totalTransactionFees;
	private double[] transactions;
	private int maxTransactions, transactionTracker;
	
	// Default Constructor
	public Chequing() {
		this("","",0,0,0.25,3,0,0,0);	
	}
	
	// 5 argument constructor
	public Chequing(String fullName, String number, double currentBalance, int usrId, double serviceCharge, int max, int year, int month, int day) {
		super(fullName, number, currentBalance, usrId, year, month, day);
		
		if(serviceCharge >= 0)
			transactionFee = new BigDecimal(serviceCharge);
		else
			transactionFee = new BigDecimal(0.25);
		if(max > 0)
			maxTransactions = max;
		else
			maxTransactions = 3;
		
		transactions = new double[maxTransactions];
		transactionTracker = 0;
		totalTransactionFees = new BigDecimal(0);
		
		this.resetTransactionsDetails();
	}
	
	// Calculates hashCode for Account objects following Java conventions
	@Override
    public int hashCode() {
		double fee = this.transactionFee.doubleValue();
		double total = this.totalTransactionFees.doubleValue();
		
        int hash = super.hashCode();
        hash = 31 * hash + (int)(Double.doubleToLongBits(fee) ^ (Double.doubleToLongBits(fee) >>> 32));
        hash = 31 * hash + (int)(Double.doubleToLongBits(total) ^ (Double.doubleToLongBits(total) >>> 32));
        hash = 31 * hash + this.transactions.hashCode();
        hash = 31 * hash + this.maxTransactions;
        hash = 31 * hash + this.transactionTracker;
        
        return hash;
    }
	
	/*
	 * This override of the withdraw method allows to keep a record of all the transactions made and
	 * stops the user to go over the limit of maxTransactions.
	 */
	@Override
	public boolean withdraw(double amount) {
		if (amount < 0)
			return false;
		else {
			BigDecimal Amount = new BigDecimal(amount);
			if (Amount.compareTo(this.balance) == -1 && this.transactionTracker < this.maxTransactions) { // Amount is less than the current balance
				if(transactionTracker < maxTransactions) {
					this.balance.subtract(Amount);
					this.totalTransactionFees = this.totalTransactionFees.add(transactionFee);
					this.transactions[transactionTracker++] = - amount;
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
	}
	
	//This override of the deposit method stops the user to go over the limit of maxTransactions.
	@Override
	public boolean deposit (double amount) {
		if (amount > 0 && this.transactionTracker < this.maxTransactions) {
			BigDecimal Amount = new BigDecimal(amount);
			this.balance = this.balance.add(Amount);
			this.totalTransactionFees = this.totalTransactionFees.add(transactionFee);
			this.transactions[this.transactionTracker++] = amount;
			return true;
		}
		return false;
	}
	
	// Returns the total of the currentBalance minus the totalTransactionFees
	@Override
	public double getBalance() {
		BigDecimal bal = new BigDecimal(super.getBalance());
		BigDecimal total = new BigDecimal(bal.subtract(this.totalTransactionFees).doubleValue());
		return total.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns the type of Account
	@Override
	public String getType() {
		return "CHQ";
	}
	
	// Returns the value of the charged fees
	public double getTransactionFee() {
		return this.transactionFee.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns the total amount of charged fees
	public double getTotalTransactionFees() {
		return this.totalTransactionFees.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns the total number of transactions allowed
	public int getMaxTransactions() {
		return this.maxTransactions;
	}
	
	// Returns the transactions history
	public String getTransactions() {
		DecimalFormat fmt = new DecimalFormat("+#,##0.00;-#");
		
		StringBuffer output = new StringBuffer();
		
		if(this.transactionTracker == 0) {
			output.append("<NONE>");
		}
		else {
			for(int i = 0; i < transactionTracker; i++) {
				output.append(fmt.format(transactions[i]));
				if(i != transactionTracker -1)
					output.append(", ");
			}
		}
		
		return output.toString();
	}
	
	// Reset transaction variables
	public void resetTransactionsDetails() {
		LocalDate today = LocalDate.now();
		Period period = Period.between(super.openedDate, today);
		
		if(period.getMonths() >= 1) {
			for(int i = 0; i < this.transactionTracker; i++) {
				this.transactions[i] = 0;
			}
			this.transactionTracker = 0;
		}
	}
	
	/*
	 * Returns a formatted String containing useful information for the user.
	 * StringBuffer is used to allow faster concatenation.
	 */
	@Override
	public String toString() {
		NumberFormat c = NumberFormat.getCurrencyInstance(Locale.CANADA);
		String charge = c.format(this.transactionFee.doubleValue());
		String totalCharges = c.format(this.totalTransactionFees.doubleValue());
		String balance = c.format(this.getBalance());
		DecimalFormat fmt = new DecimalFormat("+#,##0.00;-#");
		
		StringBuffer output = new StringBuffer (super.toString());
		output.append("Type: CHQ").append(System.lineSeparator());
		output.append("Service Change: ").append(charge).append(System.lineSeparator());
		output.append("Total Service Charges: ").append(totalCharges).append(System.lineSeparator());
		output.append("Number of Transactions Allowed: ").append(maxTransactions).append(System.lineSeparator());
		output.append("List of Transactions: ");
		for(int i = 0; i < transactionTracker; i++) {
			output.append(fmt.format(transactions[i]));
			if(i != transactionTracker -1)
				output.append(", ");
		}
		output.append(System.lineSeparator());
		output.append("Final Balance: ").append(balance).append(System.lineSeparator());
		
		return output.toString();
	}
}
