package game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JOptionPane;

import entity.Bar;
import entity.ClientBall;
import entity.ServerBall;
import input.KeyManager;
import input.MouseManager;
import network.Client;
import network.Server;

public class Game implements Runnable{

	//Graphics
		private Display display;
		private String title;
		private int width,height;
		private BufferStrategy bs;
		private Graphics g;
		
	//Thread
	private boolean keepRunning;
	Thread thread;
	
	//Input
	KeyManager keyManager;
	MouseManager mouseManager;
	
	//Entities
	Bar bar1, bar2;
	ServerBall sball;
	ClientBall cball;
	
	//Network
	int status=-1;
	Server s;
	Client c;
	private boolean paused;
	
	public Game(String title, int width, int height)
	{
		this.title=title;
		this.width=width;
		this.height=height;
	}
	
	public void tick()
	{
		keyManager.tick();
		if(status==1){sball.tick();}
		else{cball.tick();}
	}
	
	public void render()
	{
bs = display.getCanvas().getBufferStrategy();
		
		if(bs==null)
		{
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		g.clearRect(0, 0, width, height);
		
		//Draw here
		
		bar1.render(g);
		bar2.render(g);

		if(status==1){sball.render(g);}
		else{cball.render(g);}
		
		//End drawing
		
		bs.show();
		g.dispose();
	}
	
	public void init()
	{
		display = new Display(title,width,height);
		
		keyManager = new KeyManager(this);
		
		display.getFrame().addKeyListener(keyManager);
		
		bar1 = new Bar(0,0,25,200);
		bar2 = new Bar(775,0,25,200);
		sball= new ServerBall(this);
		cball= new ClientBall();
		
		Object[] options = {"Client","Server"};
		int n = JOptionPane.showOptionDialog(display.getFrame(),
				"Would you like to be client or server",
						"Ping-pong",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
		
		if(n==-1)
		{
			System.exit(0);
		}
		else if(n==1)
		{
			status=1;
			paused=true;
			s = new Server(86,this);
			s.start();
		}
		else
		{	
			status=0;
			paused=false;
			String ip = JOptionPane.showInputDialog("Please input the ip address of the server");
			c = new Client(ip,86,this);
			c.start();
		}
	
	}
	
	public Bar getBar()
	{
		if(status==1)
		{
			return bar1;
		}
		else
		{
			return bar2;
		}
	}
	
	public Bar getBar1()
	{
		return bar1;
	}
	
	public Bar getBar2()
	{
		return bar2;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public Server getServer()
	{
		return s;
	}
	
	public Client getClient()
	{
		return c;
	}
	
	public ServerBall getsball()
	{
		return sball;
	}
	
	public ClientBall getcball()
	{
		return cball;
	}
	
	public void unPause()
	{
		paused=false;
	}
	
	@Override
	public void run()
	{
	
		init();
		
		double fps=60;
		double timePerTick = 1000000000/fps;
		double delta=0;
		double now;
		double lastTime = System.nanoTime();
		
		while(keepRunning)
		{
			
			now=System.nanoTime();
			delta+=(now-lastTime)/timePerTick;
			lastTime=now;
			
			if(delta>=1)
			{
				if(!paused){tick();}
				render();
				delta--;
			}
		}
		
	}
	
	public synchronized void start()
	{
		
		if(keepRunning)
		{
			return;
		}
		else
		{
			keepRunning=true;
		}
		
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		
		if(!keepRunning)
		{
			return;
		}
		else
		{
			keepRunning=false;
		}
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
