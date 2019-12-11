/**
 * @title BankApplication
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 25, 2017
 * 
 * Taxable is a Java interface which is implemented only
 * by certain Account sub-classes such as Savings and GIC.
 * 
 */

package bank.common;

public interface Taxable {
	void calculateTax(double taxRate);
	double getTaxAmount();
	String createTaxStatement();
}