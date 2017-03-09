package radarnavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.HoverLable;
import common.Ship;

public class RadarPanel extends JPanel{   //显示主界面
	
	private static final long serialVersionUID = -6000318065148555968L;
	
	private float range = 6;  //量程
	private boolean headline = true;
	private boolean rangeline = true;  //是否显示量程
	private boolean headup = true;   //是否首向上
	private boolean relative = true;  //是否相对运动
	private float startX, startY, diameter;  //中间圆的左上角坐标，直径
	double pc = 1;  //每圈代表的距离,跟随range变化
	
	private HoverLable showMode;  //首向上还是北向上
	private HoverLable activeMode;  //相对运动还是绝对运动
	private HoverLable lineUp;  //是否显示船首线
	private HoverLable rangeSwitch;  //是否打开显示 量程
	private HoverLable showRange;  //显示当前的量程是多少
	private HoverLable latitude;  //当前经度
	private HoverLable longitude;  //当前纬度
	private HoverLable course;  //当前航向
	private HoverLable speed;  //当前航速
	private HoverLable perCircle;  //当前量程下每个量程圈的大小，是多少海里
	
	private Ship ship;  //当前自己的对象
	private List<Ship> ships = new LinkedList<>();  //是在外部进行过滤还是在里面？当前显示的船舶对象2017.3.9:不过滤
	
