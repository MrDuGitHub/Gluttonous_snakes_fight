import java.awt.Color;
import java.util.Random;
public class Food 
{
	private Random random=new Random();
    private int x,y;
    private Color color;
	public Food()
    {
    	x=random.nextInt(100)+1;
    	y=random.nextInt(120)+1;
    	color=new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
    }
	public int getX() 
    {
        return x;
    }
    public int getY() 
    {
        return y;
    }
    public Color getColor() 
    {
        return color;
    }
}
