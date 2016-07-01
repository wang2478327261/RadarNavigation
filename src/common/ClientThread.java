package common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{

	Socket socket;
	BufferedReader bufferedreader;
	DataOutputStream outputstream;
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
	
	
}
