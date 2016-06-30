package radarnavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import common.HoverJLable;
import common.Ship;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class RadarPanel extends JPanel{   //雷达面板的显示，更新信息
	
	//这些属性有默认值         这些是雷达面板的额属性值，应当放在雷达类中
	private float range = 6;  //雷达的量程， 单位是海里    雷达量程  最大12海里，最小1海里, 初始化为6海里
	//雷达模式     雷达显示模式    北向上  船首向上    /////相对运动  绝对运动.......
	private boolean headline = true;  //雷达船首线     开启或关闭船首线
	private boolean rangeline = true;  //量程划分线  分多段，随量程变化
	private boolean headup = true;   //首向上    北向上 模式
	private boolean relative = true;  //相对运动  绝对运动
	
	private float startX, startY, diameter;  //显示雷达界面的左上角坐标以及     圆的 --》直径 
	double pc = 1;  //显示每格多少海里
	private Ship ship;  //传入本船的引用
	
	private HoverJLable showMode;
	private HoverJLable activeMode;
	private HoverJLable lineUp;
	private HoverJLable rangeSwitch;
	private HoverJLable showRange;
	private HoverJLable latitude;
	private HoverJLable longitude;
	private HoverJLable course;
	private HoverJLable speed;
	private HoverJLable perCircle;
	
	public RadarPanel() {
		super();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
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
				//System.out.println(((radarPanel) radarpanel).getRange());
				showRange.setText("RANGE : " + range + " KN ");
				//判断梅格表示的海里
				if (range <= 3) {  //一格   0.5  海里
					//pc = diameter/(range*2)/2;
					pc = 0.5;
				}
				else if(range <=6 ){   //一格1海里
					//pc = diameter/(range*2);
					pc = 1;
				}
				else if (range <= 24) {  //一格2海里
					//pc = diameter/(range*2)*2;
					pc = 2;
				}
				else {
					//pc = diameter/(range*2)*4;  //一格4海里
					pc = 4;
				}
				
				perCircle.setText("PER CIRCLE : " + pc +" KN/PC");
				revalidate();
				repaint(1000);
			}
		});
		
		//初始化界面
		initComponents();
	}
	private void initComponents() {    //将这里改成公共方法，有其他更好的方法吗？望解决
		addComponentListener(new ComponentAdapter() {  //实现自动布局
			@Override
			public void componentResized(ComponentEvent e) {  //这个方法可以对布局进行重新设计，可行
				Font font = new Font("Default", Font.PLAIN, (int) (diameter*0.025));
				int h = (int)(diameter*0.04);
				//左上角
				lineUp.setBounds(4, 4, (int)(diameter*0.3), h);
				rangeSwitch.setBounds(4, lineUp.getY()+lineUp.getHeight(), (int)(diameter*0.3), h);
				showMode.setBounds(4, rangeSwitch.getY()+rangeSwitch.getHeight(), (int)(diameter*0.25), h);
				activeMode.setBounds(4, showMode.getY()+showMode.getHeight(), (int)(diameter*0.2), h);
				//左下角
				showRange.setBounds(4, (int) (getHeight()*0.9), (int)(diameter*0.25), h);
				perCircle.setBounds(4, showRange.getY()+showRange.getHeight(), (int)(diameter*0.35), h);
				//右上角
				latitude.setBounds(getWidth()-(int)(diameter*0.3), 4, (int)(diameter*0.3), h);
				longitude.setBounds(getWidth()-(int)(diameter*0.3), latitude.getY()+latitude.getHeight(), (int)(diameter*0.3), h);
				course.setBounds(getWidth()-(int)(diameter*0.25), longitude.getY()+longitude.getHeight(), (int)(diameter*0.25), h);
				speed.setBounds(getWidth()-(int)(diameter*0.2), course.getY()+course.getHeight(), (int)(diameter*0.2), h);
				//设置字体大小
				lineUp.setFont(font);
				rangeSwitch.setFont(font);
				showMode.setFont(font);
				perCircle.setFont(font);
				activeMode.setFont(font);
				showRange.setFont(font);
				latitude.setFont(font);
				longitude.setFont(font);
				course.setFont(font);
				speed.setFont(font);
			}
		});
		
		// TODO Auto-generated constructor stub
		setBorder(null);  //这些属性可以在雷达面板类中改变
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
		
		lineUp = new HoverJLable("HEADLINE --> ON");
		lineUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headline) {
					lineUp.setText("HEADLINE --> OFF");
				}
				else {
					lineUp.setText("HEADLINE --> ON");
				}
				headline = !headline;
			}
		});
		add(lineUp);
		
		rangeSwitch = new HoverJLable("RANGE --> ON");
		rangeSwitch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (rangeline) {
					rangeSwitch.setText("RANGE --> OFF");
					
				}
				else {
					rangeSwitch.setText("RANGE --> ON");
				}
				rangeline = !rangeline;
			}
		});
		add(rangeSwitch);
		
		showMode = new HoverJLable("HEADUP");   //可以写一个JLable的子类，实现相同的动作，减少代码量
		showMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headup) {
					showMode.setText("NORTHUP");
				}
				else {
					showMode.setText("HEADUP");
				}
				headup = !headup;
			}
		});
		
		add(showMode);
		
		activeMode = new HoverJLable("REL");
		activeMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (relative) {
					activeMode.setText("ABS");
				}
				else {
					activeMode.setText("REL");
				}
				relative = !relative;
				
			}
		});
		
		add(activeMode);
		//左下角显示当前设置信息
		showRange = new HoverJLable("RANGE : " + range + " KN ");
		add(showRange);
		perCircle = new HoverJLable("PER CIRCLE : " + pc + " KN/PC ");
		add(perCircle);
		//右上方显示数据信息
		latitude = new HoverJLable("LAT : 0 ", SwingConstants.RIGHT);
		add(latitude);
		longitude = new HoverJLable("LOG : 0 ", SwingConstants.RIGHT);
		add(longitude);
		course = new HoverJLable("COS : 0 °T ", SwingConstants.RIGHT);
		add(course);
		speed = new HoverJLable("SPD : 0 KT ", SwingConstants.RIGHT);
		add(speed);
	}
	
	/***********************普通功能性程序区**********************************************/
	public void setRange(String option) {   //量程大于3及以上是乘法，小于3除法  0.75,1.5,3,6,12,24,48,96
		//如果量程没有开启，则返回，不进行量程变换                    6.27取代哦这项，不人性化
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
	public void getShip(Ship ship){
		this.ship = ship;
	}
	/*public void fresh(){
		//刷新右上角的数据
		latitude.setText("LAT : " + ship.getParameter(1) + " ");
		longitude.setText("LOG : " + ship.getParameter(2) + " ");
		course.setText("COS : " + ship.getParameter(3) + "°T ");
		speed.setText("SPD : " + ship.getParameter(4) + "KT");
	}*/
	/*******************图形绘画区**************************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g.create();   //采用create方法后，如果后边要用到g，同样可行   copy
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//*************画出背景圈     计算画面的大小调整*****************************
		
		//画出背景   这部分不变化
		g2.setColor(Color.GREEN);
		diameter = (float) (Math.min(getWidth(), getHeight())*0.93);
		startX = (getWidth() - diameter)/2;  //左上角位置
		startY = (getHeight() - diameter)/2;
		g2.drawOval((int)startX-1, (int)startY-1, (int)diameter+2, (int)diameter+2);
		//填充黑色背景
		g2.setColor(Color.BLACK);
		g2.fillOval((int)startX, (int)startY, (int)diameter, (int)diameter);
		//***************************更新界面数据******************************************
		//fresh();
		
		//**************接下来画边上的刻度，参考指针表的实现方法***********************
		//每个格点为3°
		if (headup) {    //利用船舶的属性参数来设置界面显示
			drawScale(g2, 0);  //可以随着船舶动态转向      使盘向左转，用负值（——），右转正值（+）
		}
		//**********************计算画几个圈,根据量程来决定*******************************
		if (rangeline) {
			drawRange(g2);
		}
		//********************画线，表示船首向*********************************************
		if (headline) {
			drawHeadLine(g2, 45);
		}
	}
	//画出雷达界面上的数字 ，可以随着船舶航向的变化而变化            还有刻度，方便辨识方向
	public void drawScale(Graphics2D g2, double theta){
		g2.setColor(Color.GREEN);
		//每个格点为3°
		float xCircle = startX + diameter/2;  //计算圆心
		float yCircle = startY + diameter/2;
		//画数字
		for(int i = 0; i<36; i++){
			float semi = diameter/2+10;  //半径
			//数字布局优点问题，以后再改
			float degree = (float) Math.toRadians(i*10-90 + theta);   //在这里添加旋转
			int x = (int) (xCircle + semi * Math.cos(degree));
			int y = (int) (yCircle + semi * Math.sin(degree));
			int num = i * 10;
			g2.setColor(Color.CYAN);
			g2.setFont(new Font("Consolas", Font.PLAIN, (int) (diameter*0.025)));
			g2.drawString(Integer.toString(num) + "°", (int)(x - 0.01*diameter), (int)(y+0.005*diameter));
		}
		//画刻度，可以随着船舶转向转动
        //方向的60个刻度
		AffineTransform af = g2.getTransform();  //从当前上下文获得
		g2.rotate(Math.toRadians(theta), startX+diameter/2, startY+diameter/2);     //在这里更改了
		
        for (int i = 0; i < 360; i++) {   //x  yCircle都是圆心位置
            int bulge = (int) (i % 5 == 0 ? (i%10 == 0?0.02*diameter:0.01*diameter ): 0.005*diameter);  //bulge 凸出
            g2.fillRect((int)(xCircle-(diameter*0.0015)), (int)(startY), (int)(0.003*diameter), bulge);
            g2.rotate(Math.toRadians(1), xCircle, yCircle);  //每一小格转1度, 以圆心为中心点
        }
        //设置旋转重置
        g2.setTransform(af);  //恢复原来的状态
	}
	//画出随着量程变化的环形圈，  方便估算对方的位置
	public void drawRange(Graphics2D g2) {
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
	
	public void drawHeadLine(Graphics2D g2, double theta) {
		//在北向上模式中需要获取船舶航向
		AffineTransform af = g2.getTransform();  //从当前上下文获得
		g2.rotate(Math.toRadians(theta), startX+diameter/2, startY+diameter/2);     //在这里更改了
		
		g2.setColor(Color.GREEN);
		g2.drawLine((int)(startX+diameter/2), (int)(startY+diameter/2), (int)(startX+diameter/2), (int)startY);
		
		g2.setTransform(af);  //恢复原来的状态
	}
}
