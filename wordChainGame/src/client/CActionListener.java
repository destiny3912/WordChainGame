package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class CActionListener implements ActionListener{
	public BufferedWriter bw = null;
	public BufferedReader br = null;
	public Socket socket = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public CActionListener(BufferedWriter bw, BufferedReader br) {
		super();
		this.bw = bw;
		this.br = br;
	}
	
	public CActionListener(Socket socket, BufferedWriter bw, BufferedReader br)	{
		super();
		this.socket = socket;
		this.bw = bw;
		this.br = br;
	}
	
	public CActionListener(Socket socket) {
		super();
		this.socket = socket;
		try {
			this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
