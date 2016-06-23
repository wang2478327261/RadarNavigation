/**
 * 
 */
package common;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
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
		initComponents();
	}
	private void initComponents() {
		
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
		setHorizontalAlignment(SwingConstants.LEADING);
		setFont(new Font("Consolas", Font.BOLD, 18));
		setForeground(Color.GREEN);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createEmptyBorder());
	}
}
