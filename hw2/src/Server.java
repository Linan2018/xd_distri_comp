import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;


class Process extends Thread{
	private DatagramPacket request;
	private BlockingQueue<DatagramPacket> outQueue;
    private TransactionCounter counter1;

	Process(BlockingQueue<DatagramPacket> outQueue, DatagramPacket r, TransactionCounter counter) {
		this.outQueue = outQueue;
		request = r;
		this.counter1 = counter;
	}


	private byte[] compute(float a, float b, char op){
		switch(op)
		{
			case '+' :
				return Float.toString(a + b).getBytes();
			case '-' :
				return Float.toString(a - b).getBytes();
			case '*' :
				return Float.toString(a * b).getBytes();
			case '/' :
				return Float.toString(a / b).getBytes();
			default :
				return "Wrong input format! (only supports +, -, *, /)".getBytes();
		}
	}
	
    public void run(){
		int op_index = 0;
		char op = '\0';
		int error_flag = 0;
		float num1 = 0;
		float num2 = 0;
		byte[] reply_data = null;
		
		try{
            String s = new String(request.getData()).trim();
			System.out.println("Process Thread: Start to process " + s); 
			Thread.sleep(2000);
			// System.out.println(s);

			for(int i = 0; i<s.length(); i++) {
				char t = s.charAt(i);
				if(!(t<='9' && t>='0') && t!='.') {
					op = t;
					op_index = s.indexOf(op);
					break;
				}
			}

			try{
				num1 = Float.valueOf(s.substring(0, op_index).trim()).floatValue();
				num2 = Float.valueOf(s.substring(op_index+1, s.length()).trim()).floatValue();
				
			} catch (NumberFormatException e){
				reply_data = "Wrong input format! (only supports two operands)".getBytes();
				error_flag = 1;
			}

            // compute
			if(error_flag == 0){
				reply_data = compute(num1, num2, op);
			}

			// to modify  TransactionCounter
			DatagramPacket reply = new DatagramPacket(reply_data, reply_data.length, request.getAddress(), request.getPort());
			
			
			outQueue.put(reply);
		    // aSocket.send(reply);
			System.out.println("Process Thread: Complete " + s + ".Put a DatagramPacket into outQueue. (" + outQueue.size() + " elements in the outQueue)");
            counter1.increase();
			Thread.sleep(500);
			
		} catch (InterruptedException e) {
			System.out.println("Interrupted: " + e.getMessage());
		}
    } 
};

class TransactionCounter {
	private int counter; 
	public TransactionCounter() {
		counter = 0;
	}
	public synchronized void increase() { // Thread Safe
		counter++;
		System.out.println("Process Thread: Number of completed task(s) is " + counter);
	}
}

class Worker implements Runnable {
	private BlockingQueue<DatagramPacket> inQueue;
	private BlockingQueue<DatagramPacket> outQueue;
	private TransactionCounter counter;

	public Worker(BlockingQueue<DatagramPacket> inQueue, BlockingQueue<DatagramPacket> outQueue) {
		this.inQueue = inQueue;
		this.outQueue = outQueue;
		this.counter = new TransactionCounter();
	}


	public void run() {
		try{
			Thread.sleep(2000);
		} catch(InterruptedException e){
			System.out.println("Int: " + e.getMessage());
		}
		String tempStr = null;
		while (true) {
			try {
				Thread.sleep(2000);
				DatagramPacket request = inQueue.take();
				System.out.println("Worker Thread: Take a DatagramPacket from inQueue. (" + inQueue.size() + " elements in the inQueue)");

                Process t = new Process(outQueue, request, counter);  
				t.start();
                Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Sender implements Runnable {
	private BlockingQueue<DatagramPacket> outQueue;

	public Sender(BlockingQueue<DatagramPacket> outQueue) {
		this.outQueue = outQueue;
	}

	public void run() {
		try{
			Thread.sleep(2000);
		} catch(InterruptedException e){
			System.out.println("Int: " + e.getMessage());
		}
		int serverPort = 6789;
		DatagramSocket outSocket = null;
		try{
			outSocket = new DatagramSocket(serverPort);
			while (true){
					DatagramPacket reply = outQueue.take();
		            System.out.println("Sender Thread: Take a DatagramPacket from outQueue. (" + outQueue.size() + " elements in the outQueue)");
					outSocket.send(reply);
					Thread.sleep(500);
			}
		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Int: " + e.getMessage());
		} finally {
			if (outSocket != null) outSocket.close();
		}
	}
}

class Receiver implements Runnable {
	private BlockingQueue<DatagramPacket> inQueue;

	public Receiver(BlockingQueue<DatagramPacket> inQueue) {
		this.inQueue = inQueue;
	}

	public void run() {
		try{
			Thread.sleep(2000);
		} catch(InterruptedException e){
			System.out.println("Int: " + e.getMessage());
		}
		
		DatagramSocket inSocket = null;
		int serverPort = 6788;
		try{
			inSocket = new DatagramSocket(serverPort);
			
			while(true){
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				inSocket.receive(request);
                inQueue.put(request);
                System.out.println("Receiver Thread: Put a DatagramPacket into inQueue. (" + inQueue.size() + " elements in the inQueue)");
			    Thread.sleep(500);
			}
		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Int: " + e.getMessage());
		} finally {
			if (inSocket != null) inSocket.close();
		}
	}
}

public class Server{
	public static void main(String args[]){
		@SuppressWarnings("unchecked")
		BlockingQueue<DatagramPacket> inQueue = new LinkedBlockingQueue();
		@SuppressWarnings("unchecked")
		BlockingQueue<DatagramPacket> outQueue = new LinkedBlockingQueue();
		new Thread(new Receiver(inQueue), "Receiver").start();
		new Thread(new Sender(outQueue), "Sender").start();
		new Thread(new Worker(inQueue, outQueue), "Worker").start();
		
	}
}