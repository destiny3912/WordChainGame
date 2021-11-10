package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class gameServer extends Thread 
{

	Socket socket = null;
	ServerSocket serverSocket = null;
	gameRoomManager roomList;
	gameThreadServer server = null;
	int portNumber = 3100; // port ��ȣ
	int clientNumber = -1;
	int roomNumber = -1;

	public gameServer(gameRoomManager roomList) 
	{
		this.roomList = roomList;
	}

	public void run()
	{
		try {
			// portNumber �� ������� �ϴ� ���� ����
			serverSocket = new ServerSocket(portNumber);
			System.out.println("[Game Server �غ� �Ϸ�]");

			// Server ����
			while (true) 
			{

				// Client�� �����û ���, ����Ǹ� Client Socket �� �������
				socket = serverSocket.accept();
				System.out.println("[Game Server ����]" + socket.getInetAddress());
				
				clientNumber++;
				
				// ������� �濡 ���� Ŭ���̾�Ʈ�� ��û�ϰ��
				if(clientNumber % 2 != 0)
				{
					server = new gameThreadServer(socket, roomList.getRoom(++roomNumber));
					roomList.getRoom(roomNumber).enterRoom(server);
					server.start();
				}
				//���� ����� ���� Ŭ���̾�Ʈ�� ��û�ϰ��
				else
				{
					gameRoom room = new gameRoom();
					roomList.addRoom(room);
					server = new gameThreadServer(socket, room);
					room.enterRoom(server);
					server.start();
				}
			}
		}catch (final IOException e) {
			e.printStackTrace();
		}
	}
}