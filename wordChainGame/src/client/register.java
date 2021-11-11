package client;
/*
 * 유저 회원가입을 위한 class입니다.
 *
 * */
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import javax.swing.*;

public class register extends CFrame{
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
		JLabel PWText = new JLabel("Password");
		PWText.setHorizontalAlignment(JLabel.LEFT);
		JTextField PW = new JTextField();
		PW.setColumns(12);
		
		JPanel RePane = new JPanel();
		RePane.setLayout(fl);
		infomationPane.add(RePane);
		JLabel ReText = new JLabel("Confirm Password");
		JTextField PWCheck = new JTextField();
		PWCheck.setColumns(12);
		
		JPanel NickNamePane = new JPanel();
		NickNamePane.setLayout(fl);
		infomationPane.add(NickNamePane);
		JLabel NickNameText = new JLabel("Nickname");
		JTextField NickName = new JTextField();
		NickName.setColumns(12);
		JButton NickDuplicate = new JButton("Check");
		
		JPanel NamePane = new JPanel();
		NamePane.setLayout(fl);
		infomationPane.add(NamePane);
		JLabel NameText = new JLabel("Name");
		NameText.setHorizontalAlignment(JLabel.LEFT);
		JTextField Name = new JTextField();
		Name.setColumns(12);
		
		JPanel EMailPane = new JPanel();
		EMailPane.setLayout(fl);
		infomationPane.add(EMailPane);
		JLabel EMailText = new JLabel("E-Mail");
		EMailText.setHorizontalAlignment(JLabel.LEFT);
		JTextField EMail = new JTextField();
		EMail.setColumns(12);
		
		JPanel SNSPane = new JPanel();
		SNSPane.setLayout(fl);
		infomationPane.add(SNSPane);
		JLabel SNSText = new JLabel("SNS");
		SNSText.setHorizontalAlignment(JLabel.LEFT);
		JTextField SNS = new JTextField();
		SNS.setColumns(12);
		
		
		IDPane.add(IDText);
		IDPane.add(ID);
		IDPane.add(checkDuplicate);
		PWPane.add(PWText);
		PWPane.add(PW, BorderLayout.WEST);
		RePane.add(ReText, BorderLayout.WEST);
		RePane.add(PWCheck, BorderLayout.WEST);
		NickNamePane.add(NickNameText, BorderLayout.WEST);
		NickNamePane.add(NickName, BorderLayout.WEST);
		NickNamePane.add(NickDuplicate, BorderLayout.WEST);
		NamePane.add(NameText, BorderLayout.WEST);
		NamePane.add(Name, BorderLayout.WEST);
		NamePane.add(Name, BorderLayout.WEST);
		EMailPane.add(EMailText, BorderLayout.WEST);
		EMailPane.add(EMail, BorderLayout.WEST);
		SNSPane.add(SNSText, BorderLayout.WEST);
		SNSPane.add(SNS, BorderLayout.WEST);

		
		//Footer
		buttonPane.setLayout(new FlowLayout());
		JButton Enter = new JButton("Submit");
		buttonPane.add(Enter);
		
		Enter.addActionListener(new CActionListener(super.bw, super.br) {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				/* ID, PW, NickName, Name, EMail, SNS 순입니다 */
				String result = "";
				String q = "REG " +
							ID.getText().replaceAll(" ", "") + 
							PW.getText().replaceAll(" ", "") + 
							NickName.getText().replaceAll(" ", "") + 
							Name.getText().replaceAll(" ", "") + 
							EMail.getText().replaceAll(" ", "") + 
							SNS.getText().replaceAll(" ", "");
							
				try {
					bw.write(q + "\n");
					bw.flush();
					result = br.readLine();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				
				login loginSection = new login();
				
				loginSection.setWindow();
				mainWindow.dispose();
				
			}
		});
		
		
		/*
		new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				login loginSection = new login();
				
				loginSection.setWindow();
				mainWindow.dispose();
			}
		});
		*/
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
