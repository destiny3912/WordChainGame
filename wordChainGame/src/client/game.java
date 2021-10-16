package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class game extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	private String[] users = {"bundang", "coolj"};
	
	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(1366, 768);
		mainWindow.setTitle("Game");
		mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainWindow.setLayout(new BorderLayout());//(3, 1));
		
		// Header		
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(1, 3));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(2, 1));
		
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		// USER ID
		JLabel title = new JLabel(users[0] + " vs. " + users[1]);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(title);
		
		// 전적
		JLabel record = new JLabel("(3승 1패 1무) (1승 1패 0무)");
		record.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		record.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(record);
		headerPanel.add(titlePanel);
		
		// Timer
		JLabel timer = new JLabel("3초 남음");
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		headerPanel.add(timer);
		
		JButton Return = new JButton("대기실로");//Return 버튼
		returnPanel.add(Return);
		headerPanel.add(returnPanel);
		
		// Content
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new FlowLayout());
		JTextArea textForm = new JTextArea(50, 100);
		textForm.setBackground(Color.WHITE);
		textForm.setEditable(false);
		textForm.setText("[bundang] 김치\n[coolj] 치약\n[bundang] 약술\n");
		contentPanel.add(textForm);
		
		// Content -- Result
		JPanel contentResultPanel = new JPanel();
		contentResultPanel.setLayout(new GridLayout(3, 1));
		JLabel resultTitle = new JLabel("패배!");
		resultTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		resultTitle.setHorizontalAlignment(SwingConstants.CENTER);
		resultTitle.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		contentResultPanel.add(resultTitle);
		
		JLabel resultContent = new JLabel("cooj님이 이겼습니다!");
		resultContent.setVerticalAlignment(SwingConstants.TOP);
		resultContent.setHorizontalAlignment(SwingConstants.CENTER);
		resultContent.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		contentResultPanel.add(resultContent);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout());

		JButton exitBtn = new JButton("종료");
		contentResultPanel.add(btnPanel);
		
		// Footer		
		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new FlowLayout());
		
		JTextArea msgForm = new JTextArea(1, 40);
		msgForm.setBackground(Color.WHITE);
		footerPanel.add(msgForm);
		
		JButton sendBtn = new JButton("전송");
		footerPanel.add(sendBtn);
		
		
		mainWindow.add(headerPanel, BorderLayout.NORTH);
		mainWindow.add(contentPanel, BorderLayout.CENTER);
		mainWindow.add(footerPanel, BorderLayout.SOUTH);
		
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
		
		/* 게임 결과창을 확인할 수 있게, sendBtn을 누르면 무조건 패배하도록 임시로 세팅해두었습니다. */
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				btnPanel.add(Return);	// Return button moves to contentResultPanel--btnPanel from headerPanel--returnPanel
				btnPanel.add(exitBtn);
				
				headerPanel.remove(timer);				
				mainWindow.remove(contentPanel);
				mainWindow.remove(footerPanel);
				mainWindow.add(contentResultPanel, BorderLayout.CENTER);
				SwingUtilities.updateComponentTreeUI(mainWindow);	// Refresh mainWindow

			}
		});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
