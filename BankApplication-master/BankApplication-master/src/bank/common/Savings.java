/**
 * @title BankApplication
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 25, 2017
 * @param annualInterestRate BigDecimal holding the annual interest rate for the Account
 * @param interestIncome BigDecimal containing the money earned by the interest only
 * @param taxAmount BigDecimal holding the Tax Rate
 * @param finaBalance BigDecimal holding the final balance of the Account
 * 
 * The Java class Savings is a subclass of the Account class
 * and it implements the Taxable interface making it more
 * suitable for dealing with Accounts which have an interest rate.
 * It holds a serialVersionUID value so that it can be used in a
 * distributed application.
 * 
 */

package bank.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class Savings extends Account implements Taxable {
	// Declaring Variables
	private static final long serialVersionUID = 1L;
	private BigDecimal annualInterestRate, interestIncome, taxAmount, withdrawFee, withdrawFees;
	
	// Default Constructor
	public Savings() {
		this("","",0,0,0.10,0,0,0,0);	
	}
	
	// 4 argument constructor
	public Savings(String fullName, String number, double startingBalance, int UsrId, double interest, double fee, int year, int month, int day) {
		super(fullName, number, startingBalance, UsrId, year, month, day);
		
		if(interest >= 0.00)
			this.annualInterestRate = new BigDecimal(interest);
		else
			this.annualInterestRate = new BigDecimal(0.10);
		
		if(fee > 0)
			this.withdrawFee = new BigDecimal(fee);
		else
			this.withdrawFee = new BigDecimal(0.15);
		
		this.withdrawFees = new BigDecimal(0);
		this.interestIncome = new BigDecimal(0);
		this.taxAmount = new BigDecimal(0);
	}
	
	// Calculates hashCode for Account objects following Java conventions
	@Override
    public int hashCode() {
		double annualInterest = annualInterestRate.doubleValue();
		double InterestIncome = interestIncome.doubleValue();
		double tax = taxAmount.doubleValue();
		double fee = withdrawFee.doubleValue();
		double fees = withdrawFees.doubleValue();
		
        int hash = super.hashCode(); // Calls super class hashCode method as a starting point 
        hash = 31 * hash + (int)(Double.doubleToLongBits(annualInterest) ^ (Double.doubleToLongBits(annualInterest) >>> 32));
        hash = 31 * hash + (int)(Double.doubleToLongBits(InterestIncome) ^ (Double.doubleToLongBits(InterestIncome) >>> 32));
        hash = 31 * hash + (int)(Double.doubleToLongBits(tax) ^ (Double.doubleToLongBits(tax) >>> 32));
        hash = 31 * hash + (int)(Double.doubleToLongBits(fee) ^ (Double.doubleToLongBits(fee) >>> 32));
        hash = 31 * hash + (int)(Double.doubleToLongBits(fees) ^ (Double.doubleToLongBits(fees) >>> 32));
        
        return hash;
    }
	
	// Calculates the interestIncome and taxAmount according to standard accounting rules.
	@Override
	public void calculateTax(double taxRate) {
		BigDecimal balance = new BigDecimal(super.getBalance());
		BigDecimal rate = this.annualInterestRate.divide(new BigDecimal(100));
		BigDecimal t_rate = new BigDecimal(taxRate);
		BigDecimal value = new BigDecimal(50.00);
		
		this.interestIncome = balance.multiply(rate);
		if (this.interestIncome.compareTo(value) == 1) {
			rate = t_rate.divide(new BigDecimal(100));
			this.taxAmount = this.interestIncome.multiply(rate);
		}
	}
	
	// Returns a string containing a tax statement with detailed information.
	@Override
	public String createTaxStatement() {
		this.calculateTax(15);
		
		NumberFormat c = NumberFormat.getCurrencyInstance(Locale.CANADA);
		String income = c.format(this.interestIncome.doubleValue());
		String tax = c.format(this.taxAmount.doubleValue());
		
		StringBuffer output = new StringBuffer("Tax rate: 15%").append(System.lineSeparator());
		output.append("Account Number: ").append(super.getAccountNumber()).append(System.lineSeparator());
		output.append("Interest Income: ").append(income).append(System.lineSeparator());
		output.append("Amount of tax: ").append(tax).append(System.lineSeparator());
		
		return output.toString();
	}
	
	// Withdraw money from an account as long as the amount entered is positive and the current balance will be positive
	@Override
	public boolean withdraw(double amount) {
		if (amount < 0)
			return false;
		else {
			BigDecimal Amount = new BigDecimal(amount);
			if (Amount.compareTo(this.balance) == -1) { // Amount is less than the current balance
				this.balance = this.balance.subtract(Amount);
				this.withdrawFees = this.withdrawFees.add(this.withdrawFee);
				return true;
			}
			else
				return false;
		}
	}
	
	@Override
	public boolean deposit(double amount) {
		if(super.deposit(amount)) {
			this.calculateTax(15);
			return true;
		}
		return false;
	}
	
	// Returns the sum of the currentBalance and the interestIncome
	@Override
	public double getBalance() {
		this.calculateTax(15);
		
		BigDecimal bal = new BigDecimal(super.getBalance());
		BigDecimal sum = new BigDecimal(bal.add(this.interestIncome).subtract(withdrawFees).doubleValue());
		
		return sum.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns the taxAmount
	@Override
	public double getTaxAmount() {
		this.calculateTax(15);
		
		return taxAmount.doubleValue();
	}
	
	// Returns Interest Rate
	public double getAnnualInterestRate() {
		return this.annualInterestRate.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns Interest Income
	public double getInterestIncome() {
		this.calculateTax(15);
		
		return this.interestIncome.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns Interest Income
	public double getWithdrawFee() {
		return this.withdrawFee.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns Interest Income
	public double getWithdrawFees() {
		return this.withdrawFees.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Returns the type of Account
	@Override
	public String getType() {
		return "SAV";
	}
	
	/*
	 * Returns a formatted String containing useful information for the user.
	 * StringBuffer is used to allow faster concatenation.
	 */
	@Override
	public String toString() {
		NumberFormat c = NumberFormat.getCurrencyInstance(Locale.CANADA);
		String income = c.format(this.interestIncome.doubleValue());
		String balance = c.format(this.getBalance());
		
		StringBuffer output = new StringBuffer (super.toString());
		output.append("Type: SAV").append(System.lineSeparator());
		output.append("Interest Rate: ").append(annualInterestRate.doubleValue()).append("%" + System.lineSeparator());
		output.append("Interest Income: ").append(income).append(System.lineSeparator());
		output.append("Final Balance: ").append(balance).append(System.lineSeparator());
		return output.toString();
	}
}
