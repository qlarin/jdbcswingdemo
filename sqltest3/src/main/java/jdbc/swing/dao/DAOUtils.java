package jdbc.swing.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOUtils {

	public static void close(Connection conn, Statement stmt, ResultSet rs)
			throws SQLException {

		if (rs != null) {
			rs.close();
		}

		if (stmt != null) {
			
		}
		
		if (conn != null) {
			conn.close();
		}
	}

	public static void close(Statement stmt, ResultSet rs) throws SQLException {
		close(null, stmt, rs);		
	}

	public static void close(Statement stmt) throws SQLException {
		close(null, stmt, null);		
	}
}
