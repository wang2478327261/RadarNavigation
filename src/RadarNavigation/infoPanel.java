package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import javax.swing.JPanel;

public class infoPanel extends JPanel{   //�����Ϣ��ʾ���

	public infoPanel() {
		super();
		// TODO Auto-generated constructor stub
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setLayout(null);
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		
	}

	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		super.update(g);
	}
	
	
}
