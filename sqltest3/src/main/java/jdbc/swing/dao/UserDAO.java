package jdbc.swing.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jdbc.swing.domain.User;
import jdbc.swing.util.PasswordUtils;



public class UserDAO {

	private Connection conn;
	
	public UserDAO() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("src/main/resources/testdb.properties"));
		
		String user = props.getProperty("user");
		String pass = props.getProperty("password");
		String url = props.getProperty("url");
		
		conn = DriverManager.getConnection(url, user, pass);
		System.out.println("Sucessfully connected to UserDAO at: " + url);
		
	}
	
	private User convertRowToUser(ResultSet rs) throws SQLException {
		
		int id = rs.getInt("id");
		String lastName = rs.getString("last_name");
		String firstName = rs.getString("first_name");
		String email = rs.getString("email");
		boolean isAdmin = rs.getBoolean("is_admin");
		User newUser = new User(id, lastName, firstName, email, isAdmin);
		return newUser;
	}
	
	public List<User> getUsers(boolean isAdmin, int userId) throws Exception {
		List<User> list = new ArrayList<User>();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			
			String sql = null;
			
			if(isAdmin){
				sql = "select * from users order by last_name";
			}else{
				sql = "select * from users where id =" + userId + " order by last_name";
			}
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				User newUser = convertRowToUser(rs);
				list.add(newUser);
			}
			return list;
		}finally{
			DAOUtils.close(stmt, rs);
		}
	}
	
	public void addUser(User theUser) throws Exception {
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement("insert into users" + " (first_name, last_name, email, is_admin, password)" + " values (?, ?, ?, ?, ?)");
			
			stmt.setString(1, theUser.getFirstName());
			stmt.setString(2, theUser.getLastName());
			stmt.setString(3, theUser.getEmail());
			stmt.setBoolean(4,  theUser.isAdmin());
			
			String enryptedPassword = PasswordUtils.encryptPassword(theUser.getPassword());
			stmt.setString(5, enryptedPassword);
			stmt.executeUpdate();
		}finally{
			DAOUtils.close(stmt);
		}
	}
	
	public void updateUser(User theUser) throws Exception {
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement("update users" + " set first_name= ?, last_name= ?, email= ?, is_admin= ?" + " where id= ?");
			
			stmt.setString(1, theUser.getFirstName());
			stmt.setString(2, theUser.getLastName());
			stmt.setString(3, theUser.getEmail());
			stmt.setBoolean(4, theUser.isAdmin());
			stmt.setInt(5, theUser.getId());
			
			stmt.executeUpdate();
		}finally{
			DAOUtils.close(stmt);
		}
	}
	
	public boolean authenticate(User theUser) throws Exception {
		boolean result = false;
		String plainTextPassword = theUser.getPassword();
		String encryptedPasswordFromDatabase = getEncryptedPassword(theUser.getId());
		result = PasswordUtils.checkPassword(plainTextPassword, encryptedPasswordFromDatabase);
		return result;
	}
	
	private String getEncryptedPassword(int id) throws Exception {
		String encryptedPassword = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select password from users where id=" + id);
			
			if(rs.next()){
				encryptedPassword = rs.getString("password");
			}else{
				throw new Exception("User id not found: " + id);
			}
			return encryptedPassword;
		}finally{
			DAOUtils.close(stmt, rs);
		}
	}
	
	public void changePassword(User user) throws Exception {
		String plainTextPassword = user.getPassword();
		String encryptedPassword = PasswordUtils.encryptPassword(plainTextPassword);
		
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement("update users" + " set password=?" + " where id=?");
			stmt.setString(1, encryptedPassword);
			stmt.setInt(2, user.getId());
			
			stmt.executeUpdate();
		}finally{
			DAOUtils.close(stmt);
		}
	}
}
