package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	Socket socket;
	
	DataInputStream inboundFromServer;
	DataOutputStream outboundToServer;
	
	Scanner userInput;
	
	String Server_IP;
	int serverPort;
	
	public Client(String Server_IP, int serverPort)
	{
		this.Server_IP = Server_IP;
		this.serverPort = serverPort;
		
		
		this.init();
	}
	
	void init()
	{
		try {
			socket = new Socket(Server_IP, serverPort);
			
			inboundFromServer = new DataInputStream(socket.getInputStream());
			outboundToServer = new DataOutputStream(socket.getOutputStream());
			
			outboundToServer.writeUTF("Not Server");
			
			userInput = new Scanner(System.in);
			
			/*
			 * While loop that handles username fetching, will always be true if the user selects a username that is already taken
			 */
			
			while (true)
			{
				System.out.println("<<Please select a username>>");
				
				String username = userInput.nextLine();
				
				outboundToServer.writeUTF(username);
				
				String response = inboundFromServer.readUTF();
				
				System.out.println(response);
				
				if (!response.contains("another"))
					break;
			}
			
			
			Send send = new Send(userInput, outboundToServer, this);
			Receive receive = new Receive(inboundFromServer);
			
			Thread sendThread = new Thread(send);
			Thread receiveThread = new Thread(receive);
			
			sendThread.start();
			receiveThread.start();
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Client client = new Client("localhost", 6000);
		System.out.println(client.Server_IP+ client.serverPort);
	}
}
