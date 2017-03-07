package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.text.Document;

public class InfoShow extends JTextArea {
	
	private static final long serialVersionUID = -6736968308209642335L;

	private String info = null;
	private String idName = null;
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
		Font font = new Font("Default", Font.PLAIN, 14);
		setFont(font);
		setForeground(Color.CYAN);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(Color.GREEN));
		info = ship.getName() + "-->" + ship.getType()+"\ndbshjcvshacjvgacvghxccsa\ncnsjbsckd";
		idName = ship.getName();
		
		this.setEditable(false);
		this.setMargin(new Insets(2, 2, 2, 2));
		this.setText(info);
	}
	
	public String getID(){
		return idName;
	}
}
