package ShipServer;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class ShipManager extends JFrame {   //�������Ҫ��Ӵ����Ĺ��ܣ��������
	
	private JPanel contentPane;
	private SmallPanel smallpanel;
	private JButton btnNewButton;
	private JEditorPane editorPane;
	
	//������Ҫ     ��������
	private boolean changed = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShipManager frame = new ShipManager();
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
	public ShipManager() {
		//������ͨ�ţ����ܿͻ�����Ϣ
		
		//��ʼ������
		initComponents();
	}
	
	private void initComponents() {
		setTitle("ShipManager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		smallpanel = new SmallPanel();
		smallpanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_C) {
					smallpanel.setBounds(0, 0, 150, 150);
					changed = !changed;
				}
			}
		});
		smallpanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!changed) {
					smallpanel.setBounds(0, 0, getWidth()-8, getHeight()-35);
					revalidate();
					changed = !changed;
				}
				
			}
		});
		smallpanel.setBounds(0, 0, 150, 150);
		contentPane.add(smallpanel);
		
		btnNewButton = new JButton("New button");
		btnNewButton.setBounds(176, 35, 95, 25);
		contentPane.add(btnNewButton);
		
		editorPane = new JEditorPane();
		editorPane.setBounds(165, 85, 129, 115);
		contentPane.add(editorPane);
	}
}
