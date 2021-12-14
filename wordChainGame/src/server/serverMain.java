package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class serverMain {
	public static void main(String[] args) {
		System.setProperty("file.encoding","UTF-8");
		try{
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		}
		
		catch(Exception e){ }
		Socket socket = null;
		ServerSocket serverSocket = null;
		ChatRoom room = null;
		Server server = null;
		gameServer GServer = null;
		int portNumber = 3000; // port 번호
		
		gameRoomManager roomList = new gameRoomManager();
		
		try {

			// 1. MultiChatRoom 객채 생성
			room = new ChatRoom();

			// 2. portNumber 를 기반으로 하는 소켓 생성
			serverSocket = new ServerSocket(portNumber);
			System.out.println("[Server 준비 완료]");
			System.out.println("[Game Server 대기]");
			//game server 스레드 내보내기
			GServer = new gameServer(roomList);
			GServer.start();
			
			// Server 유지
			while (true) {

				// 3. Client의 연결요청 대기, 연결되면 Client Socket 이 만들어짐
				socket = serverSocket.accept();
				System.out.println("[연결]" + socket.getInetAddress());

				// 4. 접속된 Client 를 ArrayList에 저장
				// 채팅 객체 생성t
				server = new Server(socket, room, roomList);
				// Thread 작동시켜 1)로그인 처리 2)채팅 시작
				server.start();
				// 채팅 객체를 ArrayList에 저장
				room.enterRoom(server);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}