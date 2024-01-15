package model;


public abstract class User {

	private Integer id;
	private String fName;
	private String lName;
	private String email;
	private String watchword;
	private Integer userType;
	private String token;

	public User() {

	}

	public User (String firstName, String lastName, String email, String password) {
		this.fName = firstName;
		this.lName = lastName;
		this.email = email;
		this.watchword = password;
	}
	
	
	public User (String firstName, String lastName, String email) {
		this.fName = firstName;
		this.lName = lastName;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return fName;
	}

	public void setFirstName(String fName) {
		this.fName = fName;
	}

	public String getLastName() {
		return lName;
	}

	public void setLastName(String lastName) {
		this.lName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return watchword;
	}

	public void setPassword(String password) {
		this.watchword = password;
	}
	
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


}
