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
	int portNumber = 3100; // port 번호
	int clientNumber = -1;
	int roomNumber = -1;

	public gameServer(gameRoomManager roomList) 
	{
		this.roomList = roomList;
	}

	public void run()
	{
		try {
			// portNumber 를 기반으로 하는 소켓 생성
			serverSocket = new ServerSocket(portNumber);
			System.out.println("[Game Server 준비 완료]");

			// Server 유지
			while (true) 
			{

				// Client의 연결요청 대기, 연결되면 Client Socket 이 만들어짐
				socket = serverSocket.accept();
				System.out.println("[Game Server 연결]" + socket.getInetAddress());
				
				clientNumber++;
				
				// 만들어진 방에 들어가는 클라이언트의 요청일경우
				if(clientNumber % 2 != 0)
				{
					server = new gameThreadServer(socket, roomList.getRoom(++roomNumber));
					roomList.getRoom(roomNumber).enterRoom(server);
					server.start();
				}
				//방을 만들어 들어가는 클라이언트의 요청일경우
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