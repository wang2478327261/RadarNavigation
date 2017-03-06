package radarnavigation;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import common.Ship;

public class RadarNavigation extends JFrame{  //客户端的主类
    
    private static final long serialVersionUID = 4076498288039253119L;
    
    private RadarPanel radarpanel;    //雷达显示面板
    private InfoPanel infopanel;     //右侧的信息面板
    private JScrollPane jsp;  //将右侧信息板添加-->滚动界面
    private Ship ship;              //船舶对象，本船的对象
    
    private List<Ship> ships = new LinkedList<Ship>();   //其他船舶信息
    ClientThread client;            //客户端通信线程
    
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
     *
     * @author ERON
     * @see RadarPanel
     * @throws IOException
     */
    public RadarNavigation() throws IOException {

        String customer = JOptionPane.showInputDialog(this, "Please input Ship name and position : ");
        while (customer == null || customer.equals("")) {
            if (customer == null) {
                this.dispose();
                System.exit(0);
            }
            JOptionPane.showMessageDialog(this, "you should input ship infoemation !", "Warning", JOptionPane.ERROR_MESSAGE);
            customer = JOptionPane.showInputDialog(this, "Please input Ship name : ");
        }
        //String[] source = customer.split(",");
        /*ship = new Ship(source[0], Double.parseDouble(source[1]),
				Double.parseDouble(source[2]), 34,      //Double.parseDouble(source[3])
				Double.parseDouble(source[4]), source[5]);
		for(int i = 0; i < source.length; i++){
			System.out.println(source[i]);
		}*/
        ship = new Ship();
        /**
         * *******************************************************************
         */
        client = new ClientThread(ship, ships);
        //client.start();
        /**
         * ******************************************************************
         */
        initComponents();
    }

    private void initComponents() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	System.out.println("keyCode");
                String command = "";
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    ship.setValue(3, ship.getParameter(3) + 2);
                    command = ship.getName() + ",course," + "starboard";
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    ship.setValue(3, ship.getParameter(3) - 2);
                    command = ship.getName() + ",course," + "port";
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    ship.setValue(4, ship.getParameter(4) + 1);
                    command = ship.getName() + ",speed," + "increase";
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    ship.setValue(4, ship.getParameter(4) - 1);
                    command = ship.getName() + ",speed," + "reduce";
                }
                /*try {
					client.sendData(command);
				} catch (IOException e1) {
					e1.printStackTrace();
				}*/
                repaint();
                System.out.println("RadarNavigation -> keyPress" + command);
                //下面这段带码对总体没有意义，去掉2017.3.5 record
                /*if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                	setBounds(20, 20, 1008, 735);
                    dispose();
                    setUndecorated(false);
                    setVisible(true);
                    radarpanel.setSize(getWidth() * 7 / 9, getHeight());
				}*/
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                /*try {
					client.logOut();
				} catch (IOException e1) {
					e1.printStackTrace();
				}*/
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override          //windows resized， then relayout
            public void componentResized(ComponentEvent e) {
                if (!isUndecorated()) {
                    radarpanel.setBounds(0, 0, getWidth() * 7 / 9, getHeight() - 35);
                    //infopanel.setSize(getWidth()*2/9, getHeight()-35);
                    jsp.setBounds(radarpanel.getWidth(), 0, getWidth() * 2 / 9, getHeight() - 35);
                } else {
                    radarpanel.setBounds(0, 0, getWidth() * 7 / 9, getHeight());
                    //infopanel.setSize(getWidth()*2/9, getHeight());
                    jsp.setBounds(radarpanel.getWidth(), 0, getWidth() * 2 / 9, getHeight());
                }
                revalidate();  //这个得了解清楚
                //repaint();
            }
        });
        
        setTitle("RadarNavigation");
        setBackground(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(20, 20, 1008, 735);
        
        Container contentPane = this.getContentPane();
        contentPane.setBackground(null);
        //contentPane.setBorder(BorderFactory.createEmptyBorder());
        setContentPane(contentPane);
        this.setLayout(null);
        
        radarpanel = new RadarPanel();
        radarpanel.setBounds(0, 0, getWidth() * 7 / 9, getHeight() - 35);
        contentPane.add(radarpanel);
        radarpanel.getShip(ship);
        
        radarpanel.addMouseListener(new MouseAdapter() {
            @Override  //全屏功能
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //e.getModifiers() == 16
                    if (e.getClickCount() == 1) {
                        Iterator<Ship> index = ships.iterator();
                        while (index.hasNext()) {
                            Ship boat = index.next();
                            if (Math.abs(e.getX() - boat.getParameter(1)) < 10
                                    && Math.abs(e.getY() - boat.getParameter(2)) < 10) {
                                infopanel.addShip(ship);
                            } else {
                                System.out.println("mei you");
                            }
                        }

                    }
                    //这里会与选择对象冲突，所以去掉，还有，应该使用timer类进行，百度一下，可以避免多次单机后的错误
                    /*if (e.getClickCount() >= 2) {
						if (!isUndecorated()) {
							setLocation(0, 0);
							setSize(Toolkit.getDefaultToolkit().getScreenSize());
							dispose();
							setUndecorated(true);
							setVisible(true);
							radarpanel.setSize(getWidth()*7/9, getHeight()-35);
						}
						else {
							setBounds(20, 20, 1008, 735);
							dispose();
							setUndecorated(false);
							setVisible(true);
							radarpanel.setSize(getWidth()*7/9, getHeight());
						}
						revalidate();
					}*/
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //删除操作
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
                }
            }
        });
        infopanel = new InfoPanel();
        //infopanel.setBounds(radarpanel.getWidth(), 0, getWidth()*2/9, getHeight()-35);
        //infopanel.setSize(getWidth()*2/9, getHeight()-35);
        jsp = new JScrollPane();
        jsp.setBounds(radarpanel.getWidth(), 0, getWidth() * 2 / 9 - 50, getHeight() - 35);
        jsp.setLocation(radarpanel.getWidth(), 0);
        jsp.setViewportView(infopanel);

        //contentPane.add(infopanel);
        contentPane.add(jsp);
        this.setFocusable(true);
    }

}
