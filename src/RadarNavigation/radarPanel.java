package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class radarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	//��Щ������Ĭ��ֵ         ��Щ���״����Ķ�����ֵ��Ӧ�������״�����
	private int range = 6;  //�״�����̣� ��λ�Ǻ���    �״�����  ���24�����С3����, ��ʼ��Ϊ6����
	private int mode = 0;  //�״�ģʽ     �״���ʾģʽ    ������  ��������    /////����˶�  �����˶�
	private boolean headLine = true;  //�״ﴬ����     ������رմ�����
	private boolean rangeLine = true;  //���̻�����dfhggj
	
	public radarPanel() {
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
		//���Բ�ĺ����̣��������ʹ�� ���Ը��ģ�
		/*range = getWidth()>getHeight()?getHeight():getWidth();
		for(int i = 1; i < range; i++){  //������Ҫ�Ľ������������
			int r = i * 1;
			g2.drawOval(getWidth()-range, getHeight()-range, range, range);
		}*/
		
	}
	
	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		super.update(g);
	}
	
}
