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
public class ShipManager extends JFrame{   //服务端需要添加船舶的功能，方便测试
	
	private JPanel contentPane;
	private List<Socket> sockets = new LinkedList<Socket>();  //连接的套接字对象
	private List<Ship> clientShips = new LinkedList<Ship>();   //所有客户端和服务端产生的船舶维护对象
	private List<Ship> serverShips = new LinkedList<Ship>();   //服务端生成的对象
	
	//功能需要     变量区域
	private boolean fullScreen = false;
	
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
		//打开网络通信，接受客户端消息
		//ServerThread server = new ServerThread();
		//初始化界面
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		setTitle("ShipManager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder());
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		clientShips.add(new Ship("dheu",123,23,23,14,"fr"));
	}

	@Override
	public void print(Graphics g) {
		// TODO Auto-generated method stub
		super.print(g);
		Graphics2D g2 = (Graphics2D)g.create();
		
		paintShips(g2);
	}
	
	public void paintShips(Graphics2D g2){
		
		g2.setColor(Color.BLACK);
        double Px, Py, course, speed;
        //
        for(Ship vessel : clientShips){
            Px = vessel.getParameter(1);
            Py = vessel.getParameter(2);
            course = Math.toRadians(vessel.getParameter(3));
            speed = vessel.getParameter(4);
            
            switch(vessel.getType()){
            //根据船舶类型的不同进行绘制
                /*case 0: normalShip(x, y, speed, c,g); break;
                case 1: sailingShip(x, y, speed, c,g); break;
                case 2: fishingShip(x, y, speed, c,g); break;
                case 3: outofControl(x, y, speed, c,g); break;
                case 4: limitbyControl(x, y, speed, c,g); break;
                case 5: limitbuDraft(x, y, speed, c,g); break;*/
            }
            normalShip(g2, Px, Py, course, speed);
            
        }
        
    }
	
	public void normalShip(Graphics2D g2, double Px, double Py, double course, double speed){
        int linestartx, linestarty, lineendx, lineendy;
        linestartx = (int)(Px + 20*Math.sin(course));
        linestarty = (int)(Py - 20*Math.cos(course));
        lineendx = (int) (linestartx + speed*Math.sin(course));
        lineendy = (int) (linestarty - speed*Math.cos(course));
        
        int[] trianglex = {linestartx, 
            (int)(Px + 7*Math.sin(course+Math.PI/2)),
            (int)(Px-10*Math.sin(course)+7*Math.sin(course+Math.PI/2)),
            (int)(Px-10*Math.sin(course)+7*Math.sin(course+3*Math.PI/2)),
            (int)(Px + 7*Math.sin(course+3*Math.PI/2))
        };
        int[] triangley = {linestarty, 
            (int)(Py - 7*Math.cos(course+Math.PI/2)), 
            (int)(Py+10*Math.cos(course)-7*Math.cos(course+Math.PI/2)),
            (int)(Py+10*Math.cos(course)-7*Math.cos(course+3*Math.PI/2)),
            (int)(Py - 7*Math.cos(course+3*Math.PI/2))
        };
        //drawbody and courseline
        g2.drawPolygon(trianglex, triangley, 5);
        g2.drawLine(linestartx, linestarty, lineendx, lineendy);
    }
}
