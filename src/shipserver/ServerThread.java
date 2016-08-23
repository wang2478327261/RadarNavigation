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
	//���ݲ������Զ�����в���
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
		new Thread(){   //�յ��ͻ��˵�ע����Ϣ���½����׽���
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
		while(!logOut){  //ͬ��
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendData(Socket socket, String data) throws IOException{   //��ĳһ���׽��ַ�����Ϣ����������
		output.writeUTF(data);
		output.flush();
		System.out.println("ServerThread.sendData()");
	}
	
	public String getData() throws IOException{
		String data = input.readUTF();
		System.out.println("ServerThread.getData()");
		return data;
	}
	
	public void sync() throws IOException{  //ÿ��һ��ͬ��һ��
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
	
	public void kickOut(Ship ship) throws IOException {  //�߳�ĳ���ͻ���
		sendData(ship.getName() + "kickOut");
	}
	
	public void logOut(Ship ship) throws IOException {  //�����пͻ��˷��͸�������
		sendData(ship.getName() + "logOut");
	}
	
}
