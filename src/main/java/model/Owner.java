package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Owner extends User {
	private Date dateOfBirth;
	private List<Location> locations = new ArrayList<>();

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
	
	public Owner findOwnerByToken (String token) {
		UserDAO dao = new UserDAO();
		return dao.findOwnerByToken(token);
	}
	
	
	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

}
