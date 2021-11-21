package server;

import java.util.ArrayList;

/*
 * game room class입니다. 각 room의 정원은 2명이
 * */
public class gameRoom extends gameRoomManager{
	ArrayList<gameThreadServer> list = new ArrayList<gameThreadServer>();
	gameRoomManager manager;
	int userNumber = 0;
	public int roomNumber = -1;
	
	public gameRoom(gameRoomManager manager)
	{
		this.manager = manager;
	}
	//user가 직접적으로 소통하는 gameThreadServer를 room에 추가
	public void enterRoom(gameThreadServer server)
	{
		list.add(server);
		userNumber++;
		roomNumber = manager.getRoomNumber(this);
		
		if(userNumber % 2 == 0)//방에 방장이 아닌 플레이어가 들어간 경우
		{
			gameThreadServer tempServer = list.get(0);
			tempServer.sendMessage("otherCame");
			tempServer = list.get(1);
			tempServer.sendMessage("enteredAndWait");
		}
	}
	
	public int getRoomNumber() 
	{
		return roomNumber;
	}
	
	public void broadcast(String message) {
		gameThreadServer server = null;
		
		if(list.size() != 2)
			return;
		
		System.out.println("Broadcast : " + message);
		
		for (int i = 0; i < userNumber; i++) {
			server = list.get(i);
			server.sendMessage(message);
			
		}
	}
	
	public void exitRoom(gameThreadServer server) {
		boolean isDelete = list.remove(server);
		userNumber--;
		
		if (isDelete) {
			System.out.println(server.getID() + " Client 제거");
		} else {
			System.out.println(server.getID() + " Client 제거 실패");
		}
		
		if(userNumber < 1)
		{
			manager.purgeRoom(this);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}