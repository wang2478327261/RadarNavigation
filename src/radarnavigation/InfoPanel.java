package radarnavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import common.Ship;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoPanel extends JPanel{   //�����Ϣ��ʾ���
	
	//��Ҫ��ʾ��Ϣ�洢������
	List<Ship> ships = new LinkedList<Ship>();   //�洢��ʾ��Ϣ�Ĵ�������
	private int scroll_Y = 0;     //���ֹ���     �߶ȵı仯ֵ
	//private int scrollStart;
	
	public InfoPanel() {
		super();
		
		initComponents();
	}
	
	private void initComponents() {
		/*addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		});
		*/
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {    //�Ժ���ԸĽ���������
				//������ҳ
				if (e.getWheelRotation() > 0) {   //���������¹���       ����    ������
					scroll_Y -= 40;
				}
				else if(e.getWheelRotation() < 0){
					scroll_Y += 40;
				}
				repaint();
			}
		});
		// TODO Auto-generated constructor stub
		setBackground(Color.DARK_GRAY);
		//setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setLayout(null);    //��������
		ships.add(new Ship("huawei",123,23,34,13,"normal"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
	}
	
	/*********��ͼ����********************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setFont(new Font("Default", Font.PLAIN, (int) (Math.min(getWidth(), getHeight())*0.07)));
		g2.setColor(Color.CYAN);
		//******************************************************************************
		//��ʾ���ӵĴ�����Ϣ
		int h = g2.getFont().getSize();   //����߶�
		int locate = 1;     //�����ַ�������
		//scrollStart = (int) (locate*h*1.3) + scroll_Y;
		for (Ship vessel : ships) {
			g2.drawString(vessel.getName()+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(1)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(2)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(3)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(3)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getType(), 2, (int) (locate*h*1.3)+scroll_Y);      locate++;
			g2.drawLine(2, (int) (locate*h*1.3)+scroll_Y, getWidth()-2, (int) (locate*h*1.3)+scroll_Y);
			locate++;
		}
	}
	
	/********��ͨ��������**************************************************/
	//�򴬲���ȡ�б��ϸ��¶���
	public void addShip(Ship ship) {   //Ҫ��ʾ�Ĵ���
		ships.add(ship);
		//���½���
		repaint();
	}
	public void removeShip(Ship ship) {  //Ҳ������������
		ships.remove(ship);
		//��Ҫ���½���
		repaint();
	}
}
