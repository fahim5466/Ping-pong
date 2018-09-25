package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import game.Game;

public class KeyManager implements KeyListener {

	private Game game;
	private boolean keys[];
	public boolean up,down,w,s;
	
	public KeyManager(Game game)
	{
		this.game = game;
		keys = new boolean[256];
	}
	
	public void tick()
	{
		up=keys[KeyEvent.VK_UP];
		down=keys[KeyEvent.VK_DOWN];
		w=keys[KeyEvent.VK_W];
		s=keys[KeyEvent.VK_S];
		
		if(w)
		{
			if(game.getStatus()==1){System.out.println("Server: ");}
			else{System.out.println("Client: ");}
			System.out.println("w pressed");
			game.getBar().moveUp();
		}
		
		if(s)
		{
			if(game.getStatus()==1){System.out.println("Server: ");}
			else{System.out.println("Client: ");}
			System.out.println("s pressed");
			game.getBar().moveDown();
		}
		
		//Server code
		if(game.getStatus()==1)
		{
			if(w)
			{
				try {
					game.getServer().getOut().writeUTF("w\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(s)
			{
				try {
					game.getServer().getOut().writeUTF("s\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//Client code
		if(game.getStatus()==0)
		{
			if(w)
			{
				try {
					game.getClient().getOut().writeUTF("w\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(s)
			{
				try {
					game.getClient().getOut().writeUTF("s\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
