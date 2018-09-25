package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

import game.Game;

public class ServerBall {

	Game game;
	private int x=300,y=400,r=5;
	private int xmove=0,ymove=0;
	private Bar bar1,bar2;
	
	public ServerBall(Game game)
	{
		this.game = game;
		this.bar1 = game.getBar1();
		this.bar2 = game.getBar2();
		xmove = 7;
		ymove = 7;
		
	}
	
	public void tick()
	{
		
		Rectangle r1 = new Rectangle(bar1.x,bar1.y,bar1.w,bar1.h);
		Rectangle r2 = new Rectangle(bar2.x,bar2.y,bar2.w,bar2.h);
		
		x+=xmove;
		
		Rectangle br = new Rectangle(x-r,y-r,2*r,2*r);
		
		if(x<0 || x>800 ||
		  br.intersects(r1) ||
		  br.intersects(r2)
		)
		{
			xmove*=-1;
			x+=xmove;
		}
		
		y+=ymove;
		
		br = new Rectangle(x-r,y-r,2*r,2*r);
		
		if(y<0 || y>600 ||
		   br.intersects(r1) ||
		   br.intersects(r2)
		)
		{
			ymove*=-1;
			y+=ymove;
		}
		
		try {
			game.getServer().getOut().writeUTF("Ball "+x+" "+y+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.red);
		g.fillOval(x-r, y-r, 2*r, 2*r);
	}
	
}
