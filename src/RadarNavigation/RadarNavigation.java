package RadarNavigation;

import java.awt.EventQueue;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.ErrorManager;

public class RadarNavigation extends JFrame {

	private String name;   //´¬²°Ãû³Æ
	
	private JPanel contentPane;
	private JPanel radarPanel;
	private JPanel infoPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RadarNavigation frame = new RadarNavigation();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RadarNavigation() {
		name = JOptionPane.showInputDialog(this, "Please input Ship name : ").trim();
		while(name == null){
			JOptionPane.showMessageDialog(this, "you should input ship name !", "ERROR", JOptionPane.ERROR_MESSAGE);
			name = JOptionPane.showInputDialog(this, "Please input Ship name : ").trim();
		}
		
		initComponents();
	}
	private void initComponents() {
		setTitle("RadarNavigation");
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 908, 635);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(new Color(0, 128, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radarPanel = new JPanel();
		radarPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				radarPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				radarPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		});
		radarPanel.setBorder(null);
		radarPanel.setBackground(Color.DARK_GRAY);
		radarPanel.setBounds(0, 0, 700, 600);
		radarPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		radarPanel.setLayout(null);
		contentPane.add(radarPanel);
		
		infoPanel = new JPanel();
		infoPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				infoPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				infoPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		});
		infoPanel.setBackground(Color.DARK_GRAY);
		infoPanel.setBounds(radarPanel.getWidth(), 0, 200, 600);
		infoPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		infoPanel.setLayout(null);
		contentPane.add(infoPanel);
	}
}
