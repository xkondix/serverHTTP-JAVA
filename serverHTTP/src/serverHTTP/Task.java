package serverHTTP;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;


public class Task implements Runnable {
	private Socket socket;
	private PrintStream out;
	private BufferedReader in;
	
	  public Task(Socket s) {
	    socket=s;
	  }

	  public void run() {
		  
	    try {

	       in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	       out=new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

	      
	      //print all
	      String request;
          List <String> lista = new ArrayList();
          while((request=in.readLine())!=null)
          {
         	 if(request.isEmpty())
         	 {
         		 break;
         	 }
         	 lista.add(request);
         	 System.out.println(request);
          }
	      
	      String filename="";
	      synchronized (ServerHTTP.logWriter){
              String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
              ServerHTTP.logWriter.printf("[%s]%s%s\n", timeStamp,lista.get(0),this.socket.getRemoteSocketAddress().toString());
              ServerHTTP.logWriter.flush();
          }
	      
	      try {
	    	  String word = lista.get(0).split("/")[1];
	    	  
	    	  if(lista.get(0).startsWith("GET"))
	    	  {
	    		  if(!word.startsWith(" "))
	    		  {
	    			  String path = excCommand(word.split(" ")[0]);		
	    			  send(path,word.split(" ")[0]);
	    		  }
	    		  else
	    		  {
	    			  //out.print("HTTP/1.1 404 Not supported.\r\n");
	    			  //throw new FileNotFoundException();  
	    			  send("/home/konrad/eclipse-workspace/serverHTTP/src/serverHTTP/plik.html","plik.html");
	    		  }
	    	  
	    	  }
	    	  else
	    	  {
	    		  out.print("HTTP/1.1 501 Not supported.\r\n");
	    	  }

	        

	    }
	    catch (IOException x)
	    {
	      System.out.println(x);
	    }
	  } 
	    catch (IOException e) 
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	     
	 }
	 finally
     {
		out.close();
     }
}

	  
	  public void send(String name, String filename) throws IOException
	  {
		    InputStream f=new FileInputStream(name);

	        String mimeType="text/plain";
	        if (filename.endsWith(".html") || filename.endsWith(".htm"))
	          mimeType="text/html";
	        else 
		      mimeType=" ";

	        out.print("HTTP/1.0 200 OK\r\n"+
	          "Content-type: "+mimeType+"\r\n\r\n");

	        

	        byte[] a=new byte[1024];
	        int n;
	        while ((n=f.read(a))>0)
	          out.write(a, 0, n);
	        out.flush();
	  }
	  
	
	  public String excCommand(String filename){
		    
		  String s="";
		  try {
			  Process p = Runtime.getRuntime().exec("locate "+filename);                                                                                                                                                     
			  BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			return stdInput.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			return s;
		
		 
		}
	  
}


 