package radarnavigation;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import common.InfoShow;
import common.Ship;

public class InfoPanel extends JPanel{
	
	private static final long serialVersionUID = -1344586063850816104L;
	
	public static List<Ship> ships = new LinkedList<Ship>();
	private int scroll_Y = 0;
	
	public InfoPanel() {
		super();
		System.out.println("InfoPanel -> infopanel");
		
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		
		initComponents();
	}
	
	private void initComponents() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() > 0) {
					scroll_Y -= 40;
				}
				else if(e.getWheelRotation() < 0){
					scroll_Y += 40;
				}
				repaint();
			}
		});
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  //这个有点问题，但是可以运行
		setBorder(BorderFactory.createLineBorder(Color.RED));  //测试
		setBackground(Color.DARK_GRAY);
		//setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		//setLayout(null);
		/*ships.add(new Ship("huawei",123,23,34,13,"normal"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));*/
		for(int i=0;i<ships.size();i++){
			this.add(new InfoShow(ships.get(i)));
		}
	}
	
	/******************绘制面板的重写方法*******************************************/
	/*@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setFont(new Font("Default", Font.PLAIN, (int) (Math.min(getWidth(), getHeight())*0.07)));
		g2.setColor(Color.CYAN);
		//******************************************************************************
		int h = g2.getFont().getSize();
		int locate = 1;
		//scrollStart = (int) (locate*h*1.3) + scroll_Y;
		for (Ship vessel : ships) {
			g2.drawString(vessel.getName()+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(1)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(2)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(3)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(3)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getType(), 2, (int) (locate*h*1.3)+scroll_Y);      locate++;
			g2.setColor(Color.ORANGE);
			g2.drawLine(2, (int) (locate*h*1.3)+scroll_Y, getWidth()-2, (int) (locate*h*1.3)+scroll_Y);
			locate++;
			g2.setColor(Color.CYAN);
		}
	}*/
	
	/******************控制信息传递的方法群**********************************/
	public static void addShip(Ship ship) {
		ships.add(ship);
		System.out.println("InfoPanel -> addShip");
		//repaint();
	}
	public static void removeShip(Ship ship) {
		ships.remove(ship);
		System.out.println("InfoPanel -> removeShip");
		//repaint();
	}
	
	public void changeMode(){
		
	}
}
