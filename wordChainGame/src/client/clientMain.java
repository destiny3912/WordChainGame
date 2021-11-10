package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/*
 * Client의 기능을 실행하는 실질 러너 클래스입니다.
 * 
 * */
public class clientMain {
	public static void main(String[] args) {
		
		Socket socket = null;
		String ipNumber = "127.0.0.1";
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
			e.printStackTrace();
		}
		
		loginSection.setWindow();

		
	}
}
