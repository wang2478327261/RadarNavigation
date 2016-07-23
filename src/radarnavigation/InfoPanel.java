package radarnavigation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import common.Ship;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel{   //点击信息显示面板
	
	//需要显示信息存储的数据
	List<Ship> ships = new LinkedList<Ship>();   //存储显示信息的船舶对象
	private int scroll_Y = 0;     //滚轮滚动     高度的变化值
	
	public InfoPanel() {
		super();
		
		initComponents();
	}
	
	private void initComponents() {
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {    //以后可以改进滚动控制
				//滚动翻页
				if (e.getWheelRotation() > 0) {   //鼠标滚轮向下滚动       让字    向上走
					scroll_Y -= 40;
				}
				else if(e.getWheelRotation() < 0){
					scroll_Y += 40;
				}
				repaint();
			}
		});
		// TODO Auto-generated constructor stub
		setBackground(Color.DARK_GRAY);
		//setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setLayout(null);    //测试用例
		ships.add(new Ship("huawei",123,23,34,13,"normal"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
		ships.add(new Ship("youyuuou", 156, 34, 15,17, "limit"));
	}
	
	/*********绘图区域********************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setFont(new Font("Default", Font.PLAIN, (int) (Math.min(getWidth(), getHeight())*0.07)));
		g2.setColor(Color.CYAN);
		//******************************************************************************
		//显示增加的船舶信息
		int h = g2.getFont().getSize();   //字体高度
		int locate = 1;     //画出字符的行数
		//scrollStart = (int) (locate*h*1.3) + scroll_Y;
		for (Ship vessel : ships) {
			g2.drawString(vessel.getName()+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(1)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(2)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(3)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getParameter(3)+"\n", 2, (int) (locate*h*1.3)+scroll_Y); locate++;
			g2.drawString(vessel.getType(), 2, (int) (locate*h*1.3)+scroll_Y);      locate++;
			g2.setColor(Color.ORANGE);
			g2.drawLine(2, (int) (locate*h*1.3)+scroll_Y, getWidth()-2, (int) (locate*h*1.3)+scroll_Y);
			locate++;
			g2.setColor(Color.CYAN);
		}
	}
	
	/********普通方法区域**************************************************/
	//向船舶获取列表上更新对象
	public void addShip(Ship ship) {   //要显示的船舶
		ships.add(ship);
		//更新界面
		repaint();
	}
	public void removeShip(Ship ship) {  //也可以名称索引
		ships.remove(ship);
		//需要更新界面
		repaint();
	}
	
	public void changeMode(){
		
	}
}
