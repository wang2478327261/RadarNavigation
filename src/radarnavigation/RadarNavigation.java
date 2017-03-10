package radarnavigation;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import common.Ship;

/**
 * @author ERON
 * 主类
 */
public class RadarNavigation extends JFrame{  //客户端的主类
	//有一个问题不明白，将数据添加到多个链表的形式是什么，引用还是会复制一个新对象？测试过，不是新对象，但是赋值为null之后为何还是存在？？
    private static final long serialVersionUID = 4076498288039253119L;
    
    private RadarPanel radarpanel;    //雷达显示面板
    private InfoPanel infopanel;     //右侧的信息面板
    private JScrollPane jsp;  //将右侧信息板添加-->滚动界面
    private Ship ship;              //船舶对象，本船的对象
    
    private List<Ship> ships = new LinkedList<Ship>();   //其他船舶信息,应当传递绘制面板局部的，因为只能在圆内绘制
    //private List<Ship> innerShips = new LinkedList<>();  //雷达面板显示的船舶信息，再想想还有什么好办法
    													//使用树结构怎么样？大于量程加入左子树，小于量程加入右子树
    ClientThread client;            //客户端通信线程
    public Ship test = new Ship("huawei",123,22,20,10,"Normal");  //测试使用.....................................
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RadarNavigation frame = new RadarNavigation();
                    frame.setVisible(true);
                    //frame.ships.add(new Ship());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the frame.
     * @author ERON
     * @see RadarPanel
     * @throws IOException
     */
    public RadarNavigation() throws IOException {  //录取客户端船舶信息，暂时录入名称和位置信息
    	
        String customer = JOptionPane.showInputDialog(this, "Please input Ship name and position : ");
        while (customer == null || customer.equals("")) {
            if (customer == null) {
                this.dispose();
                System.exit(0);
            }
            JOptionPane.showMessageDialog(this, "you should input ship infoemation !", "Warning", JOptionPane.ERROR_MESSAGE);
            customer = JOptionPane.showInputDialog(this, "Please input Ship name an position : ");
        }  //输入的格式：船名，经度，纬度，航向，航速
        String[] source = customer.split(",");  //这里需要判断完整性，暂时不做，没意思
        if (source.length!=5) {
			ship = new Ship();
			ship.setName(Calendar.getInstance().getTimeInMillis()+"");
			//System.out.println("信息不完整，创建默认船舶-->" + ship.toString());
		}
        else{
			for(Ship ship:ships){
				if (source[0].equals(ship.getName())) {
					JOptionPane.showMessageDialog(null, "your name had exist!");
					System.exit(0);
				}
			}
			ship = new Ship(source[0],
	        		Double.parseDouble(source[1]),
					Double.parseDouble(source[2]),
					Double.parseDouble(source[3]),
					Double.parseDouble(source[4]),
	        		"Normal"
					);
		}
        initComponents();
        
        /**
         * *****************开始通信**************************************************
         */
        client = new ClientThread(radarpanel, ship, ships);
        client.start();
    }
    
