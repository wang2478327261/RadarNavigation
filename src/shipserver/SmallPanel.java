package shipserver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import common.Ship;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class SmallPanel extends JPanel{    //这个类不需要了， 采用原来的设计
	
	List<Ship> ships = new LinkedList<Ship>();
	
	public SmallPanel() {
		super();
		initComponents();
	}
	private void initComponents() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
				//setOpaque(true);  //设置成不透明
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder());
				//setOpaque(false);   //设置成透明
			}
		});
		// TODO Auto-generated constructor stub
		
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);  //设置成透明的       opaque不透明
		setBackground(Color.WHITE);
	}
	
	/**************通用程序区*********************************************/
	public void addShip(Ship ship) {  //是通过船舶名称还是对象传入？
		ships.add(ship);
	}
	public void removeShip(Ship ship){
		ships.remove(ship);
	}
	
	/*********************图形画画区**********************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLUE);
		g2.drawString("hello worls", 0, 20);
	}
	
}
