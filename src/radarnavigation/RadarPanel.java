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
public class RadarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	//��Щ������Ĭ��ֵ         ��Щ���״����Ķ�����ֵ��Ӧ�������״�����
	private float range = 6;  //�״�����̣� ��λ�Ǻ���    �״�����  ���12�����С1����, ��ʼ��Ϊ6����
	//�״�ģʽ     �״���ʾģʽ    ������  ��������    /////����˶�  �����˶�.......
	private boolean headline = true;  //�״ﴬ����     ������رմ�����
	private boolean rangeline = true;  //���̻�����  �ֶ�Σ������̱仯
	private boolean headup = true;   //������    ������ ģʽ
	private boolean relative = true;  //����˶�  �����˶�
	
	private float startX, startY, diameter;  //��ʾ�״��������Ͻ������Լ�     Բ�� --��ֱ�� 
	private Ship ship;  //���뱾��������
	
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
			public void componentResized(ComponentEvent e) {  //����������ԶԲ��ֽ���������ƣ�����
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
				//�ı��״�����,   ���Ϲ���Ϊ��ֵ -1�����¹�����ֵ  1
				if (e.getWheelRotation() > 0) {   //��С����
					setRange("reduce");
				}
				if(e.getWheelRotation() < 0){  //��������
					setRange("increase");
				}
				revalidate();
				//System.out.println(((radarPanel) radarpanel).getRange());
				repaint(1000);
			}
		});
		// TODO Auto-generated constructor stub
		setBorder(null);  //��Щ���Կ������״�������иı�
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
		
		showMode = new JLabel("HEADUP");   //����дһ��JLable�����࣬ʵ����ͬ�Ķ��������ٴ�����
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
