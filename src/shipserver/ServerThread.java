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
	
	//���ݲ������Զ�����в���
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
		this.smallpanel = smallpanel;  //ˢ�½���
		System.out.println("ServerThread -> @overidethread");
	}
	
	@Override
	public void run() {  //���ܿͻ�������
		//super.run();
		try {
			serversocket = new ServerSocket(8888);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		new Thread(){        //�Է����������Ķ������ͬ��ǰ��
			@Override
			public void run() {
				System.out.println("�Է���˴����Ķ������ͬ��");
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
					}
					smallpanel.repaint();
					try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
		while(!logOut){  //�Կͻ��˽��д������յ�һ���ͻ��˵��׽��֣�����һ���̣߳�����ѭ������
			try {
				Socket newsocket = serversocket.accept();
				System.out.println("Get a newsocket!!");
				
				//���ݴ洢����,���µĿͻ��˶�����봦�����
				sockets.add(newsocket);
				for (int i = 0; i < sockets.size(); i++) {    //����Ѿ��رյ��׽���
					if (sockets.get(i).isClosed()) {
						sockets.remove(i);
						i--;
					}
				}
				new Thread(){   //������ô������û�취���ٴ�ֹͣ  2016.8.24
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//super.run();    //���ø����run������ʲô��˼��
						Socket socket = sockets.get(sockets.size() - 1);  //�õ��½����׽���
						System.out.println("�յ��ŵĿͻ��˽������߳�");
						
						String getData = null;
						String[] change = null;
						String name = null;
						try {
							BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintWriter output = new PrintWriter(socket.getOutputStream());
							getData = input.readLine();
							change = getData.split(",");
							name = change[0];
							//���·�������������
							clientShips.add(new Ship(name, Double.parseDouble(change[2]), Double.parseDouble(change[3]), Double.parseDouble(change[4]), Double.parseDouble(change[5]), change[6]));
							for(Socket sk : sockets){
								//String command = name + ",logIn"+","+change[2]+change[3]+change[4]+change[5]+change[6];
								try {
									sendData(sk, getData);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						while(!socket.isClosed()){  //��������һ�����������Ա���ʶ�𲢷����ض�����Ϣ***********************
							if (change[1].equals("logOut")) {
								for(Socket sk : sockets){
									//String command = name + ",logOut";
									try {
										sendData(sk, getData);
									} catch (IOException e) {
										// TODO Auto-generated catch block
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
										// TODO Auto-generated catch block
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
										// TODO Auto-generated catch block
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
		for (Socket sk : sockets) {    //�ر����еĿͻ���
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
	
	public void sendData(Socket socket, String data) throws IOException{   //��ĳһ���׽��ַ�����Ϣ����������
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
	
	public void sync(Socket socket, String name) throws IOException{  //ÿ��һ��ͬ��һ��
		// TODO sync
		System.out.println("ServerThread -> sycn");
		String command = name + ",go";
		sendData(socket, command);
	}
	
	//һ���������������ڹ���ͻ����У��ݲ�ʵ�־��幦��
	/*public void kickOut(String name) throws IOException {  //�߳�ĳ���ͻ���
		sendData(name + ",kickOut");
	}
	
	public void logOut(Ship ship) throws IOException {  //�����пͻ��˷��͸�������
		sendData(ship.getName() + ",logOut");
	}*/
	
}
