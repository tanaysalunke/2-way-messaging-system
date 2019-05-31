package groupChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class GroupChatServer{
	
	public static ServerSocket ss;
	public static ConnectedClient cc;
	public static DataInputStream dis;
	public static Socket serverClient;
	
	
	public static class EstablishConnection implements Runnable {
	
		public void run() {
			try {
				ss = new ServerSocket(49856);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while(true) {
				try {
					Socket temp = ss.accept();
					cc = new ConnectedClient(temp);
					System.out.print("Socket established with " + temp.getInetAddress() + ".  Port " + temp.getPort() + "\n");
					dis = cc.getDataInputStream();
					Thread messageListener = new Thread(new ListenForMessage());
					messageListener.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class ListenForMessage implements Runnable {
	
		public void run() {
			
			
			while (true) {
				try {
					if (dis.available()!=0) {
						byte[] b = new byte[dis.available()];
						dis.readFully(b,0,dis.available());
						String backToString = new String(b);
						System.out.println(backToString);
					}
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
	
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Thread connection = new Thread(new EstablishConnection());
		connection.start();
		Scanner console = new Scanner(System.in);
		while (true) {
			String s = console.nextLine();	
			sendMessage(s, cc);
		}
	}
	public static void sendMessage(String str, ConnectedClient c) throws IOException {
		DataOutputStream dOut = new DataOutputStream(c.getSocket().getOutputStream());
		byte[] temp = str.getBytes();
		dOut.write(temp);
		dOut.flush();
	}

}