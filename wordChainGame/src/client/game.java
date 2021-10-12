package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class game extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	
	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(500, 400);
		mainWindow.setTitle("Game");
		mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainWindow.setLayout(new FlowLayout());
		
		JButton Return = new JButton("Return");//Return 버튼
		mainWindow.add(Return);
		
		/*
		 * Return버튼을 누르면 대기화면으로 돌아가게 세팅해두었습니다.
		 * 
		 * */
		Return.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				waiting waitingSection = new waiting();
				
				waitingSection.setWindow();
				mainWindow.dispose();
			}
		});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
