package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Receive implements Runnable{
	
	DataInputStream inboundToClient;
	
	public Receive(DataInputStream inboundToClient)
	{
		this.inboundToClient = inboundToClient;
	}
	
	@Override
	public void run() {
		
		while (true)
		{
			try {
				System.out.println(inboundToClient.readUTF());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				break;
			}
		}
		
	}

}
