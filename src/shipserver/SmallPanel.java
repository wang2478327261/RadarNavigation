package shipserver;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import common.Ship;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

@SuppressWarnings("serial")
public class SmallPanel extends JPanel implements Runnable{ // 这个类不需要了， 采用原来的设计
    
    private double mousex, mousey;  //移动鼠标的坐标
    private double dragx, dragy;    //拖动未松开时鼠标坐标
    private double oldx, oldy;    //按下鼠标后   坐标
    private double newx, newy;    //松开鼠标   坐标
    private double delx, dely;   //删除时鼠标的坐标
    private String type = "Normal";            //船舶类型以后可以添加
    //在界面上显示帮助说明
    String helpStr = "";
    String nameStr = "", positionStr = "", courseStr = "", speedStr = "", typeStr = "";
    private boolean pressed = false;
	
	private List<Socket> sockets = new LinkedList<Socket>(); // 连接的套接字对象
	private List<Ship> clientShips = new LinkedList<Ship>(); // 所有客户端和服务端产生的船舶维护对象
	private List<Ship> serverShips = new LinkedList<Ship>(); // 服务端生成的对象
	//存储船舶轨迹
	private Map<Ship, List<Point>> track = new HashMap<Ship, List<Point>>();   //一条船对应一条轨迹链
	
