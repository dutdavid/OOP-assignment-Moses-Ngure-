/**
 * @author Rosario Alessandro Cali
 * @title BankApplication
 * @date April 25, 2017
 * 
 * The Java class GIC is a subclass of the Account class
 * and it implements the Taxable interface making it more
 * suitable for dealing with Accounts which have an interest rate
 * and a maturity.
 */

package bank.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public class GIC extends Account implements Taxable {
	// Declaring Variables
	private static final long serialVersionUID = 1L;
	private int years;
	private BigDecimal annualInterestRate, interestIncome, taxAmount, balanceAtMaturity;
	
	// Default constructor
	public GIC() {
		this("","",0,0,1,1.25,0,0,0);
	}
	
	// 5 argument constructor
	public GIC(String fullName, String number, double currentBalance, int usrId, int yrs, double interest, int year, int month, int day) {
		super(fullName, number, currentBalance, usrId, year, month, day);
		
		if(yrs >= 0)
			years = yrs;
		else
			years = 1;
		if(interest >= 0.00)
			this.annualInterestRate = new BigDecimal(interest);
		else
			this.annualInterestRate = new BigDecimal(1.25);
		
		this.balanceAtMaturity = new BigDecimal(0);
		this.calculateTax(15);
	}
	
	// Calculates hashCode for Account objects following Java conventions
	@Override
    public int hashCode() {
		double annualInterest = annualInterestRate.doubleValue();
		double InterestIncome = interestIncome.doubleValue();
		double tax = taxAmount.doubleValue();
        int hash = super.hashCode();
        hash = 31 * hash + (int)(Double.doubleToLongBits(annualInterest) ^ (Double.doubleToLongBits(annualInterest) >>> 32));
        hash = 31 * hash + (int)(Double.doubleToLongBits(InterestIncome) ^ (Double.doubleToLongBits(InterestIncome) >>> 32));
        hash = 31 * hash + (int)(Double.doubleToLongBits(tax) ^ (Double.doubleToLongBits(tax) >>> 32));
        hash = 31 * hash + this.years;
        return hash;
    }
	
	// Empty because this operation is not allowed on this type of Account
	@Override
	public boolean withdraw(double amount) {
		LocalDate today = LocalDate.now();
		Period period = Period.between(super.openedDate, today);
		
		if(period.getYears() >= years) {
			if (amount < 0)
				return false;
			else {
				this.balance = this.balanceAtMaturity;
				BigDecimal Amount = new BigDecimal(amount);
				if (Amount.compareTo(this.balance) == -1) { // Amount is less than the current balance
					this.balance = this.balance.subtract(Amount);
					return true;
				}
				else
					return false;
			}
		}
		return false;
	}
	
	// Empty because this operation is not allowed on this type of Account
	@Override
	public boolean deposit (double amount) {
		return false;
	}
	
	// Returns the balanceAtMaturiy
	@Override
	public double getBalance() {
		return this.balanceAtMaturity.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	// Return Annual Interest Rate
	public double getAnnualInterestRate() {
		return this.annualInterestRate.doubleValue();
	}
		
	// Returns Period of Investiment
	public int getYears() {
		return this.years;
	}
		
	// Return Annual Interest Rate
	public double getInterestIncome() {
		return this.interestIncome.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	/*
	 * Calculates the balanceAtMaturity, interestIncome and taxAmount according to
	 * standard accounting rules.
	 */
	@Override
	public void calculateTax(double taxRate) {
		BigDecimal t_rate = new BigDecimal(taxRate).divide(new BigDecimal(100),2, BigDecimal.ROUND_HALF_UP);
		
		BigDecimal rate = this.annualInterestRate.divide(new BigDecimal(100)).add(new BigDecimal(1)).pow(this.years);
		this.balanceAtMaturity = super.balance.multiply(rate);
		
		this.interestIncome = this.balanceAtMaturity.subtract(this.balance);
		this.taxAmount = this.interestIncome.multiply(t_rate);
	}

	// Returns the taxAmount
	@Override
	public double getTaxAmount() {
		return taxAmount.doubleValue();
	}

	// Returns a string containing a tax statement with detailed information.
	@Override
	public String createTaxStatement() {
		NumberFormat c = NumberFormat.getCurrencyInstance(Locale.CANADA);
		String income = c.format(this.interestIncome.doubleValue());
		String tax = c.format(this.taxAmount.doubleValue());
		
		StringBuffer output = new StringBuffer("Tax rate: 15%").append(System.lineSeparator());
		output.append("Account Number: ").append(super.getAccountNumber()).append(System.lineSeparator());
		output.append("Interest Income: ").append(income).append(System.lineSeparator());
		output.append("Amount of tax: ").append(tax).append(System.lineSeparator());
		
		return output.toString();
	}
	
	// Returns the type of Account
	@Override
	public String getType() {
		return "GIC";
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
		output.append("Type: GIC").append(System.lineSeparator());
		output.append("Annual Interest Rate: ").append(annualInterestRate.doubleValue()).append("%" + System.lineSeparator());
		output.append("Period of Investement: ").append(years).append(" years").append(System.lineSeparator());
		output.append("Interest Income at Maturity: ").append(income).append(System.lineSeparator());
		output.append("Balance at Maturity: ").append(balance).append(System.lineSeparator() + System.lineSeparator());
		return output.toString();
	}
}
