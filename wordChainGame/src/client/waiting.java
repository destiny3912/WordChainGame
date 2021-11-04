package client;
/*
 * 좌측에 접속유저 우측에 랭킹을 표시하고 중앙에 채팅창을 만들어 두었습니다.
 * */
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.*;

public class waiting extends CFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	
	public waiting(BufferedWriter bw, BufferedReader br) {
		// TODO Auto-generated constructor stub
		super.bw = bw;
		super.br = br;
	}

	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(1200, 800);
		mainWindow.setTitle("Waiting");
		mainWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mainWindow.setLayout(new BorderLayout());
		mainWindow.setResizable(false);
		
		JPanel descriptionPane = new JPanel();
		descriptionPane.setSize(1200,50);
		descriptionPane.setLayout(new BorderLayout());
		JPanel userListPane = new JPanel();
		userListPane.setSize(100,700);
		JPanel chatBoxPane = new JPanel();
		chatBoxPane.setSize(500,700);
		JPanel rankingPane = new JPanel();
		rankingPane.setSize(200,700);
		JPanel chatPane = new JPanel();
		chatPane.setSize(1200,50);
		
		mainWindow.add(descriptionPane, BorderLayout.NORTH);
		mainWindow.add(userListPane, BorderLayout.WEST);
		mainWindow.add(chatBoxPane, BorderLayout.CENTER);
		mainWindow.add(rankingPane, BorderLayout.EAST);
		mainWindow.add(chatPane, BorderLayout.SOUTH);
		
		JLabel description = new JLabel("Lobby");
		JLabel userSectionDes = new JLabel("Current User");
		JLabel rankingSectionDes = new JLabel("Ranking");
		JTextArea userList = new JTextArea();
		userList.setEditable(false);
		userList.setColumns(20);
		userList.setRows(50);
		//채팅창
		JTextArea chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setColumns(50);
		chatBox.setRows(50);
		JTextArea ranking = new JTextArea();
		ranking.setEditable(false);
		ranking.setColumns(20);
		ranking.setRows(50);
		//채팅 작성창
		JTextField chat = new JTextField();
		//채팅 전송 버튼
		JButton chatSubmit = new JButton("Submit");
		//게임 room을 만드는 버튼 -> createGame class의 기능을 실행하기위한 트리거
		JButton createGame = new JButton("Make room");
		
		descriptionPane.add(description, BorderLayout.NORTH);
		descriptionPane.add(userSectionDes, BorderLayout.WEST);
		descriptionPane.add(rankingSectionDes, BorderLayout.EAST);
		chat.setColumns(30);
		userListPane.add(userList);
		chatBoxPane.add(chatBox);
		rankingPane.add(ranking);
		chatPane.add(chat);
		chatPane.add(chatSubmit);
		chatPane.add(createGame);
		
		ClientCore client = new ClientCore(bw, br);
		client.setChatBox(chatBox);
		client.setUserList(userList);
		client.start();
	
		chatSubmit.addActionListener(new CActionListener(super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String chatData;
				chatData = chat.getText();
				chat.setText("");
				//chatBox.append(userName + " : " + chatData + "\n");
				
				try {
					bw.write(chatData + "\n");
					bw.flush();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
		});
		
		createGame.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createGame creator = new createGame();
				
				creator.setWindow();
				mainWindow.dispose();
			}
			
		});
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
