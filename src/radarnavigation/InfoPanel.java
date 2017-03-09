package radarnavigation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import common.InfoShow;
import common.Ship;

public class InfoPanel extends JPanel{
	
	private static final long serialVersionUID = -1344586063850816104L;
	
	public static List<Ship> innerShips = new LinkedList<Ship>();  //当前面板中的显示对象队列
	private static List<InfoShow> infoPanes = new LinkedList<InfoShow>();  //对应的组件列表，方便操作
	
	public InfoPanel() {
		super();
		System.out.println("InfoPanel -> infopanel");
		//面板添加信息会随着添加数量而变化，想想制作的办法
		/*innerShips.add(new Ship());
		innerShips.add(new Ship());
		innerShips.add(new Ship());
		innerShips.add(new Ship());
		innerShips.add(new Ship());
		innerShips.add(new Ship());
		innerShips.add(new Ship());
		innerShips.add(new Ship());
		for(int i=0;i<innerShips.size();i++){  //这个以后就不需要了
			infoPanes.add(new InfoShow(innerShips.get(i)));
		}*/
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  //这个有点问题，但是可以运行
		setBorder(BorderFactory.createLineBorder(Color.RED));  //测试
		setBackground(Color.PINK);
		
		/*for(int i=0;i<infoPanes.size();i++){  //这样添加组件是不是好一些？是否需要增加索引？
			this.add(infoPanes.get(i));
		}*/
		//repaint();
	}
	
	/******************刷新数据和面板*******************************************/
	//以前是绘制出选择的信息，现在直接组件排版
	/*public void refresh(){  //根据数据，更新面板
		//怎么实现面板的动态更新
		Iterator<InfoShow> items = infoPanes.iterator();
		while(items.hasNext()){
			InfoShow temp = items.next();
			if (!temp.isExist) {
				this.remove(temp);  //面板移除组件
				items.remove();
			}
		}
		updateUI();
	}*/
	
	@Override
	public void paint(Graphics g) {  //以前是绘制出选择的信息，现在直接组件排版    根据数据，更新面板
		// TODO Auto-generated method stub
		super.paint(g);
		//怎么实现面板的动态更新
		Iterator<InfoShow> items = infoPanes.iterator();
		while(items.hasNext()){
			InfoShow temp = items.next();
			if (!temp.isExist) {
				for(int i=0;i<innerShips.size();i++){  //当直接在组建上删除时，船舶数据没有更新，所以在这里更新
					Ship del=innerShips.get(i);
					if (temp.name.equals(del.getName())) {
						innerShips.remove(i);
						break;
					}
				}
				System.out.println("信息面板船舶数量del-->"+innerShips.size());
				this.remove(temp);  //面板移除组件
				items.remove();  //infoshow链表移除
			}
		}
		updateUI();
	}
	
	/******************控制信息传递的方法群,更新数据**********************************/
	public void addShip(Ship ship) {  //这里的两个链表，ships和infos应该异步更新--考虑代理的做法
		System.out.println("InfoPanel -> addShip");
		innerShips.add(ship);
		infoPanes.add(new InfoShow(ship));
		add(infoPanes.get(infoPanes.size()-1));  //add to panel
		System.out.println("信息面板船舶数量add-->"+innerShips.size());
	}
	public void removeShip(Ship ship) {  //这个是在雷达界面点击的时候可以去除侧边栏对应的...
		System.out.println("InfoPanel -> removeShip");
		/*Iterator<Ship> inner = innerShips.iterator();  //出现多次移除的原因是这里，根据自我删除和外界删除，重复了
		while(inner.hasNext()){
			Ship it = inner.next();
			if (it.getName().equals(ship.getName())) {
				inner.remove();
				break;
			}
		}*/
		Iterator<InfoShow> info=infoPanes.iterator();  //在repaint里移除船舶对象
		while(info.hasNext()){
			InfoShow it=info.next();
			if (it.name.equals(ship.getName())) {
				it.isExist=false;
				break;
			}
		}
		//innerShips.remove(ship);  //这样也可以
		//?可以直接删除对象吗？应该不可以，没有唯一识别符-->yes，内部原理是遍历吗？
		/*for(int i=0;i<infoPanes.size();i++){
			if ( infoPanes.get(i).name.equals(ship.getName()) ) {  //变换属性，不移除，在refresh里移除
				infoPanes.get(i).isExist=false;
				break;
			}
		}*/
		repaint();
	}
	
	public void changeMode(){
		//改变模式，以前想加的功能，碍于现在的进度，暂时不识闲，以后或者下一个版本考虑这种界面整体变化的情况
	}
}
