package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.text.Document;

import org.omg.CORBA.PUBLIC_MEMBER;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoShow extends JTextArea {
	
	private static final long serialVersionUID = -6736968308209642335L;
	
	private String info = null;
	public boolean isExist = true;  //标志这个组件是否存在，这样就可以通过顶层实现控制了
	public String name = null;  //删除时判断是不是这个
	
	public InfoShow() {
		super();
		addMouseAction();
	}

	public InfoShow(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		// TODO Auto-generated constructor stub
		addMouseAction();
	}

	public InfoShow(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
		addMouseAction();
	}

	public InfoShow(int rows, int columns) {
		super(rows, columns);
		// TODO Auto-generated constructor stub
		addMouseAction();
	}

	public InfoShow(String text, int rows, int columns) {
		super(text, rows, columns);
		// TODO Auto-generated constructor stub
		addMouseAction();
	}

	public InfoShow(String text) {
		super(text);
		// TODO Auto-generated constructor stub
		addMouseAction();
	}
	public InfoShow(Ship ship){
		super();
		addMouseAction();
		this.name = ship.getName();
		//setBorder(BorderFactory.createLineBorder(Color.GREEN));
		info = ship.getName() + "-->" + ship.getType()+"\ndbshjcvshacjvgacvghxccsa\ncnsjbsckd";
		
		this.setText(info);
	}
	//isExist设置成public后就不需要方法了
	/*public boolean isExist(){  //返回这个组件是否还存在
		return isExist;
	}*/
	
	public void addMouseAction(){
		Font font = new Font("Default", Font.PLAIN, 14);
		setFont(font);
		setTabSize(4);
		setForeground(Color.CYAN);
		setBackground(Color.DARK_GRAY);
		this.setEditable(false);
		//this.setMargin(new Insets(2, 2, 2, 2));
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.GREEN));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createEmptyBorder());
			}
			@Override  //这个先放一放，应该通过顶层删除，而不是自己删除自己
			public void mouseClicked(MouseEvent e) {  //这样竟然可以！！！以后在研究吧
				if (e.getButton() == MouseEvent.BUTTON3) {
					//getParent().remove(InfoShow.this);
					isExist = false;
					getParent().repaint();
				}
			}
		});
	}
}
