package shipserver;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import common.Ship;

public class ServerThread extends Thread{
	
	private ServerSocket serversocket;
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
				//数据存储处理
				BufferedReader input = new BufferedReader(new InputStreamReader(newsocket.getInputStream()));
				String[] login = input.readLine().split(",");
				if (login[1].equals("logIn")) {
					sockets.put(login[0], newsocket);
					//存储船舶对象
					clientShips.add(new Ship(login[0], Double.parseDouble(login[2]), Double.parseDouble(login[3]),
							Double.parseDouble(login[4]), Double.parseDouble(login[5]), login[6]));
				}else{
					continue;
				}
				
				new Thread(){   //这里怎么处理？？没办法，再次停止
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
		for (Socket sk : sockets.values()) {    //关闭所有的客户端
			try {
				sk.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendData(Socket socket, String data) throws IOException{   //对某一个套接字发送信息，更新数据
		PrintWriter output = new PrintWriter(socket.getOutputStream());
		output.println(data);
		output.flush();
		System.out.println("ServerThread.sendData()");
	}
	
	public String getData(Socket socket) throws IOException{
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String data = input.readLine();
		System.out.println("ServerThread.getData()");
		return data;
	}
	
	public void sync() throws IOException{  //每走一步同步一次
		// TODO sync
		for (Ship vessel : clientShips) {
			String command = vessel.getName() + ",go";
			sendData(sockets.get(vessel.getName()), command);
		}
		for (Ship vessel : serverShips) {
			String command = vessel.getName() + ",go";
			sendData(sockets.get(vessel.getName()), command);
		}
	}
	
	//一下这两个方法用在管理客户端中，暂不实现具体功能
	/*public void kickOut(String name) throws IOException {  //踢出某个客户端
		sendData(name + ",kickOut");
	}
	
	public void logOut(Ship ship) throws IOException {  //向所有客户端发送更新数据
		sendData(ship.getName() + ",logOut");
	}*/
	
}
