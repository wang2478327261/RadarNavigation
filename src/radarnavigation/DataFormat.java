package radarnavigation;

import javax.swing.JTextArea;
import java.awt.Color;

public class DataFormat extends JTextArea{  //�������벼���ʱ���Ϣ��ʾ���ģ����ǿ��ǵ���ʹ����ṹ��ø��ӣ�������ʱ����ԭ��
	                                         //˼·�����������Ժ��ȵ��õĽ���취���� �ع�
	String show;
	
	public DataFormat() {
		setLineWrap(true);
		setForeground(Color.WHITE);
		setEditable(false);
		setBackground(Color.DARK_GRAY);
	}
	
}
