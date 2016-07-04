package shipserver;

import java.awt.EventQueue;
import javax.swing.BorderFactory;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ShipManager extends JFrame{   //服务端需要添加船舶的功能，方便测试
	
	private SmallPanel smallpanel;
	
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
		//打开网络通信，接受客户端消息
		//ServerThread server = new ServerThread();
		//初始化界面
		initComponents();
		//repaint();
	}
	
	private void initComponents() {
		setTitle("ShipManager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1208, 735);
		smallpanel = new SmallPanel();
		smallpanel.setBorder(BorderFactory.createEmptyBorder());
		smallpanel.setLayout(null);
		setContentPane(smallpanel);
	}
	
}
