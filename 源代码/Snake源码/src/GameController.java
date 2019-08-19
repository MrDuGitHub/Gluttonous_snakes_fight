import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
public class GameController implements Runnable, KeyListener
{
	private Object[] option={"玩累了，歇会~","我不服！再来"};
	private Object[] speedoption={"快","很快","特别快~hhh","好吧~来个慢的"};
	private Object[] AIoption={"我不信！来啊","要不...来个简单的"};
	private final Grid grid;
    private final GameView gameView;
    private int time=50;
    private boolean running;
    public GameController(Grid grid, final GameView gameView) 
    {
        this.grid = grid;
        this.gameView = gameView;
        this.running = true;
    }
    public void run() 
    {
    	int i=0;
        while (running)
        {
            try 
            {
              Thread.sleep(time);
            } catch (Exception e) 
            {
                break;
            }
            if (gameView.start)
            	{ 
            	  if (i==0)
            	  {
            		  i++;
            		  showChoice();
            		  showChoice2();
            	  }
            	  if (gameView.pause) 
            		  continue;  
            	  if (!grid.getSnake().die)
            	  {
            		  if (grid.nextRound()) 
            		  {
            		      grid.blast(grid.getSnake());
            		      showGameOverMessage();
            		  }
            	  }
            	  else 
            	  {
            		  grid.nextRound();
            	      if (gameView.restart) 
            	      {
            			  showChoice();
            	    	  grid.restart();
            	          gameView.restart=false;
            	      }
            	  }
                  gameView.draw();
               	}
        }     
        running = false;
    }
	public void keyTyped(KeyEvent e) 
	{		
	}
	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode())
		{
		  case KeyEvent.VK_UP:
			  if (grid.getSnake().getDirection()!=2)
			     grid.getSnake().changeDirection(Direction.UP);
			     break;
		  case KeyEvent.VK_DOWN:
			  if (grid.getSnake().getDirection()!=0)
				 grid.getSnake().changeDirection(Direction.DOWN);
				 break;
		  case KeyEvent.VK_LEFT:
			  if (grid.getSnake().getDirection()!=1)
				 grid.getSnake().changeDirection(Direction.LEFT);
				 break;
		  case KeyEvent.VK_RIGHT:
			  if (grid.getSnake().getDirection()!=3)
				 grid.getSnake().changeDirection(Direction.RIGHT);
				 break;
		  case KeyEvent.VK_SPACE:
				 if (grid.getSnake().body.size()>5)grid.getSnake().setSpeed(2);
				 break;
		}
	}
	public void keyReleased(KeyEvent e) 
	{
	     if (e.getKeyCode()==KeyEvent.VK_SPACE)
            grid.getSnake().setSpeed(1);
	}
	public void showGameOverMessage() 
	{
       int t=JOptionPane.showOptionDialog(null, "你击杀了"+grid.getSnake().getKilled()+"个敌人"+"你的长度为"+grid.getSnake().body.size(), 
	    		"hhhhhh你死了(^▽^)", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option,option[0]);
	   if (t==1) gameView.setRestaet();
	   t=-1;
	}
	public void showChoice()
	{
	   int t=JOptionPane.showOptionDialog(null, "你想玩多快的呢~不选默认特别快哦",
	    		"想开始的话先选择难度吧~hhh", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, speedoption,speedoption[0]);
	   if (t==0) setTime(100);
	   if (t==1) setTime(80);
	   if (t==2) setTime(50);
	   if (t==3) setTime(150);
	}
	public void showChoice2()
	{
		int t=JOptionPane.showOptionDialog(null, "我的AI可是很厉害的哦~而且不选默认难的",
	    		"算了，要不我放放水~hhh",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, AIoption,AIoption[0]);
		grid.setAI(t);
	}
	public void setTime(int t)
	{
		time=t;
	}
}