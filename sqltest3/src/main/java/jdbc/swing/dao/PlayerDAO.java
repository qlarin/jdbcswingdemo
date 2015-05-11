package jdbc.swing.dao;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jdbc.swing.domain.Player;

public class PlayerDAO {

	private Connection conn;
	
	public PlayerDAO() throws Exception {
		
		Properties props = new Properties();
		props.load(new FileInputStream("src/main/resources/testdb.properties"));
		
		String url = props.getProperty("url");
		String user = props.getProperty("user");
		String pass = props.getProperty("password");
		
		conn = DriverManager.getConnection(url, user, pass);
		System.out.println("Succesfully connected to DB: " + url);
	
	}
	
	public List<Player> getAllPlayers() throws Exception {
		List<Player> list = new ArrayList<Player>();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from players");
			
			while(rs.next()){
				Player newPlayer = convertRowToPlayer(rs);
				list.add(newPlayer);
			}
			return list;
		}finally{
			close(stmt, rs);
		}
	}
	
	public List<Player> searchPlayers(String guildName) throws Exception {
		List<Player> list = new ArrayList<Player>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			guildName += "%";
			pstmt = conn.prepareStatement("select * from players where guildname like ?");
			pstmt.setString(1, guildName);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Player newPlayer = convertRowToPlayer(rs);
				list.add(newPlayer);
			}
			return list;
		}finally{
			close(pstmt, rs);
		}
	}
	
	private Player convertRowToPlayer(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String nickName = rs.getString("nickname");
		String profession = rs.getString("profession");
		String guildName = rs.getString("guildname");
		BigDecimal income = rs.getBigDecimal("income");
		
		Player newPlayer = new Player(id, nickName, profession, guildName, income);
		return newPlayer;
	}
	
	private static void close(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
		
		if(rs != null){
			rs.close();
		}
		if(stmt != null){
			stmt.close();
		}
		if(conn != null){
			conn.close();
		}
	}
	
	private void close(Statement stmt, ResultSet rs) throws SQLException {
		close(null, stmt, rs);
	}
	
	public static void main(String[] args) throws Exception {
		
		PlayerDAO dao = new PlayerDAO();
		System.out.println(dao.searchPlayers("HellsArmy"));
		System.out.println(dao.getAllPlayers());
	}
}
