package shipserver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import common.Ship;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class SmallPanel extends JPanel { // ����಻��Ҫ�ˣ� ����ԭ�������
	
	private List<Socket> sockets = new LinkedList<Socket>(); // ���ӵ��׽��ֶ���
	private List<Ship> clientShips = new LinkedList<Ship>(); // ���пͻ��˺ͷ���˲����Ĵ���ά������
	private List<Ship> serverShips = new LinkedList<Ship>(); // ��������ɵĶ���
	//�洢�����켣
	private Map<Ship, List<Point>> track = new HashMap<Ship, List<Point>>();   //һ������Ӧһ���켣��
	
	public SmallPanel() {
		super();
		initComponents();
		clientShips.add(new Ship("shejghj", 123, 345, 23, 15, "limit"));
		clientShips.add(new Ship("shejghj", 500, 234, 12, 15, "limit"));
	}
	private void initComponents() {
		// TODO Auto-generated constructor stub
		setBorder(BorderFactory.createEmptyBorder());
		//setOpaque(false); // ���ó�͸���� opaque��͸��
		setBackground(Color.WHITE);
	}

	/************** ͨ�ó����� *********************************************/
	
	
	/********************* ͼ�λ����� **********************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		
		paintShips(g2);
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
			// ���ݴ������͵Ĳ�ͬ���л���
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
        g2.drawString(mousex + " , " + mousey, 20, 680);//mouse position 820,  680
        g2.drawString(helpStr, 170, 680);//help position
        
        g2.drawString(positionStr, 850, 25);//infomation showing
        g2.drawString(speedStr, 850, 50);
        g2.drawString(courseStr, 850, 75);
    }
	
}
