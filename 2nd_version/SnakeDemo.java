import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

class Tile{
	int x;
	int y;
	
	public Tile(int x0,int y0){
		x = x0;
		y = y0;
	}
}

public class SnakeDemo extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3794762291171148906L;
	
	private JLabel label  = new JLabel("当前长度：");
	private JLabel label2 = new JLabel("所花时间：");
	private JLabel label3 = new JLabel("当前得分：");
	private JLabel label4 = new JLabel("剩余时间：");
	private JLabel Length = new JLabel("1");
	private JLabel Score = new JLabel("0");
	private JLabel Time = new JLabel("");
	private JLabel Time2 = new JLabel("5");
	private Font f = new Font("微软雅黑",Font.PLAIN,15);
	private JPanel p = new JPanel();
	
	private final int MAX_SIZE = 400;//蛇身体最长为400节
	private Tile temp = new Tile(0,0);
	private Tile temp2 = new Tile(0,0);
	private Tile head;
	private Tile[] body = new Tile[MAX_SIZE];
	
	private String direction = "R";//默认向右走
	private String current_direction = "R";//当前方向
	private boolean first_launch = false;
	private boolean iseaten = false;
	private boolean isrun = true;
	private int randomx,randomy;
	private int body_length = 0;//身体长度初始化为0
	private Thread run;
	
	private int hour =0;
	private int min =0;
	private int sec =0 ;
	
	private boolean pause = false;
	
	private long millis = 500;//每隔500毫秒刷新一次
	private boolean speed = true;
	private Calendar Cld;
	private int MI,MI2,MI3;
	private int SS,SS2,SS3;
	
	private final int foodkind = 6;
	private int score = 0;
	private int foodtag;
	private int[] point_list = new int[6];
	private ImageIcon snakehead;
	private ImageIcon snakebody;
	private ImageIcon[] food = new ImageIcon[foodkind];
	private JLabel head_label;
	private JLabel[] body_label = new JLabel[MAX_SIZE];
