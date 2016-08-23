package shipserver;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
	private List<Ship> serverShips;
	private Map<String, Socket> sockets;
	private Map<String, List<Point>> track;
	
	public ServerThread() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public ServerThread(List<Ship> clientShips, List<Ship> serverShips, Map<String, Socket> sockets, Map<String, List<Point>> track) {
		super();
		this.clientShips = clientShips;
		this.serverShips = serverShips;
		this.sockets = sockets;
		this.track = track;
	}
	
	@Override
	public void run() {
		
		super.run();
		try {
			serversocket = new ServerSocket(3333);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(){   //收到客户端的注册信息，新建新套接字
			public void run(){
				/*Socket connectionsocket;
				try {
					connectionsocket = serversocket.accept();
					input = new ObjectInputStream(new BufferedInputStream(connectionsocket.getInputStream()));
					output = new ObjectOutputStream(new BufferedOutputStream(connectionsocket.getOutputStream()));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
			}
		}.start();
		while(!logOut){  //同步
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendData(Socket socket, String data) throws IOException{   //对某一个套接字发送信息，更新数据
		output.writeUTF(data);
		output.flush();
		System.out.println("ServerThread.sendData()");
	}
	
	public String getData() throws IOException{
		String data = input.readUTF();
		System.out.println("ServerThread.getData()");
		return data;
	}
	
	public void sync() throws IOException{  //每走一步同步一次
		// TODO sync
		for (Ship vessel : clientShips) {
			String command = vessel.getName() + " go";
			sendData(command);
		}
		for (Ship vessel : serverShips) {
			String command = vessel.getName() + " go";
			sendData(command);
		}
	}
	
	public void kickOut(Ship ship) throws IOException {  //踢出某个客户端
		sendData(ship.getName() + "kickOut");
	}
	
	public void logOut(Ship ship) throws IOException {  //向所有客户端发送更新数据
		sendData(ship.getName() + "logOut");
	}
	
}
