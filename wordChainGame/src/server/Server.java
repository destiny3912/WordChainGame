package server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {
	private Socket socket;
	private BufferedReader br = null;
	private BufferedWriter bw = null;

	public String strId = "";
	boolean isLogin;
	ChatRoom chatRoom;
	gameRoom gameRoom;
	ArrayList <gameRoom> roomList = new ArrayList<gameRoom>();
	int numberOfRoom = -1;
	
	public Server(Socket tmpSocket, ChatRoom tmpChatRoom) {
		socket = tmpSocket;
		chatRoom = tmpChatRoom;
		
		// 1. ��/��� Stream ����
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) { }
	}

	public void run() {
		String message = "";

		// 1. �α��� ó��(Client ���̵� �޾ƿ���)
		login();
		try {
			while (!message.equals("bye")) {


				// 4. Client �� ���� �޽��� ���� �� Server ���� ���
				message = br.readLine();
				String tokens[] = message.split(" ");
				// ���� �޽������
				System.out.println("���� �޽��� ==> " + strId + " : " + message);

				// bye �Է� �� ���� ����
				if (message.equals("bye")) {
					chatRoom.broadCasting("DEU " + strId);
				}
				//game room�� ������ִ� ��û
				if(message.equals("createGameRoom"))
				{
					gameRoom = new gameRoom();
					roomList.add(gameRoom);
					numberOfRoom++;
					
					gameThreadServer server = new gameThreadServer(socket,gameRoom, strId);
					
					sendMessage("roomNumber " + numberOfRoom);
					//sendMessage("OWN");
					server.start();
					
					chatRoom.exitRoom(this);
					try {
						br.close();
						bw.close();
					}catch(Exception e) { }
					
					gameRoom.enterRoom(server);
					continue;
				}
				//������� �ִ� game room�� ���� ��û
				if(tokens[0].equals("enterRoom"))
				{
					
					int roomNumber = Integer.parseInt(tokens[1]);
					
					gameThreadServer server = new gameThreadServer(socket, roomList.get(roomNumber), strId);
					server.start();
					
					chatRoom.exitRoom(this);
					try {
						br.close();
						bw.close();
					}catch(Exception e) { }
					
					roomList.get(roomNumber).enterRoom(server);
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
			} catch (Exception e) { }
			System.out.println("MultiServerThread2 ����");
		}
	}

	// �α��� (Client ���� Server �� ID �����ؼ� ����)
	public void login() {

		try {
			System.out.println("Client ID �޾ƿ��� ��....");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Client �� �Է��� ID �޾ƿ���
			strId = br.readLine(); 

			// 3. ������ �� �����ֱ�
			String userlistStr = chatRoom.display();
			sendMessage("ok");
			chatRoom.broadCasting(userlistStr);
		} catch (IOException e) { }
	}

	// �޼��� ����
	public void sendMessage(String message) {
		try {
			bw.write(message + "\n");
			bw.flush();

		} catch (Exception e) { }
	}
}
