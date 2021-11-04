package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import javax.swing.JFrame;

public class CFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedWriter bw = null;
	public BufferedReader br = null;
	
	public CFrame(BufferedWriter bw, BufferedReader br)	{
		super();
		this.bw = bw;
		this.br = br;		
	}
	
	public CFrame() {
		super();
	}
}
