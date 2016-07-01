package radarnavigation;

import javax.swing.JTextArea;
import java.awt.Color;

public class DataFormat extends JTextArea{  //本来是想布局邮编信息显示面板的，但是考虑到会使总体结构变得复杂，所以暂时按照原来
	                                         //思路进行制作，以后先到好的解决办法再行 重构
	String show;
	
	public DataFormat() {
		setLineWrap(true);
		setForeground(Color.WHITE);
		setEditable(false);
		setBackground(Color.DARK_GRAY);
	}
	
}
