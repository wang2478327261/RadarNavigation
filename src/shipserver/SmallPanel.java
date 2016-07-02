package shipserver;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import common.Ship;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.HashMap;
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
    //在界面上现实的额帮助说明
    String helpStr = "";
    String nameStr = "", positionStr = "", speedStr = "", courseStr = "", typeShow = "";
	
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
				
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				mousex = e.getX();
				mousey = e.getY();
				helpStr = "'Left Click & Drag' Create Ships, 'Right Click' Delete, 'C' Button Change Type";
				repaint();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				dragx = e.getX();
				dragy = e.getY();
				//用paint进行重新绘制
				helpStr = "Drag to Create Moving Ship, 'Right Double Click' Delete All Ship";
				repaint();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					oldx = e.getX();
					oldy = e.getY();
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					delx = e.getX();
					dely = e.getY();
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
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
		            if (name == null) {   //如果返回空值，则不进行动作
						return;
					}
		            Ship ship = new Ship(name, mousex, mousey, speed, course, type);
		            serverShips.add(ship);
		            switch(ship.getType()){
		                /*case 0: typeShow = "Normal";break;
		                case 1: typeShow = "Sailing";break;
		                case 2: typeShow = "Fishing";break;
		                case 3: typeShow = "Out of Control";break;
		                case 4: typeShow = "Limit by Control";break;
		                case 5: typeShow = "Limit by Draft";break;*/
		            }
		            //在下方显示的帮助文字
		            helpStr = "A Ship Exist";
		            //右上方，显示的当前船舶信息
		            nameStr = "Ship name : "+name;
		            positionStr = "Position : "+mousex+","+mousey;
		            speedStr = "Speed : "+(int)speed;
		            courseStr = "Course : "+(int)course;
		            typeShow = "Type : "+typeShow;
		        }
				new Thread(SmallPanel.this).start();
			}
		});
		
		initComponents();
		//测试
		clientShips.add(new Ship("shejghj", 123, 345, 12, 7, "limit"));
		clientShips.add(new Ship("shejghj", 500, 234, 27, 9, "limit"));
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
	 * @param start_x  起点 x
	 * @param start_y  起点  y
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
		g2.setColor(Color.BLACK);
		for (Ship vessel : clientShips) {
			Px = vessel.getParameter(1);
			Py = vessel.getParameter(2);
			course = Math.toRadians(vessel.getParameter(3));
			speed = vessel.getParameter(4);
			
			switch (vessel.getType()) {
			// 根据船舶类型的不同进行绘制
			}
			normalShip(g2, Px, Py, course, speed);
			
		}
		for (Ship vessel : serverShips) {
			Px = vessel.getParameter(1);
			Py = vessel.getParameter(2);
			course = Math.toRadians(vessel.getParameter(3));
			speed = vessel.getParameter(4);
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
        //auto hide
        g2.drawString(mousex + " , " + mousey, 20, getHeight()-30);//mouse position 820,  680
        g2.drawString(helpStr, 170, getHeight()-30);//help position
        
        g2.drawString(nameStr, 850, 25);
        g2.drawString(positionStr, 850, 50);
        g2.drawString(speedStr, 850, 75);
        g2.drawString(courseStr, 850, 100);
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);      //调整显示时间
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		nameStr = "";
		positionStr = "";
		speedStr = "";
		courseStr = "";
		repaint();
	}
	
}
