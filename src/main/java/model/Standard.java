package model;

import java.util.ArrayList;
import java.util.List;
import model.dao.StandardDAO;

public class Standard {
	private String id;
	private String name;
	private List<Questions> questions;
	
	public Standard(String name, List<Questions> questions) {
		super();
		this.name = name;
		this.questions = questions;
	}
	
	public Standard(String name, String id) {
		this.name= name;
		this.id = id;
		this.questions = new ArrayList<>();
	}
	
	public Standard() {
		
	}
	
	public List<Standard> consultStandard() {
		StandardDAO dao = new StandardDAO();
		return dao.getStandards();
	}
	
	public List<Questions> getQuestionsByStandard(Standard standard) {
	    List<Questions> questions = new ArrayList<>();
	    // Chame o método da DAO para obter as questões
	    StandardDAO StandardDAO = new StandardDAO();
	    questions = StandardDAO.getQuestionsByStandard(standard.getId());
	    return questions;
	}
	
	public void addQuestions(List<Questions> questions) {
	    this.questions.addAll(questions);
	}
	
	 @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Standard Name: ").append(name).append("\n");
	        sb.append("Questions:\n");
	        for (Questions question : questions) {
	            sb.append("\t").append(question).append("\n");
	        }
	        return sb.toString();
	    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Questions> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Questions> questions) {
		this.questions = questions;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
