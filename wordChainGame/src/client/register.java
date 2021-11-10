package client;
/*
 * 유저 회원가입을 위한 class입니다.
 *
 * */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class register extends JFrame{
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	
	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(300, 300);
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
		
		//Header
		descriptionPane.setLayout(new FlowLayout());
		JLabel description = new JLabel("Enter infomation to register");
		descriptionPane.add(description);
		description.setHorizontalAlignment(JLabel.CENTER);
		
		//main text box to get user information
		infomationPane.setLayout(new GridLayout(4,1));
		
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		
		JPanel IDPane = new JPanel();
		IDPane.setLayout(fl);
		infomationPane.add(IDPane);
		JLabel IDText = new JLabel("ID");
		JTextField ID = new JTextField();
		ID.setColumns(12);
		JButton checkDuplicate = new JButton("Check");
		
		JPanel PWPane = new JPanel();
		PWPane.setLayout(fl);
		infomationPane.add(PWPane);
		JLabel PWText = new JLabel("PW");
		PWText.setHorizontalAlignment(JLabel.LEFT);
		JTextField PW = new JTextField();
		PW.setColumns(12);
		
		JPanel RePane = new JPanel();
		RePane.setLayout(fl);
		infomationPane.add(RePane);
		JLabel ReText = new JLabel("Repeat");
		JTextField PWCheck = new JTextField();
		PWCheck.setColumns(12);
		
		JPanel NickNamePane = new JPanel();
		NickNamePane.setLayout(fl);
		infomationPane.add(NickNamePane);
		JLabel NameText = new JLabel("Nick Name");
		JTextField NickName = new JTextField();
		NickName.setColumns(12);
		JButton NickDuplicate = new JButton("Check");
		
		IDPane.add(IDText);
		IDPane.add(ID);
		IDPane.add(checkDuplicate);
		PWPane.add(PWText);
		PWPane.add(PW, BorderLayout.WEST);
		RePane.add(ReText, BorderLayout.WEST);
		RePane.add(PWCheck, BorderLayout.WEST);
		NickNamePane.add(NameText, BorderLayout.WEST);
		NickNamePane.add(NickName, BorderLayout.WEST);
		NickNamePane.add(NickDuplicate, BorderLayout.WEST);
		
		//Footer
		buttonPane.setLayout(new FlowLayout());
		JButton Enter = new JButton("Subimt");
		buttonPane.add(Enter);
		
		Enter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				login loginSection = new login();
				
				loginrSection.setWindow();
				mainWindow.dispose();
			}
		});
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
