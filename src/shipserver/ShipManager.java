package shipserver;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class ShipManager extends JFrame {   //�������Ҫ��Ӵ����Ĺ��ܣ��������
	
	private JPanel contentPane;
	private SmallPanel smallpanel;
	private List<Socket> sockets = new ArrayList<Socket>();
	
	//������Ҫ     ��������
	private boolean changed = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShipManager frame = new ShipManager();
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
	public ShipManager() {
		//������ͨ�ţ����ܿͻ�����Ϣ                                    �����������޷�ִ��
		try {
			ServerSocket managersocket = new ServerSocket(8888);
			
			Socket socket = managersocket.accept();
			sockets.add(socket);
			
			for(int i = 0;i<sockets.size();i++){
				Socket s = sockets.get(i);
				if (s.isClosed()) {
					sockets.remove(i);
					i--;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��ʼ������
		initComponents();
	}
	
	private void initComponents() {
		setTitle("ShipManager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		smallpanel = new SmallPanel();
		smallpanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {    //������ôû���ã�������������
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE && changed) {
					smallpanel.setBounds(0, 0, 150, 150);
					changed = !changed;
				}
			}
		});
		smallpanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!changed) {
					smallpanel.setBounds(0, 0, getWidth()-8, getHeight()-35);
					revalidate();
					changed = !changed;
				}
				
			}
		});
		smallpanel.setBounds(0, 0, 150, 150);
		contentPane.add(smallpanel);
	}
}
