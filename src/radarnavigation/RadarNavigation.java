package radarnavigation;

import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import common.Ship;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class RadarNavigation extends JFrame {  //��½�����
	                                            //ע�⣺�Ժ������ô�д��ͷ,������ǰСд���д��������Сд
	private JPanel contentPane;
	private RadarPanel radarpanel;    //�״ﶯ̬��ʾ���
	private InfoPanel infopanel;     //��Ϣ��ʾ���
	private Ship ship;
	
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
	 */
	public RadarNavigation() {
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
		//���������ݽ��з������������������ƣ�λ�õ���Ϣ
		String name = "Default";
		
		ship = new Ship();  //�ͻ��˵�һ������
		ship.setName(name);
		
		//�������������������Ϣ
		//����Ҫ���п���������Ϣ���׽���
		
		//��ʼ������
		initComponents();
	}
	
	private void initComponents() {
		//�����Ź������н������仯
		addComponentListener(new ComponentAdapter() {
			@Override
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
				repaint();
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
		//�״ﵥ����Ӧ�¼�
		radarpanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {   //������Լ���ʱ����ԣ�ʵ�ֹ���֮����������ϲ���ѵ
				//ѡ�жԷ����������ȡ��ѡ�У��Ҽ�������
				if(e.getButton() == MouseEvent.BUTTON1){   //��� 16���м� 8���Ҽ� 4    e.getModifiers() == 16
					//�����¼�
					if (e.getClickCount() >= 2) {
						if (!isUndecorated()) {
							setLocation(0, 0);
							setSize(Toolkit.getDefaultToolkit().getScreenSize());
							//ȥ��������
							dispose();
							setUndecorated(true);
							//getRootPane().setWindowDecorationStyle(JRootPane.NONE);
							setVisible(true);
						}
						else {
							setBounds(20, 20, 1008, 735);
							//��λ,����ԭ���ĳߴ磬ֻ�ܵ���ʼ���ߴ磬����Ҫ�Ŵ�ǰ����Ҫ���ӱ����洢֮ǰ�ĳߴ缰λ��
							dispose();
							setUndecorated(false);
							setVisible(true);
						}
						revalidate();
					}
				}
				if(e.getButton() == MouseEvent.BUTTON3){
					//ʵ��ȡ��ѡ�й���
					setTitle("RadarNavigation -->" + e.getX()  + "," + e.getY());
				}
			}
		});
		
		infopanel = new InfoPanel();   //�½���Ϣ��ʾ���
		infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
		contentPane.add(infopanel);
		//��Ϣ�����Ӧ
		infopanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//��������ʾ�˵����任����  ������������
				revalidate();
				if (e.getButton() == MouseEvent.BUTTON1) {
					//����˵�����
				}
				else if (e.getButton() == MouseEvent.BUTTON3) {
					//�˳��˵�����
				}
			}
		});
		
	}
}
