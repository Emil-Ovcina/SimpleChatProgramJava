import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main 
{
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	
	private static int port = 2222;
	
	private static List<clientThreads> threads = new ArrayList<clientThreads>();
	
	public static void main(String[] args)
	{
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true)
		{
			try {
				clientSocket = serverSocket.accept();
				clientThreads t = new clientThreads(clientSocket, threads);
				threads.add(t);
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class clientThreads extends Thread
{
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final List<clientThreads> threads;
	
	public clientThreads(Socket socket, List<clientThreads> threads)
	{
		this.clientSocket = socket;
		this.threads = threads;
	}
	
	public void run()
	{
		List<clientThreads> threads = this.threads;

		try {
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			
			os.println("Connected");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(true)
		{
			try {
				String line = is.readLine();
				
				if(line.equals("\\quit")) break;
				
				for(clientThreads t : threads)
					t.os.println(line);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			is.close();

			os.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}