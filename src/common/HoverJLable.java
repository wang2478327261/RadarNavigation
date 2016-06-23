/**
 * 
 */
package common;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Administrator
 *
 */
public class HoverJLable extends JLabel{
	
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
		//setFont(new Font("Condolas", Font.BOLD, getHeight()));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder());
			}
		});
		setHorizontalAlignment(SwingConstants.LEFT);
		setFont(new Font("Consolas", Font.BOLD, 18));
		setForeground(Color.GREEN);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createEmptyBorder());
	}
}