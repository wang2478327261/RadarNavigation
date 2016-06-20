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

public class RadarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	//��Щ������Ĭ��ֵ         ��Щ���״����Ķ�����ֵ��Ӧ�������״�����
	private float range = 6;  //�״�����̣� ��λ�Ǻ���    �״�����  ���12�����С1����, ��ʼ��Ϊ6����
	private String mode = "HeadUp";  //�״�ģʽ     �״���ʾģʽ    ������  ��������    /////����˶�  �����˶�.......
	private boolean headLine = true;  //�״ﴬ����     ������رմ�����
	private boolean rangeLine = true;  //���̻�����  �ֶ�Σ������̱仯
	
	private boolean layout = true;  //���ֵı仯�������߶ȵıȽ�ֵ�����������ֵ���в���
	
	public RadarPanel() {
		super();
		initComponents();
	}
	private void initComponents() {
		// TODO Auto-generated constructor stub
		setBorder(null);  //��Щ���Կ������״�������иı�
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
	}
	
	public void setRange(String option) {   //���̴���3�������ǳ˷���С��3����  0.75,1.5,3,6,12,24,48,96
		if (option.equals("add")) {  //��������
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
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.GREEN);
		/***************��������Ȧ     ���㻭��Ĵ�С����*****************************/
		float startX, startY, diameter;
		if (getWidth()> getHeight()) {     //Ϊʲô���ﲻ������ģ����ֱ���жϸ�ֵ     a >b ? a: b;
			diameter = getHeight() - 50; //ֱ��
			layout = true;
		}
		else {
			diameter = getWidth() - 50;
			layout = false;
		}
		startX = (getWidth() - diameter)/2;  //���Ͻ�λ��
		startY = (getHeight() - diameter)/2;
		g2.drawOval((int)startX-1, (int)startY-1, (int)diameter+2, (int)diameter+2);
		g2.setColor(Color.BLACK);
		g2.fillOval((int)startX, (int)startY, (int)diameter, (int)diameter);
		/**************************�����������ϵĿ̶ȣ��ο�ָ����ʵ�ַ���***********************/
		g2.setColor(Color.GREEN);
		
		
		/**************************���㻭����Ȧ,��������������*******************************/
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
	
	
}
