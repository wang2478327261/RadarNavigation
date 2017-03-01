package common;

import javax.swing.Icon;
import javax.swing.JLabel;

public class InfoLabel extends JLabel {
	//右侧显示信息面板的组件
	private static final long serialVersionUID = -6442142361149266834L;

	public InfoLabel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InfoLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public InfoLabel(Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	public InfoLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public InfoLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}
	
	public InfoLabel(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		super.setText(text);
	}
	
	
}
