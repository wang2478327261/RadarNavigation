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
 * �����ʵ�ֿͻ��˵�ͨ�Ź��ܣ� ���߳�  
 * @author ERON  
 * @see RadarNavigation  
 */
public class ClientThread extends Thread{  //���������仯��Ϣ  ---���������ܸ��¶Է���������Ϣ
	
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Ship ship;
	private List<Ship> ships;   //����洢���ǶԷ��Ĵ��������б�
	
	public boolean logOut = false;
	
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
		super.run();
		try {
			socket = new Socket(InetAddress.getLocalHost(),3333);  //��һ���˿ں�3333���׽���
			//������������
			input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
		} catch (IOException e) {
			// TODO ���Ӳ��Ϸ������Ĵ���
			//e.printStackTrace();
			System.out.println("Server is not Exist OR is not Connected!");
			//System.exit(1);
		}
		new Thread(){  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<�򿪽����߳�,��������
			public void run() {
				while(!logOut){ //���û�еǳ�����ѭ������ͬ��
					try {
						String data = getData();
						String[] change = data.split("s");  //�ָ���
						if (change[1].equals("change")) {
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
		while(!logOut){   //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ͬ�����ݣ�������Ϣ�������
			try {
				System.out.println("logout loops");
				sleep(1000);
				ship.goAhead();  //������ǰ��һ��
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
	 * @return
	 * @throws IOException
	 */
	public synchronized void sendData(String data) throws IOException{  //���������ظ���������ʱ�����
		// TODO �����ַ���������Ϣ
		output.writeUTF(data);
		output.flush();
		System.out.println("sendData");
	}
	
	public String getData() throws IOException{
		// TODO �ӷ���˽��������ͻ��˻����Ƿ������ı仯��Ϣ
		String data = null;
		data = input.readUTF();   //��UTF�ĸ�ʽ�����ַ���
		
		System.out.println("getData");
		return data;
	}
	
	public void sync() throws IOException{  //ÿ��һ��ͬ��һ��
		// TODO ��½�����
		String command = ship.getName() + " go";
		sendData(command);
	}
	
	public void logIn() throws IOException{
		//TODO ��½�ͻ���
		String command = ship.getName() + " logIn";
		sendData(command);
	}
	
	public void logOut(String info) throws IOException{
		//TODO �˳��ͻ���
		String command = ship.getName() + " logOut";
		sendData(command);
	}
}
