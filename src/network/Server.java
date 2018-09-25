package network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import entity.Bar;
import game.Game; 
  
public class Server implements Runnable
{ 
	int port;
	Game game;
	
	//Thread
	private boolean keepRunning;
	Thread thread;
	
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null;
    private DataOutputStream out     = null; 
  
    // constructor with port 
    public Server(int port, Game game) 
    { 
    	this.port=port;
    	this.game = game;
    }
    
    public Socket getSocket()
    {
    	return socket;
    }
    
    public DataOutputStream getOut()
    {
    	return out;
    }

	@Override
	public void run() {
		
		while(keepRunning)
		{
			 // starts server and waits for a connection 
	        try
	        { 
	            server = new ServerSocket(port); 
	            System.out.println("Server started"); 
	  
	            System.out.println("Waiting for a client ..."); 
	  
	            socket = server.accept(); 
	            System.out.println("Client accepted"); 
	            game.unPause();
	  
	            // takes input from the client socket 
	            in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); 
	            
	            out    = new DataOutputStream(socket.getOutputStream());
	  
	            String line = ""; 
	  
	            // reads message from client until "Over" is sent 
	            while (!line.equals("Over")) 
	            {
	                try
	                { 
	                    line = in.readUTF();
	                    System.out.print("Server line: "+line);
	                    if(line.startsWith("w"))
	                    {
	                    	game.getBar2().moveUp();
	                    }
	                    else if(line.startsWith("s"))
	                    {
	                    	game.getBar2().moveDown();
	                    }
	  
	                } 
	                catch(IOException i) 
	                { 
	                    System.out.println(i); 
	                } 
	            } 
	            System.out.println("Closing connection"); 
	  
	         // close the connection 
	            try
	            { 
	                in.close(); 
	                out.close(); 
	                socket.close();
	                keepRunning=false;
	            } 
	            catch(IOException i) 
	            { 
	                System.out.println(i); 
	            }  
	        } 
	        catch(IOException i) 
	        { 
	            System.out.println(i); 
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
