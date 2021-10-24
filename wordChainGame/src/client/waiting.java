package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class waiting extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	private String userName = "Test";
	
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
		JTextArea chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setColumns(50);
		chatBox.setRows(50);
		JTextArea ranking = new JTextArea();
		ranking.setEditable(false);
		ranking.setColumns(20);
		ranking.setRows(50);
		JTextField chat = new JTextField();
		JButton chatSubmit = new JButton("Submit");
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
		
		chatSubmit.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String chatData;
				chatData = chat.getText();
				chat.setText("");
				chatBox.append(userName + " : " + chatData + "\n");
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
