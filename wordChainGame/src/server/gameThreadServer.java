package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.*;

public class gameThreadServer extends Thread{
	
	private BufferedReader br = null;
	private BufferedWriter bw = null;
	private String strId = null;
	private Socket socket = null;
	private gameRoom room = null;
	private DBServer DB = null;
	
	//������ �����Ҷ� socket�� �޾� stream�� establish�մϴ�.
	public gameThreadServer(Socket socket, gameRoom room)
	{
		this.socket = socket;
		this.room = room;
		this.DB = new DBServer();
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) { }
	}
	
	/*public gameThreadServer(BufferedReader br, BufferedWriter bw, gameRoom room)
	{
		this.room = room;
		this.DB = new DBServer();
		this.br = br;
		this.bw = bw;
	}*/
	@Override
	public void run() {
		
		try {
			while(true)
			{
				String req;
				
				req = br.readLine();
				System.out.println("Received : " + req);
				if(req == null)
				{
					System.out.println("Ŭ���̾�Ʈ�� ������ ������ϴ�");
					break;
				}
				
				String[] tokens = null;
				tokens = req.split(":");
				//���� ������
				if("turnOver".equalsIgnoreCase(tokens[0]))
				{
					sendMessage("isYourTurn");
					tokens = null;
				}
				//�ð��ʰ�
				else if("end".equalsIgnoreCase(tokens[0]))
				{
					room.broadcast("end");
					tokens = null;
					//��� Ŭ���̾�Ʈ���� ���� ���� end response
				}
				else if("answer".equalsIgnoreCase(tokens[0]))
				{
					String prev, ans, srcPlayer;
					prev = tokens[2];
					ans = tokens[1];
					srcPlayer = tokens[3];
					
					room.broadcast("src:" + srcPlayer + ":" + ans);
						//�����ΰ��
						if(prev.charAt(prev.length() - 1) == ans.charAt(0))
						{
							room.broadcast("correct:" + ans);
						}
						//�����ΰ��
						else
						{
							room.broadcast("wrong");
						}
						tokens = null;
					//1���� 2�� ��ū���� �����ձ� �����ϸ� correct response
					//Ʋ�ȴٸ� end response
				}
				else if("iWin".equalsIgnoreCase(tokens[0]))
				{
					String query;
					query = "UPDATE userinfo SET win = win + 1 WHERE ID =?";
					DB.updateScore(query, strId);
					tokens = null;
					//DB�� tokens[1]�� �̸������ϴ� �÷��̾��� �¸� ī��Ʈ + 1
				}
				else if("iLose".equalsIgnoreCase(tokens[0]))
				{
					room.broadcast("end");
					String query;
					query = "UPDATE userinfo SET lose = lose + 1 WHERE ID =?";
					DB.updateScore(query, strId);
					tokens = null;
					//DB�� tokens[1]�� �̸������ϴ� �÷��̾��� �й� ī��Ʈ + 1
				}
				else if("myName".equalsIgnoreCase(tokens[0]))
				{
					strId = tokens[1];
					room.broadcast("otherID:" + strId);
					sendMessage("roomNumber:" + room.getRoomNumber());
					
					String query = "SELECT win, lose FROM userinfo WHERE nickName = " + "'" + strId + "'";
					
					ArrayList<String> result = DB.selectScore(query);
				 
					sendMessage("total:Score = "+ result.get(0) + "-" + result.get(1));
					tokens = null;
				}
				else if("giveMyName".equalsIgnoreCase(tokens[0]))
				{
					room.broadcast("giveOnwerName:" + tokens[1]);
				}
				else if("timeOver".equalsIgnoreCase(tokens[0]))
				{
					room.broadcast("timeOver:" + tokens[2]);
				}
				else if("chatText".equalsIgnoreCase(tokens[0]))
				{
					room.broadcast("otherChat:" + tokens[1] + ":" + tokens[2]);
				}
				else if("serverClose".equalsIgnoreCase(tokens[0]))
				{
					System.out.println("Stream disconnected with " + tokens[1]);
					room.exitRoom(this);
				}
				req = "";
			}
		}catch(IOException e) {
			System.out.println(this.strId + "���� ���ӿ��� �������ϴ�.");
		}
	}
	
	
	
	public void sendMessage(String message) {
		try {
			System.out.println("Send to " + strId + " : " + message);
			bw.write(message + "\n");
			bw.flush();

		} catch (Exception e) { }
	}
	
	public String getID()
	{
		return strId;
	}
}
