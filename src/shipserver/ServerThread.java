package shipserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import common.Ship;

public class ServerThread extends Thread {  //1秒小同步，5秒一大同步
	
	private ServerSocket serversocket;
	private boolean logOut = false;
	
	private SmallPanel smallpanel;
	private List<Ship> clientShips;
	private List<Ship> serverShips;
	private List<Socket> sockets;  //在这个类中只要不new一个新的链表，那它就是指向了引用的套接字聊表
	// private Map<String, List<Point>> track;
	
	public ServerThread(SmallPanel smallpanel, List<Ship> clientShips, List<Ship> serverShips,
			List<Socket> sockets) {  //怎么解决超界限的问题，下一个版本中要加入地球模型
		super();
		this.clientShips = clientShips;  //得到数据引用，并没有创建新的对象
		this.serverShips = serverShips;
		this.smallpanel = smallpanel;
		this.sockets = sockets;
		System.out.println("ServerThread -> @overidethread");
	}
	
	@Override
	public void run() {  //server总线程
		// super.run();
		try {
			serversocket = new ServerSocket(8888);  //打开服务端口，接受客户端请求
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/******************服务端创建船舶进行同步************************************/
		new Thread() {
			@Override
			public void run() {  //对服务端的船舶对象同步
				System.out.println("serverships sync");
				while (!logOut) {
					for (Ship ship : serverShips) {
						ship.goAhead();  //本地前进一步
						for (Socket sk : sockets) {
							smallSync(sk, ship.getName());
						}
					}
					smallpanel.repaint();
					try {
						sleep(200);  //要和本地相同的刷新率
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		/*******************************************************/
		while (!logOut) {
			try {
				Socket newsocket = serversocket.accept();
				System.out.println("Get a newsocket!!");
				
				sockets.add(newsocket);
				Iterator<Socket> items = sockets.iterator();
				while(items.hasNext()){  //检测有没有关闭的客户端
					Socket it = items.next();
					if (it.isClosed()) {
						items.remove();  //移除当前指向
					}
				}
				/*for (int i = 0; i < sockets.size(); i++) {
					if (sockets.get(i).isClosed()) {
						sockets.remove(i);
						i--;  //？？？
					}
				}*/
				new Thread() {  //每接收到一个新客户端，就开启一个新线程针对这个新的客户端
					@Override
					public void run() {
						// super.run();
						Socket socket = sockets.get(sockets.size() - 1);
						System.out.println("get latest socket");
						
						String getData = null;
						String[] change = null;
						String name = null;
						try {
							BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintWriter output = new PrintWriter(socket.getOutputStream());
							getData = input.readLine();
							change = getData.split(",");
							name = change[0];

							clientShips.add(new Ship(name, Double.parseDouble(change[2]), Double.parseDouble(change[3]),
									Double.parseDouble(change[4]), Double.parseDouble(change[5]), change[6]));
							for (Socket sk : sockets) {
								// String command = name +
								// ",logIn"+","+change[2]+change[3]+change[4]+change[5]+change[6];
								try {
									sendData(sk, getData);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						while (!socket.isClosed()) {
							if (change[1].equals("logOut")) {
								for (Socket sk : sockets) {
									// String command = name + ",logOut";
									try {
										sendData(sk, getData);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

								break;
							} else if (change[1].equals("speed")) {
								for (Socket sk : sockets) {
									// String command = name + ",speed" + ","
									// +change[2];
									try {
										sendData(sk, getData);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else if (change[1].equals("course")) {
								for (Socket sk : sockets) {
									// String command = name + ",course" +
									// ","+change[2];
									try {
										sendData(sk, getData);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else if (change[1].equals("go")) {
								for (Socket sk : sockets) {
									// String command = name + ",go";
									try {
										sendData(sk, getData);
									} catch (IOException e) {
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
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (Socket sk : sockets) {
			try {
				if (!sk.isClosed()) {
					sk.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendData(Socket socket, String data) throws IOException {
		PrintWriter output = new PrintWriter(socket.getOutputStream());
		output.println(data);
		output.flush();
		System.out.println("ServerThread.sendData()");
	}
	
	public String getData(Socket socket) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String data = input.readLine();
		System.out.println("ServerThread.getData()");
		return data;
	}
	//单个对象进行同步
	public void smallSync(Socket socket, String name){  //小同步
		// TODO 小同步动作，一秒一次
		System.out.println("ServerThread -> SSSmallsycn");
		String command = name + ",go";
		try {
			sendData(socket, command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void bigSync(Socket socket, String name, double x,double y,double course, double speed){
		//大同步，5秒一次
		System.out.println("ServerThread -> BBBigsycn");
		String command = name +","+x+","+y+","+course+","+speed;
		try {
			sendData(socket, command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*new Thread(new Runnable() {  //怎么做才能大同步？
			@Override
			public void run() {
				// TODO 大同步新建线程进行
			}
		}).start();*/
	}
	//2017.3.7  服务端无权踢出用户
	/*public void kickOut(String name){  //服务端将某个客户端踢出
		for(int i=0;i<sockets.size();i++){
			//给客户端发送提出信息，客户端关闭发送端口，退出程序
			try {
				sendData(sockets.get(i), "kickout,"+name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("kick out "+name);
	}*/
	
	public void logIn(String command){
		//String command = name + "logIn" + mousex + mousey + course + speed + type;
		for(int i=0;i<sockets.size();i++){
			try {
				sendData(sockets.get(i), command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(command);
	}
	
	public void logOut(String name){
		for(int i=0;i<sockets.size();i++){
			try {
				sendData(sockets.get(i), "logout,"+name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("log out "+name);
	}
}