	public SmallPanel() {
		super();
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				setCursor(new Cursor(Cursor.MOVE_CURSOR));  //移动光标
				if (e.getWheelRotation() > 0) {   //滚轮向下滚动      》0
					//缩小显示尺寸，但是实际船舶属性不变
					helpStr = "Scroll to Zoom out";
				}
				else if(e.getWheelRotation() < 0){
					//放大尺寸
					helpStr = "Scroll to Zoom in";
				}
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				mousex = e.getX();
				mousey = e.getY();
				helpStr = "'Left Click & Drag' Create Ships, 'Right Click' Delete, 'Right Double Click' Delete All Ship";
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
				//记录鼠标按下时的坐标
				if (e.getButton() == MouseEvent.BUTTON1) {
					oldx = e.getX();
					oldy = e.getY();
					dragx = oldx;
					dragy = oldy;
					helpStr = "Drag to Create Moving Ship ";
					pressed = true;
				}
				//实现删除功能
				if (e.getButton() == MouseEvent.BUTTON3) {
					delx = e.getX();
					dely = e.getY();
					double disx, disy;
			        double dis;
			        //只能删除服务器产生的对象，不能操纵客户端
					if(e.getClickCount() >= 2){
						if (serverShips.isEmpty()) {
							helpStr = "No ship to Clear";
						}
						else {
							serverShips.clear();
			                for (Ship vessel : serverShips) {
			                    track.get(vessel).clear();     //清除本船路径信息
			                }
			                helpStr = "Delete All Ships";
						}
		            }
		            else{
		                Iterator<Ship> shIt = serverShips.iterator();
		                while(shIt.hasNext()){
		                    Ship vessel = shIt.next();
		                    disx = Math.abs(delx-vessel.getParameter(1));
		                    disy = Math.abs(dely-vessel.getParameter(2));
		                    dis = Math.sqrt(disx*disx + disy*disy);
		                    if(dis <= 20){
		                        shIt.remove();
		                        track.remove(vessel);     //一处对应船舶的路径信息
		                        helpStr = "Deleted a Ship <Done>";
		                    }
		                }
		            }
				}
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1){
		            newx = e.getX();
		            newy = e.getY();
		            double course = CaculateRatio(mousex, mousey, newx, newy);
		            double differentx = newx - mousex;
		            double differenty = newy - mousey;
		            double speed = Math.sqrt(Math.pow(differentx, 2) + Math.pow(differenty, 2))/10;
		            //弹出参数填充窗口
		            String name = JOptionPane.showInputDialog("请输入船名 ： ");
		            if (name != null) {   //如果返回空值，则不进行动作
		            	Ship ship = new Ship(name, mousex, mousey, course, speed, type);
			            serverShips.add(ship);
			            //右上方，显示的当前船舶信息
			            nameStr = "Ship name : "+name;
			            positionStr = "Position : "+mousex+","+mousey;
			            courseStr = "Course : "+(int)course;
			            speedStr = "Speed : "+(int)speed;
			            typeStr = "Type : "+type;
					}
		        }
				new Thread(SmallPanel.this).start();
				pressed = false;
				//repaint();
			}
		});
		
		initComponents();
		//测试
	}
	private void initComponents() {
		// TODO Auto-generated constructor stub
		setBorder(BorderFactory.createEmptyBorder());
		//setOpaque(false); // 设置成透明的 opaque不透明
		setBackground(Color.WHITE);
	}
	
	/************** 通用程序区 *********************************************/
	/**
	 * 通过两点-->计算出两点斜率,方向--》向上为0， 顺时针旋转    到  360°
	 * @param start_x  第一次按下    起点 x
	 * @param start_y  第一次按下    起点  y
	 * @param end_x  终点  x
	 * @param end_y  终点  y
	 * @return  两点之间的斜率
	 */
	private double CaculateRatio(double start_x, double start_y, double end_x, double end_y){
        double differentx = end_x - start_x;
        double differenty = end_y - start_y;
        double course = 0;
        int adjust = 0;//switch case///
        
        if(differentx == 0 && differenty == 0) adjust = 0;
        else if(differentx >= 0 && differenty <0) adjust = 1;
        else if(differentx < 0 && differenty <= 0) adjust = 2;
        else if(differentx <= 0 && differenty > 0) adjust = 3;
        else if(differentx > 0 && differenty >= 0) adjust = 4;
        
        switch(adjust){
            case 0 : course = 0; break;
            case 1 : course = 450 - Math.toDegrees(Math.atan2(-differenty, differentx)); break;
            case 2 : course = 90 - Math.toDegrees(Math.atan2(-differenty, differentx)); break;
            case 3 : course = 90 - Math.toDegrees(Math.atan2(-differenty, differentx)); break;
            case 4 : course = 90 - Math.toDegrees(Math.atan2(-differenty, differentx)); break;
        }
        
        while (course<0||course>=360){
            if(course<0) course+=360;
            if(course>=360) course-=360;
        }
        return course;
    }
	
	/********************* 图形绘制区 **********************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Default", Font.PLAIN, (int) (Math.min(getWidth(), getHeight())*0.03)));
		g2.setColor(Color.BLUE);
		
		paintShips(g2);
		printString(g2);
	}
	
	public void paintShips(Graphics2D g2) {
		
		double Px, Py, course, speed;
		g2.setColor(Color.RED);
		if (pressed) {               //动态显示当前的参数
			Px = oldx;
			Py = oldy;
			course = CaculateRatio(oldx, oldy, dragx, dragy);
			double differentx = dragx - oldx;
            double differenty = dragy - oldy;
            speed = Math.sqrt(differentx*differentx + differenty*differenty);
            g2.drawLine((int)oldx, (int)oldy, (int)dragx, (int)dragy);
            g2.drawString("Course : " + (int)course, (int)dragx + 30, (int)dragy);
            g2.drawString("Speed : "+(int)speed/10, (int)dragx + 30, (int)dragy+30);
		}
		g2.setColor(Color.BLUE);
		for (Ship vessel : clientShips) {     //绘制客户端产生的船舶对象
			Px = vessel.getParameter(1);
			Py = vessel.getParameter(2);
			course = Math.toRadians(vessel.getParameter(3));
			speed = vessel.getParameter(4);
			
			switch (vessel.getType()) {
			// 根据船舶类型的不同进行绘制
			}
			normalShip(g2, Px, Py, course, speed);
			
		}
		g2.setColor(Color.MAGENTA);
		for (Ship vessel : serverShips) {         //绘制服务端生成的对象
			Px = vessel.getParameter(1);
			Py = vessel.getParameter(2);
			course = Math.toRadians(vessel.getParameter(3));
			speed = vessel.getParameter(4);
			normalShip(g2, Px, Py, course, speed);
		}
		
	}
	
	public void normalShip(Graphics2D g2, double Px, double Py, double course, double speed) {
		int linestartx, linestarty, lineendx, lineendy;
		linestartx = (int) (Px + 20 * Math.sin(course));
		linestarty = (int) (Py - 20 * Math.cos(course));
		lineendx = (int) (linestartx + speed * Math.sin(course));
		lineendy = (int) (linestarty - speed * Math.cos(course));
		
		int[] trianglex = { linestartx, (int) (Px + 7 * Math.sin(course + Math.PI / 2)),
				(int) (Px - 10 * Math.sin(course) + 7 * Math.sin(course + Math.PI / 2)),
				(int) (Px - 10 * Math.sin(course) + 7 * Math.sin(course + 3 * Math.PI / 2)),
				(int) (Px + 7 * Math.sin(course + 3 * Math.PI / 2)) };
		int[] triangley = { linestarty, (int) (Py - 7 * Math.cos(course + Math.PI / 2)),
				(int) (Py + 10 * Math.cos(course) - 7 * Math.cos(course + Math.PI / 2)),
				(int) (Py + 10 * Math.cos(course) - 7 * Math.cos(course + 3 * Math.PI / 2)),
				(int) (Py - 7 * Math.cos(course + 3 * Math.PI / 2)) };
		// drawbody and courseline
		g2.drawPolygon(trianglex, triangley, 5);
		g2.drawLine(linestartx, linestarty, lineendx, lineendy);
	}
	
	public void printString(Graphics g2){
        g2.setColor(Color.BLACK);
        int h = g2.getFont().getSize()+10;
        g2.drawString(mousex + " , " + mousey, h, h);//mouse position 820,  680
        g2.drawString(helpStr, h, getHeight()-30);//help position
        
        g2.drawString(nameStr, getWidth() - 300, h);
        g2.drawString(positionStr, getWidth() - 300, 2*h);
        g2.drawString(courseStr, getWidth() - 300, 3*h);
        g2.drawString(speedStr, getWidth() - 300, 4*h);
        //Type
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		helpStr = "";
		
		nameStr = "";
		positionStr = "";
		speedStr = "";
		courseStr = "";
		repaint();
	}
	
}
