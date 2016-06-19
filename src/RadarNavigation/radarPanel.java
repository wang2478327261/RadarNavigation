package RadarNavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class radarPanel extends JPanel{   //�״�������ʾ��������Ϣ
	
	private int range;  //�״�����̣� ��λ�Ǻ���
	private int mode;  //�״�ģʽ
	private boolean headLine;  //�״ﴬ����
	
	public radarPanel(int range, int mode, boolean headLine) {
		super();
		// TODO Auto-generated constructor stub
		this.range = range;
		this.mode = mode;
		this.headLine = headLine;
		
		setBorder(null);  //��Щ���Կ������״�������иı�
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setLayout(null);
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
