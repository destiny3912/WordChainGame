package client;
/*
 * 상대방의 아이디나 닉네임을 key로 1대1 게임을 만드는 class입니다.
 * */
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
		mainWindow.setTitle("Create Game");
		mainWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mainWindow.setLayout(new BorderLayout());
		mainWindow.setResizable(false);
		
		JPanel userNamePane = new JPanel();
		JPanel buttonPane = new JPanel();
		
		mainWindow.add(userNamePane, BorderLayout.NORTH);
		mainWindow.add(buttonPane, BorderLayout.SOUTH);
		
		//상대 user의 이름을 작성하는 작성란
		JTextField userName = new JTextField("Enter user name");
		userName.setColumns(20);
		userNamePane.add(userName);
		
		//방을 만드는 버튼
		JButton submit = new JButton("Subimt");
		buttonPane.add(submit);
		//방을 만드는 작업을 중지하고 waiting으로 돌아가는 버튼
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
