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
		
		JButton Return = new JButton("Return");//Return ��ư
		mainWindow.add(Return);
		
		/*
		 * Return��ư�� ������ ���ȭ������ ���ư��� �����صξ����ϴ�.
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
