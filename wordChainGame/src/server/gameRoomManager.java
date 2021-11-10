package server;

import java.util.ArrayList;

public class gameRoomManager {
	private ArrayList<gameRoom> roomList = new ArrayList<gameRoom>();
	private int roomNumber = 0;
	
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
