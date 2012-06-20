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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		//Make a Scanner to read in all sorts of wonderful things
		Scanner scan=new Scanner(System.in);
		
		//Set a whole bunch of default values
		boolean validAddress=false;
		InetAddress destinationAddress=null;
		Socket server=null;
		PrintWriter out=null;
		BufferedReader in=null;
		boolean valid=false;
		while (!valid) {
			//Read in a valid IP Address
			while (!validAddress) {
				System.out.print("Enter Server IP Address: ");
				try {
					destinationAddress=InetAddress.getByName(scan.nextLine());
					validAddress=true;
				} catch (UnknownHostException e) {
					System.out.println("Error: Invalid IP Address"); //Uhoh
				}
			}
			
			//Read in a valid port, and start the connection
			int destinationPort=0;
			System.out.print("Enter Server Port: ");
			destinationPort=scan.nextInt();
			try {
				server=new Socket(destinationAddress,destinationPort);
				valid=true;
				out=new PrintWriter(server.getOutputStream(),true);
				in=new BufferedReader(new InputStreamReader(server.getInputStream()));
			} catch (IOException e) {
				System.out.println("Error: Invalid Destination"); //Something went wrong
			}
		}
		
		scan.nextLine(); //Clear the input
		
		String input="";
		do {
			System.out.print("> ");
			input=scan.nextLine();
			if (input!=null&&input.startsWith("put ")) { //If it's a put command, add the file to the packet
				File f=new File(input.substring(4));
				FileInputStream fIn=null;
				try { //Read in the file, and add it (with a delimiter of (char) 0) to the end of the message 
					fIn=new FileInputStream(f);
					byte[] bytes=new byte[(int) f.length()];
					fIn.read(bytes);
					input+=(char) 0+new String(bytes);
					fIn.close();
				} catch (FileNotFoundException e) {
					System.out.println("File could not be found"); //The user probably screwed up
					input=null;
				} catch (IOException e) {
					System.out.println("File could not be read"); //Oh well
					input=null;
				}
			}
			out.println(input); //Send the packet
			if (input==null||!input.startsWith("get ")) //If it's anything other than a get command, print out the response
				try {
					System.out.println(in.readLine());
					if (input.equals("ls")) //ls will return multiple lines
						while (in.ready())
							System.out.println(in.readLine());
				} catch (IOException e) {
					System.out.println("Error: Data could not be read"); //Too bad
				}
			else { //If it's a get command, create a new file and write the data to it
				File f=new File(input.substring(4));
				try {
					FileOutputStream fOut=new FileOutputStream(f);
					fOut.write(in.readLine().getBytes());
					System.out.println("Complete");
					fOut.close();
				} catch (IOException e) {
					System.out.println("Error: Data could not be written"); //How terrible
				}
			}
		} while (!input.equals("bye")); //If it was a bye command, we're done here
	}
}
