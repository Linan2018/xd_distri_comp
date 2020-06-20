import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class EchoServer {
	public static void main(String[] args) throws Exception {
		Socket clientSocket = null;
		ServerSocket listenSocket = new ServerSocket(8189);
		try{
			ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
			System.out.println("Server listening at 8189");
			do{
				clientSocket = listenSocket.accept();
				System.out.println("Accepted connection from client");
				executor.execute(new ThreadTask(clientSocket));
                System.out.println("Number of existing threads: " + executor.getPoolSize() + "\nNumberNumber of completed tasks: " + executor.getCompletedTaskCount());
			}
			while(clientSocket != null);
		} catch(SocketException e){
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			clientSocket.close();
			listenSocket.close();
		}
	}
}

class ThreadTask implements Runnable{
	private BufferedReader in;
	private PrintWriter out;


	public ThreadTask(Socket clientSocket) {
		try{
			InputStream inStream = clientSocket.getInputStream();
			OutputStream outStream = clientSocket.getOutputStream();
			in = new BufferedReader(new InputStreamReader(inStream));
			out = new PrintWriter(outStream);

		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}

	}

	public void run() {
		try{
			System.out.println(Thread.currentThread().getName());
			String line = null;
			while((line=in.readLine())!=null) {
				System.out.println(Thread.currentThread().getName() + " -- Message from client:" + line);
				out.println(line);
				out.flush();
			}
		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}

	}
}