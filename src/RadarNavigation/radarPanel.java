package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class radarPanel extends JPanel{   //雷达面板的显示，更新信息
	
	private int range;  //雷达的量程， 单位是海里
	private int mode;  //雷达模式
	private boolean headLine;  //雷达船首线
	
	public radarPanel(int range, int mode, boolean headLine) {
		super();
		// TODO Auto-generated constructor stub
		this.range = range;
		this.mode = mode;
		this.headLine = headLine;
		
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
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.GREEN);
		//获得圆心和量程（加入变量使得 可以更改）
		/*range = getWidth()>getHeight()?getHeight():getWidth();
		for(int i = 1; i < range; i++){  //这里需要改进，有许多问题
			int r = i * 1;
			g2.drawOval(getWidth()-range, getHeight()-range, range, range);
		}*/
		
	}
	
	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		super.update(g);
	}
	
}
