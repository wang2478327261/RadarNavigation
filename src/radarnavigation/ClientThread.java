package radarnavigation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import common.Ship;

/**
 * 这个类实现客户端的通信功能， 新线程  
 * @author ERON  
 * @see RadarNavigation  
 */
public class ClientThread extends Thread{
	
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Ship ship;
	private List<Ship> ships;
	
	public boolean logOut = false;
	
	/**
	 * <p>传播指令集的设计  </p>
	 * <p>客户端： 哪艘船--  哪个状态--  变化了多少    type</p>
	 * @param ships
	 * @author ERON
	 */
	public ClientThread(Ship ship, List<Ship> ships) {
		super();
		this.ship = ship;
		this.ships = ships;
	}
	
	@Override
	public void run() {
		// TODO 新建套接字并打开两条数据流
		super.run();
		try {
			socket = new Socket(InetAddress.getLocalHost(),3333);
			//打开两条数据流
			input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
		} catch (IOException e) {
			// TODO 连接不上服务器的处理
			//e.printStackTrace();
			System.out.println("Server is not Exist OR is not Connected!");
			//System.exit(1);
		}
		new Thread(){  //打开接收线程,接收数据
			public void run() {
				while(!logOut){
					try {
						String data = getData();
						String[] change = data.split("s");
						if (change[0].equals("change")) {
							System.out.println("");
						}
						else {  //ship modify
							System.out.println("");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}.start();
		while(!logOut){   //同步数据，发送信息到服务端
			try {
				System.out.println("logout loops");
				sleep(1000);
				ship.goAhead();  //船舶向前走一步
				goAhead();  //向服务端发送同步信号
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * <p>发送--》发送本船状态变化      //changeType// 船名（唯一识别符），状态， 变化值  </p>
	 * <p>		//-->发送同步信息      //changeType// 船名  状态 变化值, 变化值加一的过程，  不用这个方法
	 * <p>接收--》 changeType判断是变化还是创建销毁 </p>
	 * <p>		接收他船状态变化        changeType 船名， 状态，值  </p>
	 * <p>		接受创建或登出船舶信息      changeType 船名，创建还是删除  </p>
	 * @return
	 * @throws IOException
	 */
	public synchronized void sendData(String data) throws IOException{  //船舶名称重复问题先暂时不解决
		// TODO 发送字符串控制信息
		output.writeUTF(data);
		output.flush();
		System.out.println("sendData");
	}
	
	public String getData() throws IOException{
		// TODO 从服务端接受其他客户端或者是服务器的变化信息
		String data = null;
		data = input.readUTF();   //以UTF的格式接受字符串
		
		System.out.println("getData");
		return data;
	}
	
	public void goAhead() throws IOException{  //每走一步同步一次
		// TODO 登陆服务端
		String command = ship.getName() + "goAhead";
		sendData(command);
	}
	
	public void logOut(String info) throws IOException{
		//TODO 退出客户端
		String command = ship.getName() + "logOut";
		sendData(command);
	}
}