	public RadarPanel() {
		super();
		//2017.3.1 去掉鼠标进出的边框变化效果，没必要这种动态变化
		//初始化界面
		initComponents();
	}
	private void initComponents() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {  //缩放偏移时应当记录当前的信息，以便于还原
				if (e.getWheelRotation() > 0) {   //减小量程
					setRange("reduce");
				}
				if(e.getWheelRotation() < 0){  //增大量程
					setRange("increase");
				}
				//System.out.println(((radarPanel) radarpanel).getRange());
				showRange.setText("RANGE : " + range + " KN ");
				//更新显示信息
				if (range <= 3) {
					//pc = diameter/(range*2)/2;
					pc = 0.5;
				}
				else if(range <=6 ){   //每圈1海里
					//pc = diameter/(range*2);
					pc = 1;
				}
				else if (range <= 24) {
					//pc = diameter/(range*2)*2;
					pc = 2;
				}
				else {
					//pc = diameter/(range*2)*4;
					pc = 4;
				}
				
				perCircle.setText("PER CIRCLE : " + pc +" KN/PC");
				revalidate();
				repaint(1000);
			}
		});
		
		addComponentListener(new ComponentAdapter() {  //全屏缩放，怎么制作响应式界面
			@Override
			public void componentResized(ComponentEvent e) {  //缩放后的同时更新界面
				Font font = new Font("Default", Font.PLAIN, (int) (diameter*0.025));
				int h = (int)(diameter*0.04);
				//响应式布局--以下的计算都是根据一个对象的尺寸同步变换，这里是圆的直径
				lineUp.setBounds(0, 4, (int)(diameter*0.3), h);
				rangeSwitch.setBounds(0, lineUp.getY()+lineUp.getHeight(), (int)(diameter*0.3), h);
				showMode.setBounds(0, rangeSwitch.getY()+rangeSwitch.getHeight(), (int)(diameter*0.25), h);
				activeMode.setBounds(0, showMode.getY()+showMode.getHeight(), (int)(diameter*0.2), h);
				
				showRange.setBounds(0, (int) (getHeight()*0.9), (int)(diameter*0.25), h);
				perCircle.setBounds(0, showRange.getY()+showRange.getHeight(), (int)(diameter*0.35), h);
				
				latitude.setBounds(getWidth()-(int)(diameter*0.3), 4, (int)(diameter*0.3), h);
				longitude.setBounds(getWidth()-(int)(diameter*0.3), latitude.getY()+latitude.getHeight(), (int)(diameter*0.3), h);
				course.setBounds(getWidth()-(int)(diameter*0.25), longitude.getY()+longitude.getHeight(), (int)(diameter*0.25), h);
				speed.setBounds(getWidth()-(int)(diameter*0.2), course.getY()+course.getHeight(), (int)(diameter*0.2), h);
				//字体设置，有没有好点的办法
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
		
		// TODO 总体设置
		setBorder(BorderFactory.createLineBorder(Color.GREEN));  //自由布局
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
		
		lineUp = new HoverLable("HEADLINE < ON > ");
		lineUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headline) {
					lineUp.setText("HEADLINE < OFF > ");
				}
				else {
					lineUp.setText("HEADLINE < ON > ");
				}
				headline = !headline;
				repaint();
			}
		});
		add(lineUp);
		
		rangeSwitch = new HoverLable("RANGE < ON > ");
		rangeSwitch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (rangeline) {
					rangeSwitch.setText("RANGE < OFF > ");
				}
				else {
					rangeSwitch.setText("RANGE < ON > ");
				}
				rangeline = !rangeline;
				repaint();
			}
		});
		add(rangeSwitch);
		
		showMode = new HoverLable("HEADUP");
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
				repaint();
			}
		});
		add(showMode);
		
		activeMode = new HoverLable("RELATIVE");
		activeMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (relative) {
					activeMode.setText("ABSOLUTE");
				}
				else {
					activeMode.setText("RELATIVE");
				}
				relative = !relative;
				repaint();
			}
		});
		
		add(activeMode);
		showRange = new HoverLable("RANGE : " + range + " KN ");
		add(showRange);
		perCircle = new HoverLable("PER CIRCLE : " + pc + " KN/PC ");
		add(perCircle);
		latitude = new HoverLable("LAT : 0 ", SwingConstants.RIGHT);
		add(latitude);
		longitude = new HoverLable("LOG : 0 ", SwingConstants.RIGHT);
		add(longitude);
		course = new HoverLable("COS : 0 T ", SwingConstants.RIGHT);
		add(course);
		speed = new HoverLable("SPD : 0 KT ", SwingConstants.RIGHT);
		add(speed);
	}
	
	/**************normal method*******************************************************/
	public void setRange(String option) {   //变化量程
		if (option.equals("increase")) {  //判断缩放动作
			range *= 2;
			if (range >= 96) {
				range = 96;
			}
		}
		else{   //reduce scale
			range /= 2;
			if (range <= 0.75) {
				range = (float) 0.75;
			}
		}
		System.out.println("RadarPanel -> setRange");
	}
	public float getRange() {
		return range;
	}
	public void getShip(Ship ship){
		this.ship = ship;
	}
	
	public void dataFresh(){
		System.out.println("RadarPanel -> dataFresh");
		//显示组件的刷新
		latitude.setText("LAT : " + ship.getParameter(1) + "  ");  //多加了空格看起来清除
		longitude.setText("LOG : " + ship.getParameter(2) + "  ");
		course.setText("COS : " + ship.getParameter(3) + " T  ");
		speed.setText("SPD : " + ship.getParameter(4) + "KT  ");
	}
	/*******************Repaint**************************************************************/
	@Override
	public void paint(Graphics g) {  //需要根据量程重新绘制，这种方法不好，和信息面板一样，用组件，消耗更少的资源
									//或者查阅资料，实现动画的局部刷新
		// TODO 绘制雷达界面
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g.create();   //转换成2D
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  //渲染效果
		Font f = new Font("Default", Font.PLAIN, (int) (diameter*0.025));
		g2.setFont(f);  //设置字体
		//*************************************************************
		g2.setColor(Color.GREEN);
		diameter = (float) (Math.min(getWidth(), getHeight())*0.93);
		startX = (getWidth() - diameter)/2;  //雷达显示圆形边框
		startY = (getHeight() - diameter)/2;
		g2.drawOval((int)startX-1, (int)startY-1, (int)diameter+2, (int)diameter+2);
		//背景
		g2.setColor(Color.BLACK);
		g2.fillOval((int)startX, (int)startY, (int)diameter, (int)diameter);
		//***********************刷新**********************************
		dataFresh();
		
		//**************绘制刻度***********************
		if (headup) {
			drawScale(g2, ship.getParameter(3));
		}
		//**********************绘制量程*******************************
		if (rangeline) {
			drawRange(g2);
		}
		//******************绘制首向线********************************************
		if (headline) {
			drawHeadLine(g2, ship.getParameter(3));
		}
		//***********************************************************************
		drawShips();
	}
	
	public void drawScale(Graphics2D g2, double theta){  //角度的刻度  theta rotate
		System.out.println("RadarPanel -> drawScale");
		//圆心坐标
		float xCircle = startX + diameter/2;  //圆心x坐标
		float yCircle = startY + diameter/2;  //圆心y坐标
		
		//画出一圈数字
		g2.setColor(Color.CYAN);  //这里虽然计算正确，但是应该考虑degree过大时候会溢出的情况，启动线程将角度化简
		for(int i = 0; i<36; i++){  //一圈的度数  指示
			float semi = diameter/2+10;  //度数显示的坐标半径
			float degree = (float) Math.toRadians(i*10-90 + theta);  //-90是因为起始是在x坐标那块
			int x = (int) (xCircle + semi * Math.cos(degree));
			int y = (int) (yCircle + semi * Math.sin(degree));
			int num = i * 10;
			g2.drawString(Integer.toString(num) + "^", (int)(x - 0.01*diameter), (int)(y+0.005*diameter));
		}
		//图形旋转
		AffineTransform af = g2.getTransform();  //保存以前的坐标信息
		g2.rotate(Math.toRadians(theta), xCircle, yCircle);  //这里以前出错了，转角应该是弧度，不能是角度
		//g2.rotate(theta, xCircle, yCircle);
		//画刻度
		g2.setColor(Color.GREEN);
        for (int i = 0; i < 360; i++) {
        	//i是5的倍数吗？不是就画短，如果是，i是10的倍数吗？
            int bulge = (int) (i % 5 == 0 ? (i%10 == 0?0.02*diameter:0.01*diameter ): 0.005*diameter);  //计算是长凸出还是短凸出
            g2.fillRect((int)(xCircle-(diameter*0.0015)), (int)(startY), (int)(0.003*diameter), bulge);
            //g2.drawLine((int)(xCircle-(diameter*0.0015)), (int)(startY), (int)(xCircle-(diameter*0.0015)+0.003*diameter), (int)(startY+bulge));
            g2.rotate(Math.toRadians(1), xCircle, yCircle);
        }
        //g2.rotate(Math.toRadians(theta), xCircle, yCircle);  //放在后面不行，必须放在前面
        //还原坐标系
        g2.setTransform(af);
	}
	//绘制量程
	public void drawRange(Graphics2D g2) {
		System.out.println("RadarPanel -> drawRange");
		g2.setColor(Color.LIGHT_GRAY);
		float diaVar = 0;  //每次变化的幅度
		float diaStep = diameter/(range * 2);
		while(diaVar < diameter/2){
			g2.drawOval((int)(startX+diameter/2-diaVar), (int)(startY+diameter/2-diaVar), (int)(diaVar*2), (int)(diaVar*2));
			if (range <= 3) {
				diaVar += diaStep/2;
			}
			else if(range <=6 ){
				diaVar += diaStep;
			}
			else if (range <= 24) {
				diaVar += diaStep*2;
			}
			else {
				diaVar += diaStep*4;
			}
		}
	}
	
	public void drawHeadLine(Graphics2D g2, double theta) {  //theta -> rotate degree
		System.out.println("RadarPanel -> drawHeadline");
		AffineTransform af = g2.getTransform();  //这里应该是存储当前坐标系的变换
		g2.rotate(Math.toRadians(theta), startX+diameter/2, startY+diameter/2);  //以圆心为中心，旋转theta角度
		
		g2.setColor(Color.GREEN);
		g2.drawLine((int)(startX+diameter/2), (int)(startY+diameter/2), (int)(startX+diameter/2), (int)startY);
		g2.setTransform(af);
	}
	
	public void drawShips(){
		//绘制当前船舶的模糊对象
	}
}
