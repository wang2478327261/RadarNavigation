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

public class RadarNavigation extends JFrame {  //��½�����

	private String name;   //��������
	
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
	 */
	public RadarNavigation() {
		//�����û�����Ĵ�������
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
		addWindowStateListener(new WindowStateListener() {   //���¹�����Щ����
			public void windowStateChanged(WindowEvent e) {  //������setbounds���У�Ϊʲô����
				radarPanel.setSize(RadarNavigation.this.getWidth()*7/9, RadarNavigation.this.getHeight()-35);
				radarPanel.setLocation(0, 0);
				infoPanel.setSize(RadarNavigation.this.getWidth()*2/9, RadarNavigation.this.getHeight()-35);
				infoPanel.setLocation(radarPanel.getWidth(), 0);
				revalidate();System.out.println(getWidth() + "," + getHeight());
			}
		});
		//��ʼ����������
		setTitle("RadarNavigation");
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 908, 635);  //�������տ�ʼ��λ�úʹ�С
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(new Color(0, 128, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radarPanel = new radarPanel(); //�½��״���ʾ���
		radarPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//�ı��״�����
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
		
		radarPanel.setBounds(0, 0, 700, 600);
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
		
		infoPanel.setBounds(radarPanel.getWidth(), 0, 200, 600);
		contentPane.add(infoPanel);
		
	}
}
