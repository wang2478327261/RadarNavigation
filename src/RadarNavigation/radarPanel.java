package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class radarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	private int radarDiameter = 600;  //��λ�Ǻ���
	
	public radarPanel() {
		super();
		// TODO Auto-generated constructor stub
		setBorder(null);  //��Щ���Կ������״�������иı�
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.GREEN);
		//���Բ�ĺ����̣��������ʹ�� ���Ը��ģ�
		radarDiameter = getWidth()>getHeight()?getHeight():getWidth();
		for(int i = 1; i < radarDiameter; i++){  //������Ҫ�Ľ������������
			int r = i * 1;
			g2.drawOval(getWidth()-radarDiameter, getHeight()-radarDiameter, radarDiameter, radarDiameter);
		}
	}
	
	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		super.update(g);
	}
	
	
}
