import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
public class GameController implements Runnable, KeyListener
{
	private Object[] option={"�����ˣ�Ъ��~","�Ҳ���������"};
	private Object[] speedoption={"��","�ܿ�","�ر��~hhh","�ð�~��������"};
	private Object[] AIoption={"�Ҳ��ţ�����","Ҫ��...�����򵥵�"};
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
       int t=JOptionPane.showOptionDialog(null, "���ɱ��"+grid.getSnake().getKilled()+"������"+"��ĳ���Ϊ"+grid.getSnake().body.size(), 
	    		"hhhhhh������(^��^)", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option,option[0]);
	   if (t==1) gameView.setRestaet();
	   t=-1;
	}
	public void showChoice()
	{
	   int t=JOptionPane.showOptionDialog(null, "�����������~��ѡĬ���ر��Ŷ",
	    		"�뿪ʼ�Ļ���ѡ���ѶȰ�~hhh", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, speedoption,speedoption[0]);
	   if (t==0) setTime(100);
	   if (t==1) setTime(80);
	   if (t==2) setTime(50);
	   if (t==3) setTime(150);
	}
	public void showChoice2()
	{
		int t=JOptionPane.showOptionDialog(null, "�ҵ�AI���Ǻ�������Ŷ~���Ҳ�ѡĬ���ѵ�",
	    		"���ˣ�Ҫ���ҷŷ�ˮ~hhh",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, AIoption,AIoption[0]);
		grid.setAI(t);
	}
	public void setTime(int t)
	{
		time=t;
	}
}