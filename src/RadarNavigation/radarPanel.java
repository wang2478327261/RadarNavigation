package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class radarPanel extends JPanel{   //雷达面板的显示，更新信息
	
	//这些属性有默认值         这些是雷达面板的额属性值，应当放在雷达类中
	private int range = 6;  //雷达的量程， 单位是海里    雷达量程  最大24海里，最小3海里, 初始化为6海里
	private int mode = 0;  //雷达模式     雷达显示模式    北向上  船首向上    /////相对运动  绝对运动
	private boolean headLine = true;  //雷达船首线     开启或关闭船首线
	private boolean rangeLine = true;  //量程划分线dfhggj
	
	public radarPanel() {
		super();
		// TODO Auto-generated constructor stub
		setBorder(null);  //这些属性可以在雷达面板类中改变
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
	}
	
	public void setRange(String option) {
		if (option.equals("add")) {  //增加量程
			if (range == 1) {
				range = 3;
			}
			else{
				range *= 2;
				if (range > 24) {
					range = 24;
				}
			}
		}
		else{   //reduce    减少量程
			range /= 2;
			if (range < 1) {
				range = 1;
			}
		}
	}
	public int getRange() {
		return range;
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
