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
	private String address = "jdbc:mysql://localhost:3306/nwproject?useUnicode=true&characterEncoding=utf8";
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
				System.out.println("[받은 메시지] " + strId + " : " + message);

				// bye 입력 시 서버 나감
				if (message.equals("bye")) {
					chatRoom.broadCasting("DEU " + strId);
				}
				// game room을 만들어주는 요청
				if (message.equals("createGameRoom")) {
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
			System.out.println("예외발생");
		} finally {
			chatRoom.exitRoom(this);
			try {
				br.close();
				bw.close();
				socket.close();
				// DEU 필요함
			} catch (Exception e) {
			}
			System.out.println("Server.java 종료 (Client가 연결을 종료했습니다)");
		}
	}

	// 로그인 (Client 에서 Server 로 ID 전송해서 받음)
	public boolean login() {

		try {
			System.out.println("[Get Client ID...]");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Client 가 입력한 ID 받아오기
			String message = br.readLine(); 
			String tokens[] = message.split(" ");
			String result = null;
			if (tokens[0].equals("LOG"))	{
				result = auth(tokens);
				if(result.equals("FAL"))	{
					sendMessage("FAL");
					return false;
				}
			}
			else if (tokens[0].equals("REG")) {
				String id = register(tokens);
				sendMessage("REG " + id);
				return false;	// 회원가입 후 다시 로그인으로 돌아감
			}
			
			String resultTokens[] = result.split(" ");
			// 3. 접속자 수 보여주기
			this.strId = resultTokens[1];
			String userlistStr = chatRoom.display();
			sendMessage("WEL " + resultTokens[1]);
			chatRoom.broadCasting(userlistStr);
		} catch (IOException e) { }
		return true;
	}
	
	
	// Auth : Auth User
	// 로그인 성공시 WEL nickName 반환
	// 실패시 FAL 반환
	public String auth(String[] tokens) {
		String result = "FAL";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		/**/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(address, userName, password);
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = con.createStatement();
			String sql = "select nickName from userinfo where ID='" + tokens[1] + "' and PW='" + tokens[2]+"'";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String nickName = rs.getString(1);
				if (rs.wasNull()) {
					nickName = "null";
					result = "FAL";
				}
				else {
					result = "WEL " + nickName;
					PreparedStatement pstmt = null;
					String[] sets = {"lastTime=now()", "accessNum=accessNum+1"};
					int count = 0;
					for(int i = 0; i < sets.length; i++) {
						String psql = "update userinfo set " + sets[i] + " where id=?";
						pstmt = con.prepareStatement(psql);
						pstmt.setString(1, tokens[1]);
						count += pstmt.executeUpdate();
					}
					if(count != sets.length) {
						System.out.println("ERROR : Error occurs during update lastTime, accessNum.");
					}
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
	// 회원가입 성공시 ID를 반환
	// 그 외 null을 반환
	public String register(String[] tokens) {
		String name = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(address, userName, password);
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("DB계정오류");
			e.printStackTrace();
		}
		
		try {
			String psql = "insert into userinfo(ID, PW, nickName, name, Email, SNS) values (?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(psql);
			for(int i = 1; i <= 6; i++)	{
				pstmt.setString(i, tokens[i]);
			}
			int count = pstmt.executeUpdate();
			if (count == 1)	{
				name = tokens[1];	// 회원가입 성공시 가입 ID를 리턴해주기 위해서
			}
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
		return name;
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
