package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class Server extends Thread {
	private Socket socket;
	private BufferedReader br = null;
	private BufferedWriter bw = null;

	public String strId = "";
	boolean isLogin;
	ChatRoom chatRoom;
	gameRoomManager roomList;
	int numberOfRoom = -1;

	/* About DB */
	private String userName = "root";
	private String password = "Destiny3910!";
	private String address = "jdbc:mysql://localhost:3306/nwproject?useSSL=false";
	/**/

	public Server(Socket tmpSocket, ChatRoom tmpChatRoom, gameRoomManager roomList) {
	
		socket = tmpSocket;
		chatRoom = tmpChatRoom;
		this.roomList = roomList;
		
		// 1. ��/��� Stream ����
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
		}
	}

	public void run() {
		String message = "";
		Boolean isLogin = false;

		// 1. �α��� ó��(Client ���̵� �޾ƿ���)
		do {
			isLogin = login();
		} while (!isLogin);
		
		try {
			while (!message.equals("bye")) {

				// 4. Client �� ���� �޽��� ���� �� Server ���� ���
				message = br.readLine();
				if(message == null)
				{
					message = "cont";
					continue;
				}
					
				// ���� �޽������
				System.out.println("���� �޽��� ==> " + strId + " : " + message);

				// bye �Է� �� ���� ����
				if (message.equals("bye")) {
					chatRoom.broadCasting("DEU " + strId);
				}
				// game room�� ������ִ� ��û
				if (message.equals("createGameRoom")) {

					//sendMessage("roomNumber " + numberOfRoom);
					chatRoom.exitRoom(this);
					continue;
				}
				// ������� �ִ� game room�� ���� ��û
				if (message.equals("enterRoom")) {	
					chatRoom.exitRoom(this);
					continue;
				}
				// 5. ��� Client ���� �޽��� ����
				chatRoom.broadCasting("MSG " + strId + " : " + message);

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("�޼����� �����Ͽ� �۽��� ���� �߻�....");
		} finally {
			chatRoom.exitRoom(this);
			try {
				br.close();
				bw.close();
				socket.close();
			} catch (Exception e) {
			}
			System.out.println("MultiServerThread2 ����");
		}
	}

	// �α��� (Client ���� Server �� ID �����ؼ� ����)
	public boolean login() {

		try {
			System.out.println("Client ID �޾ƿ��� ��....");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Client �� �Է��� ID �޾ƿ���
			String message = br.readLine(); 
			String tokens[] = message.split(" ");
			if (tokens[0].equals("LOG"))	{
				if(auth(tokens))	{
					sendMessage("WEL");
				} else {
					sendMessage("FAL");
					return false;
				}
			}
			else if (tokens[0].equals("REG")) {
				register(tokens);
				
			}

			// 3. ������ �� �����ֱ�
			String userlistStr = chatRoom.display();
			sendMessage("ok");
			chatRoom.broadCasting(userlistStr);
		} catch (IOException e) { }
		return true;
	}
	
	
	// Auth : Auth User
	public boolean auth(String[] tokens) {
		Boolean result = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		/**/
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(address, userName, password);
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = con.createStatement();
			String sql = "select ID, course_id from userinfo where ID=" + tokens[1] +"and PW=" + tokens[2];
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String name = rs.getString(1);
				if (rs.wasNull()) {
					name = "null";
					result = false;
				}
				else {
					result = tokens[1].equals(name);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	// register : Insert New User to DB
	/* ID, PW, NickName, Name, EMail, SNS ���Դϴ� */
	public void register(String[] tokens) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(address, userName, password);
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			String psql = "insert into userinfo(ID, PW, nickName, name, Email, SNS) values ('?', '?', '?', '?', '?', '?');";
			pstmt = con.prepareStatement(psql);
			for(int i = 1; i <= 6; i++)	{
				pstmt.setString(i, tokens[i]);
			}
			int count = pstmt.executeUpdate();
			System.out.println(count);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	

	// �޼��� ����
	public void sendMessage(String message) {
		try {
			bw.write(message + "\n");
			bw.flush();

		} catch (Exception e) {
		}
	}

}
