package radarnavigation;

import java.awt.Color;
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
	private static List<InfoShow> infos = new LinkedList<InfoShow>();  //对应的组件列表，方便操作
	
	public InfoPanel() {
		super();
		System.out.println("InfoPanel -> infopanel");
		//面板添加信息会随着添加数量而变化，想想制作的办法
		/*ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());
		ships.add(new Ship());*/
		for(int i=0;i<ships.size();i++){  //这个以后就不需要了
			infos.add(new InfoShow(ships.get(i)));
		}
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  //这个有点问题，但是可以运行
		setBorder(BorderFactory.createLineBorder(Color.RED));  //测试
		setBackground(Color.DARK_GRAY);
		
		for(int i=0;i<infos.size();i++){  //这样添加组件是不是好一些？是否需要增加索引？
			this.add(infos.get(i));
		}
	}
	
	/******************绘制面板的重写方法*******************************************/
	//以前是绘制出选择的信息，现在直接组件排版
	
	/******************控制信息传递的方法群**********************************/
	public void addShip(Ship ship) {  //这里的两个链表，ships和infos应该异步更新--考虑代理的做法
		ships.add(ship);
		System.out.println("InfoPanel -> addShip");
		infos.add(new InfoShow(ship));
		repaint();
	}
	public void removeShip(Ship ship) {
		for(int i=0;i<ships.size();i++){
			if (ships.get(i).getName() == ship.getName()) {
				ships.remove(i);
				break;
			}
		}
		//ships.remove(ship);  //?可以直接删除对象吗？应该不可以，没有唯一识别符
		System.out.println("InfoPanel -> removeShip");
		for(int i=0;i<infos.size();i++){
			if (infos.get(i).getID() == ship.getName()) {
				infos.remove(i);
				break;
			}
		}
		repaint();
	}
	
	public void changeMode(){
		//改变模式，以前想加的功能，碍于现在的进度，暂时不识闲，以后或者下一个版本考虑这种界面整体变化的情况
	}
}
