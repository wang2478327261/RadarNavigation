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
	private SmallPanel smallpanel;
	
	//传递参数，对对象进行操作
	private List<Ship> clientShips;
	private List<Ship> serverShips;
	private List<Socket> sockets;
	private Map<String, List<Point>> track;
	
	public ServerThread(List<Ship> clientShips, List<Ship> serverShips, List<Socket> sockets, Map<String, List<Point>> track,
			SmallPanel smallpanel) {
		super();
		this.clientShips = clientShips;
		this.serverShips = serverShips;
		this.sockets = sockets;
		this.track = track;
		this.smallpanel = smallpanel;  //刷新界面
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
		
		new Thread(){        //对服务器创建的对象进行同步前进
			@Override
			public void run() {
				while(!logOut){
					for(Ship ship : serverShips){
						ship.goAhead();
						for (Socket sk : sockets) {
							try {
								sync(sk, ship.getName());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						smallpanel.repaint();
					}
					try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
		while(!logOut){
			try {
				Socket newsocket = serversocket.accept();
				System.out.println("Get a newsocket!!");
				
				//数据存储处理
				sockets.add(newsocket);
				for (int i = 0; i < sockets.size(); i++) {    //检查已经关闭的套接字
					if (sockets.get(i).isClosed()) {
						sockets.remove(i);
						i--;
					}
				}
				new Thread(){   //这里怎么处理？？没办法，再次停止  8.24
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//super.run();    //调用父类的run方法是什么意思？
						Socket socket = sockets.get(sockets.size() - 1);  //得到新建的套接字
						
						String[] change = null;
						String name = null;
						try {
							BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintWriter output = new PrintWriter(socket.getOutputStream());
							change = input.readLine().split(",");
							name = change[0];
							
							for(Socket sk : sockets){
								String command = name + ",logIn"+","+change[1]+change[2]+change[3]+change[4]+change[5];
								try {
									sendData(sk, command);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						while(true){
							if (change[1].equals("logOut")) {
								for(Socket sk : sockets){
									String command = name + ",logOut";
									try {
										sendData(sk, command);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								break;
							}
							else if (change[1].equals("speed")) {
								for(Socket sk : sockets){
									String command = name + ",speed" + "," +change[2];
									try {
										sendData(sk, command);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if (change[1].equals("course")) {
								for(Socket sk : sockets){
									String command = name + ",speed" + change[2];
									try {
										sendData(sk, command);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else if (change[1].equals("go")) {
								for(Socket sk : sockets){
									String command = name + ",speed" + change[2];
									try {
										sendData(sk, command);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
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
		for (Socket sk : sockets) {    //关闭所有的客户端
			try {
				if(!sk.isClosed()){
					sk.close();
				}
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
	
	public void sync(Socket socket, String name) throws IOException{  //每走一步同步一次
		// TODO sync
		String command = name + ",go";
		sendData(socket, command);
	}
	
	//一下这两个方法用在管理客户端中，暂不实现具体功能
	/*public void kickOut(String name) throws IOException {  //踢出某个客户端
		sendData(name + ",kickOut");
	}
	
	public void logOut(Ship ship) throws IOException {  //向所有客户端发送更新数据
		sendData(ship.getName() + ",logOut");
	}*/
	
}
