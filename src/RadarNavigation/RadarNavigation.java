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
import java.beans.PropertyChangeListener;
import java.util.concurrent.Exchanger;
import java.beans.PropertyChangeEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RadarNavigation extends JFrame {  //登陆主面板

	private String name;   //船舶名称
	private int range = 6;  //雷达量程  最大24海里，最小3海里, 初始化为6海里
	private int mode = 0;  //雷达显示模式    北向上  船首向上    /////相对运动  绝对运动
	private boolean headLine = true;  //开启或关闭船首线
	
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
	 * 弹出一个输入船舶名称的窗口
	 * 之后启动套接字线程与服务器进行通信，这里需要写一个线程类，同时设计通信协议
	 * 界面的初始化，窗口内界面的动态布局
	 */
	public RadarNavigation() {
		//处理用户输入的船舶名称,可以在名字中加入位置信息，后期再处理切片出来，全局地图放在服务器上
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
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {   //更新组建的大小
				radarPanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
				infoPanel.setBounds(radarPanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
				revalidate();  //刷新组件
				//System.out.println(getWidth() + "," + getHeight());
			}
		});
		
		//初始化其他属性
		setTitle("RadarNavigation");
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1008, 735);  //设置面板刚开始的位置和大小
		
		contentPane = new JPanel();
		contentPane.setBackground(null);
		contentPane.setForeground(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radarPanel = new radarPanel(range, mode, headLine); //新建雷达显示面板
		radarPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//改变雷达量程,   向上滚动为负值 -1，向下滚动正值  1
				if (e.getWheelRotation() > 0) {   //减小量程
					range /= 2;
					if (range < 1) {
						range = 1;
					}
				}
				if(e.getWheelRotation() < 0){  //增大量程
					if (range == 1) {
						range = 3;
					}
					else{
						range *= 2;
						if (range > 24) {
							range = 24;
						}
					}
				}
				System.out.println(range);
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
		
		radarPanel.setBounds(0, 0, (getWidth()-8)*7/9, getHeight()-35);
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
		
		infoPanel.setBounds(radarPanel.getWidth(), 0, (getWidth()-8)*2/9, getHeight()-35);
		contentPane.add(infoPanel);
		
	}
}
