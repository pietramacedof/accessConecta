package model;

import java.util.List;

public class Questions {
	
	private String id;
	private String question;
	
	
	public Questions(String id, String question) {
		super();
		this.id = id;
		this.question = question;
	}
	
	public Questions () {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	

}
