package server;

import java.util.ArrayList;
/*
 * game room���� �����ϴ� ���� class�Դϴ�.
 * 
 * game room�� 0������ �����ϸ� 
 * */
public class gameRoomManager {
	private ArrayList<gameRoom> roomList = new ArrayList<gameRoom>();//game room list
	private int roomNumber = 0;
	
	//room �߰�
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
