/**
 * 
 */
package RadarNavigation;

/**
 * @author Administrator
 *
 */
public class Ship {

	private String name;
	private float Px;  //船舶纬度       在程序中是   x   值
	private float Py;  //船舶经度   在程序中是   y   值
	private float course;  //航向
	private float speed;  //速度
	private int type = 0;  //船舶类型
	
	public Ship(String name, float Px, float Py, float course, float speed, int type) {
		//super();
		this.name = name;
		this.Px = Px;
		this.Py = Py;
		this.course = course;
		this.speed = speed;
		this.type = type;
	}
	
	public Ship() {
		//super();
		this.name = "Default";
		this.Px = 0;
		this.Py = 0;
		this.course = 0;
		this.speed = 0;
		this.type = 0;
	}

	public float getParameter(int index){   //得到船舶计算相关数据
        switch(index){
            case 1 : return Px;
            case 2 : return Py;
            case 3 : return course;
            case 4 : return speed;
            default : return 0;//error
        }
    }
	
	public void setType(int type) { //这个方法多余，初始化后船舶的类型不可更改
		this.type = type;
	}
	public int getType(){
		return type;
	}
	public void setName(String name){  //船舶的名称在登陆时确定，只能有用户注销重新登录改名
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public synchronized void setValue(int index, float newValue){//change ship's parameter
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
        //这里也需要对速度进行校验
    }
	
	public void goAhead() {
		//使船舶前进一步，更新船舶数据信息
	}
}
