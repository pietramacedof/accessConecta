package model;

import model.dao.LocationDAO;

public class Store extends Location{
	private String typeProduct;

	public Store( String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number, String typeProduct) {
		super( publicPlace, neighborhood, city, uf, placeName, cep, number);
		this.typeProduct = typeProduct;
	}

	public Store(String id, String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number, String typeProduct) {
		super(id, publicPlace, neighborhood, city, uf, placeName, cep, number);
		this.typeProduct = typeProduct;
	}
	
	public boolean updateLocation (Store s) {
		LocationDAO dao = new LocationDAO();
		if(dao.updateStore(s)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getTypeProduct() {
		return typeProduct;
	}

	public void setTypeProduct(String typeProduct) {
		this.typeProduct = typeProduct;
	}
	
}
