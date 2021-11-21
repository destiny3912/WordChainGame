package server;

import java.util.ArrayList;
/*
 * game room들을 관리하는 관리 class입니다.
 * 
 * game room은 0번부터 시작하며 
 * */
public class gameRoomManager {
	private ArrayList<gameRoom> roomList = new ArrayList<gameRoom>();//game room list
	private int roomNumber = 0;
	
	//room 추가
	public void addRoom(gameRoom room)
	{
		roomList.add(room);
		roomNumber++;
	}
	
	public void purgeRoom(gameRoom room)
	{
		//roomList.remove(room);
		roomNumber--;
	}
	
	public gameRoom getRoom(int index)
	{
		return roomList.get(index);
	}
	
	public ArrayList<gameRoom> getRoomList()
	{
		return roomList;
	}
	
	public int getRoomNumber(gameRoom room)
	{
		return roomList.indexOf(room);
	}
}