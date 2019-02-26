package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AcceptanceHandler implements Runnable {

	ServerSocket serverSocket;

	Socket socket; // Socket to accept client

	DataInputStream inboundFromClient;
	DataOutputStream outboundToClient;

	ArrayList<ClientHandler> memberList;
	
	Socket CServer;
	DataInputStream inboundFromCServer;
	DataOutputStream outboundToCServer;
	
	int userID = 0;

	public AcceptanceHandler(ServerSocket serverSocket, ArrayList<ClientHandler> memberList) {
		this.serverSocket = serverSocket;
		this.memberList = memberList;
	}

	@Override
	public void run() {

		while (true) {
			try {
				
				
				socket = serverSocket.accept();

				System.err.println(socket);

				inboundFromClient = new DataInputStream(socket.getInputStream());
				outboundToClient = new DataOutputStream(socket.getOutputStream());
				
				boolean isServer = inboundFromClient.readUTF().equals("Server Status");
				
				if (!isServer) {
					ClientHandler clientHandler = new ClientHandler(inboundFromClient, outboundToClient, userID,
							memberList, socket);
					Thread clientThread = new Thread(clientHandler);
					memberList.add(clientHandler);
					clientThread.start();
					userID++;
				}
				else
				{
					CServer = socket;
					inboundFromCServer = inboundFromClient;
					outboundToCServer = outboundToClient;
					
					ClientHandler serverHandler = new ClientHandler(inboundFromCServer, outboundToCServer, -1, memberList, socket);
					Thread serverThread = new Thread(serverHandler);
					
					memberList.add(serverHandler);
					
					serverThread.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
