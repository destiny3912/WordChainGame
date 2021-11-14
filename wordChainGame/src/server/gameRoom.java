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
/*
 * game room class입니다. 각 room의 정원은 2명이며 추후 가능하면 확장할 예정
 * */
public class gameRoom {
	ArrayList<gameThreadServer> list = new ArrayList<gameThreadServer>();
	int userNumber = 0;
	private ArrayList<String> roomUsers = new ArrayList<String>();
	public int roomNumber;
	
	//user가 직접적으로 소통하는 gameThreadServer를 room에 추가
	public void enterRoom(gameThreadServer server)
	{
		list.add(server);
		userNumber++;
		
		if(userNumber % 2 == 0)//방에 방장이 아닌 플레이어가 들어간 경우
		{
			gameThreadServer tempServer = list.get(0);
			tempServer.sendMessage("otherCame");
			tempServer = list.get(1);
			tempServer.sendMessage("enteredAndWait");
		}
	}
	
	public void broadcast(String message) {
		gameThreadServer server = null;
		
		if(list.size() != 2)
			return;
		
		System.out.println("Broadcast : " + message);
		
		for (int i = 0; i < 2; i++) {
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
