package radarnavigation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import common.Ship;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoPanel extends JPanel{   //点击信息显示面板
	
	//需要显示信息存储的数据
	List<Ship> ships = new LinkedList<Ship>();   //存储显示信息的船舶对象
	
	public InfoPanel() {
		super();
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
				setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				//滚动翻页
			}
		});
		// TODO Auto-generated constructor stub
		setBackground(Color.DARK_GRAY);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setLayout(null);
	}
	
	/*********绘图区域********************************************************/
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setFont(new Font(getName(), Font.PLAIN, (int) (Math.min(getWidth(), getHeight())*0.1)));
		//显示信息
		g2.setColor(Color.CYAN);
		g2.drawString("hello", 0, getFont().getSize()+10);
	}
	public void dataFormate(Graphics2D g2, String name, double Px, double Py,double course, double speed){
		
	}
	/********普通方法区域**************************************************/
	//向船舶获取列表上更新对象
	public void addShip(Ship ship) {   //要显示的船舶
		ships.add(ship);
	}
	public void removeShip(Ship ship) {  //也可以名称索引
		ships.remove(ship);
	}
}
