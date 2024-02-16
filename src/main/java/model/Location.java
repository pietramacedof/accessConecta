package model;

import java.sql.SQLException; 
import java.util.List; 

import model.dao.LocationDAO;

public class Location {
	private String id;
	private String publicPlace;
	private String neighborhood;
	private String city;
	private double acessibilityNote;
	private Integer quantityOfEvaluation;
	private String uf;
	private String placeName;
	private String cep;
	private String number;
	
	public List<Location> consultLocationsByOwner(int ownerId) {
        LocationDAO dao = new LocationDAO();
        return dao.consultLocations(ownerId);
    }

    public List<Location> consultAllLocations() {
        LocationDAO dao = new LocationDAO();
        // Supondo que o método consultAllLocations na DAO retorne uma lista de todas as locations
        return dao.consultAllLocations();
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
		if(dao.existLocationAlter(location.getId(), location.getCep(), location.getPublicPlace(), location.getNumber(), location.getCity(), location.getUf())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public double getLocationTotalRating(String id) {
		LocationDAO dao = new LocationDAO();
		double total = dao.getLocationTotalRatingById(id);
		return total;
	}
	
	public Location evaluateLocation(String id, double note, User u) throws SQLException {
		double convertedNote = (5*note)/16;
		System.out.println(convertedNote);
		LocationDAO dao = new LocationDAO();
		Location l = new Location();
		l = dao.consultLocationById(id);
		dao.insertEvaluation(u.getId(), l.getId(), convertedNote);
		double total = l.getLocationTotalRating(l.getId());
		l.setQuantityOfEvaluation(l.getQuantityOfEvaluation() + 1);
		double x = (convertedNote + total)/l.getQuantityOfEvaluation();
		l.setAcessibilityNote(x);
		dao.updateNoteById(l, total, convertedNote);
		return l;
	}
	
	public boolean checkerNote(double note) {
		if(note <= 16) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public Location() {
		
	}
	
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

	public Location(String id, String publicPlace, String neighborhood, String city, double acessibilityNote, Integer quantityOfEvaluation, String uf, String placeName,
			String cep, String number) {
		this.id = id;
		this.publicPlace = publicPlace;
		this.neighborhood = neighborhood;
		this.city = city;
		this.acessibilityNote = acessibilityNote;
		this.quantityOfEvaluation = quantityOfEvaluation;
		this.uf = uf;
		this.placeName = placeName;
		this.cep = cep;
		this.number = number;
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
	

	public double getAcessibilityNote() {
		return acessibilityNote;
	}

	public void setAcessibilityNote(double acessibilityNote) {
		this.acessibilityNote = acessibilityNote;
	}

	public Integer getQuantityOfEvaluation() {
		return quantityOfEvaluation;
	}

	public void setQuantityOfEvaluation(Integer quantityOfEvaluation) {
		this.quantityOfEvaluation = quantityOfEvaluation;
	}
	
	
}
