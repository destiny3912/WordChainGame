package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class createGame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	
	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(500, 150);
		mainWindow.setTitle("Waiting");
		mainWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mainWindow.setLayout(new BorderLayout());
		
		JPanel userNamePane = new JPanel();
		JPanel buttonPane = new JPanel();
		
		mainWindow.add(userNamePane, BorderLayout.NORTH);
		mainWindow.add(buttonPane, BorderLayout.SOUTH);
		
		JTextField userName = new JTextField("Enter user name");
		userName.setColumns(20);
		userNamePane.add(userName);
		
		JButton submit = new JButton("Subimt");
		buttonPane.add(submit);
		JButton cancle = new JButton("Cancle");
		buttonPane.add(cancle);
		
		submit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				game gameRunner = new game();
				
				gameRunner.setWindow();
				mainWindow.dispose();
			}
		});
		
		cancle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				waiting creator = new waiting();
				
				creator.setWindow();
				mainWindow.dispose();
			}
		});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
