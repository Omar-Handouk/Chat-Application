package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ServerConnector {
	

		
		Scanner serverInput;
		DataOutputStream outboundToDestination;
		
		Server s;
		
		public ServerConnector(Scanner serverInput, DataOutputStream outboundToDestination, Server s)
		{
			this.serverInput = serverInput;
			this.outboundToDestination=outboundToDestination;
			this.s=s;
			
			
			while (true)
			{
				String Servermsg = serverInput.nextLine();
				
				
				try {
					outboundToDestination.writeUTF(Servermsg);
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
		
	

	
	

}
