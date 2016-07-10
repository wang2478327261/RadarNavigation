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
public class SmallPanel extends JPanel implements Runnable{ // ����಻��Ҫ�ˣ� ����ԭ�������
    
    private double mousex, mousey;  //�ƶ���������
    private double dragx, dragy;    //�϶�δ�ɿ�ʱ�������
    private double oldx, oldy;    //��������   ����
    private double newx, newy;    //�ɿ����   ����
    private double delx, dely;   //ɾ��ʱ��������
    private String type = "Normal";            //���������Ժ�������
    //�ڽ�������ʾ����˵��
    String helpStr = "";
    String nameStr = "", positionStr = "", courseStr = "", speedStr = "", typeStr = "";
    private boolean pressed = false;
	
	private List<Socket> sockets = new LinkedList<Socket>(); // ���ӵ��׽��ֶ���
	private List<Ship> clientShips = new LinkedList<Ship>(); // ���пͻ��˺ͷ���˲����Ĵ���ά������
	private List<Ship> serverShips = new LinkedList<Ship>(); // ��������ɵĶ���
	//�洢�����켣
	private Map<Ship, List<Point>> track = new HashMap<Ship, List<Point>>();   //һ������Ӧһ���켣��
	
	public SmallPanel() {
		super();
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				setCursor(new Cursor(Cursor.MOVE_CURSOR));  //�ƶ����
				if (e.getWheelRotation() > 0) {   //�������¹���      ��0
					//��С��ʾ�ߴ磬����ʵ�ʴ������Բ���
					helpStr = "Scroll to Zoom out";
				}
				else if(e.getWheelRotation() < 0){
					//�Ŵ�ߴ�
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
				//��¼��갴��ʱ������
				if (e.getButton() == MouseEvent.BUTTON1) {
					oldx = e.getX();
					oldy = e.getY();
					dragx = oldx;
					dragy = oldy;
					helpStr = "Drag to Create Moving Ship ";
					pressed = true;
				}
				//ʵ��ɾ������
				if (e.getButton() == MouseEvent.BUTTON3) {
					delx = e.getX();
					dely = e.getY();
					double disx, disy;
			        double dis;
			        //ֻ��ɾ�������������Ķ��󣬲��ܲ��ݿͻ���
					if(e.getClickCount() >= 2){
						if (serverShips.isEmpty()) {
							helpStr = "No ship to Clear";
						}
						else {
							serverShips.clear();
			                for (Ship vessel : serverShips) {
			                    track.get(vessel).clear();     //�������·����Ϣ
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
		                        track.remove(vessel);     //һ����Ӧ������·����Ϣ
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
		            //����������䴰��
		            String name = JOptionPane.showInputDialog("�����봬�� �� ");
		            if (name != null) {   //������ؿ�ֵ���򲻽��ж���
		            	Ship ship = new Ship(name, mousex, mousey, course, speed, type);
			            serverShips.add(ship);
			            //���Ϸ�����ʾ�ĵ�ǰ������Ϣ
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
		//����
	}
	private void initComponents() {
		// TODO Auto-generated constructor stub
		setBorder(BorderFactory.createEmptyBorder());
		//setOpaque(false); // ���ó�͸���� opaque��͸��
		setBackground(Color.WHITE);
	}
	
	/************** ͨ�ó����� *********************************************/
	/**
	 * ͨ������-->���������б��,����--������Ϊ0�� ˳ʱ����ת    ��  360��
	 * @param start_x  ��һ�ΰ���    ��� x
	 * @param start_y  ��һ�ΰ���    ���  y
	 * @param end_x  �յ�  x
	 * @param end_y  �յ�  y
	 * @return  ����֮���б��
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
	
	/********************* ͼ�λ����� **********************************/
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
		if (pressed) {               //��̬��ʾ��ǰ�Ĳ���
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
		for (Ship vessel : clientShips) {     //���ƿͻ��˲����Ĵ�������
			Px = vessel.getParameter(1);
			Py = vessel.getParameter(2);
			course = Math.toRadians(vessel.getParameter(3));
			speed = vessel.getParameter(4);
			
			switch (vessel.getType()) {
			// ���ݴ������͵Ĳ�ͬ���л���
			}
			normalShip(g2, Px, Py, course, speed);
			
		}
		g2.setColor(Color.MAGENTA);
		for (Ship vessel : serverShips) {         //���Ʒ�������ɵĶ���
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
