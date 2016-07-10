package radarnavigation;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import common.Ship;

@SuppressWarnings("serial")
public class RadarNavigation extends JFrame{  //登陆主面板
	                                            //注意：以后类名用大写开头,方法名前小写后大写，变量用小写
	private JPanel contentPane;
	private RadarPanel radarpanel;    //雷达动态显示面板
	private InfoPanel infopanel;     //信息显示面板
	private Ship ship;              //本船对象
	
	private List<Ship> ships;// = new LinkedList<Ship>();   //保存现场存在的船舶对象
	ClientThread client;            //发送信息的新线程
	
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
		addKeyListener(new KeyAdapter() {        //测试方向转换功能                本船的状态改变
			@Override
			public void keyPressed(KeyEvent e) {
				//TODO 当本船状态改变时，需要向服务端发送信息，同步显示状态
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					ship.setValue(3, ship.getParameter(3)+1);
					client.sendData(new StringBuilder());    //发出指令
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					ship.setValue(3, ship.getParameter(3)-1);
					client.sendData(new StringBuilder());
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					ship.setValue(4, ship.getParameter(4)+1);
					client.sendData(new StringBuilder());
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					ship.setValue(4, ship.getParameter(4)-1);
					client.sendData(new StringBuilder());
				}
				repaint();
			}
		});
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
		//JOptionPane.showMessageDialog(this, "本软件由\n@玉龙视觉效果工作室@\n制作\nPOWERED BY ERON STUDIO");
		//将输入数据进行分析操作，分析出名称，位置等信息     ----->**  依次输入船名、位置x y、方向、速度
		String[] source = customer.split(",");
		//这里需要继续处理只输入一部分参数的情况
		/*ship = new Ship(source[0], Double.parseDouble(source[1]),
				Double.parseDouble(source[2]), 34,      //Double.parseDouble(source[3])
				Double.parseDouble(source[4]), source[5]);  //客户端的一个船舶
		for(int i = 0; i < source.length; i++){
			System.out.println(source[i]);
		}*/
		
		ship = new Ship();             //需要将ship对象传入，以更新本船信息
		//检查服务器并发送相关信息
		//这里要进行开启发送信息的套接字                      新建线程                     启动信息传送的新线程
		client = new ClientThread(ships);
		client.start();    //开启线程，这时才实际运行
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
				//repaint();    //需要repaint吗？
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
		radarpanel.setBounds(0, 0, getWidth()*7/9, getHeight()-35);
		contentPane.add(radarpanel);
		radarpanel.getShip(ship);    //将船舶对象传入加载在这里，可运行
		//雷达单击响应事件
		radarpanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {   //这里可以加入时间测试，实现功能之间的区别，网上擦还训
				//选中对方船舶或或者取消选中（右键单击）
				if(e.getButton() == MouseEvent.BUTTON1){   //左键 16，中键 8，右键 4    e.getModifiers() == 16
					//单击事件
					if (e.getClickCount() == 1) {    //左键 单击一次
						//添加雷达对象到信息面板
						Iterator<Ship> index = ships.iterator();  //原因搜索解决
						while (index.hasNext()) {
							Ship boat = index.next();
							if (Math.abs(e.getX()-boat.getParameter(1))<10 && 
									Math.abs(e.getY()-boat.getParameter(2))<10) {
								infopanel.addShip(ship);
							}
							else {
								System.out.println("mei you");
							}
						}
						
					}
					//全屏动作
					if (e.getClickCount() >= 2) {   //这里可以添加时间判断，实现更精确的控制
						if (!isUndecorated()) {
							setLocation(0, 0);
							setSize(Toolkit.getDefaultToolkit().getScreenSize());
							//去除标题栏
							dispose();
							setUndecorated(true);
							setVisible(true);
							//radarpanel.setSize(getWidth()*7/9, getHeight()-35);
						}
						else {
							setBounds(20, 20, 1008, 735);
							//归位,返回原来的尺寸，只能到初始化尺寸，若果要放大前，需要增加变量存储之前的尺寸及位置
							dispose();
							setUndecorated(false);
							setVisible(true);
							//radarpanel.setSize(getWidth()*7/9, getHeight());
						}
						revalidate();
					}
					
				}
			}
		});
		
		infopanel = new InfoPanel();   //新建信息显示面板
		infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
		contentPane.add(infopanel);
		//信息面板响应
		infopanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//单击后显示菜单，变换界面  设置整体属性
				revalidate();
				if (e.getButton() == MouseEvent.BUTTON1) {
					//进入菜单界面
				}
				else if (e.getButton() == MouseEvent.BUTTON3) {
					//退出菜单界面
				}
			}
		});
		
	}
	
	
}
