package goat;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class Goat extends JComponent implements Runnable
{
	private Random r = new Random();

	private JWindow parent;
	private int frame = 0;
	
	private Image[] walking = null;
	@SuppressWarnings("unused")
	private Image[] standing = null;
	
	private Image[] frames = null;
	
	private int x = 0;
	private int y = 0;
	int direction = 1;
	int speed = 1;
	GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice[] devices = g.getScreenDevices();
	private int width = devices[0].getDisplayMode().getWidth();
	private int height = devices[0].getDisplayMode().getHeight();
	private final static int GOATWIDTH = 200, GOATHEIGHT = 200;
	
	private final static int SPRITEWIDTH = 48,SPRITEHEIGHT = 48;
	
	public final static Point GRAY = new Point(0,0), TAN = new Point(1,0), BLACK = new Point(2,0), DARKGRAY = new Point(3,0), WHITE = new Point(0,1), LIGHTBROWN =  new Point(1,1), DARKBROWN =  new Point(2,1), BROWN =  new Point(3,1);
	public final static Point[] COLORS = new Point[]{GRAY,TAN,BLACK,DARKGRAY,WHITE,LIGHTBROWN,DARKBROWN,BROWN};
	int framecount = 0;
	
	
	Action action = Action.WALKING;

	//All of the actions a goat can do
	private enum Action
	{
		STANDING,WALKING,JUMP,DIE
	}
		
	public Goat(JWindow frame, Point color)
	{
		super();
		try{
			BufferedImage master = ImageIO.read(this.getClass().getResource("/spritesheet.png"));
			walking = new Image[3];
			for(int i = 0; i < 3; i++)
			{
				walking[i] = master.getSubimage((color.x*3) * SPRITEWIDTH+ SPRITEWIDTH* i , ((color.y*4) + 2)*SPRITEHEIGHT, SPRITEWIDTH,SPRITEHEIGHT);
			}
		}catch(IOException e){e.printStackTrace();}
		frames = walking;
		this.parent = frame;
		Point loc = calculationJumpLocation();
		this.setLocation(0,0);
		parent.setLocation(loc.x,loc.y);
		this.setSize(GOATWIDTH,GOATHEIGHT);
		y = height - GOATHEIGHT;
		parent.setLocation(0, height-GOATHEIGHT-10);
		
		new Thread(this).start();
	}
	
	public void setAction(Action action)
	{
		this.action = action;
	}
	
	private void goatAction()
	{
		if(action == Action.WALKING)
		{
			x+=direction*speed;
			if(framecount++ == 3)
			{
				frame++;
				framecount = 0;
				if(frame >= frames.length)
					frame = 0;
				parent.repaint();
			}
			if((x > width - GOATWIDTH && direction ==1) || (x < GOATWIDTH && direction == -1)||r.nextDouble() > .993)
				switchDirection();		
			if(!((x > width - GOATWIDTH && direction ==1) || (x < GOATWIDTH && direction == -1))&&r.nextDouble() > .993)
				switchDirection();
		}
		if(action == Action.STANDING)
		{
			long duration = (long) r.nextInt(10000);
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
			}
			action = Action.WALKING;
		}
		if(action == Action.JUMP)
		{
			int idirection = direction;
			Point destination = calculationJumpLocation();
			double dx = destination.x-x;
			double dy = destination.y-y;
			dx/=20;
			dy/=20;
			direction = dx > 0 ? 1 : -1;
			parent.repaint();
			double tempx = x;
			double tempy = y;
			boolean gtx = dx > 0;
			boolean gty = dy > 0;
			boolean donex = false,doney = false;
			while(!donex || !doney)
			{
				if(gtx)
				{
					if(x >= destination.x)
						donex = true;
				}
				else
				{
					if(x <= destination.x)
						donex = true;
				}
				if(gty)
				{
					if(y >= destination.y)
						doney = true;
				}
				else
				{
					if(y <= destination.y)
						doney = true;
				}
				
				tempx+=dx;
				tempy+=dy;
				x=(int)tempx;
				y=(int)tempy;
				parent.setLocation(x, y);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {

				}
			}
			direction = idirection;
			parent.repaint();
			action = Action.WALKING;
		}
		if(action == Action.DIE){
			frame = 1;
			parent.repaint();
		}
	}
	
	private Point calculationJumpLocation() 
	{
		return new Point(r.nextInt(width-GOATWIDTH),r.nextInt(height-GOATHEIGHT));
	}
	private void switchDirection()
	{
		direction = -direction;
		parent.repaint();
	}
	@Override 
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(direction > 0)
			g.drawImage(frames[frame], 0, 0,GOATWIDTH*direction,GOATHEIGHT, null);
		else
			g.drawImage(frames[frame], GOATWIDTH, 0,GOATWIDTH*direction,GOATHEIGHT, null);
	}

	
	@Override
	public void run() 
	{
		while(true)
		{
			goatAction();
			parent.setLocation(x, y);
			if(r.nextDouble()>.999)
			{
				if(action == Action.WALKING) {
					action = Action.JUMP;
					frames = walking;
					frame = 1;
				}

			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
