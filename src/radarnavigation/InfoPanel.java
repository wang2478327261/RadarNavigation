package radarnavigation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import common.Ship;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel{
	
	List<Ship> ships = new LinkedList<Ship>();
	private int scroll_Y = 0;
	
	public InfoPanel() {
		super();
		System.out.println("InfoPanel -> infopanel");
		initComponents();
	}
	
	private void initComponents() {
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//������ҳ
				if (e.getWheelRotation() > 0) {
					scroll_Y -= 40;
				}
				else if(e.getWheelRotation() < 0){
					scroll_Y += 40;
				}
				repaint();
			}
		});
		setBackground(Color.DARK_GRAY);
		//setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setLayout(null);
		ships.add(new Ship("huawei",123,23,34,13,"normal"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
	}
	
	/*************************************************************/
	@Override
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
	}
	
	/****************************************************/
	public void addShip(Ship ship) {
		ships.add(ship);
		System.out.println("InfoPanel -> addShip");
		repaint();
	}
	public void removeShip(Ship ship) {
		ships.remove(ship);
		System.out.println("InfoPanel -> removeShip");
		repaint();
	}
	
	public void changeMode(){
		
	}
}
