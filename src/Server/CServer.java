package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Client.Client;

public class CServer {

	ServerSocket serverSocket;
	Socket socket;
	
	Socket server;
	DataInputStream inboundFromServer;
	DataOutputStream outboundToServer;
	
	DataInputStream inboundFromClient;
	DataOutputStream outboundToClient;

	int port;

	int userID = 0;

	ArrayList<ClientHandler> memberList;

	public CServer(int port) throws IOException {
		this.port = port;

		memberList = new ArrayList<>();
		// Socket connectionBetween2Servers;

		this.init();
	}

	void init() throws IOException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AcceptanceHandler acceptanceHandler = new AcceptanceHandler(serverSocket, memberList);
		Thread acceptanceThread = new Thread(acceptanceHandler);

		acceptanceThread.start();
		
		int wait = 0;
		
		while (true)
		{
			try {
				server = new Socket("localhost", 6000);
				inboundFromServer = new DataInputStream(server.getInputStream());
				outboundToServer = new DataOutputStream(server.getOutputStream());
				
				outboundToServer.writeUTF("Server Status");
				
				ClientHandler serverHandler = new ClientHandler(inboundFromServer, outboundToServer, -1, memberList, socket);
				Thread serverThread = new Thread(serverHandler);
				
				memberList.add(serverHandler);
				
				serverThread.start();
				
				System.out.println("Connected to server 1");
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (wait == 0)
					System.out.println("Attempting to connecting to connect to server 1");
				wait = (wait + 1) % 100;
			}
			
		}
	}

	public static void main(String[] args) throws IOException {
		
		CServer server = new CServer(3000);
	}
}
