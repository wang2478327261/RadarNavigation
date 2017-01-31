package radarnavigation;

import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import common.Ship;

@SuppressWarnings("serial")
public class RadarNavigation extends JFrame{  //��½�����
	                                            //ע�⣺�Ժ������ô�д��ͷ,��������Сд���д��������Сд
	private JPanel contentPane;
	private RadarPanel radarpanel;    //�״ﶯ̬��ʾ���
	private InfoPanel infopanel;     //��Ϣ��ʾ���
	private Ship ship;              //��������
	
	private List<Ship> ships = new LinkedList<Ship>();   //�����ֳ����ڵĴ�������
	ClientThread client;            //������Ϣ�����߳�
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RadarNavigation frame = new RadarNavigation();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 * ����һ�����봬�����ƵĴ���
	 * ֮�������׽����߳������������ͨ�ţ�������Ҫдһ���߳��࣬ͬʱ���ͨ��Э��
	 * ����ĳ�ʼ���������ڽ���Ķ�̬����
	 * @throws IOException 
	 */
	public RadarNavigation() throws IOException {
		addKeyListener(new KeyAdapter() {        //���Է���ת������                ������״̬�ı�
			@Override
			public void keyPressed(KeyEvent e) {
				//TODO ������״̬�ı�ʱ����Ҫ�����˷�����Ϣ��ͬ����ʾ״̬
				String command = "";
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					ship.setValue(3, ship.getParameter(3)+1);
					command = ship.getName() + ",course," + "starboard";  //ʹ�ÿո���зָ�
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					ship.setValue(3, ship.getParameter(3)-1);
					command = ship.getName() + ",course," + "port";
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					ship.setValue(4, ship.getParameter(4)+1);
					command = ship.getName() + ",speed," + "increase";
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					ship.setValue(4, ship.getParameter(4)-1);
					command = ship.getName() + ",speed,"  + "reduce";
				}
				try {
					client.sendData(command);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				repaint();
				System.out.println("RadarNavigation -> keyPress" + command);
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//�ڹرտͻ��˵�ʱ����ע����Ϣ
				try {
					client.logOut();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		//�����û�����Ĵ�������,�����������м���λ����Ϣ�������ٴ�����Ƭ������ȫ�ֵ�ͼ���ڷ�������
		String customer = JOptionPane.showInputDialog(this, "Please input Ship name and position : ");
		while(customer == null || customer.equals("")){
			if (customer == null) {
				this.dispose();
				System.exit(0);
			}
			JOptionPane.showMessageDialog(this, "you should input ship infoemation !", "Warning", JOptionPane.ERROR_MESSAGE);
			customer = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		}
		//JOptionPane.showMessageDialog(this, "�������\n@�����Ӿ�Ч��������@\n����\nPOWERED BY ERON STUDIO");
		//���������ݽ��з������������������ƣ�λ�õ���Ϣ     ----->**  �������봬����λ��x y�������ٶ�
		//String[] source = customer.split(",");
		//������Ҫ��������ֻ����һ���ֲ��������
		/*ship = new Ship(source[0], Double.parseDouble(source[1]),
				Double.parseDouble(source[2]), 34,      //Double.parseDouble(source[3])
				Double.parseDouble(source[4]), source[5]);  //�ͻ��˵�һ������
		for(int i = 0; i < source.length; i++){
			System.out.println(source[i]);
		}*/
		ship = new Ship();             //��Ҫ��ship�����룬�Ը��±�����Ϣ
		/**********************************************************************/
		//����Ҫ���п���������Ϣ���׽���                      �½��߳�                     ������Ϣ���͵����߳�
		client = new ClientThread(ship, ships);  //����ship����Ϊ���ܹ������߳��п��Ʊ�����ǰ����ͬ��
		client.start();    //�����̣߳���ʱ��ʵ������
		/*********************************************************************/
		//��ʼ������
		initComponents();
	}
	
	private void initComponents() {
		//�����Ź������н������仯
		addComponentListener(new ComponentAdapter() {
			@Override          //windows resized， then relayout
			public void componentResized(ComponentEvent e) {   //�����齨�Ĵ�С
				if (!isUndecorated()) {  //û��ȫ��״̬�µĲ���           ȫ��״̬��û��װ�ε�
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
				}
				else {
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight());
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight());
				}
				revalidate();  //����ˢ��
				//repaint();    //��Ҫrepaint��
			}
		});
		
		//��ʼ����������
		setTitle("RadarNavigation");
		setBackground(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1008, 735);  //�������տ�ʼ��λ�úʹ�С   1000  + 700
		
		contentPane = new JPanel();
		contentPane.setBackground(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);  //���Ʋ���
		
		radarpanel = new RadarPanel(); //�½��״���ʾ���
		radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
		contentPane.add(radarpanel);
		radarpanel.getShip(ship);    //��������������������������
		//�״ﵥ����Ӧ�¼�
		radarpanel.addMouseListener(new MouseAdapter() {
			
			@Override  //全屏功能
			public void mouseClicked(MouseEvent e) {   //������Լ���ʱ����ԣ�ʵ�ֹ���֮����������ϲ���ѵ
				//ѡ�жԷ����������ȡ��ѡ�У��Ҽ�������
				if(e.getButton() == MouseEvent.BUTTON1){   //��� 16���м� 8���Ҽ� 4    e.getModifiers() == 16
					//�����¼�
					if (e.getClickCount() == 1) {    //��� ����һ��
						//����״������Ϣ���
						Iterator<Ship> index = ships.iterator();  //ԭ���������
						while (index.hasNext()) {   //����������Ӧ�ø�����ʾЧ�������Ĵ������Ա�־ �� ׷��ģʽ����������
							Ship boat = index.next();
							if (Math.abs(e.getX()-boat.getParameter(1))<10 && 
									Math.abs(e.getY()-boat.getParameter(2))<10) {
								infopanel.addShip(ship);
							}
							else {
								System.out.println("mei you");
							}
						}
						
					}
					//ȫ������
					if (e.getClickCount() >= 2) {   //����������ʱ���жϣ�ʵ�ָ���ȷ�Ŀ���
						if (!isUndecorated()) {
							setLocation(0, 0);
							setSize(Toolkit.getDefaultToolkit().getScreenSize());
							//ȥ��������
							dispose();
							setUndecorated(true);
							setVisible(true);
							radarpanel.setSize(getWidth()*7/9, getHeight()-35);  //�����С��ȫ��ʱ����ı仯����
						}
						else {
							setBounds(20, 20, 1008, 735);
							//��λ,����ԭ���ĳߴ磬ֻ�ܵ���ʼ���ߴ磬����Ҫ�Ŵ�ǰ����Ҫ���ӱ����洢֮ǰ�ĳߴ缰λ��
							dispose();
							setUndecorated(false);
							setVisible(true);
							radarpanel.setSize(getWidth()*7/9, getHeight());
						}
						revalidate();
					}
				}
			}
		});
		//------------------------------------>�Ķ���ʹ�ý�����Թ���
		infopanel = new InfoPanel();   //�½���Ϣ��ʾ���
		infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
		//ScrollPane sp = new ScrollPane();
		//sp.add(infopanel);
		contentPane.add(infopanel);
		//contentPane.add(sp);
		//��Ϣ�����Ӧ
		infopanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//��������ʾ�˵����任����  ������������
				revalidate();
				if (e.getButton() == MouseEvent.BUTTON1) {  //�˵�ģʽ��Ҫ���ģʽ��־��1 ������ʾ��Ϣ��0 ���� ��ʾ�˵������²���
					//����˵�����
				}
				else if (e.getButton() == MouseEvent.BUTTON3) {
					//�˳��˵�����
				}
			}
		});
		
	}
	
}
