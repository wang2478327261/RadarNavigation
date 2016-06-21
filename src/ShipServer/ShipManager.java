package ShipServer;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ShipManager extends JFrame {   //服务端需要添加船舶的功能，方便测试

	private JPanel contentPane;
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
		//打开网络通信，接受客户端消息
		
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
		smallpanel.refer(this);
		smallpanel.setBounds(0, 0, 150, 150);
		contentPane.add(smallpanel);
	}
	
}
