package shipserver;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import common.Ship;

public class SmallPanel extends JPanel implements Runnable { // 船舶绘制有点问题，下一个版本制作的时候应当注意
	
	private static final long serialVersionUID = 5493000947340277541L;
	
	private double mousex, mousey; // 移动时的鼠标坐标
	private double dragx, dragy; // 拖动时鼠标坐标
	private double pressx, pressy; // 创建时按压下的位置
	private double releasex, releasey; // 松开鼠标时的位置
	private double delx, dely; // 要删除的对象位置，鼠标点击的位置
	private String type = "Normal"; // 这个暂时默认，不进行更改
	/**********帮助提示符，以后可以单独一个组件，局部更新***************************************/
	String helpStr = "";
	String nameStr = "", positionStr = "", courseStr = "", speedStr = "", typeStr = "";
	private boolean pressed = false;
	/***********************通信相关变量**********************************/
	ServerThread server;  //通信线程
	private List<Ship> clientShips = new LinkedList<Ship>();  //客户端消息创建的对象
	private List<Ship> serverShips = new LinkedList<Ship>();  //服务端本地创建，用于测试
	// private Map<String, Socket> sockets = new HashMap<String, Socket>();
	private List<Socket> sockets = new LinkedList<Socket>();
	
	//private Map<String, List<Point>> track = new HashMap<String, List<Point>>(); //2017.3.7 去掉这种做法，参考ChatRoom
	/*****************************************/
	/*******下边记录缩放比例，绘制缩放后的船舶影像******/
	/*****************************************/
	private float level = 1F;  //缩放级别，1:1
	
