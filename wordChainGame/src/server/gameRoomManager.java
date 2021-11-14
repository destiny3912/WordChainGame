package server;

import java.util.ArrayList;
/*
 * game room들을 관리하는 관리 class입니다.
 * */
public class gameRoomManager {
	private ArrayList<gameRoom> roomList = new ArrayList<gameRoom>();
	private int roomNumber = 0;
	
	//room 추가
	public void addRoom(gameRoom room)
	{
		roomList.add(room);
		roomNumber++;
	}
	
	public gameRoom getRoom(int index)
	{
		return roomList.get(index);
	}
	
	public ArrayList<gameRoom> getRoomList()
	{
		return roomList;
	}
	
	public int getRoomNumber()
	{
		return roomNumber;
	}
}
