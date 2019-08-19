import java.awt.Color;
public class Node 
{
    private final int x;
    private final int y;
    private Color color;
    public Node(int x, int y) 
    {
        this.x = x;
        this.y = y;
    }
    public int getX() 
    {
        return x;
    }
    public int getY() 
    {
        return y;
    }
    public void setColor(Color color)
    {
    	this.color=color;
    }
    public Color getColor()
    {
    	return color;
    }
}
