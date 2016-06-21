package RadarNavigation;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@SuppressWarnings("serial")
public class RadarNavigation extends JFrame {  //登陆主面板
	                                            //注意：以后类名用大写开头,方法名前小写后大写，变量用小写
	private JPanel contentPane;
	private RadarPanel radarpanel;    //雷达动态显示面板
	private InfoPanel infopanel;     //信息显示面板
	private Ship ship;
	
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
		String customer = JOptionPane.showInputDialog(this, "Please input Ship name and position : ");
		while(customer == null || customer.equals("")){
			if (customer == null) {
				this.dispose();
				System.exit(0);
			}
			JOptionPane.showMessageDialog(this, "you should input ship infoemation !", "Warning", JOptionPane.ERROR_MESSAGE);
			customer = JOptionPane.showInputDialog(this, "Please input Ship name : ");
		}
		//将输入数据进行分析操作，分析出名称，位置等信息
		String name = "Default";
		
		ship = new Ship();  //客户端的一个船舶
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
				if (!isUndecorated()) {  //没有全屏状态下的布局           全局状态是没有装饰的
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
				}
				else {
					radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight());
					infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight());
				}
				revalidate();  //布局刷新
				repaint();
			}
		});
		
		//初始化其他属性
		setTitle("RadarNavigation");
		setBackground(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1008, 735);  //设置面板刚开始的位置和大小   1000  + 700
		
		contentPane = new JPanel();
		contentPane.setBackground(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);  //定制布局
		
		radarpanel = new RadarPanel(); //新建雷达显示面板
		radarpanel.refer(this);   //将主界面的引用传到雷达面板上，控制界面
		
		radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
		contentPane.add(radarpanel);
		
		infopanel = new InfoPanel();   //新建信息显示面板
		infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
		contentPane.add(infopanel);
		
	}
}
