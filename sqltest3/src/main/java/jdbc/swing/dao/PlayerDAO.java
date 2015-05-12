package jdbc.swing.dao;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jdbc.swing.domain.Player;
import jdbc.swing.domain.AuditHistory;

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

			while (rs.next()) {
				Player newPlayer = convertRowToPlayer(rs);
				list.add(newPlayer);
			}
			return list;
		} finally {
			DAOUtils.close(stmt, rs);
		}
	}
	
	public List<AuditHistory> getAuditHistory(int playerId) throws Exception {
		List<AuditHistory> list = new ArrayList<AuditHistory>();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			
			String sql = "SELECT history.user_id, history.player_id, history.action, history.action_date_time, users.first_name, users.last_name  "
					+ "FROM audit_history history, users users "
					+ "WHERE history.user_id=users.id AND history.player_id=" + playerId;
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				int userId = rs.getInt("history.user_id");
				String action = rs.getString("history.action");
				Timestamp timestamp = rs.getTimestamp("history.action_date_time");
				java.util.Date actionDateTime = new java.util.Date(timestamp.getTime());
				
				String userFirstName = rs.getString("users.first_name");
				String userLastName = rs.getString("users.last_name");
				
				AuditHistory temp = new AuditHistory(userId, playerId, action, actionDateTime, userFirstName, userLastName);
				list.add(temp);
				
			}
			return list;
		}finally{
			DAOUtils.close(stmt, rs);
		}
	}

	public void addPlayer(Player thePlayer, int userId) throws Exception {

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("insert into players"
					+ " (nickname, profession, guildname, income)"
					+ " values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, thePlayer.getNickName());
			stmt.setString(2, thePlayer.getProfession());
			stmt.setString(3, thePlayer.getGuildName());
			stmt.setBigDecimal(4, thePlayer.getIncome());

			stmt.executeUpdate();

			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				thePlayer.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Error generating key for player");
			}

			stmt.close();
			stmt = conn.prepareStatement("insert into audit_history"
					+ " (user_id, player_id, action, action_date_time)"
					+ " values (?, ?, ?, ?)");

			stmt.setInt(1, userId);
			stmt.setInt(2, thePlayer.getId());
			stmt.setString(3, "Added a new player");
			stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
		
			stmt.executeUpdate();
			
		} finally {
			DAOUtils.close(stmt);
		}
	}

	public void updatePlayer(Player thePlayer, int userId) throws SQLException {

		PreparedStatement stmt = null;

		try {
			stmt = conn
					.prepareStatement("update players"
							+ " set nickname = ?, profession = ?, guildname = ?, income = ?"
							+ " where id = ?");

			stmt.setString(1, thePlayer.getNickName());
			stmt.setString(2, thePlayer.getProfession());
			stmt.setString(3, thePlayer.getGuildName());
			stmt.setBigDecimal(4, thePlayer.getIncome());
			stmt.setInt(5, thePlayer.getId());

			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement("insert into audit_history"
					+ " (user_id, player_id, action, action_date_time)"
					+ " values (?, ?, ?, ?)");

			stmt.setInt(1, userId);
			stmt.setInt(2, thePlayer.getId());
			stmt.setString(3, "Updated player.");
			stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

			stmt.executeUpdate();

		} finally {
			DAOUtils.close(stmt);
		}
	}

	public void deletePlayer(Player thePlayer, int userId) throws SQLException {
		PreparedStatement stmt = null;

		try {
			
			stmt = conn.prepareStatement("insert into audit_history"
					+ " (user_id, player_id, action, action_date_time)"
					+ " values (?, ?, ?, ?)");

			stmt.setInt(1, userId);
			stmt.setInt(2, thePlayer.getId());
			stmt.setString(3, "Deleted player.");
			stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

			stmt.executeUpdate();
			stmt.close();
			
			stmt = conn.prepareStatement("delete from players where id = ?");
			stmt.setInt(1, thePlayer.getId());
			stmt.executeUpdate();
			
		} finally {
			DAOUtils.close(stmt);
		}
	}

	public List<Player> searchPlayers(String guildName) throws Exception {
		List<Player> list = new ArrayList<Player>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			guildName += "%";
			pstmt = conn
					.prepareStatement("select * from players where guildname like ?");
			pstmt.setString(1, guildName);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Player newPlayer = convertRowToPlayer(rs);
				list.add(newPlayer);
			}
			return list;
		} finally {
			DAOUtils.close(pstmt, rs);
		}
	}

	private Player convertRowToPlayer(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String nickName = rs.getString("nickname");
		String profession = rs.getString("profession");
		String guildName = rs.getString("guildname");
		BigDecimal income = rs.getBigDecimal("income");

		Player newPlayer = new Player(id, nickName, profession, guildName,
				income);
		return newPlayer;
	}

}