//	private JLabel[] food_label = new JLabel[2];//每次产生两种食物
	private JLabel food_label;//每次产生一种食物
	
	private int countsecond = 5;//每种食物产生5秒后消失或者换位置
	private boolean ifcount = false;
	
	public SnakeDemo(){
		
		String lookAndFeel =UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		//布局
		add(label);
	    label.setBounds(500, 10, 80, 20);
	    label.setFont(f);
	    add(Length);
	    Length.setBounds(500, 35, 80, 20);
	    Length.setFont(f);
	    add(label2);
	    label2.setBounds(500, 70, 80, 20);
	    label2.setFont(f);
	    add(Time);
	    Time.setBounds(500, 95, 80, 20);
	    Time.setFont(f);    
	    add(label3);
	    label3.setBounds(500, 130, 80, 20);
	    label3.setFont(f);
	    add(Score);
	    Score.setBounds(500, 155, 80, 20);
	    Score.setFont(f);
	    add(p);
	    p.setBounds(498, 200, 93, 1);
	    p.setBorder(BorderFactory.createLineBorder(Color.black));
	    add(label4);
	    label4.setBounds(500, 210, 80, 20);
	    label4.setFont(f);
	    add(Time2);
	    Time2.setBounds(500, 235, 80, 20);
	    Time2.setFont(f); 
	    
	    //初始化头部坐标
	    ProduceRandom();
	    head = new Tile(randomx,randomy);
	    snakehead = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\head\\head.png");
	    snakehead.setImage(snakehead.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
        head_label = new JLabel(snakehead); 
        add(head_label);
        head_label.setBounds(head.x, head.y, 20, 20);
        head_label.setOpaque(false);
        
        //初始化身体所有节点
        snakebody = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\body\\body.png");
		snakebody.setImage(snakebody.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
		for(int i = 0; i < MAX_SIZE;i++)
		{
			body[i] = new Tile(0,0);
			body_label[i] = new JLabel(snakebody); 
			body_label[i].setOpaque(false);
		}
		
		//初始化食物
		food[0] = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\food\\hotdog.png");
	    food[0].setImage(food[0].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
	    food[1] = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\food\\hamburger.png");
	    food[1].setImage(food[1].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
	    food[2] = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\food\\drink.png");
	    food[2].setImage(food[2].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
	    food[3] = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\food\\green_apple.png");
	    food[3].setImage(food[3].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
	    food[4] = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\food\\blue_apple.png");
	    food[4].setImage(food[4].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
	    food[5] = new ImageIcon("C:\\Users\\ZelyakovAY\\IdeaProjects\\Snake2\\2nd_version\\food\\red_apple.png");
	    food[5].setImage(food[5].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
	    
	    //初始化各食物对应的得分
	    point_list[0] = 20;
	    point_list[1] = 40;
	    point_list[2] = 30;
	    point_list[3] = 10;
	    point_list[4] = 30;
	    point_list[5] = 50;
	    
	    ProduceFood();
        food_label.setOpaque(false);
		
		//添加监听器
		this.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		super.keyPressed(e);
	    		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
	    		{
	    			if(isrun && current_direction != "L")
	    			{
	    				direction = "R";
	    			}
	    		}
	    		if(e.getKeyCode() == KeyEvent.VK_LEFT)
	    		{
	    			if(isrun && current_direction != "R")
	    			{
	    				direction = "L";
	    			}
	    		}
	    		if(e.getKeyCode() == KeyEvent.VK_UP)
	    		{
	    			if(isrun && current_direction != "D")
	    			{
	    				direction = "U";
	    			}
	    		}
	    		if(e.getKeyCode() == KeyEvent.VK_DOWN)
	    		{
	    			if(isrun && current_direction != "U")
	    			{
	    				direction = "D";
	    			}
	    		}
	    		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)//重置所有变量初始值
	    		{
	    			remove(food_label);//去掉被吃掉的食物
					score = 0;
					Score.setText("0");
	    			//初始化头部坐标
	    		    ProduceRandom();
	    		    head = new Tile(randomx,randomy);
	    		    //初始化身体节点部坐标
	    		    for(int i = 0; i < MAX_SIZE;i++)
	    			{
	    				body[i].x = 0;
	    				body[i].y = 0;
	    			}
	    		    
	    			hour =0;
	    			min =0;
	    			sec =0 ;
	    			direction = "R";//默认向右走
	    			current_direction = "R";//当前方向
	    			first_launch = false;
	    			iseaten = false;
	    			isrun = true;
	    			pause = false;
	    			millis = 500;//每隔500毫秒刷新一次
	    			speed = true;
	    			body_length = 0;
	    			Length.setText("1");
	    			
	    			run = new Thread();
	    			run.start();
	    			
	    			ifcount = false;
		    		countsecond = 5;
		    		Time2.setText("5");
		    		ifcount = true;
		    		
	    			System.out.println("Start again");
	    		}
	    		
	    		if(e.getKeyCode() == KeyEvent.VK_SPACE)
	    		{
	    			if(!pause)//暂停
	    			{
	    				pause = true;
	    				isrun = false;
	    				ifcount = false;
	    			}
	    			else//开始
	    			{
	    				pause = false;
	    				isrun = true;
	    				ifcount = true;
	    			}
	    		}
	    	}
		});
		
		this.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		int key = e.getKeyCode();
	    		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
	    		{
	    			if(speed)
	    			{
	    				Cld = Calendar.getInstance();
		    			int YY = Cld.get(Calendar.YEAR) ;
		    	        int MM = Cld.get(Calendar.MONTH)+1;
		    	        int DD = Cld.get(Calendar.DATE);
		    	        int HH = Cld.get(Calendar.HOUR_OF_DAY);
		    	        int mm = Cld.get(Calendar.MINUTE);
		    	        SS = Cld.get(Calendar.SECOND);
		    	        MI = Cld.get(Calendar.MILLISECOND); 
		    	        System.out.println("Pressed time  " + YY + "/" + MM + "/" + DD + "-" + HH
		    	        		+ ":" + mm + ":" + SS + ":" + MI);
		    	        
		    	        speed = false;
	    			}
	    			
	    			Cld = Calendar.getInstance();
	    			SS3 = Cld.get(Calendar.SECOND);
	    	        MI3 = Cld.get(Calendar.MILLISECOND); 
	    	        int x = SS3 * 1000 + MI3 - ( SS * 1000 + MI);
	    	        if(x > 400)//按一个按钮的时长大于400毫秒识别为长按
	    	        {
	    	        	millis = 100;
	    	        	System.out.println("Long Pressed");
	    	        }
	    		}
	    	}
		});
		
		this.addKeyListener(new KeyAdapter() {
	    	public void keyReleased(KeyEvent e) {
	    		int key = e.getKeyCode();
	    		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
	    		{
	    			Cld = Calendar.getInstance();
	    			int YY2 = Cld.get(Calendar.YEAR) ;
	    	        int MM2 = Cld.get(Calendar.MONTH)+1;
	    	        int DD2 = Cld.get(Calendar.DATE);
	    	        int HH2 = Cld.get(Calendar.HOUR_OF_DAY);
	    	        int mm2 = Cld.get(Calendar.MINUTE);
	    	        SS2 = Cld.get(Calendar.SECOND);
	    	        MI2 = Cld.get(Calendar.MILLISECOND); 
	    	        System.out.println("Released time " + YY2 + "/" + MM2 + "/" + DD2 + "-" + HH2 
	    	        		+ ":" + mm2 + ":" + SS2 + ":" + MI2 + "\n" );

	    	        speed = true;
	    	        millis = 500;
	    		}
	    	}
		});
		
		
		new Timer();//开始计时
		
		new Countdown();//开始倒计时
		
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g1){
		super.paintComponent(g1);
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		
		//画头部
//		g.setColor(Color.red);
//		g.fillRoundRect(head.x, head.y, 20, 20, 10, 10);
		head_label.setBounds(head.x, head.y, 20, 20);
		
		g.setPaint(new GradientPaint(115,135,Color.CYAN,230,135,Color.MAGENTA,true));
		if(!first_launch)
		{
			//初始化食物位置
			ProduceFood();
			ProduceRandom();
			add(food_label);
			food_label.setBounds(randomx, randomy, 19, 19);
			ifcount = true;
		}
		else
		{
			//每次刷新身体
			for(int i = 0;i < body_length;i++)
			{
//				g.fillRoundRect(body[i].x, body[i].y, 20, 20, 10, 10);
				body_label[i].setBounds(body[i].x, body[i].y, 20, 20);
			}
			
			if(EatFood())//被吃了重新产生食物
			{
				remove(food_label);//去掉被吃掉的食物
				ProduceFood();
				ProduceRandom();
//				g.fillOval(randomx, randomy, 19, 19);
				add(food_label);
				food_label.setBounds(randomx, randomy, 19, 19);
				iseaten = false;
				
				ifcount = false;
				Time2.setText("5");
				countsecond = 5;
				ifcount = true;
			}
			else
			{
//				g.fillOval(randomx, randomy, 19, 19);
				food_label.setBounds(randomx, randomy, 19, 19);
			}
		}
		first_launch = true;
		
		//墙
		g.setStroke( new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
		g.setBackground(Color.black);
		g.drawRect(2, 7, 491, 469);
		
		//网格线
		for(int i = 1;i < 22;i++)
		{
			g.setStroke( new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
			g.setColor(Color.black);
			g.drawLine(5+i*22,9,5+i*22,472);
			if(i <= 20)
			{
				g.drawLine(4,10+i*22,491,10+i*22);
			}
		}
	}
	
	public void ProduceFood(){
		Random rand = new Random();
		double x;
		x = rand.nextDouble();
		
		if(x >= 0 && x <0.1)
		{
			food_label = new JLabel(food[1]);
			foodtag = 1;
		}
		else if(x >= 0.1 && x <0.25)
		{
			food_label = new JLabel(food[2]);
			foodtag = 2;
		}
		else if(x >= 0.25 && x <0.5)
		{
			food_label = new JLabel(food[0]);
			foodtag = 0;
		}
		else if(x >= 0.5 && x <0.8)
		{
			food_label = new JLabel(food[3]);
			foodtag = 3;
		}
		else if(x >= 0.8 && x <0.95)
		{
			food_label = new JLabel(food[4]);
			foodtag = 4;
		}
		else if(x >= 0.95 && x <1)
		{
			food_label = new JLabel(food[5]);
			foodtag = 5;
		}
		
	} 
	
	public void ProduceRandom(){
		boolean flag = true;
		Random rand = new Random();
		randomx = (rand.nextInt(21) + 1) * 22 + 7;
		randomy = (rand.nextInt(20) + 1) *22 + 12;
		while(flag)
		{
			if(body_length == 0)
			{
				flag = false;
			}
			else
			{
				for(int i = 0;i < body_length; i++)
				{
					if(body[i].x == randomx && body[i].y == randomy)
					{
						randomx = (rand.nextInt(21) + 1) * 22 + 7;
						randomy = (rand.nextInt(20) + 1) *22 + 12;
						flag = true;
						break;
					}
					else
					{
						if(i == body_length - 1)
						{
							flag = false;
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void HitWall(){//判断是否撞墙
		if(current_direction == "L")
		{
			if(head.x < 7)
			{
				new AePlayWave("over.wav").start();
				isrun = false;
				int result=JOptionPane.showConfirmDialog(null, "Game over! Try again?", "Information", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_NO_OPTION)
				{
					remove(food_label);//去掉被吃掉的食物
					score = 0;
					Score.setText("0");
					//初始化头部坐标
	    		    ProduceRandom();
	    		    head = new Tile(randomx,randomy);
	    		    //初始化身体节点部坐标
	    		    for(int i = 0; i < MAX_SIZE;i++)
	    			{
	    				body[i].x = 0;
	    				body[i].y = 0;
	    			}
	    		    
	    		    for(int k = 0;k < body_length;k++)
	    		    {
	    		    	remove(body_label[k]);
	    		    }
	    		    
	    			hour =0;
	    			min =0;
	    			sec =0 ;
	    			direction = "R";//默认向右走
	    			current_direction = "R";//当前方向
	    			first_launch = false;
	    			iseaten = false;
	    			isrun = true;
	    			pause = false;
	    			millis = 500;//每隔500毫秒刷新一次
	    			speed = true;
	    			body_length = 0;
	    			Length.setText("1");
	    			
	    			run = new Thread();
	    			run.start();
	    			
	    			ifcount = false;
		    		countsecond = 5;
		    		Time2.setText("5");
		    		ifcount = true;
		    		
	    			System.out.println("Start again");
				}
				else
				{
					run.stop();
				}		
			}
		}
		if(current_direction == "R")
		{
			if(head.x > 489)
			{
				new AePlayWave("over.wav").start();
				isrun = false;
				int result=JOptionPane.showConfirmDialog(null, "Game over! Try again?", "Information", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_NO_OPTION)
				{
					remove(food_label);//去掉被吃掉的食物
					score = 0;
					Score.setText("0");
					//初始化头部坐标
	    		    ProduceRandom();
	    		    head = new Tile(randomx,randomy);
	    		    //初始化身体节点部坐标
	    		    for(int i = 0; i < MAX_SIZE;i++)
	    			{
	    				body[i].x = 0;
	    				body[i].y = 0;
	    			}
	    		    
	    		    for(int k = 0;k < body_length;k++)
	    		    {
	    		    	remove(body_label[k]);
	    		    }
	    		    
	    			hour =0;
	    			min =0;
	    			sec =0 ;
	    			direction = "R";//默认向右走
	    			current_direction = "R";//当前方向
	    			first_launch = false;
	    			iseaten = false;
	    			isrun = true;
	    			pause = false;
	    			millis = 500;//每隔500毫秒刷新一次
	    			speed = true;
	    			body_length = 0;
	    			Length.setText("1");
	    			
	    			run = new Thread();
	    			run.start();
	    			
	    			ifcount = false;
		    		countsecond = 5;
		    		Time2.setText("5");
		    		ifcount = true;
		    		
	    			System.out.println("Start again");
				}
				else
				{
					run.stop();
				}
			}
		}
		if(current_direction == "U")
		{
			if(head.y < 12)
			{
				remove(food_label);//去掉被吃掉的食物
				score = 0;
				Score.setText("0");
				new AePlayWave("over.wav").start();
				isrun = false;
				int result=JOptionPane.showConfirmDialog(null, "Game over! Try again?", "Information", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_NO_OPTION)
				{
					//初始化头部坐标
	    		    ProduceRandom();
	    		    head = new Tile(randomx,randomy);
	    		    //初始化身体节点部坐标
	    		    for(int i = 0; i < MAX_SIZE;i++)
	    			{
	    				body[i].x = 0;
	    				body[i].y = 0;
	    			}
	    		    
	    		    for(int k = 0;k < body_length;k++)
	    		    {
	    		    	remove(body_label[k]);
	    		    }
	    		    
	    			hour =0;
	    			min =0;
	    			sec =0 ;
	    			direction = "R";//默认向右走
	    			current_direction = "R";//当前方向
	    			first_launch = false;
	    			iseaten = false;
	    			isrun = true;
	    			pause = false;
	    			millis = 500;//每隔500毫秒刷新一次
	    			speed = true;
	    			body_length = 0;
	    			Length.setText("1");
	    			
	    			run = new Thread();
	    			run.start();
	    			
	    			ifcount = false;
		    		countsecond = 5;
		    		Time2.setText("5");
		    		ifcount = true;
		    		
	    			System.out.println("Start again");
				}
				else
				{
					run.stop();
				}
			}
		}
		if(current_direction == "D")
		{
			if(head.y > 472)
			{
				remove(food_label);//去掉被吃掉的食物
				score = 0;
				Score.setText("0");
				new AePlayWave("over.wav").start();
				isrun = false;
				int result=JOptionPane.showConfirmDialog(null, "Game over! Try again?", "Information", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_NO_OPTION)
				{
					//初始化头部坐标
	    		    ProduceRandom();
	    		    head = new Tile(randomx,randomy);
	    		    //初始化身体节点部坐标
	    		    for(int i = 0; i < MAX_SIZE;i++)
	    			{
	    				body[i].x = 0;
	    				body[i].y = 0;
	    			}
	    		    
	    		    for(int k = 0;k < body_length;k++)
	    		    {
	    		    	remove(body_label[k]);
	    		    }
	    		    
	    			hour =0;
	    			min =0;
	    			sec =0 ;
	    			direction = "R";//默认向右走
	    			current_direction = "R";//当前方向
	    			first_launch = false;
	    			iseaten = false;
	    			isrun = true;
	    			pause = false;
	    			millis = 500;//每隔500毫秒刷新一次
	    			speed = true;
	    			body_length = 0;
	    			Length.setText("1");
	    			
	    			run = new Thread();
	    			run.start();
	    			
	    			ifcount = false;
		    		countsecond = 5;
		    		Time2.setText("5");
		    		ifcount = true;
		    		
	    			System.out.println("Start again");
				}
				else
				{
					run.stop();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void HitSelf(){//判断是否撞到自己身上
		for(int i = 0;i < body_length; i++)
		{
			if(body[i].x == head.x && body[i].y == head.y)
			{
				new AePlayWave("over.wav").start();
				isrun = false;
				int result=JOptionPane.showConfirmDialog(null, "Game over! Try again?", "Information", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_NO_OPTION)
				{
					remove(food_label);//去掉被吃掉的食物
					score = 0;
					Score.setText("0");
					//初始化头部坐标
	    		    ProduceRandom();
	    		    head = new Tile(randomx,randomy);
	    		    //初始化身体节点部坐标
	    		    for(int j = 0; j < MAX_SIZE;j++)
	    			{
	    				body[j].x = 0;
	    				body[j].y = 0;
	    			}
	 
	    		    for(int k = 0;k < body_length;k++)
	    		    {
	    		    	remove(body_label[k]);
	    		    }
	    		    
	    			hour =0;
	    			min =0;
	    			sec =0 ;
	    			direction = "R";//默认向右走
	    			current_direction = "R";//当前方向
	    			first_launch = false;
	    			iseaten = false;
	    			isrun = true;
	    			pause = false;
	    			millis = 500;//每隔500毫秒刷新一次
	    			speed = true;
	    			body_length = 0;
	    			Length.setText("1");
	    			
	    			run = new Thread();
	    			run.start();
	    			
	    			ifcount = false;
		    		countsecond = 5;
		    		Time2.setText("5");
		    		ifcount = true;
		    		
	    			System.out.println("Start again");
				}
				else
				{
					run.stop();
				}
				break;
			}
		}
	}
	
	public boolean  EatFood(){
		if(head.x == randomx && head.y == randomy)
		{
			iseaten = true;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void Thread(){
//		millis = 500;//默认每隔500毫秒刷新一次
		run = new Thread() {
			public void run() {
				while (true) 
				{
					try {
						Thread.sleep(millis);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					
					if(!pause)
					{	
						temp.x = head.x;
						temp.y = head.y;
						//头部移动
						if(direction == "L")
						{
							head.x -= 22;
						}
						if(direction == "R")
						{
							head.x += 22;
						}
						if(direction == "U")
						{
							head.y -= 22;
						}
						if(direction == "D")
						{
							head.y += 22;
						}
						current_direction = direction;//刷新当前前进方向
						//身体移动
						for(int i = 0;i < body_length;i++)
						{
							temp2.x = body[i].x;
							temp2.y = body[i].y;
							body[i].x = temp.x;
							body[i].y = temp.y;
							temp.x = temp2.x;
							temp.y = temp2.y;
						}
						
						if(EatFood())
						{
							body_length ++;
							body[body_length-1].x = temp.x;
							body[body_length-1].y = temp.y;
							
							add(body_label[body_length-1]);
							
							Length.setText("" + (body_length+1) );//刷新长度
							score += point_list[foodtag];
							Score.setText("" + score);//刷新得分
							new AePlayWave("eat.wav").start();
						}
						
						repaint();
						//刷新完判断是否撞墙和撞自己的身体
						HitWall();
						HitSelf();
					}
				}
			}
		};
		
		run.start();
	}
	
	public static void main(String[] args) {
		SnakeDemo snake = new SnakeDemo();
		snake.Thread();
		
		JFrame game = new JFrame();
		Image img = Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
		game.setIconImage(img);
	    game.setTitle("Snake By XJX");
	    game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    game.setSize(502, 507);
	    game.setSize(602, 507);
	    game.setResizable(false);
	    game.setLocationRelativeTo(null);
	   
	    game.add(snake);
	    System.out.println("OK");
	    game.setVisible(true);
	}
	
	//倒计时类
	class Countdown extends Thread{
	    public Countdown(){
	        this.start();
	    }
	    
	    @SuppressWarnings("deprecation")
		public void run() {
	    	while(true){
	    		if(ifcount)
		    	{
		    		if(countsecond >= 2)
			    	{
			    		countsecond --;
			    		Time2.setText("" + countsecond);
			    	}
			    	else
			    	{
			    		ifcount = false;
			    		countsecond = 5;
			    		Time2.setText("5");
					
					remove(food_label);
			    		if(foodtag == 1 || foodtag == 5)//分值最高的两种食物在规定时间内没被吃掉就消失
			    		{
			    			ProduceFood();
			    		}
			    		
			    		ProduceRandom();
			    		add(food_label);
			    		ifcount = true;
			    	}
		    	}
	    		
	    		try {
	    			Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	    	}
	    }
	}
	
	//计时器类
	class Timer extends Thread{  
		    public Timer(){
		        this.start();
		    }
		    @Override
		    public void run() {
		        while(true){
		            if(isrun){
		                sec +=1 ;
		                if(sec >= 60){
		                    sec = 0;
		                    min +=1 ;
		                }
		                if(min>=60){
		                    min=0;
		                    hour+=1;
		                }
		                showTime();
		            }
		 
		            try {
		                Thread.sleep(1000);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		             
		        }
		    }

		    private void showTime(){
		        String strTime ="" ;
		        if(hour < 10)
		            strTime = "0"+hour+":";
		        else
		            strTime = ""+hour+":";
		         
		        if(min < 10)
		            strTime = strTime+"0"+min+":";
		        else
		            strTime =strTime+ ""+min+":";
		         
		        if(sec < 10)
		            strTime = strTime+"0"+sec;
		        else
		            strTime = strTime+""+sec;
		         
		        //在窗体上设置显示时间
		        Time.setText(strTime);
		    }
		}	
}

class AePlayWave extends Thread { 	 
    private String filename;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb 

    public AePlayWave(String wavfile) { 
        filename = wavfile;
    } 
    	    
    public void run() { 
        File soundFile = new File(filename); 
        AudioInputStream audioInputStream = null;
        try { 
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e1) { 
            e1.printStackTrace();
            return;
        } catch (IOException e1) { 
            e1.printStackTrace();
            return;
        } 
 
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
 
        try { 
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) { 
            e.printStackTrace();
            return;
        } catch (Exception e) { 
            e.printStackTrace();
            return;
        } 

        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
 
        try { 
            while (nBytesRead != -1) { 
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) 
                    auline.write(abData, 0, nBytesRead);
            } 
        } catch (IOException e) { 
            e.printStackTrace();
            return;
        } finally { 
            auline.drain();
            auline.close();
        } 
    } 
}
