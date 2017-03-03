package common;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.text.Document;

public class InfoShow extends JTextArea {
	
	private static final long serialVersionUID = -6736968308209642335L;

	private String info = null;
	public InfoShow() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InfoShow(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		// TODO Auto-generated constructor stub
	}

	public InfoShow(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}

	public InfoShow(int rows, int columns) {
		super(rows, columns);
		// TODO Auto-generated constructor stub
	}

	public InfoShow(String text, int rows, int columns) {
		super(text, rows, columns);
		// TODO Auto-generated constructor stub
	}

	public InfoShow(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}
	public InfoShow(Ship ship){
		setForeground(Color.GREEN);
		setBackground(Color.DARK_GRAY);
		setSize(200, 100);
		setBorder(BorderFactory.createLineBorder(Color.GREEN));
		info = ship.getName() + "-->" + ship.getType();
		this.setEditable(false);
		this.setText(info);
	}
}