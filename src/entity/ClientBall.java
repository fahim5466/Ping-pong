package entity;

import java.awt.Color;
import java.awt.Graphics;

public class ClientBall {
	
	private int x,y,r=5;
	
	public void tick()
	{
		
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.red);
		g.fillOval(x-r, y-r, 2*r, 2*r);
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
}
