package shipserver;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import common.Ship;

public class ServerThread extends Thread{
	
	private ServerSocket serversocket;
	private BufferedReader input;
	private PrintWriter output;
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
	public void run() {  //接受客户端数据
		//super.run();
		try {
			serversocket = new ServerSocket(8888);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!logOut){
			try {
				Socket newsocket = serversocket.accept();
				System.out.println("Get a newsocket!!");
				
				input = new BufferedReader(new InputStreamReader(newsocket.getInputStream()));
				String newName = input.readLine().split(",")[0];  //得到该线程的船名
				
				new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//super.run();    //调用父类的run方法是什么意思？
						
					}
				}.start();
				
				try {
					sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void sendData(String data) throws IOException{   //对某一个套接字发送信息，更新数据
		output.println(data);
		output.flush();
		System.out.println("ServerThread.sendData()");
	}
	
	public String getData() throws IOException{
		String data = input.readLine();
		System.out.println("ServerThread.getData()");
		return data;
	}
	
	public void sync() throws IOException{  //每走一步同步一次
		// TODO sync
		for (Ship vessel : clientShips) {
			String command = vessel.getName() + ",go";
			sendData(command);
		}
		for (Ship vessel : serverShips) {
			String command = vessel.getName() + ",go";
			sendData(command);
		}
	}
	//一下这两个方法用在管理客户端中，暂不实现具体功能
	public void kickOut(Ship ship) throws IOException {  //踢出某个客户端
		sendData(ship.getName() + ",kickOut");
	}
	
	public void logOut(Ship ship) throws IOException {  //向所有客户端发送更新数据
		sendData(ship.getName() + ",logOut");
	}
	
}
