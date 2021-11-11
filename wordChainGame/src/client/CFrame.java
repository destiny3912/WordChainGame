package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class CFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedWriter bw = null;
	public BufferedReader br = null;
	public Socket socket = null;
	
	public CFrame(Socket socket, BufferedWriter bw, BufferedReader br)	{
		super();
		this.socket = socket;
		this.bw = bw;
		this.br = br;
	}
	
	public CFrame() {
		super();
	}
}
