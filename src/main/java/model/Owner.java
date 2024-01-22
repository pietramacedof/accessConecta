package model;

import java.util.Date;

public class Owner extends User {
	private Date dateOfBirth;

	
	public Owner() {

	}

	public Owner(String firstName, String lastName, String email, String password, Date dateOfBirth) {
		super(firstName, lastName, email, password);
		this.dateOfBirth = dateOfBirth;
	}
	
	public Owner(String firstName, String lastName, String email, String password, Date dateOfBirth, Integer id) {
		super(firstName, lastName, email, password, id);
		this.dateOfBirth = dateOfBirth;
	}
	

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}
