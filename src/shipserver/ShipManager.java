package shipserver;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class ShipManager extends JFrame implements Runnable{   //服务端需要添加船舶的功能，方便测试
	
	private JPanel contentPane;
	private SmallPanel smallpanel;
	private List<Socket> sockets = new ArrayList<Socket>();
	
	//功能需要     变量区域
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
		initComponents();
		//打开网络通信，接受客户端消息                                    这里有问题无法执行      新建线程执行
		new Thread(this).start();   //开启新线程
		//初始化界面
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
			public void keyPressed(KeyEvent e) {    //这里怎么没有用？？？？？？？
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			ServerSocket serversocket = new ServerSocket(8888);
			Socket connectionsocket = serversocket.accept();
			BufferedReader bufferedoeader = new BufferedReader(new InputStreamReader(connectionsocket.getInputStream()));
			DataOutputStream dataoutputotream = new DataOutputStream(connectionsocket.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
