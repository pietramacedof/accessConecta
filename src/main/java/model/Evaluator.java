package model;

public class Evaluator extends User {
	private String typeOfDisability;

	public Evaluator() {

	}

	public Evaluator(String firstName, String lastName, String email, String password,String typeOfDisability) {
		super(firstName, lastName, email, password);
		this.typeOfDisability = typeOfDisability;
	}
	

	public String getTypeOfDisability() {
		return typeOfDisability;
	}

	public void setTypeOfDisability(String typeOfDisability) {
		this.typeOfDisability = typeOfDisability;
	}

}
