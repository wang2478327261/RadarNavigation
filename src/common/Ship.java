
package common;

import java.io.Serializable;

/**
 * 船舶对象本身，可以改进，增加动态信息和静态信息分离
 * @author ERON
 */
public class Ship implements Serializable{  //下一个版本中需要加入船舶的静态属性和动态属性，动态属性重新写一个类，增加灵活度
	public static int i=0;  //保证每次创建的船名不同
	private static final long serialVersionUID = -3576425321740625846L;
	
	private String name = null;
	private double Px = 0;
	private double Py = 0;
	private double course = 0;
	private double speed = 3;
	private String type = null;
	
	public Ship(String name, double Px, double Py, double course, double speed, String type) {
		this.name = name;
		this.Px = Px;
		this.Py = Py;
		this.course = course;
		this.speed = speed;
		if (this.speed < 0 || this.speed > 20) {
			System.err.println("Ship->setValue: your speed is : " + speed + "\ninitial normal speed : 5");
			this.speed = 5;
		}
		this.type = type;
	}
	
	public Ship() {
		this.name = "Default"+(i++);
		this.Px = 0;
		this.Py = 0;
		this.course = 120;
		this.speed = 3;
		this.type = "Normal";
	}
	/**
	 * @param index
	 * @return ship's param
	 */
	public double getParameter(int index){
        switch(index){
            case 1 : return Px;
            case 2 : return Py;
            case 3 : return course;
            case 4 : return speed;
            default : return 0;//error
        }
    }
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType(){
		return type;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public synchronized void setValue(int index, double newValue){
        switch(index){
            case 1: Px = newValue; break;
            case 2: Py = newValue; break;
            case 3: course = newValue; break;
            case 4: speed = newValue; break;
            default : Px = 0; Py = 0; course = 0; speed = 0; break;
        }
        
        while (this.course<0||this.course>=360) {
            if(this.course<0) this.course+=360;
            if(this.course>=360) this.course-=360;
        }
        if (speed < 0 || speed > 20) {
			System.err.println("Ship->setValue: your speed is : " + speed + "\nPlease get normal speed!");
			speed = 5;
		}
    }
	
	public void goAhead() {
        double stepx = speed*Math.sin(Math.toRadians(course));
        double stepy = speed*Math.cos(Math.toRadians(course));
        setValue(1, Px+stepx);
        setValue(2, Py-stepy);
	}
	
	@Override
	public String toString() {
		return "\nname-->"+getName()+"\npx-->"+getParameter(1)+"\npy-->"+getParameter(2)
			+"\ncourse-->"+getParameter(3)+"\nspeed-->"+getParameter(4)
			+"\ntype-->"+getType();
	}
}
