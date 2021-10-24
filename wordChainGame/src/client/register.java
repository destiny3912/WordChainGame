package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class register extends JFrame{
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	
	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(300, 400);
		mainWindow.setTitle("Register");
		mainWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mainWindow.setLayout(new BorderLayout());
		mainWindow.setResizable(false);
		
		JPanel descriptionPane = new JPanel();
		JPanel infomationPane = new JPanel();
		JPanel buttonPane = new JPanel();
		
		mainWindow.add(descriptionPane, BorderLayout.NORTH);
		mainWindow.add(infomationPane, BorderLayout.CENTER);
		mainWindow.add(buttonPane, BorderLayout.SOUTH);
		
		descriptionPane.setLayout(new FlowLayout());
		JLabel description = new JLabel("Enter infomation to register");
		descriptionPane.add(description);
		description.setHorizontalAlignment(JLabel.CENTER);
		
		infomationPane.setLayout(new FlowLayout());
		
		
		buttonPane.setLayout(new FlowLayout());
		JButton Enter = new JButton("Subimt");
		buttonPane.add(Enter);
		
		Enter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				login loginrSection = new login();
				
				loginrSection.setWindow();
				mainWindow.dispose();
			}
		});
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
