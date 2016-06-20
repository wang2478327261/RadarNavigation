package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class RadarPanel extends JPanel{   //雷达面板的显示，更新信息
	
	//这些属性有默认值         这些是雷达面板的额属性值，应当放在雷达类中
	private int range = 6;  //雷达的量程， 单位是海里    雷达量程  最大12海里，最小1海里, 初始化为6海里
	private int mode = 0;  //雷达模式     雷达显示模式    北向上  船首向上    /////相对运动  绝对运动
	private boolean headLine = true;  //雷达船首线     开启或关闭船首线
	private boolean rangeLine = true;  //量程划分线  分多段，随量程变化
	
	private boolean layout = true;
	
	public RadarPanel() {
		super();
		// TODO Auto-generated constructor stub
		setBorder(null);  //这些属性可以在雷达面板类中改变
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
	}
	
	public void setRange(String option) {
		if (option.equals("add")) {  //增加量程
			range *= 2;
			if (range > 12) {
				range = 12;
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
		//画出背景圈     计算画面的大小调整
		int startX, startY, diameter;
		if (getWidth()> getHeight()) {     //为什么这里不能用三模运算直接判断赋值     a >b ? a: b;
			diameter = getHeight() - 50; //直径
			layout = true;
		}
		else {
			diameter = getWidth() - 50;
			layout = !layout;
		}
		startX = (getWidth() - diameter)/2;  //左上角位置
		startY = (getHeight() - diameter)/2;
		g2.drawOval(startX-1, startY-1, diameter+2, diameter+2);
		g2.setColor(Color.BLACK);
		g2.fillOval(startX, startY, diameter, diameter);
		//接下来画边上的刻度，参考指针表的实现方法
		g2.setColor(Color.GREEN);
		
		//计算画几个圈,根据量程来决定
		int diaVar = 0;  //画圈的过程中临时的直径
		int diaStep = diameter/(range * 2);  //每次增大直径后的步进值, 得到的值是  每海里的像素值
		for(int i = 1; i < range;System.out.println(range)){
			if (range <= 3) {
				diaVar += diaStep;  //先画最大圈
				g2.drawOval(startX + diaVar, startY + diaVar, diameter - diaVar*2, diameter - diaVar*2);
				diaVar += diaStep;
				i++;
			}
			else {
				diaVar += diaStep*2;
				g2.drawOval(startX + diaVar, startY + diaVar, diameter - diaVar*2, diameter - diaVar*2);
				diaVar += diaStep;
				i += 2;
			}
		}
		
	}
	
	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		super.update(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GREEN);
		
	}
	
}
