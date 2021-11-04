package client;
import java.util.ArrayList;

import server.Server;

public class ChatRoom {
	ArrayList<Server> list = new ArrayList<Server>();
	
	public String display() {
		String msg = "USR";
		System.out.println("[���� ������ ����] ������ �� : " + list.size());
		
		if (list.size() != 0) {
			System.out.println("[������ Client ID]");
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

	// Client �� ArrayList �� �߰�
	public void enterRoom(Server server) {
		list.add(server);
	}

	// Client ���� �޽��� ����
	public void broadCasting(String message) {
		Server server = null;

		for (int i = 0; i < list.size(); i++) {
			server = list.get(i);
			server.sendMessage(message);
		}
	}

	// Client �� ä�ÿ��� ���� ��
	public void exitRoom(Server server) {
		boolean isDelete = list.remove(server);
		if (isDelete) {
			System.out.println(server.strId + " Client ����");
		} else {
			System.out.println(server.strId + " Client ���� ����");
		}
	}
}