package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class serverMain {
	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket serverSocket = null;
		ChatRoom room = null;
		Server server = null;
		gameServer GServer = null;
		int portNumber = 3000; // port ��ȣ
		
		gameRoomManager roomList = new gameRoomManager();
		
		try {

			// 1. MultiChatRoom ��ä ����
			room = new ChatRoom();

			// 2. portNumber �� ������� �ϴ� ���� ����
			serverSocket = new ServerSocket(portNumber);
			System.out.println("[Server �غ� �Ϸ�]");
			System.out.println("[Game Server ���]");
			//game server ������ ��������
			GServer = new gameServer(roomList);
			GServer.start();
			
			// Server ����
			while (true) {

				// 3. Client�� �����û ���, ����Ǹ� Client Socket �� �������
				socket = serverSocket.accept();
				System.out.println("[����]" + socket.getInetAddress());

				// 4. ���ӵ� Client �� ArrayList�� ����
				// ä�� ��ü ����t
				server = new Server(socket, room, roomList);
				// Thread �۵����� 1)�α��� ó�� 2)ä�� ����
				server.start();
				// ä�� ��ü�� ArrayList�� ����
				room.enterRoom(server);
				
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}