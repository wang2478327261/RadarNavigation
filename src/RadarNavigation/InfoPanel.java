package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import javax.swing.JPanel;

public class InfoPanel extends JPanel{   //点击信息显示面板

	public InfoPanel() {
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
	
}
