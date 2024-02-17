package model;

import java.util.ArrayList;
import java.util.List;

import model.dao.LocationDAO;

public class Evaluation {
	private String id;
	private double note;
	private User user;
	private Location location;
	public String getId() {
		return id;
	}
	
	public List<Evaluation> consultEvaluationByUser(User u){
		LocationDAO d = new LocationDAO();
		List<Evaluation> e = new ArrayList<>();
		e = d.consultEvaluationByUser(u);
		return e;
	}
	
	
	public Evaluation(String id, double note, User user, Location location) {
		this.id = id;
		this.note = note;
		this.user = user;
		this.location = location;
	}
	
	public Evaluation(double note, User user, Location location) {
		this.note = note;
		this.user = user;
		this.location = location;
	}
	
	public Evaluation() {

	}



	public void setId(String id) {
		this.id = id;
	}
	public double getNote() {
		return note;
	}
	public void setNote(double note) {
		this.note = note;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
