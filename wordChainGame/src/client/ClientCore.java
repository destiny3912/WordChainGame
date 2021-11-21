package client;

import java.io.*;
import java.net.*;
import javax.swing.JTextArea;

public class ClientCore extends Thread {
	private BufferedWriter bw = null;
	private BufferedReader br = null;
	private JTextArea chatBox = null;
	private JTextArea userList = null;
	
    public ClientCore(BufferedWriter bw, BufferedReader br) {
    	this.bw = bw;
    	this.br = br;
	}
    
    public void setChatBox(JTextArea chatBox) {
    	this.chatBox = chatBox;
    }
    
    public void setUserList(JTextArea userList) {
    	this.userList = userList;
    }

	@Override
	public void run() {
		try {
			String msg = "";

			while (!msg.equals("bye")) {
				msg = br.readLine();
				String protocol = msg.substring(0, 3);
				
				// 채팅 수신의 경우
				if(protocol.equals("MSG"))	{
					chatBox.append(msg.substring(4) + "\n");
				}
				
				//	사용자 리스트를 받는 경우
				else if(protocol.equals("USR"))	{
					String[] users = msg.substring(4).split(" ");					
					userList.setText("");
					for(int i = 0; i < users.length; i++) {
						userList.append(users[i] + "\n");
					}

				}
				
				else if(protocol.equals("DEU"))	{
					String usersStr = userList.getText();
					String delUserId = msg.substring(4);
					userList.setText(usersStr.replaceAll(delUserId + "\n", ""));
				}
			}

		} catch (Exception e) { } finally {
			try {
				br.close();
				bw.close();
			} catch (Exception e) { }
		}
	}
}