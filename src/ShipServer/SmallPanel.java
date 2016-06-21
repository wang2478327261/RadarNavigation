package ShipServer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import RadarNavigation.Ship;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class SmallPanel extends JPanel{
	
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
				setOpaque(true);  //���óɲ�͸��
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder());
				setOpaque(false);   //���ó�͸��
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				setBounds(0, 0, shipmanager.getWidth()-8, shipmanager.getHeight()-35);
				revalidate();
			}
		});
		// TODO Auto-generated constructor stub
		
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);  //���ó�͸����       opaque��͸��
		setBackground(Color.WHITE);
	}
	
	/**************ͨ�ó�����*********************************************/
	public void getShips(List<Ship> ships) {
		this.ships = ships;
	}

	ShipManager shipmanager;
	public void refer(ShipManager shipmanager) {
		this.shipmanager = shipmanager;
	}
	
	/*********************ͼ�λ�����**********************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLUE);
		g2.drawString("hello worls", 0, 20);
	}
	
}
