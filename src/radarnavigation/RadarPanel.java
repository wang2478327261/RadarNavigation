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
import common.HoverJLable;
import common.Ship;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
		
		
		//��ʼ������
		initComponents();
	}
	private void initComponents() {
		addComponentListener(new ComponentAdapter() {  //ʵ���Զ�����
			@Override
			public void componentResized(ComponentEvent e) {  //����������ԶԲ��ֽ���������ƣ�����
				//���Ͻ�
				lineUp.setBounds(4, 4, (int)(diameter*0.25), (int)(diameter*0.04));
				rangeSwitch.setBounds(4, lineUp.getY()+lineUp.getHeight(), (int)(diameter*0.25), (int)(diameter*0.04));
				showMode.setBounds(4, rangeSwitch.getY()+rangeSwitch.getHeight(), (int)(diameter*0.125), (int)(diameter*0.04));
				activeMode.setBounds(4, showMode.getY()+showMode.getHeight(), (int)(diameter*0.125), (int)(diameter*0.04));
				//���½�
				showRange.setBounds(4, (int) (getHeight()*0.9), (int)(diameter*0.25), (int)(diameter*0.04));
				perCircle.setBounds(4, showRange.getY()+showRange.getHeight(), (int)(diameter*0.4), (int)(diameter*0.04));
				//���Ͻ�
				latitude.setBounds(getWidth()-(int)(diameter*0.3), 4, (int)(diameter*0.3), (int)(diameter*0.04));
				longitude.setBounds(getWidth()-(int)(diameter*0.3), latitude.getY()+latitude.getHeight(), (int)(diameter*0.3), (int)(diameter*0.04));
				course.setBounds(getWidth()-(int)(diameter*0.25), longitude.getY()+longitude.getHeight(), (int)(diameter*0.25), (int)(diameter*0.04));
				speed.setBounds(getWidth()-(int)(diameter*0.2), course.getY()+course.getHeight(), (int)(diameter*0.2), (int)(diameter*0.04));
				//���������С
				lineUp.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				rangeSwitch.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				showMode.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				perCircle.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				activeMode.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				showRange.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				latitude.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				longitude.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				course.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
				speed.setFont(new Font("Consolas", Font.BOLD, (int) (diameter*0.03)));
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
				//�ı��״�����,   ���Ϲ���Ϊ��ֵ -1�����¹�����ֵ  1
				if (e.getWheelRotation() > 0) {   //��С����
					setRange("reduce");
				}
				if(e.getWheelRotation() < 0){  //��������
					setRange("increase");
				}
				revalidate();
				//System.out.println(((radarPanel) radarpanel).getRange());
				showRange.setText("RANGE :" + range + " KN ");
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
				perCircle.setText("PER CIRCLE: " + pc +" KN/PC");
				repaint(1000);
			}
		});
		// TODO Auto-generated constructor stub
		setBorder(null);  //��Щ���Կ������״�������иı�
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
		
		lineUp = new HoverJLable("HEADLINE-->ON");
		lineUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (headline) {
					lineUp.setText("HEADLINE-->OFF");
					headline = !headline;
				}
				else {
					lineUp.setText("HEADLINE-->ON");
					headline = !headline;
				}
			}
		});
		add(lineUp);
		
		rangeSwitch = new HoverJLable("RANGE-->ON");
		rangeSwitch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (rangeline) {
					rangeSwitch.setText("RANGE-->OFF");
					rangeline = !rangeline;
				}
				else {
					rangeSwitch.setText("RANGE-->ON");
					rangeline = !rangeline;
				}
			}
		});
		add(rangeSwitch);
		
		showMode = new HoverJLable("HEADUP");   //����дһ��JLable�����࣬ʵ����ͬ�Ķ��������ٴ�����
		showMode.addMouseListener(new MouseAdapter() {
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
		add(showMode);
		
		activeMode = new HoverJLable("REL");
		activeMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (relative) {
					activeMode.setText("ABS");
					relative = !relative;
				}
				else{
					activeMode.setText("REL");
					relative = !relative;
				}
			}
		});
		add(activeMode);
		//���½���ʾ��ǰ������Ϣ
		showRange = new HoverJLable("RANGE :" + range + " KN ");
		add(showRange);
		//���Ϸ���ʾ������Ϣ
		latitude = new HoverJLable("LAT : 123.345.34.");
		add(latitude);
		
		longitude = new HoverJLable("LOG : 434.243.32");
		add(longitude);
		
		course = new HoverJLable("COS : 156�� T");
		add(course);
		
		speed = new HoverJLable("SPD : 95 KT");
		add(speed);
		
		perCircle = new HoverJLable("PER CIRCLE : " + pc + " KN/PC");
		add(perCircle);
	}
	
	/***********************��ͨ�����Գ�����**********************************************/
	public void setRange(String option) {   //���̴���3�������ǳ˷���С��3����  0.75,1.5,3,6,12,24,48,96
		if (rangeline) {  //�������û�п������򷵻أ����������̱任
			if (option.equals("increase")) {  //��������
				range *= 2;
				if (range >= 96) {
					range = 96;
				}
			}
			else{   //reduce    ��������
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
	
	/*******************ͼ�λ滭��**************************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//*************��������Ȧ     ���㻭��Ĵ�С����*****************************
		
		//��������   �ⲿ�ֲ��仯
		g2.setColor(Color.GREEN);
		diameter = (float) (Math.min(getWidth(), getHeight())*0.93);
		startX = (getWidth() - diameter)/2;  //���Ͻ�λ��
		startY = (getHeight() - diameter)/2;
		g2.drawOval((int)startX-1, (int)startY-1, (int)diameter+2, (int)diameter+2);
		g2.setColor(Color.BLACK);
		g2.fillOval((int)startX, (int)startY, (int)diameter, (int)diameter);
		//**************�����������ϵĿ̶ȣ��ο�ָ����ʵ�ַ���***********************
		//ÿ�����Ϊ3��
		if (headup) {
			drawScale(g2);  //�������Ŵ�����̬ת��
		}
		//**********************���㻭����Ȧ,��������������*******************************
		//g2.fillOval((int)(startX+diameter/2-2), (int)(startY+diameter/2-2), 4, 4);  //�����ĵ�
		if (rangeline) {
			drawRange(g2);
		}
		if (headline) {
			drawHeadLine(g2);
		}
	}
	//�����״�����ϵ����� ���������Ŵ�������ı仯���仯            ���п̶ȣ������ʶ����
	public void drawScale(Graphics2D g2){
		g2.setColor(Color.GREEN);
		//ÿ�����Ϊ3��
		float xCircle = startX + diameter/2;  //����Բ��
		float yCircle = startY + diameter/2;
		for(int i = 0; i<36; i++){
			float semi = diameter/2+10;  //�뾶
			//���ֲ����ŵ����⣬�Ժ��ٸ�
			float degree = (float) Math.toRadians(i*10-90);
			int x = (int) (xCircle + semi * Math.cos(degree));
			int y = (int) (yCircle + semi * Math.sin(degree));
			int num = i * 10;
			g2.setColor(Color.CYAN);
			g2.setFont(new Font("Consolas", Font.PLAIN, (int) (diameter*0.025)));
			g2.drawString(Integer.toString(num) + "��", (int)(x - 0.01*diameter), (int)(y+0.005*diameter));
		}
		
		//���̶ȣ��������Ŵ���ת��ת��
		AffineTransform old = g2.getTransform();
        //�����60���̶�
        for (int i = 0; i < 360; i++) {   //x  yCircle����Բ��λ��
            int bulge = (int) (i % 10 == 0 ? 0.02*diameter : 0.005*diameter);  //bulge ͹��
            g2.fillRect((int)(xCircle-(diameter*0.0015)), (int)(startY), (int)(0.003*diameter), bulge);
            g2.rotate(Math.toRadians(1), xCircle, yCircle);  //ÿһС��ת6��, ��Բ��Ϊ���ĵ�
        }
        //������ת����
        g2.setTransform(old);
	}
	//�����������̱仯�Ļ���Ȧ��  �������Է���λ��
	public void drawRange(Graphics2D g2) {
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
	
	public void drawHeadLine(Graphics2D g2) {
		//�ڱ�����ģʽ����Ҫ��ȡ��������
		g2.setColor(Color.GREEN);
		g2.drawLine((int)(startX+diameter/2), (int)(startY+diameter/2), (int)(startX+diameter/2), (int)startY);
	}
}
