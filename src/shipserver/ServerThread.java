package shipserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

	ServerSocket serversocket;
	Socket connectionsocket;
	BufferedReader bufferedoeader;
	DataOutputStream dataoutputotream;
	
	/*public ServerThread(ServerSocket serversocket, Socket connectionsocket, BufferedReader bufferedoeader,
			DataOutputStream dataoutputotream) {
		super();
		this.serversocket = serversocket;
		this.connectionsocket = connectionsocket;
		this.bufferedoeader = bufferedoeader;
		this.dataoutputotream = dataoutputotream;
	}*/
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			serversocket = new ServerSocket(8888);
			connectionsocket = serversocket.accept();
			bufferedoeader = new BufferedReader(new InputStreamReader(connectionsocket.getInputStream()));
			dataoutputotream = new DataOutputStream(connectionsocket.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
