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
	private Connection con = null;
	private String userName = "root";
	private String password = "Destiny3910!";
	private String address = "jdbc:mysql://localhost:3306/nwproject?useSSL=false";
	
	public gameThreadServer(Socket socket, gameRoom room, String strId)
	{
		this.socket = socket;
		this.strId = strId;
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) { }
	}
	
	@Override
	public void run() {
		
		try {
			while(true)
			{
				String req;
				//String res;
				
				req = br.readLine();
				
				if(req == null)
				{
					System.out.println("Ŭ���̾�Ʈ�� ������ ������ϴ�");
					break;
				}
				
				String[] tokens = req.split(":");
				
				//���� ������
				if("turnOver".equalsIgnoreCase(tokens[0]))
				{
					//broadcast("isYourTurn");
					//��� Ŭ���̾�Ʈ���� isYourTurn response
					
				}
				//�ð��ʰ�
				else if("end".equalsIgnoreCase(tokens[0]))
				{
					//broadcast("end");
					//quitGame(out);
					//��� Ŭ���̾�Ʈ���� ���� ���� end response
				}
				else if("answer".equalsIgnoreCase(tokens[0]))
				{
					String prev, ans;
					prev = tokens[2];
					ans = tokens[1];
					
					if(prev.charAt(prev.length() - 1) == ans.charAt(0))
					{
						bw.write("correct:" + ans + "\n");
						bw.flush();
					}
					else
					{
						bw.write("wrong\r\n");
						bw.flush();
					}
					//1���� 2�� ��ū���� �����ձ� �����ϸ� correct response
					//Ʋ�ȴٸ� end response
				}
				/*else if("join".equalsIgnoreCase(tokens[0]))
				{
					joinGame(tokens[1], out);
				}*/
				else if("iWin".equalsIgnoreCase(tokens[0]))
				{
					String query;
					query = "UPDATE userinfo SET win = win + 1 WHERE ID = tokens[1] ";
					insertDB(query);
					//DB�� tokens[1]�� �̸������ϴ� �÷��̾��� �¸� ī��Ʈ + 1
				}
				else if("iLose".equalsIgnoreCase(tokens[0]))
				{
					//broadcast("end");
					String query;
					query = "UPDATE userinfo SET lose = lose + 1 WHERE ID = tokens[1] ";
					insertDB(query);
					//DB�� tokens[1]�� �̸������ϴ� �÷��̾��� �й� ī��Ʈ + 1
				}
			}
		}catch(IOException e) {
			System.out.println(this.strId + "���� ���ӿ��� �������ϴ�.");
		}
	}
	
	/*private void quitGame(PrintWriter writer)
	{
		synchronized (writerList){
			writerList.clear();
		}
		
	}
	
	private void joinGame(String nickName, PrintWriter writer)
	{
		this.nickName = nickName;
		
		synchronized (writerList){
			writerList.add(writer);
		}
	}
	
	private void broadcast(String data)
	{
		synchronized (writerList) {
			for(PrintWriter W : writerList)
			{
				W.println(data);
				W.flush();
			}
		}
	}*/
	
	private void insertDB(String query)
	{
		Statement state = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(address, userName, password);
			
			state = con.createStatement();
			state.execute(query);
		}catch(ClassNotFoundException e) {
			
		}catch(SQLException e) {
			
		}finally {
			
			try {
				if(state != null)
					state.close();
			}catch(SQLException e1) {
				
			}
			
			try {
				if(con != null)
					con.close();
			}catch(SQLException e1) {
				
			}
		}
	}
	
	public void sendMessage(String message) {
		try {
			bw.write(message + "\n");
			bw.flush();

		} catch (Exception e) { }
	}
	
	public String getID()
	{
		return strId;
	}
}
