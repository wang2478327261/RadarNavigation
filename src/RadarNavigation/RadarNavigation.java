package RadarNavigation;

import java.awt.EventQueue;
import java.awt.Toolkit;
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RadarNavigation extends JFrame {  //登陆主面板
	                                            //注意：以后类名用大写开头,方法名前小写后大写，变量用小写
	private JPanel contentPane;
	private RadarPanel radarpanel;    //雷达动态显示面板
	private InfoPanel infopanel;     //信息显示面板
	
	private boolean fullScreen = false;  //判断是否在全屏状态
	
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
		String name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		while(name == null || name.equals("")){
			if (name == null) {
				this.dispose();
				System.exit(0);
			}
			JOptionPane.showMessageDialog(this, "you should input ship name !", "Warning", JOptionPane.ERROR_MESSAGE);
			name = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		}
		
		Ship ship = new Ship();  //客户端的一个船舶
		ship.setName(name);
		
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
				if (!fullScreen) {  //没有全屏
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
					revalidate();  //刷新组件
					//System.out.println(getWidth() + "," + getHeight());
				}
				else {
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight());
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight());
					//infopanel.setVisible(false);
					revalidate();  //刷新组件
				}
				
			}
		});
		
		//初始化其他属性
		setTitle("RadarNavigation");
		setBackground(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1008, 735);  //设置面板刚开始的位置和大小
		
		contentPane = new JPanel();
		contentPane.setBackground(null);
		contentPane.setForeground(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radarpanel = new RadarPanel(); //新建雷达显示面板
		
		radarpanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//选中对方船舶或或者取消选中（右键单击）
				if(e.getButton() == MouseEvent.BUTTON1){   //左键 16，中键 8，右键 4    e.getModifiers() == 16
					//单机事件
					if (e.getClickCount() >= 2) {
						if (!fullScreen) {
							fullScreen = !fullScreen;
							setLocation(0, 0);
							setSize(Toolkit.getDefaultToolkit().getScreenSize());
							//去除标题栏
							dispose();
							setUndecorated(true);
							setVisible(true);
						}
						else {
							fullScreen = !fullScreen;
							setBounds(20, 20, 1008, 735);
							//归位,返回原来的尺寸，只能到初始化尺寸，若果要放大前，需要增加变量存储之前的尺寸及位置
							dispose();
							setUndecorated(false);
							setVisible(true);
							//infopanel.setVisible(true);
						}
						revalidate();
					}
				}
				if(e.getButton() == MouseEvent.BUTTON3){
					//实现取消选中功能
					setTitle("RadarNavigation -->" + e.getX()  + "," + e.getY());
				}
			}
		});
		
		radarpanel.setBounds(0, 0, (getWidth()-8)*7/9, getHeight()-35);
		contentPane.add(radarpanel);
		
		infopanel = new InfoPanel();   //新建信息显示面板
		infopanel.setBounds(radarpanel.getWidth(), 0, (getWidth()-8)*2/9, getHeight()-35);
		contentPane.add(infopanel);
		
	}
}
