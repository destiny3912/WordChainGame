package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class login extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	
	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(500, 400);
		mainWindow.setTitle("Login");
		mainWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mainWindow.setLayout(new FlowLayout());
		
		JButton Enter = new JButton("Enter");
		mainWindow.add(Enter);
		
		/*
		 * Enter ��ư�� ������ ��� ȭ������ �Ѿ�� �����߽��ϴ�.
		 * */
		Enter.addActionListener(new ActionListener() {
			
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
