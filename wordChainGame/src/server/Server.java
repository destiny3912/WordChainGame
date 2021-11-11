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
		
		// 1. 입/출력 Stream 생성
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
		}
	}

	public void run() {
		String message = "";
		Boolean isLogin = false;

		// 1. 로그인 처리(Client 아이디 받아오기)
		do {
			isLogin = login();
		} while (!isLogin);
		
		try {
			while (!message.equals("bye")) {

				// 4. Client 가 보낸 메시지 받은 후 Server 에서 출력
				message = br.readLine();
				if(message == null)
				{
					message = "cont";
					continue;
				}
					
				// 받은 메시지출력
				System.out.println("받은 메시지 ==> " + strId + " : " + message);

				// bye 입력 시 서버 나감
				if (message.equals("bye")) {
					chatRoom.broadCasting("DEU " + strId);
				}
				// game room을 만들어주는 요청
				if (message.equals("createGameRoom")) {

					//sendMessage("roomNumber " + numberOfRoom);
					chatRoom.exitRoom(this);
					continue;
				}
				// 만들어져 있는 game room에 들어가는 요청
				if (message.equals("enterRoom")) {	
					chatRoom.exitRoom(this);
					continue;
				}
				// 5. 모든 Client 에게 메시지 전송
				chatRoom.broadCasting("MSG " + strId + " : " + message);

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("메세지를 수신하여 송신중 예외 발생....");
		} finally {
			chatRoom.exitRoom(this);
			try {
				br.close();
				bw.close();
				socket.close();
			} catch (Exception e) {
			}
			System.out.println("MultiServerThread2 종료");
		}
	}

	// 로그인 (Client 에서 Server 로 ID 전송해서 받음)
	public boolean login() {

		try {
			System.out.println("Client ID 받아오는 중....");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Client 가 입력한 ID 받아오기
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

			// 3. 접속자 수 보여주기
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
	/* ID, PW, NickName, Name, EMail, SNS 순입니다 */
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
	

	// 메세지 전송
	public void sendMessage(String message) {
		try {
			bw.write(message + "\n");
			bw.flush();

		} catch (Exception e) {
		}
	}

}
