import java.awt.Color;
import java.util.LinkedList;
public class Snake 
{
	protected LinkedList<Node> body = new LinkedList<>();
	protected int[][] move=new int[][]{{-1,0},{0,1},{1,0},{0,-1}};
	protected int num,i,speed=0,smallfood=0,speedlength=0,length=5,nextX,nextY,foodValue=3,limitlength=20;
	protected boolean die=false;
    protected Direction snakeDirection = Direction.RIGHT;     
    protected int eternalTime=15;
    protected Color color;
    private int Killed=0;
    public Snake(int i)
    {
    	color=new Color(0,0,255);
    	speed=1;
    	num=i;
    }
    public void eat(int food,int body)
    {   
    	smallfood+=food;
    	while (smallfood>=foodValue)
    		{
    		  addTail();
    		  smallfood-=foodValue;
    		}
        while (body>0) 
        {
        	 addTail();
             body--;
        }
    }
    public void next()
    {
    	int x=getHead().getX();
    	int y=getHead().getY();
    	nextX=x+move[snakeDirection.ordinal()][0];
    	nextY=y+move[snakeDirection.ordinal()][1];
    }
    public int move(int i) 
    {
    	int x=1;
    	if ((i>1)&&(body.size()>5)) speedlength++;
    	Node head=new Node(nextX,nextY);
    	body.add(0,head);
    	if ((speedlength>limitlength)&&(body.size()>5)) 
    	{
            speedlength-=10;
            x++;
    	}
    	return x;
    }
    public Node getHead() 
    {
        return body.getFirst();
    }
    public Node getTail() 
    {
        return body.getLast();
    }
    public void addTail() 
    {
    	int tailX=body.getLast().getX();
    	int tailY=body.getLast().getY();
    	int temX=body.get(body.size()-2).getX();
    	int temY=body.get(body.size()-2).getY();
    	Node tail;
		if ((tailX+tailX-temX>0)&&(tailX+tailX-temX<101)&&(tailY+tailY-temY>0)&&(tailY+tailY-temY<121))
            tail=new Node(tailX+tailX-temX,tailY+tailY-temY);
		else tail=new Node(tailX,tailY); 
    	body.addLast(tail);
    }
    public void addTail(Node n)
    {
    	body.addLast(n);
    }
    public LinkedList<Node> getBody() 
    {
        return body;
    }
    public void addKilled()
    {
    	Killed++;
    }
    public int getKilled()
    {
    	return Killed;
    }
    public void setSpeed(int s)
    {
    	this.speed=s;
    }
    public int getDirection()
    {
    	return snakeDirection.ordinal();
    }
    public void changeDirection(Direction newDirection) 
    {
   	      snakeDirection=newDirection;	 
    }
    public void setColor() 
	{
		color=new Color(255,0,0);
	}
	public Color getColor() 
	{
		return color;
	}
}