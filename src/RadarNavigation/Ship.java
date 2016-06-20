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
	private float Px;  //����γ��       �ڳ�������   x   ֵ
	private float Py;  //��������   �ڳ�������   y   ֵ
	private float course;  //����
	private float speed;  //�ٶ�
	private int type = 0;  //��������
	
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

	public float getParameter(int index){   //�õ����������������
        switch(index){
            case 1 : return Px;
            case 2 : return Py;
            case 3 : return course;
            case 4 : return speed;
            default : return 0;//error
        }
    }
	
	public void setType(int type) { //����������࣬��ʼ���󴬲������Ͳ��ɸ���
		this.type = type;
	}
	public int getType(){
		return type;
	}
	public void setName(String name){  //�����������ڵ�½ʱȷ����ֻ�����û�ע�����µ�¼����
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
        //����Ҳ��Ҫ���ٶȽ���У��
    }
	
	public void goAhead() {
		//ʹ����ǰ��һ�������´���������Ϣ
	}
}
