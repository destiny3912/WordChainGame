package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class gameRoom {
	ArrayList<gameThreadServer> list = new ArrayList<gameThreadServer>();
	public int roomNumber;
	
	public void enterRoom(gameThreadServer server)
	{
		list.add(server);
	}
	
	public void broadcast(String message) {
		gameThreadServer server = null;

		for (int i = 0; i < list.size(); i++) {
			server = list.get(i);
			server.sendMessage(message);
		}
	}
	
	public void exitRoom(gameThreadServer server) {
		boolean isDelete = list.remove(server);
		if (isDelete) {
			System.out.println(server.getID() + " Client 제거");
		} else {
			System.out.println(server.getID() + " Client 제거 실패");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
