/**
 * @title BankApplication
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 25, 2017
 * 
 * The RemoteBank interface is used to implement RMI functionalities.
 * It has methods which are implemented in RemoteBankImpl.java.
 * The methods allow users to add accounts, remove accounts and
 * search accounts inside on a RemoteBank object.
 * More details about the methods are discussed in the implementation file.
 */

package bank.common;

import java.io.FileNotFoundException;
import java.rmi.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public interface RemoteBank extends Remote {
	public Boolean isAdmin() throws RemoteException;
	public int getUsrSIN() throws RemoteException;
	public boolean addAccount(Account obj) throws RemoteException, FileNotFoundException;
	public Account removeAccount(String accountNumber) throws RemoteException;
	public Account searchByAccountNumber(String number) throws RemoteException;
	public Account[] searchByAccountName(String accountName) throws RemoteException;
	public Account[] getAll() throws RemoteException;
	public Account[] getAllByType(String type) throws RemoteException;
	public Account[] getAllBySIN(int sin) throws RemoteException;
	public String getTaxableAccounts(int SIN) throws RemoteException;
	public int getAccountIndex(String type, boolean isAdmin) throws RemoteException;
	public boolean withdraw(int index, double amount) throws RemoteException;
	public boolean deposit (int index, double amount) throws RemoteException;
	public boolean transfer(int index1, int index2, double amount) throws RemoteException;
	public boolean register(int sin, String fullName, String email, String password, LocalDate dateOfBirth, boolean isAdmin) throws RemoteException;
	public boolean login(String email, String password) throws RemoteException, NoSuchAlgorithmException, FileNotFoundException;
	public boolean updateName(String newName) throws RemoteException;
	public boolean updateEmail(String newEmail) throws RemoteException;
	public boolean updatePassword(String newPassword) throws RemoteException;
	public void logOut() throws RemoteException;
	public String getUserName() throws RemoteException;
	public String getUserName(int SIN) throws RemoteException;
	public String getUserInfo(int SIN) throws RemoteException;
	public User getUserBySIN(int SIN) throws RemoteException;
}