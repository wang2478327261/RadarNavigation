package shipserver;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class ShipManager extends JFrame{  //注意设计界面系统的结构，方便数据传递
	
	private static final long serialVersionUID = 5649146024506368826L;
	
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
	}
	
	private void initComponents() {
		setTitle("ShipManager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1208, 735);
		smallpanel = new SmallPanel();
		setContentPane(smallpanel);
	}
	
}
