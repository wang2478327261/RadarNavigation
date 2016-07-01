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
public class SmallPanel extends JPanel{    //����಻��Ҫ�ˣ� ����ԭ�������
	
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
				//setOpaque(true);  //���óɲ�͸��
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder());
				//setOpaque(false);   //���ó�͸��
			}
		});
		// TODO Auto-generated constructor stub
		
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);  //���ó�͸����       opaque��͸��
		setBackground(Color.WHITE);
	}
	
	/**************ͨ�ó�����*********************************************/
	public void addShip(Ship ship) {  //��ͨ���������ƻ��Ƕ����룿
		ships.add(ship);
	}
	public void removeShip(Ship ship){
		ships.remove(ship);
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
