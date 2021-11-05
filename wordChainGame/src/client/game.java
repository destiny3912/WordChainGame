package client;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class game extends CFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	private String[] users = {"bundang", "coolj"};
//	private Socket player;
	private String playerName;
	private String prevWord = null;
	private int isWin = 1;
	private int isMyturn = 0;
	
	private JLabel timer = new JLabel("10초 남음");
	private JTextArea textForm = new JTextArea(50, 100);
	private JTextArea msgForm = new JTextArea(1, 40);//입력창
	private JPanel headerPanel = new JPanel();
	private JPanel contentPanel = new JPanel();
	private JPanel contentResultPanel = new JPanel();
	private JPanel btnPanel = new JPanel();
	private JPanel footerPanel = new JPanel();
	private JButton exitBtn = new JButton("종료");
	private JButton Return = new JButton("대기실로");//Return 버튼
	private JButton sendBtn = new JButton("전송");
	private JLabel resultTitle = new JLabel("");
	private JLabel resultContent = new JLabel("");
	
	public game(BufferedWriter bw, BufferedReader br)
	{
		super.bw = bw;
		super.br = br;
	}

	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(1366, 768);
		mainWindow.setTitle("Game");
		mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainWindow.setLayout(new BorderLayout());//(3, 1));
		
		// Header		
		
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
		JLabel record = new JLabel("전적 : ");
		record.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		record.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(record);
		headerPanel.add(titlePanel);
		
		// Timer
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		headerPanel.add(timer);
	
		
		returnPanel.add(Return);
		headerPanel.add(returnPanel);
		
		// Content
		contentPanel.setLayout(new FlowLayout());
		textForm.setBackground(Color.WHITE);
		textForm.setEditable(false);
		textForm.setText("단어를 입력해주세요\n");
		contentPanel.add(textForm);
		
		// Content -- Result
		
		contentResultPanel.setLayout(new GridLayout(3, 1));
		
		resultTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		resultTitle.setHorizontalAlignment(SwingConstants.CENTER);
		resultTitle.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		contentResultPanel.add(resultTitle);
		
		
		resultContent.setVerticalAlignment(SwingConstants.TOP);
		resultContent.setHorizontalAlignment(SwingConstants.CENTER);
		resultContent.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		contentResultPanel.add(resultContent);
		
		
		btnPanel.setLayout(new FlowLayout());
		contentResultPanel.add(btnPanel);
		
		// Footer		
		footerPanel.setLayout(new FlowLayout());
		
		msgForm.setBackground(Color.WHITE);
		footerPanel.add(msgForm);
		
		
		footerPanel.add(sendBtn);
		
		mainWindow.add(headerPanel, BorderLayout.NORTH);
		mainWindow.add(contentPanel, BorderLayout.CENTER);
		mainWindow.add(footerPanel, BorderLayout.SOUTH);
		
		/*
		 * Return버튼을 누르면 대기화면으로 돌아가게 세팅해두었습니다.
		 * 
		 * */
		Return.addActionListener(new CActionListener(super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				waiting waitingSection = new waiting(super.bw, super.br);
				
				waitingSection.setWindow();
				mainWindow.dispose();
			}
		});
		
		new gameClientReceiveThread(br).start();
		//turnProgress();
	}
	
	private void sendMsg(String msg, String labelName)
	{
		try {
			String req = labelName + ":" + msg + "\n";
			bw.write(req);
			bw.flush();
		}catch(IOException IOe) {
			
		}
	}
	
	private class gameClientReceiveThread extends Thread
	{
		BufferedReader br;
		
		gameClientReceiveThread(BufferedReader br){
			this.br = br;
		}
		
		public void run() {
			try {				
				while(true) {
					String msg = br.readLine();
					
					String[] tokens = msg.split(":");
					
					if("end".equalsIgnoreCase(msg)) {
						btnPanel.add(Return);	// Return button moves to contentResultPanel--btnPanel from headerPanel--returnPanel
						btnPanel.add(exitBtn);
						
						if(isWin == 1)
						{
							resultTitle.setText("승리");
							sendMsg("iWin:" + playerName, "iWin");
						}
						else
						{
							resultTitle.setText("패배");
						}
						
						headerPanel.remove(timer);				
						mainWindow.remove(contentPanel);
						mainWindow.remove(footerPanel);
						mainWindow.add(contentResultPanel, BorderLayout.CENTER);
						SwingUtilities.updateComponentTreeUI(mainWindow);	// Refresh mainWindow
					}
					else if("correct".equalsIgnoreCase(tokens[0]))
					{
						prevWord = tokens[1];
						sendMsg("turnOver:" + playerName, "turnOver");
					}
					else if("wrong".equalsIgnoreCase(msg))
					{
						sendMsg("iLose:" + playerName, "iLose");
						isWin = 0;
					}
					else if("isYourTurn".equalsIgnoreCase(msg))
					{
						if(isMyturn == 0)
						{
							isMyturn = 1;
							turnProgress();
						}
						else if(isMyturn == 1)
						{
							isMyturn = 0;
						}
						
					}
				}
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 턴 진행 메서드
	 * */
	public void turnProgress()
	{
		Timer T = new Timer();
		msgForm.setEnabled(true);
		
		T.scheduleAtFixedRate(new TimerTask() {
			int i = 10;
			
			public void run() {
				
				timer.setText(i + "초 남음");
				i--;
				
				sendBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						T.cancel();
						
						msgForm.setEnabled(false);
						timer.setText("");
						String answer = msgForm.getText();
						msgForm.setText("");
						textForm.append(playerName + " : " + answer + ":" + prevWord + "\n");
						sendMsg(answer, "answer");
					}
				});
				
				if(i < 0)
				{
					timer.setText("시간초과");
					sendMsg("end", "end");
					msgForm.setEnabled(false);
					T.cancel();
				}
			}
		}, 0, 1000);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
