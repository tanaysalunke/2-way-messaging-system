package groupChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class GroupChatClient {

	public static Socket client;
	public static DataInputStream dis;
	
	public static class ListenForMessage implements Runnable {
	
		public void run() {
			try {
				dis = new DataInputStream(client.getInputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage());
			}
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


	public static void main(String[] args) throws IOException {
		client = new Socket("10.0.0.139", 36310);
		System.out.println("connected");
		Thread messageListener = new Thread(new ListenForMessage());
		messageListener.start();
		
		DataOutputStream dOut = new DataOutputStream(client.getOutputStream());
		Scanner console = new Scanner(System.in);
		while (true) {
			String s = console.nextLine();
			sendMessage(s, dOut);
		}

	}

	public static void sendMessage(String str, DataOutputStream dOut) throws IOException {
		byte[] temp = str.getBytes();
		dOut.write(temp);
		dOut.flush();
		System.out.println("Sent " + temp.length + " bytes");
	}

}