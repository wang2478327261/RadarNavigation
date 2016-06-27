##Radar Navigation, More Function, New Look(markdown还没有学，请见谅(●⌒◡⌒●))
###这是船舶避碰的第三个版本（JRE lib 1-7）
本次改进仍然只是程序上的改进，并没对避碰算法做深入研究，以后有时间再做
在前两个版本的基础上，这个版本加入雷达功能，采用用户/服务模式，用户只能看到站在此视角的方向上进行操纵
不存在边界，前两个一个是进行反射，一个是进行循环，这个将总体地图放入服务器端，有服务方进行总体控制，可以总揽全局


总体想法是这样，编程过程中再进行详细调整，争取两个月做出来，加油哦  
####markdown是得学学了(*￣︶￣)y

1、2016年6月22日  凌晨 0:25 记：  
	由于在master分支上的实验未完成，所以有回退到原有的传统方法，在组件之间有交互的情况下，在总控组件中进行调度最为方便  
	而涉及到组建自身的属性可以在组件类中进行定义，交互的话还是放到上级的控制程序中比较好  
	此次将涉及到点击事件的任务放到JFrame中，方便讲ship对象传递到两个JPanel对象中，从而能够进行内部计算  
	如果将这些放入到各自的类中，然后进行引入上级对象进行简介调度，会比较麻烦  
	如果有大神设计出更好的办法，还希望能够指点，谢谢  
	至今没有摸透什么是面向对象，总是将两者会在一起（面向过程），程序架构的知识体系比较薄弱  
2、2016.6.23记：  
	抱歉，github没有学会，分支弄乱了，以后会注意的。  
	开始修改infopanel面板的程序  
	同时开始设计通信过程，设计主控面板的功能及界面布局  
3、2016.6.24： 通信方面没有进展，需要学习，通信同步没有思路  
没有办法，暂停一段时间，以后有时间在慢慢完善  
4. 2016.6.26 mark:  可以作为雷达对象模糊显示的参考代码
```java
	public class Snow extends JFrame{//图片路径问题待解决

	Random rd = new Random();
	MyPanel panel = null;
	int[] x = new int[600];
	int[] y = new int[600];
	
	public static void main(String[] args){
		Snow nn = new Snow();
	}
	
	Snow(){
		slevin();
		this.setTitle("雪");
		this.setSize(800,800);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	void slevin(){
		
		
		for (int i = 0; i < 600; i++) {
			x[i] = rd.nextInt(800);
			y[i] = rd.nextInt(800);
		}
		
		MyThread thread = new MyThread();
		Thread t = new Thread(thread);
		t.start();
		
		panel = new MyPanel();
		
		this.add(panel);
	}
	
	class MyPanel extends JPanel{
		@Override
		protected void paintComponent(Graphics arg0){
			// TODO Auto-generated method stub
			super.paintComponent(arg0);
			ImageIcon icon = new ImageIcon("E:\\Picture Book\\San Francisco.jpg");//图片的问题，待解决
			arg0.drawImage(icon.getImage(), 0, 0, panel.getSize().width,panel.getSize().height,panel);
			
			for (int i = 0; i < 600; i++) {
				y[i] += 1;
				x[i] += 1;
				
				if(y[i] > 800){
					y[i] -=800;
				}
				if(x[i] > 800){
					x[i] -=800;
				}
				arg0.setColor(Color.WHITE);
				arg0.fillOval(x[i], y[i], rd.nextInt(4) + 1, rd.nextInt(4) + 1);
			}
		}
	}
	
	class MyThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				try {
					Thread.sleep(50);
					repaint();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

}
```
	出现了字体大小在全屏切换后出现字体变化，无法解决，请高人指点，谢谢  

5. 6.27记： 字体大小动态变化问题解决  
6. 
