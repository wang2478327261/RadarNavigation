package radarnavigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import common.Ship;

/**
 * @author ERON<br>
 * *********************************<br>
 * 动作集合<hr>
 * 登录--》船名，经度，纬度，方向，速度，船类型<br>
 * 前进一步：go--》船名，go<br>
 * 登出：logout--》船名，logout<br>
 * 方向变化：船名，course，变化量<br>
 * 速度变化：船名，speed，变化量<br>
 * 
 * @see RadarNavigation
 */
public class ClientThread extends Thread{
	
	public Socket socket = null;
	private BufferedReader input;
	private PrintWriter output;
	
	private RadarPanel radarpanel = null;
	private Ship ship = null;  //本船
	private List<Ship> ships = null;  //其他船舶
	//public boolean logOut = false;
	
	/**
	 * @param ships
	 * @author ERON
	 */
	public ClientThread(RadarPanel radarpanel, Ship ship, List<Ship> ships) {
		super();
		this.radarpanel = radarpanel;
		this.ship = ship;
		this.ships = ships;
	}
	
	@Override
	public void run() {
		//super.run();
		try {
			socket = new Socket(InetAddress.getLocalHost(), 6000);  //当连接不到服务端时，socket为null
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			logIn();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Server is not Exist OR is not Connected!");
			JOptionPane.showMessageDialog(null, "Client Will exit after 6s!");
			try {  //如果连接失败，就等6秒后关闭
				Thread.sleep(60000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(1);
		}
		new Thread(){
			public void run() {
				//接收信息
				while(!socket.isClosed()){
					try {
						String data = input.readLine();
						if (data==null) {
							continue;
						}
						String[] change = data.split(",");
						if (change[0].equals(ship.getName())) {
							if (change[1].equals("kickOut")) {
								input.close();
								output.close();
								
								socket.close();
							}
							continue;
						}
						else{
							switch(change[1]){
								case "logIn":{
									ships.add(new Ship(change[0], Double.parseDouble(change[2]), 
											Double.parseDouble(change[3]), Double.parseDouble(change[4]),
											Double.parseDouble(change[5]), change[6]));
									break;
								}
								case "logOut":{
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
											//这里不需要判断增加还是减少，增加传送正值，减少传送负值
											vessel.setValue(4, vessel.getParameter(4)+Double.parseDouble(change[2]));
											break;
										}
									}
									break;
								}
								case "course":{
									for (Ship vessel : ships) {
										if (vessel.getName().equals(change[0])) {  //这里一样，左-右+
											vessel.setValue(3, vessel.getParameter(3)+Double.parseDouble(change[2]));
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
						radarpanel.repaint();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}.start();
		while(!socket.isClosed()){   //前进同步
			try {
				radarpanel.repaint();
				ship.goAhead();
				sendData(ship.getName() + ",go");  //同步信号，向前走一步
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public synchronized void sendData(String data) throws IOException{
		output.println(data);
		output.flush();
		System.out.println("ClientThread->sendData");
	}
	
	public String getData() throws IOException{
		String data = input.readLine();
		return data;
	}
	
	/*public void sync() throws IOException{
		String command = ship.getName() + ",go";
		sendData(command);
		System.out.println("ClientThread->sycn");
	}*/
	
	public void logIn() throws IOException{
		String command = ship.getName() + ",logIn," + ship.getParameter(1) +","+ ship.getParameter(2)
						+ "," +ship.getParameter(3) +","+ ship.getParameter(4) +","+ ship.getType();
		sendData(command);
	}
	
	public void logOut() throws IOException{
		String command = ship.getName() + ",logOut";
		sendData(command);
		System.out.println("ClientThread->logOut");
		
		input.close();
		output.close();
		socket.close();
	}
}
