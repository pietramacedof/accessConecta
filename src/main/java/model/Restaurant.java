package model;

import model.dao.LocationDAO;

public class Restaurant extends Location{
	private String typeOfCuisine;
	private String operatingDays;
	
	
	public Restaurant(String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number, String typeOfCuisine, String operatingDays) {
		super(publicPlace, neighborhood, city, uf, placeName, cep, number);
		this.typeOfCuisine = typeOfCuisine;
		this.operatingDays = operatingDays;
	}
	
	public Restaurant(String id, String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number, String typeOfCuisine, String operatingDays) {
		super(id, publicPlace, neighborhood, city, uf, placeName, cep, number);
		this.typeOfCuisine = typeOfCuisine;
		this.operatingDays = operatingDays;
	}
	
	public boolean updateLocation (Restaurant r) {
		LocationDAO dao = new LocationDAO();
		if(dao.updateRestaurant(r)) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getTypeOfCuisine() {
		return typeOfCuisine;
	}


	public void setTypeOfCuisine(String typeOfCuisine) {
		this.typeOfCuisine = typeOfCuisine;
	}


	public String getOperatingDays() {
		return operatingDays;
	}


	public void setOperatingDays(String operatingDays) {
		this.operatingDays = operatingDays;
	}
	
	
	
	
}
