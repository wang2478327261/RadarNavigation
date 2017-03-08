##Radar Navigation, More Function, New Look  
######(markdown还没有学，请见谅(●⌒◡⌒●))
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
6. 6.30记：  
	出现新问题--》在传入船舶对象时出现空指针问题，因为对象未传入便开始刷新，这个问题怎么解决？  
7. 6.30 23:32 记事：    

	a. 将船舶对象传入的操作放到组件初始化中，可以解决空指针问题，顺利传入船舶对象，同时实现控制；    
	b. 通信过程采用变动通信的方法，即客户端船舶不操作则不进行通信，服务端进行同步步进，操作则传递操作识别符；    
	c. 今天解决了两个问题，休息一段时间，有其他的事情，以后再继续写，完善！!  

8.2016年7.2记：  今天在原有程序上解决了绘制文字过段时间消失的功能：  
    有多种实现方法:  
    
    1. 用一个数字计算，运行一次 + 1，直到某大于某一数字就不显示   
    2. 新建一个线程，暂停重置字符串的时间（本次用的该方法）  
    3. 使用timer类，想试试，不知道行不行   
    4. 采用新线程方法会有一个问题：当多次创建时，会启动多个线程，而线程之间没有关系，所以会出现前面的线程将文字显示提前置为0，清空，所以有的会瞬间消失    
    
9. StringBuffer 与 StringBuilder区别，线程安全与不安全   
        启动线程，通过线程引用来调用方法，实现数据输出和引入   
10. 2016年7月7日    
	出现问题：在输入船舶信息时，如果信息不全应该采用填写模式，并发出警告  
	需要对通信指令进行设计  
11. 7.10 通信数据的类型选用，如何选择？？  
	该如何保证服务端与客户端同步问题，不传递对象，只传递变化信息，根据变化信息的识别命令来改变本地维护的状态表    
12. 在infopanel的设计中忽略了界面的重新布局问题，函数应该这么写：  

```java
	public void paint(Graphics g){
		if(mode1){
		  //模式一界面的更新
		}
		if(mode2){
		  //模式2的界面更新
		}
	}
	//实现具体功能的子函数
	public void method(graphics g){
		//具体方法
	}
	......
```  

同步问题，总体布局问题需要解决  , 如果将发送的的信息变为二进制码，1、0序列，对其分析，是否会更快一些？？    

13. 2016.7.24 --->通信协议的设计：  
	对于本船的变化只发送，不接收。。。他船信息只接收更新本地数据，不发送  
	在这里做一个简单处理，当接收到本船信息---》不做动作，免去服务端的检索  
  一、客户端  
	
	发送数据:  	
	1. 速度、方向变化 ---》 船名 + 属性（speed/course） + 变化量；  
	2. 登陆/登出 ---》  船名 + login/logout； 
	3. 同步，向前走一步  ---》 船名 + go;  
	
	接收数据：  
	1、哪艘船   属性变化   
	2、哪艘船登陆 或者 登出    
	3、向前一步走  

	speed course login logout go  
   二、服务端  
   
	接收数据：   
	1. 速度、方向变化---》本地数据更新  --》向所有客户端发送更改信息信息    
	2. 登陆/登出 ---》 结束套接字  ---》向所有客户端发送登出信息；   
	3. 同步 ---》更新本地数据 ---》发送信息到所有客户端；    
	
	发送数据:  
	1. 服务端新创建的对象 --》发送到每个客户端中  
	2. 各个客户端更改--》发送改变信息  
	3. 登陆/登出--》发送更新数据  
		
14、  7.25  18:36记：  
	同步出现问题，应该多参考示例代码，server/cliemt工程，试着完成对应功能  
	服务端与客户端能够进行同步数据，值得参考  
15.  2016.8.24:  
    1. 最近休息了一段时间，接近开学，争取完成的差不多  
    2. 关于客户端/服务端的体会：现在的雏形类似于云编辑的模式，不存在什么客户服务的模式，是双向同步的，只不过在服务端看到的是全局  
    3. 学习套接字编程，javascript和javaFx也应该涉猎  
16.  2016.8.24 晚上23:31记：  
	在服务端代码有问题，对于每一个客户端，新建一个线程，但是从map索引需要键，不能直接得到，所以现阶段的解决办法是将类型改为链表，用链表存储套接字，通过索引最后一个套接字，得到引用，方便创建新线程    
	如果要进一步改进性能，需要更好的方法，求救啊......
	完成通信协议后，需要完善其他部分  

	a. 对象的模糊显示  
	b. 雷达的扫描效果  
	c. 同步驱动使得整体能动起来，同步一次动一下  
	d. 将点击对象的方法动作实现，加入到显示列表，暂不实现界面的切换功能    

17. 本项目暂时到这，以后有时间再解决同步问题，还有后续的显示效果以后有空慢慢补充  
18. 2016.9.7  

	a. 在服务端进行同步时出现问题，如果有客户端加入，则将对象加入列表，可以进行索引，但是当客户端退出时，怎么处理，比如多人玩游戏，有人想退出，怎么处理该事件？  
	b. 使用map数据类型是否可以解决该问题？数据传递的问题，需要学习程序架构的知识，补充！  

19. 2017.2.23 编码问题，以后应该使用英文注释，并且编码为utf-8  
20. 分支该删除的就删除，整体整改完毕，上传  
21. 2017.2.28记：  

    * 客户端本船航向调整的时候可读变化有问题  
    * 客户端的响应式布局有问题，下个版本再解决  
    * 服务端创建的时候船舶朝向的旋转原点需要改动，重新设计船舶的绘制方式   

22. 2017.3.4记：  

	侧边栏问题只是临时解决，根本问题还是实现不了，参考开源代码学习
	下一步，开始通信整改，参考WebChat工程的源代码

23. 2017年3月5日记：  

    主窗口没办法获取焦点，不能操纵船舶数据的变化    
    获取焦点的办法：setFocusable(true);  

24. 现在基本问题解决，开始显示雷达影像的工作，考虑考虑  
    点击对象后添加到信息面板中，但是由于是画出来的，所以不能显示额外的信息，不太好实现，下次实现应当每艘对象是一个组件，点击后高亮边界......  
25. 当初在组织数据存储结构的时候没有过多的考虑，所以效率不怎么样，今后加强数据结构的应用    
26. 在eclipse上保留master分支，在netbeans上保留netbeans分支，各自负责各自的部分，之后masterpull request主分支  
27. 2017.3.7  
服务端这块就剩下速度和方向的同步了，接下来进行客户端  
28. 2017.3.8  
今天有点不高兴。。。
服务端创建船名时，索引已经创建的对象，如果名称相同，不能创建，因为这里是根据船名来索引的，应该保证唯一性  
考虑使用哈希索引提高索引速度，检索匹配是否有重名，或者使用树结构存储。。。  
29. 





