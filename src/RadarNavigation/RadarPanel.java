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
public class RadarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	//��Щ������Ĭ��ֵ         ��Щ���״����Ķ�����ֵ��Ӧ�������״�����
	private float range = 6;  //�״�����̣� ��λ�Ǻ���    �״�����  ���12�����С1����, ��ʼ��Ϊ6����
	private String mode = "HeadUp";  //�״�ģʽ     �״���ʾģʽ    ������  ��������    /////����˶�  �����˶�.......
	private boolean headLine = true;  //�״ﴬ����     ������رմ�����
	private boolean rangeLine = true;  //���̻�����  �ֶ�Σ������̱仯
	//ȥ�����ֱ�־�򻯴��룬���ֱ���жϱȽ�
	//private boolean layout = true;  //���ֵı仯�������߶ȵıȽ�ֵ�����������ֵ���в���
	
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
			public void mouseClicked(MouseEvent e) {   //������Լ���ʱ����ԣ�ʵ�ֹ���֮����������ϲ���ѵ
				//ѡ�жԷ����������ȡ��ѡ�У��Ҽ�������
				if(e.getButton() == MouseEvent.BUTTON1){   //��� 16���м� 8���Ҽ� 4    e.getModifiers() == 16
					//�����¼�
					if (e.getClickCount() >= 2) {
						if (!radarnavigation.isUndecorated()) {
							radarnavigation.setLocation(0, 0);
							radarnavigation.setSize(Toolkit.getDefaultToolkit().getScreenSize());
							//ȥ��������
							radarnavigation.dispose();
							radarnavigation.setUndecorated(true);
							//getRootPane().setWindowDecorationStyle(JRootPane.NONE);
							radarnavigation.setVisible(true);
						}
						else {
							radarnavigation.setBounds(20, 20, 1008, 735);
							//��λ,����ԭ���ĳߴ磬ֻ�ܵ���ʼ���ߴ磬����Ҫ�Ŵ�ǰ����Ҫ���ӱ����洢֮ǰ�ĳߴ缰λ��
							radarnavigation.dispose();
							radarnavigation.setUndecorated(false);
							radarnavigation.setVisible(true);
						}
						radarnavigation.revalidate();
					}
				}
				if(e.getButton() == MouseEvent.BUTTON3){
					//ʵ��ȡ��ѡ�й���
					radarnavigation.setTitle("RadarNavigation -->" + e.getX()  + "," + e.getY());
				}
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
	}
	/***********************��ͨ�����Գ�����**********************************************/
	public void setRange(String option) {   //���̴���3�������ǳ˷���С��3����  0.75,1.5,3,6,12,24,48,96
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
	public float getRange() {
		return range;
	}
	RadarNavigation radarnavigation;
	public void refer(RadarNavigation radarnavigation){  //��radarnavigation����
		this.radarnavigation = radarnavigation;
	}
	
	/*******************ͼ�λ滭��**************************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//*************��������Ȧ     ���㻭��Ĵ�С����*****************************
		float startX, startY, diameter;
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
		if (mode.equalsIgnoreCase("HeadUp")) {
			drawScale(g2, diameter, startX, startY);  //�������Ŵ�����̬ת��
		}
		//**********************���㻭����Ȧ,��������������*******************************
		//g2.fillOval((int)(startX+diameter/2-2), (int)(startY+diameter/2-2), 4, 4);  //�����ĵ�
		if (rangeLine) {
			drawRange(g2, diameter, startX, startY);
		}
		if (headLine) {
			drawHeadLine(g2, diameter, startX, startY);
		}
	}
	//�����״�����ϵ����� ���������Ŵ�������ı仯���仯            ���п̶ȣ������ʶ����
	public void drawScale(Graphics2D g2, float diameter, float startX, float startY){
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
	public void drawRange(Graphics2D g2, float diameter, float startX, float startY) {
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
	
	public void drawHeadLine(Graphics2D g2, float diameter, float startX, float startY) {
		//�ڱ�����ģʽ����Ҫ��ȡ��������
		g2.setColor(Color.GREEN);
		g2.drawLine((int)(startX+diameter/2), (int)(startY+diameter/2), (int)(startX+diameter/2), (int)startY);
	}
	
	
}
