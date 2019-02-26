package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Client.Client;

public class Server {

	ServerSocket serverSocket;
	Socket socket;
	
	Socket comm;
	
	DataInputStream inboundFromClient;
	DataOutputStream outboundToClient;

	int port;

	int userID = 0;

	ArrayList<ClientHandler> memberList;

	static Socket connectionBetween2Servers;

	public Server(int port) throws IOException {
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
		
	}

	public static void main(String[] args) throws IOException {
		
		Server server = new Server(6000);
	}
}
