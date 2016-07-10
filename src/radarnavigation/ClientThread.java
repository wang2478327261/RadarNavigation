package radarnavigation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import common.Ship;

public class ClientThread extends Thread{
	
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private List<Ship> ships;
	
	public ClientThread(List<Ship> ships) {
		super();
		this.ships = ships;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Socket socket = null;
		try {
			socket = new Socket("localhost",8888);
			//打开两条数据流
			input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			ships = new LinkedList<Ship>();  //初始化对象
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Server is not Exist OR is not Connection!");
			//System.exit(1);
		}
	}
	
	public void sendData(StringBuilder data) {
		// TODO 发送字符串控制信息
		
	}
	
	public StringBuilder[] getData(){
		// TODO 从服务端接受其他客户端或者是服务器的变化信息
		StringBuilder[] data = new StringBuilder[3];
		
		return data;
	}
	
	public void Logout(){
		//TODO 退出客户端
	}
}
