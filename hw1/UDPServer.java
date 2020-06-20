import java.net.*;
import java.io.*;
import java.util.*;

class UDPThread extends Thread{  
    private DatagramSocket aSocket; 
	private DatagramPacket request;

	UDPThread(DatagramSocket s, DatagramPacket r) {
		aSocket = s; 
		request = r; 
	}
	
    public void run(){
		try{
			DatagramPacket reply = new DatagramPacket(request.getData(),
		    request.getLength(), request.getAddress(), request.getPort());
			this.sleep(3000);
		    aSocket.send(reply);
			System.out.println("Current Thread: " + Thread.currentThread().getName()); 
		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Interrupted: " + e.getMessage());
		}
    } 
};

public class UDPServer{
	public static void main(String args[]){
		DatagramSocket aSocket = null;
		int serverPort = 6789;
		try{
			aSocket = new DatagramSocket(serverPort);
			byte[] buffer = new byte[1000];
			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				
				UDPThread t = new UDPThread(aSocket, request);  
				t.start();
				
				//DatagramPacket reply = new DatagramPacket(request.getData(),
				//request.getLength(), request.getAddress(), request.getPort());
				//aSocket.send(reply);
			}
		} catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null) aSocket.close();
		}
	}
}