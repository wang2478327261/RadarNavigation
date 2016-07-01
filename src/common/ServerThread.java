package common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			ServerSocket serversocket = new ServerSocket(8888);
			Socket connectionsocket = serversocket.accept();
			BufferedReader bufferedoeader = new BufferedReader(new InputStreamReader(connectionsocket.getInputStream()));
			DataOutputStream dataoutputotream = new DataOutputStream(connectionsocket.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
