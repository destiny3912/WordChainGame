package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/*
 * �� Ŭ������ �ڵ尡 �ʹ� ����� �������� ���� �ٸ� Ŭ������ DB ��� ������ �޼��带 ���� ���� Ŭ���� �Դϴ�.
 * 
 * ���� ����Ͻ� �κ��� DB ��� �޼��带 �̰��� ��ġ�ŵ� �ǰ� ���ϼŵ� �˴ϴ�.
 * written by ����ȣ
 * */
public class DBServer {
	
	private String userName = "root";
	private String password = "Destiny3910!";
	private String address = "jdbc:mysql://localhost:3306/nwproject?useUnicode=true&characterEncoding=utf8";
	
	public void updateScore(String psql, String name)
	{
		Statement state = null;
		Connection con = null;
		PreparedStatement psmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(address, userName, password);
			
			psmt = con.prepareStatement(psql);
			psmt.setString(1, name);
			
			psmt.executeUpdate();
			
		}catch(ClassNotFoundException e) {
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("DB����ERROR in update");
		}finally {
			try {
				con.close();
					
			}catch(SQLException e1) {
				
			}
		}
	}
	
	public ArrayList<String> selectScore(String query)
	{
		Statement state = null;
		ArrayList<String> resultString = new ArrayList<String>();
		ResultSet rset = null;
		Connection con = null;
		int i = 1;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(address, userName, password);
			System.out.println(con);
			state = con.createStatement();
			
			rset = state.executeQuery(query);
			
			while(rset.next())
			{
				resultString.add(rset.getString("win"));
				resultString.add(rset.getString("lose"));
			}
			
		}catch(ClassNotFoundException e) {
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("DB����ERROR in select");
		}finally {
			
			try {
				rset.close();
				state.close();
				con.close();
				
					
			}catch(SQLException e1) {
				
			}
		}
		System.out.println(resultString);
		return resultString;
	}
}
