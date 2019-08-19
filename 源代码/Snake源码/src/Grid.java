import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
public class Grid
{
    private final int width;
    private final int height;
    private int FoodNum=750,AInum=15,biggestAL=10,AverageLength=5,BornX=40,BornY=50,BornX2,BornY2,AIlevel=0;
    private int i,j,k,tem;
	private Random random=new Random();   
    private int[][][] status=new int[501][501][101];
    private int[][] move={{-1,0},{0,1},{1,0},{0,-1}};
    private int[][] BornSpace={{20,20,20,20,20,50,50,50,50,50,80,80,80,80,80},
    		                           {20,40,60,80,100,20,40,60,80,100,20,40,60,80,100}};
    private boolean[] f=new boolean[15];
    private Snake snake;
    private Snake snake2;
    private boolean Snake2=false;
    private int[] DiedAI=new int[16];
    private int[] EatenFood=new int[501];
    private int[] EatenBigFood=new int[501];
    private LinkedList<SnakeAI> snakeAI = new LinkedList<>(); 
    private LinkedList<Food> SmallFood = new LinkedList<>();
    private LinkedList<Node> BigFood = new LinkedList<>();
   
    public Grid(int height, int width) 
    {
    	 this.width = width;
    	 this.height = height;
     	 snake=new Snake(1);
     	 createSnake(snake,BornX,BornY);
    	 createSnakeAI(AInum);
         if (Snake2) 
         {
         	 snake2=new Snake(2);
         	 snake2.setColor();
        	 createSnake(snake2,BornX2,BornY2);
         }
    	 createFood(FoodNum);
    	 renew();
    }
    public void createSnake(Snake snake,int x,int y) 
	{
        for (i=1;i<=snake.length;i++)
        {
        	Node n=new Node(x,y-i);
        	n.setColor(snake.color);
        	snake.body.add(n);
        }		
	}
    private void createSnakeAI(int num) 
	{
       for (i=1;i<=num;i++)
       {
    	   j=random.nextInt(AInum);
    	   while (f[j]) j=random.nextInt(AInum);
    	   f[j]=true;
    	   SnakeAI AI=new SnakeAI(AverageLength,BornSpace[0][j],BornSpace[1][j],i+1);   	   
    	   snakeAI.add(AI);
       }
       for (i=0;i<=AInum-1;i++) f[i]=false;
	}
    private void createFood(int num) 
	{
       for (i=1;i<=num;i++)
       {
    	   Food f=new Food();
           SmallFood.add(f);
       }
	}
    public void renew()
    {
    	cleanstatus();
   	    if (!snake.die) initsnake(snake);
   	    if (Snake2)
   	    	if (!snake2.die) initsnake(snake2);
   	    initsnakeAI();
   	    initsmallfood();
   	    initbigfood();
    }
    public void cleanstatus()
    {
    	int i,j,k;
    	for (k=0;k<=50;k++)
   		  for (i=0;i<=height+20;i++)
   			 for(j=0;j<=width+20;j++)
   				 status[i][j][k]=50;
   	    for (k=0;k<=30;k++)
   		   for (i=1;i<=height;i++)
   			 for (j=1;j<=width;j++)
   				 status[i][j][k]=0;
    }
    public void initsnake(Snake snake)
    {
    	int i;
    	if (snake.eternalTime<=0)
    	for (i=1;i<=snake.body.size();i++) 
        	   status[snake.body.get(i-1).getX()][snake.body.get(i-1).getY()][2]=snake.num;
    }
    public void initsnakeAI()
    {
    	int i,j;
    	for (i=1;i<=snakeAI.size();i++)
    	{
    		snakeAI.get(i-1).setnum(i+1);
    	   	if (snakeAI.get(i-1).eternalTime<=0)
   	    	for (j=1;j<=snakeAI.get(i-1).body.size();j++)
   	    		status[snakeAI.get(i-1).body.get(j-1).getX()][snakeAI.get(i-1).body.get(j-1).getY()][2]=i+1;
    	}
    }
    public void initsmallfood()
    {
    	int i,t;
    	for (i=1;i<=SmallFood.size();i++)
		{ 
   	      status[SmallFood.get(i-1).getX()][SmallFood.get(i-1).getY()][0]++;
   	      t=status[SmallFood.get(i-1).getX()][SmallFood.get(i-1).getY()][0];
   	      status[SmallFood.get(i-1).getX()][SmallFood.get(i-1).getY()][t+2]=i;
		}
    }
    public void initbigfood()
    {
    	int i,t;
    	if (BigFood.size()>0)
   	    	for (i=1;i<=BigFood.size();i++)
    		{ 
	    	      status[BigFood.get(i-1).getX()][BigFood.get(i-1).getY()][1]++;
	    	      t=status[BigFood.get(i-1).getX()][BigFood.get(i-1).getY()][1];
	    	      status[BigFood.get(i-1).getX()][BigFood.get(i-1).getY()][t+12]=i;
    		}
    }
    public boolean nextRound()
    { 
    	if (!snake.die) snake.die=snakenext(snake);
    	if (Snake2)
    		if (!snake2.die) snake2.die=snakenext(snake2);
    	AInext(); 	
    	if (!snake.die) snake.die=work(snake);
    	if (Snake2)
    		if (!snake2.die) snake2.die=work(snake2);
        AIwork();
        remove();
        add();
        renew();
        AI();
    	return snake.die;
    }
    public boolean snakenext(Snake snake)
    {
    	int i,j,t;
		if (snake.eternalTime>-5)snake.eternalTime--;
    	for (i=1;i<=snake.speed;i++)
        {
    		snake.next();
    	    if ((snake.nextX<1)||(snake.nextX>100)||(snake.nextY<1)||(snake.nextY>120))
    		   return snake.die=true;
            else
    	    {
            	t=snake.move(i);
    		    for (j=1;j<=t;j++)
    		    	status[snake.body.get(snake.body.size()-j).getX()][snake.body.get(snake.body.size()-j).getY()][2]=0;
    		    for (j=1;j<=t;j++)
    		    	snake.body.remove(snake.body.size()-1);
    	    }
    	    int food=status[snake.getHead().getX()][snake.getHead().getY()][0];
    	    int body=status[snake.getHead().getX()][snake.getHead().getY()][1];
    	    snake.eat(food, body);
    	    for (j=1;j<=snake.body.size();j++)
    	    	if (status[snake.body.get(j-1).getX()][snake.body.get(j-1).getY()][2]==0)
    	    		status[snake.body.get(j-1).getX()][snake.body.get(j-1).getY()][2]=1;
        }
    	return false;
    }
    public void AInext()
    {
    	int i,j,k;
    	for (k=1;k<=snakeAI.size();k++)
    	{
    		SnakeAI AI=getAI(k);
    		if 	(AI.eternalTime>-5) AI.eternalTime--;
       		if (AI.getGoal()!=null) 
       		{
       			if ((Math.abs(AI.getGoal().getX()-AI.getHead().getX())
       					+Math.abs(AI.getGoal().getY()-AI.getHead().getY()))<3)
       					AI.setSpeed(1);
       			else
       			AI.setSpeed(2);
       		}
       		else AI.setSpeed(1);
    		for (i=1;i<=AI.speed;i++)
            {
        		AI.next();
        		if (AI.getGoal()!=null)
        		{
            		if ((AI.nextX==AI.getGoal().getX())&&(AI.nextY==AI.getGoal().getY()))
        				AI.setGoal(null);
        		}
        	    if ((AI.nextX<1)||(AI.nextX>100)||(AI.nextY<1)||(AI.nextY>120))
        		     {
        	    	   AI.die=true;
        	    	   int t=1;
               		   while (DiedAI[t]!=0) t++;
               		   DiedAI[t]=k+1;
               		   AI.speed=0;
        	    	   break;
        		     }
        	    int tt=AI.move(i);
        	    for (j=1;j<=tt;j++)
    		    	status[AI.body.get(AI.body.size()-j).getX()][AI.body.get(AI.body.size()-j).getY()][2]=0;
        	    for (j=1;j<=tt;j++)
    		    	AI.body.remove(AI.body.size()-1);
        	 }
    		int food=status[AI.getHead().getX()][AI.getHead().getY()][0];
    	    int body=status[AI.getHead().getX()][AI.getHead().getY()][1];
    	    AI.eat(food, body);
    	    for (j=1;j<=AI.body.size();j++)
    	    	if (status[AI.body.get(j-1).getX()][AI.body.get(j-1).getY()][2]==0)
    	    		status[AI.body.get(j-1).getX()][AI.body.get(j-1).getY()][2]=k+1;
        }
    }
    public boolean work(Snake snake)
    {
    	boolean die=false;
    	die=snakework(snake);
        return die;
    }
    public boolean snakework(Snake snake)
    {
    	int i,j,t;
    	if (snake.speed!=0)
        for (i=1;i<=snake.speed;i++)
        {
        	int x=snake.body.get(i-1).getX();
        	int y=snake.body.get(i-1).getY();
        	if ((status[x][y][2]==0)||(status[x][y][2]==1))
        	{t=1;
        	while (EatenFood[t]!=0) t++;
        	for (j=1;j<=status[x][y][0];j++)
        	{
                EatenFood[t+j-1]=status[x][y][j+2];
        		status[x][y][j+2]=0;
        	}
        	status[x][y][0]=0;
        	t=1;
        	while (EatenBigFood[t]!=0) t++;
        	for (j=1;j<=status[x][y][1];j++)
        	{
                EatenBigFood[t+j-1]=status[x][y][j+12];
        		status[x][y][j+12]=0;
        	}
        	status[x][y][1]=0;
        	}
        	else 
        	{
        	   if (snake.eternalTime<=0)return true;
        	}
        }
    	return false;
    }
    public void AIwork()
    {
    	int i,j,k,t;
    	for (k=1;k<=snakeAI.size();k++)
        {
        	SnakeAI AI=snakeAI.get(k-1);
        	for (i=1;i<=AI.speed;i++)
            {
            	int x=AI.body.get(i-1).getX();
            	int y=AI.body.get(i-1).getY();
            	if (((status[x][y][2]==0)||(status[x][y][2]==k+1)))//&&!AI.die) 
            	{t=1;
            	while (EatenFood[t]!=0) t++;
            	for (j=1;j<=status[x][y][0];j++)
            	{
                    EatenFood[t+j-1]=status[x][y][j+2];
            		status[x][y][j+2]=0;
            	}
            	status[x][y][0]=0;
            	t=1;
            	while (EatenBigFood[t]!=0) t++;
            	for (j=1;j<=status[x][y][1];j++)
            	{
                    EatenBigFood[t+j-1]=status[x][y][j+12];
            		status[x][y][j+12]=0;
            	}
            	status[x][y][1]=0;
            	}
            	else 
            	{
            		if ((status[x][y][2])==1) snake.addKilled();
            		AI.die=true;
            		t=1;
            		while (DiedAI[t]!=0) t++;
            		DiedAI[t]=k+1;
            		AI.speed=0;
            	}
            }
        }
    }
    public void remove()
    {
        int t=1;
        while (EatenFood[t]!=0) t++;
        Arrays.sort(EatenFood,1,t);
        for (i=t-1;i>=1;i--)
        	SmallFood.remove(EatenFood[i]-1);
        for (i=1;i<=t;i++) EatenFood[i]=0;
        t=1;
        while (EatenBigFood[t]!=0) t++;
        Arrays.sort(EatenBigFood,1,t);
        for (i=t-1;i>=1;i--)
        	BigFood.remove(EatenBigFood[i]-1);
        for (i=1;i<=t;i++) EatenBigFood[i]=0;
       	t=1;
    	while (DiedAI[t]!=0) t++;
        Arrays.sort(DiedAI,1,t);
        for (i=t-1;i>=1;i--)
        {
        	blast(snakeAI.get(DiedAI[i]-2));
        	snakeAI.remove(DiedAI[i]-2);
        }
        for (i=1;i<=t;i++) DiedAI[i]=0;
    }
    public void blast(Snake snake2)
    {
    	int i;
    	Color color=snake2.getColor();
    	for (i=1;i<=snake2.body.size();i++)
    	{
    		if (i%2==1) 
    				{
    			            int x=snake2.getBody().get(i-1).getX()+move[random.nextInt(4)][0];
    			            int y=snake2.getBody().get(i-1).getY()+move[random.nextInt(4)][1];
							if ((x>0)&&(x<101)&&(y>0)&&(y<121))	
							{
								Node f=new Node(x,y);
								f.setColor(color);
								BigFood.add(f);
							}
    				}
    	}
    }   
    public void add()
    {
    	if(snakeAI.size()<AInum) createSnakeAI(AInum-snakeAI.size());
    	if((SmallFood.size()+BigFood.size()*4)<FoodNum) createFood(FoodNum-(SmallFood.size()+BigFood.size()*4));
    }
    public void AI()
    {
    	int i=1;
    	AverageLength=0;
    	for (i=1;i<=snakeAI.size();i++)
    	   {
    		 search(snakeAI.get(i-1));
    		 AverageLength+=snakeAI.get(i-1).body.size();
    	   }
    	if (snakeAI.size()!=0)AverageLength/=snakeAI.size();
    	if (AverageLength>biggestAL) AverageLength=biggestAL;
    }
    public void search(SnakeAI AI)
    {
   
       	int i,j,k,x,y,temx,temy,food=0,body,AId,dx,dy,dy1,dy2,dx1,dx2,dx3,dy3;
       	boolean l=false,r=false,s=false,rr=false,ll=false;
       	Node[] goal=new Node[5];
       	int[] Food=new int[5];
       	x=AI.getHead().getX();
       	y=AI.getHead().getY();
       	temx=x-3;
       	temy=y-3;
       	for(i=1;i<=4;i++)
          	{
          		if (i==2) temy+=3;
          		if (i==3) temx+=3;
          		if (i==4) temy-=3;
          		for (j=0;j<=3;j++)
          			for(k=0;k<=3;k++)
          			{
          				if ((temx+j)>0&&(temx+j)<101&&(temy+k)>0&&(temy+k)<121)
          				{
          					food+=status[temx+j][temy+k][0];
          				    body=status[temx+j][temy+k][1];
          				    if ((status[temx+j][temy+k][2]==AI.getnum())||(status[temx+j][temy+k][2]==0))
          				    	      if (body!=0)
          				    	    	  { 
          				    	    	     if ((goal[i]==null))goal[i]=new Node(temx+j,temy+k);		    
          				    	    	     else 
          				    	    	     {
          				    	    	    	 if ((Math.abs(temx+j-x)+Math.abs(temy+k-y))
          				    	    	    			 <(Math.abs(goal[i].getX()-x)+Math.abs(goal[i].getY()-y)))
          				    	    	    			goal[i]=new Node(temx+j,temy+k);
          				    	    	     }
          				    	    	  }
          				}
          			}
       		    Food[i]+=food;
       		    body=0;food=0;
          	}
       	AId=AI.getDirection();
       	temx=x+move[AI.getDirection()][0];
       	temy=y+move[AI.getDirection()][1];
       	if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) s=true;
       	else
       	if ((status[temx][temy][2]!=AI.getnum())&&(status[temx][temy][2]!=0))
       	 s=true;
       	temx=x+2*move[AI.getDirection()][0];
       	temy=y+2*move[AI.getDirection()][1];
       	if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) s=true;
       	else
       	if ((status[temx][temy][2]!=AI.getnum())&&(status[temx][temy][2]!=0))
      	 s=true;
      	temx=x+move[(AI.getDirection()+1)%4][0];
       	temy=y+move[(AI.getDirection()+1)%4][1];
       	if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) r=true;
       	else
       	if ((status[temx][temy][2]!=AI.getnum())&&(status[temx][temy][2]!=0))
       	r=true;
       	if (AIlevel==1)
       	{
       		if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) rr=true;
       	}
       	temx=x+2*move[(AI.getDirection()+1)%4][0];
       	temy=y+2*move[(AI.getDirection()+1)%4][1];
       	if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) r=true;
       	else
       	if ((status[temx][temy][2]!=AI.getnum())&&(status[temx][temy][2]!=0))
       	r=true;
       	if (AIlevel==1)
       	{
       		if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) rr=true;
       	}
       	temx=x+move[(AI.getDirection()+3)%4][0];
       	temy=y+move[(AI.getDirection()+3)%4][1];
       	if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) l=true;
       	else
       	if ((status[temx][temy][2]!=AI.getnum())&&(status[temx][temy][2]!=0))
       	l=true;
       	if (AIlevel==1)
       	{
       		if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) ll=true;
       	}
       	temx=x+2*move[(AI.getDirection()+3)%4][0];
       	temy=y+2*move[(AI.getDirection()+3)%4][1];
       	if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) l=true;
       	else
       	if ((status[temx][temy][2]!=AI.getnum())&&(status[temx][temy][2]!=0))
       	l=true;
       	if (AIlevel==1)
       	{
       		if ((temx<1)||(temx>100)||(temy<1)||(temy>120)) ll=true;
       	}
       	if((AIlevel==1)&&(random.nextInt(2)%2==0))
       	{
       		ll=l;rr=r;
       	}
       	if (AIlevel==0)
       	{
        	if (s)
        		{
        		   if (l) AI.changeDirection((AId+1)%4); 
        		   else
        			   {
        			      if (r) AI.changeDirection((AId+3)%4);   
        			      else
        			      {
        			    	  if (random.nextInt(2)==0) AI.changeDirection((AId+1)%4); 
        			    	  else AI.changeDirection((AId+3)%4);  
        			      }
        			   }
        		   return;
        		}
        	else
        	{
        	   if ((l)||(r))
        	   {
        		   if (!l) AI.changeDirection((AId+3)%4); 
        		   if (!r) AI.changeDirection((AId+1)%4);
        		   if ((l)&&(r)) return;
        		   return;
        	   }
        	}
       	}
       	else
       	{
       		if (s)
    		{
    		   if (ll) AI.changeDirection((AId+1)%4); 
    		   else
    			   {
    			      if (rr) AI.changeDirection((AId+3)%4);   
    			      else
    			      {
    			    	  if (random.nextInt(2)==0) AI.changeDirection((AId+1)%4); 
    			    	  else AI.changeDirection((AId+3)%4);  
    			      }
    			   }
    		   return;
    		}
    	else
    	{
    	   if ((ll)||(rr))
    	   {
    		   if (!ll) AI.changeDirection((AId+3)%4); 
    		   if (!rr) AI.changeDirection((AId+1)%4);
    		   if ((ll)&&(rr)) return;
    		   return;
    	   }
    	}
       	}
       		if ((AI.getGoal()!=null)||(goal[AId]!=null)||(goal[(AId+1)%4]!=null))
       		{
       			if (AI.getGoal()==null)
       				{
       				    if (goal[AId]==null)  AI.setGoal(goal[(AId+1)%4]);
       				    else
       				    {
       			           if (goal[(AId+1)%4]==null) AI.setGoal(goal[(AId)%4]);
       			           else
       			           if (random.nextInt(2)==0) AI.setGoal(goal[(AId)%4]); 
       		    		     else AI.setGoal(goal[(AId+1)%4]);
       				    }
       				}
       			temx=AI.getGoal().getX();
       			temy=AI.getGoal().getY();
       			dx=Math.abs(temx-x);
       			dy=Math.abs(temy-y);
       			dx1=Math.abs(temx-x-move[(AId+3)%4][0]);
       			dy1=Math.abs(temy-y-move[(AId+3)%4][1]);
       			dx2=Math.abs(temx-x-move[(AId+1)%4][0]);
       			dy2=Math.abs(temy-y-move[(AId+1)%4][1]);
       			dx3=Math.abs(temx-x-move[(AId+2)%4][0]);
       			dy3=Math.abs(temy-y-move[(AId+2)%4][1]);
       			if ((dx1+dy1)<(dx+dy))  AI.changeDirection((AId+3)%4); 
       			if ((dx2+dy2)<(dx+dy))  AI.changeDirection((AId+1)%4); 
       			if (((dx3+dy3)<(dx+dy))&&((dx1+dy1)==(dx2+dy2)))
       				if (random.nextInt(2)==0)
       					AI.changeDirection((AId+3)%4);
       				else
       					AI.changeDirection((AId+1)%4);
       			return;
       		}
       		int t=random.nextInt(10000);
       		if (t%6==0)
       			{
       			   if (random.nextInt(2)==0) AI.changeDirection((AId+1)%4); 
           		      else AI.changeDirection((AId+3)%4); 
       			}
       		else
       		{
       			if ((Food[AId]!=0)||(Food[(AId+1)%4]!=0)&&(t%6==0))
       			{
       				if (Food[AId]>=5) AI.changeDirection((AId+3)%4);
       				else 
       				if (Food[(AId+1)%4]>=5) AI.changeDirection((AId+1)%4); 
       			}
       		}
    }
    public void setAI(int i)
    {
    	AIlevel=i;
    }
    public void restart()
    {
    	setSnake(new Snake(1));
    	createSnake(snake,BornX,BornY);
    }
    public int getWidth()
    {
    	return this.width;
    }
    public int getheight()
    {
    	return this.height;
    }
    public Snake getSnake() 
	{
		return snake;
	}
    public Food getFood(int i) 
	{
		return SmallFood.get(i-1);
	}
	public SnakeAI getAI(int i) 
	{
		return snakeAI.get(i-1);
	}
	public Node getBigfood(int i) 
	{
		return BigFood.get(i-1);
	}
    public int getBignum() 
	{
		return BigFood.size();
	}
	public int getFoodNum()
	{
		return SmallFood.size();
	}
	public int getAInum() 
	{	
		return snakeAI.size();
	}
	public void setSnake(Snake s) 
	{
	    snake=s;
	}
}
