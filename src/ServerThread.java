//----------------------------------------------------------------
//Joseph Avolio
//05/14/2012
//COSC 335
//Project 2
//----------------------------------------------------------------
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private File cd;
	
	public ServerThread(Socket client) throws IOException {
		this.client=client;
		
		//Set up the input and output streams
		in=new BufferedReader(new InputStreamReader(client.getInputStream()));
		out=new PrintWriter(client.getOutputStream(),true);
		
		//Initialize the tracker for the current directory
		cd=new File(".");
	}
	
	@Override
	public void run() {
		String input;
		do {
			input="";
			try {
				if (in.ready()) { //If there is input
					try {
						//Read it in
						input=in.readLine();
						
						//Handle it, and respond
						out.println(processInput(input));
					} catch (IOException e) {
					}
				}
			} catch (IOException e) {
			}
		} while (!input.equals("bye"));
		try {
			//Close everything down
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			//Failed to close? Oh well. We tried.
		}
	}
	
	private String processInput(String input) {
		if (input==null) //An empty request
			return "Invalid input";
		else if (input.equals("bye")) //It's polite to say goodbye when someone is leaving
			return "Bye!";
		else if (input.equals("pwd"))
			try {
				return cd.getCanonicalPath(); //Get the canonical path to the cd tracker
			} catch (IOException e) {
				return "Error: pwd could not be determined"; //Because sometimes stuff happens that just can't be explained
			}
		else if (input.equals("ls")) {
			String s="";
			File[] files=cd.listFiles();
			for (File f:files)
				s+=f.getName()+"\n";
			return s;
		} else if (input.startsWith("cd ")) {
			cd=new File(cd,input.substring(3)); //Change the current directory tracker
			try {
				return cd.getCanonicalPath(); //Give the client the updated current directory
			} catch (IOException e) {
				return "Error: pwd could not be determined"; //This should not happen. That's why it says, "Error"
			}
		} else if (input.startsWith("get ")) {
			File f=new File(cd,input.substring(4)); //Open a file with the name 
			FileInputStream fIn;
			try { //Read in the bytes from the file, and return them as a string
				fIn=new FileInputStream(f);
				byte[] bytes=new byte[(int) f.length()];
				fIn.read(bytes);
				fIn.close();
				return new String(bytes);
			} catch (FileNotFoundException e) {
				return "Error: File could not be found"; //This is probably the client's fault
			} catch (IOException e) {
				return "Error: File could not be read"; //The file is unreadable; nothing to be done
			}
		} else if (input.startsWith("put ")) {
			File f=new File(cd,input.substring(4,input.indexOf(0))); //Parse the filename from the packet
			FileOutputStream fOut;
			try { //Parse the data out of the packet and write it to the file
				fOut=new FileOutputStream(f);
				fOut.write(input.substring(input.indexOf(0)+1).getBytes());
				fOut.close();
				return "Complete";
			} catch (FileNotFoundException e) {
				return "Error: File could not be found"; //Probably a user-error
			} catch (IOException e) {
				return "Error: File could not be written"; //Something unfortunate happened
			}
		} else
			return "Invalid input"; //User was probably being dumb
	}
}
