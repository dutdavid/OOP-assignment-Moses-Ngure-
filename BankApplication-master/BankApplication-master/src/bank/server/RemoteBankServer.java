/**
 * @title Assignment 2
 * @author Rosario Alessandro Cali
 * @version 1.0
 * @date April 9, 2017
 * @param bank Object of type RemoteBankImpl which is being binded into the RMI registry
 * 
 * RemoteBankServer defines a class of type RemoteBankServer which has an Object of type RemoteBankImpl
 * which is binded into the RMI registry and used by the Clients that connect to this server.
 * 
 */

package bank.server;

public class RemoteBankServer {
	public static void main(String[] args) {

	      try {
	    	  System.out.println( "Starting the Seneca@York bank server..." );
	    	  
	    	  RemoteBankImpl bank = new RemoteBankImpl("Seneca@York");

	    	  // Creating an RMI registry using the programmatically method
	    	  java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.createRegistry(5678);
			
	    	  // Binding the RemoteBankImpl object to the RMI registry just created above
	    	  // Waiting for clients to use the new Remote object
	    	  registry.rebind("bank", bank);
	    	  
	    	  System.out.println("Remote objects have been binded to RMI registry");
	    	  
	    	  System.out.println("Remote objects are waiting for clients...");
	    	  
	    	  System.out.println( "Welcome to Seneca@York Bank!" );
	      }
	      catch( Exception e ) {
		      System.out.println( "Error: " + e );
	      }
	      System.out.println("... the main thread is put into a wait state!");
	   }
}
