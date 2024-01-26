package model;

import model.dao.LocationDAO;

public class Location {
	private String id;
	private String publicPlace;
	private String neighborhood;
	private String city;
	private String uf;
	private String placeName;
	private String cep;
	private String number;
	
	
	
	public Location(String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number) {
		this.publicPlace = publicPlace;
		this.neighborhood = neighborhood;
		this.city = city;
		this.uf = uf;
		this.placeName = placeName;
		this.cep = cep;
		this.number = number;
	}
	
	public Location() {
		
	}
	
	public Location(String id, String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number) {
		this.id = id;
		this.publicPlace = publicPlace;
		this.neighborhood = neighborhood;
		this.city = city;
		this.uf = uf;
		this.placeName = placeName;
		this.cep = cep;
		this.number = number;
	}
	
	public boolean deleteLocation(String id) {
		LocationDAO dao = new LocationDAO();
		if(dao.deleteLocation(id)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isExist(Location location) {
		LocationDAO dao = new LocationDAO();
		if(dao.existLocation(location.getCep(), location.getPublicPlace(), location.getNumber(), location.getCity(), location.getUf())) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPublicPlace() {
		return publicPlace;
	}

	public void setPublicPlace(String publicPlace) {
		this.publicPlace = publicPlace;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
