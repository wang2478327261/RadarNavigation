/**
 * 
 */
package RadarNavigation;

import java.awt.Point;

import javax.tools.JavaFileManager.Location;

/**
 * @author Administrator
 *
 */
public class Ship {

	private String name;
	private Point location;
	private float course;
	private int speed;
	
	public Ship(String name, Point location, float course, int speed) {
		super();
		this.name = name;
		this.location = location;
		this.course = course;
		this.speed = speed;
	}
	
	
}
