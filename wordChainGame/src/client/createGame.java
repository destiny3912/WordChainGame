package client;
/*
 * 상대방의 아이디나 닉네임을 key로 1대1 게임을 만드는 class입니다.
 * */
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class createGame extends CFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	private String id = null;
	private Socket socket = null;
	
	public createGame(Socket socket, BufferedWriter bw, BufferedReader br, String id)
	{
		super.bw = bw;
		super.br = br;
		this.id = id;
		this.socket = null;
	}
	
	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(500, 150);
		mainWindow.setTitle("Create Game");
		mainWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mainWindow.setLayout(new BorderLayout());
		mainWindow.setResizable(false);
		
		JPanel userNamePane = new JPanel();
		JPanel buttonPane = new JPanel();
		
		mainWindow.add(userNamePane, BorderLayout.NORTH);
		mainWindow.add(buttonPane, BorderLayout.SOUTH);
		
		//상대 user의 이름을 작성하는 작성란
		JTextField userName = new JTextField("Enter room number");
		userName.setColumns(20);
		userNamePane.add(userName);
		
		//방을 만드는 버튼
		JButton create = new JButton("Create Game");
		buttonPane.add(create);
		//방을 만드는 작업을 중지하고 waiting으로 돌아가는 버튼
		JButton enter = new JButton("Enter Game");
		buttonPane.add(enter);
		JButton cancle = new JButton("Cancle");
		buttonPane.add(cancle);
		
		create.addActionListener(new CActionListener(super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				game gameRunner = new game(id);
				try {
					bw.write("createGameRoom" + "\n");
					bw.flush();
					bw.close();
					br.close();
					socket.close();
					
				}catch(Exception e2) { };
				
				gameRunner.setWindow();
				
				
				mainWindow.dispose();
			}
		});
		
		enter.addActionListener(new CActionListener(super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				game gameRunner = new game(id);
				try {
					bw.write("enterRoom" + "\n");
					bw.flush();
					bw.close();
					br.close();
					socket.close();
					
				}catch(Exception e2) { };
				gameRunner.setWindow();
				mainWindow.dispose();
			}
		});
		cancle.addActionListener(new CActionListener(super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				waiting creator = new waiting(socket, super.bw, super.br, id);
				
				creator.setWindow();
				mainWindow.dispose();
			}
		});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
