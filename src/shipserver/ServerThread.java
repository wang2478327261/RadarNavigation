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
	private boolean logOut = false;
	private SmallPanel smallpanel;
	
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
		this.smallpanel = smallpanel;
		System.out.println("ServerThread -> @overidethread");
	}
	
	@Override
	public void run() {
		//super.run();
		try {
			serversocket = new ServerSocket(8888);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		new Thread(){
			@Override
			public void run() {
				System.out.println("server thread");
				while(!logOut){
					for(Ship ship : serverShips){
						ship.goAhead();
						for (Socket sk : sockets) {
							try {
								sync(sk, ship.getName());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					smallpanel.repaint();
					try {
						sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		while(!logOut){
			try {
				Socket newsocket = serversocket.accept();
				System.out.println("Get a newsocket!!");
				
				sockets.add(newsocket);
				for (int i = 0; i < sockets.size(); i++) {
					if (sockets.get(i).isClosed()) {
						sockets.remove(i);
						i--;
					}
				}
				new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//super.run();
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
							
							clientShips.add(new Ship(name, Double.parseDouble(change[2]), Double.parseDouble(change[3]), Double.parseDouble(change[4]), Double.parseDouble(change[5]), change[6]));
							for(Socket sk : sockets){
								//String command = name + ",logIn"+","+change[2]+change[3]+change[4]+change[5]+change[6];
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
						while(!socket.isClosed()){
							if (change[1].equals("logOut")) {
								for(Socket sk : sockets){
									//String command = name + ",logOut";
									try {
										sendData(sk, getData);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								
								break;
							}
							else if (change[1].equals("speed")) {
								for(Socket sk : sockets){
									//String command = name + ",speed" + "," +change[2];
									try {
										sendData(sk, getData);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
							else if (change[1].equals("course")) {
								for(Socket sk : sockets){
									//String command = name + ",course" + ","+change[2];
									try {
										sendData(sk, getData);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
							else if (change[1].equals("go")) {
								for(Socket sk : sockets){
									//String command = name + ",go";
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
				if(!sk.isClosed()){
					sk.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void sendData(Socket socket, String data) throws IOException{
		PrintWriter output = new PrintWriter(socket.getOutputStream());
		output.println(data);
		output.flush();
		System.out.println("ServerThread.sendData()");
	}
	
	public static String getData(Socket socket) throws IOException{
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String data = input.readLine();
		System.out.println("ServerThread.getData()");
		return data;
	}
	
	public static void sync(Socket socket, String name) throws IOException{
		// TODO sync
		System.out.println("ServerThread -> sycn");
		String command = name + ",go";
		sendData(socket, command);
	}
	
	/*public void kickOut(String name) throws IOException {
		sendData(name + ",kickOut");
	}
	
	public void logOut(Ship ship) throws IOException {
		sendData(ship.getName() + ",logOut");
	}*/
	
}
