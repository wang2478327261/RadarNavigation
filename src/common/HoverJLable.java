/**
 * 
 */
package common;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class HoverJLable extends JLabel implements Serializable{
	
	public HoverJLable() {
		super();
		initComponents();
	}
	
	public HoverJLable(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
		initComponents();
	}

	public HoverJLable(Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
		initComponents();
	}

	public HoverJLable(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		// TODO Auto-generated constructor stub
		initComponents();
	}

	public HoverJLable(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
		initComponents();
	}

	public HoverJLable(String text) {
		super(text);
		// TODO Auto-generated constructor stub
		initComponents();
	}

	private void initComponents() {
		//setToolTipText(getText());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				//setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));   //Ӧ����borderfactory����һ��border����
				setBorder(new LineBorder(Color.GREEN, 1));    //���ù���һ����Ч��
			}
			@Override
			public void mouseExited(MouseEvent e) {
				//setBorder(BorderFactory.createEmptyBorder());
				setBorder(new EmptyBorder(null));
			}
		});
		//Font font = new Font("Consolas", Font.PLAIN, 20);
		//setFont(font);
		setForeground(Color.GREEN);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
