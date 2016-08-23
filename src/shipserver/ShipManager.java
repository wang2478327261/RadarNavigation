package shipserver;

import java.awt.EventQueue;
import java.awt.Point;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import common.Ship;

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
	public ShipManager() {  //smallpanel中新建server线程，传入参数，进行控制，所以主类只是一个壳子
		initComponents();
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
