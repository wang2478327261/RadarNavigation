package shipserver;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class ShipManager extends JFrame { // 注意界面结构的合理设计
	
	private static final long serialVersionUID = 5649146024506368826L;
	
	private SmallPanel smallpanel; // 做成全局定义可以方便以后外层调用方法

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ShipManager frame = new ShipManager();
					frame.setVisible(true);
				} catch (Exception e) {
					System.exit(1);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ShipManager() {  //把通信部分放在这里有点不方便，需要从smallpanel获取数据，然后发送
		initComponents();
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {  //判断是否连上服务端
                smallpanel.server.serverClose();
            }
        });
	}

	private void initComponents() {
		setTitle("ShipManager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1208, 735);

		smallpanel = new SmallPanel();
		setContentPane(smallpanel);
	}

}
