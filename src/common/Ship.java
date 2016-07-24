
package common;

/**
 * �������������࣬���ƴ������Ա仯   ��ʼ�������ɴ�������
 *@author ERON
 *@param name--����, Px--γ��,Py--����, course--��������, speed--�ٶ�, type--����
 */
public class Ship {
	
	private String name = "Default";
	private double Px = 0;  //����γ��       �ڳ�������   x   ֵ
	private double Py = 0;  //��������   �ڳ�������   y   ֵ
	private double course = 0;  //����
	private double speed = 3;  //�ٶ�
	private String type = "Normal";  //��������
	
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
	 * <p>ͨ�������õ�����������1--����λ�ã� 2--����λ�ã� 3--���� 4--�ٶ�</p>
	 * @param index
	 * @return Px(����λ��), Py(����λ��), course(����), speed(�ٶ�)
	 */
	public double getParameter(int index){   //�õ����������������
        switch(index){
            case 1 : return Px;
            case 2 : return Py;
            case 3 : return course;
            case 4 : return speed;
            default : return 0;//error
        }
    }
	
	public void setType(String type) { //����������࣬��ʼ���󴬲������Ͳ��ɸ���
		this.type = type;
	}
	public String getType(){
		return type;
	}
	public void setName(String name){  //�����������ڵ�½ʱȷ����ֻ�����û�ע�����µ�¼����
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	/**
	 * synchronizedͬ������   �Դ����������¸�ֵ
	 * @param index    1--��Px�� 2--��Py�� 3--������ 4--���ٶ�
	 * @param newValue   ����Ӧ�����Ը�ֵΪ  newValue
	 */
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
        //����Ҳ��Ҫ���ٶȽ���У��   �ٶȲ��ܴ���30��kt
        if (speed < 0 || speed > 30) {
			System.err.println("your speed is : " + speed + "\nPlease get normal speed!");
			speed = 3;
		}
    }
	
	public void goAhead() {
		//ʹ����ǰ��һ�������´���������Ϣ
        double stepx = speed*Math.sin(Math.toRadians(course));
        double stepy = speed*Math.cos(Math.toRadians(course));
        setValue(1, Px+stepx);
        setValue(2, Py-stepy);
	}
}
