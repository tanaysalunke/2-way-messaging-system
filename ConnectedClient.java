package groupChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectedClient {

	private Socket connection;
	private DataOutputStream dOut;
	private DataInputStream dIn;
	
	public ConnectedClient(Socket s) throws IOException {
		this.connection = s;
		this.dOut = new DataOutputStream(s.getOutputStream());
		this.dIn = new DataInputStream(s.getInputStream());
	}
	
	public void sendMessage(String str) throws IOException {
		byte[] temp = str.getBytes();
		dOut.write(temp);
		dOut.flush();
		System.out.print("Sent " + temp.length + " bytes");
	}
	
	public String acceptMessage() throws IOException {
		byte[] b = new byte[dIn.available()];
		dIn.readFully(b, 0, dIn.available());
		String s = new String(b);
		return s;		
	}
	
	public Socket getSocket() {
		return this.connection;
	}
	
	public DataOutputStream getDataOutputStream() {
		return this.dOut;
	}
	
	public DataInputStream getDataInputStream() {
		return this.dIn;
	}
	
	public String getInetAdress() {
		return "" + this.connection.getInetAddress();
	}
	
	public int getPort() {
		return this.connection.getPort();
	}
}
