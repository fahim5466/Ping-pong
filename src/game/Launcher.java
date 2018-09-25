package game;

import javax.swing.JOptionPane;

import network.Client;
import network.Server;

public class Launcher {
	
	public static void main(String[] args)
	{	
		Game game = new Game("Title!",800,600);
		game.start();
	}
	
}
