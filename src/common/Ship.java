
package common;

/**
 * 船舶对象本身，可以改进，增加动态信息和静态信息分离
 * @author ERON
 */
public class Ship {
	
	private String name = "Default";
	private double Px = 0;
	private double Py = 0;
	private double course = 0;
	private double speed = 3;
	private String type = "Normal";
	
	public Ship(String name, double Px, double Py, double course, double speed, String type) {
		this.name = name;
		this.Px = Px;
		this.Py = Py;
		this.course = course;
		this.speed = speed;
		this.type = type;
	}
	
	public Ship() {
		this.name = "Default";
		this.Px = 0;
		this.Py = 0;
		this.course = 0;
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
	
	public void printShip() {
		System.out.println(getName());
		System.out.println(getParameter(1));
		System.out.println(getParameter(2));
		System.out.println(getParameter(3));
		System.out.println(getParameter(4));
		System.out.println(getType());
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
        if (speed < 0 || speed > 30) {
			System.err.println("your speed is : " + speed + "\nPlease get normal speed!");
			speed = 3;
		}
    }
	
	public void goAhead() {
        double stepx = speed*Math.sin(Math.toRadians(course));
        double stepy = speed*Math.cos(Math.toRadians(course));
        setValue(1, Px+stepx);
        setValue(2, Py-stepy);
	}
}
