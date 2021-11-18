package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class login extends CFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame mainWindow = new JFrame();
	
	public login(Socket socket, BufferedWriter bw, BufferedReader br) {
		// TODO Auto-generated constructor stub
		super.bw = bw;
		super.br = br;
		super.socket = socket;
	}
	
	public login()
	{
		
	}

	public void setWindow()
	{
		mainWindow.setVisible(true);
		mainWindow.setSize(500, 400);
		mainWindow.setTitle("Login");
		mainWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mainWindow.setLayout(new GridLayout(3,1));
		mainWindow.setResizable(false);
		
		//Header
		JPanel DescriptionPane = new JPanel();
		DescriptionPane.setSize(500, 200);
		JPanel textFeildPane = new JPanel();
		
		textFeildPane.setLayout(new GridLayout(2,2));
		textFeildPane.setSize(500, 100);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.setSize(500, 100);
		
		mainWindow.add(DescriptionPane);
		mainWindow.add(textFeildPane);
		mainWindow.add(buttonPane);
		
		JLabel DesText = new JLabel("Word chain game");
		JLabel IDText = new JLabel("ID : ");
		JLabel PWText = new JLabel("PW : ");
		DesText.setHorizontalAlignment(JLabel.CENTER);
		
		DescriptionPane.add(DesText);
		
		//Center -> input id and pw textField
		JPanel IDPane = new JPanel();
		JPanel PWPane = new JPanel();
		IDPane.setSize(250,10);
		PWPane.setSize(250,10);
		
		IDPane.setLayout(new FlowLayout());
		PWPane.setLayout(new FlowLayout());
		
		JTextField ID = new JTextField("ID");
		JTextField PW = new JTextField("PW");
		ID.setColumns(12);
		PW.setColumns(12);
		IDPane.add(IDText);
		IDPane.add(ID);
		PWPane.add(PWText);
		PWPane.add(PW);
		
		textFeildPane.add(IDPane);
		textFeildPane.add(PWPane);
		
		ID.revalidate();
		ID.repaint();
		PW.revalidate();
		PW.repaint();
		
		//Footer
		JButton Register = new JButton("Registrer");
		buttonPane.add(Register);
		Register.revalidate();
		Register.repaint();
		
		JButton Login = new JButton("Login");
		buttonPane.add(Login);
		Login.revalidate();
		Login.repaint();
		
		Register.addActionListener(new CActionListener(super.socket, super.bw, super.br) {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				register registerSection = new register(super.socket, super.bw, super.br);
				
				registerSection.setWindow();
				mainWindow.dispose();
			}
		});

		Login.addActionListener(new CActionListener(super.socket) {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String result = "";
				String id = "";
				String q;

					
				q = "LOG " + ID.getText().replaceAll(" ", "") + " ";	
				
		        SHA256 sha256 = new SHA256();
				try {
					q += sha256.encrypt(PW.getText().replaceAll(" ", ""));
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					bw.write(q + "\n");
					bw.flush();
					result = br.readLine();
					if(result.equals("FAL"))	{
						JOptionPane.showMessageDialog(null, "ID와 비밀번호를 확인 후 다시 시도해 주세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE); 
					}
					else if (result.substring(0, 3).equals("WEL")) {
						
						String resultTokens[] = result.split(" ");
						id = new String(resultTokens[1]);
						waiting waitingSection = new waiting(socket, super.bw, super.br, id);	// 여기서 ID는 닉네임입니다.
						waitingSection.setWindow();
						mainWindow.dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "알 수 없는 오류.", "로그인 실패", JOptionPane.ERROR_MESSAGE); 
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
