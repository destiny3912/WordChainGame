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
		mainWindow.setLayout(new GridLayout(3,1));
		mainWindow.setResizable(false);
		
		//Header
		JPanel DescriptionPane = new JPanel();
		DescriptionPane.setSize(500, 200);
		JPanel textFeildPane = new JPanel();
		
		textFeildPane.setLayout(new GridLayout(2,2));
		textFeildPane.setSize(500, 100);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.setSize(500, 100);
		
		mainWindow.add(DescriptionPane);
		mainWindow.add(textFeildPane);
		mainWindow.add(buttonPane);
		
		JLabel DesText = new JLabel("Word chain game");
		JLabel IDText = new JLabel("ID : ");
		JLabel PWText = new JLabel("PW : ");
		DesText.setHorizontalAlignment(JLabel.CENTER);
		
		DescriptionPane.add(DesText);
		
		//Center -> input id and pw textField
		JPanel IDPane = new JPanel();
		JPanel PWPane = new JPanel();
		IDPane.setSize(250,10);
		PWPane.setSize(250,10);
		
		IDPane.setLayout(new FlowLayout());
		PWPane.setLayout(new FlowLayout());
		
		JTextField ID = new JTextField("ID");
		JTextField PW = new JTextField("PW");
		ID.setColumns(12);
		PW.setColumns(12);
		IDPane.add(IDText);
		IDPane.add(ID);
		PWPane.add(PWText);
		PWPane.add(PW);
		
		textFeildPane.add(IDPane);
		textFeildPane.add(PWPane);
		
		ID.revalidate();
		ID.repaint();
		PW.revalidate();
		PW.repaint();
		
		//Footer
		JButton Register = new JButton("Registrer");
		buttonPane.add(Register);
		Register.revalidate();
		Register.repaint();
		
		JButton Login = new JButton("Login");
		buttonPane.add(Login);
		Login.revalidate();
		Login.repaint();
		
		Register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				register registerSection = new register();
				
				registerSection.setWindow();
				mainWindow.dispose();
			}
		});
		Login.addActionListener(new ActionListener() {
			
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
