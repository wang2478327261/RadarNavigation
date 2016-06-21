package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class RadarPanel extends JPanel{   //雷达面板的显示，更新信息
	
	//这些属性有默认值         这些是雷达面板的额属性值，应当放在雷达类中
	private float range = 6;  //雷达的量程， 单位是海里    雷达量程  最大12海里，最小1海里, 初始化为6海里
	private String mode = "HeadUp";  //雷达模式     雷达显示模式    北向上  船首向上    /////相对运动  绝对运动.......
	private boolean headLine = true;  //雷达船首线     开启或关闭船首线
	private boolean rangeLine = true;  //量程划分线  分多段，随量程变化
	//去掉布局标志简化代码，后边直接判断比较
	//private boolean layout = true;  //布局的变化，宽度与高度的比较值，并根据这个值进行布局
	
	public RadarPanel() {
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
			public void mouseClicked(MouseEvent e) {   //这里可以加入时间测试，实现功能之间的区别，网上擦还训
				//选中对方船舶或或者取消选中（右键单击）
				if(e.getButton() == MouseEvent.BUTTON1){   //左键 16，中键 8，右键 4    e.getModifiers() == 16
					//单击事件
					if (e.getClickCount() >= 2) {
						if (!radarnavigation.isUndecorated()) {
							radarnavigation.setLocation(0, 0);
							radarnavigation.setSize(Toolkit.getDefaultToolkit().getScreenSize());
							//去除标题栏
							radarnavigation.dispose();
							radarnavigation.setUndecorated(true);
							//getRootPane().setWindowDecorationStyle(JRootPane.NONE);
							radarnavigation.setVisible(true);
						}
						else {
							radarnavigation.setBounds(20, 20, 1008, 735);
							//归位,返回原来的尺寸，只能到初始化尺寸，若果要放大前，需要增加变量存储之前的尺寸及位置
							radarnavigation.dispose();
							radarnavigation.setUndecorated(false);
							radarnavigation.setVisible(true);
						}
						radarnavigation.revalidate();
					}
				}
				if(e.getButton() == MouseEvent.BUTTON3){
					//实现取消选中功能
					radarnavigation.setTitle("RadarNavigation -->" + e.getX()  + "," + e.getY());
				}
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//改变雷达量程,   向上滚动为负值 -1，向下滚动正值  1
				if (e.getWheelRotation() > 0) {   //减小量程
					setRange("reduce");
				}
				if(e.getWheelRotation() < 0){  //增大量程
					setRange("increase");
				}
				revalidate();
				//System.out.println(((radarPanel) radarpanel).getRange());
				repaint(1000);
			}
		});
		// TODO Auto-generated constructor stub
		setBorder(null);  //这些属性可以在雷达面板类中改变
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
	}
	/***********************普通功能性程序区**********************************************/
	public void setRange(String option) {   //量程大于3及以上是乘法，小于3除法  0.75,1.5,3,6,12,24,48,96
		if (option.equals("increase")) {  //增加量程
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
	RadarNavigation radarnavigation;
	public void refer(RadarNavigation radarnavigation){  //将radarnavigation引入
		this.radarnavigation = radarnavigation;
	}
	
	/*******************图形绘画区**************************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//*************画出背景圈     计算画面的大小调整*****************************
		float startX, startY, diameter;
		//画出背景   这部分不变化
		g2.setColor(Color.GREEN);
		diameter = (float) (Math.min(getWidth(), getHeight())*0.93);
		startX = (getWidth() - diameter)/2;  //左上角位置
		startY = (getHeight() - diameter)/2;
		g2.drawOval((int)startX-1, (int)startY-1, (int)diameter+2, (int)diameter+2);
		g2.setColor(Color.BLACK);
		g2.fillOval((int)startX, (int)startY, (int)diameter, (int)diameter);
		//**************接下来画边上的刻度，参考指针表的实现方法***********************
		//每个格点为3°
		if (mode.equalsIgnoreCase("HeadUp")) {
			drawScale(g2, diameter, startX, startY);  //可以随着船舶动态转向
		}
		//**********************计算画几个圈,根据量程来决定*******************************
		//g2.fillOval((int)(startX+diameter/2-2), (int)(startY+diameter/2-2), 4, 4);  //画中心点
		if (rangeLine) {
			drawRange(g2, diameter, startX, startY);
		}
		if (headLine) {
			drawHeadLine(g2, diameter, startX, startY);
		}
	}
	//画出雷达界面上的数字 ，可以随着船舶航向的变化而变化            还有刻度，方便辨识方向
	public void drawScale(Graphics2D g2, float diameter, float startX, float startY){
		g2.setColor(Color.GREEN);
		//每个格点为3°
		float xCircle = startX + diameter/2;  //计算圆心
		float yCircle = startY + diameter/2;
		for(int i = 0; i<36; i++){
			float semi = diameter/2+10;  //半径
			//数字布局优点问题，以后再改
			float degree = (float) Math.toRadians(i*10-90);
			int x = (int) (xCircle + semi * Math.cos(degree));
			int y = (int) (yCircle + semi * Math.sin(degree));
			int num = i * 10;
			g2.setColor(Color.CYAN);
			g2.setFont(new Font("Consolas", Font.PLAIN, (int) (diameter*0.025)));
			g2.drawString(Integer.toString(num) + "°", (int)(x - 0.01*diameter), (int)(y+0.005*diameter));
		}
		
		//画刻度，可以随着船舶转向转动
		AffineTransform old = g2.getTransform();
        //方向的60个刻度
        for (int i = 0; i < 360; i++) {   //x  yCircle都是圆心位置
            int bulge = (int) (i % 10 == 0 ? 0.02*diameter : 0.005*diameter);  //bulge 凸出
            g2.fillRect((int)(xCircle-(diameter*0.0015)), (int)(startY), (int)(0.003*diameter), bulge);
            g2.rotate(Math.toRadians(1), xCircle, yCircle);  //每一小格转6度, 以圆心为中心点
        }
        //设置旋转重置
        g2.setTransform(old);
	}
	//画出随着量程变化的环形圈，  方便估算对方的位置
	public void drawRange(Graphics2D g2, float diameter, float startX, float startY) {
		g2.setColor(Color.LIGHT_GRAY);
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
	
	public void drawHeadLine(Graphics2D g2, float diameter, float startX, float startY) {
		//在北向上模式中需要获取船舶航向
		g2.setColor(Color.GREEN);
		g2.drawLine((int)(startX+diameter/2), (int)(startY+diameter/2), (int)(startX+diameter/2), (int)startY);
	}
	
	
}