    private void initComponents() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {  //更改客户端信息
                String command = "";
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    ship.setValue(3, ship.getParameter(3) + 2);
                    command = ship.getName() + ",course," + "2";
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    ship.setValue(3, ship.getParameter(3) - 2);
                    command = ship.getName() + ",course," + "-2";
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    ship.setValue(4, ship.getParameter(4) + 1);
                    command = ship.getName() + ",speed," + "1";
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    ship.setValue(4, ship.getParameter(4) - 1);
                    command = ship.getName() + ",speed," + "-1";
                }
                if (client.socket!=null) {
                	try {
    					client.sendData(command);
    				} catch (IOException e1) {
    					e1.printStackTrace();
    				}
				}
                repaint();
                //下面这段带码对总体没有意义，去掉2017.3.5 record
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {  //判断是否连上服务端
            	if (client.socket!=null) {
            		try {
    					client.logOut();
    				} catch (IOException e1) {
    					e1.printStackTrace();
    				}
				}
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override          //windows resized， then relayout
            public void componentResized(ComponentEvent e) {
                if (!isUndecorated()) {
                    radarpanel.setBounds(0, 0, getWidth() * 7 / 9, getHeight() - 35);
                    //infopanel.setSize(getWidth()*2/9, getHeight()-35);
                    jsp.setBounds(radarpanel.getWidth(), 0, getWidth() * 2 / 9, getHeight() - 35);
                    jsp.revalidate();
                } else {
                    radarpanel.setBounds(0, 0, getWidth() * 7 / 9, getHeight());
                    //infopanel.setSize(getWidth()*2/9, getHeight());
                    jsp.setBounds(radarpanel.getWidth(), 0, getWidth() * 2 / 9, getHeight());
                    jsp.revalidate();
                }
                revalidate();  //这个得了解清楚
                repaint();  //如果不重绘,会出现刷新不及时的情况
            }
        });
        
        setTitle("RadarNavigation");
        setBackground(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(20, 20, 1008, 735);
        
        Container contentPane = this.getContentPane();
        contentPane.setBackground(null);
        //contentPane.setBorder(BorderFactory.createEmptyBorder());
        //setContentPane(contentPane);
        this.setLayout(null);
        
        radarpanel = new RadarPanel();  //显示海里应当必能直接像素，像素扩大再显示，这样里的远一些，更实际
        radarpanel.setBounds(0, 0, getWidth() * 7 / 9, getHeight() - 35);
        contentPane.add(radarpanel);
        radarpanel.getShip(ship, ships);  //导入本船和他船的引用
        
        radarpanel.addMouseListener(new MouseAdapter() {
            @Override  //全屏功能
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //e.getModifiers() == 16
                    if (e.getClickCount() == 1) {
                        Iterator<Ship> index = ships.iterator();
                        while (index.hasNext()) {
                            Ship boat = index.next();
                            int clickX = (int) (e.getX()-radarpanel.startX-radarpanel.diameter/2+ship.getParameter(1));
                            int clicky = (int) (e.getY()-radarpanel.startY-radarpanel.diameter/2+ship.getParameter(2));
                            if (Math.abs(clickX - boat.getParameter(1)) < 10
                                    && Math.abs(clicky - boat.getParameter(2)) < 10) {
                                infopanel.addShip(boat);
                                System.out.println("get a ship : "+boat.toString());
                            }
                        }
                        //infopanel.addShip(test);  //测试，过后删掉
                    }
                    //这里会与选择对象冲突，所以去掉，还有，应该使用timer类进行，百度一下，可以避免多次单机后的错误
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //删除操作
                	Iterator<Ship> index = ships.iterator();
                    while (index.hasNext()) {
                        Ship boat = index.next();
                        int clickx = (int) (e.getX()-radarpanel.startX-radarpanel.diameter/2+ship.getParameter(1));
                        int clicky = (int) (e.getY()-radarpanel.startY-radarpanel.diameter/2+ship.getParameter(2));
                        if (Math.abs(clickx - boat.getParameter(1)) < 10
                                && Math.abs(clicky - boat.getParameter(2)) < 10) {
                            infopanel.removeShip(boat);  //可以直接移除对象，以后弄清楚原理，好像也是通过索引,毕竟指向的是同一个对象
                            System.out.println("remove one: "+boat.toString());
                        }
                    }
                    //infopanel.removeShip(test);  //测试，过后删掉
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    //全屏
                    if (!isUndecorated()) {
                        setLocation(0, 0);
                        setSize(Toolkit.getDefaultToolkit().getScreenSize());
                        dispose();
                        setUndecorated(true);
                        setVisible(true);
                        radarpanel.setSize(getWidth() * 7 / 9, getHeight() - 35);
                    } else {
                        setBounds(20, 20, 1008, 735);
                        dispose();
                        setUndecorated(false);
                        setVisible(true);
                        radarpanel.setSize(getWidth() * 7 / 9, getHeight());
                    }
                    revalidate();
                    //需要repaint吗？
                }
            }
        });
        infopanel = new InfoPanel();
        jsp = new JScrollPane();
        jsp.setBounds(radarpanel.getWidth(), 0, getWidth() * 2 / 9 - 50, getHeight() - 35);
        jsp.setLocation(radarpanel.getWidth(), 0);
        jsp.setViewportView(infopanel);
        contentPane.add(jsp);
        
        this.setFocusable(true);  //如果没有这句就无法侦听到按键事件
        this.requestFocus();
    }

}
