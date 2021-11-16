package server;

import java.util.ArrayList;

/*
 * game room class�Դϴ�. �� room�� ������ 2����
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
	//user�� ���������� �����ϴ� gameThreadServer�� room�� �߰�
	public void enterRoom(gameThreadServer server)
	{
		list.add(server);
		userNumber++;
		roomNumber = manager.getRoomNumber(this);
		
		if(userNumber % 2 == 0)//�濡 ������ �ƴ� �÷��̾ �� ���
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
			System.out.println(server.getID() + " Client ����");
		} else {
			System.out.println(server.getID() + " Client ���� ����");
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
