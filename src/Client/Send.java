package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Send implements Runnable {
	
	Scanner userInput;
	DataOutputStream outboundToDestination;
	
	Client client;
	
	public Send(Scanner userInput, DataOutputStream outboundToDestination, Client client)
	{
		this.userInput = userInput;
		this.outboundToDestination=outboundToDestination;
		this.client = client;
	}
	
	@Override
	public void run() {
		
		while (true)
		{
			String userMsg = userInput.nextLine();
			
			
			if (userMsg.equals("quit"))
			{
				try {
					outboundToDestination.writeUTF(userMsg);
					client.socket.close();
					client.inboundFromServer.close();
					client.outboundToServer.close();
					client.userInput.close();
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				if (userMsg.contains("#")) {
				userMsg = userMsg+"#2";}
				outboundToDestination.writeUTF(userMsg);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
