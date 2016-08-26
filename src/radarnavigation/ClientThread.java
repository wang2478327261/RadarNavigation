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
 * �����ʵ�ֿͻ��˵�ͨ�Ź��ܣ� ���߳�  
 * @author ERON  
 * @see RadarNavigation  
 */
public class ClientThread extends Thread{  //���������仯��Ϣ  ---���������ܸ��¶Է���������Ϣ
	
	private Socket socket;  //���ͻ��˵��׽���
	private BufferedReader input;
	private PrintWriter output;
	private Ship ship;
	private List<Ship> ships;   //����洢���ǶԷ��Ĵ��������б�
	//ͨ���ж��׽��־���ѭ���ļ���
	//public boolean logOut = false;
	
	/**
	 * <p>����ָ������  </p>
	 * <p>�ͻ��ˣ� ���Ҵ�--  �ĸ�״̬--  �仯�˶���    type</p>
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
		// TODO �½��׽��ֲ�������������
		//super.run();
		try {
			socket = new Socket(InetAddress.getLocalHost(),8888);  //��һ���˿ں�3333���׽���
			System.out.println(socket);
			//������������
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			
			logIn();  //���͵�¼��Ϣ
			
		} catch (IOException e) {
			// TODO ���Ӳ��Ϸ������Ĵ���
			//e.printStackTrace();
			System.out.println("Server is not Exist OR is not Connected!");
			System.exit(1);
		}
		new Thread(){  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<�򿪽����߳�,��������
			public void run() {
				while(!socket.isClosed()){ //���û�еǳ�����ѭ������ͬ��
					try {
						String data = getData();
						String[] change = data.split(",");  //�ָ���
						if (change[0].equals(ship.getName())) {
							continue;
						}
						else{
							switch(change[1]){
								case "logIn":{  //���´�����ϵͳ
									ships.add(new Ship(change[0], Double.parseDouble(change[2]), 
											Double.parseDouble(change[3]), Double.parseDouble(change[4]),
											Double.parseDouble(change[5]), change[6]));
									break;
								}
								case "logOut":{  //�����ǳ�ϵͳ
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
		while(!socket.isClosed()){   //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ͬ�����ݣ�������Ϣ�������
			try {
				System.out.println("logout loops");
				sleep(1000);
				ship.goAhead();  //������ǰ��һ��
				//	ship.printShip();
				sync();  //�����˷���ͬ���ź�
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
	 * <p>����--�����ͱ���״̬�仯      //changeType// ������Ψһʶ�������״̬�� �仯ֵ  </p>
	 * <p>		//-->����ͬ����Ϣ      //changeType// ����  ״̬ �仯ֵ, �仯ֵ��һ�Ĺ��̣�  �����������
	 * <p>����--�� changeType�ж��Ǳ仯���Ǵ������� </p>
	 * <p>		��������״̬�仯        changeType ������ ״̬��ֵ  </p>
	 * <p>		���ܴ�����ǳ�������Ϣ      changeType ��������������ɾ��  </p>
	 * @return null
	 * @throws IOException
	 */
	public synchronized void sendData(String data) throws IOException{  //���������ظ���������ʱ�����
		// TODO �����ַ���������Ϣ
		output.println(data);
		output.flush();
		System.out.println("sendData");
	}
	
	public String getData() throws IOException{   //����������࣬�ô����������
		// TODO �ӷ���˽��������ͻ��˻����Ƿ������ı仯��Ϣ
		String data = input.readLine();   //��UTF�ĸ�ʽ�����ַ���
		
		System.out.println("getData");
		return data;
	}
	
	public void sync() throws IOException{  //ÿ��һ��ͬ��һ��
		// TODO ��½�����
		String command = ship.getName() + ",go";
		sendData(command);
		System.out.println("sycn");
	}
	
	public void logIn() throws IOException{
		//TODO ��½�ͻ���
		String command = ship.getName() + ",logIn," + ship.getParameter(1) +","+ ship.getParameter(2)
						+ "," +ship.getParameter(3) +","+ ship.getParameter(4) +","+ ship.getType();
		sendData(command);
		System.out.println("logIn");
	}
	
	public void logOut() throws IOException{
		//TODO �˳��ͻ���
		String command = ship.getName() + ",logOut";
		sendData(command);
		System.out.println("logOut");
		
		input.close();
		output.close();
		socket.close();
	}
}
