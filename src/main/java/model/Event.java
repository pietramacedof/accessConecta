package model;

import java.util.Date;

import model.dao.LocationDAO;

public class Event extends Location{
	private Date startDate;
	private Date endDate;
	private String eventPrice;
	public Date getStartDate() {
		return startDate;
	}
	
	public Event( String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number, Date startDate, Date endDate, String eventPrice) {
		super( publicPlace, neighborhood, city, uf, placeName, cep, number);
		this.startDate = startDate;
		this.endDate = endDate;
		this.eventPrice = eventPrice;
	}
	
	public Event(String id, String publicPlace, String neighborhood, String city, double acessibilityNote, String uf, String placeName,
			String cep, String number, Date startDate, Date endDate, String eventPrice) {
		super(id, publicPlace, neighborhood, city, acessibilityNote, uf, placeName, cep, number);
		this.startDate = startDate;
		this.endDate = endDate;
		this.eventPrice = eventPrice;
	}
	
	public Event(String id, String publicPlace, String neighborhood, String city, String uf, String placeName,
			String cep, String number, Date startDate, Date endDate, String eventPrice) {
		super(id, publicPlace, neighborhood, city, uf, placeName, cep, number);
		this.startDate = startDate;
		this.endDate = endDate;
		this.eventPrice = eventPrice;
	}
	
	public boolean updateLocation (Event e) {
		LocationDAO dao = new LocationDAO();
		if(dao.updateEvent(e)) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getEventPrice() {
		return eventPrice;
	}
	public void setEventPrice(String eventPrice) {
		this.eventPrice = eventPrice;
	}
}
