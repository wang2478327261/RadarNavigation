package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import java.awt.Choice;

public class RadarPanel extends JPanel{   //雷达面板的显示，更新信息
	
	//这些属性有默认值         这些是雷达面板的额属性值，应当放在雷达类中
	private float range = 6;  //雷达的量程， 单位是海里    雷达量程  最大12海里，最小1海里, 初始化为6海里
	private String mode = "HeadUp";  //雷达模式     雷达显示模式    北向上  船首向上    /////相对运动  绝对运动.......
	private boolean headLine = true;  //雷达船首线     开启或关闭船首线
	private boolean rangeLine = true;  //量程划分线  分多段，随量程变化
	
	private boolean layout = true;  //布局的变化，宽度与高度的比较值，并根据这个值进行布局
	
	public RadarPanel() {
		super();
		initComponents();
	}
	private void initComponents() {
		// TODO Auto-generated constructor stub
		setBorder(null);  //这些属性可以在雷达面板类中改变
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
	}
	
	public void setRange(String option) {   //量程大于3及以上是乘法，小于3除法  0.75,1.5,3,6,12,24,48,96
		if (option.equals("add")) {  //增加量程
			range *= 2;
			if (range >= 96) {
				range = 96;
			}
		}
		else{   //reduce    减少量程
			range /= 2;
			if (range <= 0.75) {
				range = (float) 0.75;
			}
		}
		System.out.println(range);
	}
	public float getRange() {
		return range;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.GREEN);
		/***************画出背景圈     计算画面的大小调整*****************************/
		float startX, startY, diameter;
		if (getWidth()> getHeight()) {     //为什么这里不能用三模运算直接判断赋值     a >b ? a: b;
			diameter = getHeight() - 50; //直径
			layout = true;
		}
		else {
			diameter = getWidth() - 50;
			layout = false;
		}
		startX = (getWidth() - diameter)/2;  //左上角位置
		startY = (getHeight() - diameter)/2;
		g2.drawOval((int)startX-1, (int)startY-1, (int)diameter+2, (int)diameter+2);
		g2.setColor(Color.BLACK);
		g2.fillOval((int)startX, (int)startY, (int)diameter, (int)diameter);
		/**************************接下来画边上的刻度，参考指针表的实现方法***********************/
		g2.setColor(Color.GREEN);
		
		
		/**************************计算画几个圈,根据量程来决定*******************************/
		float diaVar = 0;  //画圈的过程中临时的半径
		float diaStep = diameter/(range * 2);  //每次增大半径后的步进值, 得到的值是  每海里的像素值
		while(diaVar < diameter/2){
			g2.drawOval((int)(startX+diameter/2-diaVar), (int)(startY+diameter/2-diaVar), (int)(diaVar*2), (int)(diaVar*2));
			//判断半径增长率
			if (range <= 3) {  //一格   0.5  海里
				diaVar += diaStep/2;
			}
			else if(range <=6 ){   //一格一海里
				diaVar += diaStep;
			}
			else if (range <= 24) {  //一格2海里
				diaVar += diaStep*2;
			}
			else {
				diaVar += diaStep*4;  //一格4海里
			}
		}
		
	}
	
	
}
