package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Restaurant;
import model.Store;
import model.UserDAO;
import model.dao.LocationDAO;

import model.Event;
import model.Location;
import model.Owner;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;

/**
 * Servlet implementation class RegisterLocation
 */
@WebServlet("/registerLocation")
public class RegisterLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	LocationDAO dao = new LocationDAO();
	UserDAO udao = new UserDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");

		token = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
		Owner owner = udao.findOwnerByToken(token);
		String locationName = request.getParameter("locationName");
		String postalCode = request.getParameter("postalCode");
		String street = request.getParameter("street");
		String neighborhood = request.getParameter("neighborhood");
		String number = request.getParameter("number");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		System.out.println(state + city + number);
		String establishmentType = request.getParameter("establishmentType");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if (dao.existLocation(postalCode, street, number, city, state)) {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"O endereço informado já está cadastrado.\"}";
			out.println(jsonResponse);
			System.out.println("Local já existe.");
			return;
		}
		System.out.println("Informações do local:" + locationName + ", " + postalCode + ", " + street + ", "
				+ neighborhood + ", " + number + ", " + city + ", " + state + ", " + establishmentType);
		if (establishmentType.equalsIgnoreCase("restaurant")) {
			String cuisineType = request.getParameter("cuisineType");
			String operatingDays = request.getParameter("operatingDays");

			Restaurant restaurant = new Restaurant(street, neighborhood, city, state, locationName, postalCode, number,
					cuisineType, operatingDays);
			System.out.println(restaurant.getCep() + restaurant.getTypeOfCuisine());
			dao.insertRestaurant(restaurant, owner);
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
			out.println(jsonResponse);
		} else if (establishmentType.equalsIgnoreCase("event")) {
			String startDateStr = request.getParameter("startDate");
			String endDateStr = request.getParameter("endDate");
			String eventPrice = request.getParameter("eventPrice");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			try {
				startDate = formatter.parse(startDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date endDate = null;
			try {
				endDate = formatter.parse(endDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Event event = new Event(street, neighborhood, city, state, locationName, postalCode, number, startDate,
					endDate, eventPrice);
			System.out.println(event.getCep() + event.getEventPrice());
			dao.insertEvent(event, owner);
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
			out.println(jsonResponse);

		} else {
			String productType = request.getParameter("productType");

			Store store = new Store(street, neighborhood, city, state, locationName, postalCode, number, productType);

			System.out.println(store.getCep() + store.getTypeProduct());
			dao.insertStore(store, owner);
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
			out.println(jsonResponse);
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String locationId = request.getParameter("id");
		System.out.println(locationId);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		Location location = new Location();
		if (location.deleteLocation(locationId)) {
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
			out.println(jsonResponse);
		} else {
			String jsonResponse = "{\"status\": \"failed\", \"message\": \"Operação mal-sucedida\"}";
			out.println(jsonResponse);
		}
	}
}