	public SmallPanel() {
		super();
		initComponents();
	}
	private void initComponents() {
		addMouseWheelListener(new MouseWheelListener() { //缩放过程应当记录缩放比例，便于还原
			public void mouseWheelMoved(MouseWheelEvent e) {
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
				if (e.getWheelRotation() > 0) {
					helpStr = "Scroll to Zoom out";
					level /=2;
				} else if (e.getWheelRotation() < 0) {
					helpStr = "Scroll to Zoom in";
					level *=2;
				}
				System.out.println(level);
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				mousex = e.getX();
				mousey = e.getY();
				helpStr = "'Left Click & Drag' Create Ships, 'Right Click' Delete, 'Right Double Click' Delete All Ships";
				repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				dragx = e.getX();
				dragy = e.getY();
				repaint();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					pressx = e.getX();
					pressy = e.getY();
					dragx = pressx;  //为什么以前要这么做？
					dragy = pressy;  //试验后：因为刚开始drag=0，以后会记录上一次drag位置，这样可以更新点击坐标
					helpStr = "Drag to Create Moving Ship ";
					pressed = true; // 需要按下标志
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					delx = e.getX();
					dely = e.getY();
					double disx, disy;
					double dis;
					if (e.getClickCount() >= 2) {
						if (serverShips.isEmpty()) {
							helpStr = "No ship to Clear --> Left Drag to Create";
						} else {
							serverShips.clear();
							for (Ship vessel : serverShips) {
								//track.get(vessel).clear();
							}
							helpStr = "Clear All Ships --> No server ships";
						}
					} else {
						Iterator<Ship> shIt = serverShips.iterator();
						while (shIt.hasNext()) {
							Ship vessel = shIt.next();
							disx = Math.abs(delx - vessel.getParameter(1));
							disy = Math.abs(dely - vessel.getParameter(2));
							dis = Math.sqrt(disx * disx + disy * disy);
							if (dis <= 20) {
								shIt.remove();
								//track.remove(vessel);
								helpStr = "Deleted a Ship --> Done";
							}
						}
					}
				}
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					releasex = e.getX();
					releasey = e.getY();
					double course = CaculateRatio(pressx, pressy, releasex, releasey); //计算方向
					double differentx = releasex-pressx;  //newx - mousex;
					double differenty = releasey-pressy;  //newy - mousey;
					double speed = Math.sqrt(differentx*differentx + differenty*differenty)/10;
					
					String name = JOptionPane.showInputDialog("ship name");
					if (name != null && !name.equals("")) {
						Ship ship = new Ship(name, mousex, mousey, course, speed, type);
						serverShips.add(ship);
						
						nameStr = "Ship name : " + name;
						positionStr = "Position : " + mousex + "," + mousey;
						courseStr = "Course : " + (int) course;
						speedStr = "Speed : " + (int) speed;
						typeStr = "Type : " + type;

						new Thread(SmallPanel.this).start();

						for (Socket sk : sockets) {
							String command = name + "logIn" + mousex + mousey + course + speed + type;
							try {
								ServerThread.sendData(sk, command);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				pressed = false;
				repaint();
			}
		});

		setBorder(BorderFactory.createEmptyBorder());
		setLayout(null);
		// setOpaque(false);
		setBackground(Color.WHITE);
		
		server = new ServerThread(clientShips, serverShips, sockets, this);
		server.start();
	}

	/**
	 * ***************根据起始点计算,这个我算了好久，最后才把所有的情况分类成功*****************************
	 */
	private double CaculateRatio(double start_x, double start_y, double end_x, double end_y) {
		// 这里返回角度是度，不是弧度
		double differentx = end_x - start_x;
		double differenty = end_y - start_y;
		double course = 0;
		int adjust = 0;// switch case///

		if (differentx == 0 && differenty == 0) {
			adjust = 0;
		} else if (differentx >= 0 && differenty < 0) {
			adjust = 1;
		} else if (differentx < 0 && differenty <= 0) {
			adjust = 2;
		} else if (differentx <= 0 && differenty > 0) {
			adjust = 3;
		} else if (differentx > 0 && differenty >= 0) {
			adjust = 4;
		}

		switch (adjust) {
		case 0:
			course = 0;
			break;
		case 1:
			course = 450 - Math.toDegrees(Math.atan2(-differenty, differentx));
			break;
		case 2:
			course = 90 - Math.toDegrees(Math.atan2(-differenty, differentx));
			break;
		case 3:
			course = 90 - Math.toDegrees(Math.atan2(-differenty, differentx));
			break;
		case 4:
			course = 90 - Math.toDegrees(Math.atan2(-differenty, differentx));
			break;
		}

		while (course < 0 || course >= 360) {
			if (course < 0) {
				course += 360;
			}
			if (course >= 360) {
				course -= 360;
			}
		}
		return course;
	}

	/**
	 * ***************绘制界面上的船舶对象*******************************
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Default", Font.PLAIN, (int) (Math.min(getWidth(), getHeight()) * 0.03)));
		g2.setColor(Color.BLUE);

		paintShips(g2); // 自己写的代码竟然看不懂了，船舶绘制的设计没有记录局部坐标系的信息
		printString(g2);
	}

	public void paintShips(Graphics2D g2) {
		double Px, Py, course, speed;
		g2.setColor(Color.RED);
		if (pressed) { // 在创建新的船舶对象时能够显示创建过程
			course = CaculateRatio(pressx, pressy, dragx, dragy);
			double diffx = dragx - pressx;
			double diffy = dragy - pressy;
			speed = Math.sqrt(diffx * diffx + diffy * diffy); // 速度与拖动距离成正比
			g2.drawString("Course : " + (int) course, (int) dragx + 30, (int) dragy);
			g2.drawString("Speed : " + (int) speed / 10, (int) dragx + 30, (int) dragy + 30);
			
			normalShip(g2, pressx, pressy, course, speed);
			// creatingShip(g2, oldx, oldy, course, speed);
		}
		g2.setColor(Color.BLUE);
		for (Ship vessel : clientShips) { // 客户端船舶
			Px = vessel.getParameter(1);
			Py = vessel.getParameter(2);
			// course = Math.toRadians(vessel.getParameter(3));
			course = vessel.getParameter(3);
			speed = vessel.getParameter(4);

			switch (vessel.getType()) {
			}
			normalShip(g2, Px, Py, course, speed);
		}
		g2.setColor(Color.MAGENTA);
		for (Ship vessel : serverShips) { // 服务端创建的船舶
			Px = vessel.getParameter(1);
			Py = vessel.getParameter(2);
			//course = Math.toRadians(vessel.getParameter(3));
			course = vessel.getParameter(3);
			speed = vessel.getParameter(4);
			normalShip(g2, Px, Py, course, speed);
		}
	}
	
	public void normalShip(Graphics2D g2, double Px, double Py, double course, double speed) { // 可以整体旋转
		course = Math.toRadians(course);  //角度转换成弧度
		
		//AffineTransform af = g2.getTransform(); // 以后用这种方法更好,如果使用这个，那么后边画出来的就应该是朝向向上的船舶
		//g2.rotate(course, Px, Py);
		//计算画图时应该依据动态变化的点来
		int linestartx, linestarty, lineendx, lineendy;
		linestartx = (int) (Px + 20 * Math.sin(course));
		linestarty = (int) (Py - 20 * Math.cos(course));
		lineendx = (int) (linestartx + speed * Math.sin(course));
		lineendy = (int) (linestarty - speed * Math.cos(course));
		
		int[] trianglex = { linestartx,
				(int) (Px + 7 * Math.sin(course + Math.PI / 2)),
				(int) (Px - 10 * Math.sin(course) + 7 * Math.sin(course + Math.PI / 2)),
				(int) (Px - 10 * Math.sin(course) + 7 * Math.sin(course + 3 * Math.PI / 2)),
				(int) (Px + 7 * Math.sin(course + 3 * Math.PI / 2))
		};
		int[] triangley = { linestarty,
				(int) (Py - 7 * Math.cos(course + Math.PI / 2)),
				(int) (Py + 10 * Math.cos(course) - 7 * Math.cos(course + Math.PI / 2)),
				(int) (Py + 10 * Math.cos(course) - 7 * Math.cos(course + 3 * Math.PI / 2)),
				(int) (Py - 7 * Math.cos(course + 3 * Math.PI / 2))
		};
		// drawbody and courseline
		g2.drawPolygon(trianglex, triangley, 5);
		g2.drawLine(linestartx, linestarty, lineendx, lineendy);
		
		//g2.setTransform(af);
	}
	// 试试用旋转创建-->试过了，是个好方法，但是还需要修改
	public void creatingShip(Graphics2D g2, double Px, double Py, double course, double speed) { // 拖拽时创建船舶对象，不用绘制船舶首向
		AffineTransform af = g2.getTransform(); // 以后用这种方法更好
		double radiusCourse = Math.toRadians(course);
		g2.rotate(Math.toRadians(radiusCourse), Px, Py);

		int linestartx, linestarty;//, lineendx, lineendy;
		linestartx = (int) (Px + 20 * Math.sin(radiusCourse));
		linestarty = (int) (Py - 20 * Math.cos(radiusCourse));
		/*
		 * lineendx = (int) (linestartx + speed * Math.sin(course)); lineendy =
		 * (int) (linestarty - speed * Math.cos(course));
		 */
		int[] trianglex = { linestartx, (int) (Px + 7 * Math.sin(radiusCourse + Math.PI / 2)),
				(int) (Px - 10 * Math.sin(radiusCourse) + 7 * Math.sin(radiusCourse + Math.PI / 2)),
				(int) (Px - 10 * Math.sin(radiusCourse) + 7 * Math.sin(radiusCourse + 3 * Math.PI / 2)),
				(int) (Px + 7 * Math.sin(radiusCourse + 3 * Math.PI / 2)) };
		int[] triangley = { linestarty, (int) (Py - 7 * Math.cos(radiusCourse + Math.PI / 2)),
				(int) (Py + 10 * Math.cos(radiusCourse) - 7 * Math.cos(radiusCourse + Math.PI / 2)),
				(int) (Py + 10 * Math.cos(radiusCourse) - 7 * Math.cos(radiusCourse + 3 * Math.PI / 2)),
				(int) (Py - 7 * Math.cos(radiusCourse + 3 * Math.PI / 2)) };
		// drawbody and courseline
		g2.drawPolygon(trianglex, triangley, 5);
		// g2.drawLine(linestartx, linestarty, lineendx, lineendy);

		g2.setTransform(af);
	}

	public void printString(Graphics g2) {
		g2.setColor(Color.BLACK);
		int h = g2.getFont().getSize() + 10;
		g2.drawString(mousex + " , " + mousey, h, h);// mouse position 820, 680
		g2.drawString(helpStr, h, getHeight() - 30);// help position

		g2.drawString(nameStr, getWidth() - 300, h);
		g2.drawString(positionStr, getWidth() - 300, 2 * h);
		g2.drawString(courseStr, getWidth() - 300, 3 * h);
		g2.drawString(speedStr, getWidth() - 300, 4 * h);
		// Type
	}

	@Override
	public void run() { // 释放鼠标开始计时，5秒后更新数据
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		helpStr = "";

		nameStr = "";
		positionStr = "";
		speedStr = "";
		courseStr = "";
		repaint();
	}

}
