package client;
import java.util.ArrayList;

import server.Server;

public class ChatRoom {
	ArrayList<Server> list = new ArrayList<Server>();
	
	public String display() {
		String msg = "USR";
		System.out.println("[현재 접속자 정보] 접속자 수 : " + list.size());
		
		if (list.size() != 0) {
			System.out.println("[접속한 Client ID]");
			for (int i = 0; i < list.size(); i++) {
				String userId = list.get(i).strId;
				System.out.println(userId);
				msg = msg + " " + userId;
			}
			System.out.println("**********************************");
		}
		System.out.println(msg);
		return msg;
	}

	// Client 를 ArrayList 에 추가
	public void enterRoom(Server server) {
		list.add(server);
	}

	// Client 에게 메시지 전송
	public void broadCasting(String message) {
		Server server = null;

		for (int i = 0; i < list.size(); i++) {
			server = list.get(i);
			server.sendMessage(message);
		}
	}

	// Client 가 채팅에서 나갈 때
	public void exitRoom(Server server) {
		boolean isDelete = list.remove(server);
		if (isDelete) {
			System.out.println(server.strId + " Client 제거");
		} else {
			System.out.println(server.strId + " Client 제거 실패");
		}
	}
}