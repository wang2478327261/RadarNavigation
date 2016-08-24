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
	
	public ServerThread(List<Ship> clientShips, List<Ship> serverShips, Map<String, Socket> sockets, Map<String, List<Point>> track) {
		super();
		this.clientShips = clientShips;
		this.serverShips = serverShips;
		this.sockets = sockets;
		this.track = track;
	}
	
	@Override
	public void run() {
		//super.run();
		while(!logOut){
			try {
				serversocket = new ServerSocket(8888);
				Socket newsocket = serversocket.accept();
				input = new ObjectInputStream(new BufferedInputStream(newsocket.getInputStream()));
				output = new ObjectOutputStream(new BufferedOutputStream(newsocket.getOutputStream()));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Socket socket : sockets.values()) {
			//发送关闭信息到客户端
			
		}
		/*Thread receive = new Thread(){   //接收客户端信息  更改本地数据后对客户端及逆行同步
			public void run(){
				while(!logOut){
					try {
						Socket newsocket = serversocket.accept();
						input = new ObjectInputStream(new BufferedInputStream(newsocket.getInputStream()));
						output = new ObjectOutputStream(new BufferedOutputStream(newsocket.getOutputStream()));
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (Socket socket : sockets.values()) {
					//发送关闭信息到客户端
					
				}
			}
		};
		receive.start();*/
		
	}
	
	public void sendData(String data) throws IOException{   //对某一个套接字发送信息，更新数据
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
