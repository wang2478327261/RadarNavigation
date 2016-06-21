package RadarNavigation;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
		radarpanel.refer(this);   //������������ô����״�����ϣ����ƽ���
		
		radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
		contentPane.add(radarpanel);
		
		infopanel = new InfoPanel();   //�½���Ϣ��ʾ���
		infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
		contentPane.add(infopanel);
		
	}
}
