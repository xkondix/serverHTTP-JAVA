package serverHTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerHTTP {

	
	public static String log = "log.txt";
    public static PrintWriter logWriter;

    static {
        try{
            logWriter = new PrintWriter(new BufferedWriter(new FileWriter(log, true)));
        }catch (IOException e){
            System.out.println(e);
        }
    }
	
    
    public static void main(String[] args) throws IOException 
    {  
	int port;
    boolean accepted = false;
    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
    ServerSocket server = null;
    Socket clientSocket;
    
    if(args.length == 0)
    {
        System.out.println("Specify port number!");
        System.out.println("PORT:");
    }
    else
    {
        try
        {
            port = Integer.parseInt(args[0]);
            server = new ServerSocket(port);
            System.out.println("Server started" + server);
            accepted = true;
        } 
        catch (BindException e)
        {
            System.out.println("Port is already in use");
            System.out.print("PORT:");
        }
    }
    
    while (!accepted){
        if(keyboard.ready())
        {
            try
            {
                port = Integer.parseInt(keyboard.readLine());
                server = new ServerSocket(port);
                System.out.println("Server started:" +server);
                accepted = true;
            } 
            catch (BindException e)
            {
                System.out.println("Port is already in use");
                System.out.print("PORT:");
            }
        }
    }
    while(true)
    {
        System.out.println("Waiting for connection...");
        clientSocket = server.accept();
        new Thread(new Task(clientSocket)).start();
    }
}
}