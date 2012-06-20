//----------------------------------------------------------------
//Joseph Avolio
//05/14/2012
//COSC 335
//Project 2
//----------------------------------------------------------------
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Server {
	public static void main(String[] args) {
		ServerSocket server=null;
		boolean valid=false;
		//Try ports until one works
		for (int port=3500;!valid;port++)
			try {
				server=new ServerSocket(port);
				valid=true;
			} catch (IOException e) {
				System.out.println("Could not listen on port: "+port+"\nTrying next port");
			}
		
		//Print out the IP Address 
		try {
			System.out.println("IP Address: "+InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			System.out.println("Error: Unknown Local IP Address");
		}
		
		//Print out the port
		System.out.println("Port: "+server.getLocalPort());
		boolean active=true;
		
		//Listen for new connections until an accept fails
		while (active) {
			try {
				//Start a new ServerThread for the connection
				new Thread(new ServerThread(server.accept())).start();
			} catch (IOException e) {
				//Upon failure, stop looking for new connections
				System.err.println("Accept failed.");
				active=false;
			}
		}
	}
}
