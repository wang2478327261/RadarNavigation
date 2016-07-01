package radarnavigation;

import javax.swing.JTextArea;
import java.awt.Color;

public class DataFormat extends JTextArea{
	
	String show;
	
	public DataFormat() {
		setLineWrap(true);
		setForeground(Color.WHITE);
		setEditable(false);
		setBackground(Color.DARK_GRAY);
	}
	
}
