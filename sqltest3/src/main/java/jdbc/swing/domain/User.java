package jdbc.swing.domain;

public class User {

	private int id;
	private String lastName;
	private String firstName;
	private String email;
	private boolean isAdmin;
	private String password;
	
	public User(){
		
	}

	public User(String lastName, String firstName, String email, boolean isAdmin, String password) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.isAdmin = isAdmin;
		this.password = password;
	
	}
	
	public User(int id, String lastName, String firstName, String email, boolean isAdmin) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.isAdmin = isAdmin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
