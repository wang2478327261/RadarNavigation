package radarnavigation;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import common.InfoShow;
import common.Ship;

public class InfoPanel extends JPanel{
	
	private static final long serialVersionUID = -1344586063850816104L;
	
	public static List<Ship> ships = new LinkedList<Ship>();  //当前面板中的显示对象队列
	private static List<InfoShow> infos = new LinkedList<>();  //对应的组件列表，方便操作
	
	public InfoPanel() {
		super();
		System.out.println("InfoPanel -> infopanel");
		//面板添加信息会随着添加数量而变化，想想制作的办法
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		/*ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());*/
		
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  //这个有点问题，但是可以运行
		setBorder(BorderFactory.createLineBorder(Color.RED));  //测试
		setBackground(Color.DARK_GRAY);
		for(int i=0;i<ships.size();i++){
			this.add(new InfoShow(ships.get(i)));
		}
	}
	
	/******************绘制面板的重写方法*******************************************/
	//以前是绘制出选择的信息，现在直接组件排版
	
	/******************控制信息传递的方法群**********************************/
	public static void addShip(Ship ship) {
		ships.add(ship);
		System.out.println("InfoPanel -> addShip");
		//repaint();
	}
	public static void removeShip(Ship ship) {
		ships.remove(ship);
		System.out.println("InfoPanel -> removeShip");
		//repaint();
	}
	
	public void changeMode(){
		
	}
}
