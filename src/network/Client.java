package network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import game.Game; 
  
public class Client implements Runnable 
{ 
	String address; 
	int port;
	Game game;
	
	//Thread
	private boolean keepRunning;
	Thread thread;
	
    // initialize socket and input output streams 
    private Socket socket            = null; 
    private DataInputStream  in   = null; 
    private DataOutputStream out     = null; 
  
    // constructor to put ip address and port 
    public Client(String address, int port, Game game) 
    { 
    	this.address = address;
    	this.port = port;
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
			  // establish a connection 
	        try
	        { 
	            socket = new Socket(address, port); 
	            System.out.println("Connected"); 
	  
	            in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); 
	  
	            // sends output to the socket 
	            out    = new DataOutputStream(socket.getOutputStream()); 
	        } 
	        catch(UnknownHostException u) 
	        { 
	            System.out.println(u); 
	        } 
	        catch(IOException i) 
	        { 
	            System.out.println(i); 
	        } 
	  
	        String line = ""; 
	        
	        // reads message from client until "Over" is sent 
	        while (!line.equals("Over")) 
	        { 
	        	game.tick();
	        	game.render();
	            try
	            { 
	                line = in.readUTF(); 
	                System.out.print("Client line: "+line);
	                if(line.startsWith("w"))
	                {
	                	game.getBar1().moveUp();
	                }
	                else if(line.startsWith("s"))
	                {
	                	System.out.println("hello");
	                	game.getBar1().moveDown();
	                }
	                else if(line.startsWith("Ball"))
	                {
	                	int[] ints = Arrays.stream(line.replaceAll("-", " -").split("[^-\\d]+"))
	                            .filter(s -> !s.matches("-?"))
	                            .mapToInt(Integer::parseInt).toArray();
	                	
	                	/*
	                	for(int i : ints)
	                	{
	                		System.out.println(i);
	                	}
	                	System.out.println("end");
	                	*/
	                	
	                	game.getcball().setX(ints[0]);
	                	game.getcball().setY(ints[1]);
	                	
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
	            keepRunning = false;
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
