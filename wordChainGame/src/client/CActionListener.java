package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class CActionListener implements ActionListener{
	public BufferedWriter bw = null;
	public BufferedReader br = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public CActionListener(BufferedWriter bw, BufferedReader br) {
		super();
		this.bw = bw;
		this.br = br;
	}
}
