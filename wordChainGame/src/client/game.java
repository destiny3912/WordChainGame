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
	private String ipNumber = "localhost";//���Ӽ��� ip
	private String chatIpNumber = "localhost"; //ä�ü��� ip
	private int chatPortNumber = 3000;// ä�ü��� ��Ʈ
	private int portNumber = 3100;//���Ӽ��� ��Ʈ
	BufferedReader br = null;
	BufferedWriter bw = null;
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	private String playerName;
	private String otherPlayer;
	private String prevWord = "first";
	private int isWin = 1;// 1�̸� �̱�� 0�̸� ����
	private int isMyturn = 0;// 1�̸� �ڽ��� ��, 0�̸� ������
	
	private JLabel timer = new JLabel("10�� ����");
	private JTextArea textForm = new JTextArea(50, 50);
	private JTextArea chatForm = new JTextArea(50, 50);
	private JTextArea msgForm = new JTextArea(1, 40);//�Է�â
	private JTextArea chatMsgForm = new JTextArea(1, 40);
	JLabel title = new JLabel("���� �����");
	private JPanel headerPanel = new JPanel();
	private JPanel contentPanel = new JPanel();
	private JPanel contentResultPanel = new JPanel();
	private JPanel btnPanel = new JPanel();
	private JPanel footerPanel = new JPanel();
	private JButton exitBtn = new JButton("����");
	private JButton Return = new JButton("���Ƿ�");//Return ��ư
	private JButton sendBtn = new JButton("����");
	private JButton chatBtn = new JButton("����");
	private JLabel resultTitle = new JLabel("");
	private JLabel resultContent = new JLabel("");
	private JLabel record = new JLabel("���� : N/A");
	
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
		title.setFont(new Font("���� ���", Font.BOLD, 20));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(title);
		
		// ����
		record.setFont(new Font("���� ���", Font.BOLD, 12));
		record.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(record);
		headerPanel.add(titlePanel);
		
		// Timer
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("���� ���", Font.BOLD, 20));
		headerPanel.add(timer);
		returnPanel.add(Return);
		headerPanel.add(returnPanel);
		
		// Content
		contentPanel.setLayout(new FlowLayout());
		textForm.setBackground(Color.WHITE);
		textForm.setEditable(false);
		chatForm.setEditable(false);
		textForm.setText("�����ձ� ������ �Է����ּ���\n");
		chatForm.setText("ä���� �Է��� �ּ���\n");
		contentPanel.add(textForm);
		contentPanel.add(chatForm);
		
		// Content -- Result
		contentResultPanel.setLayout(new GridLayout(3, 1));
		
		resultTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		resultTitle.setHorizontalAlignment(SwingConstants.CENTER);
		resultTitle.setFont(new Font("���� ���", Font.BOLD, 25));
		contentResultPanel.add(resultTitle);
		
		
		resultContent.setVerticalAlignment(SwingConstants.TOP);
		resultContent.setHorizontalAlignment(SwingConstants.CENTER);
		resultContent.setFont(new Font("���� ���", Font.BOLD, 20));
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
		 * Return��ư�� ������ ���ȭ������ ���ư��� �����صξ����ϴ�.
		 * 
		 * ���⿡�� ��ư�� ������ ���� �������� ��Ʈ���� �ݰ� ä�� �������� ��Ʈ���� ��� �ٷ�
		 * waiting���� �Ѿ�� ������ �ֽø� �����ϰڽ��ϴ�.
		 * ������ �ʿ��� ip�� port��ȣ�� �ֻ�ܿ� �ֽ��ϴ�.
		 * */
		Return.addActionListener(new CActionListener(super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// ������ �ݴ´ٴ� �޼����̸� �濡�� ������ �۾��� �����մϴ�.
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
					System.out.println("Waiting���� ���ư��� ���� Stream ���� �� Exception �߻�");
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
					JOptionPane.showMessageDialog(null, "�� �� ���� ����.", "����", JOptionPane.ERROR_MESSAGE); 
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
								resultTitle.setText("�¸�");
								sendMsg("iWin:" + playerName, "iWin");
							}
							else
							{
								resultTitle.setText("�й�");
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
							textForm.append(" �����Դϴ�\n");
							textForm.append("�� ����\n");
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
						//�������� ���� �Ѿ������ �˷��ִ� ���
						else if("isYourTurn".equalsIgnoreCase(msg))
						{
							//������ �� �����ڰ� �ڽ��� �ƴҶ�
							if(isMyturn == 0)
							{
								isMyturn = 1;
								turnProgress();
							}
							//������ �� �����ڰ� �ڽ��϶�
							else if(isMyturn == 1)
							{
								isMyturn = 0;
							}
							
						}
						//�� ����� ������ ���� ���
						else if("otherCame".equalsIgnoreCase(msg))
						{
							turnInitiallize(0);
							turnProgress();
						}
						//������� �濡 ������ ���
						else if("enteredAndWait".equalsIgnoreCase(msg))
						{
							turnInitiallize(1);
						}
						//������ �Է��� ����� �޴°��
						else if("src".equalsIgnoreCase(tokens[0]))
						{
							prevWord = tokens[2];
							if(isMyturn == 0)
							{
								textForm.append(tokens[1] + " : " + tokens[2] + "\n");
							}
						}
						//������ �г����� �޾ƿ��� ���
						else if("otherID".equalsIgnoreCase(tokens[0]))
						{
							if(!playerName.equalsIgnoreCase(tokens[1]))
							{
								otherPlayer = tokens[1];
								textForm.append(otherPlayer + "���� ���ӿ� �����Ͽ����ϴ�.\n");
								title.setText(playerName + " vs. " + otherPlayer);
								sendMsg(playerName, "giveMyName");
							}
						}
						//�������� ����(����)�� �̸��� �ٶ�
						else if("giveOnwerName".equalsIgnoreCase(tokens[0]))
						{
							if(!playerName.equalsIgnoreCase(tokens[1]))
							{
								otherPlayer = tokens[1];
								title.setText(playerName + " vs. " + otherPlayer);
							}
						}
						//������ ä���� ������ �������� �� ������ �޾ƿ��� ���
						else if("otherChat".equalsIgnoreCase(tokens[0]))
						{
							if(!playerName.equalsIgnoreCase(tokens[2]))
							{
								chatMsgForm.append(tokens[2] + " : " + tokens[1] + "\n");
							}
						}
						//�������� �ڽ��� ������ �޾ƿ��� ���
						else if("total".equalsIgnoreCase(tokens[0]))
						{
							record.setText(tokens[1]);
						}
						//�ڽ��� �� ���� ��ȣ�� �޾ƿ��� ���
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
	 * �� ���� �޼���
	 * */
	public void turnProgress()
	{
		Timer T = new Timer();
		msgForm.setEnabled(true);
		
		T.scheduleAtFixedRate(new TimerTask() {
			int i = 10;
			int flag = 0;
			
			public void run() {
				
				timer.setText(i + "�� ����");
				i--;
				
				sendBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(flag == 0)
						{
							msgForm.setEnabled(false);
							timer.setText("�����");
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
					timer.setText("�ð��ʰ�");
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
