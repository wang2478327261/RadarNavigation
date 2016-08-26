package radarnavigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import common.Ship;

/**
 * 这个类实现客户端的通信功能， 新线程  
 * @author ERON  
 * @see RadarNavigation  
 */
public class ClientThread extends Thread{  //本船发出变化信息  ---》从外界接受更新对方船舶的信息
	
	private Socket socket;  //本客户端的套接字
	private BufferedReader input;
	private PrintWriter output;
	private Ship ship;
	private List<Ship> ships;   //这里存储的是对方的船舶对象列表
	//通过判断套接字决定循环的继续
	//public boolean logOut = false;
	
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
		//super.run();
		try {
			socket = new Socket(InetAddress.getLocalHost(),8888);  //打开一个端口号3333的套接字
			System.out.println(socket);
			//打开两条数据流
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			
			logIn();  //发送登录信息
			
		} catch (IOException e) {
			// TODO 连接不上服务器的处理
			//e.printStackTrace();
			System.out.println("Server is not Exist OR is not Connected!");
			System.exit(1);
		}
		new Thread(){  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<打开接收线程,接收数据
			public void run() {
				while(!socket.isClosed()){ //如果没有登出，则循环进行同步
					try {
						String data = getData();
						String[] change = data.split(",");  //分隔符
						if (change[0].equals(ship.getName())) {
							continue;
						}
						else{
							switch(change[1]){
								case "logIn":{  //有新船登入系统
									ships.add(new Ship(change[0], Double.parseDouble(change[2]), 
											Double.parseDouble(change[3]), Double.parseDouble(change[4]),
											Double.parseDouble(change[5]), change[6]));
									break;
								}
								case "logOut":{  //他船登出系统
									for(Ship vessel : ships){
										if (vessel.getName().equals(change[0])) {
											ships.remove(vessel);
											break;
										}
									}
									break;
								}
								case "speed":{
									for (Ship vessel : ships) {
										if (vessel.getName().equals(change[0])) {
											if (change[2].equals("increase")) {
												vessel.setValue(4, vessel.getParameter(4)+1);
											}
											else {
												vessel.setValue(4, vessel.getParameter(4)-1);
											}
											break;
										}
									}
									break;
								}
								case "course":{
									for (Ship vessel : ships) {
										if (vessel.getName().equals(change[0])) {
											if (change[2].equals("port")) {
												vessel.setValue(3, vessel.getParameter(3)-1);
											}
											else {
												vessel.setValue(3, vessel.getParameter(3)+1);
											}
											break;
										}
									}
									break;
								}
								case "go":{
									for (Ship vessel : ships) {
										if (vessel.getName().equals(change[0])) {
											vessel.goAhead();
											break;
										}
									}
									break;
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}.start();
		while(!socket.isClosed()){   //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>同步数据，发送信息到服务端
			try {
				System.out.println("logout loops");
				sleep(1000);
				ship.goAhead();  //船舶向前走一步
				//	ship.printShip();
				sync();  //向服务端发送同步信号
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
	 * @return null
	 * @throws IOException
	 */
	public synchronized void sendData(String data) throws IOException{  //船舶名称重复问题先暂时不解决
		// TODO 发送字符串控制信息
		output.println(data);
		output.flush();
		System.out.println("sendData");
	}
	
	public String getData() throws IOException{   //这个方法多余，好处是如果对其
		// TODO 从服务端接受其他客户端或者是服务器的变化信息
		String data = input.readLine();   //以UTF的格式接受字符串
		
		System.out.println("getData");
		return data;
	}
	
	public void sync() throws IOException{  //每走一步同步一次
		// TODO 登陆服务端
		String command = ship.getName() + ",go";
		sendData(command);
		System.out.println("sycn");
	}
	
	public void logIn() throws IOException{
		//TODO 登陆客户端
		String command = ship.getName() + ",logIn," + ship.getParameter(1) +","+ ship.getParameter(2)
						+ "," +ship.getParameter(3) +","+ ship.getParameter(4) +","+ ship.getType();
		sendData(command);
		System.out.println("logIn");
	}
	
	public void logOut() throws IOException{
		//TODO 退出客户端
		String command = ship.getName() + ",logOut";
		sendData(command);
		System.out.println("logOut");
		
		input.close();
		output.close();
		socket.close();
	}
}
