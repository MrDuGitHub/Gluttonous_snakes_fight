import java.awt.Color;
import java.util.Random;
public class SnakeAI extends Snake
{
	private Random random=new Random();
	private int i;
	private Node goal;
    public SnakeAI(int length1,int x,int y,int num)
    {
    	super(num);
        this.length=random.nextInt(length1-4)+5;
        Color c=new Color(random.nextInt(255)+1,random.nextInt(255)+1,random.nextInt(255)+1);
        setColor(c);
        Node n=new Node(x,y);
        n.setColor(c);
        this.body.add(n);
    	int m=0,t=random.nextInt(4);
        for (i=2;i<=this.length;i++)
        {
        	m=random.nextInt(4);
        	while (m==((t+2)%4))
        	{
        		m=random.nextInt(4);
        	}
        	t=m;
        	x-=move[m][0];y-=move[m][1];
        	if ((x>0)&&(x<101)&&(y>0)&&(y<121))
        	n=new Node(x,y);
        	else n=body.getLast();
        	n.setColor(c);
        	this.body.add(n);
        	if (i==2) changeDirection(m);
        }
    }
    public void changeDirection(int d)
    {
    	if (d==0) this.snakeDirection=Direction.UP;
        if (d==1) this.snakeDirection=Direction.RIGHT;
        if (d==2) this.snakeDirection=Direction.DOWN;
        if (d==3) this.snakeDirection=Direction.LEFT;
    }
    
    public void setColor(Color color)
    {
    	this.color=color;
    }
    public Color getColor()
    {
    	return color;
    }
    public int getnum()
    {
    	return num;
    }
    public void setnum(int n)
    {
    	num=n;
    }
    public Node getGoal()
    {
    	return goal;
    }
    public void setGoal(Node g)
    {
    	goal=g;
    }
}
