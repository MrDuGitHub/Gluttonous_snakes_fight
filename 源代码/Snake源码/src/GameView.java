import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import sun.audio.*;
import java.io.FileInputStream;
import javax.swing.JOptionPane;
import sun.audio.*;
public class GameView 
{
	private JPanel canvas;
	private JButton Start = new JButton("想开始吗~");
	private JButton Pause=  new JButton("休息一下~");
	private JButton Restart=new JButton("继续快活啊");
	private JButton Continue=new JButton("休息好了~");
	private JButton Music=new JButton("要不要音乐~");
    private final Grid grid;
    public volatile boolean start=false;
    public boolean pause=false;
    public boolean cont=false;
    public boolean restart=false;
    public int music=0;
    private int size=8,i=0,firstX=100,firstY=100,foodnum,AInum,BigNum;
	private Random random=new Random(System.currentTimeMillis());
	private boolean loop=true;
    private AudioStream as;
    private AudioData ad=null;
	private ContinuousAudioDataStream cads;
	private AudioPlayer player;
	private FileInputStream fileau;
    public void init() 
    { 	
        canvas = new JPanel() 
        {
        	{
        		Start.setFocusable(false);
        		Pause.setFocusable(false);
        		Continue.setFocusable(false);
        		Restart.setFocusable(false);
        		Music.setFocusable(false);
        		Pause.addActionListener(new ButtonListener());
         		Start.addActionListener(new ButtonListener());
         		Continue.addActionListener(new ButtonListener());
         		Restart.addActionListener(new ButtonListener());
         		Music.addActionListener(new ButtonListener());
         		this.add(Start);
         		this.add(Pause);  
         		this.add(Restart);
         		this.add(Continue);
         		this.add(Music);
        	}
        	public void paintComponent(Graphics graphics) 
            {
            	int i=0;
            	foodnum=grid.getFoodNum();
        		AInum=grid.getAInum();
        		BigNum=grid.getBignum();
                drawGridBackground(graphics,grid.getheight(),grid.getWidth());               
        		for (i=1;i<=foodnum;i++)
                  drawFood(graphics, grid.getFood(i));
        		if(!grid.getSnake().die)drawSnake(graphics, grid.getSnake());
                for (i=1;i<=AInum;i++)
                	drawSnakeAI(graphics, grid.getAI(i));
                for(i=1;i<=BigNum;i++)
                	drawBigfood(graphics,grid.getBigfood(i),grid.getBigfood(i).getColor());
            }
        };
    }
    private class ButtonListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e) 
    	{
    		if (e.getSource()==Start) 
    		{ 
    			start=true;
    			Start();
    		}
    		if (e.getSource()==Pause) 
    		{
    			pause=true;
    			Stop();
    		}
    		if (e.getSource()==Continue) 
    		{
    			pause=false;
    			if (music%2==0) Start();
    		}
    		if (e.getSource()==Restart) 
    		{
    			if (grid.getSnake().die)
    			restart=true;
    		}
    		if (e.getSource()==Music)
    		{
    			if (!pause)
    			{
    			    music++;
    			    if (music%2==0) Start();
    	    	    else Stop();
    			}  			
    		}
    	}
    }
    public GameView(Grid grid) 
    {
        this.grid = grid;
		foodnum=grid.getFoodNum();
		AInum=grid.getAInum();
		BigNum=grid.getBignum();
		try 
	        {
	           FileInputStream fileau = new FileInputStream("背景音乐2.wav");//D:\Java\work space\Snake
	           FileInputStream fileau2 = new FileInputStream("背景音乐1.wav");//d:\\Java\\work space\\Snake\\	          
	           if (loop) as = new AudioStream(fileau);
	       	   else as= new AudioStream(fileau2);
	       	   ad = as.getData();   
	           cads =new ContinuousAudioDataStream(ad);
	        }
	        catch (Exception e)
	        {	
	        }
		player = AudioPlayer.player;
    }
    public void draw() 
    {
        canvas.repaint();
    }
    public JPanel getCanvas() 
    {
        return canvas;
    }
    public void drawSnake(Graphics graphics, Snake snake) 
    {
    	graphics.setColor(new Color(255,0,0));
    	graphics.drawString("玩家",(snake.body.get(0).getY()-1)*size+firstY,(snake.body.get(0).getX()-1)*size+firstX);
    	Color color=snake.color;
    	for (i=0;i<snake.body.size();i++)
    		
    	{if ((i==snake.body.size()/2)&&(snake.eternalTime>0)) 
		{
			   int x=(snake.body.get(i).getX()-snake.body.size())*size+firstX;
	    	   int y=(snake.body.get(i).getY()-snake.body.size())*size+firstY;
			   drawCircle(graphics,x,y,color,2*snake.body.size()*(size-2),true);
  		}
    		drawSquare(graphics,(snake.body.get(i).getX()-1)*size+firstX,(snake.body.get(i).getY()-1)*size+firstY,1,1,color,snake.eternalTime);
    	}
    }
    public void drawSnakeAI(Graphics graphics, SnakeAI snakeAI) 
    {
    	Color color=snakeAI.getColor();
    			//(snakeAI.color.getBlue()+127)%256,(snakeAI.color.getRed()+127)%256,(snakeAI.color.getGreen()+127)%256);
    	graphics.setColor(color);
    	graphics.drawString("电脑"+snakeAI.getnum()+"号",(snakeAI.body.get(0).getY()-1)*size+firstY,(snakeAI.body.get(0).getX()-1)*size+firstX);
    	for (i=0;i<snakeAI.body.size();i++)
    	{
    	
    		if ((i==snakeAI.body.size()/2)&&(snakeAI.eternalTime>0)) 
    			{
    			   int x=(snakeAI.body.get(i).getX()-snakeAI.body.size())*size+firstX;
    	    	   int y=(snakeAI.body.get(i).getY()-snakeAI.body.size())*size+firstY;
    			   drawCircle(graphics,x,y,color,2*snakeAI.body.size()*(size-2),true);
         		}
    		drawSquare(graphics,(snakeAI.body.get(i).getX()-1)*size+firstX,(snakeAI.body.get(i).getY()-1)*size+firstY,1,1,color,snakeAI.eternalTime);
    	}
    }
    public void drawFood(Graphics graphics, Food food) 
    {
    	int place=random.nextInt(4);
    	int x,y;
    	x=(food.getX()-1)*size+firstX;y=(food.getY()-1)*size+firstY;
    	if (place==1) {x+=size/2;};
    	if (place==2) {y+=size/2;};
    	if (place==3) {x+=size/2;y+=size/2;};
    	drawCircle(graphics,x,y,food.getColor(),size/2,false);
    }
    public void drawGridBackground(Graphics graphics,int height,int width) 
    {
       	drawSquare(graphics,0,0,width+30,height+25,Color.GRAY,0);
    	Color color=new Color(255,255,255);
    	ImageIcon icon=new ImageIcon("背景.png");
    	Image image = icon.getImage(); 
        drawSquare(graphics,firstX,firstY,width,height,color,0);                  //(100,100)为地图左上角
        graphics.drawImage(image,firstX,firstY,width*size,height*size, null);
    }
    public void drawBigfood(Graphics graphics,Node n,Color color)
    {
    	drawCircle(graphics,(n.getX()-1)*size+firstX,(n.getY()-1)*size+firstY,color,size,false);
    }
    private void drawSquare(Graphics graphics,int x,int y,int width,int height,Color color,int snakeEternal) 
    {
        graphics.setColor(color);
        if (snakeEternal<=0)
        	{
        	     graphics.fillRect(y,x,width*size,height*size);
        	     graphics.setColor(new Color(255,255,255));
        	     graphics.drawRect(y,x,width*size,height*size);
        	}
        else
        	graphics.drawRect(y,x,width*size,height*size);
    }
    private void drawCircle(Graphics graphics,int x,int y, Color color, int foodsize,boolean Eternal) 
    {
       graphics.setColor(color);
       if (Eternal)
    	   graphics.drawOval(y,x,foodsize,foodsize);  
       else
    	   graphics.fillOval(y,x,foodsize,foodsize);     
    }
   	public boolean getStart() 
	{
		return start;
	}
   	public void setRestaet()
   	{
   		restart=true;
   	}
   	public void setEnable(boolean f)
   	{  
   		Restart.setEnabled(f);
   	}
	public void Start()
	{
		if (loop) player.start(cads); 
		else player.start(as);
	}
	public void Stop()
	{
		if (loop) player.stop(cads); 
		else player.stop(as);
	}
}