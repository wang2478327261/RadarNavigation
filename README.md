##Radar Navigation, More Function, New Look(markdown还没有学，请见谅(●⌒◡⌒●))
###这是船舶避碰的第三个版本（JRE lib 1-7）
本次改进仍然只是程序上的改进，并没对避碰算法做深入研究，以后有时间再做
在前两个版本的基础上，这个版本加入雷达功能，采用用户/服务模式，用户只能看到站在此视角的方向上进行操纵
不存在边界，前两个一个是进行反射，一个是进行循环，这个将总体地图放入服务器端，有服务方进行总体控制，可以总揽全局


总体想法是这样，编程过程中再进行详细调整，争取两个月做出来，加油哦  
####markdown是得学学了(*￣︶￣)y

1. 2016年6月22日  凌晨 0:25 记：  
	由于在master分支上的实验未完成，所以有回退到原有的传统方法，在组件之间有交互的情况下，在总控组件中进行调度最为方便  
	而涉及到组建自身的属性可以在组件类中进行定义，交互的话还是放到上级的控制程序中比较好  
	此次将涉及到点击事件的任务放到JFrame中，方便讲ship对象传递到两个JPanel对象中，从而能够进行内部计算  
	如果将这些放入到各自的类中，然后进行引入上级对象进行简介调度，会比较麻烦  
	如果有大神设计出更好的办法，还希望能够指点，谢谢  
	至今没有摸透什么是面向对象，总是将两者会在一起（面向过程），程序架构的知识体系比较薄弱  
2. 2016.6.23记：  
	抱歉，github没有学会，分支弄乱了，以后会注意的。  
	开始修改infopanel面板的程序  
	同时开始设计通信过程，设计主控面板的功能及界面布局  
3. 2016.6.24： 通信方面没有进展，需要学习，通信同步没有思路  
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
6. 6.30记：出现新问题--》在传入船舶对象时出现空指针问题，因为对象未传入便开始刷新，这个问题怎么解决？  
7. 6.30 23:32 记事：    
	a. 将船舶对象传入的操作放到组件初始化中，可以解决空指针问题，顺利传入船舶对象，同时实现控制；    
	b. 通信过程采用变动通信的方法，即客户端船舶不操作则不进行通信，服务端进行同步步进，操作则传递操作识别符；    
	c. 今天解决了两个问题，休息一段时间，有其他的事情，以后再继续写，完善！!  

8.2016年7.2记：  今天在原有程序上解决了绘制文字过段时间消失的功能：  
    有多种实现方法：  
    1.用一个数字计算，运行一次 + 1，直到某大于某一数字就不显示  
    2.新建一个线程，暂停重置字符串的时间（本次用的该方法）  
    3.使用timer类，想试试，不知道行不行   
    采用新线程方法会有一个问题：当多次创建时，会启动多个线程，而线程之间没有关系，所以会出现前面的线程将文字显示提前置为0，清空，所以有的会瞬间消失    
9. StringBuffer 与 StringBuilder区别，线程安全与不安全  
启动线程，通过线程引用来调用方法，实现数据输出和引入  
10. 2016年7月7日    
	出现问题：在输入船舶信息时，如果信息不全应该采用填写模式，并发出警告  
	需要对通信指令进行设计  
11. 7.10 通信数据的类型选用，如何选择？？  
	该如何保证服务端与客户端同步问题，不传递对象，只传递变化信息，根据变化信息的识别命令来改变本地维护的状态表    
12. 
	





