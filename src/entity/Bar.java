package entity;

import java.awt.Color;
import java.awt.Graphics;

public class Bar {

	public int x,y,w,h;
	
	public Bar(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	
	public void render(Graphics g)
	{
		g.setColor(Color.blue);
		g.fillRect(x, y, w, h);
	}
	
	public void moveUp()
	{
		if(y-10>=0){y-=10;}
	}
	
	public  void moveDown()
	{
		if(y+10+h<=600){y+=10;}
	}
	
}
