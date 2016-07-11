package shipserver;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import common.Ship;

public class ServerThread extends Thread{
	
	private ServerSocket serversocket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private boolean logOut = false;
	//传递参数，对对象进行操作
	private List<Ship> clientShips;
	private Map<String, Socket> sockets;
	private Map<String, List<Point>> track;
	
	public ServerThread(List<Ship> clientShips, List<Ship> serverShips, Map<String, Socket> sockets, Map<String, List<Point>> track) {
		super();
		this.clientShips = clientShips;
		this.sockets = sockets;
		this.track = track;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			serversocket = new ServerSocket(8888);
			Socket connectionsocket = serversocket.accept();
			
			input = new ObjectInputStream(new BufferedInputStream(connectionsocket.getInputStream()));
			output = new ObjectOutputStream(new BufferedOutputStream(connectionsocket.getOutputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!logOut){
			try {
				sleep(1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendData(String data) throws IOException{
		output.writeUTF(data);
	}
	
	public String getData() throws IOException{
		String data;
		data = input.readUTF();
		return data;
	}
	
	public void kickOut() throws IOException {
		sendData("");
	}
	
	public void logOut() throws IOException {
		sendData("");
	}
	
}
