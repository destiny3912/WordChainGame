package client;

import java.net.Socket;

public class User {
	public Socket socket = null;
	public Object msgBox = null;
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public Object getMsgBox() {
		return this.msgBox;
	}
	
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void setMsgBox(Object msgBox) {
		this.msgBox = msgBox;
	}
	
	
	public User(Socket socket, Object msgBox) {
		this.socket = socket;
		this.msgBox = msgBox;
	}
}
