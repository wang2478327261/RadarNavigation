package RadarNavigation;

import java.awt.EventQueue;
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
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Exchanger;
import java.beans.PropertyChangeEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RadarNavigation extends JFrame {  //��½�����

	private String name;   //��������
	private int range = 6;  //�״�����  ���24�����С3����, ��ʼ��Ϊ6����
	private int mode = 0;  //�״���ʾģʽ    ������  ��������    /////����˶�  �����˶�
	private boolean headLine = true;  //������رմ�����
	
	private JPanel contentPane;
	private JPanel radarPanel;
	private JPanel infoPanel;
	
	/**
	 * Launch the application.
	 */
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
		name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		while(name == null || name.equals("")){
			if (name == null) {
				this.dispose();
				System.exit(0);
			}
			JOptionPane.showMessageDialog(this, "you should input ship name !", "Warning", JOptionPane.ERROR_MESSAGE);
			name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		}
		
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
				radarPanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
				infoPanel.setBounds(radarPanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
				revalidate();  //ˢ�����
				//System.out.println(getWidth() + "," + getHeight());
			}
		});
		
		//��ʼ����������
		setTitle("RadarNavigation");
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1008, 735);  //�������տ�ʼ��λ�úʹ�С
		
		contentPane = new JPanel();
		contentPane.setBackground(null);
		contentPane.setForeground(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radarPanel = new radarPanel(range, mode, headLine); //�½��״���ʾ���
		radarPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//�ı��״�����,   ���Ϲ���Ϊ��ֵ -1�����¹�����ֵ  1
				if (e.getWheelRotation() > 0) {   //��С����
					range /= 2;
					if (range < 1) {
						range = 1;
					}
				}
				if(e.getWheelRotation() < 0){  //��������
					if (range == 1) {
						range = 3;
					}
					else{
						range *= 2;
						if (range > 24) {
							range = 24;
						}
					}
				}
				System.out.println(range);
			}
		});
		
		radarPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				radarPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				radarPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				//ѡ�жԷ����������ȡ��ѡ�У��Ҽ�������
				if(e.getModifiers() == MouseEvent.BUTTON1){
					//�����¼�
				}
				if(e.getModifiers() == MouseEvent.BUTTON2){
					//ʵ��ȡ��ѡ�й���
				}
			}
		});
		
		radarPanel.setBounds(0, 0, (getWidth()-8)*7/9, getHeight()-35);
		contentPane.add(radarPanel);
		
		infoPanel = new infoPanel();   //�½���Ϣ��ʾ���
		infoPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				infoPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				infoPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		});
		
		infoPanel.setBounds(radarPanel.getWidth(), 0, (getWidth()-8)*2/9, getHeight()-35);
		contentPane.add(infoPanel);
		
	}
}
