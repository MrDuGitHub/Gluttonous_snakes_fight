import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
public class Snakeapp 
{
    public static void main(String[]args)
    {
    	Snakeapp snakeapp=new Snakeapp();
    	snakeapp.init();
    }
    public void init()
    {
    	JFrame window =new JFrame("贪吃蛇大作战专属原创简化版");
    	Grid grid=new Grid(100,120);
    	GameView gameview=new GameView(grid);
    	gameview.init();                                    //添加面板
    	gameview.getCanvas().setPreferredSize(new Dimension(1200,1000));
		window.getContentPane().add(gameview.getCanvas());
        GameController gameController = new GameController(grid, gameview);
        window.addKeyListener(gameController);
        window.pack();
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        new Thread(gameController).start();      
        //管理员权限 Jdialog
        //双人版
    }
}
