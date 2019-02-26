package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler implements Runnable {

	DataInputStream inboundFromClient;
	DataOutputStream outboundToClient;

	String username = "";
	int userID;

	Socket socket;

	ArrayList<ClientHandler> memberList;

	public ClientHandler(DataInputStream inboundFromClient, DataOutputStream outboundToClient, int userID,
			ArrayList<ClientHandler> memberList, Socket socket) {
		System.err.println(">>ID: " + userID);

		this.inboundFromClient = inboundFromClient;
		this.outboundToClient = outboundToClient;
		this.userID = userID;
		this.memberList = memberList;
		this.socket = socket;

	}

	@Override
	public void run() {
		try {
			String username = inboundFromClient.readUTF();

			boolean found = false;

			for (ClientHandler client : memberList) {
				if (client.username.equals(username)) {
					found = true;

				}
			}

			if (found) {
				outboundToClient.writeUTF("Username already taken, please select another name");

			} else {
				outboundToClient.writeUTF(username);
				this.username = username;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {

			String msg = "";
			try {
				msg = inboundFromClient.readUTF();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (msg.equals("quit")) {
				try {
					inboundFromClient.close();
					outboundToClient.close();
					socket.close();
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				
				//TODO Add client token
				
				String[] a = msg.split("#");
				//System.out.println(Arrays.toString(a));

				String toBeSentTo = a[1];
				String TTLString = a[2];
				int TTL = Integer.parseInt(TTLString) - 1;

				String destinationID = toBeSentTo;
				
				boolean found = false;
				
				for (int i = 0; i < memberList.size(); i++) {

					ClientHandler ch = memberList.get(i);

					if (ch.username.equals(destinationID) && !destinationID.equals(username)) {
						try {
							found = true;
							ch.outboundToClient.writeUTF(a[0]);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}

				}
				
				if (!found && TTL > 0)
				{
					for (ClientHandler check : memberList)
						if (check.userID  == -1)
						{
							try {
								check.outboundToClient.writeUTF(a[0] + "#" + destinationID + "#" + TTL);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}

			}

		}

	}
}
