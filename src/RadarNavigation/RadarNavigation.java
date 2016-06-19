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

public class RadarNavigation extends JFrame {  //登陆主面板

	private String name;   //船舶名称
	
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
		//处理用户输入的船舶名称
		name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		while(name == null || name.equals("")){
			if (name == null) {
				this.dispose();
				System.exit(0);
			}
			JOptionPane.showMessageDialog(this, "you should input ship name !", "Warning", JOptionPane.ERROR_MESSAGE);
			name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		}
		
		//检查服务器并发送相关信息
		//这里要进行开启发送信息的套接字
		
		//初始化界面
		initComponents();
	}
	
	private void initComponents() {
		//在缩放过程中中界面跟随变化
		addWindowStateListener(new WindowStateListener() {   //更新过程有些问题
			public void windowStateChanged(WindowEvent e) {  //这里用setbounds不行，为什么？？
				radarPanel.setSize(RadarNavigation.this.getWidth()*7/9, RadarNavigation.this.getHeight()-35);
				radarPanel.setLocation(0, 0);
				infoPanel.setSize(RadarNavigation.this.getWidth()*2/9, RadarNavigation.this.getHeight()-35);
				infoPanel.setLocation(radarPanel.getWidth(), 0);
				revalidate();System.out.println(getWidth() + "," + getHeight());
			}
		});
		//初始化其他属性
		setTitle("RadarNavigation");
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 908, 635);  //设置面板刚开始的位置和大小
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(new Color(0, 128, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radarPanel = new radarPanel(); //新建雷达显示面板
		radarPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//改变雷达量程
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
				//选中对方船舶或或者取消选中（右键单击）
				if(e.getModifiers() == MouseEvent.BUTTON1){
					//单机事件
				}
				if(e.getModifiers() == MouseEvent.BUTTON2){
					//实现取消选中功能
				}
			}
		});
		
		radarPanel.setBounds(0, 0, 700, 600);
		contentPane.add(radarPanel);
		
		infoPanel = new infoPanel();   //新建信息显示面板
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
