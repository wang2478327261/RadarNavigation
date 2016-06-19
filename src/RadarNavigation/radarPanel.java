package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class radarPanel extends JPanel{   //雷达面板的显示，更新信息
	
	private int radarDiameter = 600;  //单位是海里
	
	public radarPanel() {
		super();
		// TODO Auto-generated constructor stub
		setBorder(null);  //这些属性可以在雷达面板类中改变
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
		//获得圆心和量程（加入变量使得 可以更改）
		radarDiameter = getWidth()>getHeight()?getHeight():getWidth();
		for(int i = 1; i < radarDiameter; i++){  //这里需要改进，有许多问题
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
