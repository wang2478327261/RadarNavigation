package radarnavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import common.Ship;
import javax.swing.JLabel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
	private Ship ship;  //传入本船的引用
	
	private JLabel showMode;
	private JLabel activeMode;
	private JLabel lineUp;
	private JLabel rangeSwitch;
	private JLabel showRange;
	private JLabel latitude;
	private JLabel longitude;
	private JLabel course;
	private JLabel speed;
	
	public RadarPanel() {
		super();
		initComponents();
	}
	private void initComponents() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {  //这个方法可以对布局进行重新设计，可行
				course.setBounds(getWidth() - 150, 4, 150, 25);
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
		
		showMode = new JLabel("HEADUP");   //可以写一个JLable的子类，实现相同的动作，减少代码量
		showMode.setHorizontalAlignment(SwingConstants.LEADING);
		showMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				showMode.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				showMode.setBorder(BorderFactory.createEmptyBorder());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headup) {
					showMode.setText("NORTHUP");
					headup = !headup;
				}
				else {
					showMode.setText("HEADUP");
					headup = !headup;
				}
				
			}
		});
		showMode.setFont(new Font("Consolas", Font.BOLD, 18));
		showMode.setForeground(Color.GREEN);
		showMode.setBackground(Color.DARK_GRAY);
		showMode.setBorder(BorderFactory.createEmptyBorder());
		showMode.setBounds(4, 54, 100, 25);
		add(showMode);
		
		activeMode = new JLabel("RELATIVE");
		activeMode.setHorizontalAlignment(SwingConstants.LEADING);
		activeMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				activeMode.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				activeMode.setBorder(BorderFactory.createEmptyBorder());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (relative) {
					activeMode.setText("ABSOLUTE");
					relative = !relative;
				}
				else{
					activeMode.setText("RELATIVE");
					relative = !relative;
				}
				
			}
		});
		activeMode.setFont(new Font("Consolas", Font.BOLD, 18));
		activeMode.setForeground(Color.GREEN);
		activeMode.setBackground(Color.DARK_GRAY);
		activeMode.setBorder(BorderFactory.createEmptyBorder());
		activeMode.setBounds(4, 79, 100, 25);
		add(activeMode);
		
		lineUp = new JLabel("HEADLINE->ON");
		lineUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lineUp.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lineUp.setBorder(BorderFactory.createEmptyBorder());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headline) {
					lineUp.setText("HEADLINE->OFF");
					headline = !headline;
				}
				else {
					lineUp.setText("HEADLINE->ON");
					headline = !headline;
				}
			}
		});
		lineUp.setHorizontalAlignment(SwingConstants.LEADING);
		lineUp.setForeground(Color.GREEN);
		lineUp.setFont(new Font("Consolas", Font.BOLD, 18));
		lineUp.setBorder(BorderFactory.createEmptyBorder());
		lineUp.setBackground(Color.DARK_GRAY);
		lineUp.setBounds(4, 4, 150, 25);
		add(lineUp);
		
		rangeSwitch = new JLabel("RANGE->ON");
		rangeSwitch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rangeSwitch.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				rangeSwitch.setBorder(BorderFactory.createEmptyBorder());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (rangeline) {
					rangeSwitch.setText("RANGE->OFF");
					rangeline = !rangeline;
				}
				else {
					rangeSwitch.setText("RANGE->ON");
					rangeline = !rangeline;
				}
			}
		});
		rangeSwitch.setHorizontalAlignment(SwingConstants.LEADING);
		rangeSwitch.setForeground(Color.GREEN);
		rangeSwitch.setFont(new Font("Consolas", Font.BOLD, 18));
		rangeSwitch.setBorder(BorderFactory.createEmptyBorder());
		rangeSwitch.setBackground(Color.DARK_GRAY);
		rangeSwitch.setBounds(4, 29, 150, 25);
		add(rangeSwitch);
		
		showRange = new JLabel("RANGE :" + range);
		showRange.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				showRange.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				showRange.setBorder(BorderFactory.createEmptyBorder());
			}
		});
		showRange.setFont(new Font("Consolas", Font.BOLD, 18));
		showRange.setForeground(Color.GREEN);
		showRange.setBackground(Color.DARK_GRAY);
		showRange.setBounds(4, 600, 150, 25);
		add(showRange);
		
		latitude = new JLabel("LAT : ");
		latitude.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				latitude.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				latitude.setBorder(BorderFactory.createEmptyBorder());
			}
		});
		latitude.setHorizontalAlignment(SwingConstants.LEADING);
		latitude.setForeground(Color.GREEN);
		latitude.setFont(new Font("Consolas", Font.BOLD, 18));
		latitude.setBorder(BorderFactory.createEmptyBorder());
		latitude.setBackground(Color.DARK_GRAY);
		latitude.setBounds(600, 4, 180, 25);
		add(latitude);
		
		longitude = new JLabel("LOG : ");
		longitude.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				longitude.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				longitude.setBorder(BorderFactory.createEmptyBorder());
			}
		});
		longitude.setHorizontalAlignment(SwingConstants.LEADING);
		longitude.setForeground(Color.GREEN);
		longitude.setFont(new Font("Consolas", Font.BOLD, 18));
		longitude.setBorder(BorderFactory.createEmptyBorder());
		longitude.setBackground(Color.DARK_GRAY);
		longitude.setBounds(600, 29, 180, 25);
		add(longitude);
		
		course = new JLabel("COS : ");
		course.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				course.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				course.setBorder(BorderFactory.createEmptyBorder());
			}
		});
		course.setHorizontalAlignment(SwingConstants.LEADING);
		course.setForeground(Color.GREEN);
		course.setFont(new Font("Consolas", Font.BOLD, 18));
		course.setBorder(BorderFactory.createEmptyBorder());
		course.setBackground(Color.DARK_GRAY);
		course.setBounds(650, 54, 120, 25);
		add(course);
		
		speed = new JLabel("SPD : ");
		speed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				speed.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				speed.setBorder(BorderFactory.createEmptyBorder());
			}
		});
		speed.setHorizontalAlignment(SwingConstants.LEADING);
		speed.setForeground(Color.GREEN);
		speed.setFont(new Font("Consolas", Font.BOLD, 18));
		speed.setBorder(BorderFactory.createEmptyBorder());
		speed.setBackground(Color.DARK_GRAY);
		speed.setBounds(650, 79, 120, 25);
		add(speed);
	}
	
	/***********************普通功能性程序区**********************************************/
	public void setRange(String option) {   //量程大于3及以上是乘法，小于3除法  0.75,1.5,3,6,12,24,48,96
		if (rangeline) {  //如果量程没有开启，则返回，不进行量程变换
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
	}
	public float getRange() {
		return range;
	}
	public void getShip(Ship ship){
		this.ship = ship;
	}
	
	/*******************图形绘画区**************************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//*************画出背景圈     计算画面的大小调整*****************************
		
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
		if (headup) {
			drawScale(g2);  //可以随着船舶动态转向
		}
		//**********************计算画几个圈,根据量程来决定*******************************
		//g2.fillOval((int)(startX+diameter/2-2), (int)(startY+diameter/2-2), 4, 4);  //画中心点
		if (rangeline) {
			drawRange(g2);
		}
		if (headline) {
			drawHeadLine(g2);
		}
	}
	//画出雷达界面上的数字 ，可以随着船舶航向的变化而变化            还有刻度，方便辨识方向
	public void drawScale(Graphics2D g2){
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
	
	public void drawHeadLine(Graphics2D g2) {
		//在北向上模式中需要获取船舶航向
		g2.setColor(Color.GREEN);
		g2.drawLine((int)(startX+diameter/2), (int)(startY+diameter/2), (int)(startX+diameter/2), (int)startY);
	}
}
