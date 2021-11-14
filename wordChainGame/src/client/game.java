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
import java.net.UnknownHostException;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class game extends CFrame{
	
	private Socket socket = null;
	private String ipNumber = "127.0.0.1";
	private String chatIpNumber = "127.0.0.1";
	private int chatPortNumber = 3000;
	private int portNumber = 3100;
	BufferedReader br = null;
	BufferedWriter bw = null;
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	private String playerName;
	private String otherPlayer;
	private String prevWord = "가";
	private int isWin = 1;
	private int isMyturn = 0;
	
	private JLabel timer = new JLabel("10초 남음");
	private JTextArea textForm = new JTextArea(50, 100);
	private JTextArea msgForm = new JTextArea(1, 40);//입력창
	JLabel title = new JLabel("상대방 대기중");
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
	
	public game(String id)
	{
		this.playerName = id;
		
		try {
			socket = new Socket(ipNumber, portNumber);
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
		}catch(UnknownHostException e){
			
		}catch(IOException e){
			
		}
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
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(title);
		
		// 전적
		JLabel record = new JLabel("전적 : N/A");
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
				Socket chatSocket = null;
				
				try {
					chatSocket = new Socket(chatIpNumber, chatPortNumber);
				}catch(UnknownHostException e1) {
					
				}catch(IOException e1) {
					
				}
				
				waiting waitingSection = new waiting(chatSocket, super.bw, super.br, playerName);
				
				waitingSection.setWindow();
				mainWindow.dispose();
			}
		});
		
		new gameClientReceiveThread(br).start();
		sendMsg(playerName, "myName");
	}
	
	private void sendMsg(String msg, String labelName)
	{
		try {
			String req = labelName + ":" + msg + "\n";
			System.out.println("Send : " + req);
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
						String[] tokens = null;
						
						if(msg != null)
						{
							tokens = msg.split(":");
						}
							
					if(tokens != null)
					{
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
							textForm.append(" 정답입니다\n");
							textForm.append("턴 종료\n");
							sendMsg("turnOver", "turnOver");
						}
						else if("wrong".equalsIgnoreCase(msg))
						{
							if(isMyturn == 1)
							{
								isWin = 0;
								sendMsg("iLose:" + playerName, "iLose");
							}
							else
							{
								isWin = 1;
							}
							
						}
						else if("timeOver".equalsIgnoreCase(tokens[0]))
						{
							if(playerName.equalsIgnoreCase(tokens[1]))
							{
								isWin = 0;
								sendMsg("iLose:" + playerName, "iLose");
							}
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
						//방 만들고 상대방이 들어온 경우
						else if("otherCame".equalsIgnoreCase(msg))
						{
							turnInitiallize(0);
							turnProgress();
						}
						//만들어진 방에 참여한 경우
						else if("enteredAndWait".equalsIgnoreCase(msg))
						{
							turnInitiallize(1);
						}
						//상대방이 입력한 답안을 받는경우
						else if("src".equalsIgnoreCase(tokens[0]))
						{
							prevWord = tokens[2];
							if(isMyturn == 0)
							{
								textForm.append(tokens[1] + " : " + tokens[2] + "\n");
							}
						}
						else if("otherID".equalsIgnoreCase(tokens[0]))
						{
							if(!playerName.equalsIgnoreCase(tokens[1]))
							{
								otherPlayer = tokens[1];
								textForm.append(otherPlayer + "님이 게임에 참가하였습니다.\n");
								title.setText(playerName + " vs. " + otherPlayer);
							}
						}
							
						msg = null;
					}
				}
			}catch(IOException e)
			{
				System.out.println(e.getMessage());
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
			int flag = 0;
			
			public void run() {
				
				timer.setText(i + "초 남음");
				i--;
				
				sendBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(flag == 0)
						{
							msgForm.setEnabled(false);
							timer.setText("상대턴");
							String answer = msgForm.getText();
							msgForm.setText("");
							textForm.append(playerName + " : " + answer + ":" + prevWord + "\n");
							System.out.println(answer);
							sendMsg(answer + ":" + prevWord + ":" + playerName, "answer");
							prevWord = answer;
							T.cancel();
							flag = 1;
						}
						
					}
				});
				
				if(i < 0)
				{
					timer.setText("시간초과");
					sendMsg("timeOver:" + playerName, "timeOver");
					msgForm.setEnabled(false);
					T.cancel();
				}
			}
		}, 0, 1000);
	}
	
	public void turnInitiallize(int key)
	{
		if(key == 1)
		{
			isMyturn = 0;
		}
		else if(key == 0)
		{
			isMyturn = 1;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
