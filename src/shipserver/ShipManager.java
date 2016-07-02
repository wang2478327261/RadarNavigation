package shipserver;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import common.ServerThread;
import common.Ship;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@SuppressWarnings("serial")
public class ShipManager extends JFrame{   //�������Ҫ��Ӵ����Ĺ��ܣ��������
	
	private SmallPanel smallpanel;
	
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
		initComponents();
		//������ͨ�ţ����ܿͻ�����Ϣ
		//ServerThread server = new ServerThread();
		//��ʼ������
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		setTitle("ShipManager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1008, 735);
		smallpanel = new SmallPanel();
		smallpanel.setBorder(BorderFactory.createEmptyBorder());
		smallpanel.setLayout(null);
		setContentPane(smallpanel);
	}
	
}
