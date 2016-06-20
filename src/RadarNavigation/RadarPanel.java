package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class RadarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	//��Щ������Ĭ��ֵ         ��Щ���״����Ķ�����ֵ��Ӧ�������״�����
	private int range = 6;  //�״�����̣� ��λ�Ǻ���    �״�����  ���24�����С3����, ��ʼ��Ϊ6����
	private int mode = 0;  //�״�ģʽ     �״���ʾģʽ    ������  ��������    /////����˶�  �����˶�
	private boolean headLine = true;  //�״ﴬ����     ������رմ�����
	private boolean rangeLine = true;  //���̻�����  �ֶ�Σ������̱仯
	
	public RadarPanel() {
		super();
		// TODO Auto-generated constructor stub
		setBorder(null);  //��Щ���Կ������״�������иı�
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
	}
	
	public void setRange(String option) {
		if (option.equals("add")) {  //��������
			if (range == 1) {
				range = 3;
			}
			else{
				range *= 2;
				if (range > 24) {
					range = 24;
				}
			}
		}
		else{   //reduce    ��������
			range /= 2;
			if (range < 1) {
				range = 1;
			}
		}
	}
	public int getRange() {
		return range;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.GREEN);
		//��������Ȧ     ���㻭��Ĵ�С����
		int startX, startY, diameter;
		if (getWidth()> getHeight()) {     //Ϊʲô���ﲻ������ģ����ֱ���жϸ�ֵ     a >b ? a: b;
			diameter = getHeight() - 50; //ֱ��
		}
		else {
			diameter = getWidth() - 50;
		}
		startX = (getWidth() - diameter)/2;  //���Ͻ�λ��
		startY = (getHeight() - diameter)/2;
		g2.drawOval(startX-1, startY-1, diameter+2, diameter+2);
		g2.setColor(Color.BLACK);
		g2.fillOval(startX, startY, diameter, diameter);
		//�����������ϵĿ̶ȣ��ο�ָ����ʵ�ַ���
		
		//���㻭����Ȧ,��������������
		for(int i = 0; i < range; i++){  //������Ҫ�Ľ������������
			int diaStep;
			if (range <= 3) {
				diaStep = diameter/range;
				g2.drawOval(startX-diaStep, startY - diaStep, diaStep, diaStep);
				diaStep *= 2;
			}
			else {
				
			}
		}
		
	}
	
	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		super.update(g);
	}
	
}
