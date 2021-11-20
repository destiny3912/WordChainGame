package client;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class game extends CFrame{
	
	private Socket socket = null;
	private String ipNumber = "localhost";//게임서버 ip
	private String chatIpNumber = "localhost"; //채팅서버 ip
	private int chatPortNumber = 3000;// 채팅서버 포트
	private int portNumber = 3100;//게임서버 포트
	BufferedReader br = null;
	BufferedWriter bw = null;
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	private String playerName;
	private String otherPlayer;
	private String prevWord = "first";
	private int isWin = 1;// 1이면 이긴것 0이면 진것
	private int isMyturn = 0;// 1이면 자신의 턴, 0이면 상대방턴
	
	private JLabel timer = new JLabel("10초 남음");
	private JTextArea textForm = new JTextArea(50, 50);
	private JTextArea chatForm = new JTextArea(50, 50);
	private JTextArea msgForm = new JTextArea(1, 40);//입력창
	private JTextArea chatMsgForm = new JTextArea(1, 40);
	JLabel title = new JLabel("상대방 대기중");
	private JPanel headerPanel = new JPanel();
	private JPanel contentPanel = new JPanel();
	private JPanel contentResultPanel = new JPanel();
	private JPanel btnPanel = new JPanel();
	private JPanel footerPanel = new JPanel();
	private JButton exitBtn = new JButton("종료");
	private JButton Return = new JButton("대기실로");//Return 버튼
	private JButton sendBtn = new JButton("전송");
	private JButton chatBtn = new JButton("전송");
	private JLabel resultTitle = new JLabel("");
	private JLabel resultContent = new JLabel("");
	private JLabel record = new JLabel("전적 : N/A");
	
	public game(String id)
	{
		this.playerName = id;
		
		try {
			socket = new Socket(ipNumber, portNumber);
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) { }
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
		chatForm.setEditable(false);
		textForm.setText("끝말잇기 정답을 입력해주세요\n");
		chatForm.setText("채팅을 입력해 주세요\n");
		contentPanel.add(textForm);
		contentPanel.add(chatForm);
		
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
		footerPanel.add(chatMsgForm);
		footerPanel.add(chatBtn);

		mainWindow.add(headerPanel, BorderLayout.NORTH);
		mainWindow.add(contentPanel, BorderLayout.CENTER);
		mainWindow.add(footerPanel, BorderLayout.SOUTH);
		
		
		chatBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String chatString = chatMsgForm.getText();
				chatMsgForm.setText("");
				chatForm.append(playerName + " : " + chatString + "\n");
				
				sendMsg(chatString + ":" + playerName, "chatText");
			}
		});
		
		/*
		 * Return버튼을 누르면 대기화면으로 돌아가게 세팅해두었습니다.
		 * 
		 * 여기에서 버튼을 누리면 게임 서버와의 스트림을 닫고 채팅 서버와의 스트림을 열어서 바로
		 * waiting으로 넘어가게 구현해 주시면 감사하겠습니다.
		 * 구현에 필요한 ip와 port번호는 최상단에 있습니다.
		 * */
		Return.addActionListener(new CActionListener(super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// 서버를 닫는다는 메세지이며 방에서 나가는 작업도 병행합니다.
				sendMsg(playerName, "serverClose");
				
				Socket chatSocket = null;
				
				try {
					if (super.br != null) super.br.close();
					if (super.bw != null) super.bw.close();
					if (super.socket != null) super.socket.close();
				}catch(UnknownHostException e1) {
				}catch(IOException e1) {
				}
				
				try {
					chatSocket = new Socket(chatIpNumber, chatPortNumber);
					System.out.println(chatSocket);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				BufferedWriter chatBw = null;
				BufferedReader chatBr = null;
				
				String q = "REL " + playerName;
				String result = "";
				try {
					chatBw = new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream()));
					chatBr = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
					chatBw.write(q + "\n");
					chatBw.flush();
					result = chatBr.readLine();
				} catch (IOException ex)	{
					ex.printStackTrace();
					System.out.println("Waiting으로 돌아가기 위해 Stream 여는 중 Exception 발생");
				}
				
				if (result.substring(0, 3).equals("WEL")) {
					String resultTokens[] = result.split(" ");
					playerName = new String(resultTokens[1]);
					waiting waitingSection = new waiting(chatSocket, chatBw, chatBr, playerName);
					System.out.println(playerName);
					waitingSection.setWindow();
					mainWindow.dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "알 수 없는 오류.", "오류", JOptionPane.ERROR_MESSAGE); 
				}

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
							headerPanel.remove(title);
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
						//서버에서 턴이 넘어왔음을 알려주는 경우
						else if("isYourTurn".equalsIgnoreCase(msg))
						{
							//직전의 턴 종료자가 자신이 아닐때
							if(isMyturn == 0)
							{
								isMyturn = 1;
								turnProgress();
							}
							//직전의 턴 종료자가 자신일때
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
						//상대방의 닉네임을 받아오는 경우
						else if("otherID".equalsIgnoreCase(tokens[0]))
						{
							if(!playerName.equalsIgnoreCase(tokens[1]))
							{
								otherPlayer = tokens[1];
								textForm.append(otherPlayer + "님이 게임에 참가하였습니다.\n");
								title.setText(playerName + " vs. " + otherPlayer);
								sendMsg(playerName, "giveMyName");
							}
						}
						//서버에서 상대방(방장)의 이름을 줄때
						else if("giveOnwerName".equalsIgnoreCase(tokens[0]))
						{
							if(!playerName.equalsIgnoreCase(tokens[1]))
							{
								otherPlayer = tokens[1];
								title.setText(playerName + " vs. " + otherPlayer);
							}
						}
						//상대방이 채팅을 했을떄 서버에서 그 내용을 받아오는 경우
						else if("otherChat".equalsIgnoreCase(tokens[0]))
						{
							if(!playerName.equalsIgnoreCase(tokens[2]))
							{
								chatMsgForm.append(tokens[2] + " : " + tokens[1] + "\n");
							}
						}
						//서버에서 자신의 전적을 받아오는 경우
						else if("total".equalsIgnoreCase(tokens[0]))
						{
							record.setText(tokens[1]);
						}
						//자신이 들어간 방의 번호를 받아오는 경우
						else if("roomNumber".equalsIgnoreCase(tokens[0]))
						{
							mainWindow.setTitle("Game room : " + tokens[1]);
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
							textForm.append(playerName + " : " + answer + "\n");
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
}
