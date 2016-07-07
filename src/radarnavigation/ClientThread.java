package radarnavigation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import common.Ship;

public class ClientThread extends Thread{
	
	private Socket socket;
	private BufferedReader bufferedreader;
	private DataOutputStream outputstream;
	private Ship ship;
	
	public ClientThread(Ship ship) {
		super();
		this.ship = ship;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Socket socket;
		try {
			socket = new Socket("localhost",8888);
			bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputstream = new DataOutputStream(socket.getOutputStream());
			outputstream.writeUTF("hello worls");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendData(StringBuilder data) {
		// TODO Auto-generated method stub
		
	}
	
	public StringBuilder[] getData(){
		// TODO 
		StringBuilder[] data = new StringBuilder[3];
		
		return data;
	}
	
	public void Logout(){
		//TODO ÍË³ö¿Í»§¶Ë
		
	}
}
