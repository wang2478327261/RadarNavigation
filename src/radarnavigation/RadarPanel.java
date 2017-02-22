package radarnavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import common.HoverJLable;
import common.Ship;

@SuppressWarnings("serial")
public class RadarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	//��Щ������Ĭ��ֵ         ��Щ���״����Ķ�����ֵ��Ӧ�������״�����
	private float range = 6;  //�״�����̣� ��λ�Ǻ���    �״�����  ���12�����С1����, ��ʼ��Ϊ6����
	//�״�ģʽ     �״���ʾģʽ    ������  ��������    /////����˶�  �����˶�.......
	private boolean headline = true;  //�״ﴬ����     ������رմ�����
	private boolean rangeline = true;  //���̻�����  �ֶ�Σ������̱仯
	private boolean headup = true;   //������    ������ ģʽ
	private boolean relative = true;  //����˶�  �����˶�
	
	private float startX, startY, diameter;  //��ʾ�״��������Ͻ������Լ�     Բ�� --��ֱ�� 
	double pc = 1;  //��ʾÿ����ٺ���
	private Ship ship;  //���뱾��������
	
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
				//�ı��״�����,   ���Ϲ���Ϊ��ֵ -1�����¹�����ֵ  1
				if (e.getWheelRotation() > 0) {   //��С����
					setRange("reduce");
				}
				if(e.getWheelRotation() < 0){  //��������
					setRange("increase");
				}
				//System.out.println(((radarPanel) radarpanel).getRange());
				showRange.setText("RANGE : " + range + " KN ");
				//�ж�÷���ʾ�ĺ���
				if (range <= 3) {  //һ��   0.5  ����
					//pc = diameter/(range*2)/2;
					pc = 0.5;
				}
				else if(range <=6 ){   //һ��1����
					//pc = diameter/(range*2);
					pc = 1;
				}
				else if (range <= 24) {  //һ��2����
					//pc = diameter/(range*2)*2;
					pc = 2;
				}
				else {
					//pc = diameter/(range*2)*4;  //һ��4����
					pc = 4;
				}
				
				perCircle.setText("PER CIRCLE : " + pc +" KN/PC");
				revalidate();
				repaint(1000);
			}
		});
		
		//��ʼ������
		initComponents();
	}
	private void initComponents() {    //������ĳɹ������������������õķ����������
		addComponentListener(new ComponentAdapter() {  //ʵ���Զ�����
			@Override
			public void componentResized(ComponentEvent e) {  //����������ԶԲ��ֽ���������ƣ�����
				Font font = new Font("Default", Font.PLAIN, (int) (diameter*0.025));
				int h = (int)(diameter*0.04);
				//���Ͻ�
				lineUp.setBounds(4, 4, (int)(diameter*0.3), h);
				rangeSwitch.setBounds(4, lineUp.getY()+lineUp.getHeight(), (int)(diameter*0.3), h);
				showMode.setBounds(4, rangeSwitch.getY()+rangeSwitch.getHeight(), (int)(diameter*0.25), h);
				activeMode.setBounds(4, showMode.getY()+showMode.getHeight(), (int)(diameter*0.2), h);
				//���½�
				showRange.setBounds(4, (int) (getHeight()*0.9), (int)(diameter*0.25), h);
				perCircle.setBounds(4, showRange.getY()+showRange.getHeight(), (int)(diameter*0.35), h);
				//���Ͻ�
				latitude.setBounds(getWidth()-(int)(diameter*0.3), 4, (int)(diameter*0.3), h);
				longitude.setBounds(getWidth()-(int)(diameter*0.3), latitude.getY()+latitude.getHeight(), (int)(diameter*0.3), h);
				course.setBounds(getWidth()-(int)(diameter*0.25), longitude.getY()+longitude.getHeight(), (int)(diameter*0.25), h);
				speed.setBounds(getWidth()-(int)(diameter*0.2), course.getY()+course.getHeight(), (int)(diameter*0.2), h);
				//���������С
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
		setBorder(null);  //��Щ���Կ������״�������иı�
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
		
		lineUp = new HoverJLable("HEADLINE < ON > ");
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
		
		rangeSwitch = new HoverJLable("RANGE < ON > ");
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
		
		showMode = new HoverJLable("HEADUP");   //����дһ��JLable�����࣬ʵ����ͬ�Ķ��������ٴ�����
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
		
		activeMode = new HoverJLable("RELATIVE");
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
		//���½���ʾ��ǰ������Ϣ
		showRange = new HoverJLable("RANGE : " + range + " KN ");
		add(showRange);
		perCircle = new HoverJLable("PER CIRCLE : " + pc + " KN/PC ");
		add(perCircle);
		//���Ϸ���ʾ������Ϣ
		latitude = new HoverJLable("LAT : 0 ", SwingConstants.RIGHT);
		add(latitude);
		longitude = new HoverJLable("LOG : 0 ", SwingConstants.RIGHT);
		add(longitude);
		course = new HoverJLable("COS : 0 ��T ", SwingConstants.RIGHT);
		add(course);
		speed = new HoverJLable("SPD : 0 KT ", SwingConstants.RIGHT);
		add(speed);
	}
	
	/***********************��ͨ�����Գ�����**********************************************/
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
		latitude.setText("LAT : " + ship.getParameter(1) + " ");
		longitude.setText("LOG : " + ship.getParameter(2) + " ");
		course.setText("COS : " + ship.getParameter(3) + "��T ");
		speed.setText("SPD : " + ship.getParameter(4) + "KT ");
		
	}
	/*******************ͼ�λ滭��**************************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g.create();   //转换成2D
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  //渲染效果
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
		
		//**************�����������ϵĿ̶ȣ��ο�ָ����ʵ�ַ���***********************
		//ÿ�����Ϊ3��
		if (headup) {    //���ô��������Բ��������ý�����ʾ
			drawScale(g2, ship.getParameter(3));  //�������Ŵ�����̬ת��      ʹ������ת���ø�ֵ������������ת��ֵ��+��
		}
		//**********************���㻭����Ȧ,��������������*******************************
		if (rangeline) {
			drawRange(g2);
		}
		//********************���ߣ���ʾ������*********************************************
		if (headline) {
			drawHeadLine(g2, ship.getParameter(3));
		}
	}
	//�����״�����ϵ����� ���������Ŵ�������ı仯���仯            ���п̶ȣ������ʶ����
	public void drawScale(Graphics2D g2, double theta){
		System.out.println("RadarPanel -> drawScale");
		g2.setColor(Color.GREEN);
		//ÿ�����Ϊ3��
		float xCircle = startX + diameter/2;  //����Բ��
		float yCircle = startY + diameter/2;
		//������
		g2.setColor(Color.CYAN);
		for(int i = 0; i<36; i++){
			float semi = diameter/2+10;  //�뾶
			//���ֲ����ŵ����⣬�Ժ��ٸ�
			float degree = (float) Math.toRadians(i*10-90 + theta);   //�����������ת
			int x = (int) (xCircle + semi * Math.cos(degree));
			int y = (int) (yCircle + semi * Math.sin(degree));
			int num = i * 10;
			g2.drawString(Integer.toString(num) + "��", (int)(x - 0.01*diameter), (int)(y+0.005*diameter));
		}
		//���̶ȣ��������Ŵ���ת��ת��
        //�����60���̶�
		AffineTransform af = g2.getTransform();  //�ӵ�ǰ�����Ļ��
		g2.rotate(Math.toRadians(theta), startX+diameter/2, startY+diameter/2);     //�����������
		
        for (int i = 0; i < 360; i++) {   //x  yCircle����Բ��λ��
            int bulge = (int) (i % 5 == 0 ? (i%10 == 0?0.02*diameter:0.01*diameter ): 0.005*diameter);  //bulge ͹��
            g2.fillRect((int)(xCircle-(diameter*0.0015)), (int)(startY), (int)(0.003*diameter), bulge);
            g2.rotate(Math.toRadians(1), xCircle, yCircle);  //ÿһС��ת1��, ��Բ��Ϊ���ĵ�
        }
        //������ת����
        g2.setTransform(af);  //�ָ�ԭ����״̬
	}
	//�����������̱仯�Ļ���Ȧ��  �������Է���λ��
	public void drawRange(Graphics2D g2) {
		System.out.println("RadarPanel -> drawRange");
		g2.setColor(Color.LIGHT_GRAY);
		float diaVar = 0;  //��Ȧ�Ĺ�������ʱ�İ뾶
		float diaStep = diameter/(range * 2);  //ÿ������뾶��Ĳ���ֵ, �õ���ֵ��  ÿ���������ֵ
		
		while(diaVar < diameter/2){
			g2.drawOval((int)(startX+diameter/2-diaVar), (int)(startY+diameter/2-diaVar), (int)(diaVar*2), (int)(diaVar*2));
			//�жϰ뾶������
			if (range <= 3) {  //һ��   0.5  ����
				diaVar += diaStep/2;
			}
			else if(range <=6 ){   //һ��һ����
				diaVar += diaStep;
			}
			else if (range <= 24) {  //һ��2����
				diaVar += diaStep*2;
			}
			else {
				diaVar += diaStep*4;  //һ��4����
			}
		}
	}
	
	public void drawHeadLine(Graphics2D g2, double theta) {
		System.out.println("RadarPanel -> drawHeadline");
		//�ڱ�����ģʽ����Ҫ��ȡ��������
		AffineTransform af = g2.getTransform();  //�ӵ�ǰ�����Ļ��//这里应该是存储当前坐标系的变换
		g2.rotate(Math.toRadians(theta), startX+diameter/2, startY+diameter/2);     //�����������
		
		g2.setColor(Color.GREEN);
		g2.drawLine((int)(startX+diameter/2), (int)(startY+diameter/2), (int)(startX+diameter/2), (int)startY);
		
		g2.setTransform(af);  //�ָ�ԭ����״̬
	}
}
