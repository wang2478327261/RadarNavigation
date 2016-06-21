package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoPanel extends JPanel{   //�����Ϣ��ʾ���

	//��Ҫ��ʾ��Ϣ�洢������
	List<Ship> ships = new LinkedList<Ship>();   //�洢��ʾ��Ϣ�Ĵ�������
	
	public InfoPanel() {
		super();
		initComponents();
	}
	private void initComponents() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				//��������ʾ�˵����任����  ������������
				revalidate();
				if (e.getButton() == MouseEvent.BUTTON1) {
					//����˵�����
				}
				else if (e.getButton() == MouseEvent.BUTTON3) {
					//�˳��˵�����
				}
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//������ҳ
			}
		});
		// TODO Auto-generated constructor stub
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setLayout(null);
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setFont(new Font(getName(), Font.PLAIN, (int) (Math.min(getWidth(), getHeight())*0.1)));
		g2.setColor(Color.CYAN);
		g2.drawString("hello", 0, getFont().getSize()+10);
	}
	
	public void addShip(Ship ship) {   //Ҫ��ʾ�Ĵ���
		ships.add(ship);
	}
	
	public void removeShip(Ship ship) {  //Ҳ������������
		ships.remove(ship);
	}
}
