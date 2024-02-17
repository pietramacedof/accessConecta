package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

	private Integer id;
	private String fName;
	private String lName;
	private String email;
	private String watchword;
	private Integer userType;
	private String token;
	
	public User findUserByToken(String token) {
		UserDAO dao = new UserDAO();
		return dao.findUserByToken(token);
	}

	
	public boolean toAlterPassword (User user, String password) throws SQLException {
		UserDAO dao = new UserDAO();
		if(dao.toAlterPassword(user, password)) {
			return true;
		}
		else {
			return false;
		}
	}

	public User() {

	}

	public User (String firstName, String lastName, String email, String password) {
		this.fName = firstName;
		this.lName = lastName;
		this.email = email;
		this.watchword = password;
	}
	
	public User (String firstName, String lastName, String email, String password, Integer id) {
		this.fName = firstName;
		this.lName = lastName;
		this.email = email;
		this.watchword = password;
		this.id = id;
	}
	
	public User (String firstName, String lastName, String email, String password, Integer id, Integer userType) {
		this.fName = firstName;
		this.lName = lastName;
		this.email = email;
		this.watchword = password;
		this.id = id;
		this.userType = userType;
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
