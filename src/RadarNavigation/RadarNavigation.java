package RadarNavigation;

import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RadarNavigation extends JFrame {  //��½�����
	                                            //ע�⣺�Ժ������ô�д��ͷ,������ǰСд���д��������Сд
	private JPanel contentPane;
	private RadarPanel radarpanel;    //�״ﶯ̬��ʾ���
	private InfoPanel infopanel;     //��Ϣ��ʾ���
	
	private boolean fullScreen = false;  //�ж��Ƿ���ȫ��״̬
	
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
		String name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		while(name == null || name.equals("")){
			if (name == null) {
				this.dispose();
				System.exit(0);
			}
			JOptionPane.showMessageDialog(this, "you should input ship name !", "Warning", JOptionPane.ERROR_MESSAGE);
			name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		}
		
		Ship ship = new Ship();  //�ͻ��˵�һ������
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
				if (!fullScreen) {  //û��ȫ��
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
					revalidate();  //ˢ�����
					//System.out.println(getWidth() + "," + getHeight());
				}
				else {
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight());
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight());
					//infopanel.setVisible(false);
					revalidate();  //ˢ�����
				}
				
			}
		});
		
		//��ʼ����������
		setTitle("RadarNavigation");
		setBackground(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1008, 735);  //�������տ�ʼ��λ�úʹ�С
		
		contentPane = new JPanel();
		contentPane.setBackground(null);
		contentPane.setForeground(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radarpanel = new RadarPanel(); //�½��״���ʾ���
		
		radarpanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//ѡ�жԷ����������ȡ��ѡ�У��Ҽ�������
				if(e.getButton() == MouseEvent.BUTTON1){   //��� 16���м� 8���Ҽ� 4    e.getModifiers() == 16
					//�����¼�
					if (e.getClickCount() >= 2) {
						if (!fullScreen) {
							fullScreen = !fullScreen;
							setLocation(0, 0);
							setSize(Toolkit.getDefaultToolkit().getScreenSize());
							//ȥ��������
							dispose();
							setUndecorated(true);
							setVisible(true);
						}
						else {
							fullScreen = !fullScreen;
							setBounds(20, 20, 1008, 735);
							//��λ,����ԭ���ĳߴ磬ֻ�ܵ���ʼ���ߴ磬����Ҫ�Ŵ�ǰ����Ҫ���ӱ����洢֮ǰ�ĳߴ缰λ��
							dispose();
							setUndecorated(false);
							setVisible(true);
							//infopanel.setVisible(true);
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
		
		radarpanel.setBounds(0, 0, (getWidth()-8)*7/9, getHeight()-35);
		contentPane.add(radarpanel);
		
		infopanel = new InfoPanel();   //�½���Ϣ��ʾ���
		infopanel.setBounds(radarpanel.getWidth(), 0, (getWidth()-8)*2/9, getHeight()-35);
		contentPane.add(infopanel);
		
	}
}
