package bank.common;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int SIN;
	private String fullName;
	private String email;
	private String password;
	private Date dateOfBirth;
	private boolean isAdmin;
	private boolean isAuthenticated;
	
	public User() throws ParseException {
		this(0,"","","",0,0,0,false,false);
	}
	
	public User(int SIN, String fullName, String email, String password, int year, int month, int day, boolean isAdmin, boolean isAuthenticated) throws ParseException {
		this.SIN = SIN;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		
		Calendar cal = new GregorianCalendar(year,month,day);
		long millis = cal.getTimeInMillis();
		dateOfBirth = new Date(millis);
		
		this.isAdmin = isAdmin;
		this.isAuthenticated = isAuthenticated;
	}
	
	public int getSIN() {
		return SIN;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}
	
	public boolean getIsAuthenticated() {
		return isAuthenticated;
	}
	
	public void setSIN(int SIN) {
		this.SIN = SIN;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public void setIsAuthenticatedTrue() {
		this.isAuthenticated = true;
	}
	
	public void setIsAuthenticatedFalse() {
		this.isAuthenticated = false;
	}
	
	public boolean authenticate() {
		setIsAuthenticatedTrue();
		return getIsAuthenticated();
	}
	
	@Override
	public String toString() {
		String output = "Date of Birth: " + this.getDateOfBirth() + System.lineSeparator();
		output += "SIN: " + this.getSIN() + System.lineSeparator();
		output += "Email Address: " + this.getEmail();
		
		return output;
	}
}
