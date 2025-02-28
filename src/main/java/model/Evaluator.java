package model;

import model.dao.UserDAO;

public class Evaluator extends User {
	private String typeOfDisability;

	public void createEvaluator(Evaluator e) {
		UserDAO dao = new UserDAO();
		dao.createEvaluator(e);

	}

	public Evaluator findEvaluatorByToken(String token) {
		UserDAO dao = new UserDAO();
		return dao.findEvaluatorByToken(token);
	}
	
	
	public Evaluator consultEvaluator (String username) {
		UserDAO dao = new UserDAO();
		return dao.consultEvaluator(username);
	}

	public Evaluator() {

	}

	public Evaluator(String firstName, String lastName, String email, String password, String typeOfDisability) {
		super(firstName, lastName, email, password);
		this.typeOfDisability = typeOfDisability;
	}
	
	public Evaluator(String firstName, String lastName, String email, String password, String typeOfDisability, Integer id) {
		super(firstName, lastName, email, password, id);
		this.typeOfDisability = typeOfDisability;
	}

	public String getTypeOfDisability() {
		return typeOfDisability;
	}

	public void setTypeOfDisability(String typeOfDisability) {
		this.typeOfDisability = typeOfDisability;
	}

}
