package model;

public class Location {
	private Integer id;
	private String publicPlace;
	private String neighborhood;
	private String city;
	private String uf;
	private String placeName;
	private String cep;
	private String number;
	
	public Location(String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number) {
		super();
		this.publicPlace = publicPlace;
		this.neighborhood = neighborhood;
		this.city = city;
		this.uf = uf;
		this.placeName = placeName;
		this.cep = cep;
		this.number = number;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
