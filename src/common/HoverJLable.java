/**
 * 
 */
package common;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * @author ERON
 *
 */
public class HoverJLable extends JLabel implements Serializable{
	
	private static final long serialVersionUID = 3249102401404391293L;

	public HoverJLable() {
		super();
		initComponents();
	}
	
	public HoverJLable(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		initComponents();
	}

	public HoverJLable(Icon image) {
		super(image);
		initComponents();
	}

	public HoverJLable(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		initComponents();
	}

	public HoverJLable(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		initComponents();
	}

	public HoverJLable(String text) {
		super(text);
		initComponents();
	}

	private void initComponents() {
		//setToolTipText(getText());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				//setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
				setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				//setBorder(BorderFactory.createEmptyBorder());
				setBorder(BorderFactory.createEmptyBorder());
			}
		});
		//Font font = new Font("Consolas", Font.PLAIN, 20);
		//setFont(font);
		setForeground(Color.GREEN);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
