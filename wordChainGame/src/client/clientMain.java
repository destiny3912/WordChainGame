package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import javax.swing.JFrame;

/*
 * Client의 기능을 실행하는 실질 러너 클래스입니다.
 * 
 * 방을 만드는 클라이언트는 createGame화면에서 createGameRoom 버튼을
 * 만들어진 방에 들어간 클라이언트는 대화상자에 방 번호를 입력하고 enterRoom 버튼을 누르면 됩니다.
 * 테스트시 방 번호는 0번 입니다.
 * */
public class clientMain {
	public static void main(String[] args) {
		System.setProperty("file.encoding","UTF-8");
		try{
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		}
		catch(Exception e){ }

		Socket socket = null;
		String ipNumber = "61.105.41.195";
		int portNumber = 3000;
		
		// User usr = new User(socket, null);
		login loginSection = null;
		
		try {
			socket = new Socket(ipNumber, portNumber);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			loginSection = new login(socket, new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),new BufferedReader(new InputStreamReader(socket.getInputStream())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("인터넷 상태를 확인해 주세요.");
			e.printStackTrace();
		}
		loginSection.setWindow();
	}
}